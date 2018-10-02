package com.github.chip.emulator.core.opcodes;

import com.github.chip.emulator.core.ExecutionContext;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author helloween
 */
public class Opcode0x2Test {
    @Test
    public void execute() throws Exception {
        int opcode = 0x0A10;
        Opcode opcodeHandler = Opcode0x2.newInstance(opcode);
        ExecutionContext context = new ExecutionContext();
        context.setOffset(0x1);
        opcodeHandler.execute(context);
        assertEquals(0x0A10, context.getOffset());
    }
}