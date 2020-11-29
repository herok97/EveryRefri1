package com.example.everyrefri;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class activity_15_post extends AppCompatActivity {

    private User user;
    private ImageButton ibt_back, ibt_alarm, ibt_chat;
    private Button bt_ask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_15_post);

        ibt_back = findViewById(R.id.ibt_back15);
        ibt_alarm=findViewById(R.id.ibt_alarm15);
        ibt_chat=findViewById(R.id.ibt_chat15);
        bt_ask=findViewById(R.id.bt_ask);

        Intent intent =getIntent();
        user = getUser(intent);

        ibt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), activity_6_board.class);
                intent = setUser(intent);
                startActivityForResult(intent,6);
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

        ibt_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), activity_10_chat_room.class);
                intent = setUser(intent);
                startActivityForResult(intent, 10);
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
                intent.getExtras().getString("id"),
                intent.getExtras().getString("email"),
                intent.getExtras().getInt("follower"),
                intent.getExtras().getInt("following"),
                intent.getExtras().getFloat("grade"));
        return _user;
    }

    private Intent setUser(Intent intent)
    {
        intent.putExtra("id", user.id);
        intent.putExtra("follower", user.follower);
        intent.putExtra("following", user.following);
        intent.putExtra("grade", user.grade);
        intent.putExtra("email", user.email);
        return intent;
    }

}