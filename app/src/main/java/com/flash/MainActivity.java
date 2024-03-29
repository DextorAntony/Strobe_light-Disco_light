package com.flash;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    SeekBar sb;
    Switch s;
    int blinkInterval;
    private boolean isOn=false;
    boolean btnenabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sb=findViewById(R.id.seekBar);
        s=findViewById(R.id.switch1);
        sb.setMax(10);


        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA},10);
                }
                else if(s.isChecked()){
                    startblink();
                }
            }
        });

    }
    private void startblink(){
        Timer t = new Timer();
        if(!s.isChecked()){
            t.cancel();
            flashlightOff();
            isOn=false;
            return;
        }
        blinkInterval=1000/(sb.getProgress()==0?1:sb.getProgress());

        t.schedule(new TimerTask() {
            @Override
            public void run() {
                if(isOn){
                    flashlightOff();
                    isOn=false;
                }
                else{
                    flashlightOn();
                    isOn=true;
                }
                startblink();
            }
        },blinkInterval);

    }
    private void flashlightOff(){
        CameraManager cm =(CameraManager)getSystemService(Context.CAMERA_SERVICE);
        try{
            String cameraId=cm.getCameraIdList()[0];
            cm.setTorchMode(cameraId,false);
        }
        catch(Exception e){

        }
    }
    private void flashlightOn(){
        CameraManager cm =(CameraManager)getSystemService(Context.CAMERA_SERVICE);
        try{
            String cameraId=cm.getCameraIdList()[0];
            cm.setTorchMode(cameraId,true);
        }
        catch(Exception e){

        }
    }
}
