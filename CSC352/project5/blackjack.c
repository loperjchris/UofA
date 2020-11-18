/*
* AUTHOR: Ruben Tequida
* FILE: blackjack.c
* ASSIGNMENT: Project5: /dev/cards
* COURSE: CSc352, Spring 2020
* PURPOSE: This program utilizes the card_driver to draw random card from a
* deck of 52 cards and prompts the player to place the card in a pile and play
* solitare blackjack.
*/

#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <string.h>
#include <unistd.h>

// Function prototypes
int get_val(char);
char check_loss(int, int, int, int, int, int, int, int, int, int, int);

/*
* Purpose: Keeps track of each piles score, the number of their aces, the player's
* overall score. Determines if the play is able to continue playing the game or
* if it's gameover because they can't add the drawn card to any pile. When the game
* is over it will open highscore.dat and see if you beat the highscore and write that
* in if it is.
*/
int main () {
	char buff[2];
	char card;
	int val;
	int score = 0;
	int p1 = 0;
	int p2 = 0;
	int p3 = 0;
	int p4 = 0;
	int p5 = 0;
	int p1_aces = 0;
	int p2_aces = 0;
	int p3_aces = 0;
	int p4_aces = 0;
	int p5_aces = 0;
	int p1_size = 0;
	int p2_size = 0;
	int p3_size = 0;
	int p4_size = 0;
	int p5_size = 0;
	char p1_hand[20] = {'\0'};
	char p2_hand[20] = {'\0'};
	char p3_hand[20] = {'\0'};
	char p4_hand[20] = {'\0'};
	char p5_hand[20] = {'\0'};
	char temp[4];
	int pile = 0;
	char gameover = 0;
	int driver;
	int card_count = 0;
	int high_score = 0;
	FILE *high_write;
	FILE *high_read;
	// Continue playing until you can't place in any pile
	while (!gameover) {
		printf("Score: %d\n\n", score);
		printf("Pile (1): %-14s= %d\n", p1_hand, p1);
		printf("Pile (2): %-14s= %d\n", p2_hand, p2);
		printf("Pile (3): %-14s= %d\n", p3_hand, p3);
		printf("Pile (4): %-14s= %d\n", p4_hand, p4);
		printf("Pile (5): %-14s= %d\n\n", p5_hand, p5);
		// Calling driver to draw a card
		driver = open("/dev/card_driver", O_RDONLY);
		read(driver, buff, 1);
		card = buff[0];
		card_count++;
		// Printing out 10 for a draw value of 0
		if (card == '0') {
			printf("Drawn Card: 10\n\n");
			temp[0] = '1';
			temp[1] = '0';
			temp[2] = ' ';
			temp[3] = '\0';
		} else {
			printf("Drawn Card: %c\n\n", card);
			temp[0] = card;
			temp[1] = ' ';
			temp[2] = '\0';
		}
		val = get_val(card);
		// Checking to see if the drawn card can be places in a pile
		gameover = check_loss(val, p1, p1_aces, p2, p2_aces, p3, p3_aces, p4, p4_aces, p5, p5_aces);
		if (gameover) {
			printf("GAMEOVER!!!\nYou can no longer add to any piles!\nYour score is %d.\n", score);
			// If a previous high score exists
			if ((high_read = fopen("highscore.dat", "r+")) != NULL) {
				fscanf(high_read, "%d", &high_score);
			}
			// Didn't beat previous high score
			if (high_score >= score) {
				printf("Sorry, you didn't beat the current high score of %d\n", high_score);
				fclose(high_read);
			// If you did beat the previous high score are one wasn't set, overwrite the previous
			// high score or create the file to hold it respectively.
			} else {
				fclose(high_read);
				printf("You beat the current high score of %d!!!\n", high_score);
				high_write = fopen("highscore.dat", "w+");
				// Writing to file
				fprintf(high_write, "%d", score);
				fclose(high_write);
			}
			continue;
		}
		// Continue asking for player to pick a pile until they pick a pile the card can fit in
		while (1) {
			printf("Which pile to place card in? ");
			scanf("%d", &pile);
			printf("\n");
			// Ask again if the player picks a pile out of range
			if (pile < 0 || pile > 5) {
				printf("You must select a number between 1 and 5\n");
				continue;
			}
			// Check if it fits in pile 1 and add it if it does
			if (pile == 1) {
				// Adding ace: add 11 or 1
				if (val == 11) {
					if ((p1 +val) <= 21) {
						p1 += val;
						p1_aces++;
					} else {
						p1++;
					}
				// Not adding ace so check if any aces in pile
				} else {
					if ((p1 + val) <= 21) {
						p1 += val;
					} else {
						// If there are 11 value aces then subtract 1-and add the new value
						if (p1_aces != 0) {
							p1 -= 10;
							p1 += val;
							p1_aces--;
						// Value can't be added
						} else {
							printf("Can't add to pile %d without going over 21\n", pile);
							continue;
						}
					}
				}
				// Add the symbol to the pile string and increase the pile size
				strcat(p1_hand, temp);
				p1_size++;
			}
			// Check if it fits in pile 2 and add it if it does
			if (pile == 2) {
				// Adding ace: add 11 or 1
				if (val == 11) {
					if ((p2 +val) <= 21) {
						p2 += val;
						p2_aces++;
					} else {
						p2++;
					}
				// Not adding ace so check if any aces in pile
				} else {
					if ((p2 + val) <= 21) {
						p2 += val;
					} else {
						// If there are 11 value aces then subtract 1-and add the new value
						if (p2_aces != 0) {
							p2 -= 10;
							p2 += val;
							p2_aces--;
						// Value can't be added
						} else {
							printf("Can't add to pile %d without going over 21\n", pile);
							continue;
						}
					}
				}
				// Add the symbol to the pile string and increase the pile size
				strcat(p2_hand, temp);
				p2_size++;
			}
			// Check if it fits in pile 3 and add it if it does
			if (pile == 3) {
				// Adding ace: add 11 or 1
				if (val == 11) {
					if ((p3 +val) <= 21) {
						p3 += val;
						p3_aces++;
					} else {
						p3++;
					}
				// Not adding ace so check if any aces in pile
				} else {
					if ((p3 + val) <= 21) {
						p3 += val;
					} else {
						// If there are 11 value aces then subtract 1-and add the new value
						if (p3_aces != 0) {
							p3 -= 10;
							p3 += val;
							p3_aces--;
						// Value can't be added
						} else {
							printf("Can't add to pile %d without going over 21\n", pile);
							continue;
						}
					}
				}
				strcat(p3_hand, temp);
				p3_size++;
			}
			// Check if it fits in pile 4 and add it if it does
			if (pile == 4) {
				// Adding ace: add 11 or 1
				if (val == 11) {
					if ((p4 +val) <= 21) {
						p4 += val;
						p4_aces++;
					} else {
						p4++;
					}
				// Not adding ace so check if any aces in pile
				} else {
					if ((p4 + val) <= 21) {
						p4 += val;
					} else {
						// If there are 11 value aces then subtract 1-and add the new value
						if (p4_aces != 0) {
							p4 -= 10;
							p4 += val;
							p4_aces--;
						// Value can't be added
						} else {
							printf("Can't add to pile %d without going over 21\n", pile);
							continue;
						}
					}
				}
				// Add the symbol to the pile string and increase the pile size
				strcat(p4_hand, temp);
				p4_size++;
			}
			// Check if it fits in pile 5 and add it if it does
			if (pile == 5) {
				// Adding ace: add 11 or 1
				if (val == 11) {
					if ((p5 +val) <= 21) {
						p5 += val;
						p5_aces++;
					} else {
						p5++;
					}
				// Not adding ace so check if any aces in pile
				} else {
					if ((p5 + val) <= 21) {
						p5 += val;
					} else {
						// If there are 11 value aces then subtract 1-and add the new value
						if (p5_aces != 0) {
							p5 -= 10;
							p5 += val;
							p5_aces--;
						// Value can't be added
						} else {
							printf("Can't add to pile %d without going over 21\n", pile);
							continue;
						}
					}
				}
				// Add the symbol to the pile string and increase the pile size
				strcat(p5_hand, temp);
				p5_size++;
			}
			break;
		}
		// Checking if player has gone through all cards in the deck
		if (card_count == 52) {
			score += 60;
			card_count = 0;
			printf("You cleared the deck! +60 points\n\n");
		}
		// Checking for blackjack, natural blackjack , and five card charlies
		if (p1 == 21 || p1_size == 5) {
			if (p1_size == 2) {
				score += 35;
				printf("Natural Blackjack! +35 points\n\n");
			} else if (p1 == 21) {
				score += 21;
				printf("Blackjack! +21 points\n\n");
			} else {
				score += p1;
				printf("Five Card Charlie! +%d points\n\n", p1);
			}
			// Reset pile values
			p1 = 0;
			p1_aces = 0;
			p1_size = 0;
			memset(p1_hand, 0, sizeof(p1_hand));
		}
		if (p2 == 21 || p2_size == 5) {
			if (p2_size == 2) {
				score += 35;
				printf("Natural Blackjack! +35 points\n\n");
			} else if (p2 == 21) {
				score += 21;
				printf("Blackjack! +21 points\n\n");
			} else {
				score += p2;
				printf("Five Card Charlie! +%d points\n\n", p2);
			}
			// Reset pile values
			p2 = 0;
			p2_aces = 0;
			p2_size = 0;
			memset(p2_hand, 0, sizeof(p2_hand));
		}
		if (p3 == 21 || p3_size == 5) {
			if (p3_size == 2) {
				score += 35;
				printf("Natural Blackjack! +35 points\n\n");
			} else if (p3 == 21) {
				score += 21;
				printf("Blackjack! +21 points\n\n");
			} else {
				score += p3;
				printf("Five Card Charlie! +%d points\n\n", p3);
			}
			// Reset pile values
			p3 = 0;
			p3_aces = 0;
			p3_size = 0;
			memset(p3_hand, 0, sizeof(p3_hand));
		}
		if (p4 == 21 || p4_size == 5) {
			if (p4_size == 2) {
				score += 35;
				printf("Natural Blackjack! +35 points\n\n");
			} else if (p4 == 21) {
				score += 21;
				printf("Blackjack! +21 points\n\n");
			} else {
				score += p4;
				printf("Five Card Charlie! +%d points\n\n", p4);
			}
			// Reset pile values
			p4 = 0;
			p4_aces = 0;
			p4_size = 0;
			memset(p4_hand, 0, sizeof(p4_hand));
		}
		if (p5 == 21 || p5_size == 5) {
			if (p5_size == 2) {
				score += 35;
				printf("Natural Blackjack! +35 points\n\n");
			} else if (p5 == 21) {
				score += 21;
				printf("Blackjack! +21 points\n\n");
			} else {
				score += p5;
				printf("Five Card Charlie! +%d points\n\n", p5);
			}
			// Reset pile values
			p5 = 0;
			p5_aces = 0;
			p5_size = 0;
			memset(p5_hand, 0, sizeof(p5_hand));
		}
		// Checking if all piles have been zeroed out
		if (p1 == 0 && p2 == 0 && p3 == 0 && p4 == 0 && p5 == 0) {
			score += 45;
			printf("You cleared the board! +45 points\n\n");
		}
	}
	return 0;
}

/*
* Purpose: Returns the integer value of the character passed in.
*/
int get_val(char card) {
	if (card == 'A') {
		return 11;
	}
	if (card == '2') {
		return 2;
	}
	if (card == '3') {
		return 3;
	}
	if (card == '4') {
		return 4;
	}
	if (card == '5') {
		return 5;
	}
	if (card == '6') {
		return 6;
	}
	if (card == '7') {
		return 7;
	}
	if (card == '8') {
		return 8;
	}
	if (card == '9') {
		return 9;
	}
	if (card == '0' || card == 'J' || card == 'Q' || card == 'K') {
		return 10;
	}
}

/*
* Purpose: Checks if the drawn card can be places in any of the piles. 
* If it can't then return 1 to indicate gameover
*/
char check_loss(int val, int p1, int p1_aces, int p2, int p2_aces, int p3, int p3_aces, int p4, int p4_aces, int p5, int p5_aces) {
	int count = 0;
	if (val != 11) {
		if ((p1 + val) > 21 && p1_aces == 0) {
			count++;
		}
		if ((p2 + val) > 21 && p2_aces == 0) {
			count++;
		}
		if ((p3 + val) > 21 && p3_aces == 0) {
			count++;
		}
		if ((p4 + val) > 21 && p4_aces == 0) {
			count++;
		}
		if ((p5 + val) > 21 && p5_aces == 0) {
			count++;
		}
		if (count == 5) {
			return 1;
		}
	}
	return 0;
}
