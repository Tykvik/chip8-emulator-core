package com.github.chip.emulator.core.opcodes;

import com.github.chip.emulator.core.ExecutionContext;
import com.github.chip.emulator.core.exceptions.UnsupportedOpcodeException;
import org.apache.log4j.Logger;

/**
 * @author helloween
 */
public class Opcode0x2 implements Opcode {
    private static final Logger LOGGER = Logger.getLogger(Opcode0x2.class);

    @Override
    public void execute(int opcode, ExecutionContext executionContext) throws UnsupportedOpcodeException {
        int address = opcode & 0x0FFF;
        LOGGER.trace(String.format("call subroutine %#X", address));
        executionContext.getStack().push(executionContext.getOffset());
        executionContext.setOffset(address);
    }
}
