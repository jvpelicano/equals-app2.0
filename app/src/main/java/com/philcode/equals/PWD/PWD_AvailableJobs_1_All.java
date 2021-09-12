package com.philcode.equals.PWD;

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
import com.philcode.equals.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class PWD_AvailableJobs_1_All extends AppCompatActivity {

    DatabaseReference reference, refForJobs;
    RecyclerView recyclerView;
    ArrayList<PWD_AvailableJobs_1_All_Information> list = new ArrayList<>();
    PWD_AvailableJobs_MyAdapter adapter;
    private FirebaseAuth firebaseAuth;
    SimpleDateFormat format = new SimpleDateFormat("MMMM dd, yyyy");
    Date c = Calendar.getInstance().getTime();
    long currentTime = c.getTime();
    String z = "Approved";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pwd_available_jobs);

        recyclerView = findViewById(R.id.myRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        refForJobs = FirebaseDatabase.getInstance().getReference().child("Job_Offers");
        // reference = FirebaseDatabase.getInstance().getReference().child("PWD");
        refForJobs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String a = dataSnapshot1.child("expDate").getValue(String.class);
                    String b = dataSnapshot1.child("permission").getValue(String.class);
                    if (b.equals(z)) {
                        //   Toast.makeText(getApplicationContext(), z+""+b, Toast.LENGTH_LONG).show();
                        try {
                            Date date = format.parse(a);
                            long expirationDate = date.getTime();
                            if (currentTime < expirationDate) {

                                PWD_AvailableJobs_1_All_Information p = dataSnapshot1.getValue(PWD_AvailableJobs_1_All_Information.class);
                                list.add(p);

                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    Collections.reverse(list);
                    adapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_1_All.this, list);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PWD_AvailableJobs_1_All.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}