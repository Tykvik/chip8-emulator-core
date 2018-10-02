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
import com.github.chip.emulator.core.events.DrawEvent;
import com.github.chip.emulator.core.services.EventService;

import static com.github.chip.emulator.core.opcodes.Instruction.DRW;

/**
 * 0xD opcode group handler
 * 0xDXYN - draws a sprite at coordinate (VX, VY) that has a width of 8 pixels and a height of N pixels.
 *          Each row of 8 pixels is read as bit-coded starting from memory location I.
 *          I value doesn’t change after the execution of this instruction.
 *
 * @author helloween
 */
public class Opcode0xD extends TwoRegistersBasedOpcode {
    private Opcode0xD(int opcode) {
        super(opcode, DRW, formatRegisterNumber(getRegisterNumber(opcode)),
                           formatRegisterNumber(getSecondRegisterNumber(opcode)),
                           String.format("#%X", opcode & 0x000F));
    }

    public static Opcode newInstance(int opcode) {
        return new Opcode0xD(opcode);
    }

    @Override
    public int execute(Register firstRegister, Register secondRegister, ExecutionContext executionContext) {
        EventService.getInstance().postEvent(new DrawEvent(firstRegister.getValue(), secondRegister.getValue(), getOpcode() & 0x000F));
        return OPCODE_SIZE;
    }
}
