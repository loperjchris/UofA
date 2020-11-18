.data
INTRO:		.asciiz "Selected which program you would like to run, then press 'enter'\n"
PRGM1:		.asciiz "1. Instrument Keyboard\n"
PRGM2:		.asciiz "2. Number Guessing Game\n"
INTRO_1:	.asciiz "This program will turn your keyboard into an instrumental keyboard.\n"
INTRO2_1:	.asciiz "Once you have selected an instrument press any key to play a note.\n"
INTRO3_1:	.asciiz	"To end the program press the 'enter' key\n\n"
INTRO4_1: 	.asciiz "Type the number of the instrument you want and then press enter\n"
BEGIN: 		.asciiz "Begin typing to play notes, hit 'enter' to exit\n"
INVLD:		.asciiz	"Please, select a valid number\n"
REPEAT:		.asciiz	"Would you like to start over?\n"
OPT0:		.asciiz "0: Piano\n"
OPT1:		.asciiz "1: Chromatic Percussion\n"
OPT2:		.asciiz "2: Organ\n"
OPT3:		.asciiz "3: Guitar\n"
OPT4:		.asciiz "4: Bass\n"
OPT5:		.asciiz "5: Strings\n"
OPT6:		.asciiz "6: Ensemble\n"
OPT7:		.asciiz "7: Brass\n"
OPT8:		.asciiz "8: Reed\n"
OPT9:		.asciiz "9: Pipe\n"
OPT10:		.asciiz "10: Synth Lead\n"
OPT11:		.asciiz "11: Synth Pad\n"
OPT12:		.asciiz "12: Synth Effects\n"
OPT13:		.asciiz "13: Ethnic\n"
OPT14:		.asciiz "14: Percussion\n"
OPT15:		.asciiz "15: Sound Effects\n"
GUESS_MSG:	.asciiz	"Guess a number between 1 and 100\nEnter the number you would like to guess:\n"
OUT_BOUNDS:	.asciiz "Invalid guess.\n"
LOW:		.asciiz	"Your guess is too low, guess again:\n"
HIGH:		.asciiz	"Your guess is too high, guess again:\n"
RIGHT:		.asciiz "Your guess the correct number!!!\n"
YES:		.asciiz "Yes\n"
NO:		.asciiz "No\n"

.text
# Main
# Purpose: Allows the user to select between running the instrument keyboard
# or the number guessing game
# 
# REGISTERS:
# t0 = integer representing user selection
# t1 = temp variable
.globl main
main: 
	addi	$v0, $zero,	4	# print_str()
	la	$a0, INTRO		# print_str(INTRO)
	syscall
	la	$a0, PRGM1		# print_str(PRGM1)
	syscall
	la	$a0, PRGM2		# print_str(PRGM2)
	syscall
	addi	$v0, $zero, 	5	# Reading in an integer
	syscall
	add	$t0, $zero, 	$v0	# t0 = typed number
CHECK:
	addi	$t1, $zero, 	1	# t1 = 1
	slt	$t1, $t0, 	$t1	# t1 = t0 < 1
	bne	$t1, $zero,	I_MSG	# if selected number is less than zero print invalid message and prompt user again
	addi	$t1, $zero, 	2	# t1 = 15
	slt	$t1, $t1,	$t0	# t1 = 15 < t0
	bne	$t1, $zero,	I_MSG	# if selected number is greater than 15 print invalid message and prompt user again
	j	GOOD			# exit loop
I_MSG:
	addi	$v0, $zero,	4	# print_str()
	la	$a0, INVLD		# print_str(INTRO)
	syscall
	la	$a0, PRGM1		# print_str(PRGM1)
	syscall
	la	$a0, PRGM2		# print_str(PRGM2)
	syscall
	addi	$v0, $zero, 	5	# Reading in an integer
	syscall
	add	$t0, $zero, 	$v0	# t0 = typed number
	j 	CHECK			# repeat CHECK loop
GOOD:
	addi	$t1, $zero,	1	# t1 = 1
	beq	$t0, $t1,	RUN1	# if (t0 == 1) RUN1
	j	RUN2			# else: RUN2

# RUN1: Instrument Keyboard
# Purpose: Allows the user to select an instrument and then turns the keyboard into 
# an instrument that is scaled by numberical and alphabetical order
#
# REGISTERS:
# t0 = user selected input for instrument
# t1 = temp variable
# t2 = escape character
# a0 = pitch
# a1 = duration
# a2 = instrument
# a3 = volume
RUN1:
	addi	$v0, $zero,	4	# print_str()
	la	$a0, INTRO_1		# print_str(INTRO_1)
	syscall
	la	$a0, INTRO2_1		# print_str(INTRO_1)
	syscall
	la	$a0, INTRO3_1		# print_str(INTRO_1)
	syscall
	la	$a0, INTRO4_1		# print_str(INTRO_1)
	syscall
	la	$a0, OPT0		# print_str(OPT0)
	syscall
	la	$a0, OPT1		# print_str(OPT1)
	syscall
	la	$a0, OPT2		# print_str(OPT2)
	syscall
	la	$a0, OPT3		# print_str(OPT3)
	syscall
	la	$a0, OPT4		# print_str(OPT4)
	syscall
	la	$a0, OPT5		# print_str(OPT5)
	syscall
	la	$a0, OPT6		# print_str(OPT6)
	syscall
	la	$a0, OPT7		# print_str(OPT7)
	syscall
	la	$a0, OPT8		# print_str(OPT8)
	syscall
	la	$a0, OPT9		# print_str(OPT9)
	syscall
	la	$a0, OPT10		# print_str(OPT10)
	syscall
	la	$a0, OPT11		# print_str(OPT11)
	syscall
	la	$a0, OPT12		# print_str(OPT12)
	syscall
	la	$a0, OPT13		# print_str(OPT13)
	syscall
	la	$a0, OPT14		# print_str(OPT14)
	syscall
	la	$a0, OPT15		# print_str(OPT15)
	syscall
	addi	$v0, $zero, 	5	# Reading in an integer
	syscall
	add	$t0, $zero, 	$v0	# t0 = typed number
VALIDATE:
	slt	$t1, $t0, 	$zero	# t1 = t0 < 0
	bne	$t1, $zero,	IV_MSG	# if selected number is less than zero print invalid message and prompt user again
	addi	$t1, $zero, 	15	# t1 = 15
	slt	$t1, $t1,	$t0	# t1 = 15 < t0
	bne	$t1, $zero,	IV_MSG	# if selected number is greater than 15 print invalid message and prompt user again
	addi	$v0, $zero,	4	# print_str()
	la	$a0, BEGIN		# print_str(BEGIN)
	syscall
	j	VALID			# exit loop
IV_MSG:
	addi	$v0, $zero,	4	# print_str()
	la	$a0, INVLD		# print_str(INTRO)
	syscall
	la	$a0, OPT0		# print_str(OPT0)
	syscall
	la	$a0, OPT1		# print_str(OPT1)
	syscall
	la	$a0, OPT2		# print_str(OPT2)
	syscall
	la	$a0, OPT3		# print_str(OPT3)
	syscall
	la	$a0, OPT4		# print_str(OPT4)
	syscall
	la	$a0, OPT5		# print_str(OPT5)
	syscall
	la	$a0, OPT6		# print_str(OPT6)
	syscall
	la	$a0, OPT7		# print_str(OPT7)
	syscall
	la	$a0, OPT8		# print_str(OPT8)
	syscall
	la	$a0, OPT9		# print_str(OPT9)
	syscall
	la	$a0, OPT10		# print_str(OPT10)
	syscall
	la	$a0, OPT11		# print_str(OPT11)
	syscall
	la	$a0, OPT12		# print_str(OPT12)
	syscall
	la	$a0, OPT13		# print_str(OPT13)
	syscall
	la	$a0, OPT14		# print_str(OPT14)
	syscall
	la	$a0, OPT15		# print_str(OPT15)
	syscall
	addi	$v0, $zero, 	5	# Reading in an integer
	syscall
	add	$t0, $zero, 	$v0	# t0 = typed number
	j 	VALIDATE		# start loop over
VALID:
	addi	$v0, $zero,	12	# Reading in next character pressed
	syscall
	add	$t1, $zero,	$v0	# t1 = key pressed by user
	addi	$t2, $zero,	0xA	# t2 = enter
	beq	$t2, $t1,	EXIT	# if (key pressed == esc) then exit
	addi	$t1, $t1,	100	# adding 100 to key to shift pitch
	div	$t2, $t1, 	128	# t2 = value pressed / 128 (number of pitches accepted)
	mfhi	$t2			# t2 = value pressed % 128
	add	$a0, $zero, 	$t2	# pitch = t2
	addi	$a1, $zero, 	500	# duration = .5 secs
	add	$a2, $zero,	$t0	# instrument = user selected instrument
	addi	$a3, $zero,	100	# volume = 100
	add	$v0, $zero, 	31	# Calling MIDI out to generate a tone
	syscall
	j 	VALID
	
# RUN2: Number Guessing Game
# Purpose: Generates a random number and has the user guess it.
# Displays too low if the guess is too low and too high if the guess it too high
# Ends when the number is guessed correctly
#
# REGISTERS:
# t0 = random number
# t1 = user guess
# t2 = temp variable
# t3 = temp variable
RUN2:
	addi	$a1, $zero, 	100	# a1 = 100 (random number upper bound)
	addi	$v0, $zero,	42	# Calling random int range generator
	syscall
	add	$t0, $zero, 	$a0	# t0 = random number
GUESS:
	addi	$v0, $zero, 	4	# print_str()
	la	$a0, GUESS_MSG		# print_str(GUESS_MSG)
	syscall
	addi	$v0, $zero, 	5	# Read in user number
	syscall
	add	$t1, $zero,	$v0	# t1 = user guess
	addi	$t3, $zero,	1	# t3 = 1
	slt	$t3, $t1,	$t3	# t3 = guess < 1
	bne	$t3, $zero,	OUT	# if (guess < 1)
	addi	$t3, $zero,	100	# t3 = 100
	slt 	$t3, $t3,	$t1	# t3 = 100 < guess
	bne	$t3, $zero,	OUT	# if (guess > 100)
	beq	$t1, $t0,	CORRECT	# if guess == answer 
	slt	$t2, $t1, 	$t0	# t2 = guess < answer
	bne	$t2, $zero,	TOO_LOW	# if (guess < answer) too low
	addi	$v0, $zero, 	4	# print_str()
	la	$a0, HIGH		# print_str(HIGH)
	syscall
	j 	GUESS
TOO_LOW:
	addi	$v0, $zero, 	4	# print_str()
	la	$a0, LOW		# print_str(LOW)
	syscall
	j 	GUESS
OUT:
	addi	$v0, $zero, 	4	# print_str()
	la	$a0, OUT_BOUNDS		# print_str(OUT_BOUNDS)
	syscall
	j 	GUESS
CORRECT:
	addi	$v0, $zero, 	4	# print_str()
	la	$a0, RIGHT		# print_str(HIGH)
	syscall

# Ask player if they would like to start program over agian
EXIT:
	addi	$v0, $zero,	4	# print_str()
	la	$a0, REPEAT		# print_str(REPEAT)
	syscall
	addi	$v0, $zero, 	50
	syscall
	beq	$a0, $zero, 	main	# if yes jump to main
	
	
	