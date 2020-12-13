package com.example.everyrefri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

public class activity_10_chat_room extends AppCompatActivity {
    private User user;
    private DatabaseReference ref;
    private String chatName;
    private ImageButton ib_send, ib_back;
    private EditText et_message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_10_chat_room);
        ib_send = findViewById(R.id.ibt_send);
        ib_back = findViewById(R.id.ibt_back10);

        et_message = findViewById(R.id.et_chat);
        // 이전 액티비티의 데이터 수신
        Intent intent =getIntent();
        user = new User();
        user.getUserFromIntent(intent);
        chatName = intent.getExtras().getString("chatName");
        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        RecyclerView recyclerView = findViewById(R.id.rv_chat) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));

        // 뒤로가기
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), activity_5_chat_list.class);
                intent = user.setUserToIntent(intent);
                startActivityForResult(intent,4);
            }
        });

        // 입력버튼
        ib_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = et_message.getText().toString();
                if(msg.isEmpty())
                {
                    // 메세지가 비어있으면 아무것도 안함
                }
                else
                {
                    TimeZone tz;                                        // 객체 생성
                    DateFormat dateFormat = new SimpleDateFormat("MMdd_HHmmss", Locale.KOREAN);
                    tz = TimeZone.getTimeZone("Asia/Seoul");  // TimeZone에 표준시 설정
                    dateFormat.setTimeZone(tz);                    //DateFormat에 TimeZone 설정

                    Date date = new Date();                        // 현재 날짜가 담긴 Date 객체 생성
                    HashMap<String, String> message = new HashMap<String, String>();
                    message.put("msg", msg);
                    message.put("type", user.email.substring(0, user.email.indexOf("@")));
                    ref.child(dateFormat.format(date)).setValue(message);
                    et_message.setText("");
                }


            }
        });


        // 리사이클러뷰에 표시할 데이터 리스트 생성.
        ArrayList<String> msgs = new ArrayList<>();
        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        ChatAdapterClass adapter = new ChatAdapterClass(getApplicationContext(), msgs, chatName, user.email.substring(0, user.email.indexOf("@")));
        recyclerView.setAdapter(adapter);

        ref = FirebaseDatabase.getInstance().getReference().child("Chats").child(chatName);
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String s) {
                String msg = snapshot.getKey();
                Log.e("getKey", msg);
                adapter.addMsg(msg);
                recyclerView.scrollToPosition(0);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError e) {

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