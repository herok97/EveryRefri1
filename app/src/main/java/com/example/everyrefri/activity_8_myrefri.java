package com.example.everyrefri;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class activity_8_myrefri extends AppCompatActivity {

    private FloatingActionButton fab_write;
    private User user;
    private ImageButton ibt_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_8_myrefri);

        // 객체 할당
        fab_write = findViewById(R.id.fab_write);
        ibt_back = findViewById(R.id.ibt_back8);

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