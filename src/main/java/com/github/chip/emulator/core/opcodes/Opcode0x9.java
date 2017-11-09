package com.github.chip.emulator.core.opcodes;

import com.github.chip.emulator.core.ExecutionContext;
import com.github.chip.emulator.core.exceptions.UnsupportedOpcodeException;
import org.apache.log4j.Logger;

/**
 * @author helloween
 */
public class Opcode0x9 implements Opcode {
    private static final Logger LOGGER = Logger.getLogger(Opcode0x9.class);

    @Override
    public void execute(int opcode, ExecutionContext executionContext) throws UnsupportedOpcodeException {
        int firstRegister   = (opcode & 0x0F00) >> 8;
        int secondRegister  = (opcode & 0x00F0) >> 4;
        LOGGER.trace(String.format("condition V%d != V%d", firstRegister, secondRegister));
        if (executionContext.getRegisters()[firstRegister].getValue() != executionContext.getRegisters()[secondRegister].getValue())
            executionContext.setOffset(executionContext.getOffset() + 2);
    }
}
