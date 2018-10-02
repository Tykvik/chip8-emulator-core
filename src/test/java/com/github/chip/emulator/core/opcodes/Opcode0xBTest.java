package com.github.chip.emulator.core.opcodes;

import com.github.chip.emulator.core.ExecutionContext;
import com.github.chip.emulator.core.Register;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author helloween
 */
public class Opcode0xBTest {
    @Test
    public void execute() throws Exception {
        int opcode = 0x0123;
        Opcode opcodeHandler = Opcode0xB.newInstance(opcode);
        ExecutionContext context = new ExecutionContext();
        context.setRegister(new Register(0x0, 0x2));
        opcodeHandler.execute(context);
        assertEquals(0x2 + 0x123, context.getOffset());
    }
}