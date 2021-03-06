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
import com.github.chip.emulator.core.exceptions.InvalidRegisterNumberException;
import com.github.chip.emulator.core.exceptions.UnsupportedOpcodeException;

/**
 * @author helloween
 */
public abstract class TwoRegistersBasedOpcode extends RegisterBasedOpcode {

    public TwoRegistersBasedOpcode(int opcode, Instruction instruction, String...arguments) {
        super(opcode, instruction, arguments);
    }

    public TwoRegistersBasedOpcode(int opcode, Instruction instruction) {
        super(opcode, instruction, formatRegisterNumber(getRegisterNumber(opcode)),
                                   formatRegisterNumber(getSecondRegisterNumber(opcode)));
    }

    @Override
    protected int execute(Register register, ExecutionContext executionContext) throws UnsupportedOpcodeException, InvalidRegisterNumberException {
        int secondRegister = getSecondRegisterNumber(getOpcode());
        checkRegisterRange(secondRegister);

        return execute(register, executionContext.getRegister(secondRegister), executionContext);
    }

    protected abstract int execute(Register firstRegister, Register secondRegister, ExecutionContext executionContext) throws UnsupportedOpcodeException;

    protected static int getSecondRegisterNumber(int opcode) {
        return (opcode & 0x00F0) >> 4;
    }
}
