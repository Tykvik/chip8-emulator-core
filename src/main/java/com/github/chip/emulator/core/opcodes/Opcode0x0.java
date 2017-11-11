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
import com.github.chip.emulator.core.events.ClearScreenEvent;
import com.github.chip.emulator.core.exceptions.UnsupportedOpcodeException;
import com.github.chip.emulator.core.services.EventService;
import org.apache.log4j.Logger;

/**
 * 0x0 opcode group handler
 * 00E0 - clear screen
 * 00EE - return from a subroutine
 *
 * @author helloween
 */
public class Opcode0x0 implements Opcode {
    private static final Logger LOGGER = Logger.getLogger(Opcode0x0.class);

    @Override
    public int execute(int opcode, ExecutionContext executionContext) throws UnsupportedOpcodeException {
        switch (opcode & 0x00FF) {
            case 0x00E0: {
                LOGGER.trace("clear screen");
                EventService.getInstance().postEvent(ClearScreenEvent.INSTANCE);
                break;
            }
            case 0x00EE: {
                LOGGER.trace("return from a subroutine");
                executionContext.setOffset(executionContext.getStack().pop());
                break;
            }
            default:
                throw new UnsupportedOpcodeException("unsupported 0x0XXX opcode");
        }
        return OPCODE_SIZE;
    }
}
