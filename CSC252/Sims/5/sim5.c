/* AUTHOR: Ruben Tequida
 * FILE: sim5.c
 * ASSIGNMENT: Simulation #5: Pipelined CPU
 * COURSE: CSc 252; Fall 2019
 * Purpose: This program replicates the function of a pipelined CPU. The CPU
 * will be broken down into phases separated by pipeline registers in order
 * for mulitple instructions to be able to be processed simultaneously in the
 * CPU. The instruction will be broken up into multiple phases and each phase
 * will be done by a different part of the CPU and once that phase is complete
 * the results are stored in the appropriate pipeline register and utilized by
 * the next phase in the cycle.
 */

#include <stdio.h>
#include "sim5.h"

/*
 * Purpose: Derives each field from the given instruction.
 *
 * @param instruction - a 32 bit value that represents the instruction.
 *
 * @param fieldsOut - provides access to the field variables of the
 * instructionFields method.
 *
 * @return None.
 */
void extract_instructionFields(WORD instruction, InstructionFields *fieldsOut) {
  fieldsOut->opcode = ((instruction >> 26) & 0x3f);
  fieldsOut->rs = ((instruction >> 21) & 0x1f);
  fieldsOut->rt = ((instruction >> 16) & 0x1f);
  fieldsOut->rd = ((instruction >> 11) & 0x1f);
  fieldsOut->shamt = ((instruction >> 6) & 0x1f);
  fieldsOut->funct = (instruction & 0x3f);
  fieldsOut->imm16 = (instruction & 0xffff);
  fieldsOut->imm32 = signExtend16to32(fieldsOut->imm16);
  fieldsOut->address = (instruction & 0x3ffffff);
}

/*
 * Purpose: Determines if it is neceassary to insert a stall into the pipeline.
 *
 * @param @param fields - provides acces to the field variables of the
 * instructionFields method.
 *
 * @param old_idex - provides access to the previous instruction's variables
 * that is now in the execute phase.
 *
 * @param old_exmem - provides access to the previous instruction's variables
 * that is now in the memory phase.
 *
 * @return a 1 if a stall is necessary and a 0 if not.
 */
int IDtoIF_get_stall(InstructionFields *fields,
  ID_EX  *old_idex, EX_MEM *old_exmem) {
    // Checking if an R format instruction following a load word is using the register the load word loads to.
    if (fields->opcode == 0 && old_idex->memRead && (old_idex->rt == fields->rs || old_idex->rt == fields->rt)) {
      return 1;
    }
    // Same check but for I format registers
    if (fields->opcode != 0 && old_idex->memRead && old_idex->rt == fields->rs) {
      return 1;
    }
    // Checking if the instruction two cycles ago has write register in common with the instruction one cycle after it.
    if (old_exmem->regWrite && (old_exmem->writeReg != old_idex->rd && old_exmem->writeReg != old_idex->rt) && (old_exmem->writeReg == fields->rt) && fields->opcode == 0x2B) {
      return 1;
    }
    // Checking if current instruction is a store word and if its storying registers is modified
    // by the instruction two cycles ahead of it.
    if (fields->opcode == 0x2B) {
      if (old_exmem->regWrite && old_exmem->writeReg == fields->rt) {
        // R format
        if (old_idex->regDst == 1 && old_idex->regWrite) {
          if (old_idex->rd != old_exmem->writeReg) {
            return 1;
          }
          // I format
        } else if (old_idex->regDst == 0 && old_idex->regWrite) {
          if (old_idex->rt != old_exmem->writeReg) {
            return 1;
          }
        }
      }
    }
    return 0;
  }

  /*
   * Purpose: Determines if the current instruction is going to cause a branch or a jump.
   *
   * @param @param fields - provides acces to the field variables of the
   * instructionFields method.
   *
   * @param rsVal - the value held by the rs register.
   *
   * @param rtVal - the value held by the rt register..
   *
   * @return a 1 if a branch or jump is going to occur.
   */
int IDtoIF_get_branchControl(InstructionFields *fields, WORD rsVal, WORD rtVal) {
  // Checking for branch statements
  if ((fields->opcode == 0x4 && rsVal == rtVal) || (fields->opcode == 0x5 && rsVal != rtVal)) {
    return 1;
  }
  // Checking for jump statements
  if (fields->opcode == 0x2) {
    return 2;
  }
  return 0;
}

/*
 * Purpose: Determines which instruction to go to if a branch is taken.
 *
 * @param pcPlus4 - the current location in instruction memory.
 *
 * @param @param fields - provides acces to the field variables of the
 * instructionFields method.
 *
 * @return the new instuction location.
 */
WORD calc_branchAddr(WORD pcPlus4, InstructionFields *fields) {
  return pcPlus4 + (fields->imm32 << 2);
}

/*
 * Purpose: Determines which instruction to go to if a jump is called.
 *
 * @param pcPlus4 - the current location in instruction memory.
 *
 * @param @param fields - provides acces to the field variables of the
 * instructionFields method.
 *
 * @return the new instuction location.
 */
WORD calc_jumpAddr  (WORD pcPlus4, InstructionFields *fields) {
  return ((fields->address << 2) | (pcPlus4 & 0xf0000000));
}

/*
 * Purpose: Determines all the control bits for each instruction as well as
 * passing on instruction fields to the next pipeline register.
 *
 * @param IDstall - a 1 if a stall is necessary and a 0 if not.
 *
 * @param fields - provides acces to the field variables of the
 * instructionFields method.
 *
 * @param pcPlus4 - the current location in instruction memory.
 *
 * @param rsVal - the value held by the rs register.
 *
 * @param rtVal - the value held by the rt register.
 *
 * @param new_idex - the varialbes held in the pipeline register that is
 * between the ID and execute phase.
 *
 * @return a 1 if the instruction is valid and a 0 if it isn't.
 */
int execute_ID(int IDstall,
  InstructionFields *fieldsIn,
  WORD pcPlus4,
  WORD rsVal, WORD rtVal,
  ID_EX *new_idex) {
    // Initializing all control bits to 0
    new_idex->ALUsrc = 0;
    new_idex->ALU.op = 0;
    new_idex->ALU.bNegate = 0;
    new_idex->memRead = 0;
    new_idex->memWrite = 0;
    new_idex->memToReg = 0;
    new_idex->regDst = 0;
    new_idex->regWrite = 0;
    // Passing on the instruction fields if it's not a stall, branch, or jump
    if (fieldsIn->opcode != 0x2 && fieldsIn->opcode != 0x4 && fieldsIn->opcode != 0x5 && !IDstall) {
      new_idex->rs = fieldsIn->rs;
      new_idex->rt = fieldsIn->rt;
      new_idex->rd = fieldsIn->rd;
      new_idex->rsVal = rsVal;
      new_idex->rtVal = rtVal;
      new_idex->imm16 = fieldsIn->imm16;
      new_idex->imm32 = fieldsIn->imm32;
      // Setting all fields to 0 if a stall, branch, or jump
    } else {
      new_idex->rs = 0;
      new_idex->rt = 0;
      new_idex->rd = 0;
      new_idex->rsVal = 0;
      new_idex->rtVal = 0;
      new_idex->imm16 = 0;
      new_idex->imm32 = 0;
    }
    if (IDstall) {
      pcPlus4 -= 4;
      // new_idex->ALUsrc = 0x2;
      return 1;
    }
    // R format
    if (fieldsIn->opcode == 0) {
      //nop
      if (fieldsIn->funct == 0) {
        new_idex->ALU.op = 0x5;
        new_idex->regDst = 1;
        new_idex->regWrite = 1;
        return 1;
      }
      //add or addu
      if (fieldsIn->funct == 0x20 || fieldsIn->funct == 0x21) {
        new_idex->ALU.op = 0x2;
        new_idex->regDst = 1;
        new_idex->regWrite = 1;
        return 1;
      }
      //sub or subu
      if (fieldsIn->funct == 0x22 || fieldsIn->funct == 0x23) {
        new_idex->ALU.op = 0x2;
        new_idex->ALU.bNegate = 1;
        new_idex->regDst = 1;
        new_idex->regWrite = 1;
        return 1;
      }
      //and
      if (fieldsIn->funct == 0x24) {
        new_idex->regDst = 1;
        new_idex->regWrite = 1;
        return 1;
      }
      //or
      if (fieldsIn->funct == 0x25) {
        new_idex->ALU.op = 0x1;
        new_idex->regDst = 1;
        new_idex->regWrite = 1;
        return 1;
      }
      //xor
      if (fieldsIn->funct == 0x26) {
        new_idex->ALU.op = 0x4;
        new_idex->regDst = 1;
        new_idex->regWrite = 1;
        return 1;
      }
      //nor
      if (fieldsIn->funct == 0x27) {
        new_idex->ALU.op = 0x1;
        new_idex->ALU.bNegate = 1;
        new_idex->regDst = 1;
        new_idex->regWrite = 1;
        return 1;
      }
      //slt
      if (fieldsIn->funct == 0x2A) {
        new_idex->ALU.op = 0x3;
        new_idex->ALU.bNegate = 1;
        new_idex->regDst = 1;
        new_idex->regWrite = 1;
        return 1;
      }
    }
    //addi or addiu
    if (fieldsIn->opcode == 0x8 || fieldsIn->opcode == 0x9) {
      new_idex->ALUsrc = 1;
      new_idex->ALU.op = 0x2;
      new_idex->regWrite = 1;
      return 1;
    }
    //slti
    if (fieldsIn->opcode == 0xA) {
      new_idex->ALUsrc = 1;
      new_idex->ALU.op = 0x3;
      new_idex->ALU.bNegate = 1;
      new_idex->regWrite = 1;
      return 1;
    }
    //lw
    if (fieldsIn->opcode == 0x23) {
      new_idex->ALUsrc = 1;
      new_idex->ALU.op = 0x2;
      new_idex->memRead = 1;
      new_idex->memToReg = 1;
      new_idex->regWrite = 1;
      return 1;
    }
    //sw
    if (fieldsIn->opcode == 0x2B) {
      new_idex->ALUsrc = 1;
      new_idex->ALU.op = 0x2;
      new_idex->memWrite = 1;
      return 1;
    }
    //beq or bne
    if (fieldsIn->opcode == 0x4 || fieldsIn->opcode == 0x5) {
      return 1;
    }
    if (fieldsIn->opcode == 0x2) {
      return 1;
    }
    //andi
    if (fieldsIn->opcode == 0xC) {
      new_idex->ALUsrc = 2;
      new_idex->regWrite = 1;
      return 1;
    }
    //ori
    if (fieldsIn->opcode == 0xD) {
      new_idex->ALUsrc = 2;
      new_idex->ALU.op = 0x1;
      new_idex->regWrite = 1;
      return 1;
    }
    //lui
    if (fieldsIn->opcode == 0xF) {
      new_idex->ALUsrc = 1;
      new_idex->ALU.op = 0x6;
      new_idex->regWrite = 1;
      return 1;
    }
    return 0;
  }

  /*
   * Purpose: Determines what the first input for the ALU will be.
   *
   * @param in - the current instructions fields variables and control bits
   * going into the execute phase.
   *
   * @param old_exMem - the field variables and control bits for the previous
   * instruction currently in the memory phase.
   *
   * @param old_memWb - the field variables and control bits for the instruction
   * two cycles ahead currently in the write back phase.
   *
   * @return the input to the ALU.
   */
WORD EX_getALUinput1(ID_EX *in, EX_MEM *old_exMem, MEM_WB *old_memWb) {
  // Forwarding from previous instruction
  if (old_exMem->regWrite && old_exMem->writeReg == in->rs) {
    return old_exMem->aluResult;
  }
  // Forwarding from instruction two cycles ago
  if (old_memWb->regWrite && old_memWb->writeReg == in->rs) {
    if (old_memWb->memToReg) {
      return old_memWb->memResult;
    }
    return old_memWb->aluResult;
  }
  return in->rsVal;
}

/*
 * Purpose: Determines what the second input for the ALU will be.
 *
 * @param in - the current instructions fields variables and control bits
 * going into the execute phase.
 *
 * @param old_exMem - the field variables and control bits for the previous
 * instruction currently in the memory phase.
 *
 * @param old_memWb - the field variables and control bits for the instruction
 * two cycles ahead currently in the write back phase.
 *
 * @return the input to the ALU.
 */
WORD EX_getALUinput2(ID_EX *in, EX_MEM *old_exMem, MEM_WB *old_memWb) {
  // Forwarding from the previous instruction
  if (old_exMem->regWrite && old_exMem->writeReg == in->rt && in->ALUsrc == 0) {
    return old_exMem->aluResult;
  }
  // Forwarding from the instruction two cycles ahead
  if (old_memWb->regWrite && old_memWb->writeReg == in->rt && in->ALUsrc == 0) {
    if (old_memWb->memToReg) {
      return old_memWb->memResult;
    }
    return old_memWb->aluResult;
  }
  // Determining if using rd or immediate fields
  if (in->ALUsrc == 0) {
    return in->rtVal;
    // zero extending for andi and ori
  } else if (in->ALUsrc == 2){
    return in->imm16 & 0x0000FFFF;
  }
  return in->imm32;
}

/*
 * Purpose: Executes the ALU and pushes forward field variables and control
 * bits.
 *
 * @param in - the current instructions fields variables and control bits
 * going into the execute phase.
 *
 * @param input1 - the value returned from EX_getALUinput1.
 *
 * @param input2 - the value returned from EX_getALUinput2.
 *
 * @param new_exMem - the pipeline register following the execute phase.
 *
 * @return None.
 */
void execute_EX(ID_EX *in, WORD input1, WORD input2,
  EX_MEM *new_exMem) {
    // AND
    if (in->ALU.op == 0) {
      new_exMem->aluResult = (input1 & input2);
      // OR
    } else if (in->ALU.op == 1) {
      if (!in->ALU.bNegate) {
        new_exMem->aluResult = (input1 | input2);
        // NOR
      } else {
        new_exMem->aluResult = ~(input1 | input2);
      }
      // ADDER
    } else if (in->ALU.op == 2) {
      // Adding
      if (in->ALU.bNegate == 0) {
        new_exMem->aluResult = (input1 + input2);
        // Subtracting
      } else {
        new_exMem->aluResult = (input1 - input2);
      }
      // LESS
    } else if (in->ALU.op == 3) {
      // return 0 if input1 is greater than or equal to input2
      if (input1 >= input2) {
        new_exMem->aluResult = 0;
        // Otherwise return 1
      } else {
        new_exMem->aluResult = 1;
      }
      // XOR
    } else if (in->ALU.op == 4) {
      new_exMem->aluResult = (input1 ^ input2);
      // NOP
    } else if (in->ALU.op == 5) {
      new_exMem->aluResult = 0;
      // Shifting bits for LUI
    } else if (in->ALU.op == 6) {
      new_exMem->aluResult = in->imm16 << 16;
    }
    // Determine zero output
    if (!new_exMem->aluResult) {
        new_exMem->extra1 = 1;
    } else {
        new_exMem->extra1 = 0;
    }
    // Pass on the rest of the control bits
    new_exMem->memRead = in->memRead;
    new_exMem->memToReg = in->memToReg;
    new_exMem->memWrite = in->memWrite;
    new_exMem->regWrite = in->regWrite;
    new_exMem->rt = in->rt;
    new_exMem->rtVal = in->rtVal;
    // Determine which register is being written to
    if (in->regDst) {
      new_exMem->writeReg = in->rd;
    } else {
      new_exMem->writeReg = in->rt;
    }
  }

  /*
   * Purpose: Writes any necessary registers to memory or memory values to a
   * register.
   *
   * @param in - the current instructions fields variables and control bits
   * going into the memory phase.
   *
   * @param old_memWb - holds the values currently in the pipline register
   * right before the write back phase.
   *
   * @param mem - an array simulating memory.
   *
   * @param new_memWb - the pipeline register following the memory phase.
   *
   * @return None.
   */
void execute_MEM(EX_MEM *in, MEM_WB *old_memWb,
  WORD *mem, MEM_WB *new_memwb) {
    // IF memRead is 1 then get the data at the specific memory
    // address.
    if (in->memRead) {
      new_memwb->memResult = *(mem + (in->aluResult / 4));
    } else {
      // If memWrite is 1 write the specified data to memory
      if (in->memWrite) {
        if (old_memWb->writeReg == in->rt && old_memWb->memToReg && old_memWb->regWrite) {
          // Get memResult if load word was previously called
          *(mem + (in->aluResult / 4)) = old_memWb->memResult;
          // Get aluResult from write back phase if forwarding is necessary
        } else if (old_memWb->writeReg == in->rt && !old_memWb->memToReg && old_memWb->regWrite) {
          *(mem + (in->aluResult / 4)) = old_memWb->aluResult;
        } else {
          *(mem + (in->aluResult / 4)) = in->rtVal;
        }
      }
      new_memwb->memResult = 0;
    }
    // Pass on other control bits
    new_memwb->regWrite = in->regWrite;
    new_memwb->memToReg = in->memToReg;
    new_memwb->aluResult = in->aluResult;
    new_memwb->writeReg = in->writeReg;
  }

  /*
   * Purpose: Executes the write back phase and writes to any registers if
   * necessary.
   *
   * @param in - the current instructions fields variables and control bits
   * going into the write back phase.
   *
   * @param regs - an array holding all the current values of each register.
   *
   * @return None.
   */
void execute_WB (MEM_WB *in, WORD *regs) {
  if (in->regWrite) {
    if (in->memToReg) {
      // write to register from memory if load word
      *(regs + in->writeReg) = in->memResult;
    } else {
      // write to register from aluResult otherwise
      *(regs + in->writeReg) = in->aluResult;
    }
  }
}