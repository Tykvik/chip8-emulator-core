package com.github.chip.emulator.core.opcodes;

import com.github.chip.emulator.core.ExecutionContext;
import com.github.chip.emulator.core.events.ClearVRAMEvent;
import com.github.chip.emulator.core.exceptions.UnsupportedOpcodeException;
import com.github.chip.emulator.core.services.EventService;
import com.google.common.eventbus.Subscribe;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author helloween
 */
public class Opcode0x0Test {
    private Opcode opcodeHandler;
    private boolean clearVramEventCalled;

    @Before
    public void init() {
        opcodeHandler = new Opcode0x0();
    }

    @Test
    public void execute() throws Exception {
        int opcode = 0xEE;
        ExecutionContext context = new ExecutionContext();
        context.pushToCallStack(0x1);
        opcodeHandler.execute(opcode, context);
        assertEquals(0x1, context.getOffset());
    }

    @Test(expected = UnsupportedOpcodeException.class)
    public void execute2() throws Exception {
        int opcode = 0xEF;
        opcodeHandler.execute(opcode, new ExecutionContext());
    }

    @Test
    public void execute3() throws Exception {
        int opcode = 0xE0;
        ExecutionContext context = new ExecutionContext();
        clearVramEventCalled = false;
        new ClearVRAMEventListener();
        opcodeHandler.execute(opcode, context);
        assertTrue(clearVramEventCalled);
    }

    private class ClearVRAMEventListener {
        public ClearVRAMEventListener() {
            EventService.getInstance().registerHandler(this);
        }

        @Subscribe
        @SuppressWarnings("unused")
        public void handleClearVRAMEvent(ClearVRAMEvent event) {
            clearVramEventCalled = true;
        }
    }
}