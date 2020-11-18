/*
* AUTHOR: Ruben Tequida
* FILE: card_driver_dev.c
* ASSIGNMENT: Project5: /dev/cards
* COURSE: CSc352, Spring 2020
* PURPOSE: This program is a driver for the blackjack game and functions as a
* deck of 52 cards that is shuffled and becomes a new deck of cards once all
* previous 52 cards have been drawn.
*/

#include <linux/fs.h>
#include <linux/init.h>
#include <linux/miscdevice.h>
#include <linux/module.h>
#include <linux/random.h>
#include <linux/kernel.h>

#include <linux/uaccess.h>

//Global variables
char deck[52];
int element;
//function prototypes
void make_array(void);
void shuffle(void);

/*
* Purpose: Randomly generates a number between 0 and max.
*/
unsigned char get_random_byte(int max) {
	unsigned char c;
	get_random_bytes(&c, 1);
	return c%max;
}

/*
* Purpose: Properly fills out the deck array to contain 4 of each
* value held in a deck of cards.
*/
void make_array() {
	// ASCII values of A,2,3,4,5,6,7,8,9,10,J,Q,K
	char values[] = {65, 50, 51, 52, 53, 54, 55, 56, 57, 48, 74, 81, 75};
	int i = 0;
	int j = 0;
	int place = 0;
	// Do 4 times for each suite
	for (i = 0; i < 4; i++) {
		// Do 13 times for each card value
		for (j = 0; j < 13; j++) {
			deck[place] = values[j];
			place++;
		}
	}
	shuffle();
}

/*
* Swaps every element with a random element in order to "shuffle" the deck
*/
void shuffle() {
	int temp = 0;
	int rand_num = 0;
	int i= 0;
	for (i = 51; i > 0; i--) {
		rand_num = get_random_byte(i);
		temp = deck[i];
		deck[i] = deck[rand_num];
		deck[rand_num] = temp;
	}
}

/*
 * card_driver_read is the function called when a process calls read() on
 * /dev/card_driver.  It draws a card from the deck and passes the value
 * to the buffer passed in the read() call.
 */
static ssize_t card_driver_read(struct file * file, char * buf,
			  size_t count, loff_t *ppos)
{
	char card_driver_str[3];
	int len = 1;
	// Generates a new deck and shuffles it when we draw all 52 cards
	if (element == 52) {
		shuffle();
		element = 0;
	}
	// Stringifies the character to be sent to the buffer
	card_driver_str[0] = deck[element++];
	card_driver_str[1] = '\0';
	if (count < len)
		return -EINVAL;
	/*
	 * If file position is non-zero, then assume the string has
	 * been read and indicate there is no more data to be read.
	 */
	if (*ppos != 0)
		return 0;
	/*
	 * Besides copying the string to the user provided buffer,
	 * this function also checks that the user has permission to
	 * write to the buffer, that it is mapped, etc.
	 */
	if (copy_to_user(buf, card_driver_str, len))
		return -EINVAL;
	/*
	 * Tell the user how much data we wrote.
	 */
	*ppos = len;

	return len;
}

/*
 * The only file operation we care about is read.
 */

static const struct file_operations card_driver_fops = {
	.owner		= THIS_MODULE,
	.read		= card_driver_read,
	.llseek		= default_llseek,
};

static struct miscdevice card_driver_dev = {
	/*
	 * We don't care what minor number we end up with, so tell the
	 * kernel to just pick one.
	 */
	MISC_DYNAMIC_MINOR,
	/*
	 * Name ourselves /dev/hello.
	 */
	"card_driver",
	/*
	 * What functions to call when a program performs file
	 * operations on the device.
	 */
	&card_driver_fops
	};

static int __init
card_driver_init(void)
{
	int ret;
	// Initializes the count and generates a new deck
	element = 0;
	make_array();


	/*
	 * Create the "hello" device in the /sys/class/misc directory.
	 * Udev will automatically create the /dev/hello device using
	 * the default rules.
	 */
	ret = misc_register(&card_driver_dev);
	if (ret)
		printk(KERN_ERR
		       "Unable to register \"Card Driver\" misc device\n");

	return ret;
}

module_init(card_driver_init);

static void __exit
card_driver_exit(void)
{
	misc_deregister(&card_driver_dev);
}

module_exit(card_driver_exit);

MODULE_LICENSE("GPL");
MODULE_AUTHOR("Valerie Henson <val@nmt.edu>");
MODULE_DESCRIPTION("\"Hello, world!\" minimal module");
MODULE_VERSION("dev");
