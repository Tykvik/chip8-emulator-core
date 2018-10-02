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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author helloween
 */
public abstract class AbstractOpcode implements Opcode {
    public static final int OPCODE_SIZE = 0x2;

    private final int           opcode;
    private final Instruction   instruction;
    private final List<String>  arguments;

    public AbstractOpcode(int opcode, Instruction instruction, String...arguments) {
        this.opcode         = opcode;
        this.instruction    = instruction;
        this.arguments      = new ArrayList<>();
        this.arguments.addAll(Arrays.asList(arguments));
    }

    @Override
    public int getRawOpcode() {
        return opcode;
    }

    public Instruction getInstruction() {
        return instruction;
    }

    public List<String> getArguments() {
        return Collections.unmodifiableList(arguments);
    }

    public int getOpcode() {
        return opcode;
    }
}
