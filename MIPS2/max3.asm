# Returns the greatest of three numbers
# Author: David Melisso
# Version: 5/2/18

  .data
  msg: .asciiz "Enter three numbers:\n"
  max: .asciiz "\nThe max of the three numbers is: "
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

  # get third num => $a2
  li $v0, 5
  syscall
  move $a2, $v0

  jal    max3        # jump to max3 and save position to $ra

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

max3:
  subu  $sp, $sp, 4       # $sp = $sp - 4
  sw    $ra, ($sp)
  jal   max2              # jump to max2 and save position to $ra
  move  $a0, $v0          # $a0 = $v0
  move  $a1, $a2          # $a1 = $a2
  jal   max2              # jump to max2 and save position to $ra
  lw    $ra ($sp)
  addu  $sp, $sp, 4       # $sp = $sp + 4
  jr    $ra               # jump to $ra


max2:
  bgt    $a0, $a1, a0gt   # if $a0 > $a1 then a0gt
  move   $v0, $a1         # $v0 = $a0
  j    finishmax2         # jump to finishmax2
a0gt:
  move   $v0, $a0         # $v0 = $a1
finishmax2:
  jr    $ra               # jump to $ra
