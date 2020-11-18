/*
* AUTHOR: Ruben Tequida
* FILE: mymalloc.h
* ASSIGNMENT: Project3: A Custom malloc()
* COURSE: CSC 352, Spring 2020
* PURPOSE: This is the header file that lets the compiler know what prototypes
* to expect when compiling mymalloc.c and the test drivers
*/

void *my_lastfit_malloc(int size);
void my_free(void *ptr);

