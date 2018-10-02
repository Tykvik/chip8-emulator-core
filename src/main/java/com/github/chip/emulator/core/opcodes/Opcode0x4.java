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

import static com.github.chip.emulator.core.opcodes.Instruction.SNE;

/**
 * 0x4 opcode code handler
 * 0x4XNN - skip the next instruction if VX doesn't equal NN
 *
 * @author helloween
 */
public class Opcode0x4 extends RegisterBasedOpcode {

    private Opcode0x4(int opcode) {
        super(opcode, SNE, opcode & 0x00FF);
    }

    public static Opcode newInstance(int opcode) {
        return new Opcode0x4(opcode);
    }

    @Override
    public int execute(Register register, ExecutionContext executionContext) {
        if (register.getValue() != (getOpcode() & 0x00FF))
            return OPCODE_SIZE * 2;
        return OPCODE_SIZE;
    }
}
