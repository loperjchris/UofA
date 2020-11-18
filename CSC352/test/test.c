#include <stdio.h>
#include <string.h>
#include <stdlib.h>

int main() {
	for(;;) {
		int *x = malloc(sizeof(int));
		printf("Enter an integer (0 to stop):");
		scanf("%d", x);
		printf("You entered %d\n", *x);
		if (*x == 0) break;
	}
	return 0;
}
