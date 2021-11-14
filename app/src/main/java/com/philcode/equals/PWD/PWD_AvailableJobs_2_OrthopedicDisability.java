package com.philcode.equals.PWD;

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

public class PWD_AvailableJobs_2_OrthopedicDisability extends AppCompatActivity {
    DatabaseReference refForJobs, refUser;
    int countMatchedDisability;
    int countMatchedPrimarySkill;
    int countPWDDisability;
    int countPostDisability;
    int pwdNumberOfPrimarySkill;
    RecyclerView recyclerView;
    ArrayList<PWD_AvailableJobs_1_All_Information> list = new ArrayList<>();
    PWD_AvailableJobs_MyAdapter adapter;
    String typeOfDisability1;
    String typeOfDisability2;
    String typeOfDisability3;
    String typeOfDisability4;
    String d1 = "Orthopedic Disability";
    String d2 = "Partial Vision Disability";
    String d3 = "Hearing Disability";
    String d4 = "Other Disabilities";
    String pwdCity;
    String pwdSkill;
    String pwdSkill1;
    String pwdSkill2;
    String pwdSkill3;
    String pwdSkill4;
    String pwdSkill5;
    String pwdSkill6;
    String pwdSkill7;
    String pwdSkill8;
    String pwdSkill9;
    String pwdSkill10;
    String primarySkillOther;
    String pwdEducAttainment;
    String pwdWorkExperience;
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
        refUser = FirebaseDatabase.getInstance().getReference().child("PWD/" + userId);
        refUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //PWD
                pwdCity = dataSnapshot.child("city").getValue(String.class);
                pwdSkill = dataSnapshot.child("skill").getValue(String.class);

                typeOfDisability1 = dataSnapshot.child("typeOfDisability1").getValue(String.class);
                typeOfDisability2 = dataSnapshot.child("typeOfDisability2").getValue(String.class);
                typeOfDisability3 = dataSnapshot.child("typeOfDisability3").getValue(String.class);
                typeOfDisability4 = dataSnapshot.child("typeOfDisabilityMore").getValue(String.class);
                pwdWorkExperience = dataSnapshot.child("workExperience").getValue(String.class);
                pwdEducAttainment = dataSnapshot.child("educationalAttainment").getValue(String.class);
                pwdNumberOfPrimarySkill = dataSnapshot.child("numberOfPrimarySkills").getValue(Integer.class);

                countPWDDisability = 0;
                if (typeOfDisability1.equals(d1)) {
                    countPWDDisability++;
                }
                if (typeOfDisability2.equals(d2)) {
                    countPWDDisability++;
                }
                if (typeOfDisability3.equals(d3)) {
                    countPWDDisability++;
                }
                if (typeOfDisability4.equals(d4)) {
                    countPWDDisability++;
                }
                //Check Job Offer Info
                refForJobs = FirebaseDatabase.getInstance().getReference().child("Job_Offers");
                refForJobs.orderByChild("typeOfDisability1").equalTo("Orthopedic Disability").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            //Look jor Job offers with Orthopedic Disability
                            String a = dataSnapshot1.child("expDate").getValue(String.class);
                            String b = dataSnapshot1.child("permission").getValue(String.class);
                            String postTypeOfDisability1 = dataSnapshot1.child("typeOfDisability1").getValue(String.class);
                            String postTypeOfDisability2 = dataSnapshot1.child("typeOfDisability2").getValue(String.class);
                            String postTypeOfDisability3 = dataSnapshot1.child("typeOfDisability3").getValue(String.class);
                            String postTypeOfDisability4 = dataSnapshot1.child("typeOfDisabilityMore").getValue(String.class);
                            String postworkExperience = dataSnapshot1.child("workExperience").getValue(String.class);
                            String posteducationalAttainment = dataSnapshot1.child("educationalAttainment").getValue(String.class);

                            //to fix
                            if (pwdWorkExperience.toLowerCase().equals(postworkExperience.toLowerCase())) {
                                if (pwdEducAttainment.toLowerCase().equals(posteducationalAttainment.toLowerCase())) {
                                    countPostDisability = 0;
                                    //count the disabilities
                                    if (postTypeOfDisability1.equals(d1)) {
                                        countPostDisability++;
                                    }
                                    if (postTypeOfDisability2.equals(d2)) {
                                        countPostDisability++;
                                    }
                                    if (postTypeOfDisability3.equals(d3)) {
                                        countPostDisability++;
                                    }
                                    if (postTypeOfDisability4.equals(d4)) {
                                        countPostDisability++;
                                    }

                                    countMatchedDisability = 0;
                                    if (typeOfDisability1.equals(postTypeOfDisability1) && typeOfDisability1.equals(d1) && postTypeOfDisability1.equals(d1)) {
                                        countMatchedDisability++;
                                    }
                                    if (typeOfDisability2.equals(postTypeOfDisability2) && typeOfDisability2.equals(d2) && postTypeOfDisability2.equals(d2)) {
                                        countMatchedDisability++;
                                    }
                                    if (typeOfDisability3.equals(postTypeOfDisability3) && typeOfDisability3.equals(d3) && postTypeOfDisability3.equals(d3)) {
                                        countMatchedDisability++;
                                    }
                                    if (typeOfDisability4.equals(postTypeOfDisability4) && typeOfDisability4.equals(d4) && postTypeOfDisability4.equals(d4)) {
                                        countMatchedDisability++;
                                    }

                                }
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
