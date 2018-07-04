package com.hypernovalabs.multichoiceform;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by ldemorais on 7/4/18. ldemorais@hypernovalabs.com
 */
public class ValidateAnimation {
    public static final int SHAKE = 1;
    public static final int SHAKE_HORIZONTAL = 2;
    public static final int SHAKE_VERTICAL = 3;

    @IntDef({SHAKE, SHAKE_HORIZONTAL, SHAKE_VERTICAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Anim {}

    public static int getAnimRes(@Anim int anim) {
        switch (anim) {
            case SHAKE:
                return R.anim.mcf_shake;
            case SHAKE_HORIZONTAL:
                return R.anim.mcf_shake_horizontal;
            case SHAKE_VERTICAL:
                return R.anim.mcf_shake_vertical;
        }

        return 0;
    }

    public static final int SHORT = 300;
    public static final int MEDIUM = 600;
    public static final int LONG = 900;

    @IntDef({SHORT, MEDIUM, LONG})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Duration {}
}
