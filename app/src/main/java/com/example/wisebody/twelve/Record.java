package com.example.wisebody.twelve;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.wisebody.twelve.widget.ProgressCircle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Record extends Activity implements SurfaceHolder.Callback {

    private static String EXTERNAL_STORAGE_PATH = "";
    private static String RECORDED_FILE = "";
    private static long fileIndex = 0;
    private static String filename = "";
    private static String id ="";
    Boolean starJoin = false;

    MediaRecorder recorder;
    ProgressCircle progress;
    Button btn_progress;
    String uploadfile;
    Boolean recording = false;
    Boolean meteringAreaSupported;
    private TimerTask second;
    int timer_sec;
    User loginUser;
    String video;
    Button frontback;
    Button touchMode;
    LinearLayout option;
    Boolean touchBool = false;
    ArrayList<String> dividedfile;
    ArrayList<String> dividedpath;
    int fileNum = 0;


    // 카메라 상태를 저장하고 있는 객체
    private Camera cam;

    SurfaceView surfaceView;
    SurfaceHolder holder;
    ImageView text;
    private int mCameraFacing;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        timer_sec = 0;
        btn_progress = (Button) findViewById(R.id.progressBtn);
        frontback = (Button) findViewById(R.id.frontback);
        touchMode  = (Button) findViewById(R.id.touchmode);
        touchMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(touchBool) {
                    touchBool = false;
                    touchMode.setText("터치 모드");
                    if(dividedfile != null)
                    dividedfile = null;
                    if(dividedpath != null)
                        dividedpath = null;
                }
                else {
                    touchBool = true;
                    touchMode.setText("일반 촬영 모드");
                    dividedfile = new ArrayList<String>();
                    dividedpath = new ArrayList<String>();
                }
            }
        });
        mCameraFacing = Camera.CameraInfo.CAMERA_FACING_BACK;
        option = (LinearLayout) findViewById(R.id.option);
        frontback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraFacing = (mCameraFacing==Camera.CameraInfo.CAMERA_FACING_BACK) ?
                        Camera.CameraInfo.CAMERA_FACING_FRONT
                        : Camera.CameraInfo.CAMERA_FACING_BACK;
                btn_progress.setVisibility(View.INVISIBLE);
                setting();
                btn_progress.setVisibility(View.VISIBLE);
                Log.d("face",Integer.toString(mCameraFacing));
            }
        });
        btn_progress.setText("12s");

        String state = Environment.getExternalStorageState();
        progress = (ProgressCircle) findViewById(R.id.circularProgressbar);
        // Environment.MEDIA_MOUNTED 외장메모리가 마운트 flog
        if (!state.equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(getApplicationContext(), "외장 메모리가 마운트 되지않았습니다.", Toast.LENGTH_LONG).show();
        } else {
            EXTERNAL_STORAGE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
            Toast.makeText(getApplicationContext(), "메모리있다", Toast.LENGTH_LONG).show();
        }
        recorder = new MediaRecorder();
        Intent getInfor = getIntent();
        starJoin = (Boolean) getInfor.getBooleanExtra("star",false);
        loginUser = (User) getInfor.getSerializableExtra("loginUser");
        surfaceView = (SurfaceView)findViewById(R.id.surface);
        holder = surfaceView.getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        btn_progress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!touchBool) {
                    if (recording) {
                        recorder.stop();
                        recorder.release();
                        recorder = null;
                        btn_progress.setVisibility(View.INVISIBLE);
                        setting();
                        Log.d("recorder","stop");
                        second.cancel();
                        recording = false;
                        timer_sec = 0;
                        option.setVisibility(View.VISIBLE);
                        new Thread(new Runnable() {
                            @Override
                                        public void run() {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    btn_progress.setText("12s");
                                       progress.startAnimation();
                                    }
                                });
                            }
                        }).start();
                        btn_progress.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(Record.this, "succeed", Toast.LENGTH_LONG).show();
                        option.setVisibility(View.INVISIBLE);
                        settingRec();
                    }
                }
            }
        });
        btn_progress.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(touchBool) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        Toast.makeText(Record.this, "succeed", Toast.LENGTH_LONG).show();
                        option.setVisibility(View.INVISIBLE);
                        settingRec();

                    } else if (event.getAction() == MotionEvent.ACTION_UP)
                    {
                        if (recording&&timer_sec<120) {
                            recorder.stop();
                            recorder.release();
                            recorder = null;
                            btn_progress.setVisibility(View.INVISIBLE);
                            setting();
                            second.cancel();
                            recording = false;
                            option.setVisibility(View.VISIBLE);
                            savefile();
                            btn_progress.setVisibility(View.VISIBLE);
                        }
                    }
                }
                return false;
            }
        });
        //setting();
        Log.d("주기","생성");
    }
    @Override
    protected void onRestart()
    {
        super.onRestart();
        option.setVisibility(View.VISIBLE);
        btn_progress.setText("12s");
        progress.setProgress(0f);
        progress.startAnimation();

    }
    private void setting(){
        if(cam!=null)
        {
            cam.lock();
            cam.release();
            cam = null;
        }
        cam = Camera.open(mCameraFacing);
        Camera.Parameters parameters = cam.getParameters();
        List<String> supFlash = cam.getParameters().getSupportedFlashModes();
        for(int i = 0; i<supFlash.size();i++)
            Log.d("flash",supFlash.get(i));
        List<String> supportedFocusModes = cam.getParameters().getSupportedFocusModes();
        if (supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE))
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        if(supFlash.contains(Camera.Parameters.FLASH_MODE_TORCH)) {
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            Log.d("flash","on!");
        }
        cam.setParameters(parameters);
        cam.setDisplayOrientation(90);
        try {
            cam.setPreviewDisplay(holder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        cam.startPreview();
        cam.unlock();
        Log.d("녹화","세팅");
    }
   private void settingRec()
   {
       try {
           recorder = new MediaRecorder();
           recorder.setCamera(cam);
           recorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
           recorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
           CamcorderProfile profile = CamcorderProfile.get(CamcorderProfile.QUALITY_480P);
           recorder.setProfile(profile);
           if (mCameraFacing != Camera.CameraInfo.CAMERA_FACING_FRONT)
               recorder.setOrientationHint(90);
           else
               recorder.setOrientationHint(270);
           recorder.setMaxDuration(12000);
           filename = createFilename();
           recorder.setOutputFile(filename);
           recorder.setPreviewDisplay(holder.getSurface());
           recorder.prepare();
           recorder.start();
           recording = true;
           testStart();
           Toast.makeText(Record.this, "start", Toast.LENGTH_LONG).show();
       } catch (final Exception ex) {
           ex.printStackTrace();
           recorder.release();
           recorder = null;
       }
   }

    private String createFilename() {
        fileIndex= System.currentTimeMillis() / 1000;

        id = loginUser.id.substring(0,loginUser.id.indexOf("@"));

        String newFilename = "";
        if (EXTERNAL_STORAGE_PATH == null || EXTERNAL_STORAGE_PATH.equals("")) {
            // 내장 메모리를 사용합니다.
            if(touchBool)
            {
                fileNum++;
                newFilename = fileIndex + id +"-"+fileNum+".mp4";
            }
            else
                newFilename = fileIndex + id +".mp4";

        } else {
            // 외장 메모리를 사용합니다.
            if(touchBool)
            {
                fileNum++;
                newFilename = EXTERNAL_STORAGE_PATH + "/"+fileIndex + id +"-"+fileNum+".mp4";
                dividedpath.add(EXTERNAL_STORAGE_PATH + "/"+fileIndex + id +"-"+fileNum+".mp4");
                dividedfile.add(fileIndex + id +"-"+fileNum+".mp4");
            }
            else
                newFilename = EXTERNAL_STORAGE_PATH + "/"+fileIndex+id+".mp4";
            Log.d("id",id);
            video=fileIndex+id+".mp4";
            Log.d("filenmaen",newFilename);
        }

        return newFilename;
    }
    private void savefile()
    {
        ContentValues values = new ContentValues(10);
        values.put(MediaStore.MediaColumns.TITLE, "RecordedVideo");
        values.put(MediaStore.Audio.Media.ALBUM, "Video Album");
        values.put(MediaStore.Audio.Media.ARTIST, "twelve");
        values.put(MediaStore.Audio.Media.DISPLAY_NAME, "Recorded Video");
        values.put(MediaStore.MediaColumns.DATE_ADDED, System.currentTimeMillis() / 1000);
        values.put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4");
        values.put(MediaStore.Audio.Media.DATA, filename);
        Uri videoUri = getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
        if (videoUri == null) {
            Log.d("SampleVideoRecorder", "Video insert failed.");
            return;
        }
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, videoUri));
        uploadfile = filename;
        Log.d("filename",filename);
        Log.d("유알아이",videoUri.toString());
        Log.d("id",loginUser.id);
        if(!starJoin&&!touchBool) {
            Intent intent = new Intent(Record.this, WritePost.class);
            intent.putExtra("path", filename);
            intent.putExtra("loginUser", loginUser);
            intent.putExtra("filename", video);
            startActivityForResult(intent, 0);
            second.cancel();
            Log.d("touch","no");
        }
        else if(starJoin&&!touchBool)
        {
            Intent intent = new Intent();
            intent.putExtra("touchmode", touchBool);
            intent.putExtra("path", filename);
            intent.putExtra("filename", video);
            setResult(1, intent);
            second.cancel();
            finish();
        }
        else if(starJoin&&touchBool)
        {
            if(timer_sec==120) {
                Intent intent = new Intent();
                intent.putExtra("touchmode", touchBool);
                intent.putExtra("paths", dividedpath);
                intent.putExtra("files", dividedfile);
                intent.putExtra("filename", video);
                setResult(1, intent);
                second.cancel();
                finish();
            }

        }
        else if(touchBool&&!starJoin)
        {
           if(timer_sec==120) {
               Intent intent = new Intent(Record.this, WritePost.class);
               intent.putExtra("paths", dividedpath);
               intent.putExtra("files", dividedfile);
               intent.putExtra("loginUser", loginUser);
               intent.putExtra("filename", video);
               intent.putExtra("touchmode", touchBool);
               startActivityForResult(intent, 0);
               if (!second.cancel())
                   second.cancel();
               timer_sec = 0;
           }
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        if(second!=null) {
            if (!second.cancel())
                second.cancel();
        }
        setResult(2);
        finish();
    }

    public void surfaceCreated(SurfaceHolder holder) {
        try {
            if (cam == null) {
                cam = Camera.open(mCameraFacing);
                cam.setPreviewDisplay(holder);
                Camera.Parameters parameters = cam.getParameters();
                List<String> supFlash = cam.getParameters().getSupportedFlashModes();
                List<String> supportedFocusModes = cam.getParameters().getSupportedFocusModes();
                if (supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE))
                    parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
                if(supFlash.contains(Camera.Parameters.FLASH_MODE_TORCH))
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                cam.setDisplayOrientation(90);
                cam.setParameters(parameters);
                cam.startPreview();
                cam.unlock();

            }
        } catch (IOException e) {
            Log.d("create", String.valueOf(e));
        }
        Log.d("녹화","크리에이트");

    }
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        Log.d("change","change");
        if (holder.getSurface() == null) {
            // preview surface does not exist
            return;
        }
        try {
            cam.stopPreview();
        } catch (Exception e) {
        }
        try {
            cam.setPreviewDisplay(holder);
            cam.startPreview();
        } catch (Exception e) {
            Log.d("parameter","error");
        }
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
       if(recording){
               recorder.stop();
               recorder.release();
               recording = false;
           }
        else
       {
           if(recorder!=null)
               recorder.release();
       }
        if(cam!=null) {
            cam.lock();
            cam.release();
            cam = null;
        }
        if(second!=null) {
            if (!second.cancel())
                second.cancel();
            Log.d("파괴", "파괴");
        }
       }

    public void testStart() {

        second = new TimerTask() {

            @Override
            public void run() {
                Log.i("Test", "Timer start");
                progress();
                timer_sec++;
                Log.d("Test", Integer.toString(timer_sec));

                if(timer_sec==120) {
                    SystemClock.sleep(2000);
                    recording = false;
                    if(recorder!=null) {
                        recorder.stop();
                        recorder.release();
                        recorder = null;
                    }
                   savefile();
                }
            }
        };
        Timer timer = new Timer();
        timer.schedule(second, 0, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0)
        {
            if(resultCode == 0) {
                finish();
            }
        }
    }

    private void progress() {

        new Thread() {
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        int sec = 12;

                        float val = (float)timer_sec/120;
                        Log.d("초초",Float.toString(val));
                        btn_progress.setText(sec - (timer_sec/10) + "s");
                        progress.setProgress(val);
                        progress.startAnimation();
                        Log.d("초다",Integer.toString(timer_sec));
                        if(val==1)
                            Thread.interrupted();
                    }
                });
            }
        }.start();
    }
}