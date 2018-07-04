package com.hypernovalabs.multichoiceform.config;

import com.hypernovalabs.multichoiceform.BuildConfig;

/**
 * Created by ldemorais on 5/23/18.
 * ldemorais@hypernovalabs.com
 * <p>
 * Main configuration class.
 * </p>
 */
public class MCFConfig {

    private static final String EXTRA_PREFIX = BuildConfig.APPLICATION_ID;

    /**
     * Key to the view ID parameter.
     */
    public static final String EXTRA_TAG_KEY = EXTRA_PREFIX + ".TagKey";
}
