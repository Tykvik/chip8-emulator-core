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
 * 0xE opcode group handler
 * 0xEX9E - skip the next instruction if the key stored in VX is pressed
 * 0xEXA1 - skip the next instruction if the key stored in VX isn't pressed
 *
 * @author helloween
 */
public class Opcode0xE extends AbstractOpcode {
    private Opcode0xE(int opcode) {
        super(opcode, UNDEFINED);
    }

    public static Opcode newInstance(int opcode) {
        switch (opcode & 0x00FF) {
            case 0x9E:
                return new Opcode0xEX9E(opcode);
            case 0xA1:
                return new Opcode0xEXA1(opcode);
            default:
                return new Opcode0xE(opcode);
        }
    }

    @Override
    public int execute(ExecutionContext executionContext) throws UnsupportedOpcodeException {
        throw new UnsupportedOpcodeException("unsupported 0xEXXX opcode");
    }

    private static final class Opcode0xEX9E extends RegisterBasedOpcode {
        public Opcode0xEX9E(int opcode) {
            super(opcode, SKP, formatRegisterNumber(getRegisterNumber(opcode)));
        }

        @Override
        protected int execute(Register register, ExecutionContext executionContext) {
            if(executionContext.getKey(register.getValue())) {
                executionContext.setKey(register.getValue(), false);
                return OPCODE_SIZE * 2;
            }
            return OPCODE_SIZE;
        }
    }

    private static final class Opcode0xEXA1 extends RegisterBasedOpcode {
        public Opcode0xEXA1(int opcode) {
            super(opcode, SKNP, formatRegisterNumber(getRegisterNumber(opcode)));
        }

        @Override
        protected int execute(Register register, ExecutionContext executionContext) {
            if(!executionContext.getKey(register.getValue()))
                return OPCODE_SIZE * 2;
            executionContext.setKey(register.getValue(), false);
            return OPCODE_SIZE;
        }
    }
}
