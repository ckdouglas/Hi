package com.example.hi.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hi.R;


public class Fragment_Camera extends Fragment {
    String TAG = "Camera Fragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera,container,false);
        Log.d(TAG, "onCreate:loading camera fragment");
        startCameraIntent();
        return view;
    }


    private void startCameraIntent(){

    }

    @Override
    public void onResume() {
        super.onResume();
        startCameraIntent();
    }
}
