	.data
	newline: .asciiz "\n"
	vartimes: .word 0
	varcount: .word 0
	varignore: .word 0
	.text
	.globl main
main:
	j skipprocprintSquares
procprintSquares:
	subu $sp $sp 4
	sw $v0 ($sp)
	lw $v0 8($sp)
	la $t0 varcount
	sw $v0 ($t0)
while1:
	la $t0 varcount
	lw $v0 ($t0)
	subu $sp $sp 4
	sw $v0 ($sp)
	lw $v0 8($sp)
	lw $t0 ($sp)
	addu $sp $sp 4
	bgt $t0 $v0 whileend2
	la $t0 varcount
	lw $v0 ($t0)
	subu $sp $sp 4
	sw $v0 ($sp)
	la $t0 varcount
	lw $v0 ($t0)
	lw $t0 ($sp)
	addu $sp $sp 4
	mult $v0 $t0
	mflo $v0
	sw $v0 0($sp)
	lw $v0 0($sp)
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
	la $t0 vartimes
	lw $v0 ($t0)
	subu $sp $sp 4
	sw $v0 ($sp)
	li $v0 1
	lw $t0 ($sp)
	addu $sp $sp 4
	addu $v0 $v0 $t0
	la $t0 vartimes
	sw $v0 ($t0)
	j while1
whileend2:
	la $t0 vartimes
	lw $v0 ($t0)
	jr $ra
	jr $ra
skipprocprintSquares:
	li $v0 196
	la $t0 varcount
	sw $v0 ($t0)
	li $v0 0
	la $t0 vartimes
	sw $v0 ($t0)
	subu $sp $sp 4
	sw $ra ($sp)
	li $v0 10
	subu $sp $sp 4
	sw $v0 ($sp)
	li $v0 13
	subu $sp $sp 4
	sw $v0 ($sp)
	jal procprintSquares
	addu $sp $sp 4
	addu $sp $sp 4
	lw $ra ($sp)
	addu $sp $sp 4
	la $t0 varignore
	sw $v0 ($t0)
	la $t0 varcount
	lw $v0 ($t0)
	move $a0 $v0
	li $v0 1
	syscall
	li $v0 4
	la $a0 newline
	syscall
	la $t0 varignore
	lw $v0 ($t0)
	subu $sp $sp 4
	sw $v0 ($sp)
	li $v0 5
	lw $t0 ($sp)
	addu $sp $sp 4
	addu $v0 $v0 $t0
	move $a0 $v0
	li $v0 1
	syscall
	li $v0 4
	la $a0 newline
	syscall
	li $v0 10
	syscall # halt
