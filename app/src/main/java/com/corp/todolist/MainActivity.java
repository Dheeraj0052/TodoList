package com.corp.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView tvMessage;
    EditText etPhone,etName;
    ImageView ivNext;
    String phonenumber, name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvMessage=findViewById(R.id.tvMessage);
        etPhone=findViewById(R.id.etPhone);
        etName=findViewById(R.id.etName);
        ivNext=findViewById(R.id.ivNext);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.lefttoright);
        tvMessage.startAnimation(animation);
        Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.blink);
        ivNext.startAnimation(animation2);
        ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phonenumber=etPhone.getText().toString();
                name=etName.getText().toString();
                if(checkValid())
                {
                    phonenumber="+91"+phonenumber;
                    Intent intent = new Intent(MainActivity.this, VerificationActivity.class);
                    intent.putExtra("mobile",phonenumber);
                    intent.putExtra("name",name);
                    startActivity(intent);

                }
            }
        });


    }

    private boolean checkValid() {
        Boolean check =true;
        if(TextUtils.isEmpty(phonenumber))
        {
            etPhone.setError("Required");
            check=false;
        }
        if(TextUtils.isEmpty(name))
        {
            etPhone.setError("Required");
            check=false;
        }
        if( !(phonenumber.length()==10))
        {
            etPhone.setError("Not valid");
            check=false;
        }
        return check;
    }

}
