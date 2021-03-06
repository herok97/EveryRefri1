package com.example.everyrefri;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.rpc.context.AttributeContext;

import java.util.ArrayList;

public class CustomAdapterClass extends RecyclerView.Adapter<CustomAdapterClass.ViewHolder> {
    private DatabaseReference ref;
    private ArrayList<String> PostIds = null;
    private Context mContext;
    private User user;
    Resources mResources;

    public interface OnItemClickListener {
        void onItemClick(View v, int pos, ArrayList<String> PostIds);
    }

    private OnItemClickListener mListener = null;

    public void setOnItemListener(OnItemClickListener listener)
    {
        this.mListener = listener;
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_id_recy, tv_grade_recy, tv_time_recy, tv_title_recy, tv_content_recy;
        ImageView iv_pic_recy, iv_profile_recy;
        FloatingActionButton fab_moveToPost;



        ViewHolder(View itemView) {
            super(itemView);
            // 뷰 객체에 대한 참조. (hold strong reference)
            tv_id_recy = itemView.findViewById(R.id.tv_id_recy_chat_list);
            tv_title_recy = itemView.findViewById(R.id.tv_title_recy);
            tv_content_recy = itemView.findViewById(R.id.tv_content_recy);
            iv_pic_recy = itemView.findViewById(R.id.iv_pic_recy);
            iv_profile_recy = itemView.findViewById(R.id.iv_pic_recy_chat_list);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION)
                    {
                        if(mListener != null)
                        {
                            mListener.onItemClick(v, pos, PostIds);
                        }
//                        String PostId = PostIds.get(pos);
//                        Intent intent = new Intent(v.getContext(), activity_15_post.class);
//                        intent.putExtra("postName", PostId);
//                        mContext.startActivity(intent.addFlags(FLAG_ACTIVITY_NEW_TASK));
                    }
                }
            });
        }

    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    CustomAdapterClass(Context mContext, ArrayList<String> _PostIds) {
        this.mContext = mContext;
        this.PostIds = _PostIds;
        Log.e("CustomAdapterClass객체 생성", "!");
        mResources = mContext.getResources();
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public CustomAdapterClass.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_in_recyclerview, parent, false);
        CustomAdapterClass.ViewHolder vh = new CustomAdapterClass.ViewHolder(view);

        return vh;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(CustomAdapterClass.ViewHolder holder, int position) {
        Log.e("onBindViewHolder 함수 호출", "!");
        String PostId = PostIds.get(position);
        Log.e("PostId", PostId);


        // 게시글 정보 가져오기
        ref = FirebaseDatabase.getInstance().getReference().child("Posts").child(PostId);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onDataChange(DataSnapshot tasksSnapshot) {

                try {
                    // 게시글 사진 가져오기
                    get_profile(PostId + ".png", holder.iv_pic_recy);
                    Log.e("게시글 사진 주소:", PostId + ".png");
                }catch (Exception e){

                }

                try {
                    // 게시글 텍스트 가져오기
                    holder.tv_title_recy.setText(tasksSnapshot.child("postTitle").getValue().toString());
                    Log.e("스냅샷종류",tasksSnapshot.getKey());
                    holder.tv_content_recy.setText(
                            "종류:" + tasksSnapshot.child("postCategory").getValue().toString() + "\n" +
                                    "보관방법:" + tasksSnapshot.child("postStorage").getValue().toString() + "\n" +
                                    "구매일자:" + tasksSnapshot.child("postBuy").getValue().toString()    + "\n" +
                                    "유통기한:" + tasksSnapshot.child("postExp").getValue().toString());
                    holder.tv_id_recy.setText(tasksSnapshot.child("postId").getValue().toString());
                }catch (Exception e){

                }

                // 프사 가져오기
                try {
                    get_profile(tasksSnapshot.child("postEmail").getValue().toString() + "_profile.PNG", holder.iv_profile_recy);
                    Log.e("프로필 사진 주소:", tasksSnapshot.child("postEmail").getValue().toString() + "_profile.PNG");
                }catch (Exception e){

                }




                Log.e("onDataChange 실행", "!");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void get_profile(String ImageName, ImageView profile)
    {
        // 프로필 사진 가져오기
        FirebaseStorage storage;
        storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference pathReference = storageRef.child("images/" + ImageName);
        Log.e("이미지 경로", "images/" + ImageName);
        final long ONE_MEGABYTE = 2048 * 2048;
        pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Log.e("이미지 가져오기 성공", "!");
                Bitmap bit = BitmapFactory.decodeByteArray( bytes , 0 , bytes.length);
                Log.e("bit 텍스트로", bit.toString());
                Log.e("bytes 텍스트로", bytes.toString());
                RoundedBitmapDrawable bitt = createRoundedBitmapDrawableWithBorder(bit);
                profile.setImageDrawable(bitt);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return PostIds.size();
    }

    private RoundedBitmapDrawable createRoundedBitmapDrawableWithBorder(Bitmap bitmap){
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        int borderWidthHalf = 10; // In pixels
        //Toast.makeText(mContext,""+bitmapWidth+"|"+bitmapHeight,Toast.LENGTH_SHORT).show();

        // Calculate the bitmap radius
        int bitmapRadius = Math.min(bitmapWidth,bitmapHeight)/4;

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
//        Paint borderPaint = new Paint();
//        borderPaint.setStyle(Paint.Style.STROKE);
//        borderPaint.setStrokeWidth(borderWidthHalf*2);
//        borderPaint.setColor(Color.rgb(34,47,119));


//        canvas.drawCircle(canvas.getWidth()/2, canvas.getWidth()/2, newBitmapSquareWidth/2, borderPaint);

        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(mResources, roundedBitmap);

        // Set the corner radius of the bitmap drawable
        roundedBitmapDrawable.setCornerRadius(bitmapRadius);

        roundedBitmapDrawable.setAntiAlias(true);
        // Return the RoundedBitmapDrawable
        return roundedBitmapDrawable;
    }
}

