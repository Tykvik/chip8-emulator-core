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
import com.github.chip.emulator.core.services.EventService;
import com.google.common.eventbus.Subscribe;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author helloween
 */
public class ExecutionContext {
    private static final int MEMORY_SIZE        = 1 << 12;
    private static final int REGISTER_COUNT     = 1 << 4;
    private static final int SCREEN_WIDTH       = 64;
    private static final int SCREEN_HEIGHT      = 32;

    private final ByteBuffer        memory;
    private final Register[]        registers;
    private final IRegister         iRegister;
    private final Deque<Integer>    stack;
    private final boolean[]         keys;
    private int                     offset;
    private AtomicInteger           delayTimer;
    private AtomicInteger           soundTimer;

    public ExecutionContext() {
        this.memory         = ByteBuffer.allocateDirect(MEMORY_SIZE);
        this.registers      = new Register[REGISTER_COUNT];
        this.iRegister      = new IRegister();
        this.stack          = new ArrayDeque<>();
        this.offset         = 0x200;
        this.delayTimer     = new AtomicInteger();
        this.soundTimer     = new AtomicInteger();
        this.keys           = new boolean[REGISTER_COUNT];

        for (short i = 0; i < REGISTER_COUNT; ++i)
            registers[i] = new Register(i);

        memory.position(offset);
        new VRAM();

        Timer timer = new Timer(true);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                int delayTimerValue = delayTimer.get();
                delayTimer.compareAndSet(delayTimerValue, Math.max(delayTimerValue - 1, 0));

                int soundTimerValue = soundTimer.get();
                if (soundTimerValue != 0) {
                    soundTimer.compareAndSet(soundTimerValue, Math.max(soundTimerValue - 1, 0));
                    EventService.getInstance().postEvent(PlaySoundEvent.INSTANCE);
                }
            }
        };

        timer.schedule(task, 0, Math.round(1000.0 / 60.0));
    }

    public ByteBuffer getMemory() {
        return memory;
    }

    public Register[] getRegisters() {
        return registers;
    }

    public IRegister getiRegister() {
        return iRegister;
    }

    public Deque<Integer> getStack() {
        return stack;
    }

    public int getDelayTimer() {
        return delayTimer.get();
    }

    public void setDelayTimer(int delayTimer) {
        int oldDelayTimerValue = this.delayTimer.get();
        this.delayTimer.compareAndSet(oldDelayTimerValue, delayTimer);
    }

    public void setSoundTimer(int soundTimer) {
        int oldSoundTimerValue = this.soundTimer.get();
        this.soundTimer.compareAndSet(oldSoundTimerValue, soundTimer);
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = (offset & 0xFFF);
    }

    public boolean[] getKeys() {
        return keys;
    }

    /**
     * video memory
     */
    @SuppressWarnings({"unused", "SpellCheckingInspection"})
    private class VRAM {
        private final boolean[][] vram;

        public VRAM() {
            vram = new boolean[SCREEN_WIDTH][SCREEN_HEIGHT];
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
            registers[0xF].setValue(0x0);

            for(int j = 0; j < height; j++) {
                int dat = (memory.get(j + iRegister.getValue())) & 0xFF;
                for(int i = 0; i < 8; i++) {
                    if((dat & (0x80 >> i)) == 0) continue;

                    int rx = i + x;
                    int ry = j + y;

                    if(rx >= SCREEN_WIDTH || ry >= SCREEN_HEIGHT) continue;

                    if(vram[rx][ry])
                        registers[0xF].setValue(0x1);

                    vram[rx][ry] ^= true;
                }
            }
            EventService.getInstance().postEvent(new RefreshScreenEvent(vram));
        }

        @Subscribe
        @SuppressWarnings("unused")
        public void handleDrawEvent(DrawEvent drawEvent) {
            draw(drawEvent.getX(), drawEvent.getY(), drawEvent.getHeight());
        }

        @Subscribe
        @SuppressWarnings("unused")
        public void handleClearEvent(ClearVRAMEvent event) {
            for (int i = 0; i < SCREEN_WIDTH; ++i)
                for (int j = 0; j < SCREEN_HEIGHT; ++j)
                    vram[i][j] = false;
            EventService.getInstance().postEvent(ClearScreenEvent.INSTANCE);
        }
    }
}
