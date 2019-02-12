package com.example.hi.activitities;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.example.hi.R;
import com.example.hi.fragments.Fragment_Profile_Info;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;

public class Auth {
    public static boolean userIsLoggedIn() {
        if (FirebaseAuth.getInstance().getCurrentUser()!=null)
            return true;
        return false;
    }

    public static void signInWithPhoneCredentials(PhoneAuthCredential phoneAuthCredential, final FragmentTransaction fragmentTransaction, final ProgressDialog progressDialog) {
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    if (currentUser!=null){
                        progressDialog.dismiss();
                        upDateUI(fragmentTransaction);
                    }
                }
            }
        });
    }

    static private void upDateUI(FragmentTransaction fragmentTransaction) {
        Fragment_Profile_Info fragment_profile_info = new Fragment_Profile_Info();
        fragmentTransaction = fragmentTransaction.replace(R.id.login_fragment_container, fragment_profile_info);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
}
