package com.example.everyrefri;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintProperties;
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

public class ChatAdapterClass extends RecyclerView.Adapter<ChatAdapterClass.ViewHolder> {
    private DatabaseReference ref;
    private ArrayList<String> msgs = null;
    private String chatName;
    private String user;
    private String type;
    private FirebaseStorage storage;
    public interface OnItemClickListener {
        void onItemClick(View v, int pos, ArrayList<String> msgs);
    }

    private OnItemClickListener mListener = null;

    public void setOnItemListener(OnItemClickListener listener)
    {
        this.mListener = listener;
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_msg_counter, tv_time_counter, tv_id_counter;
        TextView tv_msg_self, tv_time_self, tv_id_self;
        TextView tv_msg_system, tv_time_system, tv_id_system;


        TextView tv_msg_ask, tv_time_ask, tv_waiting;
        ImageView iv_ask_pic;
        Button bt_yes, bt_no;


        LinearLayout ll_msg_counter, ll_msg_self, ll_msg_system, ll_msg_ask;



        ViewHolder(View itemView) {
            super(itemView);
            // 뷰 객체에 대한 참조. (hold strong reference)
            tv_msg_counter = itemView.findViewById(R.id.tv_msg_counter);
            tv_time_counter = itemView.findViewById(R.id.tv_time_counter);
            tv_id_counter = itemView.findViewById(R.id.tv_id_counter);
            ll_msg_counter = itemView.findViewById(R.id.ll_msg_counter);

            tv_msg_self = itemView.findViewById(R.id.tv_msg_self);
            tv_time_self = itemView.findViewById(R.id.tv_time_self);
            tv_id_self = itemView.findViewById(R.id.tv_id_self);
            ll_msg_self = itemView.findViewById(R.id.ll_msg_self);

            tv_msg_system = itemView.findViewById(R.id.tv_msg_system);
            tv_time_system = itemView.findViewById(R.id.tv_time_system);
            tv_id_system = itemView.findViewById(R.id.tv_id_system);
            ll_msg_system = itemView.findViewById(R.id.ll_msg_system);

            tv_msg_ask = itemView.findViewById(R.id.tv_msg_ask);
            tv_time_ask = itemView.findViewById(R.id.tv_time_ask);
            iv_ask_pic = itemView.findViewById(R.id.iv_ask_pic);
            ll_msg_ask = itemView.findViewById(R.id.ll_msg_ask);
            tv_waiting = itemView.findViewById(R.id.tv_waiting);
            bt_yes = itemView.findViewById(R.id.bt_yes);
            bt_no = itemView.findViewById(R.id.bt_no);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION)
                    {
                        if(mListener != null)
                        {
                            mListener.onItemClick(v, pos, msgs);
                        }

                    }
                }
            });
        }
    }


    // 생성자에서 데이터 리스트 객체를 전달받음.
    ChatAdapterClass(Context mContext, ArrayList<String> _msgs, String _chatName, String _user) {
        this.msgs = _msgs;
        this.chatName = _chatName;
        this.user = _user;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public ChatAdapterClass.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.msg_in_chatroom, parent, false);
        ChatAdapterClass.ViewHolder vh = new ChatAdapterClass.ViewHolder(view);

        return vh;
    }


    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(ChatAdapterClass.ViewHolder holder, int position) {
        String msg = msgs.get(position);

        ref = FirebaseDatabase.getInstance().getReference().child("Chats").child(chatName).child(msg);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onDataChange(DataSnapshot tasksSnapshot) {
                try {
                    // 메세지 불러오기
                    type = tasksSnapshot.child("type").getValue().toString();
                    String time = tasksSnapshot.getKey().substring((int) tasksSnapshot.getKey().indexOf('_') + 1,
                            (int) tasksSnapshot.getKey().indexOf('_') + 3) + ":" +
                            tasksSnapshot.getKey().substring((int) tasksSnapshot.getKey().indexOf('_') + 3,
                                    (int) tasksSnapshot.getKey().indexOf('_') + 5);
                    String msg = tasksSnapshot.child("msg").getValue().toString();

                    if (type.equals("system")) {
                        Log.e("시스템 메세지입니다.", msg);
                        holder.ll_msg_system.setVisibility(View.VISIBLE);
                        holder.tv_msg_system.setText(msg);
                        holder.tv_time_system.setText(time);
                        holder.tv_id_system.setText(type);
//                        holder.ll_msg_system.setGravity(Gravity.CENTER_HORIZONTAL);

                    } else if (type.equals(user)) {
                        holder.ll_msg_self.setVisibility(View.VISIBLE);
                        Log.e("유저 메세지입니다.", msg);
                        holder.tv_msg_self.setText(msg);
                        holder.tv_time_self.setText(time);
                        holder.tv_id_self.setText(type);
//                        holder.ll_msg_self.setGravity(Gravity.RIGHT);

                        // 나눔 요청 메세지
                    } else if (type.indexOf("ask_") > -1) {

                        // 내가 보낸 나눔 요청 메세지인 경우
                        if (type.equals("ask" + "_" + user))
                        {
                            holder.ll_msg_ask.setVisibility(View.VISIBLE);
                            Log.e("나눔 요청 메세지입니다.", msg);
                            holder.tv_msg_ask.setText(msg);
                            holder.tv_time_ask.setText(time);
                            Log.e("postName",tasksSnapshot.child("postName").getValue().toString());
                            get_postImage(tasksSnapshot.child("postName").getValue().toString(), holder.iv_ask_pic);
                            holder.bt_no.setVisibility(View.GONE);
                            holder.bt_yes.setVisibility(View.GONE);
                        }
                        // 상대방이 보낸 나눔 요청 메세지인 경우
                        else{
                            holder.ll_msg_ask.setVisibility(View.VISIBLE);
                            holder.tv_waiting.setVisibility(View.GONE);
                            Log.e("나눔 요청 메세지입니다.", msg);
                            holder.tv_msg_ask.setText(msg);
                            holder.tv_time_ask.setText(time);
                            Log.e("postName",tasksSnapshot.child("postName").getValue().toString());
                            get_postImage(tasksSnapshot.child("postName").getValue().toString(), holder.iv_ask_pic);
                        }


                    } else {
                        holder.ll_msg_counter.setVisibility(View.VISIBLE);
                        Log.e("상대방의 메세지입니다.", msg);
                        holder.tv_msg_counter.setText(msg);
                        holder.tv_time_counter.setText(time);
                        holder.tv_id_counter.setText(type);
//                        holder.ll_msg_counter.setGravity(Gravity.LEFT);
                    }

                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("오류!","chatAdapterClass 에서 onDataChange");
            }
        });
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return msgs.size();
    }

    public void addMsg(String msg)
    {
        msgs.add(0, msg);

        notifyItemInserted(0);
    }

    private void get_postImage(String postName, ImageView imageView)
    {
        // 게시물 사진 가져오기
        storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://everyrefri.appspot.com/images").child(postName + ".png");
        final long ONE_MEGABYTE = 2048 * 2048;
        storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bit = BitmapFactory.decodeByteArray( bytes , 0 , bytes.length);
                imageView.setImageBitmap(bit);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("onFailure", "게시물 사진을 가져오는데 실패하였습니다.");
            }
        });
    }
}

