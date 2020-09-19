package com.yongyong.vredact.tool;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.yongyong.vredact.VRKit;

/**
 * @author yongyong
 *
 * desc: 屏幕工具类
 * 
 * @// TODO: 2020/9/18
 */
public class VRScreenTool {

    /**
     * 获取屏幕宽高
     * @return
     */
    public static int[] getScreenSize(){
        if (VRKit.getAppContext() == null)
            return new int[]{0,0};
        DisplayMetrics mDisplayMetrics = VRKit.getAppContext().getResources().getDisplayMetrics();
        return new int[]{mDisplayMetrics.widthPixels,mDisplayMetrics.heightPixels};
    }

    /**
     *
     * @return
     */
    public static int getScreenWidth(){
        if (VRKit.getAppContext() == null)
            return 0;
        DisplayMetrics mDisplayMetrics = VRKit.getAppContext().getResources().getDisplayMetrics();
        return mDisplayMetrics.widthPixels;
    }

    /**
     *
     * @return
     */
    public static int getScreenHeight(){
        if (VRKit.getAppContext() == null)
            return 0;
        DisplayMetrics mDisplayMetrics = VRKit.getAppContext().getResources().getDisplayMetrics();
        return mDisplayMetrics.heightPixels;
    }

    /**
     * dp转px
     * @param dpVal
     * @return
     */
    public static int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, VRKit.getAppContext().getResources().getDisplayMetrics());
    }

    /**
     * sp转px
     * @param context
     * @param spVal
     * @return
     */
    public static int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, VRKit.getAppContext().getResources().getDisplayMetrics());
    }

    /**
     * px转dp
     * @param pxVal
     * @return
     */
    public static float px2dp(float pxVal) {
        final float scale = VRKit.getAppContext().getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }

    /**
     * px转sp
     * @param pxVal
     * @return
     */
    public static float px2sp(float pxVal) {
        return (pxVal / VRKit.getAppContext().getResources().getDisplayMetrics().scaledDensity);
    }
}
