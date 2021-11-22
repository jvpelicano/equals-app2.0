package com.philcode.equals.PWD;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.philcode.equals.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class PWD_AvailableJobs_View extends AppCompatActivity {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userId = user.getUid();
    public static final String Name = "nameKey";
    FirebaseDatabase fDb;
    DatabaseReference jobOffersRef;

    TextView m_displayCompanyName, m_displayPostDescription, m_displayPostLocation,
            m_displayCategorySkill, m_displaySkill1, m_displaySkill2, m_displaySkill3, m_displaySkill4, m_displaySkill5,
            m_displaySkill6, m_displaySkill7, m_displaySkill8, m_displaySkill9, m_displaySkill10, m_displayEducationalAttainment,
            m_displayTotalWorkExperience, m_displayTypeOfDisability1, m_displayTypeOfDisability2,
            m_displayTypeOfDisability3, m_displayTypeOfDisabilityOthers, m_displayExpDate, m_displayPermission,
            m_displayPostTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pwd_availablejobs_view);

        m_displayPostTitle = findViewById(R.id.displayPostTitle);
        m_displayCompanyName = findViewById(R.id.displayCompanyName);
        m_displayPostDescription = findViewById(R.id.displayPostDescription);
        m_displayPostLocation = findViewById(R.id.displayPostLocation);
        m_displayCategorySkill = findViewById(R.id.displayCategorySkill);
        m_displaySkill1 = findViewById(R.id.displaySkill1);
        m_displaySkill2 = findViewById(R.id.displaySkill2);
        m_displaySkill3 = findViewById(R.id.displaySkill3);
        m_displaySkill4 = findViewById(R.id.displaySkill4);
        m_displaySkill5 = findViewById(R.id.displaySkill5);
        m_displaySkill6 = findViewById(R.id.displaySkill6);
        m_displaySkill7 = findViewById(R.id.displaySkill7);
        m_displaySkill8 = findViewById(R.id.displaySkill8);
        m_displaySkill9 = findViewById(R.id.displaySkill9);
        m_displaySkill10 = findViewById(R.id.displaySkill10);
        m_displayEducationalAttainment = findViewById(R.id.displayEducationalAttainment);
        m_displayTotalWorkExperience = findViewById(R.id.displayTotalWorkExperience);
        m_displayTypeOfDisability1 = findViewById(R.id.displayTypeOfDisability1);
        m_displayTypeOfDisability2 = findViewById(R.id.displayTypeOfDisability2);
        m_displayTypeOfDisability3 = findViewById(R.id.displayTypeOfDisability3);
        m_displayTypeOfDisabilityOthers = findViewById(R.id.displayTypeOfDisabilityMore);
        m_displayExpDate = findViewById(R.id.displayExpDate);
        m_displayPermission = findViewById(R.id.displayPermission);

        final String postJobID = getIntent().getStringExtra("POST_ID");
        Toast.makeText(this, postJobID, Toast.LENGTH_SHORT).show();
        fDb = FirebaseDatabase.getInstance();
        jobOffersRef = fDb.getReference().child("Job_Offers").child(postJobID);
        jobOffersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final String postTitle = snapshot.child("postTitle").getValue().toString();
                final String companyName = snapshot.child("companyName").getValue().toString();
                final String postDescription = snapshot.child("postDescription").getValue().toString();
                final String postLoc = snapshot.child("postLocation").getValue().toString();
                final String skillCategory = snapshot.child("skill").getValue().toString();
                final String educationalAttainment = snapshot.child("educationalAttainment").getValue().toString();
                final String workExperience = snapshot.child("yearsOfExperience").getValue().toString();
                final String postExpDate = snapshot.child("expDate").getValue().toString();

                ArrayList<String> jobSkillList = new ArrayList<>();
                ArrayList<String> typeOfDisabilityList = new ArrayList<>();
                for(int counter = 1; counter <= 10; counter++){
                    if(snapshot.hasChild("jobSkill" + counter)){
                        jobSkillList.add(snapshot.child("jobSkill" + counter).getValue(String.class));
                    }
                }

                for(int counter_a = 1; counter_a <= 3; counter_a++){
                    if(snapshot.hasChild("typeOfDisability" + counter_a)){
                        typeOfDisabilityList.add(snapshot.child("typeOfDisability" + counter_a).getValue(String.class));
                    }

                }
                if(snapshot.hasChild("typeOfDisabilityMore")){
                    String typeOfDisabilityMore = snapshot.child("typeOfDisabilityMore").getValue(String.class);
                }else{
                    String typeOfDisabilityMore = "";
                }
                setUserInfo(postTitle, companyName, postDescription, postLoc, skillCategory, educationalAttainment, workExperience, postExpDate);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void setUserInfo(String postTitle, String companyName, String postDescription, String postLoc, String skillCategory, String educationalAttainment, String workExperience, String postExpDate){
        m_displayPostTitle.setText(postTitle);
        m_displayCompanyName.setText(companyName);
        m_displayPostDescription.setText(postDescription);
        m_displayPostLocation.setText(postLoc);
        m_displayCategorySkill.setText(skillCategory);
        m_displayEducationalAttainment.setText(educationalAttainment);
        m_displayTotalWorkExperience.setText(workExperience); //add an if else statement for when the user have work experience.
        m_displayExpDate.setText(postExpDate);
    }
}

