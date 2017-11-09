package com.github.chip.emulator.core;

/**
 * @author helloween
 */
public class IRegister {
    private int value;

    public IRegister() {
        this.value = 0x0;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        this.value &= 0xFFFF;
    }

    public boolean add(int value) {
        int result = this.value + value;
        this.value = result & 0xFFFF;
        return result > 0xFFF;
    }

    public boolean add(Register register) {
        return add(register.getValue());
    }
}
