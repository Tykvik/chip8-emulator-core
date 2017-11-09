package com.github.chip.emulator.core.opcodes;

import com.github.chip.emulator.core.ExecutionContext;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author helloween
 */
public class Opcode0x1Test {
    private Opcode opcodeHandler;

    @Before
    public void init() {
        opcodeHandler = new Opcode0x1();
    }

    @Test
    public void execute() throws Exception {
        int opcode = 0x0A10;
        ExecutionContext executionContext = new ExecutionContext();
        opcodeHandler.execute(opcode, executionContext);
        assertEquals(0xA10, executionContext.getOffset());
    }
}