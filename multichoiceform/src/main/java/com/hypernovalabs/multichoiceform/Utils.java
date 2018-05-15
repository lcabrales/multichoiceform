package com.hypernovalabs.multichoiceform;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.AttrRes;
import android.util.TypedValue;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Defines all of the helper methods for the library.
 */

class Utils {
    /**
     * Returns a date text value from its integer values.
     *
     * @param day        int value.
     * @param month      int value.
     * @param year       int value.
     * @param dateFormat SimpleDateFormat which will parse the date.
     * @return Date text value.
     */
    static String getDateFromInteger(int day, int month, int year, SimpleDateFormat dateFormat) {
        String date = "";
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            date = dateFormat.format(calendar.getTime());// format output
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * Retrieves the default value for the specified theme's attribute.
     *
     * @param context app's context
     * @param attr    themes attribute res.
     * @return default attribute from app's theme
     */
    public static int getDefaultThemeAttr(Context context, @AttrRes int attr) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(attr, typedValue, true);
        return typedValue.data;
    }
}
