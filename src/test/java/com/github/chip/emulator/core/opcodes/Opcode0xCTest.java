package com.github.chip.emulator.core.opcodes;

import com.github.chip.emulator.core.ExecutionContext;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author helloween
 */
public class Opcode0xCTest {
    private Opcode opcodeHandler;

    @Before
    public void setUp() throws Exception {
        opcodeHandler = new Opcode0xC();
    }

    @Test
    public void execute() throws Exception {
        int opcode = 0x100;
        ExecutionContext context = new ExecutionContext();
        assertEquals(0x2, opcodeHandler.execute(opcode, context));
        assertEquals(0x0, context.getRegister(0x1).getValue());
    }

}