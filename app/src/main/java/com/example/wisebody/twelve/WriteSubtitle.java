package com.example.wisebody.twelve;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IntRange;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.devbrackets.android.exomedia.core.video.scale.ScaleType;
import com.devbrackets.android.exomedia.listener.OnBufferUpdateListener;
import com.devbrackets.android.exomedia.listener.OnCompletionListener;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.listener.OnSeekCompletionListener;
import com.devbrackets.android.exomedia.ui.widget.EMVideoView;
import com.example.wisebody.twelve.Util.CreateThumbnail;

import org.florescu.android.rangeseekbar.RangeSeekBar;
import org.florescu.android.rangeseekbar.seqData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;


/**
 * Created by wisebody on 2016. 7. 21..
 */
public class WriteSubtitle extends Activity {
    EditText writeSubtitle;
    RangeSeekBar<Integer> rangeSeekBar;
    EMVideoView VideoView;
    TextView save;
    Boolean prepare = false;
    Boolean touch = false;
    Boolean edit = false;
    ProgressBar loading;
    View editedView;
    int max;
    int min;
    String subtext;
    String url;
    String filename;
    String id;
    int buffertime;
    float minposition;
    float maxposition;
    Button play;
    Button pause;
    Button editBtn;
    Button back;
    FrameLayout seqContainer;
    Button send;
    int curMax;
    int curMin;
    int preEditMin;
    int preEditMax;
    int EditIndex;

    ArrayList<seqData> seqDatas = new ArrayList<seqData>();
    CreateThumbnail createThumbnail = new CreateThumbnail();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writesubtitle);
        Intent getInfor = getIntent();
        subtext="";
        max = 12; min = 0;
        id = getInfor.getStringExtra("id");
        url = "http://girim2.ga/twelve/uploads/"+getInfor.getStringExtra("URL");
        filename = getInfor.getStringExtra("URL");
        VideoView = (EMVideoView) findViewById(R.id.video);
        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit) {
                    edit = false;
                    send.setVisibility(View.VISIBLE);
                    editBtn.setVisibility(View.GONE);
                }
                else
                    finish();
            }
        });
        rangeSeekBar = new RangeSeekBar<Integer>(this);
        rangeSeekBar = (RangeSeekBar<Integer>) findViewById(R.id.range);
        rangeSeekBar.setRangeValues(0,12);
        rangeSeekBar.anchorViedoview(VideoView);
        loading = (ProgressBar) findViewById(R.id.loading);
        editBtn = (Button) findViewById(R.id.edit_btn);

        editBtn.setVisibility(View.GONE);
        curMin = rangeSeekBar.getSelectedMinValue(); curMax = rangeSeekBar.getSelectedMaxValue();
        writeSubtitle = (EditText) findViewById(R.id.writesubtitle);
        send = (Button) findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager mInputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                mInputMethodManager.hideSoftInputFromWindow(writeSubtitle.getWindowToken(), 0);
                if(VideoView.isPlaying()) {
                    VideoView.pause();
                    pause.setVisibility(View.VISIBLE);
                }
                seqDatas.add(new seqData(min, max, writeSubtitle.getText().toString(),minposition,maxposition));
                sortList();
                rangeSeekBar.setSeqList(seqDatas);
                addView(min, max, writeSubtitle.getText().toString());
                writeSubtitle.setText("");

            }
        });
        save = (TextView) findViewById(R.id.save);
        play = (Button) findViewById(R.id.play);
        pause = (Button) findViewById(R.id.pause);
        seqContainer = (FrameLayout) findViewById(R.id.seqContainer);
        play.setVisibility(View.INVISIBLE);
        pause.setVisibility(View.INVISIBLE);
        VideoView.setVideoURI(Uri.parse(url));
        VideoView.requestFocus();
        VideoView.setScaleType(ScaleType.CENTER_CROP);
        rangeSeekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN ){
                    float x = event.getX();
                    Log.d("엑스좌표",Float.toString(x));
                    for(int i =0; i<seqDatas.size(); i++)
                    {
                        seqData temp = seqDatas.get(i);
                        if(x>temp.getStartX()&&x<temp.getEndX()) {
                            rangeSeekBar.setSelectedMinValue(temp.getStarttime());
                            rangeSeekBar.setSelectedMaxValue(temp.getMax());
                            edit = true;
                            send.setVisibility(View.GONE);
                            editBtn.setVisibility(View.VISIBLE);
                            EditIndex = i;
                            preEditMin = temp.getStarttime(); preEditMax = temp.getMax();
                            if(VideoView.isPlaying())
                                VideoView.pause();
                            VideoView.seekTo(preEditMin*1000);
                            play.setVisibility(View.INVISIBLE);
                            loading.setVisibility(View.VISIBLE);
                        }

                    }
                }
                return false;
            }
        });
        VideoView.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared() {
                prepare = true;
                loading.setVisibility(View.GONE);
                play.setVisibility(View.VISIBLE);
                Log.d("준비중","준비중");
            }
        });

        VideoView.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion() {
                VideoView.setVideoURI(Uri.parse(url));
                VideoView.requestFocus();
                loading.setVisibility(View.VISIBLE);
                prepare = false;
                Log.d("비디오","완료");
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!VideoView.isPlaying()) {
                    VideoView.start();
                    play.setVisibility(View.INVISIBLE);
                }
                Log.d("비디오",Boolean.toString(VideoView.isPlaying()));
            }

        });
        VideoView.setOnBufferUpdateListener(new OnBufferUpdateListener() {
            @Override
            public void onBufferingUpdate(@IntRange(from = 0L, to = 100L) int percent) {
                Toast.makeText(getApplicationContext(),"버퍼링중",Toast.LENGTH_LONG).show();
            }
        });
        VideoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                    {
                        if(VideoView.isPlaying())
                        {
                            VideoView.pause();
                            pause.setVisibility(View.VISIBLE);
                        }
                        break;
                    }
                }
                return false;
            }
        });
        VideoView.setOnSeekCompletionListener(new OnSeekCompletionListener() {
            @Override
            public void onSeekComplete() {

                play.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!VideoView.isPlaying())
                {
                    VideoView.start();
                    pause.setVisibility(View.INVISIBLE);
                }

            }
        });



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (seqDatas.size()>0) {
                    for (Iterator<seqData> it = seqDatas.iterator(); it.hasNext(); ) {
                        seqData f = it.next();
                        subtext = subtext+"<SYNC="+f.getStarttime()*1000+","+f.getMax()*1000
                                +"><text>"+f.getText()+"\n";

                    }
                    writeSmi(subtext,filename);
                }
                else
                    Toast.makeText(getApplicationContext(),"자막 입력하고 저장해",Toast.LENGTH_LONG).show();
            }
        });

        writeSubtitle.addTextChangedListener(new TextWatcher() {
            String previousString = "";

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                previousString = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (writeSubtitle.getLineCount() >= 3) {
                    writeSubtitle.setText(previousString);
                    writeSubtitle.setSelection(writeSubtitle.length());
                }
            }
        });
        rangeSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<Integer> bar, Integer minValue, Integer maxValue) {
                max = maxValue;
                min = minValue;
                for(int i =0; i<seqDatas.size(); i++) {
                    if(i==EditIndex&&edit) {
                        if(i<seqDatas.size()-1)
                                i++;
                        else break;
                    }
                    seqData temp = seqDatas.get(i);
                    if (max > temp.getStarttime()&&min<=temp.getStarttime())
                    {
                        if(curMax<max) {
                            if (max <= temp.getMax())
                                max = temp.getMax() + 1;
                            min = max - 1;
                        }
                        if (curMin>min)
                        {
                            if(min==temp.getStarttime())
                                min = temp.getStarttime() -1;
                            max = min +1;
                        }
                        i =-1;
                    }
                    if(min<temp.getMax()&&max>=temp.getMax())
                    {
                        if(curMax<max) {
                            if (max == temp.getMax())
                                max = max +1;
                            min = max - 1;
                        }
                        if(curMin>min){
                            if(min>=temp.getStarttime())
                            {
                                min = temp.getStarttime()-1;
                                max = min+1;
                            }
                        }
                        i=-1;
                    }
                    if(max>=temp.getMax()&&min<=temp.getStarttime())
                    {
                        if(curMax<max)
                        {
                            min = max -1;
                        }
                        if(curMin>min)
                        {
                            max = min+1;
                        }
                        i = -1;
                    }
                    if(min>=temp.getStarttime()&&max<=temp.getMax())
                    {
                        if(curMax<max)
                        {
                            max=temp.getMax()+1;
                            min = max -1;
                        }
                        if(curMin>min)
                        {
                            min = temp.getStarttime()-1;
                            max = min+1;
                        }
                        else
                        {
                            max = temp.getMax()+1;
                            min = max -1;
                        }
                        i = -1;
                    }
                }
                /*if(VideoView.isPlaying())
                VideoView.pause();
                play.setVisibility(View.INVISIBLE);*/
                //loading.setVisibility(View.VISIBLE);
                //VideoView.seekTo(min * 1000);
                curMax = max; curMin = min;
                rangeSeekBar.setSelectedMinValue(min); rangeSeekBar.setSelectedMaxValue(max);
                min = rangeSeekBar.getSelectedMinValue(); max = rangeSeekBar.getSelectedMaxValue();
                maxposition = rangeSeekBar.getMaxmatrix();
                minposition = rangeSeekBar.getMinmatrix();
                Log.d("민밸류",Integer.toString(min));
                Log.d("맥스밸류",Integer.toString(max));
                Log.d("맥스위치", Float.toString(maxposition));
                Log.d("민위치", Float.toString(minposition));
            }
        });

        new Thread(new Runnable() {
            public void run() {
                try {
                    while (true) {
                        myHandler.sendMessage(myHandler.obtainMessage());
                        Thread.sleep(850);
                    }
                } catch (Throwable t) {
                    // Exit Thread
                }
            }
        }).start();
        //버튼 보이기 핸들러
        new Thread(new Runnable() {
            public void run() {
                try {
                    while (true) {
                        buttonHandler.sendMessage(buttonHandler.obtainMessage());
                        Thread.sleep(100);
                        Log.d("돌아","빙글");
                    }
                } catch (Throwable t) {
                    // Exit Thread
                }
            }
        }).start();

    }
    public void sortList() {
        Collections.sort(seqDatas, new Comparator<seqData>() {
            @Override
            public int compare(seqData obj1, seqData obj2) {
                // TODO Auto-generated method stub
                return (obj1.getStarttime() < obj2.getStarttime()) ? -1 : (obj1.getStarttime() > obj2.getStarttime()) ? 1 : 0;
            }
        });
    }
    public void addView(final int Viewmin, final int Viewmax, final String text)
    {
        LayoutInflater inflater = LayoutInflater.from(this);
        final FrameLayout layout = (FrameLayout) inflater.inflate(R.layout.seqlayout,null,false);
        FrameLayout.LayoutParams tvParams = new FrameLayout.LayoutParams(
                (int)(Math.round(maxposition-minposition)),
                ViewGroup.LayoutParams.MATCH_PARENT);
        Log.d("넓이",Integer.toString((int)(Math.round(maxposition-minposition))));
        tvParams.setMargins(Math.round(minposition),5,0,5);
        layout.setLayoutParams(tvParams);
        seqContainer.addView(layout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("클릭민",Integer.toString(Viewmin));
                Log.d("클릭맥스",Integer.toString(Viewmax));
                editedView = v;
                for(int i =0; i<seqDatas.size(); i++)
                {
                    seqData temp = seqDatas.get(i);
                    if(Viewmin==temp.getStarttime()) {
                        edit = true;
                        send.setVisibility(View.GONE);
                        EditIndex = i;
                        if(VideoView.isPlaying())
                            VideoView.pause();
                        VideoView.seekTo(Viewmin*1000);
                        play.setVisibility(View.INVISIBLE);
                        loading.setVisibility(View.VISIBLE);
                    }

                }
                editBtn.setVisibility(View.VISIBLE);
                rangeSeekBar.setSelectedMinValue(Viewmin);
                rangeSeekBar.setSelectedMaxValue(Viewmax);
                max = Viewmax;
                min = Viewmin;
                preEditMax = Viewmax; preEditMin = Viewmin;
                Log.d("레이아웃 민",Integer.toString(rangeSeekBar.getSelectedMinValue()));
                Log.d("레이아웃 맥스",Integer.toString(rangeSeekBar.getSelectedMaxValue()));
                writeSubtitle.setText(text);
                if(VideoView.isPlaying())
                VideoView.pause();
                loading.setVisibility(View.VISIBLE);
                play.setVisibility(View.INVISIBLE);
                VideoView.seekTo(Viewmin*1000);
            }
        });

        final ImageView seqimg = (ImageView) layout.findViewById(R.id.seqImage);
        seqimg.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Button delete = (Button) layout.findViewById(R.id.deleteseq);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seqContainer.removeView(layout);
                seqDatas.remove(new seqData(Viewmin,Viewmax,text,minposition,maxposition));
                sortList();
                rangeSeekBar.setSeqList(seqDatas);
            }
        });
        final ProgressBar progressBar = (ProgressBar) layout.findViewById(R.id.progress);
        new AsyncTask<Void, Void, Bitmap>() {

            @Override
            protected Bitmap doInBackground(Void... params) {
                Bitmap thumb = null;
                try {
                    thumb = createThumbnail.retriveVideoFrameFromVideo(url, Viewmin * 1000l);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                return thumb;
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                super.onPostExecute(result);
                seqimg.setImageBitmap(result);
                progressBar.setVisibility(View.GONE);
            }
        }.execute();
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seqContainer.removeView(editedView);
                seqDatas.remove(new seqData(preEditMin,preEditMax,text,minposition,maxposition));
                Log.d("뷰",Integer.toString(Viewmin));
                Log.d("뷰",Integer.toString(Viewmax));
                seqDatas.add(new seqData(min,max,text,minposition,minposition));
                sortList();
                rangeSeekBar.setSeqList(seqDatas);
                edit = false;
                editBtn.setVisibility(View.GONE);
                send.setVisibility(View.VISIBLE);
                addView(min,max,text);
            }
        });
    }
    Handler myHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            if(VideoView.isPlaying()) {
                if(buffertime==VideoView.getCurrentPosition())
                    loading.setVisibility(View.VISIBLE);
                else loading.setVisibility(View.GONE);
                buffertime= VideoView.getCurrentPosition();

                VideoView.setBackground(null);
            }
            if(max!=0) {
                if ((VideoView.getCurrentPosition()/1000) == max) {
                   /* if(VideoView.isPlaying())
                  VideoView.pause();
                    play.setVisibility(View.INVISIBLE);
                    loading.setVisibility(View.VISIBLE);*/
                    //VideoView.seekTo(min*1000);

                }
                Iterator<seqData> it = seqDatas.iterator();
                int counter =0;
                while (it.hasNext())
                {
                   seqData temp = it.next();
                    counter++;
                    int start =(int)temp.getStarttime();
                    int end = (int)temp.getMax();
                    if(start<=VideoView.getCurrentPosition()/1000&&end>=VideoView.getCurrentPosition()/1000)
                    {
//                        writeSubtitle.setText(temp.getText());
                        Log.d("자막",temp.getText());
                        //break;
                    }
                    if(end<VideoView.getCurrentPosition()/1000) {
//                        writeSubtitle.setText("");
                        //break;
                    }
                    if(counter==1)
                    {
                        if(start>VideoView.getCurrentPosition()/1000)
//                            writeSubtitle.setText("");
                        Log.d("스타트",Integer.toString(start));
                    }
                }
            }
            Log.d("시간",Integer.toString(VideoView.getCurrentPosition()));
            Log.d("카운터",Integer.toString(max));
        }

    };
    Handler buttonHandler = new Handler() {
        public void handleMessage(Message msg) {
            //getSyncIndex(videoView.getCurrentPosition());
            if (max != 0) {
                if ((VideoView.getCurrentPosition() / 1000) >= 12) {
                    if(VideoView.isPlaying())
                    VideoView.pause();
                    VideoView.seekTo(0);
                    play.setVisibility(View.INVISIBLE);
                    loading.setVisibility(View.VISIBLE);
                    //VideoView.seekTo(min*1000);
                    Log.d("보여라","보여");
                }
            }
            rangeSeekBar.setScrollTime(VideoView.getCurrentPosition());
        }
    };
    private String writeSmi(String text, String filename) {

        class inputData extends AsyncTask<String, Void, String> {

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

                String sub = (String)params[0];
                String filename = (String)params[1];
                DB db = new DB("writesub.php");
                String result = db.writeSub(sub, filename);
                Log.d("sub", result);
                Log.d("파일이름1", filename);
                return result;
            }
        }
        inputData task = new inputData();
        task.execute(text,filename);
        return "ok";
    }
}
