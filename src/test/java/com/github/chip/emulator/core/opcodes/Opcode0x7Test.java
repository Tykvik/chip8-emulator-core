package com.github.chip.emulator.core.opcodes;

import com.github.chip.emulator.core.ExecutionContext;
import com.github.chip.emulator.core.Register;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author helloween
 */
public class Opcode0x7Test {
    @Test
    public void execute() throws Exception {
        int opcode = 0x02EE;
        Opcode opcodeHandler = Opcode0x7.newInstance(opcode);
        ExecutionContext context = new ExecutionContext();
        context.setRegister(new Register(0x2, 0x1));
        opcodeHandler.execute(context);
        assertEquals(0xEE + 0x1, context.getRegister(0x2).getValue());
    }
}