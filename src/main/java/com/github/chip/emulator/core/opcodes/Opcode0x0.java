package com.github.chip.emulator.core.opcodes;

import com.github.chip.emulator.core.ExecutionContext;
import com.github.chip.emulator.core.exceptions.UnsupportedOpcodeException;
import org.apache.log4j.Logger;

/**
 * @author helloween
 */
public class Opcode0x0 implements Opcode {
    private static final Logger LOGGER = Logger.getLogger(Opcode0x0.class);

    @Override
    public void execute(int opcode, ExecutionContext executionContext) throws UnsupportedOpcodeException {
        switch (opcode & 0x00FF) {
            case 0x00E0: {
                LOGGER.trace("clear screen");
                break;
            }
            case 0x00EE: {
                LOGGER.trace("return from a subroutine");
                executionContext.setOffset(executionContext.getStack().pop() + 2);
                break;
            }
            default:
                throw new UnsupportedOpcodeException("unsupported 0x0XXX opcode");
        }
    }
}
