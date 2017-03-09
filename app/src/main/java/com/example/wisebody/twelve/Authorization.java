package com.example.wisebody.twelve;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;



public class Authorization extends Activity {
    Boolean star;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);
        Intent getIntent = getIntent();
        star = getIntent.getBooleanExtra("star",false);
        Intent intent = new Intent(this,Dialog_Authorization.class);
        intent.putExtra("star",star);
        startActivityForResult(intent,0);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode==0)
        {
            if(resultCode==0)
                finish();
        }
    }

    }

