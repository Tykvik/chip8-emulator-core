package com.github.chip.emulator.core.opcodes;

import com.github.chip.emulator.core.ExecutionContext;
import com.github.chip.emulator.core.Register;
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
        context.setRegister(new Register(0x3, 0x2));
        opcodeHandler.execute(opcode, context);
        assertEquals(0x2, context.getRegister(0x2).getValue());
        context.setRegister(new Register(0x2, 0x4));
        opcode = 0x0231;
        opcodeHandler.execute(opcode, context);
        assertEquals(0x6, context.getRegister(0x2).getValue());
        opcode = 0x0232;
        opcodeHandler.execute(opcode, context);
        assertEquals(0x2, context.getRegister(0x2).getValue());
        opcode = 0x0233;
        opcodeHandler.execute(opcode, context);
        assertEquals(0x0, context.getRegister(0x2).getValue());
        context.setRegister(new Register(0x2, 0x1));
        context.setRegister(new Register(0x3, 0x2));
        opcode = 0x0234;
        opcodeHandler.execute(opcode, context);
        assertEquals(0x3, context.getRegister(0x2).getValue());
        assertEquals(0x2, context.getRegister(0x3).getValue());
        assertEquals(0x0, context.getRegister(0xF).getValue());
        context.setRegister(new Register(0x2, 0xFF));
        opcodeHandler.execute(opcode, context);
        assertEquals(0x1, context.getRegister(0x2).getValue());
        assertEquals(0x1, context.getRegister(0xF).getValue());
        opcode = 0x0235;
        opcodeHandler.execute(opcode, context);
        assertEquals(0xFF, context.getRegister(0x2).getValue());
        assertEquals(0x0, context.getRegister(0xF).getValue());
        opcodeHandler.execute(opcode, context);
        assertEquals(0xFF - 0x2, context.getRegister(0x2).getValue());
        assertEquals(0x1, context.getRegister(0xF).getValue());
        opcode = 0x0236;
        opcodeHandler.execute(opcode, context);
        assertEquals(0x1, context.getRegister(0x2).getValue());
        assertEquals(0x1, context.getRegister(0x3).getValue());
        assertEquals(0x0, context.getRegister(0xF).getValue());
        opcode = 0x0237;
        opcodeHandler.execute(opcode, context);
        assertEquals(0x0, context.getRegister(0x2).getValue());
        assertEquals(0x1, context.getRegister(0xF).getValue());
        context.setRegister(new Register(0x3, 0x0));
        context.setRegister(new Register(0x2, 0x1));
        opcodeHandler.execute(opcode, context);
        assertEquals(0xFF, context.getRegister(0x2).getValue());
        assertEquals(0x0, context.getRegister(0xF).getValue());
        opcode = 0x023E;
        context.setRegister(new Register(0x3, 0xFF));
        opcodeHandler.execute(opcode, context);
        assertEquals(0xFE, context.getRegister(0x3).getValue());
        assertEquals(0xFE, context.getRegister(0x2).getValue());
        assertEquals(0x1, context.getRegister(0xF).getValue());
    }

    @Test(expected = UnsupportedOpcodeException.class)
    public void execute2() throws Exception {
        int opcode = 0x023F;
        ExecutionContext context = new ExecutionContext();
        opcodeHandler.execute(opcode, context);
    }
}