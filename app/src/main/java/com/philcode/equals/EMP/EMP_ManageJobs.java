package com.philcode.equals.EMP;

import android.content.Context;
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
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    EMP_ManageJobs_Information p = dataSnapshot1.getValue(EMP_ManageJobs_Information.class);
                    list.add(p);
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
}