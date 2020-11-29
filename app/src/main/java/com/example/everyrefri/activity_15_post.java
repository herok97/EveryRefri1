package com.example.everyrefri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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

public class activity_15_post extends AppCompatActivity {

    private User user;
    private Post post;
    private ImageButton ibt_back, ibt_alarm, ibt_chat;
    private Button bt_ask;
    private ImageView iv_profile, iv_postItem;
    private FirebaseStorage storage;
    private TextView tv_category, tv_buy, tv_exp, tv_storage, tv_content, tv_title, tv_id;
    private DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_15_post);

        // 객체 할당
        ibt_back = findViewById(R.id.ibt_back15);
        ibt_alarm=findViewById(R.id.ibt_alarm15);
        ibt_chat=findViewById(R.id.ibt_chat15);
        bt_ask=findViewById(R.id.bt_ask);
        iv_profile=findViewById(R.id.iv_pro15);
        tv_category=findViewById(R.id.tv_category15);
        tv_buy=findViewById(R.id.tv_buydate);
        tv_exp=findViewById(R.id.tv_expdate);
        tv_storage=findViewById(R.id.tv_storage15);
        tv_content=findViewById(R.id.tv_add15);
        tv_title=findViewById(R.id.tv_title15);
        tv_id=findViewById(R.id.tv_name15);
        iv_postItem=findViewById(R.id.iv_picture);
        storage = FirebaseStorage.getInstance();


        // 이전 액티비티 데이터 수신
        Intent intent =getIntent();
        user = getUser(intent);
        String postName = intent.getExtras().getString("postName");
        Log.e("getPost함수시작, postName",postName);

        // email에 해당하는 유저 정보 가져오기.
        ref = FirebaseDatabase.getInstance().getReference().child("Posts").child(postName);
        ref.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot tasksSnapshot)
            {
                Post _post = new Post(
                        tasksSnapshot.child("postName").getValue().toString(),
                        tasksSnapshot.child("postId").getValue().toString(),
                        tasksSnapshot.child("postEmail").getValue().toString(),
                        tasksSnapshot.child("postTitle").getValue().toString(),
                        tasksSnapshot.child("postCategory").getValue().toString(),
                        tasksSnapshot.child("postBuy").getValue().toString(),
                        tasksSnapshot.child("postExp").getValue().toString(),
                        (boolean) tasksSnapshot.child("postIsSold").getValue(),
                        tasksSnapshot.child("postStorage").getValue().toString(),
                        tasksSnapshot.child("postInst").getValue().toString());
                post = _post;

                // 게시글 정보 가져오기
                get_profile(post.email);
                get_postImage(post.name);
                tv_category.setText(post.category);
                tv_buy.setText(post.buy);
                tv_exp.setText(post.exp);
                tv_storage.setText(post.storage);
                tv_content.setText(post.inst);
                tv_title.setText(post.title);
                tv_id.setText(post.id);

                // 만약 거래된 게시물이라면 버튼 못누르게
                if (post.isSold)
                {
                    bt_ask.setEnabled(false);
                    ibt_chat.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("게시물 정보를 가져오지 못했습니다.", "!");
                Toast.makeText(getApplicationContext(), "게시물 정보를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
            }
        });



        // 게시글

        ibt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), activity_6_board.class);
                intent = setUser(intent);
                startActivityForResult(intent,6);
            }
        });

        ibt_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), activity_9_alarm_list.class);
                intent = setUser(intent);
                startActivityForResult(intent,9);
            }
        });

        ibt_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), activity_10_chat_room.class);
                intent = setUser(intent);
                startActivityForResult(intent, 10);
            }
        });
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
                iv_profile.setImageBitmap(bit);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getApplicationContext(), "프로필 사진을 가져오지 못했습니다.." + exception.toString(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void get_postImage(String postName)
    {
        // 게시물 사진 가져오기
        StorageReference storageRef = storage.getReferenceFromUrl("gs://database-f0589.appspot.com/images").child(postName + ".png");
        final long ONE_MEGABYTE = 2048 * 2048;
        storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bit = BitmapFactory.decodeByteArray( bytes , 0 , bytes.length);
                iv_postItem.setImageBitmap(bit);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getApplicationContext(), "게시물의 사진을 가져오지 못했습니다.." + exception.toString(),Toast.LENGTH_LONG).show();
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

    private User getUser(Intent intent)
    {
        User _user = new User(
                intent.getExtras().getString("userId"),
                intent.getExtras().getString("userEmail"),
                intent.getExtras().getInt("userFollower"),
                intent.getExtras().getInt("userFollowing"),
                intent.getExtras().getFloat("userGrade"));
        return _user;
    }

    private Intent setUser(Intent intent)
    {
        intent.putExtra("userId", user.id);
        intent.putExtra("userEmail", user.email);
        intent.putExtra("userFollower", user.follower);
        intent.putExtra("userFollowing", user.following);
        intent.putExtra("userGrade", user.grade);

        return intent;
    }
}