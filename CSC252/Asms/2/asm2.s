.data

FIB:	.asciiz	"Fibonacci Numbers:\n"
FIB0:	.asciiz	"  0: 1\n"
FIB1:	.asciiz	"  1: 1\n"
COLSPACE:
	.asciiz ": "
RUN:	.asciiz	"Run Check: "
ASC:	.asciiz "ASCENDING\n"
DESC: 	.asciiz "DESCENDING\n"
NEITHER:
	.asciiz	"NEITHER\n"
SWAP:	.asciiz	"String successfully swapped!\n\n"

.text
.globl studentMain
studentMain:
	addiu	$sp, $sp, 	-24	# allocate stack space -- default of 24 here
	sw 	$fp, 0($sp)		# save caller's frame pointer
	sw 	$ra, 4($sp) 		# save return address
	addiu 	$fp, $sp, 	20 	# setup main's frame pointer
	
	# Task 1: Fibonacci
	# if (fib != 0) {
	#	printf("Fibonacci Numbers:\n");
	#	printf(" 0: 1\n");
	#	printf(" 1: 1\n");
	#	int prev = 1, beforeThat = 1;
	#	int n = 2;
	#	while (n <= fib) {
	#		int cur = prev+beforeThat;
	#		printf(" %d: %d\n", n, cur);
	#		n++;
	#		beforeThat = prev;
	#		prev = cur;
	#	}
	#	printf("\n");
	# }
	#
	# REGISTERS:
	# $t0 = fib
	# $s0 = prev
	# $s1 = beforeThat
	# $s2 = n
	# $s3 = prev + beforeThat
	# $t1 = fib < n
	
	la 	$t0, fib			# $t0 = &fib
	lw	$t0, 0($t0)			# $t0 = fib
	beq	$t0, $zero, 	ENDTASK1	# if (fib == 0) go to ENDTASK1
	addi	$v0, $zero, 	4		# print_str()
	la 	$a0, FIB			# print_str(FIB)
	syscall
	la	$a0, FIB0			# print_str(FIB0)
	syscall
	la	$a0, FIB1			# print_str(FIB1)
	syscall
	addi	$s0, $zero, 	1		# $s0 = 1 (prev)
	addi	$s1, $zero, 	1		# $s1 = 1 (beforeThat)
	addi	$s2, $zero,	2		# $s2 = 2 (n)
WHILE1:
	slt	$t1, $t0, 	$s2		# $t1 = fib < n
	bne	$t1, $zero, 	OVERFIB		# if n > fib go to OVERFIB
	add	$s3, $s0, 	$s1		# $s3 = prev + beforeThat (cur)
	addi 	$v0, $zero, 	11		# print_char()
	addi	$a0, $zero, 	0x20		# print_char(' ')
	syscall
	addi 	$v0, $zero, 	11		# print_char()
	addi	$a0, $zero, 	0x20		# print_char(' ')
	syscall
	addi 	$v0, $zero, 	1		# print_int()
	add	$a0, $zero, 	$s2		# print_int(n)
	syscall
	addi	$v0, $zero, 	4		# print_str()
	la	$a0, COLSPACE			# print_str(COLSPACE)
	syscall
	addi 	$v0, $zero, 	1		# print_int()
	add 	$a0, $zero, 	$s3		# print_int(cur)
	syscall
	addi 	$v0, $zero, 	11		# print_char()
	addi	$a0, $zero, 	0xA		# print_char(\n)
	syscall
	addi	$s2, $s2, 1			# n++
	add	$s1, $zero, 	$s0		# beforeThat = prev
	add	$s0, $zero, 	$s3		# prev = cur
	j 	WHILE1				# start while loop over
OVERFIB:
	addi 	$v0, $zero, 	11		# print_char()
	addi	$a0, $zero, 	0xA		# print_char(\n)
	syscall
ENDTASK1:
	
	# Task 2: Square
	# if (square != 0) {
	#	// NOTE: square_fill is a byte
	#	// NOTE: square_size is a word
	#	for (int row=0; row < square_size; row++) {
	#		char lr, mid;
	#		if (row == 0 || row == square_size-1) {
	#			lr = '+';
	#			mid = '-';
	#		} else {
	#			lr = '|';
	#			mid = square_fill;
	#		}
	#		printf("%c", lr);
	#		for (int i=1; i<square_size-1; i++) {
	#			printf("%c", mid);
	#		}
	#		printf("%c\n", lr);
	#	}
	#	printf("\n");
	# }
	#
	# REGISTERS:
	# $t0 = square
	# $t1 = square_fill
	# $t2 = square_size
	# $t3 = row < square_size
	# $t4 = lr
	# $t5 = mid
	# $t6 = i
	# $t7 = i < square_size - 1
	# $s0 = row
	# $s1 = square_size - 1
	# $s2 = 1
	
	la	$t0, square			# $t0 = &square
	lw	$t0, 0($t0)			# $t0 = square
	beq	$t0, $zero, 	ENDTASK2	# if (square == zero) go to ENDTASK2
	la	$t1, square_fill		# $t1 = &square_fill
	lb	$t1, 0($t1)			# $t1 = square_fill
	la	$t2, square_size		# $t1 = &square_size
	lw	$t2, 0($t2)			# $t1 = square_size
	add	$s0, $zero, 	$zero		# $s0 = 0 (row)
	addi	$s2, $zero, 	1		# $s2 = 1
SQFOR1:
	slt	$t3, $s0, 	$t2		# $t3 = row < square_size
	beq	$t3, $zero, 	ENDSQFOR1	# if (row >= square_size) end forloop
	sub	$s1, $t2, 	$s2		# $s1 = square_size - 1
	beq	$s0, $zero, 	PROIF		# if (row == 0) go to PROIF
	bne	$s0, $s1, 	ELSE2		# if (row != square_size - 1) go to ELSE2
PROIF:
	addi	$t4, $zero, 	0x2B		# $t4 = '+' (lr)
	addi	$t5, $zero, 	0x2D		# $t5 = '-' (mid)
	j 	SKIPELSE2			# don't process else statement
ELSE2:
	addi	$t4, $zero, 	0x7C		# $t4 = '|' (lr)
	add	$t5, $zero, 	$t1		# $t5 = square_fill (mid)
SKIPELSE2:
	addi 	$v0, $zero, 	11		# print_char()
	add	$a0, $zero, 	$t4		# print_char(lr)
	syscall
	addi	$t6, $zero, 	1		# $t6 = 1 (i)
SQFOR2:
	slt	$t7, $t6, 	$s1		# $t7 = i < square_size - 1
	beq	$t7, $zero, 	ENDSQFOR2	# if (i >= square_size - 1) end forloop
	add	$a0, $zero, 	$t5		# print_char(mid)
	syscall
	addi	$t6, $t6, 	1		# i++
	j 	SQFOR2				# go to top of inner forloop	
ENDSQFOR2:
	add	$a0, $zero, 	$t4		# print_char(lr)
	syscall
	addi 	$a0, $zero, 	0xA		# print_char(\n)
	syscall	
	addi	$s0, $s0, 	1		# row++
	j	SQFOR1				# go to top of outer forloop
ENDSQFOR1:
	addi	$v0, $zero, 	11		# print_char
	addi 	$a0, $zero, 	0xA		# print_char(\n)
	syscall
ENDTASK2:

	
	# Task 3: Run Check
	# Determines if consecutive numbers in an array are ascending or descending.
	# If every consecutive number is larger than the last then the array is
	# ascending and ASCENDING is printed. If every consecutive number is smaller
	# than the last then the array is descending and DESCENDING is printed. If the
	# numbers in the array go both up and down then print NEITHER. If the array 
	# contains less than two elements or all the elements in the array are the
	# same number then print both ASCENDING and DESCENDING.
	#
	# REGISTERS:
	# $t0 = &intArray
	# $t1 = intArray_len
	# $t2 = previous element
	# $t3 = current element
	# $s0 = asc
	# $s1 = desc
	# $s2 = intArray_len < 2
	# $s3 = i
	# $s4 = comparator register
	
	la	$t0, runCheck			# $t0 = &runCheck
	lw	$t0, 0($t0)			# $t0 = runCheck
	beq	$t0, $zero, 	ENDTASK3	# if (runCheck == 0) go to ENDTASK3
	la	$t0, intArray			# $t0 = &intArray
	la 	$t1, intArray_len		# $t1 = &intArray_len
	lw 	$t1, 0($t1)			# $t1 = intArray_len
	add	$s0, $zero, 	$zero		# $s0 = 0 (asc)
	add 	$s1, $zero, 	$zero		# $s1 = 0 (desc)
	slti 	$s2, $t1,	2		# $s2 = intArray_len < 2
	bne	$s2, $zero, 	SKIPPRO		# if (intArray_len > 2) continue
	lw	$t2, 0($t0)			# $t2 = intArray[0]
	addi	$s3, $zero, 	1		# $s3 = 1 (i)
CHFOR:
	slt	$s4, $s3, 	$t1		# $s4 = i < intArray_len
	beq	$s4, $zero, 	SKIPPRO		# if (i >= intArray_len) end the forloop
	addi	$t0, $t0, 	4		# $t0 = &intArray + 4
	lw	$t3, 0($t0)			# $t3 = intArray + 4 ($t3 = next number)
	slt	$s4, $t2, 	$t3		# $s4 = intArray[n] < intArray[n + 1]
	beq	$s4, $zero, 	SKIPASC		# if (intArray[n] >= intArray[n + 1]) not ascending
	addi	$s0, $zero, 	1		# $s0 = 1 (array is ascending)
SKIPASC:
	slt 	$s4, $t3, 	$t2		# $s4 = intArray[n + 1] < intArray[n]
	beq	$s4, $zero, 	SKIPDESC	# if (intArray[n + 1] >= intArray[n]) not descending
	addi 	$s1, $zero, 	1		# $s1 = 1 (array is descending)
SKIPDESC:
	add	$t2, $zero, 	$t3		# $t2 = current number
	addi	$s3, $s3, 	1		# i++
	j 	CHFOR				# jump to top of forloop		
SKIPPRO:
	bne	$s0, $s1, 	NOTEQ		# if ($s0 != $s1) go to NOTEQ
	bne	$s0, $zero, 	NOT0		# if ($s0 != 0) then $s0 and $s1 equal 1
	addi	$v0, $zero, 	4		# print_str()
	la	$a0, RUN			# print_str(RUN)
	syscall
	la	$a0, ASC			# print_str(ASC)
	syscall
	la	$a0, RUN			# print_str(RUN)
	syscall
	la	$a0, DESC			# print_str(DESC)
	syscall
	j 	ADDNEWL
NOT0:
	addi	$v0, $zero, 	4		# print_str()
	la	$a0, RUN			# print_str(RUN)
	syscall
	la	$a0, NEITHER			# print_str(NEITHER)
	syscall
	j 	ADDNEWL
NOTEQ:
	beq	$s0, $zero, 	NOTASC		# if (asc != 1) go to NOTASC
	addi	$v0, $zero, 	4		# print_str()
	la	$a0, RUN			# print_str(RUN)
	syscall
	la	$a0, ASC			# print_str(ASC)
	syscall
	j	ADDNEWL
NOTASC:
	addi	$v0, $zero, 	4		# print_str()
	la	$a0, RUN			# print_str(RUN)
	syscall
	la	$a0, DESC			# print_str(DESC)
	syscall
ADDNEWL:
	addi	$v0, $zero, 	11		# print_char
	addi 	$a0, $zero, 	0xA		# print_char(\n)
	syscall
ENDTASK3:

	# Task 4: countWords
	# Counts the number of words that are separated by white spaces or new line
	# characters.
	#
	# REGISTERS:
	# $t0 = &str
	# $t1 = first character in the string
	# $t2 = temp holder of &str
	# $t3 = i < charCount
	# $t4 = second character in the string
	# $s0 = charCount
	# $s1 = i
	# $s2 = ' '
	# $s3 = null
	# $s4 = '\n'
	# $s5 = wordCount
	
	la 	$t0, countWords			# $t0 = &countWords
	lw	$t0, 0($t0)			# $t0 = countWords
	beq	$t0, $zero, 	ENDTASK4	# if (countWords == 0) go to ENDTASK4
	add	$s0, $zero, 	$zero		# $s0 = 0 (charCount)
	la	$t0, str			# $t0 = &str
	add	$s1, $zero, 	$zero		# $s1 = 0 (i)
	addi 	$s2, $zero, 	0x20		# $s2 = ' '
	addi 	$s3, $zero, 	0x0		# $s3 = null
	addi	$s4, $zero, 	0xA		# $s4 = '\n'
	add	$t2, $zero, 	$t0		# $t2 = $t0
	add 	$s5, $zero, 	$zero		# $s5 = 0 (wordCount)
CHARCOUNT:
	lb 	$t1, 0($t2)			# $t1 = the current character in str
	beq	$t1, $s3, 	ENDSTR		# if (curr character == null) go to ENDSTR
	addi	$s0, $s0, 	1		# charCount++
	addi	$t2, $t2, 	1		# $t2 = &str + 1
	j 	CHARCOUNT			# repeat loop
ENDSTR:
	bne	$s0, $zero, 	NOTEMPTY	# if (charCount != 0) str is not empty
	addi	$v0, $zero, 	1		# print_int()
	addi	$a0, $zero, 	0		# print_int(0)
	syscall
	j 	ENDOFSTR
NOTEMPTY:
	lb	$t1, 0($t0)			# $t1 = the first character in str
	bne	$s0, $s4, 	NOT1		# if (charCount != 1) str is not 1 character long
	beq	$t1, $s2, 	SPACE		# if ($t1 == ' ') go to SPACE
	beq	$t1, $s4, 	SPACE		# if ($t1 == '\n') go to SPACE
	addi	$v0, $zero,	1		# print_int()
	addi	$a0, $zero, 	1		# print_int(1)
	syscall
	j	ENDOFSTR
SPACE:
	addi	$v0, $zero, 	1		# print_int()
	addi	$a0, $zero, 	0		# print_int(0)
	syscall
	j 	ENDOFSTR
NOT1:
	addi 	$s1, $zero, 	1		# $s1 = 1 (i)
WORDCOUNT:
	slt	$t3, $s1, 	$s0		# $t3 = i < charCount
	beq	$t3, $zero, 	ENDOFSTR	# if (i >= charCount) go to ENDOFSTR
	addi	$t0, $t0, 	1		# $t0 = &str + 1
	lb	$t4, 0($t0)			# $t4 = next letter in str
	beq	$t1, $s2, 	LOOPTAIL	# if ($t1 == ' ') go to LOOPTAIL
	beq	$t1, $s4, 	LOOPTAIL	# if ($t1 == '\n') go to LOOPTAIL
	beq	$t4, $s2, 	NEWWORD		# if ($t4 == ' ') go to NEWWORD, non-space character followed by space character
	beq	$t4, $s4, 	NEWWORD		# if ($t4 == '\n') go to NEWWORD
	j 	LOOPTAIL
NEWWORD:
	addi	$s5, $s5, 	1		# wordCount++
LOOPTAIL:
	add	$t1, $zero, 	$t4		# $t1 = current character
	addi	$s1, $s1, 	1		# i++
	j 	WORDCOUNT			# repeat loop
ENDOFSTR:
	addi	$v0, $zero, 	1		# print_int()
	add	$a0, $zero, 	$s5		# print_int(wordCount)
	syscall
	addi	$v0, $zero, 	11		# print_char
	addi 	$a0, $zero, 	0xA		# print_char(\n)
	syscall
	addi	$v0, $zero, 	11		# print_char
	addi 	$a0, $zero, 	0xA		# print_char(\n)
	syscall	
ENDTASK4:

	# Task 5: revString
	# Takes a string input and reverses that string.
	#
	# REGISTERS:
	# $t0 = &str
	# $t1 = character placeholder
	# $t2 = address placeholder
	# $t3 = head < tail
	# $t4 = head[n]
	# $t5 = tail[n]
	# $s0 = head position
	# $s1 = tail position
	# $s2 = null
	# $s3 = 1
	
	la	$t0, revString			# $t0 = &revString
	lw	$t1, 0($t0)			# $t1 = revString
	beq	$t1, $zero, 	ENDTASK5	# if (revString == 1) go to end of task
	la	$t0, str			# $t0 = &str
	add	$s0, $zero, 	$zero		# $s0 = 0 (head)
	add	$s1, $zero, 	$zero		# $s1 = 0 (tail)
	add	$t2, $zero, 	$t0		# $t2 = &str - temp holder for str addresses
	addi	$s2, $zero, 	0x0		# $s2 = null
	addi	$s3, $zero, 	1		# $s3 = 1
COUNTCHARS:
	lb	$t1, 0($t2)			# $t1 = current character of str
	beq	$t1, $s2, 	NOCHARS		# if ($t1 == null) there are no more characters in the string
	addi 	$s1, $s1, 	1		# tail++
	addi	$t2, $t2, 	1		# $t2 = &str + 1
	j 	COUNTCHARS			# repeat loop
NOCHARS:
	sub 	$s1, $s1, 	$s3		# tail--
HLT:
	slt	$t3, $s0,	$s1		# $t3 = head < tail
	beq	$t3, $zero, 	ENDSWAP		# if (head > tail) go to ENDSWAP
	add	$t1, $t0, 	$s0		# $t1 = starting address of str + head 
	add	$t2, $t0,	$s1		# $t2 = starting address of str + tail
	lb	$t4, 0($t1)			# $t4 = str[head}
	lb	$t5, 0($t2)			# $t5 = str[tail]
	sb	$t5, 0($t1)			# storing tail into head's address
	sb	$t4, 0($t2)			# storing head into tial's address
	addi	$s0, $s0, 	1		# head++
	sub	$s1, $s1, 	$s3		# tail--
	j	HLT				# repeat loop
ENDSWAP:
	addi	$v0, $zero, 	4		# print_str()
	la	$a0, SWAP			# print_str(SWAP)
	syscall
ENDTASK5:	
	
	lw 	$ra, 4($sp) 	# get return address from stack
	lw 	$fp, 0($sp) 	# restore the caller's frame pointer
	addiu 	$sp, $sp, 24 	# restore the caller's stack pointer
	jr 	$ra 		# return to caller's code
