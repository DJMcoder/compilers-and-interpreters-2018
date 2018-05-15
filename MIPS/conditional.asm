# Takes in two numbers and prints the greater of the two
# Author: David Melisso
# Version: 4/16/18

  .data
  msg: .asciiz "Enter two numbers"
  .text
  .globl main


main:
  # print message
  la $a0, msg
  li $v0, 4
  syscall

  # get first num => $t1
  li $v0, 5
  syscall
  move $t1, $v0

  # get first num => $t2
  li $v0, 5
  syscall
  move $t2, $v0

  bgt $t1, $t2, skip

  move $a0, $t2
  j printandend

skip:
  move $a0, $t1

printandend:
  li $v0, 1
  syscall
  li $v0, 10,
  syscall
