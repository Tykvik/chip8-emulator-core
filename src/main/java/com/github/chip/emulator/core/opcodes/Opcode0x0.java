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
import com.github.chip.emulator.core.events.*;
import com.github.chip.emulator.core.exceptions.InvalidRegisterNumberException;
import com.github.chip.emulator.core.exceptions.UnsupportedOpcodeException;
import com.github.chip.emulator.core.services.AsyncEventService;
import com.github.chip.emulator.core.services.EventService;

import static com.github.chip.emulator.core.opcodes.Instruction.*;

/**
 * 0x0 opcode group handler
 * 00E0 - clear screen
 * 00EE - return from a subroutine
 *
 * @author helloween
 */
public class Opcode0x0 extends AbstractOpcode {

    private Opcode0x0(int opcode) {
        super(opcode, UNDEFINED);
    }

    public static Opcode newInstance(int opcode) {
        switch (opcode & 0x00FF) {
            case 0x00E0:
                return new Opcode0x00E0(opcode);
            case 0x00EE:
                return new Opcode0x00EE(opcode);
            case 0x00FB:
                return new Opcode0x00FB(opcode);
            case 0x00FC:
                return new Opcode0x00FC(opcode);
            case 0x00FD:
                return new Opcode0x00FD(opcode);
            case 0x00FF:
                return new Opcode0x00FF(opcode);
            default:
                if (((opcode & 0x00F0) >> 4) == 0xC)
                    return new Opcode0x00CX(opcode);
                return new Opcode0x0(opcode);
        }
    }

    @Override
    public int execute(ExecutionContext executionContext) throws UnsupportedOpcodeException {
        throw new UnsupportedOpcodeException("unsupported 0x0XXX opcode");
    }

    private static final class Opcode0x00E0 extends AbstractOpcode {
        public Opcode0x00E0(int opcode) {
            super(opcode, CLS);
        }

        @Override
        public int execute(ExecutionContext executionContext) {
            EventService.getInstance().postEvent(ClearVRAMEvent.INSTANCE);
            return OPCODE_SIZE;
        }
    }

    private static final class Opcode0x00EE extends AbstractOpcode {
        public Opcode0x00EE(int opcode) {
            super(opcode, RET);
        }

        @Override
        public int execute(ExecutionContext executionContext) {
            executionContext.popCallStack();
            return OPCODE_SIZE;
        }
    }

    private static final class Opcode0x00CX extends AbstractOpcode {
        public Opcode0x00CX(int opcode) {
            super(opcode, SCD, String.format("#%X", opcode & 0x000F));
        }

        @Override
        public int execute(ExecutionContext executionContext) {
            EventService.getInstance().postEvent(new ScrollDownEvent(getOpcode() & 0x000F));
            return OPCODE_SIZE;
        }
    }

    private static final class Opcode0x00FB extends AbstractOpcode {
        public Opcode0x00FB(int opcode) {
            super(opcode, SCR);
        }

        @Override
        public int execute(ExecutionContext executionContext) {
            EventService.getInstance().postEvent(ScrollRightEvent.INSTANCE);
            return OPCODE_SIZE;
        }
    }

    private static final class Opcode0x00FC extends AbstractOpcode {
        public Opcode0x00FC(int opcode) {
            super(opcode, SCL);
        }

        @Override
        public int execute(ExecutionContext executionContext) {
            EventService.getInstance().postEvent(ScrollLeftEvent.INSTANCE);
            return OPCODE_SIZE;
        }
    }

    private static final class Opcode0x00FD extends AbstractOpcode {
        public Opcode0x00FD(int opcode) {
            super(opcode, EXIT);
        }

        @Override
        public int execute(ExecutionContext executionContext) {
            AsyncEventService.getInstance().postEvent(StopEvent.INSTANCE);
            return OPCODE_SIZE;
        }
    }

    private static final class Opcode0x00FF extends AbstractOpcode {
        public Opcode0x00FF(int opcode) {
            super(opcode, HIGH);
        }

        @Override
        public int execute(ExecutionContext executionContext) {
            AsyncEventService.getInstance().postEvent(EnableExtendedScreenModeEvent.INSTANCE);
            return OPCODE_SIZE;
        }
    }
}
