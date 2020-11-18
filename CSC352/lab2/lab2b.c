#include <stdio.h>

int main(void) {
	int weight = 0;
	printf("Please enter the weight you'd like to convert: ");
	scanf("%d", &weight);
	printf("\nHere is the weight on other planets:\n\n");
	printf("Mercury\t%8.0f lbs\n", (weight*0.38));
	printf("Venus\t%8.0f lbs\n", (weight * 0.91));
	printf("Mars\t%8.0f lbs\n", (weight * 0.38));
	printf("Jupiter\t%8.0f lbs\n", (weight * 2.54));
	printf("Saturn\t%8.0f lbs\n", (weight * 1.08));
	printf("Uranus\t%8.0f lbs\n", (weight * 0.91));
	printf("Neptune\t%8.0f lbs\n", (weight * 1.19));
	return 0;
}