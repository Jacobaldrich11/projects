addi t0 zero 5
addi t1 zero 5
addi t2 zero 10
addi t3 zero -1
addi a0 zero 5
add a1 zero zero

Restart:
    add zero zero zero

beq t0 t2 Jump
bne t0 t1 Jump
blt t2 t3 Jump
bge t3 t2 Jump
bltu t3 t2 Jump
bgeu t2 t3 Jump

beq t0 t1 ABC

ABC:
    add zero zero zero

bne t0 t2 DEF

DEF:
    add zero zero zero

blt t3 t2 GHI

GHI:
    add zero zero zero

bge t2 t3 JKL

JKL:
    add zero zero zero

bltu t2 t3 MNO

MNO:
    add zero zero zero

bgeu t3 t2 PQR

PQR:
    add zero zero zero

addi a1 a1 1
bne a0 a1 Restart

Jump:
    add zero zero zero
