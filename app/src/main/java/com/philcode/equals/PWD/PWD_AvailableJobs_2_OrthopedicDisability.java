package com.philcode.equals.PWD;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.philcode.equals.R;

import java.util.ArrayList;

public class PWD_AvailableJobs_2_OrthopedicDisability extends AppCompatActivity {
    DatabaseReference refForJobs, refUser;

    RecyclerView recyclerView;
    PWD_AvailableJobs_MyAdapter myAdapter;

    ArrayList<PWD_AvailableJobs_Model> list;
    SwitchMaterial switchPriority;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pwd_available_jobs);
        list = new ArrayList<>();

        recyclerView = findViewById(R.id.myRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        switchPriority = findViewById(R.id.switchPriority);
        refUser = FirebaseDatabase.getInstance().getReference().child("PWD/" + userId);
        refUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot pwd_dataSnapshot) {
                //Check Job Offer Info
                if(pwd_dataSnapshot.hasChild("typeOfDisability0")){ // checking PWD
                    refForJobs = FirebaseDatabase.getInstance().getReference().child("Job_Offers");
                    refForJobs.orderByChild("typeOfDisability1").equalTo("Orthopedic Disability").addValueEventListener(new ValueEventListener() { //checking Job_Offers
                        @Override
                        public void onDataChange(@NonNull DataSnapshot jobFetch_dataSnapshot1) {

                                for (DataSnapshot job_snapshot1 : jobFetch_dataSnapshot1.getChildren()) {
                                    //Look jor Job offers with Hearing Disability
                                    final String permission = job_snapshot1.child("permission").getValue(String.class);
                                    if(permission.equals("Approved")){
                                        String imageURL = job_snapshot1.child("imageURL").getValue(String.class);
                                        String displayPostTitle = job_snapshot1.child("postTitle").getValue(String.class);
                                        String displayCompanyName = job_snapshot1.child("companyName").getValue(String.class);
                                        String displayPostDate = job_snapshot1.child("postDate").getValue(String.class);

                                        String postID = job_snapshot1.getKey(); //correct


                                        PWD_AvailableJobs_Model pwd_Model = new PWD_AvailableJobs_Model(imageURL, displayPostTitle, displayCompanyName, displayPostDate, postID);
                                        list.add(pwd_Model);
                                        myAdapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_2_OrthopedicDisability.this, list);
                                        recyclerView.setAdapter(myAdapter);
                                        myAdapter.notifyDataSetChanged();
                                    }
                                }

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        switchPriority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switchPriority.isChecked()){
                    refUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot pwd_dataSnapshot) {
                            final String pwd_skillCategory = pwd_dataSnapshot.child("skill").getValue().toString();
                            //Check Job Offer Info
                            if(pwd_dataSnapshot.hasChild("typeOfDisability0")){ // checking PWD
                                refForJobs = FirebaseDatabase.getInstance().getReference().child("Job_Offers");
                                refForJobs.orderByChild("typeOfDisability1").equalTo("Orthopedic Disability").addValueEventListener(new ValueEventListener() { //checking Job_Offers
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot jobFetch_dataSnapshot1) {
                                        list.clear();
                                        for (DataSnapshot job_snapshot1 : jobFetch_dataSnapshot1.getChildren()) {
                                            //Look jor Job offers with Hearing Disability
                                            final String permission = job_snapshot1.child("permission").getValue(String.class);
                                            final String job_skillCategory = job_snapshot1.child("skill").getValue().toString();
                                            if(permission.equals("Approved") && job_skillCategory.equals(pwd_skillCategory)){
                                                String imageURL = job_snapshot1.child("imageURL").getValue(String.class);
                                                String displayPostTitle = job_snapshot1.child("postTitle").getValue(String.class);
                                                String displayCompanyName = job_snapshot1.child("companyName").getValue(String.class);
                                                String displayPostDate = job_snapshot1.child("postDate").getValue(String.class);

                                                String postID = job_snapshot1.getKey(); //correct


                                                PWD_AvailableJobs_Model pwd_Model = new PWD_AvailableJobs_Model(imageURL, displayPostTitle, displayCompanyName, displayPostDate, postID);
                                                list.add(pwd_Model);
                                                myAdapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_2_OrthopedicDisability.this, list);
                                                recyclerView.setAdapter(myAdapter);
                                                myAdapter.notifyDataSetChanged();
                                            }
                                        }

                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }else{
                    refUser = FirebaseDatabase.getInstance().getReference().child("PWD/" + userId);
                    refUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot pwd_dataSnapshot) {
                            //Check Job Offer Info
                            if(pwd_dataSnapshot.hasChild("typeOfDisability0")){ // checking PWD
                                refForJobs = FirebaseDatabase.getInstance().getReference().child("Job_Offers");
                                refForJobs.orderByChild("typeOfDisability1").equalTo("Orthopedic Disability").addValueEventListener(new ValueEventListener() { //checking Job_Offers
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot jobFetch_dataSnapshot1) {
                                        list.clear();
                                        for (DataSnapshot job_snapshot1 : jobFetch_dataSnapshot1.getChildren()) {
                                            //Look jor Job offers with Hearing Disability
                                            final String permission = job_snapshot1.child("permission").getValue(String.class);
                                            if(permission.equals("Approved")){
                                                String imageURL = job_snapshot1.child("imageURL").getValue(String.class);
                                                String displayPostTitle = job_snapshot1.child("postTitle").getValue(String.class);
                                                String displayCompanyName = job_snapshot1.child("companyName").getValue(String.class);
                                                String displayPostDate = job_snapshot1.child("postDate").getValue(String.class);

                                                String postID = job_snapshot1.getKey(); //correct


                                                PWD_AvailableJobs_Model pwd_Model = new PWD_AvailableJobs_Model(imageURL, displayPostTitle, displayCompanyName, displayPostDate, postID);
                                                list.add(pwd_Model);
                                                myAdapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_2_OrthopedicDisability.this, list);
                                                recyclerView.setAdapter(myAdapter);
                                                myAdapter.notifyDataSetChanged();
                                            }
                                        }

                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
            }
        });
    }
}
