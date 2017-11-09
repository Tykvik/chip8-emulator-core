package com.github.chip.emulator.core.opcodes;

import com.github.chip.emulator.core.ExecutionContext;
import com.github.chip.emulator.core.Register;
import com.github.chip.emulator.core.exceptions.UnsupportedOpcodeException;
import org.apache.log4j.Logger;

/**
 * @author helloween
 */
public class Opcode0x8 implements Opcode {
    private static final Logger LOGGER = Logger.getLogger(Opcode0x8.class);

    @Override
    public void execute(int opcode, ExecutionContext executionContext) throws UnsupportedOpcodeException {
        int firstRegister       = (opcode & 0x0F00) >> 8;
        int secondRegister      = (opcode & 0x00F0) >> 4;
        Register[] registers    = executionContext.getRegisters();

        switch (opcode & 0x000F) {
            case 0x0: {
                LOGGER.trace(String.format("V%d = V%d", firstRegister, secondRegister));
                registers[firstRegister].setValue(registers[secondRegister].getValue());
                break;
            }
            case 0x1: {
                LOGGER.trace(String.format("V%d |= V%d", firstRegister, secondRegister));
                registers[firstRegister].or(registers[secondRegister]);
                break;
            }
            case 0x2: {
                LOGGER.trace(String.format("V%d &= V%d", firstRegister, secondRegister));
                registers[firstRegister].and(registers[secondRegister]);
                break;
            }
            case 0x3: {
                LOGGER.trace(String.format("V%d ^= V%d", firstRegister, secondRegister));
                registers[firstRegister].xor(registers[secondRegister]);
                break;
            }
            case 0x4: {
                LOGGER.trace(String.format("V%d += V%d", firstRegister, secondRegister));
                if (registers[firstRegister].add(registers[secondRegister]))
                    registers[15].setValue(0x1);
                else
                    registers[15].setValue(0x0);
                break;
            }
            case 0x5: {
                LOGGER.trace(String.format("V%d -= V%d", firstRegister, secondRegister));
                if (registers[firstRegister].sub(registers[secondRegister]))
                    registers[15].setValue(0x0);
                else
                    registers[15].setValue(0x1);
                break;
            }
            case 0x6: {
                LOGGER.trace(String.format("V%d = V%d = V%d >> 1", firstRegister, secondRegister, secondRegister));
                registers[15].setValue(registers[secondRegister].rightShift(1));
                registers[firstRegister].setValue(registers[secondRegister].getValue());
                break;
            }
            case 0x7: {
                LOGGER.trace(String.format("V%d = V%d - V%d", firstRegister, secondRegister, firstRegister));
                registers[15].setValue(registers[secondRegister].getValue() < registers[firstRegister].getValue() ? 0x0 : 0x1);
                registers[firstRegister].setValue((registers[secondRegister].getValue() - registers[firstRegister].getValue()) & 0xFF);
                break;
            }
            case 0xE: {
                LOGGER.trace(String.format("V%d = V%d = V%d << 1", firstRegister, secondRegister, secondRegister));
                registers[15].setValue(registers[secondRegister].leftShift(1));
                registers[firstRegister].setValue(registers[secondRegister].getValue());
                break;
            }
            default:
                throw new UnsupportedOpcodeException("unsupported 0x8XXX opcode");
        }
    }
}
