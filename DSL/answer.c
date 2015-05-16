#include <stdio.h>
#include <stdlib.h>

int main(int argc, char *argv[]) {
	int i=0, length;
	int arr[100];
	scanf("%d", &length);
	for (i=0; i < length; i++) {
		scanf("%d", &arr[i]);
	}

	for (i=0; i < length; i++) {
		printf("%d ", arr[i]);
	}
}