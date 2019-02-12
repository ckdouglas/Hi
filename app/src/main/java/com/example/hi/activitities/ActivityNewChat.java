package com.example.hi.activitities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hi.R;
import com.example.hi.adapters.UsersAdapter;
import com.example.hi.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ActivityNewChat extends AppCompatActivity implements View.OnClickListener{
    private ImageView btn_back;
    private TextView numberOfContacts;
    private final String TAG= "ActivityNewChat";

    private RecyclerView mUsersRecyclerView;
    private RecyclerView.Adapter mUserAdapter;
    private RecyclerView.LayoutManager mChatLayoutManager;
    private ArrayList<User> mAllUsers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_chat);
        btn_back = (ImageView) findViewById(R.id.btn_back);
        numberOfContacts  = (TextView)findViewById(R.id.number_of_contacts);
        readUserIntoView();
        setUpAllUsers();
        btn_back.setOnClickListener(this);
        setUpToolBar();
    }

    private void setUpAllUsers() {
        Log.d(TAG, "setUpAllUsers:setting up all users recycler view");
        mUsersRecyclerView = (RecyclerView) findViewById(R.id.chatsRecyclerView);
        mUsersRecyclerView.setHasFixedSize(false);
        mUsersRecyclerView.setNestedScrollingEnabled(true);
        mChatLayoutManager = new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false);
        mUsersRecyclerView.setLayoutManager(mChatLayoutManager);
    }

    private void readUserIntoView() {
        mAllUsers = new ArrayList<>();
        final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mAllUsers.clear();
                    for (DataSnapshot childSnapshot: dataSnapshot.getChildren()){
                         User user = (User) childSnapshot.getValue(User.class);
                         if (!user.getPhone().equals(mUser.getPhoneNumber())){
                             mAllUsers.add(user);
                        }

                    }
                    mUserAdapter = new UsersAdapter(mAllUsers, getApplicationContext());
                    mUserAdapter.notifyDataSetChanged();
                    mUsersRecyclerView.setAdapter(mUserAdapter);
                    numberOfContacts.setText(mAllUsers.size()+" "+ "Contacts");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                Intent intent = new Intent(ActivityNewChat.this,ActivityHome.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
        }
    }


    private void setUpToolBar() {
        Log.d(TAG, "setUpToolBar:setting up toolbar");
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
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.new_chat_menu, menu);
        return true;
    }


}
