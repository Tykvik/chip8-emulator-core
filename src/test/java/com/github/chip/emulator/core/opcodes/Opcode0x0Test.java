package com.github.chip.emulator.core.opcodes;

import com.github.chip.emulator.core.ExecutionContext;
import com.github.chip.emulator.core.exceptions.UnsupportedOpcodeException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author helloween
 */
public class Opcode0x0Test {
    private Opcode opcodeHandler;

    @Before
    public void init() {
        opcodeHandler = new Opcode0x0();
    }

    @Test
    public void execute() throws Exception {
        int opcode = 0xEE;
        ExecutionContext context = new ExecutionContext();
        context.getStack().push(0x1);
        opcodeHandler.execute(opcode, context);
        assertEquals(0x3, context.getOffset());
        assertTrue(context.getStack().isEmpty());
    }

    @Test(expected = UnsupportedOpcodeException.class)
    public void execute2() throws Exception {
        int opcode = 0xEF;
        opcodeHandler.execute(opcode, new ExecutionContext());
    }
}