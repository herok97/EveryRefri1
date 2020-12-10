package com.example.everyrefri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;


public class activity_8_myrefri extends AppCompatActivity {

    private FloatingActionButton fab_write;
    private User user;
    private ImageButton ibt_back, ibt_alarm;
    private DatabaseReference ref;

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
        user = new User();
        user.getUserFromIntent(intent);

        fab_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), activity_13_write.class);
                intent = user.setUserToIntent(intent);
                startActivityForResult(intent,13);
            }
        });

        ibt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // back 버튼 클릭시 main으로 이동(?)
                Intent intent = new Intent(getApplicationContext(), activity_4_main.class);
                intent = user.setUserToIntent(intent);
                startActivityForResult(intent,4);
            }
        });

        ibt_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), activity_9_alarm_list.class);
                intent = user.setUserToIntent(intent);
                startActivityForResult(intent,9);
            }
        });


        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        RecyclerView recyclerView = findViewById(R.id.rc_myrefr) ;
        Log.e("recyclerView 할당","!");
        recyclerView.setLayoutManager(new LinearLayoutManager(this)) ;


        // 리사이클러뷰에 표시할 데이터 리스트 생성.
        ArrayList<String> PostIds = new ArrayList<>();
        ref = FirebaseDatabase.getInstance().getReference().child("Posts");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot tasksSnapshot) {
                for (DataSnapshot dis : tasksSnapshot.getChildren()) {
                    Log.e("조사할 PostId:", dis.getKey());
                    if (dis.getKey().toString().substring(13).equals(user.email.substring(0, user.email.indexOf("@"))))
                    {
                        PostIds.add(dis.getKey());
                    }
                }

                // 게시물 시간순서에따라 정렬
                Collections.sort(PostIds, new Ascending());

                // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
                CustomAdapterClass adapter = new CustomAdapterClass(getApplicationContext(), PostIds);
                recyclerView.setAdapter(adapter) ;

                // 게시물  클릭 이벤트
                adapter.setOnItemListener(
                        new CustomAdapterClass.OnItemClickListener(){
                            @Override
                            public void onItemClick(View v, int pos, ArrayList<String> PostIds)
                            {
                                String PostId = PostIds.get(pos);
                                Intent intent = new Intent(getApplicationContext(), activity_15_post.class);
                                intent.putExtra("postName", PostId);
                                intent = user.setUserToIntent(intent);
                                startActivity(intent);
                            }
                        }
                );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("데이터 리스트 생성 실패","!");
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