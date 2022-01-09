addi t0 zero 1
addi t1 zero 1

beq t0 t1 Hello


Hello:
    addi s0 zero 5

bne t0 t1 End
blt t0 t1 End
bge t0 t1 Part2



Part2:
    addi s1 zero 20

bltu t0 t1 End
bne s1 t0 One

One:
    addi s1 zero 200

blt t0 s1 Two


Two:
    addi s1 zero 300

bltu t0 s1 Three

Three:
    addi s1 zero 400

bne zero s1 End


End:
    addi s1 zero 100
