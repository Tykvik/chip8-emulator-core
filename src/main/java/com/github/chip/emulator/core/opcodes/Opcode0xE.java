package com.github.chip.emulator.core.opcodes;

import com.github.chip.emulator.core.ExecutionContext;
import com.github.chip.emulator.core.exceptions.UnsupportedOpcodeException;
import org.apache.log4j.Logger;

/**
 * @author helloween
 */
public class Opcode0xE implements Opcode {
    private static final Logger LOGGER = Logger.getLogger(Opcode0xE.class);

    @Override
    public void execute(int opcode, ExecutionContext executionContext) throws UnsupportedOpcodeException {
        int register = (opcode & 0x0F00) >> 8;

        switch (opcode & 0x00FF) {
            case 0x9E : {
                LOGGER.trace(String.format("key == V%d", register));
                break;
            }
            case 0xA1 : {
                LOGGER.trace(String.format("key != V%d", register));
                break;
            }
            default:
                throw new UnsupportedOpcodeException("unsupported 0xEXXX opcode");
        }
    }
}
