package com.example.everyrefri;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;


//파이어베이스에서 이미지 뷰로 보여서 선택하는것같은데..후...
public class activity_12_profile_change extends AppCompatActivity {

    private User user;
    private ImageButton ibt_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_12_profile_change);

        Intent intent =getIntent();
        user = getUser(intent);
        ibt_back=findViewById(R.id.ibt_back12);

        ibt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), activity_7_myprofile.class);
                intent = setUser(intent);
                startActivityForResult(intent,7);
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