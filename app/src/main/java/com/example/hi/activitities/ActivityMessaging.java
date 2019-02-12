package com.example.hi.activitities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hi.R;
import com.example.hi.adapters.MessagesAdapter;
import com.example.hi.model.Chat;
import com.example.hi.model.Message;
import com.example.hi.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ActivityMessaging extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ActivityMessaging";
    private FirebaseUser mUser;
   private   DatabaseReference mDbRef;
   private ImageView btn_back,profile_image,btn_send,btn_emoji;
   private TextView username;
   private Toolbar toolbar;
   private EditText editTextMessage;


    private RecyclerView mMessagesRecyclerView;
    private RecyclerView.Adapter mMessagesAdapter;
    private RecyclerView.LayoutManager mChatLayoutManager;
    private ArrayList<Message> mAllMessages;
    private String chatId;

    /**
     *User object is the user clicked on the contact list
     */
    User user;
    private boolean chatExists;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        user= getIntent().getParcelableExtra("user");
        btn_back = (ImageView) findViewById(R.id.btn_back);
        profile_image = (ImageView) findViewById(R.id.profile_image);
        username = (TextView) findViewById(R.id.userName);
        btn_emoji = (ImageView) findViewById(R.id.emoticons_button);
        btn_send = (ImageView) findViewById(R.id.send_button);
        mDbRef = FirebaseDatabase.getInstance().getReference();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        editTextMessage  = (EditText) findViewById(R.id.textMessage);
        checkIfChatExist();
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return false;
            }
        });
        username.setText(user.getName());

        if ((user.getProfile_URL()).equals("default")){
            profile_image.setImageResource(R.drawable.ic_default_profile);
        }else {
            Glide.with(getApplicationContext()).load(user.getProfile_URL()).into(profile_image);
        }

        btn_back.setOnClickListener(this);
        username.setOnClickListener(this);
        btn_emoji.setOnClickListener(this);
        btn_send.setOnClickListener(this);
        setUpToolBar();
        setUpMessages();
        readMessagesIntoView();
    }



    /**
     * set up tool bar
     * */
    private void setUpToolBar() {
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){

                }
                return false;
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.userName:
                showProfileInfor();
                break;
            case R.id.btn_back:
                navigateBack();
                break;
            case R.id.emoticons_button:
                openEmojiKeyboard();
                break;
            case R.id.send_button:
                if (chatExists){
                    sendMessage();
                }else {
                    createChat();
                    sendMessage();
                }
                break;

        }

    }


    private void setUpMessages() {
        Log.d(TAG, "setUpMessages:setting up all users recycler view");
        mMessagesRecyclerView = (RecyclerView) findViewById(R.id.messages_recycler_view);
        mMessagesRecyclerView.setHasFixedSize(true);
        mMessagesRecyclerView.setNestedScrollingEnabled(true);
        mChatLayoutManager = new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false);
        ((LinearLayoutManager) mChatLayoutManager).setStackFromEnd(true);
        mMessagesRecyclerView.setLayoutManager(mChatLayoutManager);
    }

    private void readMessagesIntoView() {
        mAllMessages = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("messages");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mAllMessages.clear();
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()){
                    Message message = (Message) childSnapshot.getValue(Message.class);
                    if (message.getSenderId().equals(mUser.getUid() ) && message.getRecieverId().equals(user.getUid())
                            || message.getSenderId().equals(user.getUid()) && message.getRecieverId().equals(mUser.getUid())
                    )
                        mAllMessages.add(message);
                        /**
                         * send last text in user
                         * */
                }
                mMessagesAdapter = new MessagesAdapter(mAllMessages, getApplicationContext());
                mMessagesAdapter.notifyDataSetChanged();
                mMessagesRecyclerView.setAdapter(mMessagesAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void showProfileInfor() {
    }

    private void navigateBack() {
        Intent intent = new Intent(ActivityMessaging.this, ActivityHome.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void openEmojiKeyboard() {
    }

    /**
     * send message routine
     */
    private void sendMessage(){
        final String message = editTextMessage.getText().toString();
        if (!message.equals("")){
            Date date = new Date();
            String time_sent = DateFormat.getTimeInstance().format(date);
            String sender = mUser.getUid();
            String reciever = user.getUid();
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("chatId",chatId);
            hashMap.put("senderId", sender);
            hashMap.put("recieverId",reciever);
            hashMap.put("message",message);
            hashMap.put("time_sent",time_sent);

            mDbRef.child("messages").push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        editTextMessage.setText("");
                    }
                }
            });

        }else {
            editTextMessage.setError("You cant send empty message");
        }

    }

    private void checkIfChatExist() {
        mDbRef.child("chats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapShot: dataSnapshot.getChildren()){
                    Chat chat = childSnapShot.getValue(Chat.class);
                    if ((chat.getSenderId().equals(mUser.getUid()) && chat.getReceiverId().equals(user.getUid()))
                            ||
                        (chat.getSenderId().equals(user.getUid()) &&  chat.getReceiverId().equals(mUser.getUid())))
                    { chatExists = true;
                    }else {
                        chatExists = false;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void createChat() {
        String key = mDbRef.child("chats").push().getKey();
        chatId = key;
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("chatId",key);
        hashMap.put("senderId",mUser.getUid());
        hashMap.put("receiverId",user.getUid());
        mDbRef.child("chats").child(key).setValue(hashMap);
        mDbRef.child("users").child(mUser.getUid()).child("chats").child(key).setValue(true);
        mDbRef.child("users").child(user.getUid()).child("chats").child(key).setValue(true);

    }


}
