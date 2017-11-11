package com.github.chip.emulator.core;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author helloween
 */
public class RegisterTest {

    private Register register;
    private Register secondRegister;

    @Before
    public void init() {
        register            = new Register(0x0);
        secondRegister      = new Register(0x1);
    }

    @Test
    public void getValue() throws Exception {
        Register register = new Register(0x0);
        assertEquals(0x0, register.getValue());
        register.setValue(0xFF);
        assertEquals(0xFF, register.getValue());
        register.setValue(0xFFF);
        assertEquals(0xFF, register.getValue());
    }

    @Test
    public void add() throws Exception {
        secondRegister.setValue(0xFF);
        assertFalse(register.add(secondRegister));
        assertEquals(0xFF, register.getValue());
        register.setValue(0x80);
        secondRegister.setValue(0x80);
        assertTrue(register.add(secondRegister));
        assertEquals(0x0, register.getValue());
    }

    @Test
    public void sub() throws Exception {
        register.setValue(0xA);
        secondRegister.setValue(0xF);
        assertTrue(register.sub(secondRegister));
        assertEquals(0xFB, register.getValue());
        register.setValue(0xA);
        secondRegister.setValue(0x9);
        assertFalse(register.sub(secondRegister));
        assertEquals(0x1, register.getValue());
    }

    @Test
    public void or() throws Exception {
        register.setValue(0xA);
        secondRegister.setValue(0x9);
        register.or(secondRegister);
        assertEquals(0xB, register.getValue());
    }

    @Test
    public void and() throws Exception {
        register.setValue(0xA);
        secondRegister.setValue(0x9);
        register.and(secondRegister);
        assertEquals(0x8, register.getValue());
    }

    @Test
    public void xor() throws Exception {
        register.setValue(0xA);
        secondRegister.setValue(0x9);
        register.xor(secondRegister);
        assertEquals(0x3, register.getValue());
    }

    @Test
    public void rightShift() throws Exception {
        register.setValue(0x1);
        assertEquals(0x1, register.rightShift(1));
        assertEquals(0x0, register.getValue());
        register.setValue(0x2);
        assertEquals(0x0, register.rightShift(1));
        assertEquals(0x1, register.getValue());
    }

    @Test
    public void leftShift() throws Exception {
        register.setValue(0x1);
        assertEquals(0x0, register.leftShift(1));
        assertEquals(0x2, register.getValue());
        register.setValue(0xFF);
        assertEquals(0x1, register.leftShift(1));
        assertEquals(0xFE, register.getValue());
    }
}