package com.philcode.equals.PWD;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.philcode.equals.R;

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.List;

public class PWD_WorkExperienceAdapter extends RecyclerView.Adapter<com.philcode.equals.PWD.PWD_WorkExperienceAdapter.PWD_WorkExperience_ViewHolder> {

    Context context;
    private List<PWD_AddWorkInformation> workInfos;
    DatabaseReference rootRef;
    PWD_WorkExperience_Fragment fragment;
    private String uid;
    private FirebaseUser currentFirebaseUser;
    private FirebaseDatabase fdb;

    public PWD_WorkExperienceAdapter(Context c, List<PWD_AddWorkInformation> p ){
        context = c;
        workInfos = p;
    }

    @NonNull
    @Override
    public com.philcode.equals.PWD.PWD_WorkExperienceAdapter.PWD_WorkExperience_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new com.philcode.equals.PWD.PWD_WorkExperienceAdapter.PWD_WorkExperience_ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_work_pwd, parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull com.philcode.equals.PWD.PWD_WorkExperienceAdapter.PWD_WorkExperience_ViewHolder holder, int position) {
        holder.displayPosition.setText(workInfos.get(position).getPosition());
        holder.displayCompanyName.setText(workInfos.get(position).getCompany_name());
        holder.displayWorkStarted.setText(workInfos.get(position).getDateStarted());
        holder.displayWorkEnded.setText(workInfos.get(position).getDateEnded());
        final Integer pos = position;
        holder.btn_delete_workExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String workUUID = workInfos.get(pos).getWorkID();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Do you want to proceed?");
                builder.setMessage("By tapping the proceed button, you agree to delete the selected job post.");
                builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        workInfos.remove(holder.getAdapterPosition());
                        notifyDataSetChanged();
                        dialog.dismiss();
                        rootRef.child("listOfWorks").child(workUUID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(v.getContext(), "Data has been deleted successfully.", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(v.getContext(), "Error" + task.getException().toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        notifyDataSetChanged();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                dialog.setCancelable(true);
            }
        });
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