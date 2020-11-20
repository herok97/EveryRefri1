package com.example.everyrefri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class activity_3_sign_in extends AppCompatActivity {

    private FirebaseAuth fireAuth;
    private EditText et_email, et_pass;
    private Button bt_next;
    private ImageButton ibt_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3_sign_in);

        bt_next = findViewById(R.id.bt_next3);
        et_email = findViewById(R.id.et_email3);
        et_pass = findViewById(R.id.et_pass3);
        ibt_back = findViewById(R.id.ibt_back3);

        // 다음 버튼 이벤트 처리
        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 버튼 클릭시 아래 내용 수행
                String email = myGetText(et_email);
                String pass = myGetText(et_pass);

                if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$"))
                {
                    Toast.makeText(getApplicationContext(), "이메일 형식에 맞게 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 제대로 정보를 입력했다면.
                else
                    login(email, pass);
            }
        });
    }

    private void login(String email, String pass)
    {
        fireAuth = FirebaseAuth.getInstance();
        fireAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(getApplicationContext(), activity_4_main.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(),"로그인 오류" + task.getException().toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public String myGetText(EditText et)
    {
        if (et.getText().toString().isEmpty())
        {
            Toast.makeText(getApplicationContext(), et.getHint().toString() + "를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return "";
        }
        else
            return et.getText().toString();
    }
}