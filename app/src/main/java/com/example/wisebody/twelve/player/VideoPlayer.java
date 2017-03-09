package com.example.wisebody.twelve.player;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.IOException;

public class VideoPlayer extends TextureView implements TextureView.SurfaceTextureListener {

    private static String TAG = "VideoPlayer";

    /**This flag determines that if current VideoPlayer object is first item of the list if it is first item of list*/
    boolean isFirstListItem;

    boolean isLoaded;
    boolean isMpPrepared;
    boolean detached;
    public boolean play;


    String url;
    MediaPlayer mp;
    Surface surface;
    SurfaceTexture s;
    Button playbutton;
    FrameLayout videocontainer;

    public VideoPlayer(Context context, Button playbutton, FrameLayout videocontainer) {
        super(context);
        this.playbutton = playbutton;
        this.videocontainer = videocontainer;
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mp != null) {
                    if (mp.isPlaying())
                        mp.pause();
                    else if (!mp.isPlaying() && isMpPrepared)
                        mp.start();
                }
            }
        });
        setSurfaceTextureListener(this);
    }
    public VideoPlayer(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public void loadVideo(String localPath) {

        this.url = localPath;
        isLoaded = true;
        if (this.isAvailable()) {
            Log.d("customplayer","load");
            if(!detached&&play) {
                prepareVideo(getSurfaceTexture());
            }
        }

    }

    @Override
    public void onSurfaceTextureAvailable(final SurfaceTexture surface, int width, int height) {
        isMpPrepared = false;
        if(play) {
            prepareVideo(surface);
            Log.d("customerplayer","avail"+Boolean.toString(play));
        }
        Log.d("customerplayer","avail"+Boolean.toString(play));
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {

        if(mp!=null)
        {
            if(mp.isPlaying())
            mp.stop();
            mp.reset();
            mp.release();
            mp = null;
            this.setVisibility(GONE);
            playbutton.setVisibility(VISIBLE);
            isMpPrepared = false;
            Log.d("customerplayer","destroy");
            detached = true;
            play = false;
        }

        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
    }

    public void prepareVideo(SurfaceTexture t)
    {

        this.surface = new Surface(t);
        mp = new MediaPlayer();

        try {
            mp.setDataSource(url);
            mp.setSurface(this.surface);
            mp.prepareAsync();

            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                public void onPrepared(MediaPlayer mp) {
                    if(!detached) {
                        isMpPrepared = true;
                        VideoPlayer.this.setVisibility(VISIBLE);
                        if (isMpPrepared)
                            mp.start();
                        Log.d("customplayer", "start");
                    }
                }
            });
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    isMpPrepared = false;
                    VideoPlayer.this.setVisibility(GONE);
                    mp.reset();
                    mp.release();
                    mp = null;
                    playbutton.setVisibility(VISIBLE);
                }
            });
        } catch (IllegalArgumentException e1) {
            e1.printStackTrace();
        } catch (SecurityException e1) {
            e1.printStackTrace();
        } catch (IllegalStateException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        detached = false;
        this.setVisibility(INVISIBLE);
        playbutton.setVisibility(VISIBLE);
    }
   @Override
    protected void onDetachedFromWindow() {
       super.onDetachedFromWindow();
       if (mp != null) {
               mp.release();
               mp = null;
               Log.d("detatch", "detach");
                detached = true;
           play = false;
       }
       this.setVisibility(GONE);
       playbutton.setVisibility(VISIBLE);
   }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);

    }

    public boolean startPlay()
    {
        if(mp!=null)
            if(!mp.isPlaying())
            {
                mp.start();
                return true;
            }

        return false;
    }

    public void pausePlay()
    {
        if(mp!=null)
            mp.pause();
    }

    public void stopPlay()
    {
        if(mp!=null) {
            mp.stop();
            mp.reset();
            mp.release();
            mp = null;
            Log.d("customplayer","stop!");
        }
        this.setVisibility(GONE);
        playbutton.setVisibility(VISIBLE);
        Log.d("customplayer","stop");
    }
    public boolean isPlaying()
    {
        if(mp !=null)
          return mp.isPlaying();
        else
            return false;
    }

    public void changePlayState()
    {
        if(mp!=null)
        {
            if(mp.isPlaying())
                mp.pause();
            else
                mp.start();
        }

    }
}