package com.github.chip.emulator.core.opcodes;

import com.github.chip.emulator.core.ExecutionContext;
import com.github.chip.emulator.core.Register;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author helloween
 */
public class Opcode0x5Test {
    private Opcode opcodeHandler;

    @Before
    public void setUp() throws Exception {
        opcodeHandler = new Opcode0x5();
    }

    @Test
    public void execute() throws Exception {
        int opcode = 0x0230;
        ExecutionContext context = new ExecutionContext();
        int offset = context.getOffset();
        context.setRegister(new Register(0x2, 0x0));
        context.setRegister(new Register(0x3, 0x1));
        opcodeHandler.execute(opcode, context);
        assertEquals(offset, context.getOffset());
        context.setRegister(new Register(0x2, 0x1));
        assertEquals(Opcode.OPCODE_SIZE * 2, opcodeHandler.execute(opcode, context));
    }

}