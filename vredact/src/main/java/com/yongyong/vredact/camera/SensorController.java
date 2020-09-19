package com.yongyong.vredact.camera;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.yongyong.vredact.VRKit;

import java.util.Calendar;

/**
 * @author yongyong
 *
 * desc:加速控制器，用于控制对焦
 *
 * @// TODO: 2020/9/18
 */
public class SensorController implements SensorEventListener {

    public static final String TAG = SensorController.class.getSimpleName();

    private SensorManager mSensorManager;
    private Sensor mSensor;

    private int mX, mY, mZ;
    private long lastStaticStamp = 0;
    Calendar mCalendar;

    boolean isFocusing = false;
    boolean canFocusIn = false;  //内部是否能够对焦控制机制
    boolean canFocus = false;

    public static final int DELEY_DURATION = 500;

    public static final int STATUS_NONE = 0;
    public static final int STATUS_STATIC = 1;
    public static final int STATUS_MOVE = 2;
    private int STATUE = STATUS_NONE;

    private CameraFocusListener mCameraFocusListener;

    private static SensorController mInstance;

    /**
     * 1 表示没有被锁定、0表示被锁定
     */
    private int foucsing = 1;

    private SensorController() {
        mSensorManager = (SensorManager) VRKit.getAppContext().getSystemService(Activity.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);// TYPE_GRAVITY
    }

    /**
     *
     * @return
     */
    public static SensorController getInstance() {
        if (mInstance == null) {
            mInstance = new SensorController();
        }
        return mInstance;
    }

    /**
     *
     * @param mCameraFocusListener
     */
    public void setCameraFocusListener(CameraFocusListener mCameraFocusListener) {
        this.mCameraFocusListener = mCameraFocusListener;
    }

    /**
     *
     */
    public void onStart() {
        restParams();
        canFocus = true;
        mSensorManager.registerListener(this, mSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     *
     */
    public void onStop() {
        mSensorManager.unregisterListener(this, mSensor);
        canFocus = false;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }



    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == null) {
            return;
        }

        if (isFocusing) {
            restParams();
            return;
        }

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            int x = (int) event.values[0];
            int y = (int) event.values[1];
            int z = (int) event.values[2];
            mCalendar = Calendar.getInstance();
            long stamp = mCalendar.getTimeInMillis();// 1393844912

            int second = mCalendar.get(Calendar.SECOND);// 53

            if (STATUE != STATUS_NONE) {
                int px = Math.abs(mX - x);
                int py = Math.abs(mY - y);
                int pz = Math.abs(mZ - z);
                double value = Math.sqrt(px * px + py * py + pz * pz);
                if (value > 1.4) {
                    STATUE = STATUS_MOVE;
                } else {
                    if (STATUE == STATUS_MOVE) {
                        lastStaticStamp = stamp;
                        canFocusIn = true;
                    }

                    if (canFocusIn) {
                        if (stamp - lastStaticStamp > DELEY_DURATION) {
                            //移动后静止一段时间，可以发生对焦行为
                            if (!isFocusing) {
                                canFocusIn = false;
                                if (mCameraFocusListener != null) {
                                    mCameraFocusListener.onFocus();
                                }
                            }
                        }
                    }

                    STATUE = STATUS_STATIC;
                }
            } else {
                lastStaticStamp = stamp;
                STATUE = STATUS_STATIC;
            }

            mX = x;
            mY = y;
            mZ = z;
        }
    }

    /**
     *
     */
    private void restParams() {
        STATUE = STATUS_NONE;
        canFocusIn = false;
        mX = 0;
        mY = 0;
        mZ = 0;
    }

    /**
     * 对焦是否被锁定
     *
     * @return
     */
    public boolean isFocusLocked() {
        if(canFocus) {
            return foucsing <= 0;
        }
        return false;
    }

    /**
     * 锁定对焦
     */
    public void lockFocus() {
        isFocusing = true;
        foucsing--;
    }

    /**
     * 解锁对焦
     */
    public void unlockFocus() {
        isFocusing = false;
        foucsing++;
    }

    /**
     *
     */
    public void restFoucs() {
        foucsing = 1;
    }

    /**
     *
     */
    public interface CameraFocusListener {
        void onFocus();
    }
}
