# Adds 2 and 3
# Author: David Melisso
# Version: 4/16/18

  .text 0x00400000
  .globl main

main:
  li $t0, 2
  li $t1, 3
  # load 2 into $t0
  # load 3 into $t1
  addu $t2, $t0, $t1 # store $t0 + $t1 in $t2
  li $v0, 10
  syscall
