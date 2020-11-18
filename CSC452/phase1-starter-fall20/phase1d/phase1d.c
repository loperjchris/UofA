/*
* Author: Mauricio Herrera and Ruben Tequida
* File: phase1d.c
* Date: 10/23/2020
* Course: CSC 452 - Principles of Operating Systems
* Description: this program implements interrupts and handles
* the booting of the OS and the sentinel process
*/

#include <stddef.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>
#include <stdio.h>
#include "usloss.h"
#include "phase1.h"
#include "phase1Int.h"

static void DeviceHandler(int type, void *arg);
static void SyscallHandler(int type, void *arg);
static void IllegalInstructionHandler(int type, void *arg);

// Structure to keep track of devices
typedef struct Device {
    int status;
    int semaphore;
    int aborted;
} Device;

static int sentinel(void *arg);
// Keep track of device type units
static int device_type_units[] = {1,1,2,4};
// 2D array to keep track of devices and the units they have
static Device devices[4][USLOSS_MAX_UNITS];
int interrupts_counter = 0;

/*
* This method performs a bitwise and with the first bit in 
* the PSR to determine if the call was made in user or kernel
* mode.
* Returns 1 if kernel mode, halts and returns 0 otherwise.
*/
int kernel_check() {
    if ((USLOSS_PsrGet() & 1) == 1) {
        return 1;
    }
    USLOSS_IllegalInstruction();
    P1_Quit(1024);
    return 0;
}

/*
* Purpose: Initializes the devices array, creates semaphores, and initializes
* interrupt handlers as well as forking the sentinel process.
*/
void 
startup(int argc, char **argv)
{
    int pid;
    P1SemInit();
    // Initialize each device and create a semaphore for each one
    for (int i = 0; i < USLOSS_SYSCALL_INT; i++) { // 5
        for (int j = 0; j < USLOSS_MAX_UNITS; j++) { // 4
            devices[i][j].status = 0;
            devices[i][j].aborted = 0;
            char name[P1_MAXNAME+1];
            memset(name, 0, P1_MAXNAME+1);
            // Giving unique names to semaphores
            snprintf(name, P1_MAXNAME, "sem%ddev%d", j, i);
            assert(P1_SemCreate(name, 0, &devices[i][j].semaphore) == P1_SUCCESS);
        }
    }

    // initialize device data structures
    // put device interrupt handlers into interrupt vector
    USLOSS_IntVec[USLOSS_SYSCALL_INT] = SyscallHandler;
    USLOSS_IntVec[USLOSS_ILLEGAL_INT] = IllegalInstructionHandler;
    USLOSS_IntVec[USLOSS_CLOCK_INT] = DeviceHandler;
    USLOSS_IntVec[USLOSS_ALARM_INT] = DeviceHandler;
    USLOSS_IntVec[USLOSS_DISK_INT] = DeviceHandler;
    USLOSS_IntVec[USLOSS_TERM_INT] = DeviceHandler;
    USLOSS_IntVec[USLOSS_MMU_INT] = DeviceHandler;

    /* create the sentinel process */
    int rc = P1_Fork("sentinel", sentinel, NULL, USLOSS_MIN_STACK, 6 , 0, &pid);
    assert(rc == P1_SUCCESS);
    // should not return
    assert(0);
    return;

} /* End of startup */

/*
* Purpose: Performs a P operation and changes the processes status
* based on whether it was aborted or not.
* Return: Status update of the method.
*/
int 
P1_WaitDevice(int type, int unit, int *status) 
{
    if (kernel_check());
    int previously_enabled = P1DisableInterrupts();
    int result = P1_SUCCESS;
    // Check for valid type
    if (type < 0 || type >= 4) result = P1_INVALID_TYPE;
    // Check for valid unit
    else if (unit < 0 || unit >= device_type_units[type]) result = P1_INVALID_UNIT;
    else {
        // P the operation
        assert(P1_P(devices[type][unit].semaphore) == P1_SUCCESS);
        if (devices[type][unit].aborted) result = P1_WAIT_ABORTED;
        // Set status if not aborted
        else *status = devices[type][unit].status;
    }
    // restore interrupts
    if (previously_enabled) P1EnableInterrupts();
    return result;
}

/*
* Purpose: Wakes up a processes allowing P1_WaitDevice to continue and the process
* to keep running.
* Return: Status update of the method.
*/
int 
P1_WakeupDevice(int type, int unit, int status, int abort) 
{
    if (kernel_check());
    int previously_enabled = P1DisableInterrupts();
    int result = P1_SUCCESS;
    // Check for valid type
    if (type <0 || type >= 4) result = P1_INVALID_TYPE;
    // Check for valid unit
    else if (unit < 0 || unit >= device_type_units[type]) result = P1_INVALID_UNIT;
    else {
        // Save status and V semaphore
        devices[type][unit].status = status;
        assert(P1_V(devices[type][unit].semaphore) == P1_SUCCESS);
        devices[type][unit].aborted = abort;
    }
    // restore interrupts
    if (previously_enabled) P1EnableInterrupts();
    return result;
}

/*
* Purpose: Handles interrupts to device types based on the clock.
*/
static void
DeviceHandler(int type, void *arg) 
{
    int unit = (int) arg;
    int status;
    assert(USLOSS_DeviceInput(type, unit, &status) == USLOSS_DEV_OK);
    // If proper device
    if (type == USLOSS_CLOCK_DEV) {
        interrupts_counter++;
        // Call dispatch on every 4th interrupt
        if (!(interrupts_counter % 4)) {
            P1Dispatch(TRUE);
        // Call Wakeup every 100ms
        } else if (!(interrupts_counter % 5)) {
            assert(P1_WakeupDevice(0, 0, 0, 0) == P1_SUCCESS);
        }
    } else {
        assert(P1_WakeupDevice(type, unit, status, 0) == P1_SUCCESS);
    }
}

/*
* Purpose: Creates the sentinel process and continuously calls P1GetChildStatus.
* Continues running if it has children and quits when all the children have quit.
* Return: Status update of the method.
*/
static int
sentinel (void *notused)
{
    int pid;
    int rc;
    int childId;
    int status;
    int hasChildren = 1;
    /* start the P2_Startup process */
    rc = P1_Fork("P2_Startup", P2_Startup, NULL, 4 * USLOSS_MIN_STACK, 2 , 0, &pid);
    assert(rc == P1_SUCCESS);
    P1EnableInterrupts();
    // Continue running while sentinel has children
    while(hasChildren) {
        hasChildren = 0;
        // Go through all possible children
        for (int i = 0; i < P1_MAXTAG; i++) {
            while(1) {
                int rc = P1GetChildStatus(i, &childId, &status);
                // Child hasn't quit: keep running
                if (rc == P1_NO_QUIT) {
                    hasChildren = 1;
                    break;
                // Child has quit so check next children
                } else if (rc == P1_NO_CHILDREN){
                    break;
                }
            }
        }
        // Wait for interrupt
        if (hasChildren) USLOSS_WaitInt();
    }
    USLOSS_Console("Sentinel quitting.\n");
    P1_Quit(0);
    return 0;
} /* End of sentinel */

/*
* Purpose: Synchronizes children that have quit with their parent and 
* dispatched the next process.
* Return: Status update of the method.
*/
int 
P1_Join(int tag, int *pid, int *status) 
{
    if (kernel_check());
    int previously_enabled = P1DisableInterrupts();
    int result = P1_SUCCESS;
    // Check for valid tag
    if (tag != 0 && tag != 1) result = P1_INVALID_TAG;
    else {
        // Wait for a child to quit
        while(1) {
            int rc = P1GetChildStatus(tag, pid, status);
            // Child has quit
            if (rc == P1_SUCCESS) break;
            // No more children
            else if (rc == P1_NO_CHILDREN) {
                result = P1_NO_CHILDREN;
                break;
            }
            // Get next child and dispatch it
            assert(P1SetState(P1_GetPid(), P1_STATE_JOINING, -1) == P1_SUCCESS);
            P1Dispatch(FALSE);
        }
    }
    if (previously_enabled) P1EnableInterrupts();
    return result;
}

/*
* Purpose: Handler when syscalls have been issued.
*/
static void
SyscallHandler(int type, void *arg) 
{
    USLOSS_Console("System call %d not implemented.\n", (int) arg);
    USLOSS_IllegalInstruction();
}

/*
* Purpose: Handler when illegal instructions have been called.
*/
static void IllegalInstructionHandler(int type, void *arg) {
    USLOSS_IllegalInstruction();
    P1_Quit(1024);
}

void finish(int argc, char **argv) {}
