package com.example.everyrefri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;

public class activity_4_main extends AppCompatActivity {

    private DatabaseReference ref;
    private Button bt_follower,bt_following, bt_refrigerator;
    private ImageButton ibt_back,ibt_board,ibt_alarm,ibt_setting,ibt_chat;
    private Button ibt_myrefri;
    private TextView tv_div_num,tv_name, tv_grade;//사용자profile의 나눔수와 이름표시
    private ImageView iv_prof;//사용자의 사진표시
    private FirebaseStorage storage;

    private int follower;
    private int following;
    private float grade;
    private String email;
    private String id;
    private User user;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_4_main);

        ibt_myrefri = findViewById(R.id.bt_refri);
        ibt_back = findViewById(R.id.ibt_back4);
        ibt_board = findViewById(R.id.ibt_list);
        ibt_alarm = findViewById(R.id.ibt_alarm);
        ibt_setting = findViewById(R.id.ibt_pfs);
        ibt_chat=findViewById(R.id.ibt_chat);
        bt_follower=findViewById(R.id.bt_follower);
        bt_following=findViewById(R.id.bt_following);
        tv_div_num = findViewById(R.id.tv_div_num);//사용자정보와연결
        tv_name =findViewById(R.id.tv_name);//사용자정보와연결
        tv_grade =findViewById(R.id.tv_grade);//사용자정보와연결
        iv_prof= findViewById(R.id.iv_profile);//사용자정보와연결

        // 파이어베이스 스토리지
        storage = FirebaseStorage.getInstance();

        // 이전 액티비티의 데이터 수신
        Intent intent = getIntent();
        email = intent.getExtras().getString("userEmail");

        // 프로필 사진 가져오기
        get_profile(email);
        
        // email에 해당하는 유저 정보 가져오기.
        ref = FirebaseDatabase.getInstance().getReference().child("Users").child(email.substring(0,email.indexOf("@")));
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot tasksSnapshot) {
                    follower = new Integer(tasksSnapshot.child("userFollower").getValue().toString());
                    following = new Integer(tasksSnapshot.child("userFollowing").getValue().toString());
                    grade = new Float(tasksSnapshot.child("userGrade").getValue().toString());
                    id = (String) tasksSnapshot.child("userId").getValue();
                    bt_follower.setText("팔로워\n" + follower);
                    bt_following.setText("팔로잉\n" + following);
                    tv_grade.setText(String.valueOf(grade));
                    tv_name.setText(id);
                    user = new User(id, email, follower, following, grade);
                    Log.e("onDataChange 실행", "!");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "사용자 정보를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
                Log.e(error.toString(), error.toString());
            }
        });

        ibt_myrefri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // board 이미지 버튼 클릭시 게시판이동
                Intent intent = new Intent(getApplicationContext(), activity_8_myrefri.class);
                intent = user.setUserToIntent(intent);
                startActivityForResult(intent,6);//requestcode이게맞는지 다시 확인
                finish();
            }
        });

        ibt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // back 버튼 클릭시 로그인 화면으로 이동

                //로그인 화면으로 이동하면서, 저장된 로그인 정보를 지워야함
                preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                editor = preferences.edit();
                editor.putString("userEmail", "");
                editor.putString("userPass", "");
                editor.commit();

                Intent intent = new Intent(getApplicationContext(), activity_3_sign_in.class);
                startActivityForResult(intent,1);
            }
        });

        ibt_board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // board 이미지 버튼 클릭시 게시판이동
                Intent intent = new Intent(getApplicationContext(), activity_6_board.class);
                intent = user.setUserToIntent(intent);
                startActivityForResult(intent,6);//requestcode이게맞는지 다시 확인
                finish();
            }
        });

        ibt_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // alarm 이미지 버튼 클릭시 alarm_list로 이동
                Intent intent = new Intent(getApplicationContext(), activity_9_alarm_list.class);
                intent = user.setUserToIntent(intent);
                startActivityForResult(intent,9);
                finish();
            }
        });

        ibt_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // setting이미지 버튼 클릭시 프로필 설정(내프로필)화면으로 이동
                Intent intent = new Intent(getApplicationContext(), activity_7_myprofile.class);
                intent = user.setUserToIntent(intent);
                startActivityForResult(intent,7);
                finish();
            }
        });

        ibt_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // chat이미지 버튼 클릭시 chat_list로 이동
                Intent intent = new Intent(getApplicationContext(), activity_5_chat_list.class);
                intent = user.setUserToIntent(intent);
                startActivityForResult(intent,5);
                finish();
            }
        });

        bt_follower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 버튼 클릭시 아래 내용 수행
                Intent intent = new Intent(getApplicationContext(), activity_16_follower.class);//follower목록페이지 만들고 변경
                intent = user.setUserToIntent(intent);
                startActivityForResult(intent,16);
                finish();
            }
        });

        bt_following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 버튼 클릭시 아래 내용 수행
                Intent intent = new Intent(getApplicationContext(), activity_17_following.class);//following목록페이지 만들고 변경
                intent = user.setUserToIntent(intent);
                startActivityForResult(intent,17);
                finish();
            }
        });

//        bt_refrigerator.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) { // 버튼 클릭시 아래 내용 수행
//                Intent intent = new Intent(getApplicationContext(), activity_8_myrefri.class);//나의 냉장고페이지 만들고 변경
//                intent = user.setUserToIntent(intent);
//                startActivityForResult(intent,8);
//                finish();
//            }
//        });


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

    private void get_profile(String email)
    {
        // 프로필 사진 가져오기
        StorageReference storageRef = storage.getReference();
        StorageReference pathReference = storageRef.child("images/" + email + "_profile.PNG");
        final long ONE_MEGABYTE = 2048 * 2048;
        pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bit = BitmapFactory.decodeByteArray( bytes , 0 , bytes.length);
                Log.e("bit 텍스트로", bit.toString());
                Log.e("bytes 텍스트로", bytes.toString());
                iv_prof.setImageBitmap(bit);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getApplicationContext(), "프로필 사진을 가져오지 못했습니다.." + exception.toString(),Toast.LENGTH_LONG).show();
            }
        });
    }


    private void log_last_login(DatabaseReference ref)
    {
        // 현재시간을 msec 으로 구한다.
        long now = System.currentTimeMillis();
        // 현재시간을 date 변수에 저장한다.
        Date date = new Date(now);
        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        // nowDate 변수에 값을 저장한다.
        String formatDate = sdfNow.format(date);
        ref.child("last").setValue(formatDate);
    }
}