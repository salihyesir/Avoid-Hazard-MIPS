li $t2, 10
li $t7, 5
addi $t1, $t2, 15 
add $t4, $t1, $t3 
sub $t1, $t1, $t3 
add $t1, $t1, $t2 
lw $t3, 0($t1)
add $t4, $t3, $t2
sw $t4, 0($t2) 
add $t6, $t3, $t2
sw $t1, 0($t6) 

