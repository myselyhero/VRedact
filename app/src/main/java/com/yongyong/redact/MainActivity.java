package com.yongyong.redact;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.yongyong.vredact.VRConstants;
import com.yongyong.vredact.player.VRVideoRenderingLayout;
import com.yongyong.vredact.tool.VRFileUtils;
import com.yongyong.vredact.view.VRCameraLayout;

public class MainActivity extends AppCompatActivity {

    //private VRCameraLayout vrCameraLayout;
    private VRVideoRenderingLayout renderingLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //vrCameraLayout = findViewById(R.id.main_camera);
        renderingLayout = findViewById(R.id.main_rendering);
        //renderingLayout.setVideoPath(VRConstants.DIR_CACHE + "1600433643690.mp4");
        //renderingLayout.onStart();
    }

    @Override
    public void onBackPressed() {
        /*if (vrCameraLayout.isRecordFlag()){
            vrCameraLayout.setRecordFlag(false);
            return;
        }*/
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //vrCameraLayout.onStart();
        renderingLayout.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //vrCameraLayout.onPause();
        renderingLayout.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        renderingLayout.onDestroy();
    }
}