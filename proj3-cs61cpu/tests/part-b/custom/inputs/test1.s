addi t0 zero 8
slli t0 t0 2

addi t1 zero 20
srai t1 t1 2

srli t1 t1 2
xori t1 t1 32

slti t1 t1 2
ori t1 t1 32
andi t1 t1 64

addi a0 zero 1000
slli a0 a0 1
slti a0 a0 1
xori a0 a0 0
addi a0 zero 200
srli a0 a0 1
srai a0 a0 1
ori a0 a0 128
andi a0 a0 128
