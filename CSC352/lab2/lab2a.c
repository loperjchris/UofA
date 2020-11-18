#include <stdio.h>

int main(void) {
	
	printf("int\t%8lu bytes\n", sizeof(int));
	printf("short\t%8lu bytes\n", sizeof(short));
	printf("long\t%8lu bytes\n", sizeof(long));
	printf("long long%7lu bytes\n", sizeof(long long));
	printf("unsinged int%4lu bytes\n", sizeof(unsigned int));
	printf("char\t%8lu bytes\n", sizeof(char));
	printf("float\t%8lu bytes\n", sizeof(float));
	printf("double\t%8lu bytes\n", sizeof(double));
	printf("long double%5lu bytes\n", sizeof(long double));
	return 0;
}