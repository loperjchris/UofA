/*
* AUTHOR: Ruben Tequida
* FILE: mystrings.c
* ASSIGNMENT: Project2: What's the Password?
* COURSE: CSC 352, Spring 2020
* PURPOSE: This program takes in a file as an arguemtn, opens that file, and
* prints all strings that are greater than 4 characters in length.
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

/*
* Opens the passed in file and goes through every character inside it adding
* that character to an array if it is a printable character. Once it hits a 
* non-printable character it will print what is in the array and then continue
* with a new string.
*/
int main(int argc, char *argv[]) {
	// Open the pass file
	FILE *text = fopen(argv[1], "r+");
	char *word;
	int c;
	int n = 0;
	long file_size = 0;
	// Check if the file could be opened
	if (text == NULL) {
		printf("File \'%s\' could not be found.\n", argv[1]);
		exit(1);
	}
	fseek(text, 0, SEEK_END);
	file_size = ftell(text);
	fseek(text, 0, SEEK_SET);
	word = malloc(sizeof(char) * file_size);
	// Check if the memory asked for could be found
	if (word == NULL) {
		printf("Not enough space in memeory\n");
		exit(1);
	}
	// Continue to read characters until the end of the file
	while ((c = fgetc(text)) != EOF) {
		// Add to array if printable character
		if (c > 31 && c < 127) {
			word[n++] = (char) c;
		} else {
			// Terminate with null character
			word[n] = '\0';
			// Print out string if longer than 3 characters
			if (strlen(word) > 3) {
				printf("%s\n", word);
			}
			n = 0;
		}
	}
	// Free memory allocated with malloc
	free(word);
	return 0;
}
