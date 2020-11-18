/*
* AUTHOR: Ruben Tequida
* FILE: rps.c
* ASSIGNMENT: Project 1: RPS and Image Transformations
* COURSE: CSC 352, Spring 2020
* Purpose: Begins a best of 5 tournment of rock, paper, scissors 
* between the user and the computer.
*/

#include <stdio.h>
#include <string.h>
#include <time.h>
#include <stdlib.h>
#include <ctype.h>

/*
* Purpose: Selects a choice for the computer and compares that to the 
* choice the player made. If the computer won it returns a 0, if the 
* player won returns a 1, and returns 2 if a tie.
*/
int playRound(void) {
	char choice[9];
	char pcString[9];
	int playerChoice = 0;
	srand(time(NULL));
	// high and low are used as bounds for producing a random number
	int high = 2;
	int low = 0;
	int i = 0;
	int pcChoice = rand() % (high - low + 1) + low;
	// Properly associating a string to the number chosen by the computer
	if (pcChoice == 0) {
		strcpy(pcString, "scissors");
	}
	else if (pcChoice == 1) {
		strcpy(pcString, "rock");
	}
	else if (pcChoice == 2) {
		strcpy(pcString, "paper");
	}
	// Evaluating the player's choice
	while (1) {
		printf("\nWhat is your choice? ");
		scanf("%8s", choice);
		// Normalizing what the player typed to lowercase
		for (i = 0; choice[i]; i++) {
			choice[i] = tolower(choice[i]);
		}
		// Properly associating player's choice to a number
		if (strcmp(choice, "scissors") == 0) {
			playerChoice = 0;
			break;
		}
		else if (strcmp(choice, "rock") == 0) {
			playerChoice = 1;
			break;
		}
		else if (strcmp(choice, "paper") == 0) {
			playerChoice = 2;
			break;
		}
		// If the player didn't select rock, paper, or scissors
		else {
			printf("Please, choose rock, paper, or scissors.\n");
		}
	}
	// Determing who won or if there's a tie
	printf("The computer chooses %s. ", pcString);
	// PC = scissors, player = paper, PC wins
	if (pcChoice == 0 && playerChoice == 2) {
		return 0;
	}
	// PC = paper, player = scissors, player wins
	else if (pcChoice == 2 && playerChoice == 0) {
		return 1;
	}
	// PC wins
	else if (pcChoice == playerChoice) {
		return 2;
	}
	// Player wins
	else if (pcChoice < playerChoice) {
		return 1;
	}
	// Tie
	return 0;
}

/*
* Purpose: Begins a best of 5 tournament and utilizes each games outcome
* to determine if the tournament should continue or if it ends and who won
* the tournamnet.
*/
int main(void) {
	char play[4];
	int i = 0;
	printf("Welcome to Rock, Paper, Scissors\n\n");
	// Continues running the program until the play inputs 'no'
	while (1) {
		printf("Would you like to play? ");
		scanf("%3s", play);
		// Normalizing player's choice to lowercase
		for (i = 0; play[i]; i++) {
			play[i] = tolower(play[i]);
		}
		if (strcmp(play, "yes") == 0) {
			int wins = 0;
			int losses = 0;
			// Continues tournament until a player reaches 3 wins
			while (wins < 3 && losses < 3) {
				int outcome = playRound();
				// PC wims
				if (outcome == 0) {
					printf("You lose this game\n\n");
					losses++;
				}
				// Player wins
				else if (outcome == 1) {
					printf("You win this game\n\n");
					wins++;
				}
				// Tie
				else {
					printf("The game is tied\n\n");
				}
				printf("The score is now you: %d computer: %d\n", wins, losses);
			}
			// Player wins tournament
			if (wins > losses) {
				printf("\nYou won the tournament!\n\n");
			}
			// PC wins tournament
			else {
				printf("\nYou lost the tournament.\n\n");
			}
		}
		else if (strcmp(play, "no") == 0) {
			break;
		}
		else {
			printf("Please, type yes or no.\n");
		}
	}
	return 0;
}
