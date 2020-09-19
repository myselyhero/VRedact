package com.yongyong.vredact;

import android.content.Context;

import com.yongyong.vredact.tool.VRFileUtils;

/**
 * @author yongyong
 *
 * desc： 管理类
 * 
 * @// TODO: 2020/9/18
 */
public class VRKit {

    private static Context mAppContext;

    public static final void init(Context context){
        mAppContext = context;
        VRConstants.initScreen();
        VRFileUtils.initPath();
    }

    /**
     *
     * @return
     */
    public static Context getAppContext() {
        return mAppContext;
    }
}
