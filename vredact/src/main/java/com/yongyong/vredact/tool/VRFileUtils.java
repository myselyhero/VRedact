package com.yongyong.vredact.tool;

import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;

import com.yongyong.vredact.VRConstants;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author yongyong
 *
 * 文件操作工具类及目录初始化、在初始化之前请确保已获取读写权限
 *
 * @// TODO: 2020/7/21
 */
public class VRFileUtils {

    /**
     *
     */
    public static void initPath() {
        File file = new File(VRConstants.DIR_CACHE);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     *
     * @return
     */
    public static String getPathOfVideo(){
        return VRConstants.DIR_CACHE + System.currentTimeMillis() + ".mp4";
    }

    /**
     *
     * @return
     */
    public static String getPathOfImage(){
        return VRConstants.DIR_CACHE + System.currentTimeMillis() + ".png";
    }

    /**
     * 文件是否存在
     * @param path
     * @return
     */
    public static boolean isExists(String path){
        if (TextUtils.isEmpty(path))
            return false;
        return new File(path).exists();
    }

    /**
     * bitmap保存到本地
     * @param dir
     * @param bitmap
     * @return
     */
    public static String saveBitmap(String dir, Bitmap bitmap) {
        String jpegName = dir + File.separator + System.currentTimeMillis() + ".jpg";
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(jpegName);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bufferedOutputStream);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
            return jpegName;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 删除文件
     * @param path
     * @return
     */
    public static boolean deleteFile(String path) {
        boolean result = false;
        File file = new File(path);
        if (file.exists()) {
            result = file.delete();
        }
        return result;
    }

    /**
     * 判断是否有DK卡
     * @return
     */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
}
