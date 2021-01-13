/*
* Author: Mauricio Herrera and Ruben Tequida
* File: phase3d.c
* Date: 12/06/2020
* Course: CSC 452 - Principles of Operating Systems
* Description: this phase provides the tools to 
* support virtual memory. It takes care of swapping
* by storing some pages on disk and bringing them to 
* memory on demand.
*/

#include <assert.h>
#include <phase1.h>
#include <phase2.h>
#include <usloss.h>
#include <string.h>
#include <libuser.h>

#include "phase3.h"
#include "phase3Int.h"

#ifdef DEBUG
static int debugging3 = 1;
#else
static int debugging3 = 0;
#endif

static void debug3(char *fmt, ...)
{
    va_list ap;

    if (debugging3) {
        va_start(ap, fmt);
        USLOSS_VConsole(fmt, ap);
    }
}

typedef struct Frame
{
    int pid;
    int page;
    int in_use;
} Frame;

static Frame *frameTable;

typedef struct Swap{
    int pid;
    int first;
    int track;
    int page;
    int used;
} Swap;

static Swap *swapData;

static int initialized = FALSE;
static int mutex;
static int numFrames;
static int numPages;
int pageSize;
int slots;

static int sectorSize;
static int trackSize;
static int tracks;
static int sectors;

// /*
// * This method performs a bitwise and with the first bit in 
// * the PSR to determine if the call was made in user or kernel
// * mode.
// * Returns 1 if kernel mode, halts and returns 0 otherwise.
// */
// int kernel_check() {
//     if ((USLOSS_PsrGet() & 1) == 1) {
//         return 1;
//     }
//     USLOSS_IllegalInstruction();
//     P1_Quit(1024);
//     return 0;
// }


/*
 *----------------------------------------------------------------------
 *
 * P3SwapInit --
 *
 *  Initializes the swap data structures.
 *
 * Results:
 *   P3_ALREADY_INITIALIZED:    this function has already been called
 *   P1_SUCCESS:                success
 *
 *----------------------------------------------------------------------
 */
int
P3SwapInit(int pages, int frames)
{
    // if (kernel_check());
    // Checking if method has been called
    if (initialized) return P3_ALREADY_INITIALIZED;

    // Creating mutex semaphore
    assert(P1_SemCreate("Mutex", 1, &mutex) == P1_SUCCESS);

    // Storing number of frames and pages
    numFrames = frames;
    numPages = pages;

    // Initialize frame table
    frameTable = (Frame*) malloc(sizeof(struct Frame) * numFrames);
    for (int n = 0; n < frames; n++) {
        frameTable[n].pid = -1;
        frameTable[n].page = -1;
        frameTable[n].in_use = 0;
    }

    pageSize = USLOSS_MmuPageSize();
    assert(P2_DiskSize(P3_SWAP_DISK, &sectorSize, &trackSize, &tracks) == P1_SUCCESS);
    sectors = pageSize / sectorSize;
    slots = (sectorSize * trackSize * tracks) / pageSize;
    //slots = (trackSize / sectors) * tracks;
    // initialize the swap data structures, e.g. the pool of free blocks
    swapData = (Swap*) malloc(sizeof(struct Swap) * slots);
    for (int i = 0; i < slots; i++) {
        swapData[i].pid = -1;
        swapData[i].first = i / tracks;
        swapData[i].track = i % tracks;
        swapData[i].page = -1;
        swapData[i].used = 0;
    }

    // Setting initialized flag
    initialized = TRUE;

    // Initializing values of VmStats
    P3_vmStats.pages = numPages;
    P3_vmStats.frames = numFrames;
    P3_vmStats.freeFrames = numFrames;
    P3_vmStats.blocks = slots;
    P3_vmStats.freeBlocks = slots;
    P3_vmStats.faults = 0;
    P3_vmStats.new = 0;
    P3_vmStats.pageIns = 0;
    P3_vmStats.pageOuts = 0;
    P3_vmStats.replaced = 0;

    return P1_SUCCESS;
}
/*
 *----------------------------------------------------------------------
 *
 * P3SwapShutdown --
 *
 *  Cleans up the swap data structures.
 *
 * Results:
 *   P3_NOT_INITIALIZED:    P3SwapInit has not been called
 *   P1_SUCCESS:            success
 *
 *----------------------------------------------------------------------
 */
int
P3SwapShutdown(void)
{
    // if (kernel_check());
    if (!initialized) return P3_NOT_INITIALIZED;

    // clean things up
    free(swapData);
    free(frameTable);
    assert(P1_SemFree(mutex) == P1_SUCCESS);
    initialized = FALSE;
    return P1_SUCCESS;
}

/*
 *----------------------------------------------------------------------
 *
 * P3SwapFreeAll --
 *
 *  Frees all swap space used by a process
 *
 * Results:
 *   P3_NOT_INITIALIZED:    P3SwapInit has not been called
 *   P1_SUCCESS:            success
 *
 *----------------------------------------------------------------------
 */

int
P3SwapFreeAll(int pid)
{
    // if (kernel_check());
    if (!initialized) return P3_NOT_INITIALIZED;
    if (pid < 0 || pid >= P1_MAXPROC) return P1_INVALID_PID;
    // P(mutex)
    assert(P1_P(mutex) == P1_SUCCESS);

    // Free all swap space used by the process
    for (int i = 0; i < slots ;i++) {
        if (swapData[i].pid == pid) {
            swapData[i].pid  = -1;
            swapData[i].page = -1;
            swapData[i].used = 0;
            swapData[i].first = -1;
            swapData[i].track = -1;
            P3_vmStats.freeBlocks++;
        }
    }

	for (int i = 0; i < numFrames; i++) {
            if (frameTable[i].pid == pid) {
            	frameTable[i].pid = -1;
            	frameTable[i].page = -1;
            	frameTable[i].in_use = 0;
            }
   	}

    // V operation
    assert(P1_V(mutex) == P1_SUCCESS);

    return P1_SUCCESS;
}

/*
 *----------------------------------------------------------------------
 *
 * P3SwapOut --
 *
 * Uses the clock algorithm to select a frame to replace, writing the page that is in the frame out 
 * to swap if it is dirty. The page table of the page’s process is modified so that the page no 
 * longer maps to the frame. The frame that was selected is returned in *frame. 
 *
 * Results:
 *   P3_NOT_INITIALIZED:    P3SwapInit has not been called
 *   P1_SUCCESS:            success
 *
 *----------------------------------------------------------------------
 */
int
P3SwapOut(int *frame) 
{
    // if (kernel_check());
    if (!initialized) return P3_NOT_INITIALIZED;

    static int hand = -1;

    // P op on mutex
    assert(P1_P(mutex) == P1_SUCCESS);
    int target;
    int accessPointer;

    while(TRUE) {
        hand = (hand + 1) % numFrames;
        if (!frameTable[hand].in_use) {
            assert(USLOSS_MmuGetAccess(hand, &accessPointer) == USLOSS_MMU_OK);
            if (accessPointer & USLOSS_MMU_REF) {
                target = hand;
                break;
            } else {
                assert(USLOSS_MmuSetAccess(hand, (accessPointer & ~USLOSS_MMU_REF)) == USLOSS_MMU_OK);
            }
        }
    }
    
    int place;
    for (place = 0; place < slots; place++) {
        if (swapData[place].pid == frameTable[target].pid && swapData[place].page == frameTable[target].page) {
            break;
        }
    }
    assert(USLOSS_MmuGetAccess(target, &accessPointer) == USLOSS_MMU_OK);

    //if frame[target] is dirty (USLOSS_MmuGetAccess)
    if(accessPointer & USLOSS_MMU_DIRTY) {
        void *buffer = malloc(pageSize);
        void *address;
        assert(P3FrameMap(target, &address) == P1_SUCCESS);
        
        // write page to its location on the swap disk (P3FrameMap,P2_DiskWrite,P3FrameUnmap)
        memcpy(buffer, address, pageSize);
        int currTrack = (sectors * place) / trackSize;
        int currSector = (sectors * place) % trackSize;
        assert(P2_DiskWrite(P3_SWAP_DISK, currTrack, currSector, sectors, buffer) == P1_SUCCESS);
        free(buffer);
        
        // clear dirty bit (USLOSS_MmuSetAccess)
        assert(P3FrameUnmap(target) == P1_SUCCESS);
	// Updating VmStats
    	P3_vmStats.pageOuts ++;
        swapData[place].used = 0;
        assert(USLOSS_MmuSetAccess(target, (accessPointer & ~USLOSS_MMU_DIRTY)) == USLOSS_MMU_OK);
    }

    // Update page table of process to indicate page is no longer in a frame
    USLOSS_PTE *pageTable;
    assert(P3PageTableGet(frameTable[target].pid, &pageTable) == P1_SUCCESS);
    pageTable[frameTable[target].page].incore = 0;
    //pageTable[frameTable[target].page].frame = -1;
    //assert(USLOSS_MmuSetPageTable(pageTable) == USLOSS_MMU_OK);

    // Mark frames[target] as busy
    frameTable[target].in_use = TRUE;
    assert(P1_V(mutex) == P1_SUCCESS);
    *frame = target;

    return P1_SUCCESS;
}

/*
 *----------------------------------------------------------------------
 *
 * P3SwapIn --
 *
 *  Opposite of P3FrameMap. The frame is unmapped.
 *
 * Results:
 *   P3_NOT_INITIALIZED:     P3SwapInit has not been called
 *   P1_INVALID_PID:         pid is invalid      
 *   P1_INVALID_PAGE:        page is invalid         
 *   P1_INVALID_FRAME:       frame is invalid
 *   P3_EMPTY_PAGE:          page is not in swap
 *   P1_OUT_OF_SWAP:         there is no more swap space
 *   P1_SUCCESS:             success
 *
 *----------------------------------------------------------------------
 */
int
P3SwapIn(int pid, int page, int frame)
{
    // if (kernel_check());
    if (!initialized) return P3_NOT_INITIALIZED;
    if (page < 0 || page >= numPages) return P3_INVALID_PAGE;
    if (pid < 0 || pid >= P1_MAXPROC) return P1_INVALID_PID;
    if (frame < 0 || frame >= numFrames) return P3_INVALID_FRAME;
    int result = P1_SUCCESS;

    // P(mutex)
    assert(P1_P(mutex) == P1_SUCCESS);

    // Checking to see if page is on Swap Disk
    int found = 0;
    int i;
    for (i = 0; i < slots; i++) {
        if (swapData[i].pid == pid && swapData[i].page == page) {
            found = 1;
            break;
        }
    }
    if (found) {
        void *buffer = malloc(pageSize);
        void *address;
        assert(P3FrameMap(frame, &address) == P1_SUCCESS);
        memcpy(buffer, address, pageSize);
        int currTrack = (sectors * i) / trackSize;
        int currSector = (sectors * i) % trackSize;
        assert(P2_DiskRead(P3_SWAP_DISK, currTrack, currSector, sectors, buffer) == P1_SUCCESS);
        memcpy(address, buffer, pageSize);
        free(buffer);
        assert(P3FrameUnmap(frame) == P1_SUCCESS);

        // Updating VmStats
        //P3_vmStats.replaced ++;
        // Updating VmStats
        P3_vmStats.pageIns ++;
    } else {
        found = 0;
        for (int j = 0; j < slots; j++) {
            if (swapData[j].pid == -1) {
                swapData[j].pid = pid;
                swapData[j].page = page;
                swapData[j].used = 0;
                found = 1;

                // Updating vmStats
                P3_vmStats.freeBlocks --;
                break;
            }
        }
        if (!found) {
            result = P3_OUT_OF_SWAP;
        } else {
            result = P3_EMPTY_PAGE;
        }
    }

    // USLOSS_PTE *table = NULL;
    // assert(P3PageTableGet(pid, &table) == P1_SUCCESS);
    // (table + page)->incore = 1;
    // (table + page)->write = 1;
    // (table + page)->read = 1;
    // (table + page)->frame = frame;
    // assert(USLOSS_MmuSetPageTable(table) == USLOSS_MMU_OK);
    frameTable[frame].pid = pid;
    frameTable[frame].page = page;
    frameTable[frame].in_use = FALSE;

    // V op
    assert(P1_V(mutex) == P1_SUCCESS);

    return result;
}