package com.github.chip.emulator.core.opcodes;

import com.github.chip.emulator.core.ExecutionContext;
import com.github.chip.emulator.core.Register;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author helloween
 */
public class Opcode0x4Test {
    Opcode opcodeHandler;

    @Before
    public void setUp() throws Exception {
        opcodeHandler = new Opcode0x4();
    }

    @Test
    public void execute() throws Exception {
        int opcode = 0x02FF;
        ExecutionContext context = new ExecutionContext();
        int offset = context.getOffset();
        context.setRegister(new Register(0x2, 0xFF));
        opcodeHandler.execute(opcode, context);
        assertEquals(offset, context.getOffset());
        context.setRegister(new Register(0x2, 0xF0));
        assertEquals(Opcode.OPCODE_SIZE * 2, opcodeHandler.execute(opcode, context));
    }
}