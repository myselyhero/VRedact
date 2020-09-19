package com.yongyong.vredact.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;

/**
 * @author 
 * 
 * desc:录制按钮
 * 
 * @// TODO: 2020/8/13
 */
public class VRecordButton extends View {

    private static final String TAG = "CaptureButton";

    /**
     *
     */
    public static final int BUTTON_STATE_ONLY_CAPTURE = 101;      //只能拍照
    public static final int BUTTON_STATE_ONLY_RECORDER = 102;     //只能录像
    public static final int BUTTON_STATE_BOTH = 103;              //两者都可以

    private int state;              //当前按钮状态
    private int button_state;       //按钮可执行的功能状态（拍照,录制,两者）

    public static final int STATE_IDLE = 1;        //空闲状态
    public static final int STATE_PRESS = 2;       //按下状态
    public static final int STATE_LONG_PRESS = 3;  //长按状态
    public static final int STATE_RECORDERING = 4; //录制状态
    public static final int STATE_BAN = 5;         //禁止状态

    private int progress_color = 0xEE16AE16;            //进度条颜色
    private int outside_color = 0xFFFFFFFF;             //外圆背景色
    private int inside_color = 0xFFF04444;              //内圆背景色


    /**
     * Touch_Event_Down时候记录的Y值
     * 用于缩放
     */
    private float event_Y;

    private Paint mPaint;

    private float strokeWidth;          //进度条宽度
    private int outside_add_size;       //长按外圆半径变大的Size
    private int inside_reduce_size;     //长安内圆缩小的Size

    /**
     * 中心坐标
     */
    private float center_X;
    private float center_Y;

    private float button_radius;            //按钮半径
    private float button_outside_radius;    //外圆半径
    private float button_inside_radius;     //内圆半径
    private int button_size;                //按钮大小

    private float progress;         //录制视频的进度
    private int duration = 15;           //录制视频最大时间长度
    private int min_duration;       //最短录制时间限制
    private int recorded_time;      //记录当前录制的时间

    private RectF rectF;

    private LongPressRunnable longPressRunnable;    //长按后处理的逻辑Runnable
    private RecordCountDownTimer timer;             //计时器

    /**
     * 回调
     */
    private OnRecordListener onRecordListener;

    public VRecordButton(Context context) {
        super(context);
        init();
    }

    public VRecordButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VRecordButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     *
     */
    private void init(){

        WindowManager manager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int layout_width;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            layout_width = outMetrics.widthPixels;
        } else {
            layout_width = outMetrics.widthPixels / 2;
        }

        int size = (int) (layout_width / 5f);

        this.button_size = size;
        button_radius = size / 3.0f;

        button_outside_radius = button_radius;
        button_inside_radius = button_radius * 0.75f;

        strokeWidth = size / 15;
        outside_add_size = size / 6;
        inside_reduce_size = size / 9;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        progress = 0;
        longPressRunnable = new LongPressRunnable();

        state = STATE_IDLE;                //初始化为空闲状态
        button_state = BUTTON_STATE_BOTH;  //初始化按钮为可录制可拍照
        duration = duration * 1000;              //默认最长录制时间为10s
        min_duration = 3000;              //默认最短录制时间为1.5s

        center_X = (button_size + outside_add_size * 2) / 2;
        center_Y = (button_size + outside_add_size * 2) / 2;

        rectF = new RectF(
                center_X - (button_radius + outside_add_size - strokeWidth / 2),
                center_Y - (button_radius + outside_add_size - strokeWidth / 2),
                center_X + (button_radius + outside_add_size - strokeWidth / 2),
                center_Y + (button_radius + outside_add_size - strokeWidth / 2));

        timer = new RecordCountDownTimer(duration, duration / 360);    //录制定时器
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(button_size + outside_add_size * 2, button_size + outside_add_size * 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setStyle(Paint.Style.FILL);

        mPaint.setColor(outside_color); //外圆（半透明灰色）
        canvas.drawCircle(center_X, center_Y, button_outside_radius, mPaint);

        mPaint.setColor(inside_color);  //内圆（白色）
        canvas.drawCircle(center_X, center_Y, button_inside_radius, mPaint);

        //如果状态为录制状态，则绘制录制进度条
        if (state == STATE_RECORDERING) {
            mPaint.setColor(progress_color);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(strokeWidth);
            canvas.drawArc(rectF, -90, progress, false, mPaint);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (event.getPointerCount() > 1)
                    break;
                handlerDown(event.getY());
            case MotionEvent.ACTION_MOVE:
                if (onRecordListener != null
                        && state == STATE_RECORDERING
                        && (button_state == BUTTON_STATE_ONLY_RECORDER || button_state == BUTTON_STATE_BOTH)) {
                    //记录当前Y值与按下时候Y值的差值，调用缩放回调接口
                    onRecordListener.recordZoom(event_Y - event.getY());
                }
                break;
            case MotionEvent.ACTION_UP:
                //根据当前按钮的状态进行相应的处理
                handlerUnpressByState();
                break;
        }
        return true;
    }

    /**
     *
     * @param y
     */
    public void handlerDown(float y){
        if (state != STATE_IDLE)
            return;
        event_Y = y;     //记录Y值
        state = STATE_PRESS;        //修改当前状态为点击按下
        //判断按钮状态是否为可录制状态
        if ((button_state == BUTTON_STATE_ONLY_RECORDER || button_state == BUTTON_STATE_BOTH))
            postDelayed(longPressRunnable, 500);    //同时延长500启动长按后处理的逻辑Runnable
    }

    /**
     * 当手指松开按钮时候处理的逻辑
     */
    public void handlerUnpressByState() {
        removeCallbacks(longPressRunnable); //移除长按逻辑的Runnable
        //根据当前状态处理
        switch (state) {
            //当前是点击按下
            case STATE_PRESS:
                if ((onRecordListener != null || button_state == BUTTON_STATE_ONLY_CAPTURE || button_state == BUTTON_STATE_BOTH)) {
                    startCaptureAnimation(button_inside_radius);
                } else {
                    state = STATE_IDLE;
                }
                break;
            //当前是长按状态
            case STATE_RECORDERING:
                timer.cancel(); //停止计时器
                recordEnd();    //录制结束
                break;
        }
    }

    /**
     * 录制结束
     */
    private void recordEnd() {
        if (onRecordListener != null) {
            if (recorded_time < min_duration)
                onRecordListener.recordShort(recorded_time);//回调录制时间过短
            else
                onRecordListener.recordEnd(recorded_time);  //回调录制结束
        }
        resetRecordAnim();  //重制按钮状态
    }

    /**
     * 重制状态
     */
    private void resetRecordAnim() {
        state = STATE_BAN;
        progress = 0;       //重制进度
        invalidate();
        //还原按钮初始状态动画
        startRecordAnimation(
                button_outside_radius,
                button_radius,
                button_inside_radius,
                button_radius * 0.75f
        );
    }

    /**
     * 内圆动画
     * @param inside_start
     */
    private void startCaptureAnimation(float inside_start) {
        ValueAnimator inside_anim = ValueAnimator.ofFloat(inside_start, inside_start * 0.75f, inside_start);
        inside_anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                button_inside_radius = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        inside_anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //回调拍照接口
                if (onRecordListener != null)
                    onRecordListener.takePictures();
                state = STATE_BAN;
            }
        });
        inside_anim.setDuration(100);
        inside_anim.start();
    }

    //内外圆动画
    private void startRecordAnimation(float outside_start, float outside_end, float inside_start, float inside_end) {
        ValueAnimator outside_anim = ValueAnimator.ofFloat(outside_start, outside_end);
        ValueAnimator inside_anim = ValueAnimator.ofFloat(inside_start, inside_end);
        //外圆动画监听
        outside_anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                button_outside_radius = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        //内圆动画监听
        inside_anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                button_inside_radius = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        AnimatorSet set = new AnimatorSet();
        //当动画结束后启动录像Runnable并且回调录像开始接口
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //设置为录制状态
                if (state == STATE_LONG_PRESS) {
                    if (onRecordListener != null)
                        onRecordListener.recordStart();
                    state = STATE_RECORDERING;
                    timer.start();
                }
            }
        });
        set.playTogether(outside_anim, inside_anim);
        set.setDuration(100);
        set.start();
    }


    /**
     * 更新进度条
     * @param millisUntilFinished
     */
    private void updateProgress(long millisUntilFinished) {
        recorded_time = (int) (duration - millisUntilFinished);
        progress = 360f - millisUntilFinished / (float) duration * 360f;
        invalidate();
    }

    /**
     * 录制视频计时器
     */
    private class RecordCountDownTimer extends CountDownTimer {
        RecordCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            updateProgress(millisUntilFinished);
        }

        @Override
        public void onFinish() {
            updateProgress(0);
            recordEnd();
        }
    }

    /**
     * 长按线程
     */
    private class LongPressRunnable implements Runnable {
        @Override
        public void run() {
            state = STATE_LONG_PRESS;   //如果按下后经过500毫秒则会修改当前状态为长按状态
            //没有录制权限
            /*if (CheckPermission.getRecordState() != CheckPermission.STATE_SUCCESS) {
                state = STATE_IDLE;
                if (captureLisenter != null) {
                    captureLisenter.recordError();
                    return;
                }
            }*/
            //启动按钮动画，外圆变大，内圆缩小
            startRecordAnimation(
                    button_outside_radius,
                    button_outside_radius + outside_add_size,
                    button_inside_radius,
                    button_inside_radius - inside_reduce_size
            );
        }
    }

    /* 对外api */

    /**
     *
     * @param onRecordListener
     */
    public void setOnRecordListener(OnRecordListener onRecordListener) {
        this.onRecordListener = onRecordListener;
    }

    /**
     * 设置最长录制时间
     * @param duration
     */
    public void setDuration(int duration) {
        this.duration = duration;
        /**
         * 录制定时器
         */
        timer = new RecordCountDownTimer(duration, duration / 360);
    }

    /**
     * 设置最短录制时间
     * @param duration
     */
    public void setMinDuration(int duration) {
        this.min_duration = duration;
    }

    /**
     * 设置按钮功能（拍照和录像）
     * @param state
     */
    public void setButtonFeatures(int state) {
        this.button_state = state;
    }

    /**
     * 是否空闲状态
     * @return
     */
    public boolean isIdle() {
        return state == STATE_IDLE;
    }

    /**
     * 设置状态
     */
    public void resetState() {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                state = STATE_IDLE;
            }
        },1000);
    }

    /**
     * 录制按钮的回调
     */
    public interface OnRecordListener {

        /**
         * 拍照
         */
        void takePictures();

        /**
         * 录制时间短
         * @param time
         */
        void recordShort(long time);

        /**
         * 开始录制
         */
        void recordStart();

        /**
         * 录制结束
         * @param time
         */
        void recordEnd(long time);

        /**
         * 缩放摄像头
         * @param zoom
         */
        void recordZoom(float zoom);

        /**
         * 录制错误、权限
         */
        void recordError();
    }
}
