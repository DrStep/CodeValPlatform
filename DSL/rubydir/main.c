#include <stdio.h>
#include <stdlib.h>

int main(int argc, char *argv[]) {
	int i=0, temp;
	for (i=1; i < argc; i++) {
		printf("%s ", argv[i]);
	}
}