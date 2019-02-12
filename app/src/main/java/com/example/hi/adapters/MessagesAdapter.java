package com.example.hi.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.hi.R;
import com.example.hi.model.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageListViewHolder>{

    private ArrayList<Message> messageArrayList;
    private Context mContext;
    private FirebaseUser mUser;
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    public MessagesAdapter(ArrayList<Message> mAllMessages, Context mContext) {
        this.messageArrayList = mAllMessages;
        this.mContext  = mContext;
    }

    public class MessageListViewHolder extends RecyclerView.ViewHolder{

        TextView message, time, read_report;
        View view;

        /**
         * clickable layout
         * */
        RelativeLayout mLayout;

        public MessageListViewHolder(View view){
            super(view);
            this.view = view;
            this.message = view.findViewById(R.id.message);
            this.time = view.findViewById(R.id.time);
            this.read_report = view.findViewById(R.id.read_report);
            this.mLayout = view.findViewById(R.id.message_item_layout);
        }

    }


    @NonNull
    @Override
    public MessageListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int vieType) {
        MessageListViewHolder rcv;
         if (vieType == MSG_TYPE_RIGHT){
            View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_message_item_right,null,false);
            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutView.setLayoutParams(lp);
            rcv = new MessageListViewHolder(layoutView);
        }else {
            View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_message_item_left,null,false);
            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutView.setLayoutParams(lp);
            rcv = new MessageListViewHolder(layoutView);
        }
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull final MessageListViewHolder holder, int position) {
        Message message = messageArrayList.get(position);
        holder.message.setText(message.getMessage());
        holder.time.setText(message.getTime_sent());
        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return this.messageArrayList.size();
    }

    @Override
    synchronized public int getItemViewType(int position) {
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        if ((messageArrayList.get(position).getSenderId()).equals(mUser.getUid()))
            return MSG_TYPE_RIGHT;
        return MSG_TYPE_LEFT;

    }
}
