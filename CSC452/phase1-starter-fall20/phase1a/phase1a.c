#include "phase1Int.h"
#include "usloss.h"
#include <stdlib.h>
#include <assert.h>
#include <stdio.h>

extern  USLOSS_PTE  *P3_AllocatePageTable(int cid);
extern  void        P3_FreePageTable(int cid);

typedef struct Context {
    int             free;
    void            (*startFunc)(void *);
    void            *startArg;
    USLOSS_Context  context;
    char 	    *stack;
    // you'll need more stuff here
} Context;

static Context   contexts[P1_MAXPROC];

static int currentCid = -1;

int kernel_check(char* func) {
    if ((USLOSS_PsrGet() & 1) == 1) {
        return 1;
    }
    USLOSS_IllegalInstruction();
    USLOSS_Console("ERROR: Process function %s invoked in user mode!", func);
    USLOSS_Halt(1);
    return 0;
}

/*
 * Helper function to call func passed to P1ContextCreate with its arg.
 */
static void launch(void)
{
    if (kernel_check("launch")) {
        assert(contexts[currentCid].startFunc != NULL);
        contexts[currentCid].startFunc(contexts[currentCid].startArg);
    }
}

void P1ContextInit(void)
{
    // initialize contexts
    if (kernel_check("P1ContextInit")) {
        int i;
        for (i = 0; i < P1_MAXPROC; i++) {
            contexts[i].free = 1;
        }
    }
}

int P1ContextCreate(void (*func)(void *), void *arg, int stacksize, int *cid) {
    if (kernel_check("P1ContextCreate")) {
        if (stacksize < USLOSS_MIN_STACK) return P1_INVALID_STACK;
        int i;
        for (i = 0; i < sizeof(contexts); i++) {
            if (contexts[i].free) {
//		P1DisableInterrupts();
                contexts[i].free = 0;
		*cid = i;
                contexts[i].startFunc = func;
                contexts[i].startArg = arg;
		contexts[i].stack = malloc(stacksize);
                USLOSS_ContextInit(&contexts[i].context, contexts[i].stack, stacksize, P3_AllocatePageTable(i), launch);
//		P1EnableInterrupts();
                return P1_SUCCESS;
            }
        }
    }
    // find a free context and initialize it
    // allocate the stack, specify the startFunc, etc.
    return 0;
}

int P1ContextSwitch(int cid) {
    if (kernel_check("P1ContextSwitch")) {
        if ((cid < 0 || cid > P1_MAXPROC) || (contexts[cid].free)) return P1_INVALID_CID;
      	int prevCid = currentCid;
	currentCid = cid;
        if (currentCid < 0) {
            USLOSS_ContextSwitch(NULL, &contexts[cid].context);
        } else {
            USLOSS_ContextSwitch(&contexts[prevCid].context, &contexts[cid].context);
        }
        return P1_SUCCESS;
    }
    return 0;
}

int P1ContextFree(int cid) {
    if (kernel_check("P1ContextFree")) {
        if ((cid < 0 || cid > P1_MAXPROC) || (contexts[cid].free)) return P1_INVALID_CID;
        if (cid == currentCid) return P1_CONTEXT_IN_USE;
        P3_FreePageTable(cid);
        contexts[cid].free = 1;
        free(contexts[cid].stack);
        return P1_SUCCESS;
    }
    return P1_INVALID_CID;
}

void
P1EnableInterrupts(void)
{
    if (kernel_check("P1EnableInterrupts")){
        int val = USLOSS_PsrSet(USLOSS_PsrGet() | 2);
	if (val != USLOSS_DEV_OK) USLOSS_Halt(val);
    }
}

/*
 * Returns true if interrupts were enabled, false otherwise.
 */
int
P1DisableInterrupts(void)
{
    int enabled = FALSE;
    if (kernel_check("P1DisableInterrupts")){
        if ((USLOSS_PsrGet() & 2) == 2) {
            enabled = TRUE;
        }
        int val = USLOSS_PsrSet(USLOSS_PsrGet() & 13);
	if (val != USLOSS_DEV_OK) USLOSS_Halt(val);
    }
    return enabled;
}

