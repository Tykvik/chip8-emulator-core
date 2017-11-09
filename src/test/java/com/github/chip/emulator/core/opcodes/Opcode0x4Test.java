package com.github.chip.emulator.core.opcodes;

import com.github.chip.emulator.core.ExecutionContext;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author helloween
 */
public class Opcode0x4Test {
    Opcode opcodeHandler;

    @Before
    public void setUp() throws Exception {
        opcodeHandler = new Opcode0x4();
    }

    @Test
    public void execute() throws Exception {
        int opcode = 0x02FF;
        ExecutionContext context = new ExecutionContext();
        int offset = context.getOffset();
        context.getRegisters()[2].setValue(0xFF);
        opcodeHandler.execute(opcode, context);
        assertEquals(offset, context.getOffset());
        context.getRegisters()[2].setValue(0xF0);
        opcodeHandler.execute(opcode, context);
        assertEquals(offset + 2, context.getOffset());
    }
}