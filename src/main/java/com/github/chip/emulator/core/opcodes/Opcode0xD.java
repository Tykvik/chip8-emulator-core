package com.github.chip.emulator.core.opcodes;

import com.github.chip.emulator.core.ExecutionContext;
import com.github.chip.emulator.core.events.DrawEvent;
import com.github.chip.emulator.core.exceptions.UnsupportedOpcodeException;
import com.github.chip.emulator.core.services.EventService;
import org.apache.log4j.Logger;

/**
 * @author helloween
 */
public class Opcode0xD implements Opcode {
    private static final Logger LOGGER = Logger.getLogger(Opcode0xD.class);

    @Override
    public void execute(int opcode, ExecutionContext executionContext) throws UnsupportedOpcodeException {
        LOGGER.trace("draw");
        int firstRegister   = (opcode & 0x0F00) >> 8;
        int secondRegister  = (opcode & 0x00F0) >> 4;
        int height          = opcode & 0x000F;
        EventService.getInstance().postEvent(new DrawEvent(executionContext.getRegisters()[firstRegister].getValue(),
                                                          executionContext.getRegisters()[secondRegister].getValue(),
                                                          height));
    }
}
