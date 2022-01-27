package com.philcode.equals.PWD;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.philcode.equals.R;

import java.util.List;

public class PWD_WorkExperienceAdapterRegister extends RecyclerView.Adapter<PWD_WorkExperienceAdapterRegister.PWD_WorkExperience_ViewHolder> {

    Context context;
    private List<PWD_AddWorkInformation> workInfos;
    DatabaseReference rootRef;
    PWD_WorkExperience_Fragment fragment;
    private String uid;
    private FirebaseUser currentFirebaseUser;
    private FirebaseDatabase fdb;

    public PWD_WorkExperienceAdapterRegister(Context c, List<PWD_AddWorkInformation> p ){
        context = c;
        workInfos = p;
    }

    @NonNull
    @Override
    public PWD_WorkExperienceAdapterRegister.PWD_WorkExperience_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PWD_WorkExperienceAdapterRegister.PWD_WorkExperience_ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_work, parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull PWD_WorkExperienceAdapterRegister.PWD_WorkExperience_ViewHolder holder, int position) {
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
        ImageButton btn_delete_workExp;

        public PWD_WorkExperience_ViewHolder(View itemView){
            super(itemView);
            displayPosition = itemView.findViewById(R.id.displayPosition);
            displayCompanyName = itemView.findViewById(R.id.displayCompanyName);
            displayWorkStarted = itemView.findViewById(R.id.displayWorkStarted);
            displayWorkEnded = itemView.findViewById(R.id.displayWorkEnded);
            btn_delete_workExp = itemView.findViewById(R.id.btn_delete_workExp);
            fragment = new PWD_WorkExperience_Fragment();

            currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            uid = currentFirebaseUser.getUid();
            rootRef = FirebaseDatabase.getInstance().getReference().child("PWD").child(uid);

        }


    }

}