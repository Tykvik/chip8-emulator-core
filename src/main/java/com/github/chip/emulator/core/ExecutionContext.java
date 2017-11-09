package com.github.chip.emulator.core;

import com.github.chip.emulator.core.events.PlaySoundEvent;
import com.github.chip.emulator.core.services.EventService;

import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author helloween
 */
public class ExecutionContext {
    private static final int MEMORY_SIZE        = 1 << 12;
    private static final int REGISTER_COUNT     = 1 << 4;
    private static final int[] FONT = {
            0xF0, 0x90, 0x90, 0x90, 0xF0, // 0
            0x20, 0x60, 0x20, 0x20, 0x70, // 1
            0xF0, 0x10, 0xF0, 0x80, 0xF0, // 2
            0xF0, 0x10, 0xF0, 0x10, 0xF0, // 3
            0x90, 0X90, 0xF0, 0x10, 0x10, // 4
            0xF0, 0x80, 0xF0, 0x10, 0xF0, // 5
            0xF0, 0x80, 0xF0, 0x90, 0xF0, // 6
            0xF0, 0x10, 0x20, 0x40, 0x40, // 7
            0xF0, 0x90, 0xF0, 0x90, 0xF0, // 8
            0xF0, 0x90, 0xF0, 0x10, 0xF0, // 9
            0xF0, 0x90, 0xF0, 0x90, 0x90, // A
            0xE0, 0x90, 0xE0, 0x90, 0xE0, // B
            0xF0, 0x80, 0x80, 0x80, 0xF0, // C
            0xE0, 0x90, 0x90, 0x90, 0xE0, // D
            0xF0, 0x80, 0xF0, 0x80, 0xF0, // E
            0xF0, 0x80, 0xF0, 0x80, 0x80  // F
    };

    private final ByteBuffer        memory;
    private final Register[]        registers;
    private final IRegister         iRegister;
    private final Deque<Integer>    stack;
    private final boolean[][]       gfx;
    private int                     offset;
    private int                     delayTimer;
    private int                     soundTimer;

    public ExecutionContext() {
        this.memory         = ByteBuffer.allocateDirect(MEMORY_SIZE);
        this.registers      = new Register[REGISTER_COUNT];
        this.iRegister      = new IRegister();
        this.stack          = new ArrayDeque<>();
        this.gfx            = new boolean[32][64];
        this.offset         = 0x200;

        for (int i = 0; i < REGISTER_COUNT; ++i)
            registers[i] = new Register();
        for (int i = 0; i < FONT.length; ++i)
            memory.put(i, (byte) FONT[i]);

        Timer timer = new Timer(true);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                delayTimer = Math.max(delayTimer - 1, 0);
                if (soundTimer != 0) {
                    soundTimer = Math.max(soundTimer - 1, 0);
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
        return delayTimer;
    }

    public int getSoundTimer() {
        return soundTimer;
    }

    public void setDelayTimer(int delayTimer) {
        this.delayTimer = delayTimer;
    }

    public void setSoundTimer(int soundTimer) {
        this.soundTimer = soundTimer;
    }

    public boolean[][] getGfx() {
        return gfx;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
