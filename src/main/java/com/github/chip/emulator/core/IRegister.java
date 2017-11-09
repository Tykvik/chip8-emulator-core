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
 * Address register
 *
 * @author helloween
 */
public class IRegister {
    private int value;

    /**
     * ctor
     */
    public IRegister() {
        this.value = 0x0;
    }

    /**
     * @return register value
     */
    public int getValue() {
        return value;
    }

    /**
     * @param value new register value
     */
    public void setValue(int value) {
        this.value = value;
        this.value &= 0xFFF;
    }

    /**
     * add value to register
     *
     * @param value added value
     * @return carry flag
     */
    public boolean add(int value) {
        int result = this.value + value;
        this.value = result & 0xFFF;
        return result > 0xFFF;
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
}
