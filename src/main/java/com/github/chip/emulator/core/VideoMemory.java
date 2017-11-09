package com.github.chip.emulator.core;

import com.github.chip.emulator.core.events.ClearScreenEvent;
import com.github.chip.emulator.core.events.DrawEvent;
import com.github.chip.emulator.core.events.RefreshScreenEvent;
import com.github.chip.emulator.core.services.EventService;
import com.google.common.eventbus.Subscribe;

import java.nio.ByteBuffer;

/**
 * @author helloween
 */
public class VideoMemory {
    private final boolean[][] videoMemory;
    private final ByteBuffer  memory;
    private final IRegister   iRegister;
    private final Register    VF;

    public VideoMemory(ByteBuffer memory, IRegister iRegister, Register VF) {
        this.videoMemory = new boolean[64][32];
        this.memory      = memory;
        this.iRegister   = iRegister;
        this.VF          = VF;
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
        VF.setValue(0x0);

        for(int j = 0; j < height; j++) {
            int dat = (memory.get(j + iRegister.getValue())) & 0xFF;
            for(int i = 0; i < 8; i++) {
                if((dat & (0x80 >> i)) == 0) continue;

                int rx = i + x;
                int ry = j + y;

                if(rx >= 64 || ry >= 32) continue;

                if(videoMemory[rx][ry])
                    VF.setValue(0x1);

                videoMemory[rx][ry] ^= true;
            }
        }
        EventService.getInstance().postEvent(new RefreshScreenEvent(videoMemory));
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void handleDrawEvent(DrawEvent drawEvent) {
        draw(drawEvent.getX(), drawEvent.getY(), drawEvent.getHeight());
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void handleClearEvent(ClearScreenEvent event) {
        /*for (int i = 0; i < 64; ++i)
            for (int j = 0; j < 32; ++j)
                videoMemory[i][j] = false;*/
    }
}
