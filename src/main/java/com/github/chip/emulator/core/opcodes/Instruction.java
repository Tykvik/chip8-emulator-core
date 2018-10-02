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

/**
 * @author helloween
 */
@SuppressWarnings("SpellCheckingInspection")
public enum Instruction {
    UNDEFINED,   // undefined opcode
    CLS,         // clear screen
    RET,         // return from a subroutine
    JP,          // jump to address
    CALL,        // call subroutine
    SE,          // skip the next instruction if equals
    SNE,         // skip the next instruction if doesn't equal
    LD,          // set VX
    ADD,         // add to VX
    OR,          // set VX to VX or XX
    AND,         // set VX to VX and XX
    XOR,         // set VX to VX xor XX
    SUB,         // XX is subtracted from VX
    SHR,         // right shift
    SHL,         // left shift
    SUBN,        // set VX to VY minus VX
    RND,         // random
    DRW,         // draw
    SKP,         // skip if key pressed
    SKNP,        // skip if key not pressed
    BCD,         // BCD
    HIGH,        // enable extended screen mode
    SCD,         // scroll down
    SCR,         // scroll right
    SCL,         // scroll left
    EXIT         // exit
}
