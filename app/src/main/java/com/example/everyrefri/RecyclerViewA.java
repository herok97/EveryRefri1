package com.example.everyrefri;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


//recycler이용해 item에 있는 내용들 확인..좀더 많이 공부할 필요있음
//참고 https://godog.tistory.com/entry/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-%EB%A6%AC%EC%82%AC%EC%9D%B4%ED%81%B4%EB%9F%AC%EB%B7%B0RecyclerView-2
//파이어베이스 연결 내용도 공부해야함..
//싹다바꿔야할수도.....
public class RecyclerViewA extends RecyclerView.Adapter<RecyclerViewA.ViewHolder>{

    public class ViewHolder extends  RecyclerView.ViewHolder {
    public TextView tv_chat;
    public TextView tv_ID;
    public ImageView prof;

    public ViewHolder(View view) {
        super(view);
        this.tv_chat = view.findViewById(R.id.tv_chat);
        this.tv_ID = view.findViewById(R.id.tv_ID);
        this.prof = view.findViewById(R.id.prof);
    }
}
    public RecyclerViewA.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        RecyclerViewA.ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), position+"번 째!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }


}
