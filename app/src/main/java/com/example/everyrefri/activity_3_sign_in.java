package com.example.everyrefri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

    // 액티비티 요소들 선언
    private FirebaseAuth fireAuth;
    private EditText et_email, et_pass;
    private Button bt_next;
    private ImageButton ibt_back;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3_sign_in);

        bt_next = findViewById(R.id.bt_next3);
        et_email = findViewById(R.id.et_email3);
        et_pass = findViewById(R.id.et_pass3);
        ibt_back = findViewById(R.id.ibt_back3);

        // 저장된 정보 가져오기
        String[] email_and_pass = load_login_info();
        String email = email_and_pass[0];
        String pass = email_and_pass[1];

        if (!email.isEmpty() && !pass.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "저장된 로그인 정보가 있습니다.",Toast.LENGTH_SHORT).show();
            et_email.setText(email);
            et_pass.setText(pass);
            login(email, pass);
        }

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

        // 뒤로가기 버튼 이벤트 처리
        ibt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 버튼 클릭시 아래 내용 수행
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
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

    private void login(String email, String pass)
    {
        fireAuth = FirebaseAuth.getInstance();
        fireAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //로그인 정보 저장
                            save_login_info(email, pass);

                            // 로그인 성공 메세지 출력
                            Toast.makeText(getApplicationContext(),"로그인 성공",Toast.LENGTH_SHORT).show();

                            // 유저 정보 전달 객체 생성
                            Intent intent = new Intent(getApplicationContext(), activity_4_main.class);
                            intent.putExtra("email", email);
                            intent.putExtra("id", "");
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

    private void save_login_info(String email, String pass)
    {
        //기본 SharedPreferences 환경과 관련된 객체를 얻어옵니다.
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        // SharedPreferences 수정을 위한 Editor 객체를 얻어옵니다.
        editor = preferences.edit();
        editor.putString("email", email);
        editor.putString("pass", pass);
        editor.commit();
    }

    private String[] load_login_info()
    {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String email = preferences.getString("email", "");
        String pass = preferences.getString("pass","");
        String[] email_and_pass = {email, pass};
        return email_and_pass;
    }

}