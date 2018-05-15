# Returns the greater of two numbers
# Author: David Melisso
# Version: 5/2/18

  .data
  msg: .asciiz "Enter two numbers:\n"
  max: .asciiz "\nThe max of the two numbers is: "
  .text
  .globl main

main:
  # print message
  la $a0, msg
  li $v0, 4
  syscall

  # get first num => $a0
  li $v0, 5
  syscall
  move $a0, $v0

  # get second num => $a1
  li $v0, 5
  syscall
  move $a1, $v0

  jal    max2        # jump to max2 and save position to $ra

  move $t0, $v0

  # print message
  la $a0, max
  li $v0, 4
  syscall

  # print max
  move $a0, $t0
  li $v0, 1
  syscall

  # terminate
  li $v0, 10
  syscall

max2:
  bgt    $a0, $a1, a0gt   # if $a0 > $a1 then a0gt
  move   $v0, $a1         # $v0 = $a1
  j    finishmax2         # jump to finishmax2
a0gt:
  move   $v0, $a0         # $v0 = $a0
finishmax2:
  jr    $ra               # jump to $ra
