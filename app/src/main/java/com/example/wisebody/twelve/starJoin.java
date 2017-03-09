package com.example.wisebody.twelve;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by wisebody on 2016. 8. 22..
 */
public class starJoin extends Activity implements TextureView.SurfaceTextureListener {

    private EditText t_id;
    private EditText t_pass;
    private EditText t_repass;
    String job;
    EditText t_name;
    EditText t_nickname;
    EditText t_entertainment;
    RadioGroup radioGroup;
    Button record;
    TextureView video;
    Button playbtn;
    RelativeLayout mainLayout;
    ScrollView mainScroll;
    private Intent getIntent;
    private InputInfor infor;
    private int[] flag;
    MediaPlayer mediaPlayer;
    String path;
    String filename;
    String nickname;
    Boolean star = true;
    Boolean touchmod = false;
    User tempUser;
    int index = 0;
    ArrayList<String> dividedpath;
    ArrayList<String> dividedfile;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starjoin);
        mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);
        mainScroll = (ScrollView) findViewById(R.id.mainScroll);
        mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });
        mainScroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                return false;
            }
        });
        t_id = (EditText) findViewById(R.id.edit_id);
        t_pass = (EditText) findViewById(R.id.edit_pass);
        t_repass = (EditText) findViewById(R.id.edit_repass);
        t_name = (EditText) findViewById(R.id.edit_name);
        t_nickname = (EditText) findViewById(R.id.edit_nickname);
        t_entertainment = (EditText) findViewById(R.id.edit_entertainment);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                job = ((RadioButton)findViewById(checkedId)).getText().toString();
                Toast.makeText(getApplicationContext(),job,Toast.LENGTH_LONG).show();
            }
        });
        record = (Button) findViewById(R.id.recordVideo);
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((t_id.getText().toString()).contains("@")&& t_id.getText().toString().length()>=5) {
                    Intent intent = new Intent(starJoin.this, Record.class);
                    intent.putExtra("star", star);
                    intent.putExtra("loginUser",tempUser);
                    startActivityForResult(intent, 0);
                }
                else
                    Toast.makeText(getApplicationContext(),"올바른 E-mail을 입력해 주세요",Toast.LENGTH_LONG).show();
            }
        });
        playbtn = (Button) findViewById(R.id.playbtn);
        playbtn.setVisibility(View.GONE);
        playbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(starJoin.this,WritePost.class);
                if(touchmod)
                {
                    intent.putExtra("paths", dividedpath);
                    intent.putExtra("files", dividedfile);
                    intent.putExtra("touchmode", touchmod);
                }
                else
                    intent.putExtra("path",path);
                intent.putExtra("star",star);
                intent.putExtra("loginUser",tempUser);
                intent.putExtra("nickname",nickname);
                startActivityForResult(intent,1);
            }
        });
        video = (TextureView) findViewById(R.id.video);
        video.setSurfaceTextureListener(this);
        flag = new int[5];
        t_id.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkID(t_id.getText().toString());
            }


            @Override
            public void afterTextChanged(Editable arg0) {
                if(tempUser!=null)
                    tempUser = null;
                tempUser = new User(t_id.getText().toString());
                // 입력이 끝났을 때
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 입력하기 전에
            }
        });
        t_repass.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (t_pass.getText().toString().equals(t_repass.getText().toString())) {
                    t_repass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icn_pw_pp, 0, R.drawable.icn_okay_v_copy, 0);
                    flag[1] = 1;
                } else {
                    t_repass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icn_pw_pp, 0, R.drawable.icn_no_x, 0);
                    flag[1] = 0;
                }

            }


            @Override
            public void afterTextChanged(Editable arg0) {
                // 입력이 끝났을 때
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 입력하기 전에
            }
        });
        t_pass.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (t_pass.getText().toString().length() == 6)
                    t_pass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icn_pw_pp, 0, R.drawable.icn_okay_v_copy, 0);

                else
                    t_pass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icn_pw_pp, 0, R.drawable.icn_no_x, 0);

            }


            @Override
            public void afterTextChanged(Editable arg0) {
                // 입력이 끝났을 때
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 입력하기 전에
            }
        });
        t_nickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                   nickname = t_nickname.getText().toString();
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        if(requestCode==0)
        {
           if(resultCode==1)
           {
               touchmod = intent.getBooleanExtra("touchmode",false);
               filename = intent.getStringExtra("filename");
               if(touchmod)
               {
                   dividedpath = intent.getStringArrayListExtra("paths");
                   dividedfile = intent.getStringArrayListExtra("files");
               }
               else
               path = intent.getStringExtra("path");
               record.setVisibility(View.GONE);
               playbtn.setVisibility(View.VISIBLE);
           }
        }
        if(requestCode==1)
        {
            if(resultCode==1)
            {
                Intent intent1 = new Intent(this,Record.class);
                intent1.putExtra("star", star);
                intent1.putExtra("loginUser",tempUser);
                startActivityForResult(intent1, 0);
            }
        }
    }
    public void Back(View view)
    {
        setResult(1);
        finish();
    }

    public void Next(View view)
    {
        if(t_nickname.getText().toString().length()>0)
            flag[2]=1;
        if(t_name.getText().toString().length()>0)
            flag[3]=1;
        if(flag[0]==1&&flag[1]==1&&flag[2]==1&&flag[3]==1&&flag[4]==1)
        {
            insertToDatabase(t_id.getText().toString(),t_pass.getText().toString(),t_name.getText().toString(),
                    t_nickname.getText().toString(),t_entertainment.getText().toString(),filename);
        }
        else {
            if (flag[0] == 0)
                Toast.makeText(getApplicationContext(), "ID가 중복됩니다", Toast.LENGTH_LONG).show();
            else if (flag[1] == 0)
                Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다", Toast.LENGTH_LONG).show();
            else if (flag[2] == 0)
                Toast.makeText(getApplicationContext(), "닉네임을 입력해 주세요", Toast.LENGTH_LONG).show();
            else if (flag[3] == 0)
                Toast.makeText(getApplicationContext(), "이름을 입력해 주세요", Toast.LENGTH_LONG).show();
        }
    }

    private void uploadVideo(String id, String filename, String ttext) {
        class UploadVideo extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // uploading = ProgressDialog.show(Record.this, "Uploading File", "Please wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }

            @Override
            protected String doInBackground(String... params) {
                Upload u = new Upload();
                String msg = u.uploadVideo(path,"");
                String id = params[0];
                String file = params[1];
                Log.d("file",file);
                String text = params[2];
                DB db = new DB("post.php");
                db.insertPost(id,file,text);
                return msg;
            }
        }
        UploadVideo uv = new UploadVideo();
        uv.execute(id, filename, ttext);
    }

    private void insertToDatabase(String id, final String pass, String name, String nickname, String entertainment, String filename){

        class InsertData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(starJoin.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                if(s.equals("회원가입 완료")) {
                    Intent intent = new Intent(starJoin.this,Authorization.class);
                    setResult(0);
                    intent.putExtra("star",star);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            protected String doInBackground(String... params) {

                DB db = new DB("starJoin.php");
                String id = (String)params[0];
                String pass = (String)params[1];
                String name = (String)params[2];
                String nickname = (String)params[3];
                String entertainment = (String)params[4];
                String filename = (String)params[5];
                String result = db.starInsert(id,pass,name,nickname,entertainment,filename);
                Log.d(result,"result");
                uploadVideo(tempUser.id,filename,"");
                return result;
            }
        }


        InsertData task = new InsertData();
        task.execute(id, pass,name,nickname,entertainment,filename);
    }
    private void checkID(String id){

        class InsertData extends AsyncTask<String, Void, String> {


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(s.equals("ID중복"))
                {
                    flag[0]=0;
                    t_id.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icn_mail_pp, 0, R.drawable.icn_no_x, 0);

                }
                else if(!t_id.getText().toString().contains("@")||t_id.getText().toString().length()<5)
                {
                    flag[0]=0;
                    t_id.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icn_mail_pp, 0, R.drawable.icn_no_x, 0);
                }
                else
                {
                    flag[0]=1;
                    t_id.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icn_mail_pp, 0, R.drawable.icn_okay_v_copy, 0);
                }
            }

            @Override
            protected String doInBackground(String... params) {

                DB db = new DB("dupli.php");
                String id = (String)params[0];
                String result = db.dupli(id);
                Log.d(result,"result");
                return result;
            }
        }


        InsertData task = new InsertData();
        task.execute(id);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Surface s = new Surface(surface);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setSurface(s);
        if(touchmod)
        {
            if(dividedpath.size()>0) {
                flag[4] =1;
                try {
                    mediaPlayer.setDataSource(dividedpath.get(index));
                    mediaPlayer.prepareAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.stop();
                        mp.reset();
                        if (index < dividedpath.size())
                            index++;
                        if (index == dividedpath.size())
                            index = 0;
                        try {
                            mp.setDataSource(dividedpath.get(index));
                            mp.prepareAsync();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
        else
        {
            if(path!=null) {
                flag[4] =1;
                try {
                    mediaPlayer.setDataSource(path);
                    mediaPlayer.prepareAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.reset();
                        try {
                            mediaPlayer.setDataSource(path);
                            mediaPlayer.prepareAsync();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                record.setVisibility(View.GONE);
                mp.start();
            }
        });

    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        if(mediaPlayer!=null) {
            if(mediaPlayer.isPlaying())
                mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer=null;
        }
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }
}
