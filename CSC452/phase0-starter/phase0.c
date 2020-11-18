#include <stdio.h>
#include "usloss.h"


void
startup(int argc, char **argv)
{
    /*
     * Your code here. If you compile and run this as-is you
     * will get a simulator trap, which is a good opportunity
     * to verify that you get a core file and you can use it
     * with gdb.
     */
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

