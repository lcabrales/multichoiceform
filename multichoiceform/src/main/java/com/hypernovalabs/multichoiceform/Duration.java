package com.hypernovalabs.multichoiceform;

/**
 * Defines the possible durations of the validation animation
 */

public enum Duration {
    SHORT(300),
    MEDIUM(600),
    LONG(1000);

    private int millis;

    Duration(int millis) {
        this.millis = millis;
    }

    protected int getDuration() {
        return millis;
    }
}
