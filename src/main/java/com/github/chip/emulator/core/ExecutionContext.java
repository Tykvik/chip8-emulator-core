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
    private static final int MEMORY_SIZE            = 1 << 12;
    private static final int REGISTER_COUNT         = 1 << 4;
    private static final int USER_REGISTER_COUNT    = 1 << 3;
    private static final int SCREEN_WIDTH           = 64;
    private static final int SCREEN_HEIGHT          = 32;

    private final ByteBuffer             memory;
    private final Map<Integer, Register> registers;
    private final Map<Integer, Register> userRegisters;
    private AtomicReference<IRegister>   iRegister;
    private final Deque<Integer>         stack;
    private final Map<Integer, Boolean>  keys;
    private volatile AtomicInteger       offset;
    private volatile AtomicInteger       delayTimer;
    private volatile AtomicInteger       soundTimer;
    private final Timer                  timer;
    private VRAM                         vram;
    private ReadWriteLock                readWriteLock;

    public ExecutionContext() {
        this.memory         = ByteBuffer.allocateDirect(MEMORY_SIZE);
        this.registers      = new ConcurrentHashMap<>();
        this.userRegisters  = new ConcurrentHashMap<>();
        this.iRegister      = new AtomicReference<>(new IRegister());
        this.stack          = new ConcurrentLinkedDeque<>();
        this.offset         = new AtomicInteger(0x200);
        this.delayTimer     = new AtomicInteger();
        this.soundTimer     = new AtomicInteger();
        this.keys           = new ConcurrentHashMap<>();
        this.readWriteLock  = new ReentrantReadWriteLock();
        this.vram           = new VRAM();

        for (int i = 0; i < REGISTER_COUNT; ++i) {
            registers.put(i, new Register(i));
            keys.put(i, Boolean.FALSE);
        }

        for (int i = 0; i < USER_REGISTER_COUNT; ++i) {
            userRegisters.put(i, new Register(i));
        }

        memory.position(offset.get());

        this.timer = new Timer(true);
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
                    AsyncEventService.getInstance().postEvent(PlaySoundEvent.INSTANCE);
                }
                AsyncEventService.getInstance().postEvent(new ChangeDelayTimerValueEvent(delayTimer.get()));
                AsyncEventService.getInstance().postEvent(new ChangeSoundTimerValueEvent(soundTimer.get()));
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
     * sets new user register value
     *
     * @param register new user register value
     */
    public void setUserRegister(Register register) {
        this.userRegisters.put(register.getNumber(), register);
    }

    /**
     * @param registerNumber user register number
     * @return user register
     */
    public Register getUserRegister(int registerNumber) {
        return this.userRegisters.get(registerNumber);
    }

    /**
     * sets new index register value
     *
     * @param register new index register value
     */
    public void setIndexRegister(IRegister register) {
        AsyncEventService.getInstance().postEvent(new ChangeIndexRegisterValueEvent(register.getValue()));
        iRegister.getAndSet(register);
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
        this.delayTimer.getAndSet(delayTimer);
        AsyncEventService.getInstance().postEvent(new ChangeDelayTimerValueEvent(delayTimer));
    }

    public int getDelayTimer() {
        return this.delayTimer.get();
    }

    public void setSoundTimer(int soundTimer) {
        this.soundTimer.getAndSet(soundTimer);
        AsyncEventService.getInstance().postEvent(new ChangeSoundTimerValueEvent(soundTimer));
    }

    public int getSoundTimer() {
        return soundTimer.get();
    }

    public int getOffset() {
        return offset.get();
    }

    public void setOffset(int offset) {
        this.offset.getAndSet(offset);
        AsyncEventService.getInstance().postEvent(new ChangeProgramCounterEvent(offset - 0x200));
    }

    public void setKey(int key, boolean value) {
        this.keys.put(key, value);
    }

    public boolean getKey(int key) {
        return this.keys.get(key);
    }

    public synchronized void setExtendedScreenMode() {
        if (vram.screenWidth != (SCREEN_WIDTH << 1)) {
            this.vram.dispose();
            this.vram = new VRAM(SCREEN_WIDTH << 1, SCREEN_HEIGHT << 1);
        }
    }

    public void dispose() {
        vram.dispose();
        timer.cancel();
        timer.purge();
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
        public void draw(int x, int y, int height) {
            setRegister(new Register(0xF, 0x0));

            int spriteHeight = height;
            int spriteWidth  = 0x8;
            int multiplier   = 0x1;

            if (screenWidth > SCREEN_WIDTH && height == 0) { // extended mode
                spriteHeight = spriteWidth = 0x10;
                multiplier   = 0x2;
            }

            for(int j = 0; j < spriteHeight; j++) {
                int dat = (memory.get((j * multiplier) + iRegister.get().getValue())) & 0xFF;
                if (spriteWidth == 0x10) {
                    dat <<= 8;
                    dat |= ((memory.get((j * multiplier) + 1 + iRegister.get().getValue())) & 0xFF);
                }
                for(int i = 0; i < spriteWidth; i++) {
                    boolean skip;
                    if (spriteWidth == 0x10)
                        skip = (dat & (0x8000 >> i)) == 0;
                    else
                        skip = (dat & (0x80 >> i)) == 0;

                    if(!skip) {
                        int rx = i + x;
                        int ry = j + y;

                        if (rx >= screenWidth || ry >= screenHeight) continue;

                        if (vram[rx][ry])
                            setRegister(new Register(0xF, 0x1));

                        vram[rx][ry] ^= true;
                    }
                }
            }
            AsyncEventService.getInstance().postEvent(new RefreshScreenEvent(Arrays.copyOf(vram, vram.length)));
        }

        public void dispose() {
            EventService.getInstance().deleteHandler(this);
        }

        @Subscribe
        @SuppressWarnings("unused")
        public void handleDrawEvent(DrawEvent drawEvent) {
            draw(drawEvent.getX(), drawEvent.getY(), drawEvent.getHeight());
        }

        @Subscribe
        @SuppressWarnings("unused")
        public void handleClearEvent(ClearVRAMEvent event) {
            for (int i = 0; i < screenWidth; ++i)
                for (int j = 0; j < screenHeight; ++j)
                    vram[i][j] = false;
            AsyncEventService.getInstance().postEvent(new RefreshScreenEvent(Arrays.copyOf(vram, vram.length)));
        }

        @Subscribe
        @SuppressWarnings("unused")
        public void handleScrollDownEvent(ScrollDownEvent event) {
            for (int i = 0; i < vram.length; ++i) {
                for (int j = vram[i].length - 1; j >= event.getCount(); --j)
                    vram[i][j] = vram[i][j - event.getCount()];
                for(int x  = event.getCount() - 1; x >= 0; --x) {
                    vram[i][x] = false;
                }
            }
        }

        @Subscribe
        @SuppressWarnings("unused")
        public void handleScrollRightEvent(ScrollRightEvent event) {
            for (int i = vram.length - 1 ; i >= 4; --i) {
                for (int j = 0; j < vram[0].length; ++j)
                    vram[i][j] = vram[i - 4][j];
            }
            for (int i = 0; i < 4; ++i)
                for (int j = 0; j < vram[0].length; ++j)
                    vram[i][j] = false;
        }

        @Subscribe
        @SuppressWarnings("unused")
        public void handleScrollLeftEvent(ScrollLeftEvent event) {
            for (int i = 0 ; i < vram.length - 4; ++i) {
                for (int j = 0; j < vram[0].length; ++j)
                    vram[i][j] = vram[i + 4][j];
            }
            for (int i = vram.length - 1; i >= vram.length - 4; --i)
                for (int j = 0; j < vram[0].length; ++j)
                    vram[i][j] = false;
        }
    }
}
