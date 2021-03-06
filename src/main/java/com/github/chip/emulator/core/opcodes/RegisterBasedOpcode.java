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

import static com.github.chip.emulator.core.formats.Formats.REGISTER_NUMBER_FORMAT;
import static com.github.chip.emulator.core.formats.Formats.REGISTER_VALUE_FORMAT;

/**
 * @author helloween
 */
public abstract class RegisterBasedOpcode extends AbstractOpcode {

    public RegisterBasedOpcode(int opcode, Instruction instruction, String...arguments) {
        super(opcode, instruction, arguments);
    }

    public RegisterBasedOpcode(int opcode, Instruction instruction, int value) {
        super(opcode, instruction, formatRegisterNumber(getRegisterNumber(opcode)), REGISTER_VALUE_FORMAT.format(value));
    }

    @Override
    public int execute(ExecutionContext executionContext) throws UnsupportedOpcodeException, InvalidRegisterNumberException {
        int register = getRegisterNumber(getOpcode());
        checkRegisterRange(register);

        return execute(executionContext.getRegister(register), executionContext);
    }

    protected abstract int execute(Register register, ExecutionContext executionContext) throws UnsupportedOpcodeException, InvalidRegisterNumberException;

    protected void checkRegisterRange(int register) throws InvalidRegisterNumberException {
        if (register < 0 || register > 0xF)
            throw new InvalidRegisterNumberException(String.format("invalid register number : %d", register));
    }

    protected static int getRegisterNumber(int opcode) {
        return (opcode & 0x0F00) >> 8;
    }

    protected static String formatRegisterNumber(int registerNumber) {
        return REGISTER_NUMBER_FORMAT.format(registerNumber);
    }
}
