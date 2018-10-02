package com.github.chip.emulator.core.opcodes;

import com.github.chip.emulator.core.ExecutionContext;
import com.github.chip.emulator.core.IRegister;
import com.github.chip.emulator.core.Register;
import com.github.chip.emulator.core.exceptions.UnsupportedOpcodeException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author helloween
 */
public class Opcode0xFTest {
    private Opcode opcodeHandler;

    @Test
    public void execute() throws Exception {
        int opcode = 0x0207;
        opcodeHandler = Opcode0xF.newInstance(opcode);
        ExecutionContext context = new ExecutionContext();
        context.setDelayTimer(0xFF);
        opcodeHandler.execute(context);
        assertTrue(context.getRegister(0x2).getValue() > 0);
        context.setDelayTimer(0x0);
        opcode = 0x0215;
        opcodeHandler = Opcode0xF.newInstance(opcode);
        opcodeHandler.execute(context);
        assertTrue(context.getDelayTimer() > 0);
        opcode = 0x021E;
        opcodeHandler = Opcode0xF.newInstance(opcode);
        context.setRegister(new Register(0x2, 0x3));
        opcodeHandler.execute(context);
        assertEquals(0x3, context.getIndexRegister().getValue());
        opcode = 0x0229;
        opcodeHandler = Opcode0xF.newInstance(opcode);
        opcodeHandler.execute(context);
        assertEquals(0x3 * 5, context.getIndexRegister().getValue());
        opcode = 0x0233;
        opcodeHandler = Opcode0xF.newInstance(opcode);
        context.setRegister(new Register(0x2, 0x55));
        context.setIndexRegister(new IRegister(0x200));
        assertEquals(0x2, opcodeHandler.execute(context));
        assertEquals(0x0, context.getMemoryValue(0x200));
        assertEquals(0x8, context.getMemoryValue(0x200 + 1));
        assertEquals(0x5, context.getMemoryValue(0x200 + 2));
        context.setRegister(new Register(0x2, 0xFF));
        opcodeHandler.execute(context);
        assertEquals(0x2, context.getMemoryValue(0x200));
        assertEquals(0x5, context.getMemoryValue(0x200 + 1));
        assertEquals(0x5, context.getMemoryValue(0x200 + 2));
        opcode = 0x0355;
        opcodeHandler = Opcode0xF.newInstance(opcode);
        context.setRegister(new Register(0x0, 0x1));
        context.setRegister(new Register(0x1, 0x2));
        context.setRegister(new Register(0x2, 0x3));
        context.setIndexRegister(new IRegister(0x200));
        assertEquals(0x2, opcodeHandler.execute(context));
        assertEquals(0x1, context.getMemoryValue(0x200));
        assertEquals(0x2, context.getMemoryValue(0x200 + 1));
        assertEquals(0x3, context.getMemoryValue(0x200 + 2));
        assertEquals(0x0, context.getMemoryValue(0x200 + 3));
        assertEquals(0x4, context.getIndexRegister().getValue());
        opcode = 0x0F65;
        opcodeHandler = Opcode0xF.newInstance(opcode);
        context.setRegister(new Register(0x0, 0x0));
        context.setRegister(new Register(0x1, 0x0));
        context.setRegister(new Register(0x2, 0x0));
        context.setIndexRegister(new IRegister(0x200));
        assertEquals(0x2, opcodeHandler.execute(context));
        assertEquals(0x1, context.getRegister(0x0).getValue());
        assertEquals(0x2, context.getRegister(0x1).getValue());
        assertEquals(0x3, context.getRegister(0x2).getValue());
        assertEquals(0x0, context.getRegister(0x3).getValue());
    }

    @Test(expected = UnsupportedOpcodeException.class)
    public void exec2() throws Exception {
        opcodeHandler = Opcode0xF.newInstance(0x0FF);
        ExecutionContext context = new ExecutionContext();
        opcodeHandler.execute(context);
    }
}