/*
* AUTHOR: Ruben Tequida
* FILE: phase0.c
* ASSIGNMENT: Phase0
* SUBMISSION TYPE: Solo
* COURSE: CSC 452, Fall 2020
* PURPOSE: This program introduces the use of contexts
* implemented using USLOSS.
*/


#include <stdio.h>
#include "usloss.h"

//Initializing context and stack variables
USLOSS_Context context0;
USLOSS_Context context1;

char stack0[USLOSS_MIN_STACK];
char stack1[USLOSS_MIN_STACK];

/*
* Purpose: Handles the operation of context0. Prints the number
* of times it was called and Hello.
*/
void hello_func(void) {
	int i;
	for (i = 1; i < 11; i++) {
		USLOSS_Console("%d Hello ", i);
		USLOSS_ContextSwitch(&context0, &context1);
	}
}

/*
* Purpose: Handles the operation of context1. Prints World
* and ! the number of times it was called.
*/
void world_func(void) {
	int i;
	int count;
	for (i = 1; i < 11; i++) {
		USLOSS_Console("World");
		//Printing an ! i times
		for (count = 0; count < i; count++) {
			USLOSS_Console("!");
		}
		USLOSS_Console("\n");
		//Halt the context once i is 10
		if (i == 10) {
			USLOSS_Halt(0);
		//Switch to context0 otherwise
		} else {
			USLOSS_ContextSwitch(&context1, &context0);
		}
	}
}

void
startup(int argc, char **argv)
{
    /*
     * Your code here. If you compile and run this as-is you
     * will get a simulator trap, which is a good opportunity
     * to verify that you get a core file and you can use it
     * with gdb.
     */
	USLOSS_ContextInit(&context0, stack0, sizeof(stack0), NULL, hello_func);
	USLOSS_ContextInit(&context1, stack1, sizeof(stack1), NULL, world_func);
	USLOSS_ContextSwitch(NULL, &context0);
	//USLOSS_ContextSwitch(&context0, &context1);
}

// Do not modify anything below this line.

void
finish(int argc, char **argv)
{
    USLOSS_Console("Goodbye.\n");
}

void
test_setup(int argc, char **argv)
{
    // Do nothing.
}

void
test_cleanup(int argc, char **argv)
{
    // Do nothing.
}

