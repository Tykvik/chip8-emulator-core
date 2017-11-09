package com.github.chip.emulator.core.opcodes;

import com.github.chip.emulator.core.ExecutionContext;
import com.github.chip.emulator.core.exceptions.UnsupportedOpcodeException;

/**
 * @author helloween
 */
public interface Opcode {
    void execute(int opcode, ExecutionContext executionContext) throws UnsupportedOpcodeException;
}
