package com.corp.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {
    FirebaseAuth auth ;
    FirebaseUser currentuser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        auth = FirebaseAuth.getInstance();
        currentuser =auth.getCurrentUser();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(currentuser!=null)
                {
                    Intent main = new Intent(SplashActivity.this, HomeActivity.class );
                    SplashActivity.this.startActivity(main);
                    SplashActivity.this.finish();

                }
                else {
                    Intent main = new Intent(SplashActivity.this,MainActivity.class );
                    SplashActivity.this.startActivity(main);
                    SplashActivity.this.finish();
                }
            }
        }, 3000);
    }
}
