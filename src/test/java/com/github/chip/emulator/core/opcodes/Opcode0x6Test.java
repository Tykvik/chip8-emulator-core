package com.github.chip.emulator.core.opcodes;

import com.github.chip.emulator.core.ExecutionContext;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author helloween
 */
public class Opcode0x6Test {
    @Test
    public void execute() throws Exception {
        int opcode = 0x02EE;
        Opcode opcodeHandler = Opcode0x6.newInstance(opcode);
        ExecutionContext context = new ExecutionContext();
        opcodeHandler.execute(context);
        assertEquals(0xEE, context.getRegister(0x2).getValue());
    }
}