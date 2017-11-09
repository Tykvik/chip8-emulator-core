package com.github.chip.emulator.core.opcodes;

import com.github.chip.emulator.core.ExecutionContext;
import com.github.chip.emulator.core.exceptions.UnsupportedOpcodeException;
import org.apache.log4j.Logger;

/**
 * @author helloween
 */
public class Opcode0x7 implements Opcode {
    private static final Logger LOGGER = Logger.getLogger(Opcode0x7.class);

    @Override
    public void execute(int opcode, ExecutionContext executionContext) throws UnsupportedOpcodeException {
        int register = (opcode & 0x0F00) >> 8;
        int value    = opcode & 0x00FF;
        LOGGER.trace(String.format("add %#X to V%d", value, register));
        executionContext.getRegisters()[register].add(value);
    }
}
