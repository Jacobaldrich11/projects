addi a0 zero 5
addi a1 zero 10
jal ra Test
jal zero End

Test:
    add zero zero zero
    jalr zero ra 0
    
Test2:
	jal zero Final

End:
    addi s0 zero 100

jal a2 End3
jalr zero a2 -12

End2:
    addi s0 zero 200
    jalr zero ra 0

End3:
    addi s0 zero 300

jal ra End2
jalr zero a2 0

Final:
    add zero zero zero
    
jalr zero ra 16
addi zero zero 10
addi zero zero 10
addi zero zero 10
addi zero zero 10
addi zero zero 10
addi zero zero 10
addi zero zero 10
addi zero zero 10
addi zero zero 10
