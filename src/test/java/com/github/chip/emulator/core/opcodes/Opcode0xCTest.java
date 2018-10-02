package com.github.chip.emulator.core.opcodes;

import com.github.chip.emulator.core.ExecutionContext;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author helloween
 */
public class Opcode0xCTest {
    @Test
    public void execute() throws Exception {
        int opcode = 0x100;
        Opcode opcodeHandler = Opcode0xC.newInstance(opcode);
        ExecutionContext context = new ExecutionContext();
        assertEquals(0x2, opcodeHandler.execute(context));
        assertEquals(0x0, context.getRegister(0x1).getValue());
    }
}