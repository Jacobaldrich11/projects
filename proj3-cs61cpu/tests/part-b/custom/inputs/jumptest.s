add t0 zero zero
addi t1 zero 10
add t2 zero zero
addi t3 zero 10
jal zero Start

Start:
    addi t0 t0 1
    bge t0 t1 End
    jal zero Start

End:
    add t0 zero zero
    addi t2 t2 1
    blt t3 t2 Done
    jal zero Start

Done:
    addi s0 zero 5
