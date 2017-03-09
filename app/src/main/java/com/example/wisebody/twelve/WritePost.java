package com.example.wisebody.twelve;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.devbrackets.android.exomedia.core.video.scale.ScaleType;
import com.devbrackets.android.exomedia.listener.OnCompletionListener;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.EMVideoView;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by wisebody on 2016. 7. 27..
 */
public class WritePost extends Activity implements TextureView.SurfaceTextureListener {
    TextureView preview;
    User loginUser;
    String path;
    Button save;
    Button cancel;
    Button editbtn;
    ImageButton play;
    ImageView thumbnail;
    EditText text;
    RelativeLayout container;
    String filename;
    String nickname;
    Boolean star;
    Boolean touchmode;
    ArrayList<String> dividepath;
    ArrayList<String> dividefile;
    int index = 0;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wirtepost);
        path = null;
        Intent getIntent = getIntent();
        star = (Boolean) getIntent.getBooleanExtra("star",false);
        loginUser =  (User) getIntent.getSerializableExtra("loginUser");
        touchmode = (Boolean) getIntent.getBooleanExtra("touchmode",false);
        preview = (TextureView) findViewById(R.id.preview);
        preview.setSurfaceTextureListener(this);
        save = (Button) findViewById(R.id.savebtn);
        cancel = (Button) findViewById(R.id.cancelbtn);
        editbtn = (Button) findViewById(R.id.editbtn);
        play = (ImageButton) findViewById(R.id.playbtn);
        thumbnail = (ImageView) findViewById(R.id.thumbnail);
        thumbnail.setScaleType(ImageView.ScaleType.CENTER_CROP);
        text = (EditText) findViewById(R.id.text);
        container = (RelativeLayout) findViewById(R.id.bigcontainer);
        container.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(text.getWindowToken(), 0);
                return false;
            }
        });
        if(touchmode) {
            dividepath = (ArrayList<String>) getIntent.getStringArrayListExtra("paths");
            dividefile = (ArrayList<String>) getIntent.getStringArrayListExtra("files");
        }
        else {
            path = getIntent.getStringExtra("path");
            Log.d("길",path);
        }
        if(!star)
            filename = getIntent.getStringExtra("filename");
        else
        {
            nickname = getIntent.getStringExtra("nickname");
            text.setText("이 사람은 스타\n"+nickname+"이 맞나요?");
            text.setEnabled(false);
        }
        mediaPlayer = new MediaPlayer();
        invisible();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if(touchmode) {
                    if (index==0)
                        visible();
                    else
                        mp.start();
                }
                else
                    visible();
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thumbnail.setVisibility(View.GONE);
                mediaPlayer.start();
                play.setVisibility(View.INVISIBLE);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(touchmode)
                    uploadVideo(loginUser.id,filename,text.getText().toString());
                else
                uploadVideo(loginUser.id,filename,text.getText().toString());
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!star)
                setResult(1);
                finish();
            }
        });
        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(1);
                finish();
            }
        });
    }
    protected void invisible()
    {
        save.setVisibility(View.INVISIBLE);
        editbtn.setVisibility(View.INVISIBLE);
        cancel.setVisibility(View.INVISIBLE);
        play.setVisibility(View.INVISIBLE);
    }
    protected void visible()
    {
        if(!star)
        save.setVisibility(View.VISIBLE);
        else
        editbtn.setVisibility(View.VISIBLE);
        cancel.setVisibility(View.VISIBLE);
        play.setVisibility(View.VISIBLE);
    }
    private void uploadVideo(String id, final String filename, String ttext) {
        class UploadVideo extends AsyncTask<String, Void, String> {
            ProgressDialog uploading = new ProgressDialog(getApplicationContext());
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                 uploading = ProgressDialog.show(WritePost.this, "Uploading File", "Please wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                setResult(0);
                finish();
                uploading.dismiss();
            }

            @Override
            protected String doInBackground(String... params) {
                String msg = null;
                String mergeText = new String();
                String file = params[1];
                Upload u = new Upload();
                if(touchmode) {

                    for (int i = 0; i < dividepath.size(); i++) {
                        msg = u.uploadVideo(dividepath.get(i),"true");
                        if(i == dividepath.size()-1)
                            mergeText+="file "+dividefile.get(i);
                        else
                        mergeText+="file "+dividefile.get(i)+"\n";
                    }
                    DB mergefile = new DB("writesub.php");
                    mergefile.mergeVideo(mergeText,"mergevideo",file);
                }
                else
                msg = u.uploadVideo(path,"false");
                String id = params[0];
                String text = params[2];
                DB db = new DB("post.php");
                db.insertPost(id,file,text);
                return msg;
            }
        }
        UploadVideo uv = new UploadVideo();
        uv.execute(id, filename, ttext);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Surface s = new Surface(surface);
        mediaPlayer.setSurface(s);
        if(touchmode) {
            try {
                Glide.with(getApplicationContext()).load(dividepath.get(0)).into(thumbnail);
                mediaPlayer.setDataSource(dividepath.get(index));
                mediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.stop();
                    mp.reset();
                    if(index<dividepath.size())
                        index++;
                    if(index==dividepath.size())
                        index = 0;
                    try {
                        mp.setDataSource(dividepath.get(index));
                        mp.prepareAsync();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        else
        {
            try {
                Glide.with(getApplicationContext()).load(path).into(thumbnail);
                mediaPlayer.setDataSource(path);
                mediaPlayer.prepareAsync();
                Log.d("writePost","not touch");
            } catch (IOException e) {
                e.printStackTrace();
            }
           mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
               @Override
               public void onCompletion(MediaPlayer mp) {
                   mp.stop();
                   mp.reset();
                   invisible();
                   try {
                       mediaPlayer.setDataSource(path);
                       mp.prepareAsync();
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
               }
           });
        }
    }
    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }
}
