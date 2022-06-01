package com.philcode.equals.PWD;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.philcode.equals.R;

import java.util.ArrayList;

public class PWD_ViewJobApplicationList extends AppCompatActivity {
    private DatabaseReference job_reference, pwd_reference;
    private FirebaseUser user;
    private String user_ID;
    private RecyclerView recycler_jobApplicationList;
    private ArrayList<String> jobKeys;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwd_jobapplicationlist);

        job_reference = FirebaseDatabase.getInstance().getReference().child("Job_Offers");
        pwd_reference = FirebaseDatabase.getInstance().getReference().child("PWD");
        user = FirebaseAuth.getInstance().getCurrentUser();
        user_ID = user.getUid();

        jobKeys = new ArrayList<>();

        recycler_jobApplicationList = findViewById(R.id.recycler_jobApplicationList);
        recycler_jobApplicationList.setLayoutManager(new LinearLayoutManager(this));

        pwd_reference.child(user_ID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot_keys : snapshot.child("jobs_keyList").getChildren()){
                    jobKeys.add(snapshot_keys.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}