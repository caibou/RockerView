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

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
