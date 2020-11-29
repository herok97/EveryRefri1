package com.example.everyrefri;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class activity_8_myrefri extends AppCompatActivity {

    private FloatingActionButton fab_write;
    private User user;
    private ImageButton ibt_back, ibt_alarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_8_myrefri);

        // 객체 할당
        fab_write = findViewById(R.id.fab_write);
        ibt_back = findViewById(R.id.ibt_back8);
        ibt_alarm=findViewById(R.id.ibt_alarm8);

        // 이전 액티비티의 데이터 수신
        Intent intent =getIntent();
        user = getUser(intent);

        fab_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), activity_13_write.class);
                intent = setUser(intent);
                startActivityForResult(intent,13);
            }
        });

        ibt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // back 버튼 클릭시 main으로 이동(?)
                Intent intent = new Intent(getApplicationContext(), activity_4_main.class);
                intent = setUser(intent);
                startActivityForResult(intent,4);
            }
        });

        ibt_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), activity_9_alarm_list.class);
                intent = setUser(intent);
                startActivityForResult(intent,9);
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

    private User getUser(Intent intent)
    {
        User _user = new User(
                intent.getExtras().getString("userId"),
                intent.getExtras().getString("userEmail"),
                intent.getExtras().getInt("userFollower"),
                intent.getExtras().getInt("userFollowing"),
                intent.getExtras().getFloat("userGrade"));
        return _user;
    }

    private Intent setUser(Intent intent)
    {
        intent.putExtra("userId", user.id);
        intent.putExtra("userEmail", user.email);
        intent.putExtra("userFollower", user.follower);
        intent.putExtra("userFollowing", user.following);
        intent.putExtra("userGrade", user.grade);

        return intent;
    }














}