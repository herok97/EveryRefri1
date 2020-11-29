package com.example.everyrefri;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    // 액티비티 요소들 선언
    private Button bt_sign_in;
    private Button bt_sign_up;
    private long backBtnTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 액티비티 요소들 할당
        bt_sign_in = findViewById(R.id.bt_sign_up);
        bt_sign_up = findViewById(R.id.bt_sign_in);

        // 로그인 버튼 이벤트 처리 (onclick이랑 다른 방법이며, 오류가 더 적다고 합니다. 한 덩어리로 생각하시면 좋을 것 같아요)
        bt_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 버튼 클릭시 아래 내용 수행
                Intent intent = new Intent(getApplicationContext(), activity_2_sign_up.class);
                startActivityForResult(intent,2);
                finish();
            }
        });

        // 회원가입 버튼 이벤트 처리
        bt_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 버튼 클릭시 아래 내용 수행
                Intent intent = new Intent(getApplicationContext(), activity_3_sign_in.class);
                startActivityForResult(intent,3);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        long curTime = System.currentTimeMillis();
        long gapTime = curTime - backBtnTime;

        if (0 <= gapTime && 2000 >= gapTime) {
            ActivityCompat.finishAffinity(this);
            System.exit(0);
        } else {
            backBtnTime = curTime;
            Toast.makeText(this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }


}