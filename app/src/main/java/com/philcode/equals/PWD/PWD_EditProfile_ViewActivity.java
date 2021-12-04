package com.philcode.equals.PWD;

import static android.view.View.GONE;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.philcode.equals.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PWD_EditProfile_ViewActivity extends Activity{

    private FloatingActionButton fab_main, fab1_personalInfo, fab2_jobInfo, fab3_addWorkExpInfo;
    private Animation fab_open, fab_close, fab_clock, fab_anticlock;
    TextView textview_personalInfo, textview_jobInfo, textview_AddWorkInfo;
    Button changePhoto;
    private List<PWD_AddWorkInformation> work_list;
    private PWD_WorkExperienceAdapter work_adapter;
    Boolean isOpen = false;
    private RecyclerView work_recyclerView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pwd_editprofile_view);

        final TextView displayTypeOfDisability1 = findViewById(R.id.displayTypeOfDisability1);
        final TextView displayTypeOfDisability2 = findViewById(R.id.displayTypeOfDisability2);
        final TextView displayTypeOfDisability3 = findViewById(R.id.displayTypeOfDisability3);
        final TextView displayTypeOfDisabilityMore = findViewById(R.id.displayTypeOfDisabilityMore);

        final TextView displaySkill1 = findViewById(R.id.displaySkill1);
        final TextView displaySkill2 = findViewById(R.id.displaySkill2);
        final TextView displaySkill3 = findViewById(R.id.displaySkill3);
        final TextView displaySkill4 = findViewById(R.id.displaySkill4);
        final TextView displaySkill5 = findViewById(R.id.displaySkill5);
        final TextView displaySkill6 = findViewById(R.id.displaySkill6);
        final TextView displaySkill7 = findViewById(R.id.displaySkill7);
        final TextView displaySkill8 = findViewById(R.id.displaySkill8);
        final TextView displaySkill9 = findViewById(R.id.displaySkill9);
        final TextView displaySkill10 = findViewById(R.id.displaySkill10);

        final ImageView images = findViewById(R.id.pwdProfilePic);


        final TextView displayName = findViewById(R.id.displayName);
        final TextView displayEmail = findViewById(R.id.displayEmail);
        final TextView displayAddress = findViewById(R.id.displayAddress);
        final TextView displayContact = findViewById(R.id.displayContact);


        final TextView displayEducationalAttainment = findViewById(R.id.displayEducationalAttainment);
        final TextView displayCategorySkill = findViewById(R.id.displayCategorySkill);
        final TextView displayTotalWorkExperience = findViewById(R.id.displayTotalWorkExperience);

        FirebaseAuth firebaseAuth;
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = currentFirebaseUser.getUid();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("PWD").child(uid);

        changePhoto = findViewById(R.id.changePhoto);

        work_recyclerView = findViewById(R.id.workRecyclerView);
        work_recyclerView.setHasFixedSize(true);
        work_recyclerView.setLayoutManager(new LinearLayoutManager(PWD_EditProfile_ViewActivity.this));

        //animation
        fab_main = findViewById(R.id.fab);
        fab1_personalInfo = findViewById(R.id.fab1);
        fab2_jobInfo = findViewById(R.id.fab2);
        fab3_addWorkExpInfo = findViewById(R.id.fab3);
        textview_personalInfo = findViewById(R.id.textview_personalInfo);
        textview_jobInfo = findViewById(R.id.textview_jobQualifications);
        textview_AddWorkInfo = findViewById(R.id.textview_AddWork);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_clock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_clock);
        fab_anticlock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_anticlock);

        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isOpen) {
                    textview_jobInfo.setVisibility(View.INVISIBLE);
                    textview_personalInfo.setVisibility(View.INVISIBLE);
                    textview_AddWorkInfo.setVisibility(View.INVISIBLE);
                    fab3_addWorkExpInfo.startAnimation(fab_close);
                    fab2_jobInfo.startAnimation(fab_close);
                    fab1_personalInfo.startAnimation(fab_close);
                    fab_main.startAnimation(fab_anticlock);
                    fab3_addWorkExpInfo.setClickable(false);
                    fab2_jobInfo.setClickable(false);
                    fab1_personalInfo.setClickable(false);
                    isOpen = false;
                } else {
                    textview_jobInfo.setVisibility(View.VISIBLE);
                    textview_personalInfo.setVisibility(View.VISIBLE);
                    textview_AddWorkInfo.setVisibility(View.VISIBLE);
                    fab3_addWorkExpInfo.startAnimation(fab_open);
                    fab2_jobInfo.startAnimation(fab_open);
                    fab1_personalInfo.startAnimation(fab_open);
                    fab_main.startAnimation(fab_clock);
                    fab3_addWorkExpInfo.setClickable(true);
                    fab2_jobInfo.setClickable(true);
                    fab1_personalInfo.setClickable(true);
                    isOpen = true;
                }

            }
        });

        fab3_addWorkExpInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PWD_EditProfile_ViewActivity.this, PWD_AddWorkExperience.class);
                startActivity(intent);
            }
        });
        fab2_jobInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PWD_EditProfile_ViewActivity.this, PWD_EditProfile2.class);
                startActivity(intent);
            }
        });


        fab1_personalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PWD_EditProfile_ViewActivity.this, PWD_EditProfile.class);
                startActivity(intent);

            }
        });

        changePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PWD_EditProfile_ViewActivity.this, PWD_EditProfile3.class);
                startActivity(intent);
            }
        });


        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String firstname = dataSnapshot.child("firstName").getValue().toString();
                String lastname = dataSnapshot.child("lastName").getValue().toString();
                String email = dataSnapshot.child("email").getValue().toString();
                String address = dataSnapshot.child("address").getValue().toString();
                String city = dataSnapshot.child("city").getValue().toString();
                String skill = dataSnapshot.child("skill").getValue().toString();

                 displayName.setText(firstname+" "+lastname);
                 displayEmail.setText(email);
                 displayAddress.setText(address+" "+city);
                 displayCategorySkill.setText(skill);

                if(dataSnapshot.child("typeOfDisability0").exists()) {
                    String typeOfDisability1 = dataSnapshot.child("typeOfDisability0").getValue().toString();
                    displayTypeOfDisability1.setText(typeOfDisability1);
                }else{
                    displayTypeOfDisability1.setVisibility(GONE);
                }
                if(dataSnapshot.child("typeOfDisability1").exists()) {
                    String typeOfDisability2 = dataSnapshot.child("typeOfDisability1").getValue().toString();
                    displayTypeOfDisability2.setText(typeOfDisability2);
                }else{
                    displayTypeOfDisability2.setVisibility(GONE);
                }
                if(dataSnapshot.child("typeOfDisability2").exists()) {
                    String typeOfDisability3 = dataSnapshot.child("typeOfDisability2").getValue().toString();
                    displayTypeOfDisability3.setText(typeOfDisability3);
                }else{
                    displayTypeOfDisability3.setVisibility(GONE);
                }
                if(dataSnapshot.child("typeOfDisability3").exists()) {
                    String typeOfDisabilityMore = dataSnapshot.child("typeOfDisability3").getValue().toString();
                    displayTypeOfDisabilityMore.setText(typeOfDisabilityMore);
                }else{
                    displayTypeOfDisabilityMore.setVisibility(GONE);
                }
                /////////////////////////////////// Secondary skills
                if(dataSnapshot.child("jobSkills0").exists()){
                    displaySkill1.setText(dataSnapshot.child("jobSkills0").getValue().toString());
                }else{
                    displaySkill1.setVisibility(GONE);
                }

                if(dataSnapshot.child("jobSkills1").exists()){
                    displaySkill2.setText(dataSnapshot.child("jobSkills1").getValue().toString());
                }else{
                    displaySkill2.setVisibility(GONE);

                }
                if(dataSnapshot.child("jobSkills2").exists()){
                    displaySkill3.setText(dataSnapshot.child("jobSkills2").getValue().toString());
                }else{
                    displaySkill3.setVisibility(GONE);
                }

                if(dataSnapshot.child("jobSkills3").exists()){
                    displaySkill4.setText(dataSnapshot.child("jobSkills3").getValue().toString());
                }else{
                    displaySkill4.setVisibility(GONE);

                }

                if(dataSnapshot.child("jobSkills4").exists()){
                    displaySkill5.setText(dataSnapshot.child("jobSkills4").getValue().toString());
                }else{
                    displaySkill5.setVisibility(GONE);

                }

                if(dataSnapshot.child("jobSkills5").exists()){
                    displaySkill6.setText(dataSnapshot.child("jobSkills5").getValue().toString());
                }else{
                    displaySkill6.setVisibility(GONE);

                }

                if(dataSnapshot.child("jobSkills6").exists()){
                    displaySkill7.setText(dataSnapshot.child("jobSkills6").getValue().toString());
                }else{
                    displaySkill7.setVisibility(GONE);

                }

                if(dataSnapshot.child("jobSkills7").exists()){
                    displaySkill8.setText(dataSnapshot.child("jobSkills7").getValue().toString());
                }else{
                    displaySkill8.setVisibility(GONE);
                }

                if(dataSnapshot.child("jobSkills8").exists()){
                    displaySkill9.setText(dataSnapshot.child("jobSkills8").getValue().toString());
                }else{
                    displaySkill9.setVisibility(GONE);
                }
                if(dataSnapshot.child("jobSkills9").exists()){
                    displaySkill10.setText(dataSnapshot.child("jobSkills9").getValue().toString());
                }else{
                    displaySkill10.setVisibility(GONE);

                }
                /////////////////////////////////// Secondary skills

                String pwdProfilePic = dataSnapshot.child("pwdProfilePic").getValue().toString();
                Glide.with(getApplicationContext()).load(pwdProfilePic).into(images);


                String educationalAttainment = dataSnapshot.child("educationalAttainment").getValue().toString();
                String workExperience = dataSnapshot.child("workExperience").getValue().toString();
                String contact = dataSnapshot.child("contact").getValue().toString();

                DatabaseReference noice = FirebaseDatabase.getInstance().getReference().child("PWD").child(uid).child("listOfWorks");
                if(workExperience.equals("With Experience")){
                    work_recyclerView.setVisibility(View.VISIBLE);
                    noice.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            work_list = new ArrayList<>();
                            work_list.clear();
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                PWD_AddWorkInformation p = dataSnapshot1.getValue(PWD_AddWorkInformation.class);
                                work_list.add(p);
                            }
                            Collections.reverse(work_list);
                            work_adapter = new PWD_WorkExperienceAdapter(PWD_EditProfile_ViewActivity.this, work_list);
                            work_recyclerView.setAdapter(work_adapter);
                            work_adapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(PWD_EditProfile_ViewActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
                    displayTotalWorkExperience.setText("Scroll down to view work experience list.");
                }else{
                    displayTotalWorkExperience.setText(workExperience);
                }
                displayEducationalAttainment.setText(educationalAttainment);

                displayContact.setText(contact);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            final Intent intent = new Intent(this, a_PWDContentMainActivity.class);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }
}