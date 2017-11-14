package com.github.chip.emulator.core.events;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author helloween
 */
public class PressKeyEventTest {
    @Test
    public void getKeyNumber() throws Exception {
        PressKeyEvent event = new PressKeyEvent(0x1);
        assertEquals(0x1, event.getKeyNumber());
    }
}