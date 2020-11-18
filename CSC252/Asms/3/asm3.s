.data
MSG:	.asciiz	" <- strlen() retval\n"

.text

# Task 1: strlen(String str)
# Purpose: Return the number of characters within a given string
# @param: str - the string whose characters we will count
# @return: count - number of characters in the string
#
#REGISTERS:
# t0 = count
# t1 = null character
# t2 = current character
# t3 = address of current character
# a0 = str (param)
# v0 = count (return value)

.globl strlen
strlen:
	# Prologue
	addiu 	$sp, $sp, 	-24		# moving stack pointer 6 slots down in the stack
	sw	$fp, 0($sp)			# storing caller's frame pointer
	sw 	$ra, 4($sp)			# storing caller's return address
	addiu	$fp, $sp, 	20		# set new frame pointer to 5 slots above stack pointer
	
	add	$t0, $zero, 	$zero		# t0 = 0 (count)
	addi	$t1, $zero, 	0x0		# t1 = null
	add	$t3, $zero, 	$a0		# t3 = a0
COUNTLOOP:
	lb	$t2, 0($t3)			# t2 = current character
	beq	$t2, $t1, 	ENDLOOP		# if t0 = null go to ENDLOOP
	addi	$t0, $t0, 	1		# count++
	addi	$t3, $t3, 	1		# add 1 byte to a0 going to next character
	j 	COUNTLOOP
ENDLOOP:
	add	$v0, $zero, 	$t0		# set return register $v0 to count
	
	# Epilogue
	lw	$ra, 4($sp)			# loading caller's return address
	lw	$fp, 0($sp)			# loading caller's frame pointer
	addiu	$sp, $sp, 	24		# moving stack pointer up 6 slots in the stack
	jr	$ra				# returning to caller function
	
# Task 2: gcf(int a, int b)
# Purpose: Returns the greatest common factor between two numbers
# @param: a, b - integers values used to find gcf
# @return: gcf - interger representing the greatest common factor between a and b
#
#REGISTERS:
# t0 = temp register
# t1 = a
# t2 = b
# t3 = address of current character
# a0 = a (param)
# a1 = b (param)
# v0 = gcf (return value)

.globl gcf
gcf:
	#Prologue
	addiu 	$sp, $sp, 	-24		# moving stack pointer 6 slots down in the stack4
	sw	$fp, 0($sp)			# storing caller's frame pointer
	sw 	$ra, 4($sp)			# storing caller's return address
	addiu	$fp, $sp, 	20		# set new frame pointer to 5 slots above stack pointer
	
	add	$t1, $zero, 	$a0		# t1 = a
	add	$t2, $zero, 	$a1		# t2 = b
	slt	$t0, $t1, 	$t2		# t0 = a < b
	beq	$t0, $zero, 	SKIP1		# if (a > b) skip next statement
	add	$t0, $t1, 	$zero		# t0 = a
	add	$t1, $t2, 	$zero		# a = b
	add	$t2, $t0,	$zero		# b = a
SKIP1:
	addi	$t0, $zero, 	1		# t0 = 1
	bne	$t2, $t0,	SKIP2		# if (b != 1) skip next statement
	addi	$v0, $zero, 	1		# return 1
	j 	EPILOGUE2
SKIP2:
	div	$t1, $t2			# lo = a / b, hi = a % b
	mfhi	$t0				# t0 = a % b
	bne	$t0, $zero, 	SKIP3		# if (a % b != 0) skip next statement
	add	$v0, $zero, 	$t2		# return b
	j	EPILOGUE2
SKIP3:
	add	$a0, $zero, 	$t2		# a0 = b
	add	$a1, $zero, 	$t0		# a1 = a % b
	jal	gcf
	
EPILOGUE2:
	# Epilogue
	lw	$ra, 4($sp)			# loading caller's return address
	lw	$fp, 0($sp)			# loading caller's frame pointer
	addiu	$sp, $sp, 	24		# moving stack pointer up 6 slots in the stack
	jr	$ra				# returning to caller function

.data
MSG1: 	.asciiz " bottles of "
MSG2:	.asciiz " on the wall, "
MSG3:	.asciiz "!\nTake one down, pass it around, "
MSG4:	.asciiz " on the wall.\n\n"
MSG5:	.asciiz	"No more bottles of "
MSG6:	.asciiz " on the wall!\n\n"

.text

# Task 2: bottles(int n)
# Purpose: Prints out a phrase that represents the sequence of n, n-1, n-2, n-3,...,1, 0
# @param: n - the number of bottles to start with
# @return: None.
#
#REGISTERS:
# t0 = n
# t2 = less than result
# a0 = n (param)

.globl bottles
bottles:
	#Prologue
	addiu 	$sp, $sp, 	-24		# moving stack pointer 6 slots down in the stack4
	sw	$fp, 0($sp)			# storing caller's frame pointer
	sw 	$ra, 4($sp)			# storing caller's return address
	addiu	$fp, $sp, 	20		# set new frame pointer to 5 slots above stack pointer
	
	add	$t0, $zero, 	$a0		# t0 = count ($a0)
FORLOOP:
	slt 	$t2, $zero, 	$t0		# t2 = 0 < count
	beq	$t2, $zero, 	ENDFORLOOP	# if (i <= 0) go to ENDLOOP
	addi	$v0, $zero,	1		# print_int()
	add	$a0, $zero, 	$t0		# print_int(count)
	syscall
	addi	$v0, $zero, 	4		# print_str()
	la	$a0, MSG1			# print_str(MSG1)
	syscall
	addi	$v0, $zero, 	4		# print_str()
	add	$a0, $zero, 	$a1		# print_str(thing)
	syscall
	addi	$v0, $zero, 	4		# print_str()
	la	$a0, MSG2			# print_str(MSG2)
	syscall
	addi	$v0, $zero,	1		# print_int()
	add	$a0, $zero, 	$t0		# print_int(count)
	syscall
	addi	$v0, $zero, 	4		# print_str()
	la	$a0, MSG1			# print_str(MSG1)
	syscall
	addi	$v0, $zero, 	4		# print_str()
	add	$a0, $zero, 	$a1		# print_str(thing)
	syscall
	addi	$t0, $t0, -1			# count--
	addi	$v0, $zero, 	4		# print_str()
	la	$a0, MSG3			# print_str(MSG3)
	syscall
	addi	$v0, $zero,	1		# print_int()
	add	$a0, $zero, 	$t0		# print_int(count)
	syscall
	addi	$v0, $zero, 	4		# print_str()
	la	$a0, MSG1			# print_str(MSG1)
	syscall
	addi	$v0, $zero, 	4		# print_str()
	add	$a0, $zero, 	$a1		# print_str(thing)
	syscall
	addi	$v0, $zero, 	4		# print_str()
	la	$a0, MSG4			# print_str(MSG4)
	syscall
	j 	FORLOOP
ENDFORLOOP:
	addi	$v0, $zero, 	4		# print_str()
	la	$a0, MSG5			# print_str(MSG5)
	syscall
	addi	$v0, $zero, 	4		# print_str()
	add	$a0, $zero, 	$a1		# print_str(thing)
	syscall
	addi	$v0, $zero, 	4		# print_str()
	la	$a0, MSG6			# print_str(MSG6)
	syscall
	
	# Epilogue
	lw	$ra, 4($sp)			# loading caller's return address
	lw	$fp, 0($sp)			# loading caller's frame pointer
	addiu	$sp, $sp, 	24		# moving stack pointer up 6 slots in the stack
	jr	$ra				# returning to caller function

# Task 4: longestSorted(int *array, int len)
# Purpose: Returns the number of integers that for the longest consecutive, ascending
# line of numbers in the array
# @param: array - is an array of integers
# @param: len - is the length of the array
# @return: max_longest - an integer representing the longest consecutive, ascending
# line of numbers
#
#REGISTERS:
# t0 = len
# t1 = curr_longest
# t2 = array pointer
# t3 = i
# t4 = temp register
# t5 = int at current array position
# t6 = int at next array position
# t7 = max_longest
# a0 = array pointer (param)
# a1 = len (param)
# v0 = max_longest (return value)

.globl longestSorted
longestSorted:
	#Prologue
	addiu 	$sp, $sp, 	-24		# moving stack pointer 6 slots down in the stack4
	sw	$fp, 0($sp)			# storing caller's frame pointer
	sw 	$ra, 4($sp)			# storing caller's return address
	addiu	$fp, $sp, 	20		# set new frame pointer to 5 slots above stack pointer
	
	add	$t0, $zero, 	$a1		# t0 = len
	add	$t7, $zero, 	$zero		# t1 = 0 (max_longest)
	add	$t2, $zero, 	$a0		# t2 = a0
	beq	$t0, $zero,	EPILOGUE4	# if (len == 0) return 0
	addi	$t0, $t0, 	-1		# len-- (for looping purposes)
	add	$t3, $zero, 	$zero		# t3 = 0 (i)
	addi	$t7, $t7, 	1		# max_longest++
	addi	$t1, $zero, 	1		# t1 = 0 (curr_longest)
LOOP4:
	slt	$t4, $t3, 	$t0		# t4 = i < len - 1
	beq	$t4, $zero,	EPILOGUE4	# if (i >= len -1) go to EPILOGUE4
	lw	$t5, 0($t2)			# t5 = current int
	lw	$t6, 4($t2)			# t6 = next int
	slt	$t4, $t6, 	$t5		# t4 = t6 < t5
	bne	$t4, $zero,	RESET		# if (t6 < t5) go to RESET
	addi	$t1, $t1, 	1		# curr_longest++
	j 	PREPNEXT
RESET:
	slt	$t4, $t7, 	$t1		# t4 = max_longest < curr_longest
	beq	$t4, $zero,	SKIPSWAP	# if (max_longest > curr_longest) don't change max_longest
	add	$t7, $zero, 	$t1		# max_longest = curr_longest
SKIPSWAP:
	addi	$t1, $zero, 	1		# curr_longest = 1
PREPNEXT:
	addi	$t2, $t2, 	4		# t2 += 4 (next entry in array)
	addi	$t3, $t3,	1		# i++
	j 	LOOP4
	
EPILOGUE4:
	slt	$t4, $t7, 	$t1		# t4 = max_longest < curr_longest
	beq	$t4, $zero,	SKIPSWAP2	# if (max_longest > curr_longest) don't change max_longest
	add	$t7, $zero, 	$t1		# max_longest = curr_longest
SKIPSWAP2:
	add	$v0, $zero,	$t7		# return max_longest
	
	# Epilogue
	lw	$ra, 4($sp)			# loading caller's return address
	lw	$fp, 0($sp)			# loading caller's frame pointer
	addiu	$sp, $sp, 	24		# moving stack pointer up 6 slots in the stack
	jr	$ra				# returning to caller function

# Task 5: rotate(int count, int a, int b, int c, int d, int e, int f)
# Purpose: Rotates the values to the left and adds up the number in the left most position
# @param: count - the number of times to rotate
# @param: a,b,c,d,e,f - integers to be rotated about
# @return: retval - the summation of all the integers at the left most position after every rotate
#
#REGISTERS:
# t0 = retval
# t1 = i
# t2 = less than value
# s0 = count
# s1 = a
# s2 = b
# s3 = c
# s4 = d
# s5 = e
# s6 = f
# a0 = count (param)
# a1 = a (param)
# a2 = b (param)
# a3 = c (param)
# a4 = d (param)
# a5 = e (param)
# a6 = f (param)
# v0 = retval (return value)

.globl rotate
rotate:
	#Prologue
	addiu 	$sp, $sp, 	-36		# moving stack pointer 9 slots down in the stack4
	sw	$fp, 0($sp)			# storing caller's frame pointer
	sw 	$ra, 4($sp)			# storing caller's return address
	addiu	$fp, $sp, 	32		# set new frame pointer to 8 slots above stack pointer
	
	add	$t0, $zero, 	$zero		# t0 = 0 (retval)
	add	$t1, $zero,	$zero		# t1 = 0 (i)
	addiu	$sp, $sp,	-28		# moving stack pointer 7 more slots down to store caller's s0-6 values
	sw	$s0, 0($sp)			# s0 stored at 0($sp)
	sw	$s1, 4($sp)			# s1 stored at 4($sp)
	sw	$s2, 8($sp)			# s2 stored at 8($sp)
	sw	$s3, 12($sp)			# s3 stored at 12($sp)
	sw	$s4, 16($sp)			# s4 stored at 16($sp)
	sw	$s5, 20($sp)			# s5 stored at 20($sp)
	sw	$s6, 24($sp)			# s6 stored at 24($sp)
	add	$s0, $zero, 	$a0		# s0 = count
	add	$s1, $zero,	$a1		# s1 = a
	add	$s2, $zero,	$a2		# s2 = b
	add	$s3, $zero,	$a3		# s3 = c
	lw	$s4, 52($sp)			# s4 = d
	lw	$s5, 56($sp)			# s5 = e
	lw	$s6, 60($sp)			# s6 = f
LOOP5:
	slt 	$t2, $t1, 	$s0		# t2 = i < count
	beq	$t2, $zero,	ENDLOOP5	# if (i >= count) end the loop
	add	$a0, $zero, 	$s1		# a0 = a
	add	$a1, $zero, 	$s2		# a1 = b
	add	$a2, $zero, 	$s3		# a2 = c
	add	$a3, $zero, 	$s4		# a3 = d
	addiu	$sp, $sp,	-8		# moving stack pointer down 2 frames
	sw	$t0, 0($sp)			# storing t0 at new stack pointer
	sw	$t1, 4($sp)			# storing t1 at new stack pointer plus 4
	sw 	$s5, -8($sp)			# storing s5 onto the stack -8 bytes below the stack pointer
	sw 	$s6, -4($sp)			# storing s6 onto the stack -4 bytes below the stack pointer
	jal 	util				# calling util
	lw	$t1, 4($sp)			# restoring t1
	lw	$t0, 0($sp)			# restoring t0
	addiu	$sp, $sp,	8		# putting stack pointer back to its original posiiton
	add	$t0, $t0,	$v0		# retval += return value of util
	add	$t3, $zero, 	$s1		# temp = a
	add	$s1, $zero, 	$s2		# a = b
	add	$s2, $zero, 	$s3		# b = c
	add	$s3, $zero, 	$s4		# c = d
	add	$s4, $zero, 	$s5		# d = e
	add	$s5, $zero, 	$s6		# e = f
	add	$s6, $zero, 	$t3		# f = temp
	addi	$t1, $t1, 	1		# i++
	j 	LOOP5
ENDLOOP5:
	add	$v0, $zero,	$t0		# return retvall
	lw	$s0, 0($sp)			# s0 loaded as 0($sp)
	lw	$s1, 4($sp)			# s1 loaded as 4($sp)
	lw	$s2, 8($sp)			# s2 loaded as 8($sp)
	lw	$s3, 12($sp)			# s3 loaded as 12($sp)
	lw	$s4, 16($sp)			# s4 loaded as 16($sp)
	lw	$s5, 20($sp)			# s5 loaded as 20($sp)
	lw	$s6, 24($sp)			# s6 loaded as 24($sp)
	addiu	$sp, $sp,	28		# restoring stack pointer to original position
	
	# Epilogue
	lw	$ra, 4($sp)			# loading caller's return address
	lw	$fp, 0($sp)			# loading caller's frame pointer
	addiu	$sp, $sp, 	36		# moving stack pointer up 6 slots in the stack
	jr	$ra				# returning to caller function
