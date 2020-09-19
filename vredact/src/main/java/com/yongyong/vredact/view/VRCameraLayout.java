package com.yongyong.vredact.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yongyong.vredact.R;
import com.yongyong.vredact.VRBeautifyLevel;
import com.yongyong.vredact.camera.SensorController;
import com.yongyong.vredact.camera.VRCameraView;
import com.yongyong.vredact.filter.MagicFilterType;
import com.yongyong.vredact.tool.VRFileUtils;
import com.yongyong.vredact.tool.VRScreenTool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author yongyong
 *
 * desc:摄像头类
 * 
 * @// TODO: 2020/9/18
 */
public class VRCameraLayout extends FrameLayout {

    private static final String TAG = VRCameraView.class.getSimpleName();

    private Activity mActivity;

    /**
     *
     */
    private VRCameraView mCameraView;
    private LinearLayout mGoBack,mSwitchCamera;
    private ImageView mCircularProgressView;
    private FocusImageView mFocusImageView;
    private TextView textView;

    /**最长录制时长*/
    private static final int MAX_TIME = 15000;
    /**是否暂停*/
    private boolean pausing;
    /**是否正在录制*/
    private boolean recordFlag;
    /** 进度条刷新的时间 */
    private long timeStep = 50;
    /** 用于记录录制时间 */
    long timeCount = 0;
    /**  */
    private boolean autoPausing;

    private MagicFilterType mCurrentFilterType = MagicFilterType.NONE;

    /**  */
    ExecutorService mExecutorService;
    private SensorController mSensorController = SensorController.getInstance();

    public VRCameraLayout(@NonNull Context context) {
        super(context);
        initView();
    }

    public VRCameraLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public VRCameraLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView(){
        LayoutInflater.from(getContext()).inflate(R.layout.vr_camera_layout,this);
        mCameraView = findViewById(R.id.vr_camera_layout_camera);
        mGoBack = findViewById(R.id.vr_camera_layout_back);
        mSwitchCamera = findViewById(R.id.vr_camera_layout_switch);
        mCircularProgressView = findViewById(R.id.vr_camera_layout_button);
        mFocusImageView = findViewById(R.id.vr_camera_layout_foucs);
        textView = findViewById(R.id.vr_camera_layout_hint);

        mActivity = (Activity) getContext();

        mGoBack.setOnClickListener(v -> {
            if (mActivity != null)
                mActivity.finish();
        });
        mSwitchCamera.setOnClickListener(v -> {
            mCameraView.switchCamera();
            if (mCameraView.getCameraId() == 1) {
                //前置摄像头 使用美颜
                mCameraView.changeBeautyLevel(VRBeautifyLevel.LEVEL_3);
            } else {
                //后置摄像头不使用美颜
                mCameraView.changeBeautyLevel(VRBeautifyLevel.LEVEL_0);
            }
        });
        mCircularProgressView.setOnClickListener(v -> {
            if (!recordFlag) {
                mExecutorService.execute(recordRunnable);
            } else if (!pausing) {
                mCameraView.pause(false);
                pausing = true;
            } else {
                mCameraView.resume(false);
                pausing = false;
            }
        });

        /**  */
        mCameraView.setOnTouchListener((v, event) -> {
            mCameraView.onTouch(event);
            if (mCameraView.getCameraId() == 1) {
                return false;
            }
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    float sRawX = event.getRawX();
                    float sRawY = event.getRawY();
                    float rawY = sRawY * VRScreenTool.getScreenWidth() / VRScreenTool.getScreenWidth();
                    float temp = sRawX;
                    float rawX = rawY;
                    rawY = (VRScreenTool.getScreenWidth() - temp) * VRScreenTool.getScreenHeight() / VRScreenTool.getScreenWidth();

                    Point point = new Point((int) rawX, (int) rawY);
                    mCameraView.onFocus(point, mFocusCallBack);
                    mFocusImageView.startFocus(new Point((int) sRawX, (int) sRawY));
            }
            return true;
        });
        mCameraView.setOnFilterChangeListener(type -> {
            mCurrentFilterType = type;
            post(new Runnable() {
                @Override
                public void run() {
                    textView.setText(type+"");
                    textView.setVisibility(View.VISIBLE);
                    textView.animate()
                            .alpha(1f)
                            .setDuration(500)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    textView.setVisibility(View.GONE);
                                }
                            });
                }
            });
        });

        /**  */
        mExecutorService = Executors.newSingleThreadExecutor();
        mSensorController.setCameraFocusListener(new SensorController.CameraFocusListener() {
            @Override
            public void onFocus() {
                if (mCameraView.getCameraId() == 1) {
                    return;
                }
                Point point = new Point(VRScreenTool.getScreenWidth() / 2, VRScreenTool.getScreenHeight() / 2);
                mCameraView.onFocus(point, mFocusCallBack);
            }
        });
    }

    /** 外部API */

    /**
     *
     * @return 真正使用的滤镜
     */
    public MagicFilterType getCurrentFilterType(){
        return mCurrentFilterType;
    }

    /**
     * 是否正在录制
     * @return
     */
    public boolean isRecordFlag(){
        return recordFlag;
    }

    /**
     *
     * @param flag
     */
    public void setRecordFlag(boolean flag) {
        recordFlag = flag;
    }

    /**
     *
     */
    public void onStart(){
        mCameraView.onResume();
        if (recordFlag && autoPausing) {
            mCameraView.resume(true);
            autoPausing = false;
        }
    }

    /**
     *
     */
    public void onPause(){
        if (recordFlag && !pausing) {
            mCameraView.pause(true);
            autoPausing = true;
        }
        mCameraView.onPause();
    }

    /** 内部API */

    /**
     * 聚焦回调
     */
    Camera.AutoFocusCallback mFocusCallBack = (success, camera) -> {
        /** 聚焦之后根据结果修改图片 */
        if (success) {
            mFocusImageView.onFocusSuccess();
        } else {
            mFocusImageView.onFocusFailed();
        }
    };

    /**
     * 录制线程
     */
    Runnable recordRunnable = new Runnable() {
        @Override
        public void run() {
            recordFlag = true;
            pausing = false;
            autoPausing = false;
            timeCount = 0;
            String savePath = VRFileUtils.getPathOfVideo();
            try {
                mCameraView.setSavePath(savePath);
                mCameraView.startRecord();
                while (timeCount <= MAX_TIME && recordFlag) {
                    if (pausing || autoPausing) {
                        continue;
                    }
                    Log.e(TAG, "run: "+(int) timeCount);
                    Thread.sleep(timeStep);
                    timeCount += timeStep;
                }
                recordFlag = false;
                mCameraView.stopRecord();
                if (timeCount < 3000) {
                    post(new Runnable() {
                        @Override
                        public void run() {
                            Log.e(TAG, "run: 录制时间过短!");
                        }
                    });
                } else {
                    post(new Runnable() {
                        @Override
                        public void run() {
                            Log.e(TAG, "run: 保存路径"+savePath);
                        }
                    });
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}
