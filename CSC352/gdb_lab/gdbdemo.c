#include <stdio.h>

void fun(void)
{
	int a = 5;
	int b = 1;
	int c;
	c = a/b;
}

int main()
{
	int x;
	char c[30];

	printf("Enter a number: ");
	scanf("%d", &x);

	fun();

	return 0;
}
