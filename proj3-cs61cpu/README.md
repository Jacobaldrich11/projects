# CS61CPU

Look ma, I made a CPU! Here's what I did:
    ALU: The ALU is basically a giant multiplexer. First I calculate every possible value for the two inputs 
    (multiplication, addition, bit shifts, ...), then I pick the correct one based on the 5 bit long input string
    by using a MUX.

    Registers: To implement the 32 available registers in RISC V, I first added 32 registers. Then I take every 
    possible read value from the register and choose the correct ones to output based on the two 5 bit input strings
    using two muxes and tunnels. Finally, I pick what to write to by using 32 comparators to choose the correct 
    register, then changing the write value of the register if the comparator outputs 1. [reg_val -> new_reg_val if 
    input_string = register_string].

    Immediate Generator: To make the immediate generator, I considered every possible instruction type and looked
    at the immediate for each 32 bit line. I then extended the immediate to 32 bits in order to have the ALU perform
    its operations correctly.

    Datapath: The datapath is basically the same as the one seen in lecture.

    Control: To make the control, I hard coded a table of values for each instruction type. I put the bits for each 
    instruction and had the control output vary based on what I had put into the table. For example, one column was
    concerned with whether the ALU would recieve the program counter or the value at RS1. To make PCSel, aka a bit 
    which stated if a branch was taken or not, I had to use a branch comparator and boolean logic. The hardest part 
    about the control however was choosing which instruction from the table. I had to check the opcode, func7, and 
    func3 of each 32 bit address, and choose the instruction appropriately. It took many elements, and is probably 
    slow, but it still works.

    Branch Comparator: This comparator basically just takes in two branch values, and outputs if they are equal or 
    A > B. With these two elements, you can check to see if an instruction branches or not.

    DMem: To make the DMem, I first pass into it the value from the ALU. Then if we are writing, I find out what bits
    we are specifically writing to. Then I pass those bits into the memory after bit extending them. If we are passing
    the value to a register, I sign extend the specific bits I am asking for, then pass them into the registers using
    a mux.

    Pipelining: To pipeline, I had to add a couple registers which stored the values of the previous instruction. Thus, 
    we could fetch an instruction from the IMEM while simultaneously executing another instruction. However, I had to 
    add a couple muxes passing in nops, if we are on the first instruction in the cycle, or if we jump (PCSel = 1).
    
