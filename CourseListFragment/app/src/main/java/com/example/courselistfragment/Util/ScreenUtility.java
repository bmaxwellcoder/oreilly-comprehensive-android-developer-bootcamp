package com.example.courselistfragment.Util;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Display;

public class ScreenUtility {
    private Activity activity;
    private float dpWidth;
    private float dpHeight;

    public ScreenUtility(Activity activity) {
        this.activity = activity;

        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();

        display.getMetrics(outMetrics);

        float density = activity.getResources().getDisplayMetrics().density;

        dpHeight = outMetrics.heightPixels / density;
        dpWidth = outMetrics.widthPixels / density;
    }

    public float getDpWidth() {
        return dpWidth;
    }

    public void setDpWidth(float dpWidth) {
        this.dpWidth = dpWidth;
    }

    public float getDpHeight() {
        return dpHeight;
    }

    public void setDpHeight(float dpHeight) {
        this.dpHeight = dpHeight;
    }
}
