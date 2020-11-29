package com.example.everyrefri;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class activity_5_chat_list extends AppCompatActivity {

    private FirebaseAuth fireAuth;
    private ImageButton ibt_back;
    private RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_5_chat_list);

        ibt_back = findViewById(R.id.ibt_back5);
        recyclerView = findViewById(R.id.rcv_chat);
        recyclerView.setHasFixedSize(true);//recyclerview 일정한 크기 사용
        layoutManager = new LinearLayoutManager(this);//레이아웃타입

        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerViewA();
        recyclerView.setAdapter(adapter);


        ibt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // back 버튼 클릭시 main으로 이동(?)
                Intent intent = new Intent(getApplicationContext(), activity_4_main.class);
                startActivityForResult(intent,4);
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