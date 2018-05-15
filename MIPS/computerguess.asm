# The user chooses a random number, then the program repeatedly guesses the
# number and the user reports if the guess is too high or too low. Program stops
# when the user indicates that the guess is correct
# Author: David Melisso
# Version: 4/19/18

  .data
  intro0:       .asciiz "Pick a number between 0 and 128, exclusive.\n\n"
  intro1:       .asciiz "When asked 'Am I correct?', enter a negative number if "
  intro2:       .asciiz "the guess is less than the correct answer, a positive "
  intro3:       .asciiz "number if the guess is greater than the correct answer,"
  intro4:       .asciiz " and 0 if the guess is correct.\n\n"
  compare:      .asciiz "My current guess is "
  amiright:     .asciiz ". Am I correct?\n"
  correct:      .asciiz "Complete!"

  .text
  .globl main

main:
  # give intro
  li $v0, 4
  la $a0, intro0
  syscall
  la $a0, intro1
  syscall
  la $a0, intro2
  syscall
  la $a0, intro3
  syscall
  la $a0, intro4
  syscall


  # guess = $t1 = 64
  # change = $t2 = 32
  li $t1, 64
  li $t2, 32

  # gets compared value, puts in $t3
  li $v0, 4
  la $a0, compare
  syscall
  li $v0, 1
  move $a0, $t1
  syscall
  li $v0, 4
  la $a0, amiright
  syscall
  li $v0, 5
  syscall
  move $t3, $v0


while:
  beq $t3, 0, end   # finish if guess is correct

  blt $t3, 0, highguess

  # if (guess > actual) guess -= change
  subu $t1, $t1, $t2
  j decreasechange

  # if (guess < actual) guess += change
  highguess:
  addu $t1, $t1, $t2

  # change /= 2
  decreasechange:
  li $t4, 2
  div $t2, $t4
  mflo $t2

  # gets compared value, puts in $t3
  li $v0, 4
  la $a0, compare
  syscall
  li $v0, 1
  move $a0, $t1
  syscall
  li $v0, 4
  la $a0, amiright
  syscall
  li $v0, 5
  syscall
  move $t3, $v0
  j while

# Prints that they were correct and then terminates
end:
  li $v0, 4
  la $a0, correct
  syscall
  li $v0, 10
  syscall
