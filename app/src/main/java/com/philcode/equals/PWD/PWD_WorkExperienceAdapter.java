package com.philcode.equals.PWD;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.philcode.equals.R;

import java.util.List;

public class PWD_WorkExperienceAdapter extends RecyclerView.Adapter<com.philcode.equals.PWD.PWD_WorkExperienceAdapter.PWD_WorkExperience_ViewHolder> {

    Context context;
    private List<PWD_AddWorkInformation> workInfos;

    public PWD_WorkExperienceAdapter(Context c, List<PWD_AddWorkInformation> p ){
        context = c;
        workInfos = p;
    }

    @NonNull
    @Override
    public com.philcode.equals.PWD.PWD_WorkExperienceAdapter.PWD_WorkExperience_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new com.philcode.equals.PWD.PWD_WorkExperienceAdapter.PWD_WorkExperience_ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_work, parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull com.philcode.equals.PWD.PWD_WorkExperienceAdapter.PWD_WorkExperience_ViewHolder holder, int position) {
        holder.displayPosition.setText(workInfos.get(position).getPosition());
        holder.displayCompanyName.setText(workInfos.get(position).getCompany_name());
        holder.displayWorkStarted.setText(workInfos.get(position).getDateStarted());
        holder.displayWorkEnded.setText(workInfos.get(position).getDateEnded());
    }

    @Override
    public int getItemCount() {
        return workInfos.size();
    }

    class PWD_WorkExperience_ViewHolder extends RecyclerView.ViewHolder{
        TextView displayPosition, displayCompanyName, displayWorkStarted, displayWorkEnded;

        public PWD_WorkExperience_ViewHolder(View itemView){
            super(itemView);
            
            displayPosition = itemView.findViewById(R.id.displayPosition);
            displayCompanyName = itemView.findViewById(R.id.displayCompanyName);
            displayWorkStarted = itemView.findViewById(R.id.displayWorkStarted);
            displayWorkEnded = itemView.findViewById(R.id.displayWorkEnded);
        }


    }

}