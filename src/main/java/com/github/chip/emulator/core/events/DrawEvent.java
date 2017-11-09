package com.github.chip.emulator.core.events;

/**
 * @author helloween
 */
public class DrawEvent {
    private final int x;
    private final int y;
    private final int height;

    public DrawEvent(int x, int y, int height) {
        this.x      = x;
        this.y      = y;
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHeight() {
        return height;
    }
}
