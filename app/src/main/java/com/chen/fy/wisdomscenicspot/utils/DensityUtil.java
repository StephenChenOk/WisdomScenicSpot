package com.chen.fy.wisdomscenicspot.utils;

import android.content.Context;

import com.clj.fastble.callback.BleScanCallback;

public class DensityUtil {

    //根据手机分辨率从dpi单位转化为px单位(像素)
    public static int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    //根据手机分辨率从px转化为dpi
    public static int pxToDpi(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}