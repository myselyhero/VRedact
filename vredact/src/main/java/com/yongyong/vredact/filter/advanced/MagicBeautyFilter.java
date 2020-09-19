package com.yongyong.vredact.filter.advanced;

import android.opengl.GLES20;

import com.yongyong.vredact.R;
import com.yongyong.vredact.VRBeautifyLevel;
import com.yongyong.vredact.VRConstants;
import com.yongyong.vredact.filter.gpuimage.GPUImageFilter;
import com.yongyong.vredact.tool.OpenGlUtils;


/**
 * Created by Administrator on 2016/5/22.
 */
public class MagicBeautyFilter extends GPUImageFilter{
    private int mSingleStepOffsetLocation;
    private int mParamsLocation;
    private VRBeautifyLevel mLevel = VRBeautifyLevel.LEVEL_0;

    public MagicBeautyFilter(){
        super(NO_FILTER_VERTEX_SHADER ,
                OpenGlUtils.readShaderFromRawResource(R.raw.beauty));
    }

    protected void onInit() {
        super.onInit();
        mSingleStepOffsetLocation = GLES20.glGetUniformLocation(getProgram(), "singleStepOffset");
        mParamsLocation = GLES20.glGetUniformLocation(getProgram(), "params");
        setBeautyLevel(VRConstants.BEAUTY_LEVEL);
    }

    private void setTexelSize(final float w, final float h) {
        setFloatVec2(mSingleStepOffsetLocation, new float[] {2.0f / w, 2.0f / h});
    }

    @Override
    public void onInputSizeChanged(final int width, final int height) {
        super.onInputSizeChanged(width, height);
        setTexelSize(width, height);
    }

    public void setBeautyLevel(VRBeautifyLevel level){
        mLevel = level;
        setFloat(mParamsLocation, level.getLevel());
    }

    /**
     *
     * @return
     */
    public VRBeautifyLevel getBeautyLevel() {
        return mLevel;
    }

    public void onBeautyLevelChanged(){
        setBeautyLevel(VRConstants.BEAUTY_LEVEL);
    }
}
