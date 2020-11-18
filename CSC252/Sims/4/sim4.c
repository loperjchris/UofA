/* AUTHOR: Ruben Tequida
 * FILE: sim4.c
 * ASSIGNMENT: Simulation #4: Single-Cycle CPU
 * COURSE: CSc 252; Fall 2019
 * Purpose: This program replicates the function of a single-cycle CPU. For
 * milestone 1 this program will take in an instruction, breakdown the
 * instructino into its proper fields, and determine the output of the control
 * ALU. For Milestone 2 the inputs for th ALU will be set, the ALU executed,
 * determing if memory is written to or read from, the next PC is calculated,
 * and any necessary registers are written to.
 */
#include "sim4.h"

WORD getInstruction(WORD curPC, WORD *instructionMemory) {
  return instructionMemory[curPC / 4];
}

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
     * Purpose: Sets the output for the control ALU depending on the
     * instruction that was given.
     *
     * @param fields - provides acces to the field variables of the
     * instructionFields method.
     *
     * @param controlOut - provides acces to the field variables of the
     * CPUControl method.
     *
     * @return 1 if its a valid instruction and 0 if it isn't.
     */
int  fill_CPUControl(InstructionFields *fields, CPUControl *controlOut) {
  // R-format instructions
  if (fields->opcode == 0) {
    // add
    if (fields->funct == 0x20) {
      controlOut->ALUsrc = 0;
      controlOut->ALU.op = 0x2;
      controlOut->ALU.bNegate = 0;
      controlOut->memRead = 0;
      controlOut->memWrite = 0;
      controlOut->memToReg = 0;
      controlOut->regDst = 1;
      controlOut->regWrite = 1;
      controlOut->branch = 0;
      controlOut->jump = 0;
      return 1;
    }
    // addu
    if (fields->funct == 0x21) {
      controlOut->ALUsrc = 0;
      controlOut->ALU.op = 0x2;
      controlOut->ALU.bNegate = 0;
      controlOut->memRead = 0;
      controlOut->memWrite = 0;
      controlOut->memToReg = 0;
      controlOut->regDst = 1;
      controlOut->regWrite = 1;
      controlOut->branch = 0;
      controlOut->jump = 0;
      return 1;
    }
    // sub
    if (fields->funct == 0x22) {
      controlOut->ALUsrc = 0;
      controlOut->ALU.op = 0x2;
      controlOut->ALU.bNegate = 1;
      controlOut->memRead = 0;
      controlOut->memWrite = 0;
      controlOut->memToReg = 0;
      controlOut->regDst = 1;
      controlOut->regWrite = 1;
      controlOut->branch = 0;
      controlOut->jump = 0;
      return 1;
    }
    // subu
    if (fields->funct == 0x23) {
      controlOut->ALUsrc = 0;
      controlOut->ALU.op = 0x2;
      controlOut->ALU.bNegate = 1;
      controlOut->memRead = 0;
      controlOut->memWrite = 0;
      controlOut->memToReg = 0;
      controlOut->regDst = 1;
      controlOut->regWrite = 1;
      controlOut->branch = 0;
      controlOut->jump = 0;
      return 1;
    }
    // and
    if (fields->funct == 0x24) {
      controlOut->ALUsrc = 0;
      controlOut->ALU.op = 0;
      controlOut->ALU.bNegate = 0;
      controlOut->memRead = 0;
      controlOut->memWrite = 0;
      controlOut->memToReg = 0;
      controlOut->regDst = 1;
      controlOut->regWrite = 1;
      controlOut->branch = 0;
      controlOut->jump = 0;
      return 1;
    }
    // or
    if (fields->funct == 0x25) {
      controlOut->ALUsrc = 0;
      controlOut->ALU.op = 1;
      controlOut->ALU.bNegate = 0;
      controlOut->memRead = 0;
      controlOut->memWrite = 0;
      controlOut->memToReg = 0;
      controlOut->regDst = 1;
      controlOut->regWrite = 1;
      controlOut->branch = 0;
      controlOut->jump = 0;
      return 1;
    }
    // xor
    if (fields->funct == 0x26) {
      controlOut->ALUsrc = 0;
      controlOut->ALU.op = 0x4;
      controlOut->ALU.bNegate = 0;
      controlOut->memRead = 0;
      controlOut->memWrite = 0;
      controlOut->memToReg = 0;
      controlOut->regDst = 1;
      controlOut->regWrite = 1;
      controlOut->branch = 0;
      controlOut->jump = 0;
      return 1;
    }
    // slt
    if (fields->funct == 0x2a) {
      controlOut->ALUsrc = 0;
      controlOut->ALU.op = 0x3;
      controlOut->ALU.bNegate = 1;
      controlOut->memRead = 0;
      controlOut->memWrite = 0;
      controlOut->memToReg = 0;
      controlOut->regDst = 1;
      controlOut->regWrite = 1;
      controlOut->branch = 0;
      controlOut->jump = 0;
      return 1;
    }
    // Invalid R-format instruction
    controlOut->ALUsrc = 0;
    controlOut->ALU.op = 0;
    controlOut->ALU.bNegate = 0;
    controlOut->memRead = 0;
    controlOut->memWrite = 0;
    controlOut->memToReg = 0;
    controlOut->regDst = 0;
    controlOut->regWrite = 0;
    controlOut->branch = 0;
    controlOut->jump = 0;
    return 0;
  }
  // I-format instructions
  // addi
  if (fields->opcode == 0x8) {
    controlOut->ALUsrc = 1;
    controlOut->ALU.op = 0x2;
    controlOut->ALU.bNegate = 0;
    controlOut->memRead = 0;
    controlOut->memWrite = 0;
    controlOut->memToReg = 0;
    controlOut->regDst = 0;
    controlOut->regWrite = 1;
    controlOut->branch = 0;
    controlOut->jump = 0;
    return 1;
  }
  // addiu
  if (fields->opcode == 0x9) {
    controlOut->ALUsrc = 1;
    controlOut->ALU.op = 0x2;
    controlOut->ALU.bNegate = 0;
    controlOut->memRead = 0;
    controlOut->memWrite = 0;
    controlOut->memToReg = 0;
    controlOut->regDst = 0;
    controlOut->regWrite = 1;
    controlOut->branch = 0;
    controlOut->jump = 0;
    return 1;
  }
  // slti
  if (fields->opcode == 0xa) {
    controlOut->ALUsrc = 1;
    controlOut->ALU.op = 0x3;
    controlOut->ALU.bNegate = 1;
    controlOut->memRead = 0;
    controlOut->memWrite = 0;
    controlOut->memToReg = 0;
    controlOut->regDst = 0;
    controlOut->regWrite = 1;
    controlOut->branch = 0;
    controlOut->jump = 0;
    return 1;
  }
  // lw
  if (fields->opcode == 0x23) {
    controlOut->ALUsrc = 1;
    controlOut->ALU.op = 0x2;
    controlOut->ALU.bNegate = 0;
    controlOut->memRead = 1;
    controlOut->memWrite = 0;
    controlOut->memToReg = 1;
    controlOut->regDst = 0;
    controlOut->regWrite = 1;
    controlOut->branch = 0;
    controlOut->jump = 0;
    return 1;
  }
  // sw
  if (fields->opcode == 0x2b) {
    controlOut->ALUsrc = 1;
    controlOut->ALU.op = 0x2;
    controlOut->ALU.bNegate = 0;
    controlOut->memRead = 0;
    controlOut->memWrite = 1;
    controlOut->memToReg = 0;
    controlOut->regDst = 0;
    controlOut->regWrite = 0;
    controlOut->branch = 0;
    controlOut->jump = 0;
    return 1;
  }
  // beq
  if (fields->opcode == 0x4) {
    controlOut->ALUsrc = 0;
    controlOut->ALU.op = 0x2;
    controlOut->ALU.bNegate = 1;
    controlOut->memRead = 0;
    controlOut->memWrite = 0;
    controlOut->memToReg = 0;
    controlOut->regDst = 0;
    controlOut->regWrite = 0;
    controlOut->branch = 1;
    controlOut->jump = 0;
    return 1;
  }
  // j
  if (fields->opcode == 0x2) {
    controlOut->ALUsrc = 0;
    controlOut->ALU.op = 0;
    controlOut->ALU.bNegate = 0;
    controlOut->memRead = 0;
    controlOut->memWrite = 0;
    controlOut->memToReg = 0;
    controlOut->regDst = 0;
    controlOut->regWrite = 0;
    controlOut->branch = 0;
    controlOut->jump = 1;
    return 1;
  }
  // xori - Extra Instruction
  if (fields->opcode == 0xe) {
    controlOut->ALUsrc = 1;
    controlOut->ALU.op = 0x4;
    controlOut->ALU.bNegate = 0;
    controlOut->memRead = 0;
    controlOut->memWrite = 0;
    controlOut->memToReg = 0;
    controlOut->regDst = 0;
    controlOut->regWrite = 1;
    controlOut->branch = 0;
    controlOut->jump = 0;
    return 1;
  }
  // bne - Extra Instruction
  if (fields->opcode == 0x5) {
    controlOut->ALUsrc = 0;
    controlOut->ALU.op = 0x2;
    controlOut->ALU.bNegate = 1;
    controlOut->memRead = 0;
    controlOut->memWrite = 0;
    controlOut->memToReg = 0;
    controlOut->regDst = 0;
    controlOut->regWrite = 0;
    controlOut->branch = 1;
    controlOut->jump = 0;
    return 1;
  }
  // lui - Extra Instruction
  if (fields->opcode == 0xf) {
    controlOut->ALUsrc = 1;
    controlOut->ALU.op = 0x2;
    controlOut->ALU.bNegate = 0;
    controlOut->memRead = 0;
    controlOut->memWrite = 0;
    controlOut->memToReg = 0;
    controlOut->regDst = 0;
    controlOut->regWrite = 1;
    controlOut->branch = 0;
    controlOut->jump = 0;
    return 1;
  }
  // Invalid I-format instruction
    controlOut->ALUsrc = 0;
    controlOut->ALU.op = 0;
    controlOut->ALU.bNegate = 0;
    controlOut->memRead = 0;
    controlOut->memWrite = 0;
    controlOut->memToReg = 0;
    controlOut->regDst = 0;
    controlOut->regWrite = 0;
    controlOut->branch = 0;
    controlOut->jump = 0;
    return 0;
}

/*
 * Purpose: Sets the top input to the ALU. Due to the limited number of
 * instructions only rsVal is used as an input.
 *
 * @param controlIn - provides acces to the field variables of the
 * CPUControl method.
 *
 * @param fieldsIn - provides acces to the field variables of the
 * instructionFields method.
 *
 * @param rsVal, rtVal - 5 bit values that represent the rs and tr registers
 * set in extract_instructionFields.
 *
 * @param reg32, reg33 - are unused registers.
 *
 * @param oldPC - the PC value of the currenlty running instruction.
 *
 * @return The desired top input to the ALU.
 */
WORD getALUinput1(CPUControl *controlIn,
                  InstructionFields *fieldsIn,
                  WORD rsVal, WORD rtVal, WORD reg32, WORD reg33,
                  WORD oldPC) {
                    return rsVal;
                  }

/*
 * Purpose: Sets the bottom input to the ALU. Due to the limited number of
 * instructions only rsVal is used as an input.
 *
 * @param controlIn - provides acces to the field variables of the
 * CPUControl method.
 *
 * @param fieldsIn - provides acces to the field variables of the
 * instructionFields method.
 *
 * @param rsVal, rtVal - 5 bit values that represent the rs and tr registers
 * set in extract_instructionFields.
 *
 * @param reg32, reg33 - are unused registers.
 *
 * @param oldPC - the PC value of the currenlty running instruction.
 *
 * @return The desired bottom input to the ALU.
 */
WORD getALUinput2(CPUControl *controlIn,
                  InstructionFields *fieldsIn,
                  WORD rsVal, WORD rtVal, WORD reg32, WORD reg33,
                  WORD oldPC) {
                    if (fieldsIn->opcode == 0xe) {
                      // Returns the 32 bit zero extended imm16 for XORi
                      return (fieldsIn->imm16 & 0x0000ffff);
                    }
                    // Instructions that don't use the imm16 field
                    if (!controlIn->ALUsrc) {
                      return rtVal;
                      // Instructions that use the imm16 field
                    } else {
                      // Instruction lui extends imm16 by 16 zero bits
                      if (fieldsIn->opcode == 0xf) {
                        return fieldsIn->imm16 << 16;
                      }
                      return fieldsIn->imm32;
                    }
                  }

/*
 * Purpose: Executes the ALU and determines the result of the ALU and whether
 * the result is zero or not.
 *
 * @param controlIn - provides acces to the field variables of the
 * CPUControl method.
 *
 * @param input1, input2 - are the variables previously set that will be
 * operated on.
 *
 * @param aluResultOut - provides access to the field variables of the ALUResult
 * method.
 *
 * @return None.
 */
void execute_ALU(CPUControl *controlIn,
                 WORD input1, WORD input2,
                 ALUResult  *aluResultOut) {
                   // AND
                   if (controlIn->ALU.op == 0) {
                     aluResultOut->result = (input1 & input2);
                     // OR
                   } else if (controlIn->ALU.op == 1) {
                     aluResultOut->result = (input1 | input2);
                     // ADDER
                   } else if (controlIn->ALU.op == 2) {
                     // Adding
                     if (controlIn->ALU.bNegate == 0) {
                       aluResultOut->result = (input1 + input2);
                       // Subtracting
                     } else {
                       aluResultOut->result = (input1 - input2);
                     }
                     // LESS
                   } else if (controlIn->ALU.op == 3) {
                     // return 0 if input1 is greater than or equal to input2
                     if (input1 >= input2) {
                       aluResultOut->result = 0;
                       // Otherwise return 1
                     } else {
                       aluResultOut->result = 1;
                     }
                     // XOR
                   } else if (controlIn->ALU.op == 4) {
                     aluResultOut->result = (input1 ^ input2);
                   }
                   // Determine zero output
                   if (!aluResultOut->result) {
                     aluResultOut->zero = 1;
                   } else {
                     aluResultOut->zero = 0;
                   }
                 }

/*
 * Purpose: Determines if anything is written to or read from memory.
 *
 * @param controlIn - provides acces to the field variables of the
 * CPUControl method.
 *
 * @param aluResultsIn - provides access to the field variables of ALUResult
 * method.
 *
 * @param rsVal, rtVal - 5 bit values that represent the rs and tr registers
 * set in extract_instructionFields.
 *
 * @param memory - represents the data memory. A pointer to the first item of
 * the memory array.
 *
 * @param resultOut - provides access to readVal variable in MemResult method.
 *
 * @return None.
 */
void execute_MEM(CPUControl *controlIn,
                 ALUResult  *aluResultIn,
                 WORD        rsVal, WORD rtVal,
                 WORD       *memory,
                 MemResult  *resultOut) {
                   // IF memRead is 1 then get the data at the specific memory
                   // address.
                   if (controlIn->memRead) {
                     resultOut->readVal = *(memory + (aluResultIn->result / 4));
                   } else {
                     // If memWrite is 1 write the specified data to memory
                     if (controlIn->memWrite) {
                       *(memory + (aluResultIn->result / 4)) = rtVal;
                     }
                     resultOut->readVal = 0;
                   }
                 }
/*
 * Purpose: Determines the next PC based off branch and jump values.
 *
 * @param fieldsIn - provides acces to the field variables of the
 * instructionFields method.
 *
 * @param controlIn - provides acces to the field variables of the
 * CPUControl method.
 *
 * @param aluZero - the 1 bit value returned from the ALU stating whether the
 * ALU result was zero or not.
 *
 * @param rsVal, rtVal - 5 bit values that represent the rs and tr registers
 * set in extract_instructionFields.
 *
 * @param oldPC - the PC of the current instruction that is running.
 *
 * @return the address of the next instruction.
 */
WORD getNextPC(InstructionFields *fields, CPUControl *controlIn, int aluZero,
               WORD rsVal, WORD rtVal,
               WORD oldPC) {
                 WORD new = oldPC + 4;
                 // Where to go if a jump instruction is called
                 if (controlIn->jump) {
                   return ((fields->address << 2) | (new & 0xf0000000));
                 }
                 // Setting next PC for beq instruction if equal
                 if (controlIn->branch && aluZero && fields->opcode == 0x4) {
                   return (new + (fields->imm32 << 2));
                 }
                 // Setting next PC for bne instruction if not equal
                 if (controlIn->branch && !aluZero && fields->opcode == 0x5) {
                   return (new + (fields->imm32 << 2));
                 }
                 return new;
               }

/*
 * Purpose: Determines what to write to the necessary register.
 *
 * @param fieldsIn - provides acces to the field variables of the
 * instructionFields method.
 *
 * @param controlIn - provides acces to the field variables of the
 * CPUControl method.
 *
 * @param aluResultsIn - provides access to the field variables of ALUResult
 * method.
 *
 * @param memResultIn - provides access to readVal variable in MemResult method.
 *
 * @param regs - a pointer to the register array.
 *
 * @return None.
 */
void execute_updateRegs(InstructionFields *fields, CPUControl *controlIn,
                        ALUResult  *aluResultIn, MemResult *memResultIn,
                        WORD       *regs) {
                          WORD input;
                          WORD regi;
                          if (controlIn->regWrite) {
                            if (controlIn->memToReg) {
                              input = memResultIn->readVal;
                            } else {
                              input = aluResultIn->result;
                            }
                            if (controlIn->regDst) {
                              regi = fields->rd;
                            } else {
                              regi = fields->rt;
                            }
                            *(regs + regi) = input;
                          }
                        }