#include <stdio.h>
#include <string.h>

int main(void) {
	char input[100];
	printf("Enter a word: ");
	scanf("%s", input);
	int i = 0;
	int j = strlen(input) - 1;
	while(i < j) {
		if (input[i] != input[j]) {
			printf("%s is not a palindrome\n", input);
			return 0;
		}
		i++;
		j--;
	}
	printf("%s is a palindrome\n", input);
	return 0;
}
