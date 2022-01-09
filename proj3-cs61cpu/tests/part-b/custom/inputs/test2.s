addi t0 zero 10
addi t1 zero 6
addi s0 zero 1

add t2 t0 t1
mul t2 t2 s0
sub t2 t2 s0
sll t2 t2 s0
mulh t2 t2 t2
mulhu t2 t2 s0
slt t3 t2 t1
xor t3 t1 t0
srl t2 t2 s0
sra t2 t2 s0
or t1 t1 t0
and t1 t1 t0
