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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class activity_10_chat_room extends AppCompatActivity {
    private User user;
    private DatabaseReference ref;
    private String chatName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_10_chat_room);

        // 이전 액티비티의 데이터 수신
        Intent intent =getIntent();
        user = new User();
        user.getUserFromIntent(intent);
        chatName = intent.getExtras().getString("chatName");
        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        RecyclerView recyclerView = findViewById(R.id.rv_chat) ;
        Log.e("recyclerView 할당","!");
        recyclerView.setLayoutManager(new LinearLayoutManager(this)) ;



        // 리사이클러뷰에 표시할 데이터 리스트 생성.
        ArrayList<String> msgs = new ArrayList<>();
        ref = FirebaseDatabase.getInstance().getReference().child("Chats").child(chatName);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot tasksSnapshot) {
                for (DataSnapshot dis : tasksSnapshot.getChildren()) {
                    msgs.add(dis.getKey());
                }

                // 게시물 시간순서에따라 정렬
                Collections.sort(msgs, new Descending());

                // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
                ChatAdapterClass adapter = new ChatAdapterClass(getApplicationContext(), msgs, chatName, user.email.substring(0, user.email.indexOf("@")));
                recyclerView.setAdapter(adapter);

                // 게시물  클릭 이벤트
                adapter.setOnItemListener(
                        new ChatAdapterClass.OnItemClickListener() {
                            @Override
                            public void onItemClick(View v, int pos, ArrayList<String> msgs) {
                            // 메세지 클릭시

                            }
                        }
                );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("데이터 리스트 생성 실패", "!");
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