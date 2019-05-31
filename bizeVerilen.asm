addi $t0, $zero, -1
addi $t1, $zero, 0x002
add $s0, $zero, $gp
portakal: addi $t0, $t0, 1
sw $t0, 0($s0)
addi $s0, $s0, 4
bne $t0, $t1, portakal
add $s2, $s0, $gp
ori $s3, $s2, 0x0077
addi $s1, $s2, 0