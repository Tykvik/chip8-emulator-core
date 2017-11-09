package com.github.chip.emulator.core;

/**
 * register
 *
 * @author helloween
 */
public class Register {
    private int value;

    /**
     * ctor
     */
    public Register() {
        this.value = 0x0;
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
