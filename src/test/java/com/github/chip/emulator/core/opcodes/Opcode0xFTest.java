package com.github.chip.emulator.core.opcodes;

import com.github.chip.emulator.core.ExecutionContext;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author helloween
 */
public class Opcode0xFTest {
    private Opcode opcodeHandler;

    @Before
    public void setUp() throws Exception {
        opcodeHandler = new Opcode0xF();
    }

    @Test
    public void execute() throws Exception {
        int opcode = 0x0207;
        ExecutionContext context = new ExecutionContext();
        context.setDelayTimer(0xFF);
        opcodeHandler.execute(opcode, context);
        assertTrue(context.getRegisters()[2].getValue() > 0);
        context.setDelayTimer(0x0);
        opcode = 0x0215;
        opcodeHandler.execute(opcode, context);
        assertTrue(context.getDelayTimer() > 0);
        opcode = 0x021E;
        context.getRegisters()[2].setValue(0x3);
        opcodeHandler.execute(opcode, context);
        assertEquals(0x3, context.getiRegister().getValue());
        opcode = 0x0229;
        opcodeHandler.execute(opcode, context);
        assertEquals(0x3 * 5, context.getiRegister().getValue());
    }

}