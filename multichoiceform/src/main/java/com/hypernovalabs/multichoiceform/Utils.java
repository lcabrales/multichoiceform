package com.hypernovalabs.multichoiceform;

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
}
