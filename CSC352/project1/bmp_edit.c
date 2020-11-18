/*
* AUTHOR: Ruben Tequida
* FILE: bmp_edit.c
* ASSIGNMENT: Project1: RPS and Image Transformations
* COURSE: CSC 352, Spring 2020
* PURPOSE: This program takes in two command line arguments that
* tell the program whether to invert the image or to make it grayscale
* and which image to do it on.
*/

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#pragma pack(1)

/*
* This struct holds the values for each component held in the initial
* header of a bmp file.
*/
struct file_header {
	short type;
	int size;
	short reserved_1;
	short reserved_2;
	int offset;
};

/*
* This struct holds the values for each component held in the DIB
* header of the bmp file.
*/
struct dib_header {
	int header_size;
	int width;
	int height;
	short planes;
	short bpp;
	int comp_scheme;
	int image_size;
	int horiz_res;
	int vert_res;
	int num_col_pal;
	int num_important;
};

/*
* This struct holds the 1 byte values of green, blue, and red that 
* represent each pixel of an image.
*/
struct pixel {
	unsigned char blue;
	unsigned char green;
	unsigned char red;
};

/*
* Purpose: Opens up the image if it can find it and determines if the 
* image is in the proper format based on the format identifier in the initial
* header and the size of the DIB header is 40  and also that the bits per pixel
* is equal to 24. Then, depending on the first arguement passed, will either take
* all the pixel arrays and invert their binary values to invert the image or 
* calculate the appropriate grayscale pixel values to change the image to 
* grayscale.
*/
int main(int argc, char *argv[]) {
	int i;
	// Open the image and check if it exists
	FILE *image = fopen(argv[2], "r+");
	if (image == NULL) {
		printf("File \'%s\' could not be found.\n", argv[2]);
		exit(1);
	}
	fseek(image, 0, SEEK_SET);
	struct file_header fh;
	// Read in the initial header and check if it's a bmp
	fread(&fh, sizeof(fh), 1, image);
	if (fh.type != 0x4D42) {
		printf("Image format not supported. Incorrect image type.\n");
		exit(1);
	}
	struct dib_header dh;
	// Read in the DIB header and check its size and if the bits per pixel is 24
	fread(&dh, sizeof(dh), 1, image);
	if (dh.header_size != 40) {
		printf("Image format not supported. Incorrect DIB header size.\n");
		exit(1);
	}
	if (dh.bpp != 24) {
		printf("Image formate not supported. Incorrect number of bits per pixel.\n");
		exit(1);
	}
	fseek(image, fh.offset, SEEK_SET);
	struct pixel pix;
	// Going through every single pixel in the image
	for (i = 0; i < (dh.width * dh.height); i++) {
		// Skipping the padding at the end of each row of pixels if there is any
		if ((((dh.width * 3) % 4)  != 0) && i == dh.width) {
			fseek(image, ((dh.width * 3) % 4), SEEK_CUR);
		}
		fread(&pix, sizeof(pix), 1, image);
		fseek(image, -3, SEEK_CUR);
		// Take the bitwise not of the green, blue, and red portion of the pixel
		if (strcmp(argv[1], "-invert") == 0) {
			pix.blue = ~pix.blue;
			pix.green = ~pix.green;
			pix.red = ~pix.red;
		}
		// Apply the formula to each portion of the pixel
		if (strcmp(argv[1], "-grayscale") == 0) {
			double blueTemp = ((double)pix.blue/255) * 0.0722;
			double greenTemp = ((double)pix.green/255) * 0.7152;
			double redTemp = ((double)pix.red/255) * 0.2126;
			double y = blueTemp + greenTemp + redTemp;
			if (y <= 0.0031308) {
				blueTemp = 12.92 * y;
				greenTemp = 12.92 * y;
				redTemp = 12.92 * y;
			} else {
				blueTemp = (1.055 * pow(y, (1/2.4))) - 0.055;
				greenTemp = (1.055 * pow(y, (1/2.4))) - 0.055;
				redTemp = (1.055 * pow(y, (1/2.4))) - 0.055;
			}
			pix.blue = blueTemp * 255;
			pix.green = greenTemp * 255;
			pix.red = redTemp * 255;
		}
		// Overwriting the old pixel vlaues  with the new pixel values
		fwrite(&pix, 1, 3, image);
	}
	fclose(image);
	return 0;
}
