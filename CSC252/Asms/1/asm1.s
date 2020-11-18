.data

MEDIAN: .asciiz "median: "
COMPS:	.asciiz	"Comparisons: "
SPACE: 	.asciiz	" "
NEGONE: .asciiz "'one' was negative\n"
NEGTWO: .asciiz "'two' was negative\n"
NEGTHR: .asciiz "'three' was negative\n"
SUM: 	.asciiz	"sum: "
ONE: 	.asciiz	"one: "
TWO: 	.asciiz	"two: "
THREE: 	.asciiz	"three: "

.text
.globl studentMain
studentMain:
	addiu	$sp, $sp, 	-24	# allocate stack space -- default of 24 here
	sw 	$fp, 0($sp)		# save caller's frame pointer
	sw 	$ra, 4($sp) 		# save return address
	addiu 	$fp, $sp, 	20 	# setup main's frame pointer
	
	# Loading variables one, two, and three:
	la	$t7, one	# $t7 = &one
	lw	$s0, 0($t7)	# $s0 = one
	la	$t8, two	# $t8 = &two
	lw	$s1, 0($t8)	# $s0 = two
	la	$t9, three	# $t9 = &three
	lw	$s2, 0($t9)	# $s0 = threee
	
	# Task 1:
	# 
	# if (median == 1)
	# {
	# 	if (one == two || one == three)
	# 		print("median: %d\n", one);
	# 	else if (two == three)
	# 		print("median: %d\n", two);
	# 	else
	# 	{
	# 		int cmp12 = (one < two);
	# 		int cmp13 = (one < three);
	# 		int cmp23 = (two < three);
	# 		printf("Comparisons: %d %d %d\n", cmp12,cmp13,cmp23);
	# 		if (cmp12 == cmp23) // a<b<c or a>b>c
	# 			printf("median: %d\n", two);
	# 		if (cmp12 != cmp13) // b<a<c or b>a>c
	# 			printf("median: %d\n", one);
	# 		if (cmp13 != cmp23) // a<c<b or a>c>b
	# 			printf("median: %d\n", three);
	#	}
	# 	printf("\n");
	# }
	#
	# REGISTERS
	#	$s0 = one
	#	$s1 = two
	#	$s2 = three
	#	$t3 - &median
	#	$s3 - median
	#	$t0 - one < two
	#	$t1 - one < three
	#	$t2 - two < three
	#	$t4 - 1
	
	la	$t3, median		# $t3 = &median
	lw	$s3, 0($t3)		# $s3 = median
	addi 	$t4, $zero, 	1	# $t4 = 1
	bne  	$s3, $t4, 	TASK2	# if (median == 1) then...
	bne  	$s0, $s1, 	OR2EQ3	# if (one != two) test if (one == three)
	addi 	$v0, $zero, 	4	# print_str()
	la 	$a0, MEDIAN		# print_str(MEDIAN)
	syscall
	addi	$v0, $zero, 	1	# print_int()
	add	$a0, $zero,	$s0	# print_int(one)
	syscall
	j 	TASK2			# complete, jump to next task
OR2EQ3:
	bne  	$s0, $s2, 	ELSEIF	# if (one != three) test if (two == three)
	addi 	$v0, $zero, 	4	# print_str()
	la 	$a0, MEDIAN		# print_str(MEDIAN)
	syscall
	addi	$v0, $zero, 	1	# print_int()
	add	$a0, $zero, 	$s0	# print_int(one)
	syscall
	j 	ENDTASK1		# complete, jump to next task
ELSEIF:
	bne 	$s1, $s2, 	ELSE 	# if (two != three) skip to else statement
	addi 	$v0, $zero, 	4	# print_str()
	la	$a0, MEDIAN		# print_str(MEDIAN)
	syscall
	addi	$v0, $zero, 	1	# print_int()
	add	$a0, $zero,	$s1	# print_int(two)
	syscall
	j 	ENDTASK1		# complete, jump to next task
ELSE:
	slt	$t0, $s0, 	$s1	# is one < two
	slt 	$t1, $s0, 	$s2	# is one < three
	slt 	$t2, $s1, 	$s2	# is two < three
	addi 	$v0, $zero, 	4	# print_str
	la	$a0, COMPS		# print_str(COMPS)
	syscall
	addi	$v0, $zero,	1	# print_int()
	add	$a0, $zero,	$t0	# print_int($t0)
	syscall
	addi	$v0, $zero, 	4	# print_str()
	la	$a0, SPACE		# print_str(SPACE)
	syscall
	addi	$v0, $zero, 	1	# print_int()
	add	$a0, $zero,	$t1	# print_int($t1)
	syscall
	addi	$v0, $zero, 	4	# print_str()
	la	$a0, SPACE		# print_str(SPACE)
	syscall
	addi	$v0, $zero, 	1	# print_int()
	add	$a0, $zero,	$t2	# print_int($t2)
	syscall
	addi	$v0, $zero, 	11	# print_char()
	addi	$a0, $zero,	0xA	# print_char(\n)
	syscall
	bne	$t0, $t2,	COMP2	# if (one<two<three or one>two>three) continue
	addi	$v0, $zero,	4	# print_str()
	la	$a0, MEDIAN		# print_str(MEDIAN)
	syscall
	addi	$v0, $zero, 	1	# print_int()
	add	$a0, $zero,	$s1	# print_int(two)
	syscall
	j ENDTASK1			# Complete, jump to next task
COMP2:
	beq 	$t0, $t1, 	COMP3	# if (two<one<three or three<one<two) continue
	addi	$v0, $zero,	4	# print_str()
	la	$a0, MEDIAN		# print_str(MEDIAN)
	syscall
	addi	$v0, $zero, 	1	# print_int()
	add	$a0, $zero,	$s0	# print_int(one)
	syscall
	j ENDTASK1			# Complete, jump to next task
COMP3:
	beq 	$t1, $t2, 	ENDTASK1# if (one<three<two or two<three<one) continue
	addi	$v0, $zero,	4	# print_str()
	la	$a0, MEDIAN		# print_str(MEDIAN)
	syscall
	addi	$v0, $zero, 	1	# print_int()
	add	$a0, $zero,	$s2	# print_int(three)
	syscall
ENDTASK1:
	addi	$v0, $zero, 	11	# print_char()
	addi	$a0, $zero,	0xA	# print_char(\n)
	syscall
	syscall
	
	# Task 2:
	#
	# Determine if a number is negative and is it is print out which number
	# is negative and get the store the absolute value of that number in its
	# original register.
	#
	# REGISTERS
	#
	# $t7 - &one
	# $s0 - one
	# $t8 - &two
	# $s1 - two
	# $t9 - &three
	# $s2 - three
	# $t0 - one < 0
	# $t1 - two < 0
	# $t2 - three < 0
	# $t3 - &absVal
	# $s3 - absVal
	# $t4 - 1
TASK2:
	la	$t3, absVal		# $t3 = &absVal
	lw	$s3, 0($t3)		# $s3 = absVal
	bne	$t4, $s3,	TASK3	# if absVal != 1 go to next task
	slt	$t0, $s0,	$zero	# is one < 0
	slt	$t1, $s1,	$zero	# is two < 0
	slt	$t2, $s2,	$zero	# is three < 0
	beq	$t0, $zero,	IS2NEG	# if one is negative continue
	addi	$v0, $zero,	4	# print_str()
	la	$a0, NEGONE		# print_str(NEGONE)
	syscall
	sub	$s0, $zero, 	$s0	# $s0 = 0 - one, which will be positive one
	sw	$s0, 0($t7)		# store one to its address
IS2NEG:
	beq	$t1, $zero,	IS3NEG	# if two is negative continue
	addi	$v0, $zero,	4	# print_str()
	la	$a0, NEGTWO		# print_str(NEGTWO)
	syscall
	sub	$s1, $zero, 	$s1	# $s1 = 0 - two, which will be positive two
	sw	$s1, 0($t8)		# store two to its address
IS3NEG:
	beq	$t2, $zero,	ENDTASK2# if three is negative continue
	addi	$v0, $zero,	4	# print_str()
	la	$a0, NEGTHR		# print_str(NEGTHR)
	syscall
	sub	$s2, $zero, 	$s2	# $s2 = 0 - three, which will be positive three
	sw	$s2, 0($t9)		# store three to its address
ENDTASK2:
	addi	$v0, $zero, 	11	# print_char()
	addi	$a0, $zero,	0xA	# print_char(\n)
	syscall
	
	# Task 3:
	#
	# Print out the sum of all the variables.
	#
	# REGISTERS
	#
	# $s0 - one
	# $s1 - two
	# $s2 - three
	# $t3 - &sum
	# $s3 - sum
	# $t4 - 1
	# $s4 - sum of all variables
TASK3:
	la	$t3, sum		# $t3 = &sum
	lw	$s3, 0($t3)		# $s3 = sum
	bne	$t4, $s3,	TASK4	# if sum != 1 go to next task
	addi	$v0, $zero,	4	# print_str()
	la	$a0, SUM		# print_str(SUM)
	syscall
	add	$s4, $s0, 	$s1	# $s4 = one + two
	add 	$s4, $s4,	$s2	# $s4 = one + two + three
	addi 	$v0, $zero,	1	# print_int()
	add	$a0, $zero, 	$s4	# print_int($s4)
	syscall
	addi 	$v0, $zero,	11	# print_char()
	addi	$a0, $zero, 	0xA	# print_char(\n)
	syscall
	addi 	$v0, $zero,	11	# print_char()
	addi	$a0, $zero, 	0xA	# print_char(\n)
	syscall
	
	# Task 4:
	#
	# Rotate the values of all the variables.
	# move one into two, two into three, and three into one
	#
	# REGISTERS
	#
	# $s0 - one
	# $t7 - &one
	# $s1 - two
	# $t8 - &two
	# $s2 - three
	# $t9 - &three
	# $t3 - &rotate
	# $s3 - rotate
	# $t4 - 1
	# $t5 - temp holder
TASK4:
	la	$t3, rotate		# $t3 = &rotate
	lw	$s3, 0($t3)		# $s3 = rotate
	bne	$t4, $s3,	TASK5	# if rotate != 1 go to next task
	add	$t5, $zero,	$s2	# $t5 = three
	add	$s2, $zero, 	$s1	# $s2 = two
	add	$s1, $zero, 	$s0	# $s1 = one
	add	$s0, $zero, 	$t5	# $s0 = three
	sw	$s0, 0($t7)		# storing new value of one into its register
	sw	$s1, 0($t8)		# storing new value of two into its register
	sw	$s2, 0($t9)		# storing new value of three into its register
	
	# Task 4:
	#
	# Rotate the values of all the variables.
	# move one into two, two into three, and three into one
	#
	# REGISTERS
	#
	# $s0 - one
	# $s1 - two
	# $s2 - three
	# $t3 - &dump
	# $s3 - dump
	# $t4 - 1
TASK5:
	la	$t3, dump		# $t3 = &dump
	lw	$s3, 0($t3)		# $s3 = dump
	bne	$t4, $s3,	DONE	# if rotate != 1 go to next task
	addi	$v0, $zero, 	4	# print_str()
	la	$a0, ONE		# print_str(ONE)
	syscall
	addi	$v0, $zero, 	1	# print_int()
	add	$a0, $zero, 	$s0	# print_int(one)
	syscall
	addi	$v0, $zero, 	11	# print_char()
	addi	$a0, $zero,	0xA	# print_char(\n)
	syscall
	addi	$v0, $zero, 	4	# print_str()
	la	$a0, TWO		# print_str(TWO)
	syscall
	addi	$v0, $zero, 	1	# print_int()
	add	$a0, $zero, 	$s1	# print_int(two)
	syscall
	addi	$v0, $zero, 	11	# print_char()
	addi	$a0, $zero,	0xA	# print_char(\n)
	syscall
	addi	$v0, $zero, 	4	# print_str()
	la	$a0, THREE		# print_str(THREE)
	syscall
	addi	$v0, $zero, 	1	# print_int()
	add	$a0, $zero, 	$s2	# print_int(three)
	syscall
	addi	$v0, $zero, 	11	# print_char()
	addi	$a0, $zero,	0xA	# print_char(\n)
	syscall
	addi 	$v0, $zero,	11	# print_char()
	addi	$a0, $zero, 	0xA	# print_char(\n)
	syscall
	
DONE:
	lw 	$ra, 4($sp) 	# get return address from stack
	lw 	$fp, 0($sp) 	# restore the caller's frame pointer
	addiu 	$sp, $sp, 24 	# restore the caller's stack pointer
	jr 	$ra 		# return to caller's code