package com.github.chip.emulator.core.opcodes;

import com.github.chip.emulator.core.ExecutionContext;
import com.github.chip.emulator.core.IRegister;
import com.github.chip.emulator.core.Register;
import com.github.chip.emulator.core.exceptions.UnsupportedOpcodeException;
import org.apache.log4j.Logger;

import java.nio.ByteBuffer;

/**
 * @author helloween
 */
public class Opcode0xF implements Opcode {
    private static final Logger LOGGER = Logger.getLogger(Opcode0xF.class);

    @Override
    public void execute(int opcode, ExecutionContext executionContext) throws UnsupportedOpcodeException {
        int register = (opcode & 0x0F00) >> 8;

        switch (opcode & 0x00FF) {
            case 0x07: {
                LOGGER.trace(String.format("V%d = delayTimer", register));
                executionContext.getRegisters()[register].setValue(executionContext.getDelayTimer());
                break;
            }
            case 0x0A: {
                LOGGER.trace(String.format("V%d = key", register));
                break;
            }
            case 0x15: {
                LOGGER.trace(String.format("delayTimer = V%d", register));
                executionContext.setDelayTimer(executionContext.getRegisters()[register].getValue());
                break;
            }
            case 0x18: {
                LOGGER.trace(String.format("soundTimer = V%d", register));
                executionContext.setSoundTimer(executionContext.getRegisters()[register].getValue());
                break;
            }
            case 0x1E: {
                LOGGER.trace(String.format("I += V%d", register));
                executionContext.getRegisters()[15].setValue(executionContext.getiRegister().add(executionContext.getRegisters()[register]) ? 0x1 : 0x0);
                break;
            }
            case 0x29: {
                LOGGER.trace(String.format("I = sprite_addr[V%d]", register));
                executionContext.getiRegister().setValue(executionContext.getRegisters()[register].getValue() * 5);
                break;
            }
            case 0x33: {
                LOGGER.trace("BCD");
                ByteBuffer memory       = executionContext.getMemory();
                IRegister  iRegister    = executionContext.getiRegister();
                Register[] registers    = executionContext.getRegisters();

                memory.put(iRegister.getValue(), (byte) (registers[register].getValue() / 100));
                memory.put(iRegister.getValue() + 1, (byte) ((registers[register].getValue() / 10) % 10));
                memory.put(iRegister.getValue() + 2, (byte) ((registers[register].getValue() / 100) % 10));
                break;
            }
            case 0x55: {
                LOGGER.trace("reg_dump(Vx, &I)");
                ByteBuffer memory       = executionContext.getMemory();
                IRegister  iRegister    = executionContext.getiRegister();

                for (int i = 0; i <= register; ++i)
                    memory.put(iRegister.getValue() + i, (byte) executionContext.getRegisters()[i].getValue());

                iRegister.add(register + 1);
                break;
            }
            case 0x65: {
                LOGGER.trace("reg_load(Vx, &I)");
                ByteBuffer memory       = executionContext.getMemory();
                IRegister  iRegister    = executionContext.getiRegister();

                for (int i = 0; i <= register; ++i)
                    executionContext.getRegisters()[i].setValue(memory.get(iRegister.getValue() + i));

                iRegister.add(register + 1);
                break;
            }
            default:
                throw new UnsupportedOpcodeException("unsupported 0xFXXX opcode");
        }
    }
}
