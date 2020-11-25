package com.example.everyrefri;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class activity_14_member extends AppCompatActivity {

    private User user;
    private ImageButton ibt_back;
    private Button bt_follower,bt_following,bt_follow,bt_sendchat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_14_member);

        Intent intent =getIntent();
        user = getUser(intent);

        ibt_back=findViewById(R.id.ibt_back14);
        bt_follower=findViewById(R.id.bt_follower14);
        bt_following=findViewById(R.id.bt_following14);
        bt_follow=findViewById(R.id.bt_follow);
        bt_sendchat=findViewById(R.id.bt_sendchat);

        ibt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), activity_15_post.class);
                intent = setUser(intent);
                startActivityForResult(intent,15);
            }
        });

        bt_follower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 버튼 클릭시 아래 내용 수행
                Intent intent = new Intent(getApplicationContext(), activity_16_follower.class);
                intent = setUser(intent);
                startActivityForResult(intent,16);
                finish();
            }
        });

        bt_following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 버튼 클릭시 아래 내용 수행
                Intent intent = new Intent(getApplicationContext(), activity_17_following.class);
                intent = setUser(intent);
                startActivityForResult(intent,17);
                finish();
            }
        });

        bt_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 버튼 클릭시 아래 내용 수행
                Intent intent = new Intent(getApplicationContext(), activity_16_follower.class);
                intent = setUser(intent);
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