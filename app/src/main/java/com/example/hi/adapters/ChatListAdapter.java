package com.example.hi.adapters;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hi.R;
import com.example.hi.activitities.ActivityMessaging;
import com.example.hi.model.Chat;
import com.example.hi.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder>{

    private final ArrayList<Chat> chatList;
    private Context mContext;


    public ChatListAdapter(ArrayList<Chat> chatList, Context mContext) {
        this.chatList = chatList;
        this.mContext = mContext;
    }

    public class ChatListViewHolder extends RecyclerView.ViewHolder{

        TextView mUserName , mLastText,mLastTextTime, mUnreadBudge;
        LinearLayout mLayout;
        int chatSize = getItemCount();
        public ChatListViewHolder(View view){
            super(view);
            mUserName = view.findViewById(R.id.userName);
            mLastText = view.findViewById(R.id.last_text);
            mLastTextTime = view.findViewById(R.id.last_text_time);
            mUnreadBudge = view.findViewById(R.id.unread_messages_budge);
            mLayout = view.findViewById(R.id.chat_item_layout);
        }

    }


    @NonNull
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_chat_item,null,false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        ChatListViewHolder rcv = new ChatListViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatListViewHolder chatListViewHolder, final int i) {
        final String userId;
        final User[] mUser = new User[1];

        String receiverId = chatList.get(chatListViewHolder.getAdapterPosition()).getReceiverId();
        String senderId = chatList.get(chatListViewHolder.getAdapterPosition()).getSenderId();
        if (receiverId.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            userId = senderId;
        }else {
            userId = receiverId;
        }
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()){
                    User user = (User) child.getValue(User.class);
                    if (user.getUid().equals(userId)){
                        mUser[0] = user;
                        chatListViewHolder.mUserName.setText(mUser[0].getName());
                        //
                        chatListViewHolder.mUnreadBudge.setText(String.valueOf(chatListViewHolder.chatSize));
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        chatListViewHolder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UsersAdapter.openMessagingActivity(mUser[0],mContext);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.chatList.size();
    }



}
