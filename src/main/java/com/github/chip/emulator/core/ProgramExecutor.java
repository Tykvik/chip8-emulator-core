/**
 * MIT License
 * Copyright (c) 2017 Helloween
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.chip.emulator.core;

import com.github.chip.emulator.core.events.PauseEvent;
import com.github.chip.emulator.core.events.PressKeyEvent;
import com.github.chip.emulator.core.events.SetDelayEvent;
import com.github.chip.emulator.core.events.StopEvent;
import com.github.chip.emulator.core.opcodes.*;
import com.github.chip.emulator.core.services.EventService;
import com.google.common.eventbus.Subscribe;
import org.apache.log4j.Logger;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author helloween
 */
@SuppressWarnings("unused")
public class ProgramExecutor implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(ProgramExecutor.class);

    private final ExecutionContext          executionContext;
    private final Map<Integer, Opcode>      opcodeMap;
    private final AtomicInteger             delay;
    private volatile boolean                stopFlag;
    private volatile boolean                pauseFlag;

    /**
     * ctor
     *
     * @param programBuffer ROM as ByteBuffer
     * @param delayInMillis delay in millis
     */
    public ProgramExecutor(ByteBuffer programBuffer, int delayInMillis) {
        this.executionContext   = new ExecutionContext();
        this.delay              = new AtomicInteger(delayInMillis);
        this.stopFlag           = false;
        this.pauseFlag          = false;

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

        programBuffer.rewind();
        while (programBuffer.hasRemaining())
            executionContext.writeToMemory(programBuffer.get());
        EventService.getInstance().registerHandler(this);
    }

    @Override
    public void run() {
        try {
            for (;;) {
                if (stopFlag)
                    return;

                while (pauseFlag)
                    Thread.yield();

                Thread.sleep(delay.get());
                int opcode = readOpcode();

                if (opcode == 0x0)
                    break;

                LOGGER.trace(String.format("%X", opcode));
                executionContext.setOffset(opcodeMap.get(opcode & 0xF000).execute(opcode, executionContext) + executionContext.getOffset());
            }
        } catch (Exception e) {
            LOGGER.error(e);
            return;
        }
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void handleKeyPressEvent(PressKeyEvent event) {
        executionContext.setKey(event.getKeyNumber(), true);
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void handleSetDelayEvent(SetDelayEvent event) {
        int oldValue;
        do {
            oldValue = delay.get();
        } while (!this.delay.compareAndSet(oldValue, event.getDelay()));
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void handleStopEvent(StopEvent event) {
        this.stopFlag = true;
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void handlePauseEvent(PauseEvent event) {
        this.pauseFlag = event.isPauseFlag();
    }

    private int readOpcode() {
        final int offset = executionContext.getOffset();
        return ((executionContext.getMemoryValue(offset) << 8) | (executionContext.getMemoryValue(offset + 1) & 0x00FF)) & 0xFFFF;
    }
}
