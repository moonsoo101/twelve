package com.example.wisebody.twelve;

/**
 * Created by wisebody on 2016. 7. 12..
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.devbrackets.android.exomedia.core.video.scale.ScaleType;
import com.devbrackets.android.exomedia.listener.OnCompletionListener;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.EMVideoView;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Subtitle extends Activity  {
    private EMVideoView videoView;
    private TextView subtitle;
    private ArrayList<subData> parsedSub;
    Context activity;
    int counter=0;
    Button play;
    Boolean timeflag;
    String filename;
    String url;
    ProgressBar loading;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subtitle);
        videoView = (EMVideoView) findViewById(R.id.videoView1);
        subtitle = (TextView) findViewById(R.id.subtitle);
        loading = (ProgressBar) findViewById(R.id.loading);
        Intent getInfor = getIntent();
        url = getInfor.getStringExtra("url");
        filename = url;
        play = (Button) findViewById(R.id.play);
        play.setVisibility(View.INVISIBLE);
        timeflag = false;
        videoView.setScaleType(ScaleType.CENTER_CROP);
        smi(filename);
        //videoView.setMediaController(mediaController);
        videoView.setVideoURI(Uri.parse("http://girim2.ga/twelve/uploads/"+url));
        videoView.requestFocus();
        videoView.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared() {
                play.setVisibility(View.VISIBLE);loading.setVisibility(View.GONE);
            }});
        videoView.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion() {
                play.setVisibility(View.INVISIBLE);
                loading.setVisibility(View.VISIBLE);
                videoView.setVideoURI(Uri.parse("http://girim2.ga/twelve/uploads/"+url));
                videoView.requestFocus();
            }
        });
                play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        counter = 0;
                        play.setVisibility(View.INVISIBLE);
                        videoView.start();
                        new Thread(new Runnable() {
                            public void run() {
                                try {
                                    while (true) {
                                        Thread.sleep(100);
                                        myHandler.sendMessage(myHandler.obtainMessage());
                                    }
                                } catch (Throwable t) {
                                    // Exit Thread
                                }
                            }
                        }).start();
                    }
                });




    }
    Handler myHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            //getSyncIndex(videoView.getCurrentPosition());
            if(parsedSub!=null) {
                if (counter < parsedSub.size()) {
                    if ((videoView.getCurrentPosition() / 1000) >=(parsedSub.get(counter).gettime() / 1000)) {
                        subtitle.setText(Html.fromHtml(parsedSub.get(counter).gettext()));
                        if(videoView.getCurrentPosition()/1000 >= (parsedSub.get(counter).getEndTime()/1000)) {
                            Log.d("끝시간",Long.toString(parsedSub.get(counter).getEndTime()));
                            counter++;
                            subtitle.setText("");
                        }
                    }
                }
            }
            Log.d("시간",Integer.toString(videoView.getCurrentPosition()));
            Log.d("카운터",Integer.toString(counter));
            }

    };


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            videoView.stopPlayback();
            videoView.clearFocus();
            finish();
        }
        return false;
    }

    private String smi(String filename) {

        class searchData extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                //subtitle.setText(Html.fromHtml(s));
            }

            @Override
            protected String doInBackground(String... params) {

                DB db = new DB("a");
                String filename = (String)params[0];
                Log.d("파일이름",filename);
                String result = db.smi(filename);
                Log.d("sub", result);
                if(!result.contains("자막이 없다"))
                parsing(result);
                return result;
            }
        }
        searchData task = new searchData();
        task.execute(filename);
        return "ok";
    }


    protected void parsing(String subtitle) {
        parsedSub = new ArrayList<subData>();
        long time = -1;
        long EndTime = -1;
        String text = null;
        InputStream is = new ByteArrayInputStream(subtitle.getBytes());
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        Boolean substart = false;
        try {
            while ((line = br.readLine()) != null) {
                Log.d("line", line);
                if (line.contains("<SYNC")) {
                    substart = true;
                    if (time != -1) {
                        parsedSub.add(new subData(time, text, EndTime));
                    }
                    time = Integer.parseInt(line.substring(line.indexOf("=") + 1, line.indexOf(",")));
                    EndTime = Integer.parseInt(line.substring(line.indexOf(",")+1,line.indexOf(">")));
                    Log.d("time", Long.toString(time));
                    text = line.substring(line.indexOf(">") + 1, line.length());
                    text = text.substring(text.indexOf(">") + 1, text.length());
                    Log.d("text", text);
                } else {
                    if (substart == true) {
                        text += line;
                    }
                }

            }
            parsedSub.add(new subData(time, text, EndTime));
            for (subData member : parsedSub) {
                Log.d("Member name: ", member.gettext());
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



