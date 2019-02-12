package com.example.hi.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hi.R;
import com.example.hi.activitities.Auth;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class Fragment_Verify_Number  extends Fragment implements View.OnClickListener {
    ImageView btn_next;
    private EditText mPhoneNumber;
    private String phoneNumber;
    private CountryCodePicker countryCodePicker,countryCodePicker1;
    private Context mContext;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private ProgressDialog progressDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_verify_number,container,false);
        btn_next = (ImageView) view.findViewById(R.id.btn_next);
        mPhoneNumber  = (EditText) view.findViewById(R.id.user_number);
        countryCodePicker1 =view.findViewById(R.id.user_country);
        countryCodePicker = view.findViewById(R.id.user_number_iso);
        mContext = getContext();
        progressDialog =new ProgressDialog(getContext());
        /**
         * PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks
         *
         * callbacks instantiation
         * **/
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Auth.signInWithPhoneCredentials(phoneAuthCredential,getFragmentManager().beginTransaction(),progressDialog);
            }
            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(getContext(),"Authecntiation failed"+e.getMessage(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Fragment_Code_Verify fragment_code_verify = new Fragment_Code_Verify();
                Bundle args = new Bundle();
                args.putString("raw_code", s);
                fragment_code_verify.setArguments(args);
                progressDialog.dismiss();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.login_fragment_container, fragment_code_verify).addToBackStack(null).commit();
            }
        };

        btn_next.setOnClickListener(this);
        countryCodePicker.registerCarrierNumberEditText(mPhoneNumber);

        countryCodePicker1.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countryCodePicker.setCountryForPhoneCode(countryCodePicker1.getSelectedCountryCodeAsInt());
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_next:
                if (isValidPhoneNumber()){
                    startAuthentication();
                }else{
                    mPhoneNumber.setError("Number Invalid");
                }
                break;
        }
    }

    private void startAuthentication() {

        try {
            if (isValidPhoneNumber()){
                progressDialog.setMessage("Verifying "+phoneNumber);
                progressDialog.show();
                PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber,
                        60,
                        TimeUnit.SECONDS,
                        getActivity(),
                        mCallbacks);
            }
        }catch (Exception e){
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }


    private boolean isValidPhoneNumber() {
        phoneNumber = countryCodePicker.getFullNumberWithPlus();
        if (!phoneNumber.isEmpty() && countryCodePicker.isValidFullNumber())
            return true;
        return false;
    }
}
