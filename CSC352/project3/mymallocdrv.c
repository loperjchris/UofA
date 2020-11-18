/*
* AUTHOR: Ruben Tequida
* FILE: mymallocdrv.c
* ASSIGNMENT: Project3: A Custom malloc()
* COURSE: CSC 352, Spring 2020
* PURPOSE: Tests my custom malloc program
*/

#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include "mymalloc.h"

/*
* Purpose: Tests to see if mymalloc can handle the adding and freeing of one
* node
*/
void test_one_entry() {
	int *num1;
	num1 = (int *)my_lastfit_malloc(4 * sizeof(int));
	printf("test_one_entry| added one node, brk: %p\n", sbrk(0));
	my_free(num1);
}

/*
* Purpose: Tests if mymalloc can handle the adding and freeing of two nodes.
* Nodes are freed in reverse order.
*/
void test_two_entries() {
	int *n1;
	int *n2;
	n1 = my_lastfit_malloc(sizeof(int));
	printf("test_two_entries| added one node, brk: %p\n", sbrk(0));
	n2 = my_lastfit_malloc(sizeof(int));
	printf("test_two_entries| added second node, brk: %p\n", sbrk(0));
	my_free(n2);
	printf("test_two_entries| freed second node, brk: %p\n", sbrk(0));
	my_free(n1);
	printf("test_two_entries| freed first node, brk: %p\n", sbrk(0));
}

/*
* Purpose: Tests if mymalloc properly handles freeing a node in the middle of
* the linked list and then adding a node that is bigger than the space freed
* so it will add a new node to the end of the linked list. Freed in added order.
*/
void test3() {
	char *c1;
	char *c2;
	char *c3;
	int *i4;
	c1 = my_lastfit_malloc(sizeof(char));
	c2 = my_lastfit_malloc(sizeof(char));
	c3 = my_lastfit_malloc(sizeof(char));
	printf("test3| added three nodes, brk: %p\n", sbrk(0));
	my_free(c2);
	printf("test3| freed second node, brk: %p\n", sbrk(0));
	i4 = my_lastfit_malloc(sizeof(int));
	printf("test3| added larger node, brk: %p\n", sbrk(0));
	my_free(c1);
	my_free(c3);
	my_free(i4);
}

/*
* Tests if mymalloc properly handles the freeing of a node and then the reuse of
* that node with a smaller space requirement. Frees in reverse order after reusing
* a previously freed space.
*/
void test4() {
	char *c1;
	char *c2;
        int *i2;
        char *c3;
	char *c4;
	c1 = my_lastfit_malloc(sizeof(char));
        i2 = my_lastfit_malloc(sizeof(int));
        c3 = my_lastfit_malloc(sizeof(char));
	c4 = my_lastfit_malloc(sizeof(char));
	printf("test4| added four nodes, brk: %p\n", sbrk(0));
	my_free(i2);
	printf("test4| freed second node, brk: %p\n", sbrk(0));
	c2 = my_lastfit_malloc(sizeof(char));
	my_free(c4);
	printf("test4| freed fourth node, brk: %p\n", sbrk(0));
	my_free(c3);
	printf("test4| freed third node, brk: %p\n", sbrk(0));
	my_free(c2);
	printf("test4| freed second node, brk: %p\n", sbrk(0));
	my_free(c1);
	printf("test4| freed first node, brk: %p\n", sbrk(0));
}

/*
* Purpose: Tests different allocations of memory to see if mymalloc is allocating
* and freeing memory the right way. All values of brk after each test call should
* be equal to each other if allocation and free are done correctly.
*/
int main() {
	printf("origianl val brk: %p\n", sbrk(0));
	test_one_entry();
	printf("new brk after first test: %p\n", sbrk(0));
	test_two_entries();
	printf("new brk after second test: %p\n", sbrk(0));
	test3();
	printf("new brk after third test: %p\n", sbrk(0));
	test4();
	printf("new brk after fourth test: %p\n", sbrk(0));
	return 0;
}

