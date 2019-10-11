package com.corp.todolist.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.corp.todolist.R;
import com.corp.todolist.models.Mydaymodel;
import com.corp.todolist.models.PlansModel;

import java.util.ArrayList;

public class PlansAdapter  extends RecyclerView.Adapter<PlansAdapter.Planholder>{
    public Context context;
    public ArrayList<PlansModel> PlanmodelArrayList;

    public PlansAdapter(Context context, ArrayList<PlansModel> planmodelArrayList) {
        this.context = context;
        PlanmodelArrayList = planmodelArrayList;
    }

    @NonNull
    @Override
    public Planholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plan, parent, false);

        return new PlansAdapter.Planholder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Planholder holder, int position) {
        PlansModel model= PlanmodelArrayList.get(position);
        holder.tvmessage.setText(model.getMessage());
        holder.tvdate.setText(model.getDate());

    }

    @Override
    public int getItemCount() {
        return PlanmodelArrayList.size();
    }

    public class Planholder extends RecyclerView.ViewHolder {
        TextView tvmessage ,tvdate;
        public Planholder(@NonNull View itemView) {
            super(itemView);
            tvdate=itemView.findViewById(R.id.planDate);
            tvmessage=itemView.findViewById(R.id.planmessage);
        }
    }
}
