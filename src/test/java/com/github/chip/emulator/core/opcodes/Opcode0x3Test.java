package com.github.chip.emulator.core.opcodes;

import com.github.chip.emulator.core.ExecutionContext;
import com.github.chip.emulator.core.Register;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author helloween
 */
public class Opcode0x3Test {
    private Opcode opcodeHandler;

    @Before
    public void setUp() throws Exception {
        opcodeHandler = new Opcode0x3();
    }

    @Test
    public void execute() throws Exception {
        int opcode = 0x02FF;
        ExecutionContext context = new ExecutionContext();
        context.setRegister(new Register(0x2, 0xFF));
        assertEquals(Opcode.OPCODE_SIZE * 2, opcodeHandler.execute(opcode, context));
        context.setRegister(new Register(0x2, 0xF0));
        assertEquals(Opcode.OPCODE_SIZE, opcodeHandler.execute(opcode, context));
    }
}