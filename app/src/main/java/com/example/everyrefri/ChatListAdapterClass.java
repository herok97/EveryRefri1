package com.example.everyrefri;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ChatListAdapterClass extends RecyclerView.Adapter<ChatListAdapterClass.ViewHolder> {
    private DatabaseReference ref;
    private ArrayList<String> ChatIds = null;
    private Context mContext;
    private User user;

    public interface OnItemClickListener {
        void onItemClick(View v, int pos, ArrayList<String> ChatIds);
    }

    private OnItemClickListener mListener = null;

    public void setOnItemListener(OnItemClickListener listener)
    {
        this.mListener = listener;
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_id_recy_chat_list;
        ImageView iv_pic_recy_chat_list;



        ViewHolder(View itemView) {
            super(itemView);
            // 뷰 객체에 대한 참조. (hold strong reference)
            tv_id_recy_chat_list = itemView.findViewById(R.id.tv_id_recy_chat_list);
            iv_pic_recy_chat_list = itemView.findViewById(R.id.tv_title_recy);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION)
                    {
                        if(mListener != null)
                        {
                            mListener.onItemClick(v, pos, ChatIds);
                        }
                    }
                }
            });
        }

    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    ChatListAdapterClass(Context mContext, ArrayList<String> _ChatIds, User _user) {
        this.mContext = mContext;
        this.ChatIds = _ChatIds;
        this.user = _user;
        Log.e("CustomAdapterClass객체 생성", "!");
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public ChatListAdapterClass.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.chat_list, parent, false);
        ChatListAdapterClass.ViewHolder vh = new ChatListAdapterClass.ViewHolder(view);

        return vh;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(ChatListAdapterClass.ViewHolder holder, int position) {
        Log.e("onBindViewHolder 함수 호출", "!");
        String ChatId = ChatIds.get(position);
        Log.e("ChatId", ChatId);


        // 게시글 정보 가져오기
        ref = FirebaseDatabase.getInstance().getReference().child("Chats").child(ChatId);
        Log.e("onDataChange 전", "1");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @SuppressLint("LongLogTag")
            @Override
            public void onDataChange(DataSnapshot tasksSnapshot) {
                try {
                    Log.e("onDataChange 후", "1");
                    String counter;
                    // 채팅방 이름에서 상대방 이름보다 내 이름이 먼저 나오면
                    if (ChatId.indexOf(user.email.substring(0, user.email.indexOf("@"))) < ChatId.indexOf("_")) {
                        // 상대방의 이름은
                        counter = ChatId.substring(ChatId.indexOf("_") + 5);
                        Log.e("상대방이름", counter);

                    } else {
                        // 상대방의 이름은
                        counter = ChatId.substring(0, ChatId.indexOf("_"));
                        Log.e("상대방이름", counter);
                    }

                    // 상대방 이름 표시
                    holder.tv_id_recy_chat_list.setText(counter);

                } catch (Exception e) {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("오류","발생");
            }
        });
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return ChatIds.size();
    }
}

