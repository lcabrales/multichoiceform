package com.hypernovalabs.multichoiceform;

/**
 * Created by lucascabrales on 1/11/18.
 * <p>
 *     Defines the possible durations of the validation animation
 * </p>
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
