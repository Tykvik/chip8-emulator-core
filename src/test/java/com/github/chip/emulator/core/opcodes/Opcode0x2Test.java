package com.github.chip.emulator.core.opcodes;

import com.github.chip.emulator.core.ExecutionContext;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author helloween
 */
public class Opcode0x2Test {
    private Opcode opcodeHandler;

    @Before
    public void init() {
        opcodeHandler = new Opcode0x2();
    }

    @Test
    public void execute() throws Exception {
        int opcode = 0x0A10;
        ExecutionContext context = new ExecutionContext();
        context.setOffset(0x1);
        opcodeHandler.execute(opcode, context);
        assertEquals(0x0A10, context.getOffset());
        assertEquals(new Integer(0x1), context.getStack().pop());
    }
}