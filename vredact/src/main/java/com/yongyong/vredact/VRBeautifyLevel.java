package com.yongyong.vredact;

/**
 * @author yongyong
 *
 * desc:美颜级别
 * 
 * @// TODO: 2020/9/18
 */
public enum VRBeautifyLevel {

    LEVEL_0(0f),
    LEVEL_1(1.0f),
    LEVEL_2(0.8f),
    LEVEL_3(0.6f),
    LEVEL_4(0.4f),
    LEVEL_5(0.33f);

    float level;


    VRBeautifyLevel(float v) {
        level = v;
    }

    /**
     *
     * @return
     */
    public float getLevel() {
        return level;
    }
}
