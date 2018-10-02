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
import com.github.chip.emulator.core.events.WaitKeyPressEvent;
import com.github.chip.emulator.core.exceptions.InvalidRegisterNumberException;
import com.github.chip.emulator.core.exceptions.UnsupportedOpcodeException;
import com.github.chip.emulator.core.services.AsyncEventService;
import com.github.chip.emulator.core.services.EventService;

import java.awt.*;

import static com.github.chip.emulator.core.opcodes.Instruction.*;

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
public class Opcode0xF extends AbstractOpcode {
    private Opcode0xF(int opcode) {
        super(opcode, UNDEFINED);
    }

    public static Opcode newInstance(int opcode) {
        switch (opcode & 0x00FF) {
            case 0x07:
                return new Opcode0xFX07(opcode);
            case 0x0A:
                return new Opcode0xFX0A(opcode);
            case 0x15:
                return new Opcode0xFX15(opcode);
            case 0x18:
                return new Opcode0xFX18(opcode);
            case 0x1E:
                return new Opcode0xFX1E(opcode);
            case 0x29:
                return new Opcode0xFX29(opcode);
            case 0x30:
                return new Opcode0xFX30(opcode);
            case 0x33:
                return new Opcode0xFX33(opcode);
            case 0x55:
                return new Opcode0xFX55(opcode);
            case 0x65:
                return new Opcode0xFX65(opcode);
            case 0x75:
                return new Opcode0xFX75(opcode);
            case 0x85:
                return new Opcode0xFX85(opcode);
            default:
                return new Opcode0xF(opcode);
        }
    }

    @Override
    public int execute(ExecutionContext executionContext) throws UnsupportedOpcodeException {
        throw new UnsupportedOpcodeException("unsupported 0xFXXX opcode");
    }

    private static final class Opcode0xFX07 extends RegisterBasedOpcode {
        public Opcode0xFX07(int opcode) {
            super(opcode, LD, formatRegisterNumber(getRegisterNumber(opcode)), "DT");
        }

        @Override
        protected int execute(Register register, ExecutionContext executionContext) {
            executionContext.setRegister(new Register(register.getNumber(), executionContext.getDelayTimer()));
            return OPCODE_SIZE;
        }
    }

    private static final class Opcode0xFX0A extends RegisterBasedOpcode {
        public Opcode0xFX0A(int opcode) {
            super(opcode, LD, formatRegisterNumber(getRegisterNumber(opcode)), "K");
        }

        @Override
        protected int execute(Register register, ExecutionContext executionContext) {
            AsyncEventService.getInstance().postEvent(new WaitKeyPressEvent(register.getNumber()));
            return OPCODE_SIZE;
        }
    }

    private static final class Opcode0xFX15 extends RegisterBasedOpcode {
        public Opcode0xFX15(int opcode) {
            super(opcode, LD, "DT", formatRegisterNumber(getRegisterNumber(opcode)));
        }

        @Override
        protected int execute(Register register, ExecutionContext executionContext) {
            executionContext.setDelayTimer(register.getValue());
            return OPCODE_SIZE;
        }
    }

    private static final class Opcode0xFX18 extends RegisterBasedOpcode {
        public Opcode0xFX18(int opcode) {
            super(opcode, LD, "ST", formatRegisterNumber(getRegisterNumber(opcode)));
        }

        @Override
        protected int execute(Register register, ExecutionContext executionContext) {
            executionContext.setSoundTimer(register.getValue());
            return OPCODE_SIZE;
        }
    }

    private static final class Opcode0xFX1E extends RegisterBasedOpcode {
        public Opcode0xFX1E(int opcode) {
            super(opcode, ADD, "I", formatRegisterNumber(getRegisterNumber(opcode)));
        }

        @Override
        protected int execute(Register register, ExecutionContext executionContext) {
            executionContext.setRegister(new Register(0xF, executionContext.getIndexRegister().getValue() + register.getValue() > 0xFFF ? 0x1 : 0x0));
            executionContext.setIndexRegister(executionContext.getIndexRegister().add(register));
            return OPCODE_SIZE;
        }
    }

    private static final class Opcode0xFX29 extends RegisterBasedOpcode {
        public Opcode0xFX29(int opcode) {
            super(opcode, LD, "I", formatRegisterNumber(getRegisterNumber(opcode)));
        }

        @Override
        protected int execute(Register register, ExecutionContext executionContext) {
            executionContext.setIndexRegister(new IRegister(register.getValue() * 0x5));
            return OPCODE_SIZE;
        }
    }

    private static final class Opcode0xFX30 extends RegisterBasedOpcode {
        public Opcode0xFX30(int opcode) {
            super(opcode, LD, "HF", formatRegisterNumber(getRegisterNumber(opcode)));
        }

        @Override
        protected int execute(Register register, ExecutionContext executionContext) {
            executionContext.setIndexRegister(new IRegister(80 + register.getValue() * 0xA));
            return OPCODE_SIZE;
        }
    }

    private static final class Opcode0xFX33 extends RegisterBasedOpcode {
        public Opcode0xFX33(int opcode) {
            super(opcode, BCD, formatRegisterNumber(getRegisterNumber(opcode)));
        }

        @Override
        protected int execute(Register register, ExecutionContext executionContext) {
            IRegister iRegister = executionContext.getIndexRegister();

            executionContext.writeToMemory(iRegister.getValue(),        (byte) ((register.getValue() / 100) & 0xFF));
            executionContext.writeToMemory(iRegister.getValue() + 1,    (byte) (((register.getValue() / 10) % 10) & 0xFF));
            executionContext.writeToMemory(iRegister.getValue() + 2,    (byte) (((register.getValue() % 100) % 10) & 0xFF));
            return OPCODE_SIZE;
        }
    }

    private static final class Opcode0xFX55 extends RegisterBasedOpcode {
        public Opcode0xFX55(int opcode) {
            super(opcode, LD, "[I]", formatRegisterNumber(getRegisterNumber(opcode)));
        }

        @Override
        protected int execute(Register register, ExecutionContext executionContext) {
            IRegister iRegister = executionContext.getIndexRegister();

            for (int i = 0; i <= register.getNumber(); ++i) {
                executionContext.writeToMemory(iRegister.getValue() + i, (byte) executionContext.getRegister(i).getValue());
            }
            executionContext.setIndexRegister(new IRegister(register.getNumber() + 1));
            return OPCODE_SIZE;
        }
    }

    private static final class Opcode0xFX65 extends RegisterBasedOpcode {
        public Opcode0xFX65(int opcode) {
            super(opcode, LD, formatRegisterNumber(getRegisterNumber(opcode)), "[I]");
        }

        @Override
        protected int execute(Register register, ExecutionContext executionContext) {
            IRegister iRegister = executionContext.getIndexRegister();

            for (int i = 0; i <= register.getNumber(); ++i) {
                executionContext.setRegister(new Register(i, executionContext.getMemoryValue(iRegister.getValue() + i)));
            }
            executionContext.setIndexRegister(new IRegister(register.getNumber() + 1));
            return OPCODE_SIZE;
        }
    }

    private static final class Opcode0xFX75 extends RegisterBasedOpcode {
        public Opcode0xFX75(int opcode) {
            super(opcode, LD, "R", formatRegisterNumber(getRegisterNumber(opcode)));
        }

        @Override
        protected int execute(Register register, ExecutionContext executionContext) {
            for (int i = 0; i <= Math.min(register.getNumber(), 7); ++i) {
                executionContext.setUserRegister(new Register(i, executionContext.getRegister(i).getValue()));
            }
            return OPCODE_SIZE;
        }
    }

    private static final class Opcode0xFX85 extends RegisterBasedOpcode {
        public Opcode0xFX85(int opcode) {
            super(opcode, LD, formatRegisterNumber(getRegisterNumber(opcode)), "R");
        }

        @Override
        protected int execute(Register register, ExecutionContext executionContext) {
            for (int i = 0; i <= Math.min(register.getNumber(), 7); ++i) {
                executionContext.setRegister(new Register(i, executionContext.getUserRegister(i).getValue()));
            }
            return OPCODE_SIZE;
        }
    }
}
