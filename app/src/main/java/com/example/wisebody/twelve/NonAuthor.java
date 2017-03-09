package com.example.wisebody.twelve;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.devbrackets.android.exomedia.core.video.scale.ScaleType;
import com.devbrackets.android.exomedia.listener.OnCompletionListener;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.EMVideoView;
import com.example.wisebody.twelve.player.LimitPlay;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

/**
 * Created by wisebody on 2016. 8. 25..
 */
public class NonAuthor extends Activity {
    EMVideoView preview;
    ProgressBar progressBar;
    Button playbtn;
    Button yesbtn;
    Button record;
    TextView countdown;
    TextView text;
    TextView yes;
    TextView title;

    Thread count;
    User loginUser;
    String myJSON;
    JSONArray post;
    String countText;
    String video;
    String nickname;
    String totalYes;
    Boolean countThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nonauthor);
        preview = (EMVideoView) findViewById(R.id.preview);
        record = (Button) findViewById(R.id.recordbtn);
        progressBar = (ProgressBar) findViewById(R.id.loading);
        playbtn = (Button) findViewById(R.id.playbtn);
        text = (TextView) findViewById(R.id.text);
        yes = (TextView) findViewById(R.id.yes);
        countdown = (TextView) findViewById(R.id.countdown);
        title = (TextView) findViewById(R.id.title);
        yesbtn = (Button) findViewById(R.id.yesbtn);
        record.setVisibility(View.GONE);
        playbtn.setVisibility(View.GONE);
        Intent getIntent = getIntent();
        loginUser = (User) getIntent.getSerializableExtra("loginUser");
        getData(loginUser.id);
        preview.setScaleType(ScaleType.CENTER_CROP);
        preview.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared() {
                progressBar.setVisibility(View.GONE);
                playbtn.setVisibility(View.VISIBLE);
                preview.setVolume(0);
                preview.start();
            }
        });
        preview.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion() {
                if(video!=null) {
                    playbtn.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    preview.setVideoURI(Uri.parse("http://girim2.ga/twelve/uploads/" + video));
                    preview.requestFocus();
                }
            }
        });
        playbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NonAuthor.this, LimitPlay.class);
                intent.putExtra("URL", video);
                intent.putExtra("authorization","0");
                intent.putExtra("id",loginUser.id);
                startActivity(intent);
            }
        });
        yesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addYes(loginUser.id,video);
            }
        });
    }
    @Override
    protected void onPause()
    {
        super.onPause();
        countThread = false;
        if(count!=null)
            count = null;
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        if(count==null)
            MakeCountThread();
        if(video!=null) {
            countThread = true;
            count.start();
        }
    }
    protected void MakeCountThread()
    {
        count = new Thread(new Runnable() {
            @Override
            public void run() {
                while (countThread)
                {
                    try {
                        CountDown(video);
                        Thread.sleep(900);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    protected void showList(){
        try {
            nickname = null;
            video = null;
            totalYes = null;
            JSONObject jsonObj = null;
                jsonObj = new JSONObject(myJSON);
                post = jsonObj.getJSONArray("result");
                JSONObject c = post.getJSONObject(0);
                nickname = c.getString("nickname");
                video = c.getString("video");
                totalYes = c.getString("yes");
            if(count ==null)
                MakeCountThread();
            countThread = true;
            count.start();
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
    protected void getData(String id) {
        class GetData extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                text.setText("이 사람은 스타\n"+nickname+"이 맞나요?");
                double num = Double.parseDouble(totalYes);
                DecimalFormat df = new DecimalFormat("#,##0");
                yes.setText("YES "+df.format(num));
                preview.setVideoURI(Uri.parse("http://girim2.ga/twelve/uploads/"+video));
                preview.requestFocus();
            }

            @Override
            protected String doInBackground(String... params) {

                DB db = new DB("nonauthorstar.php");
                String id = (String) params[0];
                String result = db.dupli(id);
                myJSON = result;
                showList();

                return result;
            }
        }
        GetData getData = new GetData();
        getData.execute(id);
    }

    private void CountDown(String video) {

        class CountDown extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                countText = s;
                countdown.setText(s);
                Log.d("표기",s);
            }

            @Override
            protected String doInBackground(String... params) {

                String video = (String)params[0];
                DB db = new DB("sendTime.php");
                String result = db.countDown(video);
                Log.d("sub", result);
                return result;
            }
        }
        CountDown task = new CountDown();
        task.execute(video);
    }
    protected void addYes(String id, String video) {
        class AddYes extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(s.equals("이미추천"))
                    Toast.makeText(getApplicationContext(),"이미 눌렀어요",Toast.LENGTH_LONG).show();
                else if(s.equals("인증완료"))
                {

                }
                else
                {
                    double num = Double.parseDouble(s);
                    DecimalFormat df = new DecimalFormat("#,##0");
                    yes.setText("YES " + df.format(num));
                }

            }

            @Override
            protected String doInBackground(String... params) {

                DB db = new DB("pressyes.php");
                String id = (String) params[0];
                String video = (String) params[1];
                String result = db.addLatestBookmar(id, video);

                return result;
            }
        }
        AddYes addYes = new AddYes();
        addYes.execute(id, video);
    }
}

