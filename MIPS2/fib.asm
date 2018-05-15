# Returns the number in the fibonacci sequence with the given index
# Author: David Melisso
# Version: 5/2/18

  .data

  msg: .asciiz "Enter a number: "
  res: .asciiz "The number in the fibonacci sequence with that index is: "

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

  jal    fib        # jump to fib and save position to $ra

  move $t0, $v0

  # print message
  la $a0, res
  li $v0, 4
  syscall

  move   $a0, $t0    # $a0 = $t0
  li $v0, 1
  syscall

  li $v0, 10
  syscall

fib:
  bgt    $a0, 1, fibreg  # if $a0 > 1 then fibreg
  li     $v0, 1          # $v0 = 1
  jr     $ra             # jump to $ra

fibreg:
  subu  $sp, $sp, 4    # $sp = $sp - 4
  sw    $ra, ($sp)     # push $ra to stack

  subu  $sp, $sp, 4    # $sp = $sp - 4
  sw    $a0, ($sp)     # push $a0 to stack

  subu  $a0, $a0, 1    # $a0 = $a0 - 1
  jal   fib            # jump to fib and save position to $ra

  lw    $a0, ($sp)     # pop $a0 off of the stack
  addu  $sp, $sp, 4    # $sp = $sp + 4

  subu  $sp, $sp, 4    # $sp = $sp - 4
  sw    $v0, ($sp)     # push $v0 to stack

  subu  $a0, $a0, 2    # $a0 = $a0 - 2
  jal   fib            # jump to fib and save position to $ra

  lw    $t0, ($sp)     # pop $t0 off of the stack
  addu  $sp, $sp, 4    # $sp = $sp + 4

  addu  $v0, $v0, $t0    # $v0 = $v0 + $t0

  lw    $ra, ($sp)     # pop $ra off of the stack
  addu  $sp, $sp, 4    # $sp = $sp + 4
  jr    $ra            # jump to $ra
