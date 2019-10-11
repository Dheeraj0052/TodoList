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
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;

import com.corp.todolist.Adapters.Impadapter;
import com.corp.todolist.Adapters.Mydayadapter;
import com.corp.todolist.Adapters.PlansAdapter;
import com.corp.todolist.models.Mydaymodel;
import com.corp.todolist.models.PlansModel;
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

public class PlansActivity extends AppCompatActivity {
    ImageView ivback;
    FloatingActionButton fab;
    RecyclerView rvplans;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference,databaseReference1;
    ArrayList<PlansModel> mydaymodelList;
    ArrayList<String> key;
    PlansAdapter myadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plans);
        ivback=findViewById(R.id.backplan);
        fab=findViewById(R.id.fabplans);
        rvplans=findViewById(R.id.rclans);
        firebaseDatabase=FirebaseDatabase.getInstance();

        databaseReference=firebaseDatabase.getReference("Plans/"+ FirebaseAuth.getInstance().getCurrentUser().getUid());
        mydaymodelList= new ArrayList<PlansModel>();
        key=new ArrayList<String>();

       databaseReference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               mydaymodelList.clear();
               for(DataSnapshot snapshot :dataSnapshot.getChildren()){
                   PlansModel model=snapshot.getValue(PlansModel.class);
                   mydaymodelList.add(model);
               }
               myadapter=new PlansAdapter(PlansActivity.this,mydaymodelList);
               RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
               rvplans.setLayoutManager(layoutManager);
               rvplans.setItemAnimator(new DefaultItemAnimator());
               rvplans.setNestedScrollingEnabled(false);
               rvplans.setAdapter(myadapter);
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
                PlansActivity.super.onBackPressed();
            }
        });
    }
    private void showDialog() {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enter your next TO-DO");

        LayoutInflater inflater = LayoutInflater.from(this);
        View login_layout = inflater.inflate(R.layout.layout_plans,null);

        final EditText message = login_layout.findViewById(R.id.etmessagedialoge);

        final EditText etdate = login_layout.findViewById(R.id.etdatedialoge);
        final String[] formattedDate = new String[1];
        final CalendarView calendarView=login_layout.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                Calendar calendar;
                calendar = Calendar.getInstance();
                calendar.set(Calendar.MONTH, i1);
                calendar.set(Calendar.DAY_OF_MONTH, i2);
                calendar.set(Calendar.YEAR, i);
                Date c =calendar.getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");

                formattedDate[0] = df.format(c);
            }
        });


        dialog.setView(login_layout);

        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String mess=message.getText().toString();
//                String date=etdate.getText().toString();
                if(TextUtils.isEmpty(mess))
                {
                    message.setError("required");


                }

                else {
                    //   Toast.makeText(MyDayActivity.this,""+mess,Toast.LENGTH_SHORT).show();
//                    Mydaymodel model = new Mydaymodel();
//                    model.setMessage(mess);
//                    model.setStatus(0);
//                    Date c = Calendar.getInstance().getTime();
//
//
//                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
//                    String formattedDate = df.format(c);
//                    databaseReference=firebaseDatabase.getReference("Mydays/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+"/"+formattedDate);
//                    databaseReference.push().setValue(model);
//                    mydaymodelList.add(model);
//                    mydayadapter.notifyDataSetChanged();
//                    dialogInterface.dismiss();
                    PlansModel model  =new PlansModel();
                    model.setMessage(mess);
                    model.setStatus(0);
                    model.setDate(formattedDate[0]);
                    Mydaymodel mydaymodel =new Mydaymodel();
                    mydaymodel.setMessage(mess);
                    mydaymodel.setStatus(0);

                    databaseReference=firebaseDatabase.getReference("Plans/"+ FirebaseAuth.getInstance().getCurrentUser().getUid());
                    databaseReference.push().setValue(model);
                    databaseReference1=firebaseDatabase.getReference("Mydays/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/"+formattedDate[0]);
                    databaseReference1.push().setValue(mydaymodel);

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
