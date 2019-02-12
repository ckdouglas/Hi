package com.example.hi.activitities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.example.hi.R;
import com.example.hi.fragments.Fragment_Welcome;
public class ActivityLogin extends AppCompatActivity {
    private final String TAG = "ActivityLogin";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * check if one is authenticated
         * */
        if(!Auth.userIsLoggedIn()) {
            setContentView(R.layout.activity_login);
            Log.d(TAG, "onCreate:Creating login activity");
            if (findViewById(R.id.login_fragment_container) != null) {
                if (savedInstanceState != null) {
                    return;
                }
                Fragment_Welcome fragment__welcome = new Fragment_Welcome();
                getSupportFragmentManager().beginTransaction().add(R.id.login_fragment_container, fragment__welcome).commit();
                Log.d(TAG, "onCreate:Creating login welcome fragment");
            }
        }else {
            Log.d(TAG, "onCreate:Creating home activity");
            Intent intent = new Intent(ActivityLogin.this,ActivityHome.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }


    }
}
