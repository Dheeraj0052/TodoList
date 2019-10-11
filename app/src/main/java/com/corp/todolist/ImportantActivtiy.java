package com.corp.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.corp.todolist.Adapters.Impadapter;
import com.corp.todolist.Adapters.Mydayadapter;
import com.corp.todolist.models.Mydaymodel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

public class ImportantActivtiy extends AppCompatActivity {

    ImageView ivback;
    FloatingActionButton fab;
    RecyclerView rvMyday;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ArrayList<Mydaymodel> mydaymodelList;
    ArrayList<String> key;
    Impadapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_important_activtiy);
        ivback=findViewById(R.id.backimp);
        fab=findViewById(R.id.fabimp);
        rvMyday=findViewById(R.id.rcImp);
        firebaseDatabase = FirebaseDatabase.getInstance();
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);
        databaseReference=firebaseDatabase.getReference("Important/"+ FirebaseAuth.getInstance().getCurrentUser().getUid());
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
                    key.add(snapshot.getKey());
                }
                adapter =new Impadapter(ImportantActivtiy.this,mydaymodelList,key);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
                rvMyday.setLayoutManager(layoutManager);
                rvMyday.setItemAnimator(new DefaultItemAnimator());
                rvMyday.setNestedScrollingEnabled(false);
                rvMyday.setAdapter(adapter);

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
                ImportantActivtiy.super.onBackPressed();
            }
        });
    }
    private void showDialog() {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enter important reminder OR TO-Do");

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
                    databaseReference=firebaseDatabase.getReference("Important/"+FirebaseAuth.getInstance().getCurrentUser().getUid());
                    databaseReference.push().setValue(model);
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
