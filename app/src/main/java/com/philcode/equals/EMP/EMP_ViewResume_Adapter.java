package com.philcode.equals.EMP;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.philcode.equals.R;

import java.util.ArrayList;

public class EMP_ViewResume_Adapter extends RecyclerView.Adapter<EMP_ViewResume_Adapter.MyViewHolder> {
    Context context;
    ArrayList<EMP_ViewResume_Information> availablejobsinfos;
    Button btnCall, btnMessage;
    String postJobId;
    private DatabaseReference jobReference;
    public EMP_ViewResume_Adapter(Context c, ArrayList<EMP_ViewResume_Information> p, String postJobID) {
        context = c;
        availablejobsinfos = p;
        postJobId = postJobID;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.emp_resume_list_item_cardview, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.file_icon.setImageResource(R.mipmap.resume_icon);
        holder.firstName.setText(availablejobsinfos.get(position).getFirstName()+" "+availablejobsinfos.get(position).getLastName());
        holder.email.setText(availablejobsinfos.get(position).getEmail());
        holder.contact.setText(availablejobsinfos.get(position).getContact());
        String key = availablejobsinfos.get(position).getUserID();
        jobReference = FirebaseDatabase.getInstance().getReference().child("Job_Offers").child(postJobId);
        jobReference.child("Resume").child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("oldResumeFile")){
                    holder.resumeFile.setVisibility(View.VISIBLE);
                }else{
                    holder.resumeFile.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



    @Override
    public int getItemCount() {
        return availablejobsinfos.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView resumeFile, firstName, lastName, displayUid, email, contact;
        ImageView file_icon;
        Button btnCall, btnMessage;

        ImageView btnViewPost;

        public MyViewHolder(View itemView) {
            super(itemView);
            file_icon = itemView.findViewById(R.id.file_icon);
            firstName = itemView.findViewById(R.id.editFirstName);
            resumeFile = itemView.findViewById(R.id.editNotifyOldResume);
            email = itemView.findViewById(R.id.editEmail);
            contact = itemView.findViewById(R.id.editContact);
            //btnViewPost = itemView.findViewById(R.id.viewApplicant);
        }

        public void onClick(final int position) {

            btnCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_CALL);
                    String contactNum = contact.getText().toString();
                    i.setData(Uri.parse("tel:"+contactNum));
                    if(ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                        requestPermission();
                    }else{
                        context.startActivity(i);
                    }

                }

                private void requestPermission(){
                    ActivityCompat.requestPermissions((Activity) context, new String[] {Manifest.permission.CALL_PHONE},1);
                }
            });
        }



        public void onClick2(final int position) {

            btnMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_CALL);
                    String contactNum = contact.getText().toString();
                    i.setData(Uri.parse("tel:"+contactNum));
                    if(ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                        requestPermission();
                    }else{
                        context.startActivity(i);
                    }

                }

                private void requestPermission(){
                    ActivityCompat.requestPermissions((Activity) context, new String[] {Manifest.permission.CALL_PHONE},1);
                }
            });
        }
    }
}