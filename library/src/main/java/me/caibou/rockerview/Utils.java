package me.caibou.rockerview;

import android.content.Context;

/**
 * @author caibou
 */
public class Utils {

    public static boolean range(double value, double min, double max) {
        if (min < value && value <= max) {
            return true;
        }
        return false;
    }

}
