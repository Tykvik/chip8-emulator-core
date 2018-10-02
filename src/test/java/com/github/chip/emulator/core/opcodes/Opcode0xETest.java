package com.github.chip.emulator.core.opcodes;

import com.github.chip.emulator.core.ExecutionContext;
import com.github.chip.emulator.core.Register;
import com.github.chip.emulator.core.exceptions.UnsupportedOpcodeException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

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
        Opcode opcodeHandler = Opcode0xE.newInstance(opcode);
        assertEquals(0x4, opcodeHandler.execute(context));
        assertFalse(context.getKey(0x2));
        assertEquals(0x2, opcodeHandler.execute(context));
        opcode = 0x01A1;
        opcodeHandler = Opcode0xE.newInstance(opcode);
        assertEquals(0x4, opcodeHandler.execute(context));
        context.setKey(0x2, true);
        assertEquals(0x2, opcodeHandler.execute(context));
        assertFalse(context.getKey(0x2));
    }

    @Test(expected = UnsupportedOpcodeException.class)
    public void execute2() throws Exception {
        int opcode = 0x011A;
        Opcode opcodeHandler = Opcode0xE.newInstance(opcode);
        opcodeHandler.execute(new ExecutionContext());
    }
}