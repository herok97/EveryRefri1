package com.example.everyrefri;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class activity_6_board extends AppCompatActivity {

    private FirebaseAuth fireAuth;
    private ImageButton ibt_back;
    private SearchView searchView;//searchview이용
    private User user;
    private FloatingActionButton fab_write;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_6_board);

        ibt_back = findViewById(R.id.ibt_back6);
        searchView= findViewById(R.id.sv_board);
        fab_write = findViewById(R.id.fab_write);
        // 이전 액티비티의 데이터 수신
        Intent intent =getIntent();
        user = getUser(intent);

        ibt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // back 버튼 클릭시 main으로 이동(?)
                Intent intent = new Intent(getApplicationContext(), activity_4_main.class);
                intent = setUser(intent);
                startActivityForResult(intent,4);
            }
        });

        //좀더공부해봐야함..
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                TextView text = (TextView)findViewById(R.id.sv_board);
                return false;//입력받은 문자열 처리
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // 상품 올리기 버튼
        fab_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), activity_13_write.class);
                intent = setUser(intent);
                startActivityForResult(intent,13);
            }
        });

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

    private User getUser(Intent intent)
    {
        User user = new User(
                intent.getExtras().getString("id"),
                intent.getExtras().getString("email"),
                intent.getExtras().getInt("follower"),
                intent.getExtras().getInt("following"),
                intent.getExtras().getFloat("grade"));
        return user;
    }

}