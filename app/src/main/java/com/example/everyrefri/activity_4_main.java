package com.example.everyrefri;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class activity_4_main extends AppCompatActivity {

    private FirebaseAuth fireAuth;
    private Button bt_follower,bt_following, bt_refrigerator;
    private ImageButton ibt_back,ibt_board,ibt_alarm,ibt_setting,ibt_chat;
    private TextView tv_div_num,tv_name;//사용자profile의 나눔수와 이름표시
    private ImageView iv_prof;//사용자의 사진표시

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_4_main);

        ibt_back = findViewById(R.id.ibt_back4);
        ibt_board = findViewById(R.id.ibt_list);
        ibt_alarm = findViewById(R.id.ibt_alarm);
        ibt_setting = findViewById(R.id.ibt_pfs);
        ibt_chat=findViewById(R.id.ibt_chat);
        bt_follower=findViewById(R.id.bt_follower);
        bt_following=findViewById(R.id.bt_following);
        bt_refrigerator=findViewById(R.id.bt_refri);
        tv_div_num = findViewById(R.id.tv_div_num);//사용자정보와연결
        tv_name =findViewById(R.id.tv_fullname);//사용자정보와연결
        iv_prof= findViewById(R.id.iv_profile);//사용자정보와연결


        // 이전 액티비티의 데이터 수신
        Intent intent =getIntent();
        String email = intent.getExtras().getString("email");

        // email에 해당하는 유저 정보 가져오기.
        User me = get_user(email);

        ibt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // back 버튼 클릭시 첫main으로 이동(?)
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivityForResult(intent,1);
            }
        });

        ibt_board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // board 이미지 버튼 클릭시 게시판이동
                Intent intent = new Intent(getApplicationContext(), activity_6_board.class);
                startActivityForResult(intent,6);//requestcode이게맞는지 다시 확인
            }
        });

        ibt_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // alarm 이미지 버튼 클릭시 alarm_list로 이동
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);//alarmlist로 나중에 수정!
                startActivityForResult(intent,1);
            }
        });

        ibt_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // setting이미지 버튼 클릭시 프로필 설정(내프로필)화면으로 이동
                Intent intent = new Intent(getApplicationContext(), activity_7_myprofile.class);
                startActivityForResult(intent,7);
            }
        });

        ibt_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // chat이미지 버튼 클릭시 chat_list로 이동
                Intent intent = new Intent(getApplicationContext(), activity_5_chat_list.class);
                startActivityForResult(intent,5);
            }
        });

        bt_follower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 버튼 클릭시 아래 내용 수행
                Intent intent = new Intent(getApplicationContext(), activity_5_chat_list.class);//follower목록페이지 만들고 변경
                startActivityForResult(intent,5);
            }
        });

        bt_following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 버튼 클릭시 아래 내용 수행
                Intent intent = new Intent(getApplicationContext(), activity_5_chat_list.class);//following목록페이지 만들고 변경
                startActivityForResult(intent,5);
            }
        });

        bt_refrigerator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 버튼 클릭시 아래 내용 수행
                Intent intent = new Intent(getApplicationContext(), activity_5_chat_list.class);//나의 냉장고페이지 만들고 변경
                startActivityForResult(intent,5);
            }
        });


    }

    private User get_user(String email)
    {
        return new User();
    }

}