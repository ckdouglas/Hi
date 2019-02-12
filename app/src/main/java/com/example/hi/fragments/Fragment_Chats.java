package com.example.hi.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.hi.R;
import com.example.hi.activitities.ActivityNewChat;
import com.example.hi.adapters.ChatListAdapter;
import com.example.hi.model.Chat;
import com.example.hi.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Fragment_Chats extends Fragment implements View.OnClickListener {


    private String TAG = "Chat Fragment";
    private FloatingActionButton btn_new_chat;
    private FirebaseUser mUser;
    private DatabaseReference mDbRef;

    private RecyclerView mChatList;
    private RecyclerView.Adapter mChatListAdapter;
    private RecyclerView.LayoutManager mChatListLayoutManager;
    ArrayList<Chat> chatList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats,container,false);
        mDbRef = FirebaseDatabase.getInstance().getReference();
        setHasOptionsMenu(true);
        Log.d(TAG, "onCreate:loading chats layout");
        btn_new_chat = (FloatingActionButton) view.findViewById(R.id.btn_new_chat);
        chatList = new ArrayList<>();
        btn_new_chat.setOnClickListener(this);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        /**
         * call get chat list from users chat field
         */
        getUserChatList();
        initializeRecyclerView(view);
        return view;
    }


    private void initializeRecyclerView(View view){
        mChatList = view.findViewById(R.id.chats_recycler_view);
        mChatList.setNestedScrollingEnabled(true);
        mChatList.setHasFixedSize(true);
        mChatListLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayout.VERTICAL,false);
        mChatList.setLayoutManager(mChatListLayoutManager);
        mChatListAdapter= new ChatListAdapter(chatList, getContext());
        mChatList.setAdapter(mChatListAdapter);
    }


    private void getUserChatList(){
        DatabaseReference mUserChatDB = FirebaseDatabase.getInstance().getReference().child("users").child(mUser.getUid());
        mUserChatDB.child("chats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for ( final DataSnapshot childSnapshot: dataSnapshot.getChildren()){

                        mDbRef.child("chats").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot child: dataSnapshot.getChildren()){
                                    Chat chat = child.getValue(Chat.class);
                                    if (childSnapshot.getKey().equals(chat.getChatId())){
                                        chatList.add(chat);
                                    }
                                }
                                mChatListAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_new_chat:
                Intent intent = new Intent(getActivity(), ActivityNewChat.class);
                startActivity(intent);
                break;
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.chats_menu, menu);
    }



}