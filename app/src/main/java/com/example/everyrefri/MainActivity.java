package com.example.everyrefri;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    // 액티비티 요소들 선언
    private Button bt_login;
    private Button bt_sign_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 액티비티 요소들 할당
        bt_login = findViewById(R.id.bt_sign_up);
        bt_sign_up = findViewById(R.id.bt_login);

        // 로그인 버튼 이벤트 처리 (onclick이랑 다른 방법이며, 오류가 더 적다고 합니다. 한 덩어리로 생각하시면 좋을 것 같아요)
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 버튼 클릭시 아래 내용 수행
                Intent intent = new Intent(getApplicationContext(), activity_2_sign_up.class);
                startActivityForResult(intent,2);
            }
        });

        // 회원가입 버튼 이벤트 처리
        bt_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 버튼 클릭시 아래 내용 수행
                Intent intent = new Intent(getApplicationContext(), activity_3_sign_in.class);
                startActivityForResult(intent,3);
            }
        });

    }


}