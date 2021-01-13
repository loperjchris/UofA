/*
 * phase3b.c
 *
 */

#include <assert.h>
#include <phase1.h>
#include <phase2.h>
#include <usloss.h>
#include <string.h>
#include <libuser.h>

#include "phase3Int.h"

/*
* This method determines the size of pages, their offset into memory
* and which page is being requesting in order to determine if that page
* is in memory. If it isn't a page fault occurs and the proper page is 
* fetched.
*/
void
P3PageFaultHandler(int type, void *arg)
{
    int pageSize = USLOSS_MmuPageSize();
    int offset = (int) arg;
    int pid = P1_GetPid();
    int page = offset / pageSize;
    USLOSS_PTE *table;
    int rc = USLOSS_MmuGetCause();
    // What to do if page fault
    if (rc == USLOSS_MMU_FAULT) {
        // Get the page table for the process
        rc = P3PageTableGet(pid, &table);
        USLOSS_Console("PAGE FAULT!!! PID %d page %d\n", pid, page);
        // Page table hasn't been established for this process
        if (table == NULL) {
            USLOSS_Console("PAGE FAULT!!! PID %d has no page table!!!\n", pid);
            USLOSS_Halt(1);
        // Set the processes page table in memory
        } else {
            if (table[page].incore == 0) {
              table[page].incore = 1;
              table[page].read = 1;
              table[page].write = 1;
              table[page].frame = page;      
              rc = USLOSS_MmuSetPageTable(table);        
            }
        }
    } else {
        // Check for proper access
        if (rc == USLOSS_MMU_ACCESS) {
            USLOSS_Console("PROTECTION VIOLATION!!! PID %d offset 0x%x!!!\n", pid, offset);
        } else {
            USLOSS_Console("Unknown cause: %d\n", rc);
        }
        USLOSS_Halt(1);
    }
}

/*
* This method allocates memory for a page table with a number of 
* pages per entry and sets incore to 0 detailing that the page isn't
* in memory..
*/
USLOSS_PTE *
P3PageTableAllocateEmpty(int pages)
{
    USLOSS_PTE  *table = NULL;
    // Allocate memory for all page entries
    table = malloc(sizeof(USLOSS_PTE) * pages);
    // Set attributes for each entry
    for (int x = 0; x < pages; x++) {
        table[x].incore = 0;
        table[x].read = 1;
        table[x].write = 1;
        table[x].frame = x;
    }
    return table;
}
