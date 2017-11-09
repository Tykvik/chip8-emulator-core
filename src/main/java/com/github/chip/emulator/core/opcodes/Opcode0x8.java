/**
 * MIT License
 * Copyright (c) 2017 Helloween
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.chip.emulator.core.opcodes;

import com.github.chip.emulator.core.ExecutionContext;
import com.github.chip.emulator.core.Register;
import com.github.chip.emulator.core.exceptions.UnsupportedOpcodeException;
import org.apache.log4j.Logger;

/**
 * 0x8 opcode group handler
 * 0x8XY0 - set VX to the value of VY
 * 0x8XY1 - set VX to VX or VY
 * 0x8XY2 - set VX to VX and VY
 * 0x8XY3 - set VX to VX xor VY
 * 0x8XY4 - add VY to VX. VF is set to 1 when there's a carry, and to 0 when there isn't
 * 0x8XY5 - VY is subtracted from VX. VF is set to 0 when there's a borrow, and 1 when there isn't
 * 0x8XY6 - shift VY right by one and copies the result to VX. VF is set to the value of the least significant bit of VY before the shift
 * 0x8XY7 - set VX to VY minus VX. VF is set to 0 when there's a borrow, and 1 when there isn't
 * 0x8XYE - shift VY left by one and copies the result to VX. VF is set to the value of the most significant bit of VY before the shift
 *
 * @author helloween
 */
public class Opcode0x8 implements Opcode {
    private static final Logger LOGGER = Logger.getLogger(Opcode0x8.class);

    @Override
    public boolean execute(int opcode, ExecutionContext executionContext) throws UnsupportedOpcodeException {
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
        return true;
    }
}
