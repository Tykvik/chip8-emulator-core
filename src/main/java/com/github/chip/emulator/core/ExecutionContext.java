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
import com.github.chip.emulator.core.services.AsyncEventService;
import com.github.chip.emulator.core.services.EventService;
import com.google.common.eventbus.Subscribe;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author helloween
 */
public class ExecutionContext {
    private static final int MEMORY_SIZE        = 1 << 12;
    private static final int REGISTER_COUNT     = 1 << 4;
    private static final int SCREEN_WIDTH       = 64;
    private static final int SCREEN_HEIGHT      = 32;

    private final ByteBuffer             memory;
    private final Map<Integer, Register> registers;
    private AtomicReference<IRegister>   iRegister;
    private final Deque<Integer>         stack;
    private final Map<Integer, Boolean>  keys;
    private volatile AtomicInteger       offset;
    private volatile AtomicInteger       delayTimer;
    private volatile AtomicInteger       soundTimer;
    private ReadWriteLock                readWriteLock;

    public ExecutionContext() {
        this.memory         = ByteBuffer.allocateDirect(MEMORY_SIZE);
        this.registers      = new ConcurrentHashMap<>();
        this.iRegister      = new AtomicReference<>(new IRegister());
        this.stack          = new ConcurrentLinkedDeque<>();
        this.offset         = new AtomicInteger(0x200);
        this.delayTimer     = new AtomicInteger();
        this.soundTimer     = new AtomicInteger();
        this.keys           = new ConcurrentHashMap<>();
        this.readWriteLock  = new ReentrantReadWriteLock();

        for (int i = 0; i < REGISTER_COUNT; ++i) {
            registers.put(i, new Register(i));
            keys.put(i, Boolean.FALSE);
        }

        memory.position(offset.get());
        new VRAM();

        Timer timer = new Timer(true);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                int delayTimerValue;
                do {
                   delayTimerValue = delayTimer.get();
                } while (!delayTimer.compareAndSet(delayTimerValue, Math.max(delayTimerValue - 1, 0)));

                int soundTimerValue;
                do {
                   soundTimerValue = soundTimer.get();
                } while (!soundTimer.compareAndSet(soundTimerValue, Math.max(soundTimerValue - 1, 0)));

                if (soundTimer.get() != 0) {
                    EventService.getInstance().postEvent(PlaySoundEvent.INSTANCE);
                }
            }
        };

        timer.schedule(task, 0, Math.round(1000.0 / 60.0));
    }

    /**
     * sets new register value
     * @param register new register value
     */
    public void setRegister(Register register) {
        AsyncEventService.getInstance().postEvent(new ChangeRegisterValueEvent(register.getNumber(), register.getValue()));
        this.registers.put(register.getNumber(), register);
    }

    /**
     * @param registerNumber register number
     * @return register
     */
    public Register getRegister(int registerNumber) {
        return this.registers.get(registerNumber);
    }

    /**
     * sets new index register value
     *
     * @param register new index register value
     */
    public void setIndexRegister(IRegister register) {
        AsyncEventService.getInstance().postEvent(new ChangeIndexRegisterValue(register.getValue()));
        IRegister oldValue;
        do {
            oldValue = iRegister.get();
        } while (!iRegister.compareAndSet(oldValue, register));
    }

    /**
     * @return index register
     */
    public IRegister getIndexRegister() {
        return this.iRegister.get();
    }

    /**
     * writes byte to memory
     *
     * @param offset offset
     * @param value value
     */
    public void writeToMemory(int offset, byte value) {
        try {
            this.readWriteLock.writeLock().lock();
            this.memory.put(offset, value);
        } finally {
            this.readWriteLock.writeLock().unlock();
        }
    }

    /**
     * writes byte to memory with current offset
     *
     * @param value value
     */
    public void writeToMemory(byte value) {
        try {
            this.readWriteLock.writeLock().lock();
            this.memory.put(value);
        } finally {
            this.readWriteLock.writeLock().unlock();
        }
    }

    /**
     * @param offset memory offset
     * @return value
     */
    public byte getMemoryValue(int offset) {
        try {
            this.readWriteLock.readLock().lock();
            return this.memory.get(offset);
        } finally {
            this.readWriteLock.readLock().unlock();
        }
    }

    public void pushToCallStack(int offset) {
        this.stack.push(offset);
    }

    public void popCallStack() {
        setOffset(this.stack.pop());
    }

    public void setDelayTimer(int delayTimer) {
        int oldDelayTimerValue;
        do {
            oldDelayTimerValue = this.delayTimer.get();
        } while (!this.delayTimer.compareAndSet(oldDelayTimerValue, delayTimer));
    }

    public int getDelayTimer() {
        return this.delayTimer.get();
    }

    public void setSoundTimer(int soundTimer) {
        int oldSoundTimerValue;
        do {
            oldSoundTimerValue = this.soundTimer.get();
        } while(!this.soundTimer.compareAndSet(oldSoundTimerValue, soundTimer));
    }

    public int getSoundTimer() {
        return soundTimer.get();
    }

    public int getOffset() {
        return offset.get();
    }

    public void setOffset(int offset) {
        int oldValue;
        do {
            oldValue = this.offset.get();
        } while (!this.offset.compareAndSet(oldValue, offset));
    }

    public void setKey(int key, boolean value) {
        this.keys.put(key, value);
    }

    public boolean getKey(int key) {
        return this.keys.get(key);
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void handleChangeRegisterValueEvent(ChangeRegisterValueEvent event) {
        setRegister(new Register(event.getRegisterNumber(), event.getValue()));
    }

    /**
     * video memory
     */
    @SuppressWarnings({"unused", "SpellCheckingInspection"})
    private class VRAM {
        private final boolean[][] vram;
        private final int         screenWidth;
        private final int         screenHeight;

        /**
         * ctor
         */
        public VRAM() {
            this(SCREEN_WIDTH, SCREEN_HEIGHT);
        }

        /**
         * ctor
         *
         * @param screenWidth width of screen in pixels
         * @param screenHeight height of screen in pixels
         */
        public VRAM(int screenWidth, int screenHeight) {
            this.screenWidth    = screenWidth;
            this.screenHeight   = screenHeight;
            this.vram           = new boolean[screenWidth][screenHeight];
            EventService.getInstance().registerHandler(this);
        }

        /**
         * draw
         *
         * @param x x coordinate
         * @param y y coordinate
         * @param height height
         */
        public synchronized void draw(int x, int y, int height) {
            setRegister(new Register(0xF, 0x0));

            for(int j = 0; j < height; j++) {
                int dat = (memory.get(j + iRegister.get().getValue())) & 0xFF;
                for(int i = 0; i < 8; i++) {
                    if((dat & (0x80 >> i)) == 0) continue;

                    int rx = i + x;
                    int ry = j + y;

                    if(rx >=  screenWidth|| ry >= screenHeight) continue;

                    if(vram[rx][ry])
                        setRegister(new Register(0xF, 0x1));

                    vram[rx][ry] ^= true;
                }
            }
            AsyncEventService.getInstance().postEvent(new RefreshScreenEvent(Arrays.copyOf(vram, vram.length)));
        }

        @Subscribe
        @SuppressWarnings("unused")
        public void handleDrawEvent(DrawEvent drawEvent) {
            draw(drawEvent.getX(), drawEvent.getY(), drawEvent.getHeight());
        }

        @Subscribe
        @SuppressWarnings("unused")
        public synchronized void handleClearEvent(ClearVRAMEvent event) {
            for (int i = 0; i < SCREEN_WIDTH; ++i)
                for (int j = 0; j < SCREEN_HEIGHT; ++j)
                    vram[i][j] = false;
            AsyncEventService.getInstance().postEvent(new RefreshScreenEvent(Arrays.copyOf(vram, vram.length)));
        }
    }
}
