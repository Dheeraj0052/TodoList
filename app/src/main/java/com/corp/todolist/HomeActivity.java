package com.corp.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity  {
    String phone ,name;
    TextView tvName,tvPhone;
    RelativeLayout rvMyday, rvImportant, rvTasks, rvPlans;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        tvName=findViewById(R.id.tvname);
        tvPhone=findViewById(R.id.tvPhone);
        rvMyday=findViewById(R.id.rvMyDay);
        rvImportant=findViewById(R.id.rvImportant);
        rvPlans=findViewById(R.id.rvPlans);
        rvTasks=findViewById(R.id.rvTask);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
       phone= pref.getString("mobile","");
       name= pref.getString("name","");
       tvPhone.setText(phone);
       tvName.setText(name);
       rvMyday.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(HomeActivity.this, MyDayActivity.class);
               startActivity(intent);

           }
       });
        rvTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, TasksActivity.class);
                startActivity(intent);

            }
        });
        rvPlans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, PlansActivity.class);
                startActivity(intent);

            }
        });
        rvImportant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ImportantActivtiy.class);
                startActivity(intent);
            }
        });


    }
}
