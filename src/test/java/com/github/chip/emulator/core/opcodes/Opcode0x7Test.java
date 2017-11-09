package com.github.chip.emulator.core.opcodes;

import com.github.chip.emulator.core.ExecutionContext;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author helloween
 */
public class Opcode0x7Test {
    private Opcode opcodeHandler;

    @Before
    public void setUp() throws Exception {
        opcodeHandler = new Opcode0x7();
    }

    @Test
    public void execute() throws Exception {
        int opcode = 0x02EE;
        ExecutionContext context = new ExecutionContext();
        context.getRegisters()[2].setValue(0x1);
        opcodeHandler.execute(opcode, context);
        assertEquals(0xEE + 0x1, context.getRegisters()[2].getValue());
    }

}