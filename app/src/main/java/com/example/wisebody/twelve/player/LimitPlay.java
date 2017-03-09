package com.example.wisebody.twelve.player;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.devbrackets.android.exomedia.core.video.scale.ScaleType;
import com.devbrackets.android.exomedia.listener.OnCompletionListener;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.EMVideoView;
import com.example.wisebody.twelve.R;
import com.example.wisebody.twelve.WriteSubtitle;

/**
 * Created by wisebody on 2016. 7. 29..
 */

public class LimitPlay extends Activity {
    EMVideoView videoView;
    Button play;
    Button pause;
    String URL;
    String id;
    String authorization;
    int authornum;
    FrameLayout menucontainer;
    FrameLayout videocontainer;
    Button editSub;
    ProgressBar progressBar;
    int temptime;
    Thread bufThread;
    Boolean threadbreak = false;
    Boolean callpause = false;
    Boolean playing = false;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.limitplay);
        Intent getIntent = getIntent();
        URL = (String)getIntent.getStringExtra("URL");
        id = (String)getIntent.getStringExtra("id");
        authorization = (String)getIntent.getStringExtra("authorization");
        authornum = Integer.parseInt(authorization);
        videoView = (EMVideoView) findViewById(R.id.video);
        play = (Button) findViewById(R.id.play);
        pause = (Button) findViewById(R.id.pause);
        editSub = (Button) findViewById(R.id.edit_sub);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        editSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LimitPlay.this, WriteSubtitle.class);
                intent.putExtra("URL",URL);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });
        menucontainer = (FrameLayout) findViewById(R.id.menucontainer);
        videocontainer = (FrameLayout) findViewById(R.id.videocontainer);
        play.setVisibility(View.INVISIBLE);
        pause.setVisibility(View.INVISIBLE);
        if(authornum==0)
        {
            menucontainer.setVisibility(View.INVISIBLE);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            videocontainer.setLayoutParams(params);

        }
        videoView.setScaleType(ScaleType.CENTER_CROP);
        videoView.setVideoURI(Uri.parse("http://girim2.ga/twelve/uploads/"+URL));
        videoView.requestFocus();
        videoView.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared() {
                progressBar.setVisibility(View.GONE);
                play.setVisibility(View.VISIBLE);
            }
        });
        videoView.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion() {
                progressBar.setVisibility(View.VISIBLE);
                videoView.setVideoURI(Uri.parse("http://girim2.ga/twelve/uploads/"+URL));
                videoView.requestFocus();

            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!videoView.isPlaying()) {
                    videoView.start();
                    play.setVisibility(View.INVISIBLE);
                }
                    makebufThread();
                threadbreak = false;
                    bufThread.start();
                Log.d("비디오",Boolean.toString(videoView.isPlaying()));
            }

        });
        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                    {
                        if(videoView.isPlaying())
                        {
                            videoView.pause();
                            pause.setVisibility(View.VISIBLE);
                            if(!threadbreak)
                            threadbreak = true;
                        }
                        break;
                    }
                }
                return false;
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!videoView.isPlaying())
                {
                    videoView.start();
                    pause.setVisibility(View.INVISIBLE);
                    if(threadbreak)
                        makebufThread();
                    threadbreak = false;
                    bufThread.start();
                }
            }
        });
    }
    protected void makebufThread()
    {
        bufThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        if(threadbreak)
                            break;
                        buf.sendMessage(buf.obtainMessage());
                        Thread.sleep(800);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    Handler buf = new Handler() {
        public void handleMessage(Message msg) {
            //getSyncIndex(videoView.getCurrentPosition());
            if (videoView.isPlaying() && temptime == videoView.getCurrentPosition())
                progressBar.setVisibility(View.VISIBLE);
            else if(videoView.isPlaying()&&temptime != videoView.getCurrentPosition())
                progressBar.setVisibility(View.GONE);
            temptime = videoView.getCurrentPosition();
            Log.d("템프", Integer.toString(temptime));
        }
    };

    @Override
    protected void onPause()
    {
        super.onPause();
        if(!threadbreak)
            threadbreak = true;
      if(videoView.isPlaying())
          videoView.pause();
        callpause = true;
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        if(callpause) {
            if (threadbreak)
                makebufThread();
            threadbreak = false;
            bufThread.start();
            videoView.start();
            callpause = false;
        }
    }

}
