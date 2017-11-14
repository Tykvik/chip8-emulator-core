package com.github.chip.emulator.core.opcodes;

import com.github.chip.emulator.core.ExecutionContext;
import com.github.chip.emulator.core.IRegister;
import com.github.chip.emulator.core.Register;
import com.github.chip.emulator.core.exceptions.UnsupportedOpcodeException;
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
        assertTrue(context.getRegister(0x2).getValue() > 0);
        context.setDelayTimer(0x0);
        opcode = 0x0215;
        opcodeHandler.execute(opcode, context);
        assertTrue(context.getDelayTimer() > 0);
        opcode = 0x021E;
        context.setRegister(new Register(0x2, 0x3));
        opcodeHandler.execute(opcode, context);
        assertEquals(0x3, context.getIndexRegister().getValue());
        opcode = 0x0229;
        opcodeHandler.execute(opcode, context);
        assertEquals(0x3 * 5, context.getIndexRegister().getValue());
        opcode = 0x0233;
        context.setRegister(new Register(0x2, 0x55));
        context.setIndexRegister(new IRegister(0x200));
        assertEquals(0x2, opcodeHandler.execute(opcode, context));
        assertEquals(0x0, context.getMemoryValue(0x200));
        assertEquals(0x8, context.getMemoryValue(0x200 + 1));
        assertEquals(0x5, context.getMemoryValue(0x200 + 2));
        context.setRegister(new Register(0x2, 0xFF));
        opcodeHandler.execute(opcode, context);
        assertEquals(0x2, context.getMemoryValue(0x200));
        assertEquals(0x5, context.getMemoryValue(0x200 + 1));
        assertEquals(0x5, context.getMemoryValue(0x200 + 2));
        opcode = 0x0355;
        context.setRegister(new Register(0x0, 0x1));
        context.setRegister(new Register(0x1, 0x2));
        context.setRegister(new Register(0x2, 0x3));
        context.setIndexRegister(new IRegister(0x200));
        assertEquals(0x2, opcodeHandler.execute(opcode, context));
        assertEquals(0x1, context.getMemoryValue(0x200));
        assertEquals(0x2, context.getMemoryValue(0x200 + 1));
        assertEquals(0x3, context.getMemoryValue(0x200 + 2));
        assertEquals(0x0, context.getMemoryValue(0x200 + 3));
        assertEquals(0x4, context.getIndexRegister().getValue());
        opcode = 0x0F65;
        context.setRegister(new Register(0x0, 0x0));
        context.setRegister(new Register(0x1, 0x0));
        context.setRegister(new Register(0x2, 0x0));
        context.setIndexRegister(new IRegister(0x200));
        assertEquals(0x2, opcodeHandler.execute(opcode, context));
        assertEquals(0x1, context.getRegister(0x0).getValue());
        assertEquals(0x2, context.getRegister(0x1).getValue());
        assertEquals(0x3, context.getRegister(0x2).getValue());
        assertEquals(0x0, context.getRegister(0x3).getValue());
    }

    @Test(expected = UnsupportedOpcodeException.class)
    public void exec2() throws Exception {
        ExecutionContext context = new ExecutionContext();
        opcodeHandler.execute(0x0FF, context);
    }

}