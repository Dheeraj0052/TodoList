package com.corp.todolist.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.corp.todolist.R;
import com.corp.todolist.models.Mydaymodel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Mydayadapter extends RecyclerView.Adapter<Mydayadapter.Mydayholder> {
    public Context context;
    public ArrayList<Mydaymodel> mydaymodelArrayList;
    public ArrayList<String> key;

    public Mydayadapter(Context context, ArrayList<Mydaymodel> mydaymodelArrayList, ArrayList<String> key) {
        this.context = context;
        this.mydaymodelArrayList = mydaymodelArrayList;
        this.key = key;
    }

    @NonNull
    @Override
    public Mydayholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_myday, parent, false);

        return new Mydayadapter.Mydayholder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Mydayholder holder, final int position) {
        final Mydaymodel mydaymodel=mydaymodelArrayList.get(position);
        holder.tvmessage.setText(mydaymodel.getMessage());
        if(mydaymodel.getStatus()==0){
            holder.tvstatus.setVisibility(View.GONE);
        }
        else {
            holder.tvstatus.setVisibility(View.VISIBLE);
        }

        holder.llclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] messages = {"Mark as Complete","Mark as Uncomplete","Move to important"};
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
                mBuilder.setTitle("Choose an item");
                mBuilder.setSingleChoiceItems(messages, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase firebaseDatabase;
                        DatabaseReference databaseReference,databaseReference1;
                        firebaseDatabase = FirebaseDatabase.getInstance();

                        if(i==1)
                        {
                            Mydaymodel model = new Mydaymodel();
                            model.setMessage(mydaymodel.getMessage());
                            model.setStatus(0);
                            Date c = Calendar.getInstance().getTime();


                            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                            String formattedDate = df.format(c);
                            databaseReference=firebaseDatabase.getReference("Mydays/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+"/"+formattedDate);
                            databaseReference.child(key.get(position)).setValue(model);
                        }
                        else if(i==0)
                        {   Mydaymodel model = new Mydaymodel();
                            model.setMessage(mydaymodel.getMessage());
                            model.setStatus(1);
                            Date c = Calendar.getInstance().getTime();


                            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                            String formattedDate = df.format(c);
                            databaseReference=firebaseDatabase.getReference("Mydays/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+"/"+formattedDate);
                            databaseReference.child(key.get(position)).setValue(model);

                        }
                        else {
                            Mydaymodel model = new Mydaymodel();
                            model.setMessage(mydaymodel.getMessage());
                            model.setStatus(0);
                            Date c = Calendar.getInstance().getTime();


                            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                            String formattedDate = df.format(c);
                            databaseReference=firebaseDatabase.getReference("Mydays/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+"/"+formattedDate);
                            databaseReference.child(key.get(position)).removeValue();

                            databaseReference1=firebaseDatabase.getReference("Important/"+FirebaseAuth.getInstance().getCurrentUser().getUid());
                            databaseReference1.push().setValue(model);

                        }
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return mydaymodelArrayList.size();
    }

    public class Mydayholder extends RecyclerView.ViewHolder {
        TextView tvmessage;
            ImageView tvstatus;
            LinearLayout llclick;
        public Mydayholder(@NonNull View itemView) {
            super(itemView);
            tvmessage=itemView.findViewById(R.id.message);
            tvstatus=itemView.findViewById(R.id.status);
            llclick=itemView.findViewById(R.id.llitem);
        }
    }
}
