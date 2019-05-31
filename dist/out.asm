addi $t0 $zero -1 
addi $t1 $zero 0x04AA 
add $s0 $zero $gp 
portakal: addi $t0 $t0 1 
nop
nop
sw $t0 0($s0) 
addi $s0 $s0 4 
bne $t0 $t1 portakal 
nop
nop
nop
add $s2 $s0 $gp 
nop
nop
ori $s3 $s2 0x0077 
addi $s1 $s2 0 
