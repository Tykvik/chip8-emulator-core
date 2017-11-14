package com.github.chip.emulator.core;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author helloween
 */
public class ExecutionContextTest {
    @Test
    public void writeToMemory() throws Exception {
        ExecutionContext context = new ExecutionContext();
        context.writeToMemory((byte) 0x1);
        assertEquals(0x1, context.getMemoryValue(0x200));
        context.setSoundTimer(0x200);
        assertTrue(context.getSoundTimer() > 0);
    }

}