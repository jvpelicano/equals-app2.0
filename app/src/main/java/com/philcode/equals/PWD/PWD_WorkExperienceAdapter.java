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
import com.google.android.gms.tasks.OnFailureListener;
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

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PWD_WorkExperienceAdapter extends RecyclerView.Adapter<com.philcode.equals.PWD.PWD_WorkExperienceAdapter.PWD_WorkExperience_ViewHolder> {

    private Context context;
    private List<PWD_AddWorkInformation> workInfos;
    private DatabaseReference rootRef;
    private PWD_WorkExperience_Fragment fragment;
    private String uid;
    private FirebaseUser currentFirebaseUser;

    public PWD_WorkExperienceAdapter(Context c, List<PWD_AddWorkInformation> p ){
        context = c;
        workInfos = p;
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = currentFirebaseUser.getUid();
        rootRef = FirebaseDatabase.getInstance().getReference().child("PWD").child(uid).child("listOfWorks");
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
                        rootRef.child(workUUID).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                context.startActivity(new Intent(context, PWD_EditProfile_ViewActivity.class));
                                int pos = holder.getAdapterPosition();
                                workInfos.remove(pos);
                                notifyItemRemoved(pos);
                                Toast.makeText(context,"Deleted successfully", Toast.LENGTH_LONG).show();
                            }
                        });
                        dialog.dismiss();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
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

        }


    }

    /*private Completable deleteItem(String uID){
        return Completable.create(emitter -> {
           rootRef.child(uID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
               @Override
               public void onComplete(@NonNull Task<Void> task) {
                   if (task.isSuccessful()) {
                       emitter.onComplete();
                   } else {
                       if (task.getException() != null) {
                           emitter.onError(task.getException());
                       } else {
                           emitter.onError(new Throwable("No error found."));
                       }
                   }
               }
           });
        });
    }*/


}