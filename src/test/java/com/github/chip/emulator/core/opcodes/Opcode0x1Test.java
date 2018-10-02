package com.github.chip.emulator.core.opcodes;

import com.github.chip.emulator.core.ExecutionContext;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author helloween
 */
public class Opcode0x1Test {
    @Test
    public void execute() throws Exception {
        int opcode = 0x0A10;
        Opcode opcodeHandler = Opcode0x1.newInstance(opcode);
        ExecutionContext executionContext = new ExecutionContext();
        opcodeHandler.execute(executionContext);
        assertEquals(0xA10, executionContext.getOffset());
    }
}