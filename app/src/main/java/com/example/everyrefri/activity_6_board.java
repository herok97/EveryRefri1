package com.example.everyrefri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class activity_6_board extends AppCompatActivity {

    private FirebaseAuth fireAuth;
    private ImageButton ibt_back;
    private SearchView searchView;//searchview이용
    private User user;
    private FloatingActionButton fab_write, fab_moveToPost;
    private DatabaseReference ref;
    private Post post;
    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_6_board);

        ibt_back = findViewById(R.id.ibt_back6);
        searchView= findViewById(R.id.sv_board);
        fab_write = findViewById(R.id.fab_write);
        // 이전 액티비티의 데이터 수신
        Intent intent =getIntent();
        user = new User();
        user.getUserFromIntent(intent);


        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        RecyclerView recyclerView = findViewById(R.id.recyclerView) ;
        Log.e("recyclerView 할당","!");
        recyclerView.setLayoutManager(new LinearLayoutManager(this)) ;
        Log.e("recyclerView.setLayoutManager(new LinearLayoutManager(this))","!");




        // 리사이클러뷰에 표시할 데이터 리스트 생성.
        ArrayList<String> PostIds = new ArrayList<>();
        ref = FirebaseDatabase.getInstance().getReference().child("Posts");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot tasksSnapshot) {
                for (DataSnapshot dis : tasksSnapshot.getChildren()) {
                    PostIds.add(dis.getKey());
                    Log.e("조사할 PostId:", dis.getKey());
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


        ibt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // back 버튼 클릭시 main으로 이동(?)
                Intent intent = new Intent(getApplicationContext(), activity_4_main.class);
                intent = user.setUserToIntent(intent);
                startActivityForResult(intent,4);
                finish();
            }
        });


        // 상품 올리기 버튼
        fab_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), activity_13_write.class);
                intent = user.setUserToIntent(intent);
                startActivityForResult(intent,13);
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