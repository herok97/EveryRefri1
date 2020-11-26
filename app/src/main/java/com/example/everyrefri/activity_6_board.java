package com.example.everyrefri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
    private FloatingActionButton fab_write;
    private DatabaseReference ref;

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
        user = getUser(intent);


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
                CustomAdapterClass adapter = new CustomAdapterClass(PostIds);
                recyclerView.setAdapter(adapter) ;
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