package com.yongyong.redact;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import com.yongyong.vredact.VRConstants;
import com.yongyong.vredact.player.VRVideoRenderingLayout;
import com.yongyong.vredact.tool.VRFileUtils;
import com.yongyong.vredact.view.VRCameraLayout;

public class MainActivity extends AppCompatActivity {

    private VRVideoRenderingLayout renderingLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        renderingLayout = findViewById(R.id.main_rendering);

        if (!checkPermission()){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},101);
        }
    }

    private boolean checkPermission() {
        boolean flag = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)) {
            flag = false;
        }
        return flag;
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