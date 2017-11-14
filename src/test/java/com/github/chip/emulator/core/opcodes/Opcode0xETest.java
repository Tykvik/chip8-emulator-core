package com.github.chip.emulator.core.opcodes;

import com.github.chip.emulator.core.ExecutionContext;
import com.github.chip.emulator.core.Register;
import com.github.chip.emulator.core.exceptions.UnsupportedOpcodeException;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author helloween
 */
public class Opcode0xETest {
    @Test
    public void execute() throws Exception {
        int opcode = 0x019E;
        ExecutionContext context = new ExecutionContext();
        Register register = new Register(0x1, 0x2);
        context.setRegister(register);
        context.setKey(0x2, true);
        Opcode opcodeHandler = new Opcode0xE();
        assertEquals(0x4, opcodeHandler.execute(opcode, context));
        assertFalse(context.getKey(0x2));
        assertEquals(0x2, opcodeHandler.execute(opcode, context));
        opcode = 0x01A1;
        assertEquals(0x4, opcodeHandler.execute(opcode, context));
        context.setKey(0x2, true);
        assertEquals(0x2, opcodeHandler.execute(opcode, context));
        assertFalse(context.getKey(0x2));
    }

    @Test(expected = UnsupportedOpcodeException.class)
    public void execute2() throws Exception {
        int opcode = 0x011A;
        Opcode opcodeHandler = new Opcode0xE();
        opcodeHandler.execute(opcode, new ExecutionContext());
    }
}