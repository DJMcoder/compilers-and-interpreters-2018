# Prints the numbers 1 through 10, inclusive
# Author: David Melisso
# Version: 4/16/18

  .text
  .globl main

main:
  li $t1, 1

while:
  bgt $t1, 10, end

  move $a0, $t1
  li $v0, 1
  syscall

  addu $t1, $t1, 1
  j while

end:
  li $v0, 10
  syscall
