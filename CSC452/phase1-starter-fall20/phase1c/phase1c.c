/*
* Author: Mauricio Herrera and Ruben Tequida
* File: phase1c.c
* Date: 10/12/2020
* Course: CSC 452 - Principles of Operating Systems
* Description: this program implements semaphores that
* are used to synchronize processes.
*/

#include <stddef.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>
#include <sys/types.h>
#include "usloss.h"
#include "phase1Int.h"

// Queue to keep track of blocked processes
typedef struct blocked_procs {
    int     pid;
    struct  blocked_procs *next;
} blocked_procs;

// Semaphore structure to create semaphores
typedef struct Sem
{
    char        name[P1_MAXNAME+1]; // semaphore name
    u_int       value;              // semaphore value
    int         open;               // flag to indicate if free
    struct      blocked_procs *head;// queue of blocked processes
} Sem;

// Array of semaphore structures
static Sem sems[P1_MAXSEM];

/*
* Purpose: Adds a new process to a semaphore's blocked
* processes queue.
*/
void sem_enqueue(int pid, Sem *sem) {
    blocked_procs *new = malloc(sizeof(struct blocked_procs));
    new->pid = pid;
    new->next = NULL;
    if (sem->head == NULL) sem->head = new;
    else {
        blocked_procs *cur = sem->head;
        while (cur->next != NULL) cur = cur->next;
        cur->next = new;
    }
}

/*
* Purpose: dequeues a process from a semaphore's queue
* of blocked processes.
*/
int sem_dequeue(Sem *sem) {
    if (sem->head == NULL) return -1;
    blocked_procs *temp = sem->head;
    int pid = sem->head->pid;
    sem->head = sem->head->next;
    free(temp);
    return pid;
}

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
* Purpose: This method initializes the semaphore structs
* in the semaphores array.
*/
void 
P1SemInit(void) 
{
    if (kernel_check());
    P1ProcInit();
    for (int i = 0; i < P1_MAXSEM; i++) {
        //sems[i].name[0] = '\0';
        memset(sems[i].name, 0, P1_MAXNAME);
        sems[i].value = -1;
        sems[i].open = 1;
    }
}

/*
* Purpose: This method creates a given semaphore with the
* parameters passed in.
* Return: integer indicating one of 4 possible results.
*/
int P1_SemCreate(char *name, unsigned int value, int *sid)
{
    if (kernel_check());
    int previously_enabled = P1DisableInterrupts();
    int result = P1_SUCCESS;
    if (name == NULL) result = P1_NAME_IS_NULL;
    else if (strlen(name) > P1_MAXNAME) result = P1_NAME_TOO_LONG;
    else {
        int free = 0;
        for (int i = 0; i < P1_MAXSEM; i++) {
            if (strcmp(sems[i].name, name) == 0) {
                if (previously_enabled) P1EnableInterrupts();
                return P1_DUPLICATE_NAME;
            }
            if (sems[i].open == 1 && !free) {
                free = 1;
                *sid = i;
            }
        }
        if (free) {
            strcpy(sems[*sid].name, name);
            sems[*sid].value = value;
            sems[*sid].open = 0;
        } else {
            result = P1_TOO_MANY_SEMS;
        }
    }
    if (previously_enabled) P1EnableInterrupts();
    return result;
}

/*
* Purpose: This method frees a given semaphore if it has no 
* blocked processes in its queue.
* Return: integer indicating one of 3 possible results.
*/
int P1_SemFree(int sid) 	
{	
    if (kernel_check());	
    int previously_enabled = P1DisableInterrupts();	
    if (sems[sid].head != NULL){	
        if (previously_enabled) P1EnableInterrupts();	
        return P1_BLOCKED_PROCESSES;	
    } else if (sems[sid].open == 1 || (sid < 0 || sid >= P1_MAXSEM)){	
        if (previously_enabled) P1EnableInterrupts();	
        return P1_INVALID_SID;	
    } 	
    sems[sid].value = -1;	
    sems[sid].open = 1;	
    memset(sems[sid].name, 0, P1_MAXNAME);	
    if (previously_enabled) P1EnableInterrupts();
    return P1_SUCCESS;	
}

/*
* Purpose: This method performs a P operation on the 
* indicated semaphore.
* Return: int indicating one of 2 possible results.
*/
int P1_P(int sid) 
{
    // check for kernel mode
    if (kernel_check());
    // disable interrupts
    int previously_enabled = P1DisableInterrupts();
    if (sid < 0 || sid >= P1_MAXSEM || sems[sid].open) {
        if (previously_enabled) P1EnableInterrupts();
        return P1_INVALID_SID;
    }
    // while value == 0
    while(sems[sid].value == 0) {
        int pid = P1_GetPid();
        sem_enqueue(pid, &sems[sid]);
        //      set state to P1_STATE_BLOCKED
        assert(P1SetState(pid, P1_STATE_BLOCKED, sid) == P1_SUCCESS);
        P1Dispatch(FALSE);
    }
    // value--
    sems[sid].value--;
    // re-enable interrupts if they were previously enabled
    if (previously_enabled) P1EnableInterrupts();
    return P1_SUCCESS;
}

/*
* Purpose: This method performs a V operation on the 
* indicated semaphore.
* Return: int indicating one of 2 possible results.
*/
int P1_V(int sid) 
{
    if (kernel_check());
    int previously_enabled = P1DisableInterrupts();
    if (sid < 0 || sid >= P1_MAXSEM || sems[sid].open) {
        if (previously_enabled) P1EnableInterrupts();
        return P1_INVALID_SID;
    }
    sems[sid].value++;
    int pid = sem_dequeue(&sems[sid]);
    if (pid != -1) {
        assert(P1SetState(pid, P1_STATE_READY, -1) == P1_SUCCESS);
        P1Dispatch(FALSE);
    }
    if (previously_enabled) P1EnableInterrupts();
    return P1_SUCCESS;
}

/*
* Purpose: This method returns the name of the indicated semaphore
* in the name variable passed in.
* Return: int indicating one of 3 possible results.
*/
int P1_SemName(int sid, char *name) {
    if (kernel_check());
    if (sid < 0 || sid > P1_MAXSEM || sems[sid].open == 1) return P1_INVALID_SID;
    else if (sems[sid].name == NULL) return P1_NAME_IS_NULL;
    strcpy(name, sems[sid].name);
    return P1_SUCCESS;
}