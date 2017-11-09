package com.github.chip.emulator.core;

import com.github.chip.emulator.core.opcodes.*;
import com.github.chip.emulator.core.services.EventService;
import org.apache.log4j.Logger;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * @author helloween
 */
public class ProgramExecutor {
    private static final Logger LOGGER = Logger.getLogger(ProgramExecutor.class);

    private final ExecutionContext          executionContext;
    private final Map<Integer, Opcode>      opcodeMap;

    public ProgramExecutor() {
        executionContext = new ExecutionContext();

        opcodeMap = new HashMap<>();
        opcodeMap.put(0x0000, new Opcode0x0());
        opcodeMap.put(0x1000, new Opcode0x1());
        opcodeMap.put(0x2000, new Opcode0x2());
        opcodeMap.put(0x3000, new Opcode0x3());
        opcodeMap.put(0x4000, new Opcode0x4());
        opcodeMap.put(0x5000, new Opcode0x5());
        opcodeMap.put(0x6000, new Opcode0x6());
        opcodeMap.put(0x7000, new Opcode0x7());
        opcodeMap.put(0x8000, new Opcode0x8());
        opcodeMap.put(0x9000, new Opcode0x9());
        opcodeMap.put(0xA000, new Opcode0xA());
        opcodeMap.put(0xB000, new Opcode0xB());
        opcodeMap.put(0xC000, new Opcode0xC());
        opcodeMap.put(0xD000, new Opcode0xD());
        opcodeMap.put(0xE000, new Opcode0xE());
        opcodeMap.put(0xF000, new Opcode0xF());

        EventService.getInstance().registerHandler(this);
    }

    public void run() throws Exception {
        loadProgram();

        for (;;) {
            int opcode = nextOpcode();

            if (opcode == 0)
                break;

            Thread.sleep(3);
            LOGGER.trace(String.format("%X", opcode));
            if (opcodeMap.get(opcode & 0xF000).execute(opcode, executionContext))
                executionContext.setOffset(executionContext.getOffset() + 2);
        }
    }

    private int nextOpcode() {
        final ByteBuffer memory = executionContext.getMemory();
        final int offset = executionContext.getOffset();

        return ((memory.get(offset) << 8) | (memory.get(offset + 1) & 0x00FF)) & 0xFFFF;
    }

    private void loadProgram() throws IOException {
        DataInputStream input = null;
        String file = "";
        try {
            input = new DataInputStream(new FileInputStream(new File(file)));

            for (int offset = 0; input.available() > 0; ++offset)
                executionContext.getMemory().put(executionContext.getOffset() + offset, (byte) (input.readByte() & 0xFF));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(0);
        } finally {
            if (input != null) {
                input.close();
            }
        }
    }
}
