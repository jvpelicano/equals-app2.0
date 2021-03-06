package com.philcode.equals.EMP;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.philcode.equals.PWD.PWD_AvailableJobs_MyAdapter;
import com.philcode.equals.R;

import java.util.ArrayList;
import java.util.Collections;

public class EMP_ManageJobs extends AppCompatActivity {

    DatabaseReference refForJobs;
    RecyclerView recyclerView;
    ArrayList<EMP_ManageJobs_Information> list = new ArrayList<>();
    EMP_ManageJobs_MyAdapter adapter;
    private FirebaseAuth firebaseAuth;
    Context c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emp_available_jobs);
        recyclerView = findViewById(R.id.myRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String userId = user.getUid();
        refForJobs = FirebaseDatabase.getInstance().getReference().child("Job_Offers");
        refForJobs.orderByChild("uid").equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String imageURL = dataSnapshot1.child("imageURL").getValue(String.class);
                    String displayPostTitle = dataSnapshot1.child("postTitle").getValue(String.class);
                    String displayCompanyName = dataSnapshot1.child("companyName").getValue(String.class);
                    String displayPostDate = dataSnapshot1.child("postDate").getValue(String.class);
                    String displayPermission = dataSnapshot1.child("permission").getValue(String.class);
                    String postID = dataSnapshot1.getKey(); //correct

                    EMP_ManageJobs_Information model = new EMP_ManageJobs_Information(imageURL, displayPostTitle, displayCompanyName, displayPostDate, postID, displayPermission);
                    list.add(model);
                }
                Collections.reverse(list);
                adapter = new EMP_ManageJobs_MyAdapter(EMP_ManageJobs.this, list);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(EMP_ManageJobs.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, a_EmployeeContentMainActivity.class);
        startActivity(i);
    }
}