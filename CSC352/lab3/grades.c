/*
* AUTHOR: Ruben Tequida
* FILE: grades.c
* ASSIGNMENT: Lab3: Malloc and Free
* COURSE: CSC 352, Spring 2020
* PURPOSE: This program takes in grades given by the user and creates nodes
* that form a linked list by dynamically allocating memory space everytime
* a new grade is put in. Then the program calculates the average of all the
* grades and then frees all the memory that was allocated to make the linked
* list.
*/

#include <stdio.h>
#include <stdlib.h>

/*
* This struct holds the grade values for each node in the linked list
*/
struct Node {
	int grade;
	struct Node *next;
};

/*
* Purpose: Takes the linked list head as an argument and goes through each node 
* in order to calculate the users average grade.
*/
void findAverage(struct Node *head) {
	struct Node *cur = head;
	int sum = 0;
	int num_nodes = 0;
	double avg = 0;
	// Traversing throught the linked list until it reaches null
	while (cur != NULL) {
		sum += cur->grade;
		num_nodes++;
		cur = cur->next;
	}
	// If user inputs no grades 
	if (num_nodes == 0) {
		printf("Cannot divide by zero. Exiting program\n");
		exit(1);
	// Calculating average
	} else {
		avg = sum / num_nodes;
		printf("Your average is %.2lf.\n", avg);
	}
}

/*
* Purpose: Takes the linked list head as an arguement and traverses the
* linked list until it finds the last node. Then, it frees the last node
* and sets the node before it to point to null.
*/
void cleanUp(struct Node *head) {
	struct Node *cur;
	struct Node *prev;
	// Go until the head equals null
	while (head != NULL) {
		// If only one node then free the head and point to null
		if (head->next == NULL) {
			free(head);
			head = NULL;
			continue;
		}
		cur = head;
		prev = cur;
		// Find last node in list
		while (cur->next != NULL) {
			prev = cur;
			cur = cur->next;
		}
		// Free last node and have prev point to null
		free(cur);
		prev->next = NULL;
	}
}

/*
* Purpose: Has the user input all the grades they require and dynamically
* allocates space for a linked list node for every grade the user inputs.
*/
int main () {
	int input = 0;
	struct Node *head = NULL;
	struct Node *node;
	struct Node * prev;
	// Continuously ask for user input until -1 is entered
	while (input != -1) {
		printf("Enter a grade or enter -1 to stop: ");
		scanf("%d", &input);
		struct Node *node = malloc(sizeof(struct Node));
		// Exit programming if no available space can be found
		if (node == NULL) {
			printf("Not enough space in memory. Exiting program.");
			exit(1);
		}
		if (input != -1) {
			// Add node to head if linked list is empty
			if (head == NULL) {
				head = node;
				node->grade = input;
				node->next = NULL;
			// Otherwise add to end
			} else {
				prev->next = node;
				node->grade = input;
				node->next = NULL;
			}
			// Make prev the last node in the list
			prev = node;
		}
	}
	findAverage(head);
	cleanUp(head);
	return 0;
}
