package com.yongyong.vredact;

import android.os.Environment;

import com.yongyong.vredact.tool.VRScreenTool;

/**
 *
 */
public class VRConstants {

    /**
     *
     */
    private static String SD_CARD = Environment.getExternalStorageDirectory().getAbsolutePath();
    private static String APP_PATH = SD_CARD + "/" + VRKit.getAppContext().getPackageName();

    /**
     * 缓存目录
     */
    public static String DIR_CACHE = APP_PATH + "/cache/";


    /**
     * 画幅,视频的样式 9:16 1：1 16:9
     */
    public static final int MODE_POR_9_16 = 0;
    public static final int MODE_POR_1_1 = 1;
    public static final int MODE_POR_16_9 = 2;

    /**
     * 三种画幅的具体显示尺寸
     */
    public static int mode_por_width_9_16;
    public static int mode_por_height_9_16;
    public static int mode_por_width_1_1;
    public static int mode_por_height_1_1;
    public static int mode_por_width_16_9;
    public static int mode_por_height_16_9;

    /**
     * 三种画幅的具体编码尺寸(参考VUE)
     */
    public static final int mode_por_encode_width_9_16 = 540;
    public static final int mode_por_encode_height_9_16 = 960;
    public static final int mode_por_encode_width_1_1 = 540;
    public static final int mode_por_encode_height_1_1 = 540;
    public static final int mode_por_encode_width_16_9 = 960;
    public static final int mode_por_encode_height_16_9 = 540;

    /**
     *
     */
    public static VRBeautifyLevel BEAUTY_LEVEL = VRBeautifyLevel.LEVEL_5;

    /**
     *
     */
    public static void initScreen(){
        int[] size = VRScreenTool.getScreenSize();
        mode_por_width_9_16 = size[0];
        mode_por_height_9_16 = size[1];
        mode_por_width_1_1 = size[0];
        mode_por_height_1_1 = size[0];
        mode_por_width_16_9 = size[0];
        mode_por_height_16_9 = size[0] / 16 * 9;
    }
}
