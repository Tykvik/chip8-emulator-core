package com.github.chip.emulator.core.opcodes;

import com.github.chip.emulator.core.ExecutionContext;
import com.github.chip.emulator.core.Register;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author helloween
 */
public class Opcode0x4Test {
    @Test
    public void execute() throws Exception {
        int opcode = 0x02FF;
        Opcode opcodeHandler = Opcode0x4.newInstance(opcode);
        ExecutionContext context = new ExecutionContext();
        int offset = context.getOffset();
        context.setRegister(new Register(0x2, 0xFF));
        opcodeHandler.execute(context);
        assertEquals(offset, context.getOffset());
        context.setRegister(new Register(0x2, 0xF0));
        assertEquals(AbstractOpcode.OPCODE_SIZE * 2, opcodeHandler.execute(context));
    }
}