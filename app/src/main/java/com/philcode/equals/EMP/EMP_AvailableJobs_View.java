package com.philcode.equals.EMP;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.philcode.equals.R;

import java.util.ArrayList;

public class EMP_AvailableJobs_View extends AppCompatActivity {

    private FloatingActionButton fab_main, fab1_resumes, fab2_potential, fab3_delete;
    private Animation fab_open, fab_close, fab_clock, fab_anticlock;
    TextView textview_resume, textview_potential, textView_delete;
    TextView m_displayCompanyName, m_displayPostDescription, m_displayPostLocation,
    m_displayCategorySkill, m_displayJobSkillsList, m_displayEducationalAttainment,
    m_displayTotalWorkExperience, m_displayTypeOfDisabilitiesList, m_displayTypeOfDisabilityOthers, m_displayExpDate, m_displayPermission,
    m_displayPostTitle;

    ImageView m_displayPostPic, m_displayCompanyLogo;
    FirebaseDatabase fDb;
    DatabaseReference jobOffersRef, pwdRef;
    Boolean isOpen = false;
    private String companyLogoURL, postTitle,companyName, postDescription,postLoc,skillCategory,
            educationalAttainment, workExperience,postExpDate,permission,imageURL;
    DatabaseReference refForJobs;
    private static final String TAG = "PWD_AvailableJobs_View";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emp_availablejobs_view);
        Log.d(TAG, "onCreate: started.");

        //layout
        m_displayPostTitle = findViewById(R.id.displayPostTitle);
        m_displayCompanyName = findViewById(R.id.displayCompanyName);
        m_displayPostDescription = findViewById(R.id.displayPostDescription);
        m_displayPostLocation = findViewById(R.id.displayPostLocation);
        m_displayCategorySkill = findViewById(R.id.displayCategorySkill);
        m_displayJobSkillsList = findViewById(R.id.displaySkill1);
        m_displayEducationalAttainment = findViewById(R.id.displayEducationalAttainment);
        m_displayTotalWorkExperience = findViewById(R.id.displayTotalWorkExperience);
        m_displayTypeOfDisabilitiesList = findViewById(R.id.displayTypeOfDisability1);
        m_displayTypeOfDisabilityOthers = findViewById(R.id.displayTypeOfDisabilityMore);
        m_displayExpDate = findViewById(R.id.displayExpDate);
        m_displayPermission = findViewById(R.id.displayPermission);
        m_displayPostPic = findViewById(R.id.displayPostPic);
        m_displayCompanyLogo = findViewById(R.id.displayCompanyLogo);

        //animation
        fab_main = findViewById(R.id.fab);
        fab1_resumes = findViewById(R.id.fab1);
        fab2_potential = findViewById(R.id.fab2);
        fab3_delete = findViewById(R.id.fab3);
        textview_resume = findViewById(R.id.textview_resume);
        textview_potential = findViewById(R.id.textview_potential);
        textView_delete = findViewById(R.id.textview_delete);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_clock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_clock);
        fab_anticlock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_anticlock);

        final String postJobID = getIntent().getStringExtra("POST_ID");
        //Toast.makeText(this, postJobID, Toast.LENGTH_SHORT).show();
        fDb = FirebaseDatabase.getInstance();
        jobOffersRef = fDb.getReference().child("Job_Offers").child(postJobID);
        jobOffersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("postTitle")){
                    postTitle = snapshot.child("postTitle").getValue().toString();
                }
                if(snapshot.hasChild("companyName")){
                    companyName = snapshot.child("companyName").getValue().toString();
                }
                if(snapshot.hasChild("postDescription")){
                    postDescription = snapshot.child("postDescription").getValue().toString();
                }
                if(snapshot.hasChild("postLocation")){
                    postLoc = snapshot.child("postLocation").getValue().toString();
                }
                if(snapshot.hasChild("skill")){
                    skillCategory = snapshot.child("skill").getValue().toString();
                }
                if(snapshot.hasChild("educationalAttainment")){
                    educationalAttainment = snapshot.child("educationalAttainment").getValue().toString();
                }
                if(snapshot.hasChild("yearsOfExperience")){
                    workExperience = snapshot.child("yearsOfExperience").getValue().toString();
                }
                if(snapshot.hasChild("expDate")){
                    postExpDate = snapshot.child("expDate").getValue().toString();
                }
                if(snapshot.hasChild("permission")){
                    permission = snapshot.child("permission").getValue().toString();

                }
                if(snapshot.hasChild("imageURL")){
                    imageURL = snapshot.child("imageURL").getValue().toString();
                }

                if(snapshot.hasChild("empProfilePic")){
                    companyLogoURL = snapshot.child("empProfilePic").getValue().toString();
                }else{
                    companyLogoURL = null;
                }

                ArrayList<String> jobSkillList = new ArrayList<>();
                ArrayList<String> typeOfDisabilityList = new ArrayList<>();
                for(int counter = 1; counter <= 10; counter++){
                    if(snapshot.hasChild("jobSkill" + counter)){
                        if(snapshot.hasChild("jobSkill" + counter) && !snapshot.child("jobSkill" + counter).getValue().toString().equals("")){
                            jobSkillList.add(snapshot.child("jobSkill" + counter).getValue(String.class));
                        }
                    }
                }

                for(int counter_a = 1; counter_a <= 3; counter_a++){
                    if(snapshot.hasChild("typeOfDisability" + counter_a)){
                        if(snapshot.hasChild("typeOfDisability" + counter_a) && !snapshot.child("typeOfDisability" + counter_a).getValue().toString().equals("")){
                            typeOfDisabilityList.add(snapshot.child("typeOfDisability" + counter_a).getValue(String.class));
                        }
                    }
                }
                if(snapshot.hasChild("typeOfDisabilityMore")){
                    typeOfDisabilityList.add(snapshot.child("typeOfDisabilityMore").getValue(String.class));
                }
                setUserInfo(jobSkillList, typeOfDisabilityList, postTitle, companyName, postDescription, postLoc, skillCategory, educationalAttainment, workExperience, postExpDate, permission
                        , imageURL, companyLogoURL);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isOpen) {
                    textview_resume.setVisibility(View.INVISIBLE);
                    textview_potential.setVisibility(View.INVISIBLE);
                    textView_delete.setVisibility(View.INVISIBLE);
                    fab3_delete.startAnimation(fab_close);
                    fab2_potential.startAnimation(fab_close);
                    fab1_resumes.startAnimation(fab_close);
                    fab3_delete.setClickable(false);
                    fab2_potential.setClickable(false);
                    fab1_resumes.setClickable(false);
                    isOpen = false;
                } else {
                    textview_resume.setVisibility(View.VISIBLE);
                    textview_potential.setVisibility(View.VISIBLE);
                    textView_delete.setVisibility(View.VISIBLE);
                    fab3_delete.startAnimation(fab_open);
                    fab2_potential.startAnimation(fab_open);
                    fab1_resumes.startAnimation(fab_open);
                    fab3_delete.setClickable(true);
                    fab2_potential.setClickable(true);
                    fab1_resumes.setClickable(true);
                    isOpen = true;
                }

            }
        });

        fab2_potential.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(EMP_AvailableJobs_View.this, EMP_ViewPotential_All.class);
                startActivity(i);

            }
        });

        fab3_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert =  new AlertDialog.Builder(EMP_AvailableJobs_View.this);
                alert.setMessage("This post will be deleted and you won't be able to find it anymore.").setCancelable(false)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final String postJobID = getIntent().getStringExtra("POST_ID");
                                jobOffersRef = FirebaseDatabase.getInstance().getReference().child("Job_Offers");
                                jobOffersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Job_Offers");
                                            ref.child(postJobID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    getWindow().getDecorView().post(new Runnable() {

                                                        @Override
                                                        public void run() {
                                                            finish();
                                                            startActivity(new Intent(EMP_AvailableJobs_View.this, EMP_ManageJobs.class));
                                                        }

                                                    });
                                                }
                                            });
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = alert.create();
                alertDialog.setTitle("Delete Post?");
                alertDialog.show();

            }
        });

        fab1_resumes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            //Toast.makeText(EMP_AvailableJobs_View.this, "Button is working!", Toast.LENGTH_LONG).show();

                refForJobs = FirebaseDatabase.getInstance().getReference().child("Job_Offers");
                refForJobs.child(postJobID).addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild("Resume")) {
                                Intent i = new Intent(getApplicationContext(), EMP_ViewResume.class);
                                i.putExtra("POST_ID", postJobID);
                                startActivity(i);
                                return;
                            } else {
                                AlertDialog.Builder alert =  new AlertDialog.Builder(EMP_AvailableJobs_View.this);
                                alert.setMessage("You haven't received resume from applicants yet.").setCancelable(false)
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();

                                            }
                                        });
                                AlertDialog alertDialog = alert.create();
                                alertDialog.setTitle("No Resume Available");
                                alertDialog.show();
                            }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

        });

    }

    private void setUserInfo(ArrayList<String> jobSkillList, ArrayList<String> typeOfDisabilityList, String postTitle, String companyName, String postDescription, String postLoc, String skillCategory, String educationalAttainment,
                             String workExperience, String postExpDate, String permission, String imageURL, String companyLogoUrl) {
        m_displayPostTitle.setText(postTitle);
        m_displayCompanyName.setText(companyName);
        m_displayPostDescription.setText(postDescription);
        m_displayPostLocation.setText(postLoc);
        m_displayCategorySkill.setText(skillCategory);
        m_displayEducationalAttainment.setText(educationalAttainment);
        m_displayTotalWorkExperience.setText(workExperience + " years");
        m_displayExpDate.setText(postExpDate);
        m_displayPermission.setText(permission);

        StringBuilder jobSkillList_builder = new StringBuilder();
        for(String jobSkillList1 : jobSkillList){
            jobSkillList_builder.append(jobSkillList1 + "\n");
        }
        m_displayJobSkillsList.setText(jobSkillList_builder.toString());

        StringBuilder typeOfDisability_builder = new StringBuilder();
        for(String typeOfDisabilityList1 : typeOfDisabilityList) {
            typeOfDisability_builder.append(typeOfDisabilityList1 + "\n");
        }
        m_displayTypeOfDisabilitiesList.setText(typeOfDisability_builder.toString());
        Glide.with(getApplicationContext()).load(imageURL).into(m_displayPostPic);
        if(companyLogoUrl == null){
            m_displayCompanyLogo.setVisibility(View.GONE);
        }else{
            Glide.with(getApplicationContext()).load(companyLogoUrl).into(m_displayCompanyLogo);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        finish();
    }
}