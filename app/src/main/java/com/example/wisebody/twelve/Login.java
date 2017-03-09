package com.example.wisebody.twelve;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.wisebody.twelve.PagerMenu.MainActivity;

import java.io.Serializable;

public class Login extends Activity implements Serializable {
    private EditText editId;
    private EditText editPassword;
    Button starJoin;
    Button fanJoin;
    String Json;
    RelativeLayout mainLayout;
    ScrollView scrollView;
    private final int MY_PERMISSION_REQUEST_STORAGE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);
        scrollView = (ScrollView) findViewById(R.id.scrollcontainer);
        mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });
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
        editId = (EditText) findViewById(R.id.edit_id);
        editPassword = (EditText)findViewById(R.id.edit_pass);
        editId.getBackground().setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
        editPassword.getBackground().setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
        starJoin = (Button) findViewById(R.id.starJoin);
        starJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this,Agree.class);
                intent.putExtra("starOrfan","star");
                //intent.putExtra(“text”,String.valueOf(editText.getText()));
                startActivity(intent);
            }
        });
        fanJoin = (Button) findViewById(R.id.fanJoin);
        fanJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this,Agree.class);
                intent.putExtra("starOrfan","fan");
                //intent.putExtra(“text”,String.valueOf(editText.getText()));
                startActivity(intent);

            }
        });
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        checkPermission();

    }
    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermission() {
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to write the permission.
                Toast.makeText(this, "Read/Write external storage", Toast.LENGTH_SHORT).show();
            }

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO},
                    MY_PERMISSION_REQUEST_STORAGE);

            // MY_PERMISSION_REQUEST_STORAGE is an
            // app-defined int constant

        } else {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED
                        && grantResults[3] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! do the
                    // calendar task you need to do.

                } else {


                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                break;
        }
    }
    public void login(View view){
        String id = editId.getText().toString();
        String pass = editPassword.getText().toString();
        searchToDatabase(id, pass);
    }

    private void searchToDatabase(String id, String pass){

        class searchData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            String id;
            String pass;
            /*WeakReference<Activity> mActivityReference;

            public searchData(Activity activity){
                this.mActivityReference = new WeakReference<Activity>(activity);

            }*/

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Login.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();

                if(s.equals("일반인증")) {
                    Log.d(id,"login");
                    User loginUser = new User(id,pass);
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    intent.putExtra("loginUser",loginUser);
                    editId.setText("");
                    editPassword.setText("");
                    startActivity(intent);
                }
                else if(s.equals("스타인증")) {
                    Log.d(id,"login");
                    User loginUser = new User(id,pass);
                    Boolean star = true;
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    intent.putExtra("loginUser",loginUser);
                    intent.putExtra("star",star);
                    editId.setText("");
                    editPassword.setText("");
                    startActivity(intent);
                }  else if(s.equals("스타미인증")) {
                    Log.d(id,"login");
                    User loginUser = new User(id,pass);
                    Boolean star = true;
                    Intent intent = new Intent(Login.this, NonAuthor.class);
                    intent.putExtra("loginUser",loginUser);
                    intent.putExtra("star",star);
                    editId.setText("");
                    editPassword.setText("");
                    startActivity(intent);
                }

            }
            @Override
            protected String doInBackground(String... params) {

                id = (String) params[0];
                pass = (String) params[1];
                DB db = new DB("login.php");
                String result = db.search(id, pass);
                Log.d(result,"result");
                return result;
            }
        }
        //searchData task = new searchData(Application.this);
        searchData task = new searchData();
        task.execute(id, pass);
    }
    
}