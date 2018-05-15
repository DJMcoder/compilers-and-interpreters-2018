# The program guess a random number, then repeatedly asks the user to guess a
# number and reports if the user's guess is too high or too low. Stops when the
# user guesses correctly
#
# Assistance from article: https://stackoverflow.com/questions/4147952/reading-files-with-mips-assembly
#
# Author: David Melisso
# Version: 4/19/18

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

  # store random int to $s1
  la $a0, buffer
  lb $a0, ($a0)
  move $s1, $a0

while:
  # asks for guess
  li $v0, 4
  la $a0, askforguess
  syscall

  # waits for input
  li $v0, 5
  syscall
  move $t1, $v0

  beq $s1, $t1, end
  bgt $t1, $s1, highguess

  # if guess < answer, tell the user and then ask for another guess
  li $v0, 4
  la $a0, low
  syscall
  j while

  # if guess > answer, tell the user and then ask for another guess
  highguess:
  li $v0, 4
  la $a0, high
  syscall
  j while

# Prints that they were correct and then terminates
end:
  li $v0, 4
  la $a0, correct
  syscall
  li $v0, 10
  syscall

  .data

  randomfile:   .asciiz "/dev/random"
  buffer:       .asciiz " "
  askforguess:  .asciiz "Guess what number I am thinking of between -127 and 128\n"
  high:         .asciiz "Your guess is greater than the answer\n"
  low:          .asciiz "Your guess is less than the answer\n"
  correct:      .asciiz "Your guess is correct\n"
