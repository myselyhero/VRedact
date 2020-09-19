package com.yongyong.redact;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.yongyong.vredact.view.VRCameraLayout;

public class MainActivity extends AppCompatActivity {

    private VRCameraLayout vrCameraLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vrCameraLayout = findViewById(R.id.main_camera);
    }

    @Override
    public void onBackPressed() {
        if (vrCameraLayout.isRecordFlag()){
            vrCameraLayout.setRecordFlag(false);
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        vrCameraLayout.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        vrCameraLayout.onPause();
    }
}