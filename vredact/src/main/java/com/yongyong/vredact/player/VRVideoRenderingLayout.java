package com.yongyong.vredact.player;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yongyong.vredact.R;
import com.yongyong.vredact.clipper.RenderingVideoClipper;
import com.yongyong.vredact.entity.VideoInfo;
import com.yongyong.vredact.filter.MagicFilterType;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author yongyong
 *
 * desc: 本地视频渲染
 * 
 * @// TODO: 2020/9/18
 */
public class VRVideoRenderingLayout extends FrameLayout {

    private static final String TAG = VRVideoRenderingLayout.class.getSimpleName();

    private Activity mActivity;

    private VRVideoPlayerView mPlayerView;
    private LinearLayout mGoBack,mBeautify;

    private String mVideoPath;
    private boolean resumed;
    private boolean isPlaying;
    private static final int VIDEO_PREPARE = 0;
    private static final int VIDEO_START = 1;
    private static final int VIDEO_PAUSE = 2;
    private static final int VIDEO_CUT_FINISH = 3;

    /** 当前正在使用的滤镜 */
    private MagicFilterType mCurrentFilterType = MagicFilterType.NONE;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case VIDEO_PREPARE:
                    Log.e(TAG, "handleMessage: 准备好了");
                    break;
                case VIDEO_START:
                    isPlaying = true;
                    break;
                case VIDEO_PAUSE:
                    isPlaying = false;
                    break;
                case VIDEO_CUT_FINISH:
                    //TODO　已经渲染结束了　
                    Log.e(TAG, "handleMessage: 渲染结束");
                    break;
            }
        }
    };

    public VRVideoRenderingLayout(@NonNull Context context) {
        super(context);
        initView();
    }

    public VRVideoRenderingLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public VRVideoRenderingLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    /**
     *
     */
    private void initView(){
        LayoutInflater.from(getContext()).inflate(R.layout.vr_video_rendering_layout,this);
        mPlayerView = findViewById(R.id.vr_video_rendering_player);
        mGoBack = findViewById(R.id.vr_video_rendering_back);
        mBeautify = findViewById(R.id.vr_video_rendering_beautify);
        mActivity = (Activity) getContext();

        mGoBack.setOnClickListener(v -> {
            mActivity.finish();
        });
        mBeautify.setOnClickListener(v -> {
            mPlayerView.switchBeauty();
            if (mBeautify.isSelected()){
                mBeautify.setSelected(false);
            }else {
                mBeautify.setSelected(true);
            }
        });
        mPlayerView.setOnFilterChangeListener(type -> {
            mCurrentFilterType = type;
        });
        mPlayerView.setOnTouchListener((v, event) -> {
            mPlayerView.onTouch(event);
            return true;
        });

        mPlayerView.pause();

        /*VideoClipper clipper = new VideoClipper();
        if (mBeautify.isSelected()){
            clipper.showBeauty();
        }
        clipper.setInputVideoPath(mVideoPath);
        String outputPath = VRFileUtils.getPathOfVideo();
        clipper.setFilterType(mCurrentFilterType);
        clipper.setOutputVideoPath(outputPath);
        clipper.setOnVideoCutFinishListener(new VideoClipper.OnVideoCutFinishListener() {
            @Override
            public void onFinish() {
                mHandler.sendEmptyMessage(VIDEO_CUT_FINISH);
            }
        });
        try {
            clipper.clipVideo(0,mPlayerView.getDuration()*1000);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        RenderingVideoClipper clipper = new RenderingVideoClipper();
        clipper.setInputVideoPath(mVideoPath);
        final String outputPath = System.currentTimeMillis() + ".mp4";
        clipper.setFilterType(mCurrentFilterType);
        clipper.setOutputVideoPath(outputPath);
        clipper.setOnVideoCutFinishListener(new RenderingVideoClipper.OnVideoCutFinishListener() {
            @Override
            public void onFinish() {
                mHandler.sendEmptyMessage(VIDEO_CUT_FINISH);
            }

            @Override
            public void onProgress(float percent) {

            }
        });
        try {
            int clipDur = mPlayerView.getDuration() * 1000;
            clipper.clipVideo(0, clipDur, new ArrayList<>(), getResources());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** 外部API */

    /**
     *
     */
    public void onStart(){
        if (resumed) {
            mPlayerView.start();
        }
        resumed = true;
    }

    /**
     *
     */
    public void onPause(){
        mPlayerView.pause();
    }

    /**
     *
     */
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        mPlayerView.onDestroy();
    }

    /**
     *
     * @param path
     */
    public void setVideoPath(String path) {
        mVideoPath = path;
        ArrayList<String> srcList = new ArrayList<>();
        srcList.add(path);
        mPlayerView.setVideoPath(srcList);
        mPlayerView.setIMediaCallback(new MediaPlayerWrapper.IMediaCallback() {
            @Override
            public void onVideoPrepare() {
                mHandler.sendEmptyMessage(VIDEO_PREPARE);
            }

            @Override
            public void onVideoStart() {
                mHandler.sendEmptyMessage(VIDEO_START);
            }

            @Override
            public void onVideoPause() {
                mHandler.sendEmptyMessage(VIDEO_PAUSE);
            }

            @Override
            public void onCompletion(MediaPlayer mp) {
                mHandler.sendEmptyMessage(VIDEO_PAUSE);
            }

            @Override
            public void onVideoChanged(VideoInfo info) {
                //mPlayerView.seekTo(startPoint);
                //mPlayerView.start();
            }
        });
    }

    /**
     *
     * @return
     */
    public long getCurrentPosition(){
        return mPlayerView.getCurrentPosition();
    }

    /**
     *
     * @return
     */
    public long getDuration(){
        return mPlayerView.getDuration();
    }
}
