	.data
	newline: .asciiz "\n"
	varx: .word 0
	varcount: .word 0
	vary: .word 0
	.text
	.globl main
main:
	li $v0 2
	la $t0 varx
	sw $v0 ($t0)
	la $t0 varx
	lw $v0 ($t0)
	subu $sp $sp 4
	sw $v0 ($sp)
	li $v0 1
	lw $t0 ($sp)
	addu $sp $sp 4
	addu $v0 $v0 $t0
	la $t0 vary
	sw $v0 ($t0)
	la $t0 varx
	lw $v0 ($t0)
	subu $sp $sp 4
	sw $v0 ($sp)
	la $t0 vary
	lw $v0 ($t0)
	lw $t0 ($sp)
	addu $sp $sp 4
	addu $v0 $v0 $t0
	la $t0 varx
	sw $v0 ($t0)
	la $t0 varx
	lw $v0 ($t0)
	subu $sp $sp 4
	sw $v0 ($sp)
	la $t0 vary
	lw $v0 ($t0)
	lw $t0 ($sp)
	addu $sp $sp 4
	mult $v0 $t0
	mflo $v0
	move $a0 $v0
	li $v0 1
	syscall
	li $v0 4
	la $a0 newline
	syscall
	la $t0 varx
	lw $v0 ($t0)
	subu $sp $sp 4
	sw $v0 ($sp)
	la $t0 vary
	lw $v0 ($t0)
	lw $t0 ($sp)
	addu $sp $sp 4
	ble $t0 $v0 endif1
	la $t0 varx
	lw $v0 ($t0)
	move $a0 $v0
	li $v0 1
	syscall
	li $v0 4
	la $a0 newline
	syscall
	la $t0 vary
	lw $v0 ($t0)
	move $a0 $v0
	li $v0 1
	syscall
	li $v0 4
	la $a0 newline
	syscall
endif1:
	li $v0 14
	subu $sp $sp 4
	sw $v0 ($sp)
	li $v0 14
	lw $t0 ($sp)
	addu $sp $sp 4
	bne $t0 $v0 endif2
	li $v0 14
	subu $sp $sp 4
	sw $v0 ($sp)
	li $v0 14
	lw $t0 ($sp)
	addu $sp $sp 4
	beq $t0 $v0 endif3
	li $v0 3
	move $a0 $v0
	li $v0 1
	syscall
	li $v0 4
	la $a0 newline
	syscall
endif3:
	li $v0 14
	subu $sp $sp 4
	sw $v0 ($sp)
	li $v0 14
	lw $t0 ($sp)
	addu $sp $sp 4
	bgt $t0 $v0 endif4
	li $v0 4
	move $a0 $v0
	li $v0 1
	syscall
	li $v0 4
	la $a0 newline
	syscall
endif4:
endif2:
	li $v0 15
	subu $sp $sp 4
	sw $v0 ($sp)
	li $v0 14
	lw $t0 ($sp)
	addu $sp $sp 4
	ble $t0 $v0 endif5
	li $v0 5
	move $a0 $v0
	li $v0 1
	syscall
	li $v0 4
	la $a0 newline
	syscall
endif5:
	li $v0 1
	la $t0 varcount
	sw $v0 ($t0)
while6:
	la $t0 varcount
	lw $v0 ($t0)
	subu $sp $sp 4
	sw $v0 ($sp)
	li $v0 15
	lw $t0 ($sp)
	addu $sp $sp 4
	bgt $t0 $v0 whileend7
	la $t0 varcount
	lw $v0 ($t0)
	move $a0 $v0
	li $v0 1
	syscall
	li $v0 4
	la $a0 newline
	syscall
	la $t0 varcount
	lw $v0 ($t0)
	subu $sp $sp 4
	sw $v0 ($sp)
	li $v0 1
	lw $t0 ($sp)
	addu $sp $sp 4
	addu $v0 $v0 $t0
	la $t0 varcount
	sw $v0 ($t0)
	j while6
whileend7:
	li $v0 10
	syscall # halt
