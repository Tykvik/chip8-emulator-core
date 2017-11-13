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

/**
 * register
 *
 * @author helloween
 */
public class Register {
    private final int number;
    private final int value;

    /**
     * ctor
     *
     * @param number register number
     */
    public Register(int number) {
        this.number = number;
        this.value  = 0x0;
    }

    /**
     * ctor
     *
     * @param number register number
     * @param value register value
     */
    public Register(int number, int value) {
        this.number = number;
        this.value  = value & 0xFF;
    }

    /**
     * @return register number
     */
    public int getNumber() {
        return number;
    }

    /**
     * @return register value
     */
    public int getValue() {
        return value;
    }

    /**
     * add value to register
     *
     * @param value added value
     */
    public Register add(int value) {
        int result = this.value + value;
        return new Register(this.number, result);
    }

    /**
     * add register value to register
     *
     * @param register register
     */
    public Register add(Register register) {
        return add(register.getValue());
    }

    /**
     * subtracts register from register
     *
     * @param register register
     */
    public Register sub(Register register) {
        return new Register(this.number, this.value - register.getValue());
    }

    /**
     * this register | register
     *
     * @param register register
     */
    public Register or(Register register) {
        return new Register(this.number, this.value | register.getValue());
    }

    /**
     * this register & register
     *
     * @param register register
     */
    public Register and(Register register) {
        return new Register(this.number, this.value & register.getValue());
    }

    /**
     * this register ^ register
     *
     * @param register register
     */
    public Register xor(Register register) {
        return new Register(this.number, this.value ^ register.getValue());
    }

    /**
     * shifts register value right by count
     */
    public Register rightShift() {
        return new Register(this.number, this.value >> 1);
    }

    /**
     * shifts register value left by count
     */
    public Register leftShift() {
        return new Register(this.number, this.value << 1);
    }
}
