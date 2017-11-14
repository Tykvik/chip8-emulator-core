package com.github.chip.emulator.core.events;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * @author helloween
 */
public class RefreshScreenEventTest {
    @Test
    public void getScreen() throws Exception {
        boolean[][] screen = new boolean[2][2];
        RefreshScreenEvent event = new RefreshScreenEvent(screen);
        assertTrue(Arrays.equals(screen, event.getScreen()));
    }
}