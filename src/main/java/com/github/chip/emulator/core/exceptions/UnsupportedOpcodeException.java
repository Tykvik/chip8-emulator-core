package com.github.chip.emulator.core.exceptions;

/**
 * @author helloween
 */
public class UnsupportedOpcodeException extends Exception {

    public UnsupportedOpcodeException() {
    }

    public UnsupportedOpcodeException(String s) {
        super(s);
    }

    public UnsupportedOpcodeException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public UnsupportedOpcodeException(Throwable throwable) {
        super(throwable);
    }

    public UnsupportedOpcodeException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
