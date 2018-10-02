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

import static com.github.chip.emulator.core.opcodes.Instruction.*;

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
public class Opcode0x8 extends AbstractOpcode {
    private Opcode0x8(int opcode) {
        super(opcode, Instruction.UNDEFINED);
    }

    public static Opcode newInstance(int opcode) {
        switch (opcode & 0x000F) {
            case 0x0:
                return new Opcode0x8XX0(opcode);
            case 0x1:
                return new Opcode0x8XX1(opcode);
            case 0x2:
                return new Opcode0x8XX2(opcode);
            case 0x3:
                return new Opcode0x8XX3(opcode);
            case 0x4:
                return new Opcode0x8XX4(opcode);
            case 0x5:
                return new Opcode0x8XX5(opcode);
            case 0x6:
                return new Opcode0x8XX6(opcode);
            case 0x7:
                return new Opcode0x8XX7(opcode);
            case 0xE:
                return new Opcode0x8XXE(opcode);
            default:
                return new Opcode0x8(opcode);
        }
    }

    @Override
    public int execute(ExecutionContext executionContext) throws UnsupportedOpcodeException {
        throw new UnsupportedOpcodeException("unsupported 0x8XXX opcode");
    }

    private static final class Opcode0x8XX0 extends TwoRegistersBasedOpcode {
        public Opcode0x8XX0(int opcode) {
            super(opcode, LD);
        }

        @Override
        protected int execute(Register firstRegister, Register secondRegister, ExecutionContext executionContext) {
            executionContext.setRegister(new Register(firstRegister.getNumber(), secondRegister.getValue()));
            return OPCODE_SIZE;
        }
    }

    private static final class Opcode0x8XX1 extends TwoRegistersBasedOpcode {
        public Opcode0x8XX1(int opcode) {
            super(opcode, OR);
        }

        @Override
        protected int execute(Register firstRegister, Register secondRegister, ExecutionContext executionContext) {
            executionContext.setRegister(firstRegister.or(secondRegister));
            return OPCODE_SIZE;
        }
    }

    private static final class Opcode0x8XX2 extends TwoRegistersBasedOpcode {
        public Opcode0x8XX2(int opcode) {
            super(opcode, AND);
        }

        @Override
        protected int execute(Register firstRegister, Register secondRegister, ExecutionContext executionContext) {
            executionContext.setRegister(firstRegister.and(secondRegister));
            return OPCODE_SIZE;
        }
    }

    private static final class Opcode0x8XX3 extends TwoRegistersBasedOpcode {
        public Opcode0x8XX3(int opcode) {
            super(opcode, XOR);
        }

        @Override
        protected int execute(Register firstRegister, Register secondRegister, ExecutionContext executionContext) {
            executionContext.setRegister(firstRegister.xor(secondRegister));
            return OPCODE_SIZE;
        }
    }

    private static final class Opcode0x8XX4 extends TwoRegistersBasedOpcode {
        public Opcode0x8XX4(int opcode) {
            super(opcode, ADD);
        }

        @Override
        protected int execute(Register firstRegister, Register secondRegister, ExecutionContext executionContext) {
            executionContext.setRegister(new Register(0xF, firstRegister.getValue() + secondRegister.getValue() > 0xFF ? 0x1 : 0x0));
            executionContext.setRegister(firstRegister.add(secondRegister));
            return OPCODE_SIZE;
        }
    }

    private static final class Opcode0x8XX5 extends TwoRegistersBasedOpcode {
        public Opcode0x8XX5(int opcode) {
            super(opcode, SUB);
        }

        @Override
        protected int execute(Register firstRegister, Register secondRegister, ExecutionContext executionContext) {
            executionContext.setRegister(new Register(0xF, firstRegister.getValue() < secondRegister.getValue() ? 0x0 : 0x1));
            executionContext.setRegister(firstRegister.sub(secondRegister));
            return OPCODE_SIZE;
        }
    }

    private static final class Opcode0x8XX6 extends TwoRegistersBasedOpcode {
        public Opcode0x8XX6(int opcode) {
            super(opcode, SHR);
        }

        @Override
        protected int execute(Register firstRegister, Register secondRegister, ExecutionContext executionContext) {
            executionContext.setRegister(firstRegister.rightShift());
            executionContext.setRegister(new Register(0xF, firstRegister.getValue() & 0x1));
            return OPCODE_SIZE;
        }
    }

    private static final class Opcode0x8XX7 extends TwoRegistersBasedOpcode {
        public Opcode0x8XX7(int opcode) {
            super(opcode, SUBN);
        }

        @Override
        protected int execute(Register firstRegister, Register secondRegister, ExecutionContext executionContext) {
            executionContext.setRegister(new Register(0xF, secondRegister.getValue() < firstRegister.getValue() ? 0x0 : 0x1));
            executionContext.setRegister(new Register(firstRegister.getNumber(), secondRegister.getValue() - firstRegister.getValue()));
            return OPCODE_SIZE;
        }
    }

    private static final class Opcode0x8XXE extends TwoRegistersBasedOpcode {
        public Opcode0x8XXE(int opcode) {
            super(opcode, SHL);
        }

        @Override
        protected int execute(Register firstRegister, Register secondRegister, ExecutionContext executionContext) {
            executionContext.setRegister(firstRegister.leftShift());
            executionContext.setRegister(new Register(0xF, (firstRegister.getValue() & 0x80) >> 7));
            return OPCODE_SIZE;
        }
    }
}
