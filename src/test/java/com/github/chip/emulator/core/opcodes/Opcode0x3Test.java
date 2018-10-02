package com.github.chip.emulator.core.opcodes;

import com.github.chip.emulator.core.ExecutionContext;
import com.github.chip.emulator.core.Register;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author helloween
 */
public class Opcode0x3Test {
    @Test
    public void execute() throws Exception {
        int opcode = 0x02FF;
        Opcode opcodeHandler = Opcode0x3.newInstance(opcode);
        ExecutionContext context = new ExecutionContext();
        context.setRegister(new Register(0x2, 0xFF));
        assertEquals(AbstractOpcode.OPCODE_SIZE * 2, opcodeHandler.execute(context));
        context.setRegister(new Register(0x2, 0xF0));
        assertEquals(AbstractOpcode.OPCODE_SIZE, opcodeHandler.execute(context));
    }
}