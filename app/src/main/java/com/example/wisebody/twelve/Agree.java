package com.example.wisebody.twelve;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by wisebody on 2016. 6. 30..
 */
public class Agree extends Activity {

    TextView t_agree1;
    TextView t_agree2;
    TextView t_agree3;
    CheckBox allCheck;
    CheckBox agree1;
    CheckBox agree2;
    CheckBox agree3;
    ScrollView mainsrc;
    String starOrfan;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agree);
        t_agree1 = (TextView)findViewById(R.id.t_agree1);
        t_agree1.setMovementMethod(new ScrollingMovementMethod());
        t_agree2 = (TextView)findViewById(R.id.t_agree2);
        t_agree2.setMovementMethod(new ScrollingMovementMethod());
        t_agree3 = (TextView)findViewById(R.id.t_agree3);
        t_agree3.setMovementMethod(new ScrollingMovementMethod());
        allCheck = (CheckBox) findViewById(R.id.allagree);
        agree1 = (CheckBox) findViewById(R.id.agree1);
        agree2 = (CheckBox) findViewById(R.id.agree2);
        agree3 = (CheckBox) findViewById(R.id.agree3);
        mainsrc = (ScrollView) findViewById(R.id.mainscr);
        View.OnTouchListener touchListener = new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP)
                    mainsrc.requestDisallowInterceptTouchEvent(false);
                else
                    mainsrc.requestDisallowInterceptTouchEvent(true);
                //t_agree1.setVerticalScrollBarEnabled(true);
                return false;
            }
        };
        t_agree1.setOnTouchListener(touchListener);
        t_agree2.setOnTouchListener(touchListener);
        t_agree3.setOnTouchListener(touchListener);
        Intent getIntent = getIntent();
        starOrfan =  getIntent.getStringExtra("starOrfan");
    }
    protected void Back(View view)
    {
        finish();
    }
    protected void Next(View view)
    {
        if(agree1.isChecked()&&agree2.isChecked()&&agree3.isChecked()) {
            if (starOrfan.equals("fan")) {
                Intent intent = new Intent(this, Join.class);
                startActivityForResult(intent, 0);
            }
            else
            {
                Intent intent = new Intent(this, starJoin.class);
                startActivityForResult(intent, 1);
            }

        }
        else
            Toast.makeText(getApplicationContext(),"모든 약관에 동의해주세요",Toast.LENGTH_LONG).show();
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode==0)
        {
            if(resultCode==0)
                finish();
        }
        if(requestCode==1)
        {
        if(resultCode==0)
            finish();
        }
    }
    protected void allBox(View view)
    {
        if(allCheck.isChecked()) {
            agree1.setChecked(true);
            agree2.setChecked(true);
            agree3.setChecked(true);
        }
        else
        {
            agree1.setChecked(false);
            agree2.setChecked(false);
            agree3.setChecked(false);
        }
    }
    protected void check1(View view)
    {
        if(agree1.isChecked())
        {
            if(agree2.isChecked()&&agree3.isChecked())
                allCheck.setChecked(true);
            else
                allCheck.setChecked(false);
        }
        else
                allCheck.setChecked(false);
    }
    protected void check2(View view)
    {
        if(agree2.isChecked())
        {
            if(agree1.isChecked()&&agree3.isChecked())
                allCheck.setChecked(true);
            else
                allCheck.setChecked(false);
        }
        else
                allCheck.setChecked(false);
    }
    protected void check3(View view)
    {
        if(agree3.isChecked())
        {
            if(agree1.isChecked()&&agree2.isChecked())
                allCheck.setChecked(true);
            else
                allCheck.setChecked(false);
        }
        else
                allCheck.setChecked(false);
    }
}
