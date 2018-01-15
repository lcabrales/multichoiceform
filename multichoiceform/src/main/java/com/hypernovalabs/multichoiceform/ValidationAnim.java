package com.hypernovalabs.multichoiceform;

import android.support.annotation.IdRes;

/**
 * Define the possible animations for the validation.
 */

public enum ValidationAnim {
    SHAKE(R.anim.shake),
    SHAKE_HORIZONTAL(R.anim.shake_horizontal),
    SHAKE_VERTICAL(R.anim.shake_vertical);

    @IdRes
    int resId;

    ValidationAnim(int resId) {
        this.resId = resId;
    }

    protected int getResId() {
        return resId;
    }
}
