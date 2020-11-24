package com.example.everyrefri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class activity_2_sign_up extends AppCompatActivity {

    // 액티비티 요소들 선언
    private FirebaseAuth fireAuth;
    private EditText et_id, et_email, et_pass, et_passconfirm;
    private Button bt_next;
    private ImageButton ibt_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2_sign_up);

        bt_next = findViewById(R.id.bt_next);
        ibt_back = findViewById(R.id.ibt_back);
        et_id = findViewById(R.id.et_id);
        et_email = findViewById(R.id.et_email);
        et_pass = findViewById(R.id.et_pass);
        et_passconfirm = findViewById(R.id.et_passconfirm);

        // 다음 버튼 이벤트 처리
        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 버튼 클릭시 아래 내용 수행
                String email = myGetText(et_email);
                String pass = myGetText(et_pass);
                String id = myGetText(et_id);
                String passconfirm = myGetText(et_passconfirm);

                // 입력한 비밀번호와 비밀번호 확인이 다르다면
                if (!pass.equals(passconfirm))
                {
                    Toast.makeText(getApplicationContext(), "비밀번호와 비밀번호 확인이 다릅니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 이메일 형식이 맞지 않다면
                else if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$"))
                {
                    Toast.makeText(getApplicationContext(), "이메일 형식에 맞게 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 제대로 정보를 입력했다면.
                else
                {
                    signUp(id, email, pass);
                }

            }
        });

        // 뒤로가기 버튼 이벤트 처리
        ibt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 버튼 클릭시 첫메인으로
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivityForResult(intent,1);
            }
        });
    }


    private String myGetText(EditText et)
    {
        if (et.getText().toString().isEmpty())
        {
            Toast.makeText(getApplicationContext(), et.getHint().toString() + "를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return "";
        }
        else
            return et.getText().toString();
    }

    private void signUp(String id, String email, String pass){
        // 진행 상황 알려주기
        final ProgressDialog mDialog = new ProgressDialog(this);
        mDialog.setMessage("가입중..잠시만 기다려주세요.");
        mDialog.show();

        //파이어베이스에 신규계정 등록하기
        // Initialize Firebase Auth
        fireAuth = FirebaseAuth.getInstance();
        fireAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(
                this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //가입 성공시
                        mDialog.dismiss();
                        if (task.isSuccessful()) {

                            FirebaseUser user = fireAuth.getCurrentUser();
                            String email = user.getEmail();
                            String name = id;

                            //해쉬맵 테이블을 파이어베이스 데이터베이스에 저장
                            HashMap<String, Object> userInfo = new HashMap<>();
                            // 현재시간을 msec 으로 구한다.
                            long now = System.currentTimeMillis();
                            // 현재시간을 date 변수에 저장한다.
                            Date date = new Date(now);
                            // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
                            SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                            // nowDate 변수에 값을 저장한다.
                            String formatDate = sdfNow.format(date);

                            userInfo.put("email", email);
                            userInfo.put("id", id);
                            userInfo.put("pass", pass);
                            userInfo.put("start", formatDate);

                            userInfo.put("following", 0);
                            userInfo.put("follower", 0);
                            userInfo.put("grade", 0);


                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("Users");
                            reference.child(email.substring(0, email.indexOf("@"))).setValue(userInfo);


                            //가입이 이루어져을시 가입 화면을 빠져나감.
                            Intent intent = new Intent(getApplicationContext(), activity_3_sign_in.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(getApplicationContext(), "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "회원가입에 실패하셨습니다." + task.getException().toString(), Toast.LENGTH_SHORT).show();
                            return;  //해당 메소드 진행을 멈추고 빠져나감.
                        }
                    }});
    }
}