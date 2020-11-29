package com.example.everyrefri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class activity_13_write extends AppCompatActivity {
//    String text = spinner.getSelectedItem().toString();
    private User user;
    private Spinner sp_category, sp_storage;
    private Button bt_choice;
    private ImageButton ibt_save;
    private ImageView iv_pic;
    private ArrayAdapter<String> adapter;
    private String buy, exp;
    private String[] items = {"채소류", "과일류", "인스턴트류", "기타"};
    private String[] methods = {"냉장", "냉동", "실온"};
    private Uri filePath;
    private EditText et_title , et_inst;
    private DatePicker dp_buy, dp_exp;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_13_write);

        // 객체 할당
        sp_category = findViewById(R.id.sp_category);
        sp_storage = findViewById(R.id.sp_storage);
        bt_choice = findViewById(R.id.bt_choice);
        ibt_save = findViewById(R.id.ibt_save);
        iv_pic = findViewById(R.id.iv_pic);
        et_title = findViewById(R.id.et_title);
        et_inst = findViewById(R.id.et_inst);
        dp_buy = findViewById(R.id.dp_buy);
        dp_exp = findViewById(R.id.dp_exp);


        // 이전 액티비티의 데이터 수신
        Intent intent =getIntent();
        user = getUser(intent);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, items
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        sp_category.setAdapter(adapter);
//        sp_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                textView.setText(items[position]);
//            }
//
//            public void onNothingSelected(AdapterView<?> parent) {
//                textView.setText("종류");
//            }
//        });

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, methods
        );
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_item);
        sp_storage.setAdapter(adapter1);
//        sp_storage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                textView.setText(methods[position]);
//            }
//
//            public void onNothingSelected(AdapterView<?> parent) {
//                textView.setText("방법");
//            }
//        });

        // 사진 선택 버튼 클릭시
        bt_choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //이미지를 선택
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), 0);
            }
        });

        ibt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //업로드
                upload();
            }
        });



        dp_buy.init(dp_buy.getYear(), dp_buy.getMonth(), dp_buy.getDayOfMonth(),
                new DatePicker.OnDateChangedListener() {
                    String  mon, day;
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        if (monthOfYear < 10)
                            mon = "0" + monthOfYear+1;
                        else
                            mon = Integer.toString(monthOfYear+1);
                        if (dayOfMonth < 10)
                            day = '0' + Integer.toString(dayOfMonth);
                        else
                            day = Integer.toString(dayOfMonth);

                        buy = year + "년 " + mon +"월 " + day + "일";

                    }
                });

        dp_exp.init(dp_exp.getYear(), dp_exp.getMonth(), dp_exp.getDayOfMonth(),
                new DatePicker.OnDateChangedListener() {
                    String  mon, day;
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (monthOfYear < 10)
                            mon = "0" + monthOfYear+1;
                        else
                            mon = Integer.toString(monthOfYear+1);
                        if (dayOfMonth < 10)
                            day = '0' + Integer.toString(dayOfMonth);
                        else
                            day = Integer.toString(dayOfMonth);

                        exp = year + "년 " + mon +"월 " + day + "일";
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
                iv_pic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
            StorageReference storageRef = storage.getReferenceFromUrl("gs://database-f0589.appspot.com/images").child(postName + ".png");
            //올라가거라...
            storageRef.putFile(filePath)
                    //이미지 업로드 성공시 -> 데이터도 저장
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // 데이터 저장
                            ref = FirebaseDatabase.getInstance().getReference().child("Posts").child(postName);

                            // post 데이터 생성
                            try{
                                HashMap<String, Object> postInfo = new HashMap<>();
                                postInfo.put("id", user.id);
                                postInfo.put("email", user.email);
                                postInfo.put("title", et_title.getText().toString());
                                postInfo.put("category", sp_category.getSelectedItem().toString());
                                postInfo.put("exp", exp);
                                postInfo.put("buy", buy);
                                postInfo.put("isSold", 0);
                                postInfo.put("storage", sp_storage.getSelectedItem().toString());
                                postInfo.put("inst", et_inst.getText().toString());

                                ref.setValue(postInfo);
                            }catch (Exception e){
                                Toast.makeText(getApplicationContext(), "데이터 생성 실패!", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            progressDialog.dismiss(); //업로드 진행 Dialog 상자 닫기
                            Toast.makeText(getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();

                            // 게시판으로 이동하기
                            Intent intent = new Intent(getApplicationContext(), activity_6_board.class);
                            intent = setUser(intent);
                            startActivityForResult(intent,6);//requestcode이게맞는지 다시 확인
                            finish();

                        }
                    })
                    //실패시
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
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

}