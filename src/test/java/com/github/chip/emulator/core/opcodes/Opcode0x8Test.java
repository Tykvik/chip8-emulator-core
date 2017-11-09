package com.github.chip.emulator.core.opcodes;

import com.github.chip.emulator.core.ExecutionContext;
import com.github.chip.emulator.core.exceptions.UnsupportedOpcodeException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author helloween
 */
public class Opcode0x8Test {
    private Opcode opcodeHandler;

    @Before
    public void setUp() throws Exception {
        opcodeHandler = new Opcode0x8();
    }

    @Test
    public void execute() throws Exception {
        int opcode = 0x0230;
        ExecutionContext context = new ExecutionContext();
        context.getRegisters()[3].setValue(0x2);
        opcodeHandler.execute(opcode, context);
        assertEquals(0x2, context.getRegisters()[2].getValue());
        context.getRegisters()[2].setValue(0x4);
        opcode = 0x0231;
        opcodeHandler.execute(opcode, context);
        assertEquals(0x6, context.getRegisters()[2].getValue());
        opcode = 0x0232;
        opcodeHandler.execute(opcode, context);
        assertEquals(0x2, context.getRegisters()[2].getValue());
        opcode = 0x0233;
        opcodeHandler.execute(opcode, context);
        assertEquals(0x0, context.getRegisters()[2].getValue());
        context.getRegisters()[2].setValue(0x1);
        context.getRegisters()[3].setValue(0x2);
        opcode = 0x0234;
        opcodeHandler.execute(opcode, context);
        assertEquals(0x3, context.getRegisters()[2].getValue());
        assertEquals(0x2, context.getRegisters()[3].getValue());
        assertEquals(0x0, context.getRegisters()[15].getValue());
        context.getRegisters()[2].setValue(0xFF);
        opcodeHandler.execute(opcode, context);
        assertEquals(0x1, context.getRegisters()[2].getValue());
        assertEquals(0x1, context.getRegisters()[15].getValue());
        opcode = 0x0235;
        opcodeHandler.execute(opcode, context);
        assertEquals(0xFF, context.getRegisters()[2].getValue());
        assertEquals(0x0, context.getRegisters()[15].getValue());
        opcodeHandler.execute(opcode, context);
        assertEquals(0xFF - 0x2, context.getRegisters()[2].getValue());
        assertEquals(0x1, context.getRegisters()[15].getValue());
        opcode = 0x0236;
        opcodeHandler.execute(opcode, context);
        assertEquals(0x1, context.getRegisters()[2].getValue());
        assertEquals(0x1, context.getRegisters()[3].getValue());
        assertEquals(0x0, context.getRegisters()[15].getValue());
        opcode = 0x0237;
        opcodeHandler.execute(opcode, context);
        assertEquals(0x0, context.getRegisters()[2].getValue());
        assertEquals(0x1, context.getRegisters()[15].getValue());
        context.getRegisters()[3].setValue(0x0);
        context.getRegisters()[2].setValue(0x1);
        opcodeHandler.execute(opcode, context);
        assertEquals(0xFF, context.getRegisters()[2].getValue());
        assertEquals(0x0, context.getRegisters()[15].getValue());
        opcode = 0x023E;
        context.getRegisters()[3].setValue(0xFF);
        opcodeHandler.execute(opcode, context);
        assertEquals(0xFE, context.getRegisters()[3].getValue());
        assertEquals(0xFE, context.getRegisters()[2].getValue());
        assertEquals(0x1, context.getRegisters()[15].getValue());
    }

    @Test(expected = UnsupportedOpcodeException.class)
    public void execute2() throws Exception {
        int opcode = 0x023F;
        ExecutionContext context = new ExecutionContext();
        opcodeHandler.execute(opcode, context);
    }
}