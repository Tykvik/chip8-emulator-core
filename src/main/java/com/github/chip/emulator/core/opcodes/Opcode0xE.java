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
import org.apache.log4j.Logger;

/**
 * 0xE opcode group handler
 * 0xEX9E - skip the next instruction if the key stored in VX is pressed
 * 0xEXA1 - skip the next instruction if the key stored in VX isn't pressed
 *
 * @author helloween
 */
public class Opcode0xE extends RegisterBasedOpcode {
    private static final Logger LOGGER = Logger.getLogger(Opcode0xE.class);

    @Override
    public int execute(Register register, int opcode, ExecutionContext executionContext) throws UnsupportedOpcodeException {
        switch (opcode & 0x00FF) {
            case 0x9E : {
                LOGGER.trace(String.format("key == V%d", register.getNumber()));
                if(executionContext.getKeys()[register.getValue()]) {
                    executionContext.getKeys()[register.getValue()] = false;
                    return OPCODE_SIZE * 2;
                }
                break;
            }
            case 0xA1 : {
                LOGGER.trace(String.format("key != V%d", register.getNumber()));
                if(!executionContext.getKeys()[register.getValue()])
                    return OPCODE_SIZE * 2;
                break;
            }
            default:
                throw new UnsupportedOpcodeException("unsupported 0xEXXX opcode");
        }
        return OPCODE_SIZE;
    }
}
