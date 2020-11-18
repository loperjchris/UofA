/*
* AUTHOR: Ruben Tequida
* FILE: mymalloc.c
* ASSIGNMENT: Project3: A Custom malloc()
* COURSE: CSC 352, Spring 2020
* PURPOSE: This program manually allocates spaces in memory utilizing a linked list
* to keep track of which spaces of memory have been allocated and which ones are free
* and which ones aren't.
*/

#include <stdio.h>
#include <unistd.h>
#include "mymalloc.h"


// This struct holds the information necessary to create a linked list
struct node {
	int size_mem;
	char free;
	struct node *next;
	struct node *prev;
};

// Global variables to keep track of linked list positions
struct node *head = NULL;
struct node *tail = NULL;
struct node *cur = NULL;

/*
* Purpose: Traverses the linked list from the tail to the head to find a free allocated space
* that isn't being used. If the linked list is empty it will add a new node to the head. If a
* free space that is big enough to hold the current request can't be found within the linked list
* then a new node is added to the end.
*/
void *my_lastfit_malloc(int size) {
	int node_size = sizeof(struct node);
	// Variable to track if the linked list has been anazlyed yet
	char looked = 0;
	// Check if the linked list is empty
	if (head == NULL) {
		// Make space for node struct
		head = (void*)sbrk(node_size);
		head->size_mem = size;
		// Mark as not free
		head->free = 0;
		head->next = NULL;
		head->prev = NULL;
		tail = head;
		cur = head;
		return (void *)sbrk(size);
	// If linked list already has nodes in it
	} else {
		// Loop through linked list until find free space big enough or loop through
		// entire linked list and find nothing
		while (cur != tail || !looked) {
			// Found free space big enough
			if (size <= cur->size_mem && cur->free) {
				struct node *free_space;
				// The space we require is bigger than what we need so it is split
				// into two nodes
				if ((cur->size_mem - size) >= node_size) {
					free_space = (void*)cur + node_size + size;
					// Size of node is equal to whats left over from the node
					// using the memory
					free_space->size_mem = (cur->size_mem - size) - node_size;
					free_space->free = 1;
					free_space->next = cur->next;
					free_space->prev = cur;
					cur->size_mem = size;
					cur->next = free_space;
				}
				// Make the space no longer free and return the new brk
				struct node *temp = cur;
				temp->free = 0;
				cur = tail;
				return (void*)temp + node_size;
			// Current node isn't free or big enough
			} else {
				looked = 1;
				// Loop back to tail if reached head
				if (cur == head) {
					cur = tail;
				// Go backwards through linked list
				} else {
					cur = cur->prev;
				}
			}
		}
		// Create a new node and add it to the end of the linked list
		struct node *new_space = (void*)sbrk(node_size);
		new_space->size_mem = size;
		new_space->free = 0;
		new_space->next = NULL;
		new_space->prev = tail;
		tail->next = new_space;
		tail = new_space;
		cur = tail;
		return (void *)sbrk(size);
	}
}

/*
* Purpose: Makes the node in the linked list at the address passed in free. If any of the
* nodes around the freed space are also free then those nodes are all absorbed into one free
* node. If the tail if freed then the previous node is made the tail.
*/
void my_free(void *ptr) {
	int node_size = sizeof(struct node);
	struct node *free_this = ptr - node_size;
	// Set node to free
	free_this->free = 1;
	struct node *prev_node = free_this->prev;
	struct node *next_node = free_this->next;
	// The previous node is free so make them one free node
	if (prev_node != NULL && prev_node->free) {
		prev_node->size_mem = prev_node->size_mem + node_size + free_this->size_mem;
		prev_node->next = next_node;
		// If the freed node is the tail then make the previous node the tail
		if (next_node == NULL) {
			tail = prev_node;
			cur = tail;
		} else {
			next_node->prev = prev_node;
		}
		free_this = prev_node;
	}
	// The next node is free so make the current node and the next node one free node
	if (next_node != NULL && next_node->free) {
		free_this->size_mem = free_this->size_mem + node_size + next_node->size_mem;
		free_this->next = next_node->next;
		// If next node is the tail then make the current node the tail
		if (next_node == tail) {
			tail = free_this;
			cur = tail;
		// Else skip over the next node
		} else {
			struct node *temp = next_node->next;
			temp->prev = free_this;
		}
	}
	// The node being freed is the tail
	if (free_this == tail) {
		// Move brk back by the current size of the node's memory
		sbrk((void*)free_this - sbrk(0));
		// If the tail isn't the head free the space and move the tail to the
		// previous node
		if (free_this != head) {
			tail = prev_node;
			prev_node->next = NULL;
			cur = tail;
		// If the tail is the head then remove all nodes
		} else {
			head = NULL;
			tail = head;
			cur = head;
		}
	}
}
