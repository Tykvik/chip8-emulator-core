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
    private int     number;
    private int     value;

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
     * @param value register value
     */
    public void setValue(int value) {
        this.value = value & 0xFF;
    }

    /**
     * add value to register
     *
     * @param value added value
     * @return carry flag
     */
    public boolean add(int value) {
        int result = this.value + value;
        this.value = result & 0xFF;
        return result > 0xFF;
    }

    /**
     * add register value to register
     *
     * @param register register
     * @return carry flag
     */
    public boolean add(Register register) {
        return add(register.getValue());
    }

    /**
     * subtracts register from register
     *
     * @param register register
     * @return borrow flag
     */
    public boolean sub(Register register) {
        boolean borrow = this.value < register.getValue();
        this.value -= register.getValue();
        this.value &= 0xFF;
        return borrow;
    }

    /**
     * this register | register
     *
     * @param register register
     */
    public void or(Register register) {
        this.value |= register.getValue();
        this.value &= 0xFF;
    }

    /**
     * this register & register
     *
     * @param register register
     */
    public void and(Register register) {
        this.value &= register.getValue();
        this.value &= 0xFF;
    }

    /**
     * this register ^ register
     *
     * @param register register
     */
    public void xor(Register register) {
        this.value ^= register.getValue();
        this.value &= 0xFF;
    }

    /**
     * shifts register value right by count
     *
     * @param count shift count
     * @return least bit
     */
    public int rightShift(int count) {
        int leastBit = this.value & 0x1;
        this.value >>= count;
        return leastBit;
    }

    /**
     * shifts register value left by count
     *
     * @param count shift count
     * @return most bit
     */
    public int leftShift(int count) {
        int mostBit = (this.value & 0x80) >> 7;
        this.value <<= count;
        this.value &= 0xFF;
        return mostBit;
    }
}
