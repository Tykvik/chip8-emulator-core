package com.github.chip.emulator.core.events;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author helloween
 */
public class ChangeIndexRegisterValueEventTest {
    @Test
    public void getValue() throws Exception {
        ChangeIndexRegisterValueEvent event = new ChangeIndexRegisterValueEvent(0x2);
        assertEquals(0x2, event.getValue());
    }
}