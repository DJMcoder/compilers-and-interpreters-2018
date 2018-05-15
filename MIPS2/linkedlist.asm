# Returns the sum of numbers using a linked list
# Author: David Melisso
# Version: 5/4/18

  .data
  msg: .asciiz "Enter a number (-1 to quit): "
  sum: .asciiz "The sum of those numbers is: "

  .text
  .globl main

main:
  li    $a0, 0     # $a0 = 0

  jal    newlinkedlist        # jump to newlinkedlist and save position to $ra

  move   $a0, $v0    # $a0 = $v0

  jal    sumlist        # jump to sumlist and save position to $ra

  move $t0, $v0

  # print sum message
  la $a0, sum
  li $v0, 4
  syscall

  move   $a0, $t0    # $a0 = $t0
  li    $v0, 1    # $v0 = 1
  syscall

  li $v0, 10
  syscall

# allots 2 words
# returns $v0 - the address of the first word
alloc2:
  li    $a0, 8    # $a0 = 8
  li    $v0, 9    # $v0 = 9
  syscall
  jr    $ra          # jump to $ra

# gets a value from the user to make a new list node
# $a0 - the address to make the new list node refer to
# returns $v0 - the address of the new node
newlinkedlist:

  move $a1, $a0

  # ask for number
  la $a0, msg
  li $v0, 4
  syscall

  # get num from user => $a0
  li $v0, 5
  syscall
  move $a0, $v0

  beq    $a0, -1, finishlinkedlist  # if $a0 == -1 then finishlinkedlist

  subu    $sp, $sp, 4    # $sp = $sp - 4
  sw      $ra, ($sp)     # store $ra on the stack

  jal newlistnode

  move   $a0, $v0    # $a0 = $v0
  jal    newlinkedlist        # jump to newlinkedlist and save position to $ra

  lw      $ra, ($sp)     # pop $ra off the stack
  addu    $sp, $sp, 4    # $sp = $sp + 4

  jr    $ra          # jump to $ra

finishlinkedlist:
  move $v0, $a1
  jr    $ra          # jump to $ra

# make a list node
# $a0 - the value
# $a1 - address of next
# returns $v0 - the address of the new node
newlistnode:
  subu    $sp, $sp, 4    # $sp = $sp - 4
  sw      $a0, ($sp)     # store $a0 on the stack
  subu    $sp, $sp, 4    # $sp = $sp - 4
  sw      $a1, ($sp)     # store $a1 on the stack

  subu    $sp, $sp, 4    # $sp = $sp - 4
  sw      $ra, ($sp)     # store $ra on the stack

  jal    alloc2        # jump to alloc2 and save position to $ra

  lw      $ra, ($sp)     # pop $ra off the stack
  addu    $sp, $sp, 4    # $sp = $sp + 4

  lw      $a1, ($sp)     # pop $a1 off the stack
  addu    $sp, $sp, 4    # $sp = $sp + 4
  lw      $a0, ($sp)     # pop $a0 off the stack
  addu    $sp, $sp, 4    # $sp = $sp + 4

  sw    $a0, ($v0)       # store value
  sw    $a1, 4($v0)      # store next address

  jr    $ra          # jump to $ra


# gets the sum of values of nodes in a list
# $a0 - the address of the first node
# returns $v0 - the sum
sumlist:
  bne    $a0, 0, sumlistnormal  # if $a0 != 0 then sumlistnormal
  li    $v0, 0
  jr    $ra          # jump to $ra if address is 0

sumlistnormal:
  lw    $t0, ($a0)    # $t0 - value of the first node

  subu    $sp, $sp, 4    # $sp = $sp - 4
  sw      $t0, ($sp)     # store $t0 on the stack

  subu    $sp, $sp, 4    # $sp = $sp - 4
  sw      $ra, ($sp)     # store $ra on the stack

  lw    $a0, 4($a0)    # $a0 - the address of the next node
  jal    sumlist        # jump to sumlist and save position to $ra

  lw      $ra, ($sp)     # pop $ra off the stack
  addu    $sp, $sp, 4    # $sp = $sp + 4

  lw      $t0, ($sp)     # pop $t0 off the stack
  addu    $sp, $sp, 4    # $sp = $sp + 4

  addu    $v0, $v0, $t0    # $v0 = $v0 + $t0

  jr    $ra          # jump to $a
