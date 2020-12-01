package com.example.everyrefri;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class activity_14_member extends AppCompatActivity {

    private User user;
    private ImageButton ibt_back;
    private Button bt_follower,bt_following,bt_follow,bt_sendchat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_14_member);

        Intent intent =getIntent();
        user = new User();
        user.getUserFromIntent(intent);

        ibt_back=findViewById(R.id.ibt_back14);
        bt_follower=findViewById(R.id.bt_follower14);
        bt_following=findViewById(R.id.bt_following14);
        bt_follow=findViewById(R.id.bt_follow);
        bt_sendchat=findViewById(R.id.bt_sendchat);

        ibt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), activity_15_post.class);
                intent = user.setUserToIntent(intent);
                startActivityForResult(intent,15);
                finish();
            }
        });



        bt_follower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 버튼 클릭시 아래 내용 수행
                Intent intent = new Intent(getApplicationContext(), activity_16_follower.class);
                intent = user.setUserToIntent(intent);
                startActivityForResult(intent,16);
                finish();
            }
        });

        bt_following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 버튼 클릭시 아래 내용 수행
                Intent intent = new Intent(getApplicationContext(), activity_17_following.class);
                intent = user.setUserToIntent(intent);
                startActivityForResult(intent,17);
                finish();
            }
        });

        bt_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 버튼 클릭시 아래 내용 수행
                Intent intent = new Intent(getApplicationContext(), activity_16_follower.class);
                intent = user.setUserToIntent(intent);
                startActivityForResult(intent,5);
                finish();
            }
        });

        bt_sendchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 버튼 클릭시 아래 내용 수행
                Intent intent = new Intent(getApplicationContext(), activity_10_chat_room.class);
                startActivityForResult(intent,10);
                finish();
            }
        });


    }

    // 뒤로가기 버튼 두 번 눌러 앱 종료
    private long backBtnTime = 0;
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