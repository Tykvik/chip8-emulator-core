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

import com.github.chip.emulator.core.exceptions.UnsupportedOpcodeException;

/**
 * @author helloween
 */
public final class OpcodeFactory {
    private static final OpcodeFactory INSTANCE = new OpcodeFactory();

    private OpcodeFactory() {
    }

    public static OpcodeFactory getInstance() {
        return INSTANCE;
    }

    public Opcode newOpcode(int opcode) throws UnsupportedOpcodeException {
        switch (opcode & 0xF000) {
            case 0x0000:
                return Opcode0x0.newInstance(opcode);
            case 0x1000:
                return Opcode0x1.newInstance(opcode);
            case 0x2000:
                return Opcode0x2.newInstance(opcode);
            case 0x3000:
                return Opcode0x3.newInstance(opcode);
            case 0x4000:
                return Opcode0x4.newInstance(opcode);
            case 0x5000:
                return Opcode0x5.newInstance(opcode);
            case 0x6000:
                return Opcode0x6.newInstance(opcode);
            case 0x7000:
                return Opcode0x7.newInstance(opcode);
            case 0x8000:
                return Opcode0x8.newInstance(opcode);
            case 0x9000:
                return Opcode0x9.newInstance(opcode);
            case 0xA000:
                return Opcode0xA.newInstance(opcode);
            case 0xB000:
                return Opcode0xB.newInstance(opcode);
            case 0xC000:
                return Opcode0xC.newInstance(opcode);
            case 0xD000:
                return Opcode0xD.newInstance(opcode);
            case 0xE000:
                return Opcode0xE.newInstance(opcode);
            case 0xF000:
                return Opcode0xF.newInstance(opcode);
            default:
                throw new UnsupportedOpcodeException(String.format("unsupported opcode #%04X", opcode));
        }
    }
}
