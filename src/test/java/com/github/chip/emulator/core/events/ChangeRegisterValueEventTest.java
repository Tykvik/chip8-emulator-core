package com.github.chip.emulator.core.events;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author helloween
 */
public class ChangeRegisterValueEventTest {
    @Test
    public void ctor() throws Exception {
        ChangeRegisterValueEvent event =  new ChangeRegisterValueEvent(0x1, 0x2);
        assertEquals(0x1, event.getRegisterNumber());
        assertEquals(0x2, event.getValue());
    }
}