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

import com.github.chip.emulator.core.events.*;
import com.github.chip.emulator.core.opcodes.*;
import com.github.chip.emulator.core.services.AsyncEventService;
import com.google.common.eventbus.Subscribe;
import org.apache.log4j.Logger;

import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author helloween
 */
@SuppressWarnings("unused")
public class ProgramExecutor implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(ProgramExecutor.class);

    private final ExecutionContext          executionContext;
    private final ByteBuffer                programBuffer;
    private final AtomicInteger             delay;
    private final AtomicInteger             waitKeyPressEventRegister;
    private volatile boolean                stopFlag;
    private volatile boolean                pauseFlag;
    private volatile boolean                nextStep;
    private volatile boolean                waitKeyPress;


    /**
     * ctor
     *
     * @param programBuffer ROM as ByteBuffer
     * @param delayInMillis delay in millis
     */
    public ProgramExecutor(ByteBuffer programBuffer, int delayInMillis) {
        this(programBuffer, delayInMillis, false);
    }

    /**
     * ctor
     *
     * @param programBuffer ROM as ByteBuffer
     * @param delayInMillis delay in millis
     * @param pauseFlag pauseFlag (for debug)
     */
    public ProgramExecutor(ByteBuffer programBuffer, int delayInMillis, boolean pauseFlag) {
        this.executionContext           = new ExecutionContext();
        this.delay                      = new AtomicInteger(delayInMillis);
        this.stopFlag                   = false;
        this.pauseFlag                  = pauseFlag;
        this.programBuffer              = programBuffer;
        this.waitKeyPressEventRegister  = new AtomicInteger(-1);

        new FontLoader(this.executionContext).load();
        programBuffer.rewind();
        while (programBuffer.hasRemaining())
            executionContext.writeToMemory(programBuffer.get());
        AsyncEventService.getInstance().registerHandler(this);
    }

    @Override
    public void run() {
        try {
            List<Opcode> program = new Disassembler(this.programBuffer).disassemble();
            Iterator<Opcode> iterator = program.iterator();
            for (;;) {
                if (stopFlag) {
                    executionContext.dispose();
                    AsyncEventService.getInstance().deleteHandler(this);
                    return;
                }

                while (pauseFlag && !nextStep)
                        Thread.yield();

                Thread.sleep(delay.get());

                Opcode opcode = program.get((executionContext.getOffset() - 0x200) / AbstractOpcode.OPCODE_SIZE);

                executionContext.setOffset(opcode.execute(executionContext) + executionContext.getOffset());
                this.nextStep = false;
            }
        } catch (Exception e) {
            LOGGER.error(e);
            return;
        }
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void handleKeyPressEvent(PressKeyEvent event) {
       if (waitKeyPress) {
            this.waitKeyPress = false;
            executionContext.setRegister(new Register(waitKeyPressEventRegister.get(), event.getKeyNumber()));
            waitKeyPressEventRegister.getAndSet(-1);
            this.pauseFlag = false;
        }
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

    @SuppressWarnings("unused")
    @Subscribe
    public void handleNextStepEvent(NextStepEvent event) {
        this.nextStep = true;
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void handleExtendedScreenMode(EnableExtendedScreenModeEvent event) {
        this.executionContext.setExtendedScreenMode();
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void handleWaitKeyPressEvent(WaitKeyPressEvent event) {
        this.pauseFlag    = true;
        this.waitKeyPress = true;
        this.waitKeyPressEventRegister.getAndSet(event.getRegister());
    }
}
