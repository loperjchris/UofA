/*
* Author: Mauricio Herrera and Ruben Tequida
* File: phase3c.c
* Date: 12/02/2020
* Course: CSC 452 - Principles of Operating Systems
* Description: this phase implements Pagers for 
* virtual addressing and page faulting.
*/

#include <assert.h>
#include <phase1.h>
#include <phase2.h>
#include <usloss.h>
#include <string.h>
#include <libuser.h>

#include "phase3.h"
#include "phase3Int.h"

#define FRAME_UNUSED    -1
#define FRAME_INUSE      1
#define FRAME_ASSIGNED   2
#define FRAME_MAPPED     3



#ifdef DEBUG
int debugging3 = 1;
#else
int debugging3 = 0;
#endif

void debug3(char *fmt, ...)
{
    va_list ap;

    if (debugging3) {
        va_start(ap, fmt);
        USLOSS_VConsole(fmt, ap);
    }
}

struct Frame {
    int pid;
    int page;
    int state;
};

typedef struct Fault {
    PID         pid;
    int         offset;
    int         cause;
    SID         wait;
    int         page;
    int         kill;
    struct Fault *nextFault;
} Fault;

struct PagerItem {
    SID     started;
    SID     completed;
};

// Frame variables
struct Frame *table; // Frame table
int tableSize;      // Frame table size
int pageTableSize;  // Page table size
int initialized = 0;
SID frameSID;
static int Pager(void *arg);
// Pager variables
struct PagerItem* pagerTable = NULL;
int numPagers = 0;
int pagerInitialized = 0;

// FaultHandler variables
struct Fault *fq = NULL;

int faultCount = 0;
SID faultSem;

// Semaphores
SID queueSem; // fault queue
// SID pagerSem; // pager method
SID faultHandlerSem; // faultHandler method
SID faultCountSem; // vmStats fault count
SID newCountSem; // vmStats new count
SID framesSem;  // to keep count of free frames in vmStats


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
 *----------------------------------------------------------------------
 *
 * P3FrameInit --
 *
 *  Initializes the frame data structures.
 *  Four states of a frame:
 *     FRAME_UNUSED
 *     FRAME_INUSE
 *     FRAME_ASSIGNED
 *     FRAME_MAPPED
 * 
 * Results:
 *   P3_ALREADY_INITIALIZED:    this function has already been called
 *   P1_SUCCESS:                success
 *
 *----------------------------------------------------------------------
 */
int
P3FrameInit(int pages, int frames) {
    if (kernel_check());
    int result = P1_SUCCESS;
    // Checking if frame has already been initialized
    if (initialized) {
        result = P3_ALREADY_INITIALIZED;
    }
    // initialize the frame data structures, e.g. the pool of free frames
    initialized = 1;
    pageTableSize = pages;
    tableSize = frames;
    table = malloc(sizeof(struct Frame) * frames);
    for (int i = 0; i < frames; i++) {
        table[i].state = FRAME_UNUSED;
        table[i].pid = -1;
        table[i].page = -1;
    }

    // Creating semaphore
    assert(P1_SemCreate("faultCountSem", 0, &faultCountSem) == P1_SUCCESS);
    assert(P1_SemCreate("newCountSem", 0, &newCountSem) == P1_SUCCESS);
    assert(P1_SemCreate("framesSem", 0, &framesSem) == P1_SUCCESS);

    // set P3_vmStats.freeFrames
    P3_vmStats.freeFrames = frames;
    P3_vmStats.pages = pages;
    P3_vmStats.frames = frames;

    return result;
}
/*
 *----------------------------------------------------------------------
 *
 * P3FrameShutdown --
 *
 *  Cleans up the frame data structures.
 *
 * Results:
 *   P3_NOT_INITIALIZED:    P3FrameInit has not been called
 *   P1_SUCCESS:            success
 *
 *----------------------------------------------------------------------
 */
int
P3FrameShutdown(void) {
    if (kernel_check());
    int result = P1_SUCCESS;
    if (!initialized) result = P3_NOT_INITIALIZED;
    else {
        // clean things up
        free(table);
        int rc = P1_SemFree(faultCountSem);
        while (rc == P1_BLOCKED_PROCESSES) {
            assert(P1_V(faultCountSem) == P1_SUCCESS);
            rc = P1_SemFree(faultCountSem);
        }
        assert(rc == P1_SUCCESS);
        assert(P1_SemFree(newCountSem) == P1_SUCCESS);
        rc = P1_SemFree(framesSem);
        while (rc == P1_BLOCKED_PROCESSES) {
            assert(P1_V(framesSem) == P1_SUCCESS);
            rc = P1_SemFree(framesSem);
        }
        assert(rc == P1_SUCCESS);
    }
    return result;
}

/*
 *----------------------------------------------------------------------
 *
 * P3FrameFreeAll --
 *
 *  Frees all frames used by a process
 *
 * Results:
 *   P3_NOT_INITIALIZED:    P3FrameInit has not been called
 *   P1_SUCCESS:            success
 *
 *----------------------------------------------------------------------
 */

int
P3FrameFreeAll(int pid) {
    if (kernel_check());
    int result = P1_SUCCESS;
    if (!initialized) result = P3_NOT_INITIALIZED;
    if (pid < 0 || pid >= P1_MAXPROC) result = P1_INVALID_PID;
    else {
        USLOSS_PTE *pageTable = NULL;
        assert(P3PageTableGet(pid, &pageTable) == P1_SUCCESS);

        // free all frames in use by the process (P3PageTableGet)
        for (int i = 0; i < pageTableSize; i++) {
            if (pageTable[i].incore == 1){
                // If the page is incore, clear the page (set the bits) and mark the frame as available
                table[pageTable[i].frame].state = FRAME_UNUSED;
                table[pageTable[i].frame].page = -1;
                table[pageTable[i].frame].pid = -1;
                memset(&pageTable[i], 0, USLOSS_MmuPageSize());

                // Update P3_vmStats.freeFrames
                assert(P1_P(framesSem) == P1_SUCCESS);
                P3_vmStats.freeFrames++;
                assert(P1_V(framesSem) == P1_SUCCESS);
            }
        }
    }
    return result;
}

/*
 *----------------------------------------------------------------------
 *
 * P3FrameMap --
 *
 *  Maps a frame to an unused page and returns a pointer to it.
 *
 * Results:
 *   P3_NOT_INITIALIZED:    P3FrameInit has not been called
 *   P3_OUT_OF_PAGES:       process has no free pages
 *   P1_INVALID_FRAME       the frame number is invalid
 *   P1_SUCCESS:            success
 *
 *----------------------------------------------------------------------
 */
int
P3FrameMap(int frame, void **ptr)  {
    int result = P1_SUCCESS;
    if (kernel_check());
    if (!initialized) result = P3_NOT_INITIALIZED;
    if (frame < 0 || frame > P3_vmStats.frames) result = P3_INVALID_FRAME;
    else {
        // get the page table for the process (P3PageTableGet)
        USLOSS_PTE *pageTable = NULL;
        int pid = P1_GetPid();
        assert(P3PageTableGet(pid, &pageTable) == P1_SUCCESS);
        int found = 0;
        for (int i = 0; i < pageTableSize; i++) {
            // find an unused page
            if (!pageTable[i].incore) {
                found = 1;
                pageTable[i].incore = 1;
                pageTable[i].frame = frame;
                table[frame].pid = pid;
                table[frame].state = FRAME_INUSE;
                table[frame].page = i;
                // update the page's PTE to map the page to the frame
                *ptr = &pageTable[i];
                // update the page table in the MMU (USLOSS_MmuSetPageTable)
                assert(USLOSS_MmuSetPageTable(pageTable) == USLOSS_MMU_OK);

                // Update vmStats
                P3_vmStats.freeFrames--;
                break;
            }
        }
        if (!found) result = P3_OUT_OF_PAGES;
    }
    return result;
}
/*
 *----------------------------------------------------------------------
 *
 * P3FrameUnmap --
 *
 *  Opposite of P3FrameMap. The frame is unmapped.
 *
 * Results:
 *   P3_NOT_INITIALIZED:    P3FrameInit has not been called
 *   P3_FRAME_NOT_MAPPED:   process didn’t map frame via P3FrameMap
 *   P1_INVALID_FRAME       the frame number is invalid
 *   P1_SUCCESS:            success
 *
 *----------------------------------------------------------------------
 */
int
P3FrameUnmap(int frame)  {
    int result = P1_SUCCESS;
    if (kernel_check());
    if (!initialized) result = P3_NOT_INITIALIZED;
    if (frame < 0 || frame > P3_vmStats.frames) result = P3_INVALID_FRAME;
    int pid = P1_GetPid();
    USLOSS_PTE *pageTable;
    // get the process's page table (P3PageTableGet)
    assert(P3PageTableGet(pid, &pageTable) == P1_SUCCESS);
    int found = 0;
    for (int i = 0; i < pageTableSize; i++) {
        // verify that the process mapped the frame
        if (pageTable[i].frame == frame && pageTable[i].incore) {
            // update page's PTE to remove the mapping
            found = 1;
            pageTable[i].incore = 0;
            table[frame].state = FRAME_UNUSED;
            table[frame].page = -1;
            table[frame].pid = -1;
            // update the page table in the MMU (USLOSS_MmuSetPageTable)
            assert(USLOSS_MmuSetPageTable(pageTable) == USLOSS_MMU_OK);
            break;
        }
    }
    if (!found) result = P3_FRAME_NOT_MAPPED;
    return result;
}

char *
MakeName(char *prefix, int suffix)
{
    static char name[P1_MAXNAME+1];
    snprintf(name, sizeof(name), "%s%d", prefix, suffix);
    return name;
}

/*
 *----------------------------------------------------------------------
 *
 * FaultHandler --
 *
 *  Page fault interrupt handler
 *
 *----------------------------------------------------------------------
 */

static void
FaultHandler(int type, void *arg) {
    if (kernel_check());
    Fault fault;
    int pid = P1_GetPid();

    // fill in other fields in fault
    fault.offset = (int) arg;
    fault.pid = pid;
    fault.cause = type;
    fault.page = fault.offset / USLOSS_MmuPageSize();
    fault.nextFault = NULL;
    fault.kill = 0;

    struct Fault *current = fq;
    // add to queue of pending faults
    assert(P1_P(queueSem) == P1_SUCCESS);
    if (fq == NULL) {
        fq = &fault;
        current = fq;
    } else {
        current = fq;
        while (current->nextFault != NULL){
            current = current->nextFault;
        }
        current->nextFault = &fault;
    }
    assert(P1_V(queueSem) == P1_SUCCESS);

    P3_vmStats.faults ++;

    int rc = P1_V(faultHandlerSem);

    rc = P1_SemFree(faultHandlerSem);

    // Kill faulting process when there is no more swap space or when it causes an access fault
    if (current->kill) {
        if (current->cause == USLOSS_MMU_ACCESS) {
            P2_Terminate(USLOSS_MMU_ACCESS);
        }
        if (current->cause == P3_OUT_OF_SWAP) {
            P2_Terminate(P3_OUT_OF_SWAP);
        }
    }
}


/*
 *----------------------------------------------------------------------
 *
 * P3PagerInit --
 *
 *  Initializes the pagers.
 *
 * Results:
 *   P3_ALREADY_INITIALIZED: this function has already been called
 *   P3_INVALID_NUM_PAGERS:  the number of pagers is invalid
 *   P1_SUCCESS:             success
 *
 *----------------------------------------------------------------------
 */
int
P3PagerInit(int pages, int frames, int pagers) {
    if (kernel_check());
    int result = P1_SUCCESS;
    if (pagerInitialized) result = P3_ALREADY_INITIALIZED;
    pagerInitialized = 1;
    if (pagers < 1 || pagers > P3_MAX_PAGERS) result = P3_INVALID_NUM_PAGERS;
    else {
        USLOSS_IntVec[USLOSS_MMU_INT] = FaultHandler;
        fq = NULL;

        // initialize the pager data structures
        numPagers = pagers;
        pagerTable = malloc(sizeof(struct PagerItem) * numPagers);

        // Creating semaphores
        assert(P1_SemCreate("queueSem", 1, &queueSem) == P1_SUCCESS);
        assert(P1_SemCreate("faultHandlerSem", 0, &faultHandlerSem) == P1_SUCCESS);

        for (int i = 0; i < pagers; i++) {
            assert(P1_SemCreate(MakeName("started", i), 0, &pagerTable[i].started) == P1_SUCCESS);
            assert(P1_SemCreate(MakeName("completed", i), 1, &pagerTable[i].completed) == P1_SUCCESS);

            int pid;
            // fork off the pagers and wait for them to start running
            assert(P1_Fork(MakeName("Pager", i), Pager, (void*)i, USLOSS_MIN_STACK * 4, P3_PAGER_PRIORITY, 0, &pid) == P1_SUCCESS);
            assert(P1_P(pagerTable[i].started) == P1_SUCCESS);

        }

        
    }
    return result;
}

/*
 *----------------------------------------------------------------------
 *
 * P3PagerShutdown --
 *
 *  Kills the pagers and cleans up.
 *
 * Results:
 *   P3_NOT_INITIALIZED:     P3PagerInit has not been called
 *   P1_SUCCESS:             success
 *
 *----------------------------------------------------------------------
 */
int
P3PagerShutdown(void) {
    if (kernel_check());
    if (!pagerInitialized) return P3_NOT_INITIALIZED;

    struct Fault *curr = fq;
    while (curr != NULL) {
        struct Fault *next = curr->nextFault;
        free(curr);
        curr = next;
    }

    numPagers = 0;

    int rc = P1_SemFree(faultHandlerSem);
    while (rc == P1_BLOCKED_PROCESSES) {
        assert(P1_V(faultHandlerSem) == P1_SUCCESS);
        rc = P1_SemFree(faultHandlerSem);
    }
    assert(rc == P1_SUCCESS);
    assert(P1_SemFree(queueSem) == P1_SUCCESS);

    for (int i = 0; i < numPagers; i++) {
        assert(P1_SemFree(pagerTable[i].started) == P1_SUCCESS);
        assert(P1_SemFree(pagerTable[i].completed) == P1_SUCCESS);
    }

    return P1_SUCCESS;
}

/*
 *----------------------------------------------------------------------
 *
 * Pager -- the argument passed in is the pager number
 *
 *  Handles page faults
 *
 *----------------------------------------------------------------------
 */

static int
Pager(void *arg) {
    if (kernel_check());
    
    // Getting pager number
    int pager = (int) arg;

    // notify P3PagerInit that we are running
    assert(P1_V(pagerTable[pager].started) == P1_SUCCESS);

    while (numPagers) {
        // Pause until fault
        assert(P1_P(faultHandlerSem) == P1_SUCCESS);

        // Locks queue
        if (numPagers == 0) break;

        // Get fault
        struct Fault *currFault = fq;
        fq = fq->nextFault;

        if (currFault->cause == USLOSS_MMU_FAULT) {
            currFault->kill = 1;
            continue;
        } else if (currFault->cause == USLOSS_MMU_ACCESS) {
            currFault->kill = 1;
            continue;
        } else {
            int frame = -1;
            for (int i = 0; i < tableSize; i++) {
                if (table[i].state == FRAME_UNUSED) frame = i;
            }

            if (frame == -1){
                assert(P3SwapOut(&frame) == P1_SUCCESS);
            } else {
                P3_vmStats.freeFrames --;
            }
            
            int rc = P3SwapIn(P1_GetPid(), currFault->page, frame);
            USLOSS_PTE *address;
            assert(P3PageTableGet(P1_GetPid(), &address) == P1_SUCCESS);
            if (rc == P3_EMPTY_PAGE) {
                assert(P3FrameMap(frame, (void**)&address) == P1_SUCCESS);

                memset(address, 0, USLOSS_MmuPageSize());
                assert(P3FrameUnmap(frame) == P1_SUCCESS);
                P3_vmStats.new ++;
            } else if (rc == P3_OUT_OF_SWAP) {
                currFault->cause = P3_OUT_OF_SWAP;
                currFault->kill = 1;
                continue;
            }

            // update PTE in faulting process's page table to map page to frame
            address->incore = 1;
            address->read = 1;
            address->write = 1;
            address->frame = frame;

            // Unlock fault queue
            assert(P1_V(faultHandlerSem) == P1_SUCCESS);
        }
    }
    return 0;
}
