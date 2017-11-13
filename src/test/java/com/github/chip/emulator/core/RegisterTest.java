package com.github.chip.emulator.core;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author helloween
 */
public class RegisterTest {

    private Register register;
    private Register secondRegister;

    @Test
    public void getValue() throws Exception {
        Register register = new Register(0x0, 0x0);
        assertEquals(0x0, register.getValue());
        register = new Register(0x0, 0xFF);
        assertEquals(0xFF, register.getValue());
        register = new Register(0x0, 0xFFF);
        assertEquals(0xFF, register.getValue());
    }

    @Test
    public void add() throws Exception {
        register = new Register(0x0, 0x0);
        secondRegister = new Register(0x1, 0xFF);
        register = register.add(secondRegister);
        assertEquals(0xFF, register.getValue());
        register        = new Register(0x0, 0x80);
        secondRegister  = new Register(0x1, 0x80);
        register = register.add(secondRegister);
        assertEquals(0x0, register.getValue());
    }

    @Test
    public void sub() throws Exception {
        register = new Register(0x0, 0xA);
        secondRegister = new Register(0x1, 0xF);
        register = register.sub(secondRegister);
        assertEquals(0xFB, register.getValue());
        register = new Register(0x0, 0xA);
        secondRegister = new Register(0x1, 0x9);
        register = register.sub(secondRegister);
        assertEquals(0x1, register.getValue());
    }

    @Test
    public void or() throws Exception {
        register = new Register(0x0, 0xA);
        secondRegister = new Register(0x1, 0x9);
        register = register.or(secondRegister);
        assertEquals(0xB, register.getValue());
    }

    @Test
    public void and() throws Exception {
        register = new Register(0x0, 0xA);
        secondRegister = new Register(0x1, 0x9);
        register = register.and(secondRegister);
        assertEquals(0x8, register.getValue());
    }

    @Test
    public void xor() throws Exception {
        register = new Register(0x0, 0xA);
        secondRegister = new Register(0x1, 0x9);
        register = register.xor(secondRegister);
        assertEquals(0x3, register.getValue());
    }

    @Test
    public void rightShift() throws Exception {
        register = new Register(0x0, 0x1);
        assertEquals(0x0, register.rightShift().getValue());
        register = new Register(0x0, 0x2);
        assertEquals(0x1, register.rightShift().getValue());
    }

    @Test
    public void leftShift() throws Exception {
        register = new Register(0x0, 0x1);
        assertEquals(0x2, register.leftShift().getValue());
        register = new Register(0x0, 0xFF);
        assertEquals(0xFE, register.leftShift().getValue());
    }
}