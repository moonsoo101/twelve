package com.example.wisebody.twelve;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

/**
 * Created by wisebody on 2016. 7. 1..
 */
public class Dialog_Authorization extends Activity {
    TextView textView;
    Boolean star;
    protected void onCreate(Bundle Instance)
    {
        super.onCreate(Instance);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_authorization);
        Intent getIntent = getIntent();
        star = getIntent.getBooleanExtra("star",false);
        textView = (TextView) findViewById(R.id.text);
        if(star)
            textView.setText("가입이 완료되었습니다.\n당신의 팬들에게 빨리 알리세요. 12시간 내에 1000명 이상의 유저들이 당신이\n스타임을 인정해야 합니다.\n로그인 하시면 현재 상황을 볼 수 있습니다");
        else
            textView.setText("입력하신 메일주소로 인증메일을\n보내드렸습니다. 메일함에서 최종가입\n승인을 한 뒤 이용해주세요.");
    }
    protected void OK(View v)
    {
        setResult(0);
        finish();
    }
}
