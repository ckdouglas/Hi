package com.example.hi.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.example.hi.R;
import com.example.hi.activitities.Auth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class Fragment_Code_Verify extends Fragment implements View.OnClickListener {
    private final String TAG = "Fragment_Code_Verify";
    private ImageView btn_next;
    private Context mContext;
    private ProgressDialog progressDialog;
    private  String mVerificationId = "";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_code_verify,container,false);
        Log.d(TAG,"onCreate:Creating code verificatin fragment");
        btn_next = (ImageView) view.findViewById(R.id.btn_next);
        mVerificationId = getArguments().getString("raw_code");
        mContext = getContext();
        btn_next.setOnClickListener(this);
        progressDialog =new ProgressDialog(getContext());
        final PinEntryEditText pinEntry = (PinEntryEditText) view.findViewById(R.id.verification_code_entry);
        if (pinEntry != null) {
            pinEntry.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
                @Override
                public void onPinEntered(CharSequence str) {
                    verifyCodeSent(str.toString());
                }
            });
        }
        return view;
    }

    private void verifyCodeSent(String code) {
        try{
            if (!mVerificationId.isEmpty()) {
                Toast.makeText(mContext, "Verifying code sent"+mVerificationId, Toast.LENGTH_LONG).show();
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
                Auth.signInWithPhoneCredentials(credential,getFragmentManager().beginTransaction(),progressDialog);
                progressDialog.setMessage("Authenticating...");
                progressDialog.show();
            }else {
                Toast.makeText(mContext, "Empty mVerificationId",Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            Toast.makeText(mContext, "Error:"+e.getMessage(),Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_next:
                Log.d(TAG,"onClick:Creating Fragment_Profile_Info");
                break;
        }
    }
}
