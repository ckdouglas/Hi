package com.example.hi.activitities;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.hi.R;
import com.example.hi.adapters.SectionPagerAdapter;
import com.example.hi.fragments.Fragment_Calls;
import com.example.hi.fragments.Fragment_Camera;
import com.example.hi.fragments.Fragment_Chats;
import com.example.hi.fragments.Fragment_Status;

public class ActivityHome extends AppCompatActivity {
    private final String TAG = "ActivityHome";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(TAG,"onCreate:Creating login activity");
        Log.d(TAG,"onCreate:Creating fragment home");
        setUpViewPager();
    }


    private void setUpViewPager() {
        Log.d(TAG,"setUpViewPager:Setting up viewpager");
        SectionPagerAdapter sectionPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager());
        sectionPagerAdapter.addFragment(new Fragment_Camera() );
        sectionPagerAdapter.addFragment(new Fragment_Chats());
        sectionPagerAdapter.addFragment(new Fragment_Status());
        sectionPagerAdapter.addFragment(new Fragment_Calls());
        ViewPager viewPager = findViewById(R.id.container);
        viewPager.setAdapter(sectionPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.white));
        tabLayout.setTabTextColors(getResources().getColor(R.color.grey),getResources().getColor(R.color.white));

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_photo_camera_black_24dp);
        tabLayout.getTabAt(1).setText("Chat").select();
        tabLayout.getTabAt(2).setText("Status");
        tabLayout.getTabAt(3).setText("Calls");

        /**
         * setting tabs colors
         * */
        tabLayout.getTabAt(0).getIcon().setColorFilter(Color.parseColor("#b3b3b3"), PorterDuff.Mode.SRC_IN);
        tabLayout.setTabTextColors(
                ContextCompat.getColor(this, R.color.light_white),
                ContextCompat.getColor(this, R.color.white)
        );

        setUpToolBar();
    }
    /**
     * set up tool bar
     * */
    private void setUpToolBar() {
        Log.d(TAG, "setUpToolBar:setting up toolbar");
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.home_toolbar);
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



}
