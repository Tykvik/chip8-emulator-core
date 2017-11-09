package com.github.chip.emulator.core.opcodes;

import com.github.chip.emulator.core.ExecutionContext;
import com.github.chip.emulator.core.exceptions.UnsupportedOpcodeException;
import org.apache.log4j.Logger;

import java.util.Random;

/**
 * @author helloween
 */
public class Opcode0xC implements Opcode {
    private static final Logger LOGGER = Logger.getLogger(Opcode0xC.class);
    private static final Random random = new Random();

    @Override
    public void execute(int opcode, ExecutionContext executionContext) throws UnsupportedOpcodeException {
        int register    = (opcode & 0x0F0) >> 4;
        int value       = random.nextInt(0xFF) & (opcode & 0x00FF);
        LOGGER.trace(String.format("V%d = rand() & %#X", register, value));
        executionContext.getRegisters()[register].setValue(value);
    }
}
