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

public class PWD_AvailableJobs_5_MoreDisability extends AppCompatActivity {
    DatabaseReference refForJobs, refUser;
    int countMatchedDisability;
    int countMatchedPrimarySkill;
    int countPWDDisability;
    int countPostDisability;
    int pwdNumberOfPrimarySkill;
    RecyclerView recyclerView;
    ArrayList<PWD_Recycler_AvailableJobs_Model> list = new ArrayList<>();
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
                pwdSkill1 = dataSnapshot.child("primarySkill1").getValue(String.class);
                pwdSkill2 = dataSnapshot.child("primarySkill2").getValue(String.class);
                pwdSkill3 = dataSnapshot.child("primarySkill3").getValue(String.class);
                pwdSkill4 = dataSnapshot.child("primarySkill4").getValue(String.class);
                pwdSkill5 = dataSnapshot.child("primarySkill5").getValue(String.class);
                pwdSkill6 = dataSnapshot.child("primarySkill6").getValue(String.class);
                pwdSkill7 = dataSnapshot.child("primarySkill7").getValue(String.class);
                pwdSkill8 = dataSnapshot.child("primarySkill8").getValue(String.class);
                pwdSkill9 = dataSnapshot.child("primarySkill9").getValue(String.class);
                pwdSkill10 = dataSnapshot.child("primarySkill10").getValue(String.class);
                primarySkillOther = dataSnapshot.child("primarySkillOthers").getValue(String.class);
                typeOfDisability1 = dataSnapshot.child("typeOfDisability1").getValue(String.class);
                typeOfDisability2 = dataSnapshot.child("typeOfDisability2").getValue(String.class);
                typeOfDisability3 = dataSnapshot.child("typeOfDisability3").getValue(String.class);
                typeOfDisability4 = dataSnapshot.child("typeOfDisabilityMore").getValue(String.class);
                pwdWorkExperience = dataSnapshot.child("workExperience").getValue(String.class);
                pwdEducAttainment = dataSnapshot.child("educationalAttainment").getValue(String.class);
                pwdNumberOfPrimarySkill = dataSnapshot.child("numberOfPrimarySkills").getValue(Integer.class);

                if (pwdSkill1==null){
                    pwdSkill1 = "";
                }if (pwdSkill2==null){
                    pwdSkill2 = "";
                }if (pwdSkill3==null){
                    pwdSkill3 = "";
                }if (pwdSkill4==null){
                    pwdSkill4 = "";
                }if (pwdSkill5==null){
                    pwdSkill5 = "";
                }if (pwdSkill6==null){
                    pwdSkill6 = "";
                }if (pwdSkill7==null){
                    pwdSkill7 = "";
                }if (pwdSkill8==null){
                    pwdSkill8 = "";
                }if (pwdSkill9==null){
                    pwdSkill9 = "";
                }if (pwdSkill10==null){
                    pwdSkill10 = "";
                }
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
                refForJobs.orderByChild("typeOfDisabilityMore").equalTo("Other Disabilities").addValueEventListener(new ValueEventListener() {
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

                                    if (countPostDisability == countPWDDisability || countPostDisability >= countMatchedDisability || countMatchedDisability >= countPWDDisability) {
                                        String jobSkill = dataSnapshot1.child("skill").getValue(String.class);
                                        String jobSkill1 = dataSnapshot1.child("primarySkill1").getValue(String.class);
                                        String jobSkill2 = dataSnapshot1.child("primarySkill2").getValue(String.class);
                                        String jobSkill3 = dataSnapshot1.child("primarySkill3").getValue(String.class);
                                        String jobSkill4 = dataSnapshot1.child("primarySkill4").getValue(String.class);
                                        String jobSkill5 = dataSnapshot1.child("primarySkill5").getValue(String.class);
                                        String jobSkill6 = dataSnapshot1.child("primarySkill6").getValue(String.class);
                                        String jobSkill7 = dataSnapshot1.child("primarySkill7").getValue(String.class);
                                        String jobSkill8 = dataSnapshot1.child("primarySkill8").getValue(String.class);
                                        String jobSkill9 = dataSnapshot1.child("primarySkill9").getValue(String.class);
                                        String jobSkill10 = dataSnapshot1.child("primarySkill10").getValue(String.class);
                                        String jobSkillOthers = dataSnapshot1.child("primarySkillOther").getValue(String.class);
                                        if (jobSkill1 == null) {
                                            jobSkill1 = "";
                                        }
                                        if (jobSkill2 == null) {
                                            jobSkill2 = "";
                                        }
                                        if (jobSkill3 == null) {
                                            jobSkill3 = "";
                                        }
                                        if (jobSkill4 == null) {
                                            jobSkill4 = "";
                                        }
                                        if (jobSkill5 == null) {
                                            jobSkill5 = "";
                                        }
                                        if (jobSkill6 == null) {
                                            jobSkill6 = "";
                                        }
                                        if (jobSkill7 == null) {
                                            jobSkill7 = "";
                                        }
                                        if (jobSkill8 == null) {
                                            jobSkill8 = "";
                                        }
                                        if (jobSkill9 == null) {
                                            jobSkill9 = "";
                                        }
                                        if (jobSkill10 == null) {
                                            jobSkill10 = "";
                                        }
                                        if (jobSkillOthers == null) {
                                            jobSkillOthers = "";
                                        }
                                        String postCity = dataSnapshot1.child("city").getValue(String.class);
                                        if (jobSkillOthers.toLowerCase().equals(primarySkillOther.toLowerCase())) {
                                            int numberOfPrimarySkills = dataSnapshot1.child("numberOfPrimarySkills").getValue(int.class);
                                            try {
                                                Date date = format.parse(a);
                                                long expirationDate = date.getTime();
                                                if (currentTime < expirationDate) {

                                                    if (b.toLowerCase().equals(z.toLowerCase())) {
                                                        //Match skill
                                                        if (pwdSkill.equals(jobSkill)) {
                                                            //Match per skills
                                                            countMatchedPrimarySkill = 0;
                                                            if (pwdSkill1.equals(jobSkill1) && !(pwdSkill1.equals("")) && !(jobSkill1.equals(""))) {
                                                                countMatchedPrimarySkill++;
                                                            }
                                                            if (pwdSkill1.equals(jobSkill2) && !(pwdSkill1.equals("")) && !(jobSkill2.equals(""))) {
                                                                countMatchedPrimarySkill++;
                                                            }
                                                            if (pwdSkill1.equals(jobSkill3) && !(pwdSkill1.equals("")) && !(jobSkill3.equals(""))) {
                                                                countMatchedPrimarySkill++;
                                                            }
                                                            if (pwdSkill1.equals(jobSkill4) && !(pwdSkill1.equals("")) && !(jobSkill4.equals(""))) {
                                                                countMatchedPrimarySkill++;
                                                            }
                                                            if (pwdSkill1.equals(jobSkill5) && !(pwdSkill1.equals("")) && !(jobSkill5.equals(""))) {
                                                                countMatchedPrimarySkill++;
                                                            }
                                                            if (pwdSkill1.equals(jobSkill6) && !(pwdSkill1.equals("")) && !(jobSkill6.equals(""))) {
                                                                countMatchedPrimarySkill++;
                                                            }
                                                            if (pwdSkill1.equals(jobSkill7) && !(pwdSkill1.equals("")) && !(jobSkill7.equals(""))) {
                                                                countMatchedPrimarySkill++;
                                                            }
                                                            if (pwdSkill1.equals(jobSkill8) && !(pwdSkill1.equals("")) && !(jobSkill8.equals(""))) {
                                                                countMatchedPrimarySkill++;
                                                            }
                                                            if (pwdSkill1.equals(jobSkill9) && !(pwdSkill1.equals("")) && !(jobSkill9.equals(""))) {
                                                                countMatchedPrimarySkill++;
                                                            }
                                                            if (pwdSkill1.equals(jobSkill10) && !(pwdSkill1.equals("")) && !(jobSkill10.equals(""))) {
                                                                countMatchedPrimarySkill++;
                                                            }
                                                            if (pwdSkill2.equals(jobSkill2) && !(pwdSkill2.equals("")) && !(jobSkill2.equals(""))) {
                                                                countMatchedPrimarySkill++;
                                                            }
                                                            if (pwdSkill3.equals(jobSkill3) && !(pwdSkill3.equals("")) && !(jobSkill3.equals(""))) {
                                                                countMatchedPrimarySkill++;
                                                            }
                                                            if (pwdSkill4.equals(jobSkill4) && !(pwdSkill4.equals("")) && !(jobSkill4.equals(""))) {
                                                                countMatchedPrimarySkill++;
                                                            }
                                                            if (pwdSkill5.equals(jobSkill5) && !(pwdSkill5.equals("")) && !(jobSkill5.equals(""))) {
                                                                countMatchedPrimarySkill++;
                                                            }
                                                            if (pwdSkill6.equals(jobSkill6) && !(pwdSkill6.equals("")) && !(jobSkill6.equals(""))) {
                                                                countMatchedPrimarySkill++;
                                                            }
                                                            if (pwdSkill7.equals(jobSkill7) && !(pwdSkill7.equals("")) && !(jobSkill7.equals(""))) {
                                                                countMatchedPrimarySkill++;
                                                            }
                                                            if (pwdSkill8.equals(jobSkill8) && !(pwdSkill8.equals("")) && !(jobSkill8.equals(""))) {
                                                                countMatchedPrimarySkill++;
                                                            }
                                                            if (pwdSkill9.equals(jobSkill9) && !(pwdSkill9.equals("")) && !(jobSkill9.equals(""))) {
                                                                countMatchedPrimarySkill++;
                                                            }
                                                            if (pwdSkill10.equals(jobSkill10) && !(pwdSkill10.equals("")) && !(jobSkill10.equals(""))) {
                                                                countMatchedPrimarySkill++;
                                                            }

                                                            final int o = 1;
                                                            //
                                                            if((pwdNumberOfPrimarySkill>=numberOfPrimarySkills || countMatchedPrimarySkill<= numberOfPrimarySkills || countMatchedPrimarySkill!=0)&&countMatchedPrimarySkill>=1){

                                                                countMatchedPrimarySkill = 0;

                                                                PWD_Recycler_AvailableJobs_Model p = dataSnapshot1.getValue(PWD_Recycler_AvailableJobs_Model.class);
                                                                list.add(p);
                                                            }
                                                            Collections.reverse(list);
                                                            adapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_5_MoreDisability.this, list);
                                                            recyclerView.setAdapter(adapter);
                                                            adapter.notifyDataSetChanged();
                                                        }
                                                    }
                                                }
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                        }
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
