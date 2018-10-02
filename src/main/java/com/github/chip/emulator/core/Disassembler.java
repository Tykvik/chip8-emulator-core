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
package com.github.chip.emulator.core;

import com.github.chip.emulator.core.exceptions.UnsupportedOpcodeException;
import com.github.chip.emulator.core.opcodes.*;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author helloween
 */
public class Disassembler {
    private final ByteBuffer    programBuffer;
    private final List<Opcode>  opcodes;

    public Disassembler(ByteBuffer programBuffer) {
        this.programBuffer = programBuffer;
        this.opcodes       = new ArrayList<>();
    }

    public List<Opcode> disassemble() throws UnsupportedOpcodeException {
        programBuffer.rewind();
        while (programBuffer.hasRemaining()) {
            int opcode = ((programBuffer.get() << 8) | (programBuffer.get() & 0x00FF)) & 0xFFFF;
            opcodes.add(OpcodeFactory.getInstance().newOpcode(opcode));
        }
        return opcodes;
    }
}
