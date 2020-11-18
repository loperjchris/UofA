/*
* Author: Mauricio Herrera and Ruben Tequida
* File: phase1a.c
* Date: 09/30/2020
* Course: CSC 452 - Principles of Operating Systems
* Description: this program implements low-level process support,
* including forking new processes, quiting processes, getting process
* status and information, setting states, and dispatching processes.
*/

#include "phase1Int.h"
#include "usloss.h"
#include <stdlib.h>
#include <string.h>
#include <assert.h>
#include <stdio.h>

// Linked list struct to keep track of dead children
typedef struct dead {
    int     childPid;
    struct  dead* next;
    int     exitStatus;
} dead;

// Processes struct, added many variables to keep track of
// process info.
typedef struct PCB {
    int             pid;
    int             cid;                // context's ID
    int             cpuTime;            // process's running time
    char            name[P1_MAXNAME+1]; // process's name
    int             priority;           // process's priority
    P1_State        state;              // state of the PCB
    int             tag;                // process's tag
    int             children[P1_MAXPROC];   // childen PIDs
    int             (*startFunc)(void *); //process the function runs
    void            *startArg;           // args
    int             numChildren;        // number of children
    int             parent;             // parent ID
    int             sid;                // semaphore
    dead*           deadChildren;       // Dead!
} PCB;

// Defining struct for ready queue
typedef struct PCBQueueNode {
    int pid;
    int priority;
    struct PCBQueueNode *next;
    PCB *process;
} PCBQueueNode;

static PCB processTable[P1_MAXPROC];    // the process table
int initial_fork = TRUE;                // flagging initial processes
static int currentPid = -1;             // no running process PID
struct PCBQueueNode *head = NULL;       // defining head of process queue


/*
* Purpose: Adds dead processes to the dead child queue of the 
* appropriate parent.
*/
void enqueueDeadChild(int parentPid, int childPid, int exitStatus) {
    // Creating new node.
    dead* newChild = (struct dead*)malloc(sizeof(struct dead));
    newChild->next = NULL;
    newChild->childPid = childPid;
    newChild->exitStatus = exitStatus;
    // If queue is currently empty
    if (processTable[parentPid].deadChildren == NULL) {
        processTable[parentPid].deadChildren = newChild;
    // Non-empty queue
    } else {
        dead* current = processTable[parentPid].deadChildren;

        while (current->next != NULL) {
            current = current->next;
        }
        current->next = newChild;
    }
}

/*
* Purpose: Removes dead children after getChildStatus has been called.
* Returns the child's pid or -1 if empty
*/
int dequeueDeadChild(int parentPid) {
    processTable[parentPid].numChildren--;
    if (processTable[parentPid].deadChildren == NULL){
        return -1;
    } else {
        dead* deadHead = processTable[parentPid].deadChildren;
        int childPid = deadHead->childPid;
        processTable[parentPid].deadChildren = deadHead->next;
        free(deadHead);
        return childPid;
    }
    
}

// // Adds child pid to array in parents struct
void addChild(int parentPid, int childPid) {
    for (int i = 0; i < P1_MAXPROC; i++) {
        if (processTable[parentPid].children[i] == 0) {
            processTable[parentPid].children[i] = childPid;
            processTable[parentPid].numChildren++;
            break;
        }
    }
}

/*
* Purpose: Adds processes to the ready queue in priority order.
*/
void P1Enqueue(int pid) {
    // If queue if currently empty
    if (head == NULL) {
        head = (struct PCBQueueNode*)malloc(sizeof(struct PCBQueueNode));
        head->pid = pid;
        head->priority = processTable[pid].priority;
        head->next = NULL;
        head->process = &processTable[pid];
    // Non-empty queue
    } else {
        struct PCBQueueNode *new;
        new = (struct PCBQueueNode*)malloc(sizeof(struct PCBQueueNode));
        new->pid = pid;
        new->priority = processTable[pid].priority;
        new->process = &processTable[pid];
        // Newly added process has the highest priority
        if (new->priority < head->priority) {
            new->next = head;
            head = new;
        } else {
            PCBQueueNode *cur = head;
            PCBQueueNode *prev = head;
            // Add process as the last processes with its priority
            while (cur != NULL && (new->priority >= cur->priority)) {
                prev = cur;
                cur = cur->next;
            }
            prev->next = new;
            new->next = cur;
        }
    }
}

/*
* Purpose: Finds the next process to run and removes the process from the
* queue. Adds the old process back to the queue if necessary.
*/
PCB* P1Dequeue(int rotate) {
    // Trying to remove from an empty queue
    if (head == NULL) {
        return NULL;
    }
    PCB *current = &processTable[currentPid];
    int status = current->state;
    PCB *runThisOne;
    PCBQueueNode *temp;
    // If current process has quit don't add it back to queue
    if (status == P1_STATE_QUIT) {
        runThisOne = head->process;
        temp = head->next;
        free(head);
        head = temp;
    } else if (rotate) {
        // Grab next process if rotate is true and the next process
        // has an equal or greater priority
        if (head->priority <= current->priority) {
            runThisOne = head->process;
            temp = head->next;
            free(head);
            head = temp;
            P1Enqueue(current->pid);
        // Current process has the highest priority
        } else {
            runThisOne = current;
        }
    } else {
        // Cases to catch if dispatch is being called the first time
        if (current->pid == head->pid) {
            runThisOne = head->process;
            temp = head->next;
            free(head);
            head = temp;
        // Rotate is false, but there is a process of higher priority
        // in the queue
        } else if (current->priority > head->priority) {
            runThisOne = head->process;
            temp = head->next;
            free(head);
            head = temp;
            P1Enqueue(current->pid);
        // Continue running current process
        } else {
            runThisOne = current;
        }
    }
    return runThisOne;
}

/*
* Purpose: Completely removes a process from the queue.
*/
void P1DeleteFromQueue(int pid) {
    PCBQueueNode* prev = head;
    PCBQueueNode* cur = head;
    // Return if no processes in queue
    if (cur == NULL) {
        return;
    // Removing node if pids match in head
    } else if (cur->pid == pid) {
        head = cur->next;
        free(cur);
    // Iterate through queue to find matching pids
    } else {
        cur = cur->next;
        while (cur != NULL) {
            // Matching pid found
            if (cur->pid == pid) {
                break;
            } else {
                prev = cur;
                cur = cur->next;
            }
        }
        if (cur == NULL) return;
        // Freeing process from queue and from process table
        prev->next = cur->next;
        processTable[cur->pid].state = P1_STATE_FREE;
        int rc = P1ContextFree(processTable[cur->pid].cid);
        if (rc != P1_SUCCESS) {
            printf("Unable to free process %d.\n", pid);
            USLOSS_Halt(1);
        }
        free(cur);
    }
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
* Purpose: Initializes the processtable with any pertinent info.
*/
void P1ProcInit(void)
{
    int previously_enabled = P1DisableInterrupts();
    P1ContextInit();
    for (int i = 0; i < P1_MAXPROC; i++) {
        processTable[i].state = P1_STATE_FREE;
    }
    if (previously_enabled) P1EnableInterrupts();
}

/*
* Purpose: Used to launch new processes that are forked from
* any processes already in the process table.
*/
void launch_phaseB(void* args) {
    assert(processTable[currentPid].startFunc != NULL);
    int rc = processTable[currentPid].startFunc(args);
    P1_Quit(rc);
}

/*
* Purpose: Returns currentPid.
*/
int P1_GetPid(void) 
{
    return currentPid;
}

/*
* Purpose: Creates an new child process from the current running process
* and ensures that the process is create with all the appropriate parameters
* or else returns an error code.
*/
int P1_Fork(char *name, int (*func)(void*), void *arg, int stacksize, int priority, int tag, int *pid ) 
{
    int result = P1_SUCCESS;
    // check for kernel mode
    if (kernel_check());

    // disable interrupts
    int previously_enabled = P1DisableInterrupts();

    // check all parameters
    // Checking for priority values out of bounds
    if (initial_fork && (priority > 6 || priority < 1)) {
        result = P1_INVALID_PRIORITY;
    } 
    else if (!initial_fork && (priority < 1 || priority > 5)) {
        result = P1_INVALID_PRIORITY;
    }

    // Checking for invalid tag
    else if (tag != 0 && tag != 1) result = P1_INVALID_TAG;

    // Checking for invalid stacksize
    else if (stacksize < USLOSS_MIN_STACK) result = P1_INVALID_STACK;

    // Checking for name length out of bounds
    else if (strlen(name) > P1_MAXNAME) result = P1_NAME_TOO_LONG;

    // Checking for NULL names
    else if (name == NULL) result = P1_NAME_IS_NULL;

    else {
        int free_process = 0;
        int i;
        for (i = 0; i <= P1_MAXPROC; i ++) {
            // Checking for duplicate names
            if (strcmp(name, processTable[i].name) == 0 && processTable[i].state != P1_STATE_FREE) {
                if (previously_enabled) P1EnableInterrupts();
                return P1_DUPLICATE_NAME;
            }
            // Checking for free processes
            if (!free_process && (processTable[i].state == P1_STATE_FREE)){
                free_process = 1;
                *pid = i;
            }
        }

        // No free processes were found
        if (!free_process) result = P1_TOO_MANY_PROCESSES;

        else {
            // allocate and initialize PCB
            processTable[*pid].priority = priority;
            strcpy(processTable[*pid].name, name);
            processTable[*pid].tag = tag;
            processTable[*pid].pid = *pid;
            processTable[*pid].startFunc = (void *) (*func);
            processTable[*pid].startArg = arg;
            // Check if first process and set parent to 0
            if (currentPid == -1) {
                processTable[*pid].parent = 0;
            // If not first process add to some processes child queue
            // and set parent pid
            } else {
                addChild(currentPid, *pid);
                processTable[*pid].parent = currentPid;
            }
            // create a context using P1ContextCreate
            int rc = P1ContextCreate((void *)launch_phaseB, arg, stacksize, &processTable[*pid].cid);
            // In the case contextCreate returns TooManyContexts
            if (rc != P1_SUCCESS) {
                if (previously_enabled) P1EnableInterrupts();
                return P1_TOO_MANY_PROCESSES;
            }
            // Setting the state of new process
            rc = P1SetState(*pid, P1_STATE_READY, 0);
            if (rc != P1_SUCCESS) {
                printf("Unable to set process %d to ready\n", *pid);
                USLOSS_Halt(1);
            }
            // Add process to queue
            P1Enqueue(*pid);
            // if this is the first process or this process's priority is higher than the 
            // currently running process call P1Dispatch(FALSE)
            if (initial_fork || (!initial_fork && priority < processTable[currentPid].priority)) {
                initial_fork = FALSE;
                P1Dispatch(FALSE);
            }
        }
    }
    // re-enable interrupts if they were previously enabled
    if (previously_enabled) P1EnableInterrupts();
    return result;
}

/*
* Purpose: Terminates the current process, moves any children it may have to the 
* first process created, and adds the process to the queue of dead children.
*/
void 
P1_Quit(int status) 
{
    if (kernel_check()) {
        // Disable interrupts
        int previously_enabled = P1DisableInterrupts();
        processTable[currentPid].state = P1_STATE_QUIT;
        // Set any child process to be a child of the first process
        for (int i = 0; i < P1_MAXPROC; i++) {
            if (processTable[i].parent == currentPid) processTable[i].parent = 0;
        }
        // Add to dead child queue
        enqueueDeadChild(processTable[currentPid].parent, currentPid, status);

        // Handles if parent is joining, move parent to ready state
        if (processTable[processTable[currentPid].parent].state == P1_STATE_JOINING) {
            P1Enqueue(processTable[currentPid].parent);
            int rc = P1SetState(processTable[currentPid].parent, P1_STATE_READY, 0);
            if (rc != P1_SUCCESS) USLOSS_Halt(1);
        }
        
        // Remove process from ready queue
        P1DeleteFromQueue(currentPid);
        if (previously_enabled) P1EnableInterrupts();
    }
    P1Dispatch(FALSE);
    // should never get here
    assert(0);
}

/*
* Purpose: Returns the pid and status upon exit of the first dead child
* and removes the child from its parent's dead children queue.
*/
int 
P1GetChildStatus(int tag, int *pid, int *status) 
{
    int result = P1_SUCCESS;
    int previously_enabled = P1DisableInterrupts();
    if (kernel_check()) {
        // Checking for invalid tag value
        if (tag != 1 && tag != 0) {
            result = P1_INVALID_TAG;
        // No dead children in queue
        } else if (processTable[currentPid].deadChildren == NULL) {
            result = P1_NO_QUIT;
        } else {
            dead* cur = processTable[currentPid].deadChildren;
            // Finding dead child process with matching tag
            while (cur != NULL && processTable[cur->childPid].tag != tag) {
                cur = cur->next;
            }
            if (cur != NULL) {
                *pid = cur->childPid;
                *status = cur->exitStatus;
                dequeueDeadChild(processTable[currentPid].pid);
                int rc = P1ContextFree(processTable[*pid].cid);
                // Remove process from process table
                memset(&processTable[*pid], -1, sizeof(PCB));
                processTable[*pid].state = P1_STATE_FREE;
                // Clear child from parent's child queue
                for (int i = 0; i < P1_MAXPROC; i++) {
                    if (processTable[processTable[*pid].parent].children[i] == *pid) {
                        processTable[processTable[currentPid].parent].children[i] = 0;
                        break;
                    }
                }
                assert(rc == P1_SUCCESS);
            // Didn't find any matching children
            } else result = P1_NO_CHILDREN;
        }
    }
    if (previously_enabled) P1EnableInterrupts();
    return result;
}

/*
* Purpose: Attempts to set the state of the given process to the given
* state.
*/
int
P1SetState(int pid, P1_State state, int sid) 
{
    int result = P1_SUCCESS;
    if (kernel_check()) {
        int previously_enabled = P1DisableInterrupts();
        // Check for invalid pid
        if (pid < 0 || pid >= P1_MAXPROC) result = P1_INVALID_PID;
        else if (processTable[pid].pid != pid) result = P1_INVALID_PID;
        else if (state == P1_STATE_READY || state == P1_STATE_JOINING || state == P1_STATE_BLOCKED || state == P1_STATE_QUIT) {
            if (state == P1_STATE_READY || state == P1_STATE_QUIT) {
                processTable[pid].state = state;
            // If state passed is blocked set process state to blocked, set it's semaphore
            // and remove it from the ready queue
            } else if (state == P1_STATE_BLOCKED) {
                processTable[pid].state = state;
                processTable[pid].sid = sid;
                P1DeleteFromQueue(pid);
            // IF the state is joining check if process has children
            } else {
                // Set state if no children
                if (processTable[pid].deadChildren == NULL) processTable[pid].state = state;
                // Send error if it has children
                else result = P1_CHILD_QUIT;
            }
        // Invalid state passed
        } else {
            result = P1_INVALID_STATE;
        }
        if (previously_enabled) P1EnableInterrupts();
    }
    return result;
}

/*
* Purpose: Finds the next process in the ready queue to begin running
* and clocks how long each process was running for. Halts if no more processes
* are in the ready queue
*/
void
P1Dispatch(int rotate)
{
    static int start = 0;
    if (kernel_check()) {
        int previously_enabled = P1DisableInterrupts();
        // Timing how long we run the current process
        int now;
        assert(USLOSS_DeviceInput(USLOSS_CLOCK_DEV, 0, &now) == USLOSS_DEV_OK);
        processTable[currentPid].cpuTime += now - start;
        // Set the current process to ready if it hasn't already quit
        if (processTable[currentPid].state != P1_STATE_QUIT) {
            processTable[currentPid].state = P1_STATE_READY;
        }
        PCB *runThisOne = P1Dequeue(rotate);
        // Halt when ready queue is empty
        if (runThisOne == NULL) {
            USLOSS_Console("No runnable processes, halting.\n");
            USLOSS_Halt(0);
        }
        // Set the newly selected process to running
        runThisOne->state = P1_STATE_RUNNING;
        currentPid = runThisOne->pid;
        start = now;
        // Switch to process calling P1ContextSwitch with selected
        // process cid
        int rc = P1ContextSwitch(runThisOne->cid);
        if (rc != P1_SUCCESS) USLOSS_Halt(1);
        if (previously_enabled) P1EnableInterrupts();
    }
}

/*
* Purpose: Assings all fields of info the corresponding information
* of the process with matching pid.
*/
int
P1_GetProcInfo(int pid, P1_ProcInfo *info)
{
    // Check if invalid pid is passed
    if ((pid < 0 || pid >= P1_MAXPROC) || processTable[pid].state == P1_STATE_FREE) 
        return P1_INVALID_PID;
    PCB temp = processTable[pid];
    strcpy(info->name, temp.name);
    info->state = temp.state;
    info->priority = temp.priority;
    info->tag = temp.tag;
    info->cpu = temp.cpuTime;
    info->parent = temp.parent;
    info->numChildren = temp.numChildren;
    info->sid = temp.sid;
    for (int i = 0; i < P1_MAXPROC; i++) {
        if (processTable[pid].children[i] != 0) {
            info->children[i] = processTable[pid].children[i];
        }
    }
    return P1_SUCCESS;
}