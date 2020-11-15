package com.example.everyrefri;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class activity_2_sign_up extends AppCompatActivity {

    // 액티비티 요소들 선언
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2_sign_up);

        // 액티비티 요소들 할당
        Button bt_next = findViewById(R.id.bt_next);
        ImageButton ibt_back = findViewById(R.id.ibt_back);
        EditText et_id = findViewById(R.id.et_id);
        EditText et_email = findViewById(R.id.et_email);
        EditText et_pass = findViewById(R.id.et_pass);
        EditText et_passconfirm = findViewById(R.id.et_passconfirm);

        // 다음 버튼 이벤트 처리
        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 버튼 클릭시 아래 내용 수행
                Intent intent = new Intent(getApplicationContext(), activity_3_sign_in.class);
                startActivityForResult(intent,2);
            }
        });

        // 뒤로가기 버튼 이벤트 처리
        ibt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 버튼 클릭시 아래 내용 수행
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivityForResult(intent,1);
            }
        });
    }
}