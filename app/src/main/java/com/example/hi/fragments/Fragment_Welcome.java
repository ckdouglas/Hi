package com.example.hi.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.hi.R;

public class Fragment_Welcome extends Fragment implements View.OnClickListener {

    private Button btn_welcome;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_welcome,container,false);
        btn_welcome = view.findViewById(R.id.accept_welcome);
        btn_welcome.setOnClickListener(this);
        return view;

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.accept_welcome:
                Fragment_Verify_Number fragment_verify_number = new Fragment_Verify_Number();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.login_fragment_container,fragment_verify_number).commit();
                break;
        }
    }
}
