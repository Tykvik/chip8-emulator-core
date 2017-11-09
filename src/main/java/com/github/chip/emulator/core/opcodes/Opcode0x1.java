package com.github.chip.emulator.core.opcodes;

import com.github.chip.emulator.core.ExecutionContext;
import com.github.chip.emulator.core.exceptions.UnsupportedOpcodeException;
import org.apache.log4j.Logger;

/**
 * @author helloween
 */
public class Opcode0x1 implements Opcode {
    private static final Logger LOGGER = Logger.getLogger(Opcode0x1.class);

    @Override
    public void execute(int opcode, ExecutionContext executionContext) throws UnsupportedOpcodeException {
        int address = opcode & 0x0FFF;
        LOGGER.trace(String.format("jump to address %#X", address));
        executionContext.setOffset(address);
    }
}
