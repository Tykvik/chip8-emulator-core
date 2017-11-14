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
import com.github.chip.emulator.core.IRegister;
import com.github.chip.emulator.core.Register;
import com.github.chip.emulator.core.exceptions.UnsupportedOpcodeException;
import org.apache.log4j.Logger;

/**
 * 0xF opcode group handler
 * 0xFX07 - set VX to the value of the delay timer
 * 0xFX0A - a key press is awaited, and then stored in VX
 * 0xFX15 - set the delay timer to VX
 * 0xFX18 - set the sound timer to VX
 * 0xFX1E - add VX to I
 * 0xFX29 - set I to the location of the sprite for the character in VX. Characters 0-F (in hexadecimal) are represented by a 4x5 font
 * 0xFX33 - stores the binary-coded decimal representation of VX, with the most significant of three digits at the address in I,
 *          the middle digit at I plus 1, and the least significant digit at I plus 2
 * 0xFX55 - store V0 to VX (including VX) in memory starting at address I. I is increased by 1 for each value written
 * 0xFX65 - fill V0 to VX (including VX) with values from memory starting at address I. I is increased by 1 for each value written
 *
 * @author helloween
 */
public class Opcode0xF extends RegisterBasedOpcode {
    private static final Logger LOGGER = Logger.getLogger(Opcode0xF.class);

    @Override
    public int execute(Register register, int opcode, ExecutionContext executionContext) throws UnsupportedOpcodeException {
        switch (opcode & 0x00FF) {
            case 0x07: {
                LOGGER.trace(String.format("V%d = delayTimer", register.getNumber()));
                executionContext.setRegister(new Register(register.getNumber(), executionContext.getDelayTimer()));
                break;
            }
            case 0x0A: {
                LOGGER.trace(String.format("V%d = key", register.getNumber()));
                break;
            }
            case 0x15: {
                LOGGER.trace(String.format("delayTimer = V%d", register.getNumber()));
                executionContext.setDelayTimer(register.getValue());
                break;
            }
            case 0x18: {
                LOGGER.trace(String.format("soundTimer = V%d", register.getNumber()));
                executionContext.setSoundTimer(register.getValue());
                break;
            }
            case 0x1E: {
                LOGGER.trace(String.format("I += V%d", register.getNumber()));
                executionContext.setIndexRegister(executionContext.getIndexRegister().add(register));
                executionContext.setRegister(new Register(0xF, executionContext.getIndexRegister().getValue() + register.getValue() > 0xFFF ? 0x1 : 0x0));
                break;
            }
            case 0x29: {
                LOGGER.trace(String.format("I = sprite_addr[V%d]", register.getNumber()));
                executionContext.setIndexRegister(new IRegister(register.getValue() * 5));
                break;
            }
            case 0x33: {
                LOGGER.trace("BCD");
                IRegister iRegister = executionContext.getIndexRegister();

                executionContext.writeToMemory(iRegister.getValue(),        (byte) ((register.getValue() / 100) & 0xFF));
                executionContext.writeToMemory(iRegister.getValue() + 1,    (byte) (((register.getValue() / 10) % 10) & 0xFF));
                executionContext.writeToMemory(iRegister.getValue() + 2,    (byte) (((register.getValue() % 100) % 10) & 0xFF));
                break;
            }
            case 0x55: {
                LOGGER.trace("reg_dump(Vx, &I)");
                IRegister iRegister = executionContext.getIndexRegister();

                for (int i = 0; i <= register.getNumber(); ++i) {
                    executionContext.writeToMemory(iRegister.getValue() + i, (byte) executionContext.getRegister(i).getValue());
                }
                executionContext.setIndexRegister(new IRegister(register.getNumber() + 1));

                break;
            }
            case 0x65: {
                LOGGER.trace("reg_load(Vx, &I)");
                IRegister iRegister = executionContext.getIndexRegister();

                for (int i = 0; i <= register.getNumber(); ++i) {
                    executionContext.setRegister(new Register(i, executionContext.getMemoryValue(iRegister.getValue() + i)));
                }
                executionContext.setIndexRegister(new IRegister(register.getNumber() + 1));
                break;
            }
            default:
                throw new UnsupportedOpcodeException("unsupported 0xFXXX opcode");
        }
        return OPCODE_SIZE;
    }
}
