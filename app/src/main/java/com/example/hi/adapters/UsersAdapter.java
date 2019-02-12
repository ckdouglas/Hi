package com.example.hi.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hi.R;
import com.example.hi.activitities.ActivityMessaging;
import com.example.hi.model.User;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserListViewHolder>{

    ArrayList<User> userArrayList;
    private static Context mContext;

    public UsersAdapter(ArrayList<User> mAllUsers, Context mContext) {
        this.userArrayList = mAllUsers;
        this.mContext  = mContext;
    }


    public class UserListViewHolder extends RecyclerView.ViewHolder{
        TextView userName;
        ImageView profileImage;
        TextView status;
        View view;
        /**
         * clickable layout
         * */

        LinearLayout mLayout;

        public UserListViewHolder(View view){
            super(view);
            this.view = view;
            this.userName = view.findViewById(R.id.userName);
            this.profileImage = view.findViewById(R.id.profile_image);
            this.status = view.findViewById(R.id.status);
            this.mLayout = view.findViewById(R.id.user_item_layout);
        }

    }


    @NonNull
    @Override
    public UserListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_user_item,null,false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        UserListViewHolder rcv = new UserListViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull final UserListViewHolder holder, int position) {
        User user = userArrayList.get(position);
        holder.userName.setText(user.getName());
        holder.status.setText(user.getStatus());
        if ((user.getProfile_URL()).equals("default")){
            holder.profileImage.setImageResource(R.drawable.ic_default_profile);
        }else {
            Glide.with(mContext).load(user.getProfile_URL()).into(holder.profileImage);
        }

        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * TODO: start Messaging Activity and passing user object(done)
                 * @params user_id, user name
                 * */
              openMessagingActivity(userArrayList.get(holder.getAdapterPosition()),mContext);
            }

        });
    }

    @Override
    public int getItemCount() {
        return this.userArrayList.size();
    }

    public static void openMessagingActivity(User user, Context mContext) {
        Intent intent = new Intent(mContext, ActivityMessaging.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("user", user);
        mContext.startActivity(intent);
    }
}
