package com.philcode.equals.PWD;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
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
import java.util.Locale;

public class PWD_AvailableJobs_4_HearingDisability extends AppCompatActivity {
    DatabaseReference refForJobs, refUser;
    RecyclerView recyclerView;
    ArrayList<PWD_Recycler_AvailableJobs_Model> list = new ArrayList<>();
    PWD_AvailableJobs_MyAdapter adapter;
    Date c = Calendar.getInstance().getTime();
    String z = "Approved";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pwd_available_jobs);
        recyclerView = findViewById(R.id.myRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        list = new ArrayList<>();
        refUser = FirebaseDatabase.getInstance().getReference().child("PWD/" + userId);
        refUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot pwd_dataSnapshot) {
                //Check Job Offer Info
                if(pwd_dataSnapshot.hasChild("typeOfDisability2")){
                    refForJobs = FirebaseDatabase.getInstance().getReference().child("Job_Offers");
                    refForJobs.orderByChild("typeOfDisability3").equalTo("Hearing Disability").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot jobFetch_dataSnapshot1) {

                                for (DataSnapshot job_snapshot1 : jobFetch_dataSnapshot1.getChildren()) {
                                    final String permission = job_snapshot1.child("permission").getValue(String.class);
                                    // looks for approved job_offers hiring people with VisualDisability
                                    if (permission.equals("Approved")) {
                                        String imageURL = job_snapshot1.child("imageURL").getValue(String.class);
                                        String displayPostTitle = job_snapshot1.child("postTitle").getValue(String.class);
                                        String displayCompanyName = job_snapshot1.child("companyName").getValue(String.class);
                                        String displayPostDate = job_snapshot1.child("postDate").getValue(String.class);

                                        PWD_Recycler_AvailableJobs_Model pwd_Model = new PWD_Recycler_AvailableJobs_Model(imageURL, displayPostTitle, displayCompanyName, displayPostDate);
                                        list.add(pwd_Model);
                                        //to fix
                                        adapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_4_HearingDisability.this, list);
                                        recyclerView.setAdapter(adapter);
                                        adapter.notifyDataSetChanged();
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
