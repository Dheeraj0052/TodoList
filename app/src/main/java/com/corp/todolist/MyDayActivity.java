package com.corp.todolist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.corp.todolist.Adapters.Mydayadapter;
import com.corp.todolist.models.Mydaymodel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MyDayActivity extends AppCompatActivity
{
    ImageView ivback;
    FloatingActionButton fab;
    RecyclerView rvMyday;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ArrayList<Mydaymodel> mydaymodelList;
    ArrayList<String> key;
    Mydayadapter mydayadapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_day);
        ivback=findViewById(R.id.back);
        fab=findViewById(R.id.fab);
        rvMyday=findViewById(R.id.rcMyDay);
        firebaseDatabase = FirebaseDatabase.getInstance();
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);
        databaseReference=firebaseDatabase.getReference("Mydays/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/"+formattedDate);
        mydaymodelList= new ArrayList<Mydaymodel>();
        key=new ArrayList<String>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mydaymodelList.clear();
                key.clear();

                for(DataSnapshot snapshot :dataSnapshot.getChildren()){
                    Mydaymodel mydaymodel=snapshot.getValue(Mydaymodel.class);
                    mydaymodelList.add(mydaymodel);
                   key.add( snapshot.getKey());
                }
                mydayadapter =new Mydayadapter(MyDayActivity.this,mydaymodelList,key);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
                rvMyday.setLayoutManager(layoutManager);
                rvMyday.setItemAnimator(new DefaultItemAnimator());
                rvMyday.setNestedScrollingEnabled(false);
                rvMyday.setAdapter(mydayadapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                databaseReference.child("nn").setValue("heloo");
                showDialog();
            }
        });
        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDayActivity.super.onBackPressed();
            }
        });


    }
    private void showDialog() {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enter your next TO-DO");

        LayoutInflater inflater = LayoutInflater.from(this);
        View login_layout = inflater.inflate(R.layout.layout_dialog,null);

        final EditText message = login_layout.findViewById(R.id.etmessagedialog);



        dialog.setView(login_layout);

        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String mess=message.getText().toString();
                if(TextUtils.isEmpty(mess))
                {
                    message.setError("required");


                }else {
                 //   Toast.makeText(MyDayActivity.this,""+mess,Toast.LENGTH_SHORT).show();
                    Mydaymodel model = new Mydaymodel();
                    model.setMessage(mess);
                    model.setStatus(0);
                    Date c = Calendar.getInstance().getTime();


                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                    String formattedDate = df.format(c);
                    databaseReference=firebaseDatabase.getReference("Mydays/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/"+formattedDate);
                    databaseReference.push().setValue(model);
                    mydaymodelList.add(model);
                    mydayadapter.notifyDataSetChanged();
                    dialogInterface.dismiss();
                }


            }

        });


        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.show();

    }
}