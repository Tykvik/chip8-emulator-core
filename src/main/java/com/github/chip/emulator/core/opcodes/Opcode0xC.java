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

import java.util.Random;

import static com.github.chip.emulator.core.opcodes.Instruction.RND;

/**
 * 0xC opcode group handler
 * 0xCXNN - sets VX to the result of a bitwise and operation on a random number and NN
 *
 * @author helloween
 */
public class Opcode0xC extends RegisterBasedOpcode {
    private static final Random random = new Random();

    private Opcode0xC(int opcode) {
        super(opcode, RND, opcode & 0x00FF);
    }

    public static Opcode newInstance(int opcode) {
        return new Opcode0xC(opcode);
    }

    @Override
    public int execute(Register register, ExecutionContext executionContext) {
        int value = random.nextInt(0xFF) & (getOpcode() & 0x00FF);
        executionContext.setRegister(new Register(register.getNumber(), value));
        return OPCODE_SIZE;
    }
}
