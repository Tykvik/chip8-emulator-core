package com.github.chip.emulator.core.opcodes;

import com.github.chip.emulator.core.ExecutionContext;
import com.github.chip.emulator.core.exceptions.UnsupportedOpcodeException;
import org.apache.log4j.Logger;

/**
 * @author helloween
 */
public class Opcode0xB implements Opcode {
    private static final Logger LOGGER = Logger.getLogger(Opcode0xB.class);

    @Override
    public void execute(int opcode, ExecutionContext executionContext) throws UnsupportedOpcodeException {
        int value = opcode & 0x0FFF;
        LOGGER.trace(String.format("memory offset = V0(%#X) + %#X", executionContext.getRegisters()[0].getValue(), value));
        executionContext.setOffset(executionContext.getRegisters()[0].getValue() + value);
    }
}
