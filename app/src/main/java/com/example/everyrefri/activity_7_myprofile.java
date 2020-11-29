package com.example.everyrefri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class activity_7_myprofile extends AppCompatActivity {
    private User user;
    private FirebaseAuth fireAuth;
    private Button bt_follower,bt_following, bt_save,bt_pic,bt_location;
    private ImageButton ibt_back;
    private TextView tv_div_num,tv_name;//사용자profile의 나눔수와 이름표시
    private EditText et_self;//자신소개 input
    private ImageView iv_prof;//사용자의 사진표시
    private FirebaseStorage storage;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_7_myprofile);

        bt_follower=findViewById(R.id.bt_follower7);
        bt_following=findViewById(R.id.bt_following7);
        bt_save=findViewById(R.id.bt_save);//et_self와 연결해서, 좀더 공부해보고 추가해야할것같음
        bt_pic=findViewById(R.id.bt_pic);
        bt_location=findViewById(R.id.bt_location);
        ibt_back=findViewById(R.id.ibt_back7);
        tv_div_num=findViewById(R.id.tv_div_num7);//사용자정보와 연결
        tv_name=findViewById(R.id.tv_mypro);//사용자정보와 연결
        et_self=findViewById(R.id.et_self);
        iv_prof=findViewById(R.id.iv_profile7);//사용자정보와 연결

        // 이전 액티비티의 데이터 수신
        Intent intent =getIntent();
        user = getUser(intent);


        // 프로필 사진 가져오기
        get_profile(user.email);

        ibt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // back 버튼 클릭시 main으로 이동
                Intent intent = new Intent(getApplicationContext(), activity_4_main.class);
                intent = setUser(intent);
                startActivityForResult(intent,4);
            }
        });



        bt_follower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 버튼 클릭시 아래 내용 수행
                Intent intent = new Intent(getApplicationContext(), activity_16_follower.class);
                startActivityForResult(intent,16);
            }
        });

        bt_following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 버튼 클릭시 아래 내용 수행
                Intent intent = new Intent(getApplicationContext(), activity_17_following.class);
                startActivityForResult(intent,17);
            }
        });


        bt_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 버튼 클릭시 아래 내용 수행
                //이미지를 선택
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), 0);
            }
        });

        bt_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 버튼 클릭시 아래 내용 수행
                Intent intent = new Intent(getApplicationContext(), activity_11_location.class);
                startActivityForResult(intent,11);
            }
        });
    }

    private User getUser(Intent intent)
    {
        User _user = new User(
                intent.getExtras().getString("id"),
                intent.getExtras().getString("email"),
                intent.getExtras().getInt("follower"),
                intent.getExtras().getInt("following"),
                intent.getExtras().getFloat("grade"));
        return _user;
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

    private void get_profile(String email)
    {
        // 프로필 사진 가져오기
        storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference pathReference = storageRef.child("images/" + email + "_profile.PNG");
        final long ONE_MEGABYTE = 1024 * 1024;
        pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bit = BitmapFactory.decodeByteArray( bytes , 0 , bytes.length);
                iv_prof.setImageBitmap(bit);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getApplicationContext(), "프로필 사진을 가져오지 못했습니다.." + exception.toString(),Toast.LENGTH_LONG).show();
            }
        });
    }

    //결과 처리
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //request코드가 0이고 OK를 선택했고 data에 뭔가가 들어 있다면
        if(requestCode == 0 && resultCode == RESULT_OK){
            filePath = data.getData();
            Log.d("TAG", "uri:" + String.valueOf(filePath));
            try {
                //Uri 파일을 Bitmap으로 만들어서 ImageView에 집어 넣는다.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                bitmap = resizeBitmap(bitmap);
                iv_prof.setImageBitmap(bitmap);

                // 이미지 업로드 및 기존 파일 삭제
                upload();
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Bitmap resizeBitmap(Bitmap original) {

        int resizeWidth = 256;

        double aspectRatio = (double) original.getHeight() / (double) original.getWidth();
        int targetHeight = (int) (resizeWidth * aspectRatio);
        Bitmap result = Bitmap.createScaledBitmap(original, resizeWidth, targetHeight, false);
        if (result != original) {
            original.recycle();
        }
        return result;
    }


    //upload the file
    private void upload() {
        //업로드할 파일이 있으면 수행
        if (filePath != null) {
            //업로드 진행 Dialog 보이기
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("업로드중...");
            progressDialog.show();

            //storage
            FirebaseStorage storage = FirebaseStorage.getInstance();

            //Unique한 파일명을 만들자.  파일명은  현재 날짜 + 시간 + 이메일 앞부분 + .png 형태로 구성
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmm");
            Date now = new Date();
            String postName = formatter.format(now) + user.email.substring(0, user.email.indexOf("@"));

            //storage 주소와 폴더 파일명을 지정해 준다.
            StorageReference storageRef = storage.getReferenceFromUrl("gs://database-f0589.appspot.com/images").child(user.email + "_profile.PNG");


            // 이전에 올린 데이터가 있으면
            if (!storageRef.getName().isEmpty())
            {
                storageRef.delete();
                Toast.makeText(getApplicationContext(), "기존 프로필 사진 삭제!", Toast.LENGTH_SHORT).show();
            }

            //올라가거라...
            storageRef.putFile(filePath)
                    //이미지 업로드 성공시 -> 데이터도 저장
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss(); //업로드 진행 Dialog 상자 닫기
                            Toast.makeText(getApplicationContext(), "프로필 사진 변경 완료!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    //실패시
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "프로필 사진 변경 실패!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    //진행중
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests") //이걸 넣어 줘야 아랫줄에 에러가 사라진다.
                                    double progress = (100 * taskSnapshot.getBytesTransferred()) /  taskSnapshot.getTotalByteCount();
                            //dialog에 진행률을 퍼센트로 출력해 준다
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "% ...");
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "파일을 먼저 선택하세요.", Toast.LENGTH_SHORT).show();
        }
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