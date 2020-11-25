package com.example.everyrefri;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class activity_7_myprofile extends AppCompatActivity {

    private FirebaseAuth fireAuth;
    private Button bt_follower,bt_following, bt_save,bt_pic,bt_location;
    private ImageButton ibt_back;
    private TextView tv_div_num,tv_name;//사용자profile의 나눔수와 이름표시
    private EditText et_self;//자신소개 input
    private ImageView iv_prof;//사용자의 사진표시
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_7_myprofile);

        bt_follower=findViewById(R.id.bt_follower7);
        bt_following=findViewById(R.id.bt_following7);
        bt_save=findViewById(R.id.bt_save);//et_self와 연결해서, 좀더 공부해보고 추가해야할것같음
        bt_pic=findViewById(R.id.bt_pic);
        bt_location=findViewById(R.id.bt_location);
        ibt_back=findViewById(R.id.ibt_back7);
        tv_div_num=findViewById(R.id.tv_div_num7);//사용자정보와 연결
        tv_name=findViewById(R.id.tv_mypro);//사용자정보와 연결
        et_self=findViewById(R.id.et_self);
        iv_prof=findViewById(R.id.iv_profile7);//사용자정보와 연결


        ibt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // back 버튼 클릭시 main으로 이동
                Intent intent = new Intent(getApplicationContext(), activity_4_main.class);
                startActivityForResult(intent,4);
            }
        });



        bt_follower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 버튼 클릭시 아래 내용 수행
                Intent intent = new Intent(getApplicationContext(), activity_16_follower.class);
                startActivityForResult(intent,16);
            }
        });

        bt_following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 버튼 클릭시 아래 내용 수행
                Intent intent = new Intent(getApplicationContext(), activity_17_following.class);
                startActivityForResult(intent,17);
            }
        });


        bt_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 버튼 클릭시 아래 내용 수행
                Intent intent = new Intent(getApplicationContext(), activity_12_profile_change.class);
                startActivityForResult(intent,12);
            }
        });

        bt_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 버튼 클릭시 아래 내용 수행
                Intent intent = new Intent(getApplicationContext(), activity_11_location.class);
                startActivityForResult(intent,11);
            }
        });
    }
}