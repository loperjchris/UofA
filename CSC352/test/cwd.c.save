#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <limits.h>

int main() {
	char *homedir = getenv("HOME");
	char cwd[PATH_MAX];
	printf("Home dir: %s\n", homedir);
	if (getcwd(cwd, sizeof(cwd)) != NULL) {
		printf("Current working dir: %s\n", cwd);
	} else {
		perror("getcwd() error");
		return 1;
	}
	return 0;
}
