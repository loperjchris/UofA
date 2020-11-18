Extra Instructions:
xori
bne
lui

xori - In order to implement xori logic had to be added into the CPU control to set ALUsrc = 1, ALU.op = 0x4 and regWrite = 1. Next, ALUinput2 had to be set to the 32 bit zero extended (extended to the right) version of the 16 bit immediate field provided by the instruction. 

bne - In order to implement bne logic had to be added into the CPU control to set ALUsrc = 0, ALU.op = 0x2, ALU.bNegate = 1, and branch = 1. Then, getNextPC was modified so that it would recognize when branch was 1 and the zero result from the ALU was 0 and the instruction given was a bne which would then right shift the 32 bit signed extended version of the 16 bit immediate field given in the instruction and add that to the oldPC + 4.

lui - In order to implement lui logic had to be added into the CPU control to set ALUsrc = 1, ALU.op = 0x2, and regWrite = 1.Then, ALUinput2 had to be modified so that if an lui instruction were given the input would be the 16 bit immediate field shifted left by 16 zeros.