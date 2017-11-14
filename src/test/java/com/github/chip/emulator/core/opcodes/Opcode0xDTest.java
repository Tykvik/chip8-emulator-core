package com.github.chip.emulator.core.opcodes;

import com.github.chip.emulator.core.ExecutionContext;
import com.github.chip.emulator.core.Register;
import com.github.chip.emulator.core.events.DrawEvent;
import com.github.chip.emulator.core.services.EventService;
import com.google.common.eventbus.Subscribe;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author helloween
 */
public class Opcode0xDTest {
    private int x;
    private int y;
    private int height;


    @Test
    public void execute() throws Exception {
        Register register = new Register(0x1, 0x1);
        Register secondRegister = new Register(0x2, 0x2);
        int opcode = 0x0123;
        ExecutionContext context = new ExecutionContext();
        context.setRegister(register);
        context.setRegister(secondRegister);
        Opcode opcodeHandler = new Opcode0xD();
        new DrawEventListener();
        assertEquals(0x2, opcodeHandler.execute(opcode, context));
        assertEquals(0x1, x);
        assertEquals(0x2, y);
        assertEquals(0x3, height);
    }

    private class DrawEventListener {
        public DrawEventListener() {
            EventService.getInstance().registerHandler(this);
        }

        @Subscribe
        @SuppressWarnings("unused")
        public void handleDrawEvent(DrawEvent event) {
            x = event.getX();
            y = event.getY();
            height = event.getHeight();
        }
    }
}