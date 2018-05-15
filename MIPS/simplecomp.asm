# Multiplies two inputted numbers and prints it out
# Author: David Melisso
# Version: 4/16/18

  .data
  msg: .asciiz "Enter two numbers between 1 and 100"
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

  # multiply the numbers and put it in $a0
  mult $t1,$t2
  mflo $a0

  # print the result
  li $v0, 1
  syscall

  # end the program
  li $v0, 10
  syscall
