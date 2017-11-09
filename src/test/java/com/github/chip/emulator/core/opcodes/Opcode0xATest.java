package com.github.chip.emulator.core.opcodes;

import com.github.chip.emulator.core.ExecutionContext;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author helloween
 */
public class Opcode0xATest {
    private Opcode opcodeHandler;

    @Before
    public void setUp() throws Exception {
        opcodeHandler = new Opcode0xA();
    }

    @Test
    public void execute() throws Exception {
        int opcode = 0x0123;
        ExecutionContext context = new ExecutionContext();
        opcodeHandler.execute(opcode, context);
        assertEquals(0x123, context.getiRegister().getValue());
    }

}