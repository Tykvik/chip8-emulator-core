package com.github.chip.emulator.core.opcodes;

import com.github.chip.emulator.core.ExecutionContext;
import com.github.chip.emulator.core.Register;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author helloween
 */
public class Opcode0xBTest {
    private Opcode opcodeHandler;

    @Before
    public void setUp() throws Exception {
        opcodeHandler = new Opcode0xB();
    }

    @Test
    public void execute() throws Exception {
        int opcode = 0x0123;
        ExecutionContext context = new ExecutionContext();
        context.setRegister(new Register(0x0, 0x2));
        opcodeHandler.execute(opcode, context);
        assertEquals(0x2 + 0x123, context.getOffset());
    }
}