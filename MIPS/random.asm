# Print a random number
#
# Assistance from article: https://stackoverflow.com/questions/4147952/reading-files-with-mips-assembly
#
# Author: David Melisso
# Version: 4/19/18

  .data
  randomfile: .asciiz "/dev/random"
  buffer: .asciiz ""

  .text
  .globl main

# https://stackoverflow.com/questions/4147952/reading-files-with-mips-assembly
main:
  #open /dev/random
  li   $v0, 13
  la   $a0, randomfile
  li   $a1, 0
  li   $a2, 0
  syscall
  move $s6, $v0      # save the file descriptor

  #read from /dev/random
  li   $v0, 14
  move $a0, $s6
  la   $a1, buffer
  li   $a2, 1
  syscall

  # store random int to $a0
  la $a0, buffer
  lw $a0, ($a0)

  # print random number
  li $v0, 1
  syscall

  # terminate the program
  li $v0, 10
  syscall
