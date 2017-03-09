package com.example.wisebody.twelve;

/**
 * Created by wisebody on 2016. 6. 28..
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;



public class Join extends Activity{
    private EditText t_id;
    private EditText t_pass;
    private EditText t_repass;
    private Intent getIntent;
    private InputInfor infor;
    private int[] flag;
    RelativeLayout mainlayout;
    ScrollView scrollView;

    //private Boolean[] flag;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        t_id = (EditText) findViewById(R.id.edit_id);
        t_pass = (EditText) findViewById(R.id.edit_pass);
        t_repass = (EditText) findViewById(R.id.edit_repass);
        mainlayout = (RelativeLayout)findViewById(R.id.mainLayout);
        mainlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });
        scrollView = (ScrollView) findViewById(R.id.scrollcontainer);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
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
        t_id.getBackground().setColorFilter(Color.parseColor("#d7d7e5"), PorterDuff.Mode.SRC_IN);
        t_pass.getBackground().setColorFilter(Color.parseColor("#d7d7e5"), PorterDuff.Mode.SRC_IN);
        t_repass.getBackground().setColorFilter(Color.parseColor("#d7d7e5"), PorterDuff.Mode.SRC_IN);
        flag = new int[2];
        t_id.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               checkID(t_id.getText().toString());
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
        t_repass.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(t_pass.getText().toString().equals(t_repass.getText().toString())) {
                    t_repass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icn_pw_pp, 0, R.drawable.icn_okay_v_copy, 0);
                    flag[1]=1;
                }
                else
                {
                    t_repass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icn_pw_pp, 0, R.drawable.icn_no_x, 0);
                    flag[1]=0;
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
                if(t_pass.getText().toString().length()==6)
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
    }
    public void Back(View view)
    {
        setResult(1);
        finish();
    }

    public void Next(View view)
    {

        if(flag[0]==1&&flag[1]==1)
        {
            insertToDatabase(t_id.getText().toString(),t_pass.getText().toString());
        }
        else {
            if (flag[0] == 0)
                Toast.makeText(getApplicationContext(), "ID가 중복됩니다", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다", Toast.LENGTH_LONG).show();
        }
    }


    private void insertToDatabase(String id, final String pass){

        class InsertData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            String id;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Join.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                if(s.equals("회원가입 완료")) {
                    sendMail(id);
                }

            }

            @Override
            protected String doInBackground(String... params) {

                DB db = new DB("join.php");
                id = (String)params[0];
                String pass = (String)params[1];
                String result = db.insert(id,pass);
                Log.d(result,"result");
                return result;
            }
        }


        InsertData task = new InsertData();
        task.execute(id, pass);
    }
    private void sendMail(String id){

        class sendAuthorMail extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Join.this,Authorization.class);
                setResult(0);
                startActivity(intent);
                finish();
            }

            @Override
            protected String doInBackground(String... params) {

                DB db = new DB("send.php");
                String id = (String)params[0];
                String result = db.dupli(id);
                Log.d(result,"result");
                return result;
            }
        }


        sendAuthorMail task = new sendAuthorMail();
        task.execute(id);
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
}

