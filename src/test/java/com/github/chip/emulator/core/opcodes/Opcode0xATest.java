package com.github.chip.emulator.core.opcodes;

import com.github.chip.emulator.core.ExecutionContext;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author helloween
 */
public class Opcode0xATest {
    @Test
    public void execute() throws Exception {
        int opcode = 0x0123;
        Opcode opcodeHandler = Opcode0xA.newInstance(opcode);
        ExecutionContext context = new ExecutionContext();
        opcodeHandler.execute(context);
        assertEquals(0x123, context.getIndexRegister().getValue());
    }
}