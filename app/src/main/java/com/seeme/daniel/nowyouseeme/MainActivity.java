package com.seeme.daniel.nowyouseeme;

import android.Manifest;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.aiyaapp.aavt.av.CameraRecorder2;
import com.aiyaapp.aiya.AiyaEffects;
import com.aiyaapp.aiya.IEventListener;


public class MainActivity extends AppCompatActivity {

    private Runnable start = new Runnable() {
        @Override
        public void run() {
            setContentView(R.layout.activity_main);
            AiyaEffects.setEventListener(new IEventListener() {
                @Override
                public int onEvent(int i, int i1, String s) {
                    Log.e("wuwang", "MSG(type/ret/info):" + i + "/" + i1 + "/" + s);
                    return 0;
                }
            });
            int id = AiyaEffects.init(getApplicationContext(), "14d7fe378176e46a265c9101fa15d4a9");
            Log.e("wuwang", "id:" + id);
        }
    };


    private EffectController mEffectController;
    private DefaultEffectFlinger mFlinger;
    private View mContainer;
    private CameraRecorder2 mRecord;
    private String tempPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PermissionUtils.askPermission(this, new String[]{
                Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA, Manifest.permission.INTERNET, Manifest.permission.RECORD_AUDIO
        }, 10, start);
        mRecord = new CameraRecorder2();
        mRecord.setOutputPath(tempPath);
        initViews();
    }

    private void initViews() {
        mContainer = findViewById(R.id.mEffectView);
        SurfaceView surface = (SurfaceView) findViewById(R.id.mSurface);
        surface.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                mFlinger = new DefaultEffectFlinger(getApplicationContext());
                mRecord.setRenderer(mFlinger);
                mEffectController = new EffectController(MainActivity.this, mContainer, mFlinger);
                mEffectController.show();

                mRecord.open();
                mRecord.setSurface(holder.getSurface());
                mRecord.setPreviewSize(width, height);
                mRecord.startPreview();

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                mRecord.stopPreview();
                mRecord.close();
                mFlinger.release();

            }
        });

    }
}
