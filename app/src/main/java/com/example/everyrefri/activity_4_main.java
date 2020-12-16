package com.example.everyrefri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
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
    private ImageButton ibt_myrefri;
    private TextView tv_div_num,tv_name, tv_grade;//사용자profile의 나눔수와 이름표시
    private ImageView iv_prof;//사용자의 사진표시
    private FirebaseStorage storage;
    Resources mResources;
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
        mResources = getResources();
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

        //출처: https://chocorolls.tistory.com/47 [초코롤의 개발이야기]
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
            public void onDataChange(@NonNull DataSnapshot tasksSnapshot) {
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
                RoundedBitmapDrawable bitt = createRoundedBitmapDrawableWithBorder(bit);
                iv_prof.setImageDrawable(bitt);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getApplicationContext(), "프로필 사진을 가져오지 못했습니다.." + exception.toString(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private RoundedBitmapDrawable createRoundedBitmapDrawableWithBorder(Bitmap bitmap){
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        int borderWidthHalf = 10; // In pixels
        //Toast.makeText(mContext,""+bitmapWidth+"|"+bitmapHeight,Toast.LENGTH_SHORT).show();

        // Calculate the bitmap radius
        int bitmapRadius = Math.min(bitmapWidth,bitmapHeight)/2;

        int bitmapSquareWidth = Math.min(bitmapWidth,bitmapHeight);
        //Toast.makeText(mContext,""+bitmapMin,Toast.LENGTH_SHORT).show();

        int newBitmapSquareWidth = bitmapSquareWidth+borderWidthHalf;
        //Toast.makeText(mContext,""+newBitmapMin,Toast.LENGTH_SHORT).show();
        Bitmap roundedBitmap = Bitmap.createBitmap(newBitmapSquareWidth,newBitmapSquareWidth,Bitmap.Config.ARGB_8888);

        // Initialize a new Canvas to draw empty bitmap
        Canvas canvas = new Canvas(roundedBitmap);

        // Draw a solid color to canvas
            canvas.drawColor(Color.RED);

        // Calculation to draw bitmap at the circular bitmap center position
        int x = borderWidthHalf + bitmapSquareWidth - bitmapWidth;
        int y = borderWidthHalf + bitmapSquareWidth - bitmapHeight;

            canvas.drawBitmap(bitmap, x, y, null);

        // Initializing a new Paint instance to draw circular border
        Paint borderPaint = new Paint();
            borderPaint.setStyle(Paint.Style.STROKE);
            borderPaint.setStrokeWidth(borderWidthHalf*2);
            borderPaint.setColor(Color.WHITE);

            canvas.drawCircle(canvas.getWidth()/2, canvas.getWidth()/2, newBitmapSquareWidth/2, borderPaint);

        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(mResources, roundedBitmap);

        // Set the corner radius of the bitmap drawable
            roundedBitmapDrawable.setCornerRadius(bitmapRadius);

            roundedBitmapDrawable.setAntiAlias(true);
        // Return the RoundedBitmapDrawable
        return roundedBitmapDrawable;
    }
}