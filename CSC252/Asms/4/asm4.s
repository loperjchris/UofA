.text

# Task 1: turtle_init(Turtle *obj , char *name)
# Purpose: Initializes a turtle object with a passed in name, (0,0) coordinates, 
# facing north, and an odometer odometer of 0.
# @param: *obj - a pointer to the memory array that holds the turtle "object"
# @param: *name - a pointer to the name string
# @return: None.
#
# REGISTERS:
# t0 = turtle object (first arguement)
# t1 = name
# t2 = 0

.globl turtle_init
turtle_init:
	# Prologue
	addiu 	$sp, $sp, 	-24		# moving stack pointer 6 slots down in the stack
	sw	$fp, 0($sp)			# storing caller's frame pointer
	sw 	$ra, 4($sp)			# storing caller's return address
	addiu	$fp, $sp, 	20		# set new frame pointer to 5 slots above stack pointer
	
	add	$t0, $a0,	$zero		# t0 = turtle object
	add	$t1, $a1,	$zero		# t1 = name
	sw	$t1, 4($t0)			# setting turtle's name (byte 4 of the turtle array) to t1
	add	$t2, $zero, 	$zero		# t2 = 0
	sb	$t2, 0($t0)			# turtle x = 0
	sb	$t2, 1($t0)			# turtle y = 0
	sb	$t2, 2($t0)			# turtle dir = 0
	sw	$t2, 8($t0)			# turtle odom = 0
	
	# Epilogue
	lw	$ra, 4($sp)			# loading caller's return address
	lw	$fp, 0($sp)			# loading caller's frame pointer
	addiu	$sp, $sp, 	24		# moving stack pointer up 6 slots in the stack
	jr	$ra				# returning to caller function
	
.data
TURTLE:	.asciiz	"Turtle \""
POS:	.asciiz "\"\n  pos "
DIR:	.asciiz "\n  dir "
NORTH:	.asciiz "North\n"
SOUTH:	.asciiz "South\n"
WEST:	.asciiz "West\n"
EAST:	.asciiz "East\n"
ODOM:	.asciiz	"  odometer "

.text
# Task 2: turtle_debut(Turtle *obj)
# Purpose: Prints out the name, coordinates, facing direction, and odometer of 
# the passed in turtle
# @param: *obj - a pointer to the memory array that holds the turtle "object"
# @return: None.
#
# REGISTERS:
# t0 = turtle object (first arguement)
# t1 = turtle.name
# t2 = temp holder to test direction

.globl turtle_debug
turtle_debug:
	# Prologue
	addiu 	$sp, $sp, 	-24		# moving stack pointer 6 slots down in the stack
	sw	$fp, 0($sp)			# storing caller's frame pointer
	sw 	$ra, 4($sp)			# storing caller's return address
	addiu	$fp, $sp, 	20		# set new frame pointer to 5 slots above stack pointer
	
	add	$t0, $zero,	$a0		# t0 = turtle object
	addi	$v0, $zero,	4		# print_str()
	la	$a0, TURTLE			# print_str(TURTLE)
	syscall
	lw	$a0, 4($t0)			# print_str(Turtle.name)
	syscall
	la	$a0, POS			# print_str(POS)
	syscall
	addi	$v0, $zero, 	1		# print_int()
	lb	$a0, 0($t0)			# print_int(turtle.x)
	syscall
	addi	$v0, $zero, 	11		# print_char()
	addi	$a0, $zero, 	0x2C		# print_char(,)
	syscall
	addi	$v0, $zero, 	1		# print_int()
	lb	$a0, 1($t0)			# print_int(turtle.x)
	syscall
	addi	$v0, $zero, 	4		# print_str()
	la	$a0, DIR			# print_str(DIR)
	syscall	
	lb	$t1, 2($t0)			# t1 = turtle.dir
	bne	$t1, $zero, 	TRY1		# if (dir == 0)
	la	$a0, NORTH			# print_str(NORTH)
	syscall
	j	END_DIR
TRY1:
	addi	$t2, $zero, 	1		# t2 = 1
	bne	$t1, $t2,	TRY2		# else if (dir == 1)
	la	$a0, EAST			# print_str(EAST)
	syscall
	j	END_DIR
TRY2:
	addi	$t2, $zero, 	2		# t2 = 2
	bne	$t1, $t2,	TRY3		# else if (dir == 1)
	la	$a0, SOUTH			# print_str(SOUTH)
	syscall
	j	END_DIR
TRY3:
	la	$a0, WEST			# print_str(WEST)
	syscall
END_DIR:
	la	$a0, ODOM			# print_str(ODOM)
	syscall
	addi	$v0, $zero, 	1		# print_int()
	lw	$a0, 8($t0)			# print_int(turtle.odom)
	syscall
	addi	$v0, $zero, 	11		# print_char()
	addi	$a0, $zero, 	0xA		# print_char(\n)
	syscall
	syscall
	
	# Epilogue
	lw	$ra, 4($sp)			# loading caller's return address
	lw	$fp, 0($sp)			# loading caller's frame pointer
	addiu	$sp, $sp, 	24		# moving stack pointer up 6 slots in the stack
	jr	$ra				# returning to caller function
	
# Task 3.1: turtle_turnLeft(Turtle *obj)
# Purpose:  Makes the turtle face the direction to its left
# @param: *obj - a pointer to the memory array that holds the turtle "object"
# @return: None.
#
# REGISTERS:
# t0 = turtle.dir
# t1 = name

.globl turtle_turnLeft
turtle_turnLeft:
	# Prologue
	addiu 	$sp, $sp, 	-24		# moving stack pointer 6 slots down in the stack
	sw	$fp, 0($sp)			# storing caller's frame pointer
	sw 	$ra, 4($sp)			# storing caller's return address
	addiu	$fp, $sp, 	20		# set new frame pointer to 5 slots above stack pointer
	
	lb	$t0, 2($a0)			# t0 = turtle.dir
	addi	$t0, $t0, 	-1		# dir--
	andi	$t0, $t0,	0x3		# dir = dir & 3 to get bottom two bits (dir % 4)
	sb	$t0, 2($a0)			# turtle.dir = t0
	
	# Epilogue
	lw	$ra, 4($sp)			# loading caller's return address
	lw	$fp, 0($sp)			# loading caller's frame pointer
	addiu	$sp, $sp, 	24		# moving stack pointer up 6 slots in the stack
	jr	$ra				# returning to caller function
	
# Task 3.2: turtle_turnRight(Turtle *obj)
# Purpose:  Makes the turtle face the direction to its right
# @param: *obj - a pointer to the memory array that holds the turtle "object"
# @return: None.
#
# REGISTERS:
# t0 = turtle.dir
# t1 = name	
	
.globl turtle_turnRight
turtle_turnRight:
	# Prologue
	addiu 	$sp, $sp, 	-24		# moving stack pointer 6 slots down in the stack
	sw	$fp, 0($sp)			# storing caller's frame pointer
	sw 	$ra, 4($sp)			# storing caller's return address
	addiu	$fp, $sp, 	20		# set new frame pointer to 5 slots above stack pointer
	
	lb	$t0, 2($a0)			# t0 = turtle.dir
	addi	$t0, $t0, 	1		# dir++
	andi	$t0, $t0,	0x3		# dir = dir & 3 to get bottom two bits (dir % 4)
	sb	$t0, 2($a0)			# turtle.dir = t0
	
	# Epilogue
	lw	$ra, 4($sp)			# loading caller's return address
	lw	$fp, 0($sp)			# loading caller's frame pointer
	addiu	$sp, $sp, 	24		# moving stack pointer up 6 slots in the stack
	jr	$ra				# returning to caller function
	
# Task 4: turtle_move(Turtle *obj, int dist)	
# Purpose:  Moves the turtle the given distance in the direction it's facing
# and clamps to the edge if it hits the edge.
# @param: *obj - a pointer to the memory array that holds the turtle "object"
# @param: dist - the number of steps the turtle should take
# @return: None.
#
# REGISTERS:
# t0 = turtle.dir
# t1 = turtle.x
# t2 = turtle.y
# t3 = max range + 1
# t4 = turtle.dist
	
.globl turtle_move
turtle_move:
	# Prologue
	addiu 	$sp, $sp, 	-24		# moving stack pointer 6 slots down in the stack
	sw	$fp, 0($sp)			# storing caller's frame pointer
	sw 	$ra, 4($sp)			# storing caller's return address
	addiu	$fp, $sp, 	20		# set new frame pointer to 5 slots above stack pointer
	
	lb	$t0, 2($a0)			# t0 = turtle.dir
	lb	$t1, 0($a0)			# t1 = turtle.x
	lb	$t2, 1($a0)			# t2 = turtle.y
	bne	$t0, $zero,	GOING_E		# if (dir == 0)
	add	$t2, $t2, 	$a1		# y += dist
	addi	$t3, $zero,	11		# t3 = 11
	slt	$t3, $t2, 	$t3		# y < 11
	bne	$t3, $zero,	N_NEG		# if (y >= 11) else check negative
	addi	$t2, $zero, 	10		# y = 10
	j	CALC_ODOM
N_NEG:
	addi	$t3, $zero,	-11		# t3 = -11
	slt 	$t3, $t3,	$t2		# -11 < y
	bne	$t3, $zero,	CALC_ODOM	# if (y <= -11)
	addi	$t2, $zero,	-10		# y = -10
	j	CALC_ODOM
GOING_E:
	addi	$t3, $zero,	1		# t3 = 1
	bne	$t0, $t3,	GOING_S		# if (dir == 1)
	add	$t1, $t1,	$a1		# x += dist
	addi	$t3, $zero,	11		# t3 = 11
	slt	$t3, $t1,	$t3		# x < 11
	bne	$t3, $zero,	E_NEG		# if (x >= 11) else check negative
	addi	$t1, $zero,	10		# x = 10
	j 	CALC_ODOM
E_NEG:
	addi 	$t3, $zero,	-11		# t3 = -11
	slt	$t3, $t3,	$t1		# -11 < x
	bne	$t3, $zero,	CALC_ODOM	# if (x <= -11)
	addi	$t1, $zero,	-10		# x = -10
	j 	CALC_ODOM
GOING_S:
	addi	$t3, $zero,	2		# t3 = 2
	bne	$t0, $t3,	GOING_W		# if (dir == 2)
	sub	$t2, $t2,	$a1		# y -= dist
	addi	$t3, $zero,	11		# t3 = 11
	slt	$t3, $t2,	$t3		# y < 11
	bne	$t3, $zero,	S_NEG		# if (y >= 11) else check negative
	addi	$t2, $zero,	10		# y = 10
	j 	CALC_ODOM
S_NEG:
	addi 	$t3, $zero,	-11		# t3 = -11
	slt	$t3, $t3,	$t2		# -11 < y
	bne	$t3, $zero,	CALC_ODOM	# if (y <= -11)
	addi	$t2, $zero,	-10		# y = -10
	j 	CALC_ODOM
GOING_W:
	sub	$t1, $t1,	$a1		# x -= dist
	addi	$t3, $zero,	11		# t3 = 11
	slt	$t3, $t1,	$t3		# x < 11
	bne	$t3, $zero,	W_NEG		# if (x >= 11) else check negative
	addi	$t1, $zero,	10		# x = 10
	j 	CALC_ODOM
W_NEG:
	addi 	$t3, $zero,	-11		# t3 = -11
	slt	$t3, $t3,	$t1		# -11 < x
	bne	$t3, $zero,	CALC_ODOM	# if (x <= -11)
	addi	$t1, $zero,	-10		# x = -10
	j 	CALC_ODOM
CALC_ODOM:
	lb	$t4, 0($a0)			# t4 = turtle.x
	add	$t4, $zero, 	$t1		# x = t1
	sb	$t4, 0($a0)			# turtle.x = t4
	lb	$t4, 1($a0)			# t4 = turtle.y
	add	$t4, $zero,	$t2		# y = t2
	sb	$t4, 1($a0)			# turtle.y = t4
	lw	$t4, 8($a0)			# t4 = turtle.odom
	slt	$t3, $zero,	$a1		# 0 < dist
	beq	$t3, $zero,	NEG		# if (dist > 0)
	add	$t4, $t4,	$a1		# odom += dist
	j 	DONE_4
NEG:
	sub	$t4, $t4,	$a1		# odom -= dist
DONE_4:
	sw	$t4, 8($a0)			# turtle.odom = t4
	
	# Epilogue
	lw	$ra, 4($sp)			# loading caller's return address
	lw	$fp, 0($sp)			# loading caller's frame pointer
	addiu	$sp, $sp, 	24		# moving stack pointer up 6 slots in the stack
	jr	$ra				# returning to caller function
	
# Task 5: turtle_searchName(Turtle *arr, int arrLen, char *needle)
# Purpose:  Searches for a turtle with the passed in name
# @param: *arr - memory array of turtle objects
# @param: arrLen - the lenth of the turtle array
# @param: *needle - pointer to the first character in the name being searched
# @return: the index in the array if turtle found, -1 if not found.
#
# REGISTERS:
# t0 = i
# t1 = &arr
# t2 = temp
# t3 = return value

.globl turtle_searchName
turtle_searchName:
	# Prologue
	addiu 	$sp, $sp, 	-24		# moving stack pointer 6 slots down in the stack
	sw	$fp, 0($sp)			# storing caller's frame pointer
	sw 	$ra, 4($sp)			# storing caller's return address
	addiu	$fp, $sp, 	20		# set new frame pointer to 5 slots above stack pointer
	
	addi	$t3, $zero,	-1		# t3 = -1
	addi	$t0, $zero, 	0		# i = 0
	add 	$t1, $zero, 	$a0		# t1 = &arr
START_LOOP:
	slt	$t2, $t0, 	$a1		# i < arrLen
	beq	$t2, $zero,	DONE_5		# if (i < arrLen)
	add	$t2, $zero,	$t1		# turtle at arr[i]
	lw	$t2, 4($t2)			# t2 = turtle.name
	addiu	$sp, $sp,	-20		# moving stack pointer 5 slots down in the stack
	sw	$a1, 16($sp)			# adding a1 to the stack
	sw	$t0, 12($sp)			# adding t0 to the stack
	sw	$t1, 8($sp)			# adding t1 to the stack
	sw	$t2, 4($sp)			# adding t2 to the stack
	sw	$t3, 0($sp)			# adding t3 to the stack
	add	$a0, $zero,	$t2		# a0 = turtle.name of curr turtle
	add	$a1, $zero,	$a2		# a1 = *needle
	jal	strcmp				# strcmp(turtle.name, needle)
	lw	$t3, 0($sp)			# loading a1 from stack
	lw	$t2, 4($sp)			# loading a1 from stack
	lw	$t1, 8($sp)			# loading a1 from stack
	lw	$t0, 12($sp)			# loading a1 from stack
	lw	$a1, 16($sp)			# loading a1 from stack
	addiu	$sp, $sp, 	20		# moving stack pointer up 5 slots in the stack
	bne	$v0, $zero,	NOT_FOUND	# if (turtle.name == needle)
	add	$t3, $zero,	$t0		# t3 = i
	j 	DONE_5
NOT_FOUND:
	addi	$t0, $t0, 	1		# i++
	addi	$t1, $t1,	12		# t1 += 12 going to next turtle
	j	START_LOOP
DONE_5:
	add	$v0, $zero,	$t3		# v0 = t3
	
	# Epilogue
	lw	$ra, 4($sp)			# loading caller's return address
	lw	$fp, 0($sp)			# loading caller's frame pointer
	addiu	$sp, $sp, 	24		# moving stack pointer up 6 slots in the stack
	jr	$ra				# returning to caller function
	
# Task 6: turtle_sortByX_indirect(Turtle **arr, int arrLen)
# Purpose:  sorts each turlte in the array by their x position
# @param: **arr - a memory array where each position points to another pointer
# to a turlte object array
# @param: arrLen - the length of the pointer memory array
# @return: None.
#
# REGISTERS:
# t0 = arrLen
# t1 = i
# t2 = temp
# t3 = j
# t4 = **arr pointer for i loop
# t5 = **arr pointer for j loop
# t6 = turtle at j position
# t7 = min turtle's x position
# t8 = temp
# t9 = address of min turtle

.globl turtle_sortByX_indirect
turtle_sortByX_indirect:
	# Prologue
	addiu 	$sp, $sp, 	-24		# moving stack pointer 6 slots down in the stack
	sw	$fp, 0($sp)			# storing caller's frame pointer
	sw 	$ra, 4($sp)			# storing caller's return address
	addiu	$fp, $sp, 	20		# set new frame pointer to 5 slots above stack pointer
	
	add	$t0, $zero,	$a1		# t0 = arrLen
	add	$t1, $zero,	$zero		# i = 0
	add	$t4, $zero, 	$a0			# t4 = **arr
LOOP1:
	slt 	$t2, $t1,	$t0		# t2 = i < arrLen
	beq	$t2, $zero,	END_LOOP1	# if (i < arrLen)
	add	$t3, $zero,	$t1		# j = i
	add	$t5, $zero,	$t4		# t5 = arr[j] = arr[i]
	lw	$t9, 0($t5)			# t9 = turtle at i
	lb 	$t7, 0($t9)			# t7 = turtle.x at index i (min turtle.x)
LOOP2:
	slt	$t2, $t3,	$t0		# j < arrLen
	beq	$t2, $zero,	END_LOOP2	# if (j < arrLen)
	lw	$t6, 0($t5)			# t6 = turtle at j
	lb	$t2, 0($t6)			# t6 = turtle.x at index j
	slt	$t8, $t7, 	$t2		# min turtle.x < curr turtle.x
	bne	$t8, $zero,	IS_LESS	# if (curr turtle.x < min turtle.x)
	add	$t9, $zero,	$t5		# new address of pointer to min turtle
	add	$t7, $zero,	$t2		# min turtle.x = curr turtle.x
IS_LESS:
	addi	$t3, $t3,	1		# j++
	addi	$t5, $t5,	4		# t5 += 4 (moving to next turtle in j loop)
	j 	LOOP2
END_LOOP2:
	lw	$t2, 0($t4)			# t2 = address of turtle at i
	lw	$t3, 0($t9)			# t3 = address of min turtle
	sw	$t3, 0($t4)			# arr[i] = address of new min turtle
	sw	$t2, 0($t9)			# arr[t9] = address of turtle i
	addi	$t1, $t1,	1		# i++
	addi	$t4, $t4,	4		# t4 += 4 (moving to next turtle in i loop)
	j 	LOOP1
END_LOOP1:
	# Epilogue
	lw	$ra, 4($sp)			# loading caller's return address
	lw	$fp, 0($sp)			# loading caller's frame pointer
	addiu	$sp, $sp, 	24		# moving stack pointer up 6 slots in the stack
	jr	$ra				# returning to caller function
	
	