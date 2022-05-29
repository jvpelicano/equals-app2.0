package com.philcode.equals.PWD;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.philcode.equals.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class PWD_AvailableJobs_3_VisualDisability extends AppCompatActivity {
    DatabaseReference refForJobs, refUser, categories_root;
    RecyclerView recyclerView;
    ArrayList<PWD_AvailableJobs_Model> list = new ArrayList<>();
    private ArrayList <String> arrayList_jobTitles;
    PWD_AvailableJobs_MyAdapter myAdapter;
    Date c = Calendar.getInstance().getTime();
    SwitchMaterial switchPriority;
    TextView tv_noJobsAvailable;
    TextInputLayout textInputLayout_filterJobTitle, textInputLayout_filterSkillOrDisability;
    private AutoCompleteTextView autoComplete_filterSkillOrDisability, autoComplete_filterJobTitles;
    ImageView mascot;
    private ImageButton imageButton_filterJobTitle;
    private ArrayAdapter <String> arrayAdapter_jobTitles;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pwd_available_jobs);
        recyclerView = findViewById(R.id.myRecycler);
        tv_noJobsAvailable = findViewById(R.id.tv_noJobsAvailable);
        textInputLayout_filterJobTitle = findViewById(R.id.textInputLayout_filterJobTitles);
        mascot = findViewById(R.id.mascot);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        switchPriority = findViewById(R.id.switchPriority);
        categories_root = FirebaseDatabase.getInstance().getReference("Category");
        autoComplete_filterJobTitles = findViewById(R.id.autoComplete_filterJobTitles);

        imageButton_filterJobTitle = findViewById(R.id.search);

        arrayList_jobTitles = new ArrayList<>();
        getWindow().getDecorView().post(new Runnable() {

            @Override
            public void run() {
                mascot.setVisibility(View.GONE);
                tv_noJobsAvailable.setVisibility(View.GONE);
            }

        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();

        setExposedDropdownListJobTitle(userId);

        arrayAdapter_jobTitles = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arrayList_jobTitles);
        autoComplete_filterJobTitles.setAdapter(arrayAdapter_jobTitles);

        displayJobOffers(userId);

        imageButton_filterJobTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(arrayList_jobTitles.contains(autoComplete_filterJobTitles.getText().toString())){
                    filterByJobTitle(userId);
                }else{
                    displayJobOffers(userId);
                    Toast.makeText(PWD_AvailableJobs_3_VisualDisability.this, "Error invalid job title.", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    private void setExposedDropdownListJobTitle(String userId){

        refUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String chosenSkillCategory = snapshot.child("skillCategory").getValue().toString();

                categories_root.orderByChild("skill").equalTo(chosenSkillCategory).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snap_category_key : snapshot.getChildren()){
                            String parent = snap_category_key.getKey();

                            categories_root.child(parent).child("jobtitles").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot snap_jobTitles : snapshot.getChildren()){
                                        arrayList_jobTitles.add(snap_jobTitles.getValue().toString());
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void displayJobOffers(String userId){

        refUser = FirebaseDatabase.getInstance().getReference().child("PWD/" + userId);
        refUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot pwd_dataSnapshot) {
                Query query_filterToJobTitle = refForJobs.orderByChild("jobTitle").equalTo(autoComplete_filterJobTitles.getText().toString());
                final String pwd_SkillCategory = pwd_dataSnapshot.child("skill").getValue().toString();
                final String pwd_educationalAttainment = pwd_dataSnapshot.child("educationalAttainment").getValue().toString();
                final String pwd_workExp = pwd_dataSnapshot.child("workExperience").getValue().toString();
                final String pwd_location = pwd_dataSnapshot.child("city").getValue().toString();
                //Check Job Offer Info
                // checking PWD for type of disability
                refForJobs = FirebaseDatabase.getInstance().getReference().child("Job_Offers");
                autoComplete_filterJobTitles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        query_filterToJobTitle.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot snapshot_filterJobTitles : snapshot.getChildren()){
                                    final String permission = snapshot_filterJobTitles.child("permission").getValue(String.class);
                                    final String job_skillCategory = snapshot_filterJobTitles.child("skill").getValue().toString();
                                    final String job_educationalAttainmentRequirement = snapshot_filterJobTitles.child("educationalAttainmentRequirement").getValue().toString();
                                    final String job_educationalAttainment = snapshot_filterJobTitles.child("educationalAttainment").getValue().toString();
                                    final String job_workExp = snapshot_filterJobTitles.child("workExperience").getValue().toString();
                                    final String job_location = snapshot_filterJobTitles.child("city").getValue().toString();

                                    if(snapshot_filterJobTitles.child("typeOfDisability2").getValue().toString().equals("Partial Visual Disability")){
                                        if (permission.equals("Approved") && job_skillCategory.equals(pwd_SkillCategory)){
                                            //logic is even if the company posts a job that does not require experience
                                            //people with experience should still be able to see the job post.
                                            if(job_workExp.equals("With Experience")){
                                                if(pwd_workExp.equals("With Experience")){ // strictly checking if pwd has work experience otherwise the data for the job post will not show.
                                                    if(job_educationalAttainmentRequirement.equals("true")){
                                                        //if educRequirement for a job post is required, the system will check pwd's educAttainment level.
                                                        if(job_educationalAttainment.equals("Elementary Level")){// checks if job post educAttainment matches pwd's educAttainment
                                                            //no need to check pwd_educAttainment
                                                            if(job_location.equals(pwd_location)){
                                                                String imageURL = snapshot_filterJobTitles.child("imageURL").getValue(String.class);
                                                                String displayPostTitle = snapshot_filterJobTitles.child("postTitle").getValue(String.class);
                                                                String displayCompanyName = snapshot_filterJobTitles.child("companyName").getValue(String.class);
                                                                String displayPostDate = snapshot_filterJobTitles.child("postDate").getValue(String.class);

                                                                String postID = snapshot_filterJobTitles.getKey();

                                                                PWD_AvailableJobs_Model pwd_Model = new PWD_AvailableJobs_Model(imageURL, displayPostTitle, displayCompanyName, displayPostDate, postID);
                                                                list.add(pwd_Model);
                                                                if(!list.isEmpty()){
                                                                    recyclerView.setVisibility(View.VISIBLE);
                                                                    mascot.setVisibility(View.GONE);
                                                                    tv_noJobsAvailable.setVisibility(View.GONE);
                                                                    textInputLayout_filterJobTitle.setVisibility(View.VISIBLE);
                                                                    textInputLayout_filterSkillOrDisability.setVisibility(View.VISIBLE);
                                                                }else if(list.isEmpty()){
                                                                    recyclerView.setVisibility(View.GONE);
                                                                    mascot.setVisibility(View.VISIBLE);
                                                                    tv_noJobsAvailable.setVisibility(View.VISIBLE);
                                                                }
                                                                Collections.reverse(list);
                                                                myAdapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_3_VisualDisability.this, list);
                                                                recyclerView.setAdapter(myAdapter);
                                                                myAdapter.notifyDataSetChanged();

                                                            }
                                                        }else if(job_educationalAttainment.equals("High School Level") && (job_educationalAttainment.equals(pwd_educationalAttainment)
                                                                || pwd_educationalAttainment.equals("Associate Level")
                                                                || pwd_educationalAttainment.equals("Bachelor Level")
                                                                || pwd_educationalAttainment.equals("Master's Level")
                                                                || pwd_educationalAttainment.equals("Doctorate Level"))){

                                                            if(job_location.equals(pwd_location)){
                                                                String imageURL = snapshot_filterJobTitles.child("imageURL").getValue(String.class);
                                                                String displayPostTitle = snapshot_filterJobTitles.child("postTitle").getValue(String.class);
                                                                String displayCompanyName = snapshot_filterJobTitles.child("companyName").getValue(String.class);
                                                                String displayPostDate = snapshot_filterJobTitles.child("postDate").getValue(String.class);

                                                                String postID = snapshot_filterJobTitles.getKey();

                                                                PWD_AvailableJobs_Model pwd_Model = new PWD_AvailableJobs_Model(imageURL, displayPostTitle, displayCompanyName, displayPostDate, postID);
                                                                list.add(pwd_Model);
                                                                if(!list.isEmpty()){
                                                                    recyclerView.setVisibility(View.VISIBLE);
                                                                    mascot.setVisibility(View.GONE);
                                                                    tv_noJobsAvailable.setVisibility(View.GONE);
                                                                    textInputLayout_filterJobTitle.setVisibility(View.VISIBLE);
                                                                    textInputLayout_filterSkillOrDisability.setVisibility(View.VISIBLE);
                                                                }else if(list.isEmpty()){
                                                                    recyclerView.setVisibility(View.GONE);
                                                                    mascot.setVisibility(View.VISIBLE);
                                                                    tv_noJobsAvailable.setVisibility(View.VISIBLE);
                                                                }
                                                                Collections.reverse(list);
                                                                myAdapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_3_VisualDisability.this, list);
                                                                recyclerView.setAdapter(myAdapter);
                                                                myAdapter.notifyDataSetChanged();

                                                            }

                                                        }else if(job_educationalAttainment.equals("Associate Level") && (job_educationalAttainment.equals(pwd_educationalAttainment)
                                                                || pwd_educationalAttainment.equals("Bachelor Level")
                                                                || pwd_educationalAttainment.equals("Master's Level")
                                                                || pwd_educationalAttainment.equals("Doctorate Level"))){

                                                            if(job_location.equals(pwd_location)){
                                                                String imageURL = snapshot_filterJobTitles.child("imageURL").getValue(String.class);
                                                                String displayPostTitle = snapshot_filterJobTitles.child("postTitle").getValue(String.class);
                                                                String displayCompanyName = snapshot_filterJobTitles.child("companyName").getValue(String.class);
                                                                String displayPostDate = snapshot_filterJobTitles.child("postDate").getValue(String.class);

                                                                String postID = snapshot_filterJobTitles.getKey();

                                                                PWD_AvailableJobs_Model pwd_Model = new PWD_AvailableJobs_Model(imageURL, displayPostTitle, displayCompanyName, displayPostDate, postID);
                                                                list.add(pwd_Model);
                                                                if(!list.isEmpty()){
                                                                    recyclerView.setVisibility(View.VISIBLE);
                                                                    mascot.setVisibility(View.GONE);
                                                                    tv_noJobsAvailable.setVisibility(View.GONE);
                                                                    textInputLayout_filterJobTitle.setVisibility(View.VISIBLE);
                                                                    textInputLayout_filterSkillOrDisability.setVisibility(View.VISIBLE);
                                                                }else if(list.isEmpty()){
                                                                    recyclerView.setVisibility(View.GONE);
                                                                    mascot.setVisibility(View.VISIBLE);
                                                                    tv_noJobsAvailable.setVisibility(View.VISIBLE);
                                                                }
                                                                Collections.reverse(list);
                                                                myAdapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_3_VisualDisability.this, list);
                                                                recyclerView.setAdapter(myAdapter);
                                                                myAdapter.notifyDataSetChanged();

                                                            }

                                                        }else if(job_educationalAttainment.equals("Bachelor Level") && (job_educationalAttainment.equals(pwd_educationalAttainment)
                                                                || pwd_educationalAttainment.equals("Master's Level")
                                                                || pwd_educationalAttainment.equals("Doctorate Level"))){

                                                            if(job_location.equals(pwd_location)){
                                                                String imageURL = snapshot_filterJobTitles.child("imageURL").getValue(String.class);
                                                                String displayPostTitle = snapshot_filterJobTitles.child("postTitle").getValue(String.class);
                                                                String displayCompanyName = snapshot_filterJobTitles.child("companyName").getValue(String.class);
                                                                String displayPostDate = snapshot_filterJobTitles.child("postDate").getValue(String.class);

                                                                String postID = snapshot_filterJobTitles.getKey();

                                                                PWD_AvailableJobs_Model pwd_Model = new PWD_AvailableJobs_Model(imageURL, displayPostTitle, displayCompanyName, displayPostDate, postID);
                                                                list.add(pwd_Model);
                                                                if(!list.isEmpty()){
                                                                    recyclerView.setVisibility(View.VISIBLE);
                                                                    mascot.setVisibility(View.GONE);
                                                                    tv_noJobsAvailable.setVisibility(View.GONE);
                                                                    textInputLayout_filterJobTitle.setVisibility(View.VISIBLE);
                                                                    textInputLayout_filterSkillOrDisability.setVisibility(View.VISIBLE);
                                                                }else if(list.isEmpty()){
                                                                    recyclerView.setVisibility(View.GONE);
                                                                    mascot.setVisibility(View.VISIBLE);
                                                                    tv_noJobsAvailable.setVisibility(View.VISIBLE);
                                                                }
                                                                Collections.reverse(list);
                                                                myAdapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_3_VisualDisability.this, list);
                                                                recyclerView.setAdapter(myAdapter);
                                                                myAdapter.notifyDataSetChanged();

                                                            }

                                                        }else if(job_educationalAttainment.equals("Master's Level")&& (job_educationalAttainment.equals(pwd_educationalAttainment)
                                                                || pwd_educationalAttainment.equals("Doctorate Level"))){

                                                            if(job_location.equals(pwd_location)){
                                                                String imageURL = snapshot_filterJobTitles.child("imageURL").getValue(String.class);
                                                                String displayPostTitle = snapshot_filterJobTitles.child("postTitle").getValue(String.class);
                                                                String displayCompanyName = snapshot_filterJobTitles.child("companyName").getValue(String.class);
                                                                String displayPostDate = snapshot_filterJobTitles.child("postDate").getValue(String.class);

                                                                String postID = snapshot_filterJobTitles.getKey();

                                                                PWD_AvailableJobs_Model pwd_Model = new PWD_AvailableJobs_Model(imageURL, displayPostTitle, displayCompanyName, displayPostDate, postID);
                                                                list.add(pwd_Model);
                                                                if(!list.isEmpty()){
                                                                    recyclerView.setVisibility(View.VISIBLE);
                                                                    mascot.setVisibility(View.GONE);
                                                                    tv_noJobsAvailable.setVisibility(View.GONE);
                                                                    textInputLayout_filterJobTitle.setVisibility(View.VISIBLE);
                                                                    textInputLayout_filterSkillOrDisability.setVisibility(View.VISIBLE);
                                                                }else if(list.isEmpty()){
                                                                    recyclerView.setVisibility(View.GONE);
                                                                    mascot.setVisibility(View.VISIBLE);
                                                                    tv_noJobsAvailable.setVisibility(View.VISIBLE);
                                                                }
                                                                Collections.reverse(list);
                                                                myAdapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_3_VisualDisability.this, list);
                                                                recyclerView.setAdapter(myAdapter);
                                                                myAdapter.notifyDataSetChanged();

                                                            }

                                                        }else if(job_educationalAttainment.equals("Doctorate Level") && job_educationalAttainment.equals(pwd_educationalAttainment)){

                                                            if(job_location.equals(pwd_location)){
                                                                String imageURL = snapshot_filterJobTitles.child("imageURL").getValue(String.class);
                                                                String displayPostTitle = snapshot_filterJobTitles.child("postTitle").getValue(String.class);
                                                                String displayCompanyName = snapshot_filterJobTitles.child("companyName").getValue(String.class);
                                                                String displayPostDate = snapshot_filterJobTitles.child("postDate").getValue(String.class);

                                                                String postID = snapshot_filterJobTitles.getKey();

                                                                PWD_AvailableJobs_Model pwd_Model = new PWD_AvailableJobs_Model(imageURL, displayPostTitle, displayCompanyName, displayPostDate, postID);
                                                                list.add(pwd_Model);
                                                                if(!list.isEmpty()){
                                                                    recyclerView.setVisibility(View.VISIBLE);
                                                                    mascot.setVisibility(View.GONE);
                                                                    tv_noJobsAvailable.setVisibility(View.GONE);
                                                                    textInputLayout_filterJobTitle.setVisibility(View.VISIBLE);
                                                                    textInputLayout_filterSkillOrDisability.setVisibility(View.VISIBLE);
                                                                }else if(list.isEmpty()){
                                                                    recyclerView.setVisibility(View.GONE);
                                                                    mascot.setVisibility(View.VISIBLE);
                                                                    tv_noJobsAvailable.setVisibility(View.VISIBLE);
                                                                }
                                                                Collections.reverse(list);
                                                                myAdapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_3_VisualDisability.this, list);
                                                                recyclerView.setAdapter(myAdapter);
                                                                myAdapter.notifyDataSetChanged();

                                                            }

                                                        }
                                                    }//checks if job educRequirement is set to true
                                                    else{ // if educRequirement for a job post is not required, the system will not check pwd's educAttainment level.
                                                        if(job_location.equals(pwd_location)){
                                                            String imageURL = snapshot_filterJobTitles.child("imageURL").getValue(String.class);
                                                            String displayPostTitle = snapshot_filterJobTitles.child("postTitle").getValue(String.class);
                                                            String displayCompanyName = snapshot_filterJobTitles.child("companyName").getValue(String.class);
                                                            String displayPostDate = snapshot_filterJobTitles.child("postDate").getValue(String.class);

                                                            String postID = snapshot_filterJobTitles.getKey();

                                                            PWD_AvailableJobs_Model pwd_Model = new PWD_AvailableJobs_Model(imageURL, displayPostTitle, displayCompanyName, displayPostDate, postID);
                                                            list.add(pwd_Model);
                                                            if(!list.isEmpty()){
                                                                recyclerView.setVisibility(View.VISIBLE);
                                                                mascot.setVisibility(View.GONE);
                                                                tv_noJobsAvailable.setVisibility(View.GONE);
                                                                textInputLayout_filterJobTitle.setVisibility(View.VISIBLE);
                                                                textInputLayout_filterSkillOrDisability.setVisibility(View.VISIBLE);
                                                            }else if(list.isEmpty()){
                                                                recyclerView.setVisibility(View.GONE);
                                                                mascot.setVisibility(View.VISIBLE);
                                                                tv_noJobsAvailable.setVisibility(View.VISIBLE);
                                                                textInputLayout_filterJobTitle.setVisibility(View.GONE);
                                                                textInputLayout_filterSkillOrDisability.setVisibility(View.GONE);
                                                            }
                                                            Collections.reverse(list);
                                                            myAdapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_3_VisualDisability.this, list);
                                                            recyclerView.setAdapter(myAdapter);
                                                            myAdapter.notifyDataSetChanged();

                                                        }
                                                    }
                                                }//checks pwd if workExperience = With Experience
                                            }else{ // Without work experience required the system will not check if pwd does not have work experience
                                                if(job_educationalAttainmentRequirement.equals("true")){
                                                    //if educRequirement for a job post is required, the system will check pwd's educAttainment level.
                                                    if(job_educationalAttainment.equals("Elementary Level")){// checks if job post educAttainment matches pwd's educAttainment
                                                        //no need to check pwd_educAttainment
                                                        if(job_location.equals(pwd_location)){
                                                            String imageURL = snapshot_filterJobTitles.child("imageURL").getValue(String.class);
                                                            String displayPostTitle = snapshot_filterJobTitles.child("postTitle").getValue(String.class);
                                                            String displayCompanyName = snapshot_filterJobTitles.child("companyName").getValue(String.class);
                                                            String displayPostDate = snapshot_filterJobTitles.child("postDate").getValue(String.class);

                                                            String postID = snapshot_filterJobTitles.getKey();

                                                            PWD_AvailableJobs_Model pwd_Model = new PWD_AvailableJobs_Model(imageURL, displayPostTitle, displayCompanyName, displayPostDate, postID);
                                                            list.add(pwd_Model);
                                                            if(!list.isEmpty()){
                                                                recyclerView.setVisibility(View.VISIBLE);
                                                                mascot.setVisibility(View.GONE);
                                                                tv_noJobsAvailable.setVisibility(View.GONE);
                                                                textInputLayout_filterJobTitle.setVisibility(View.VISIBLE);
                                                                textInputLayout_filterSkillOrDisability.setVisibility(View.VISIBLE);
                                                            }else if(list.isEmpty()){
                                                                recyclerView.setVisibility(View.GONE);
                                                                mascot.setVisibility(View.VISIBLE);
                                                                tv_noJobsAvailable.setVisibility(View.VISIBLE);
                                                                textInputLayout_filterJobTitle.setVisibility(View.GONE);
                                                                textInputLayout_filterSkillOrDisability.setVisibility(View.GONE);
                                                            }
                                                            Collections.reverse(list);
                                                            myAdapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_3_VisualDisability.this, list);
                                                            recyclerView.setAdapter(myAdapter);
                                                            myAdapter.notifyDataSetChanged();

                                                        }

                                                    }else if(job_educationalAttainment.equals("High School Level") && (job_educationalAttainment.equals(pwd_educationalAttainment)
                                                            || pwd_educationalAttainment.equals("Associate Level")
                                                            || pwd_educationalAttainment.equals("Bachelor Level")
                                                            || pwd_educationalAttainment.equals("Master's Level")
                                                            || pwd_educationalAttainment.equals("Doctorate Level"))){

                                                        if(job_location.equals(pwd_location)){
                                                            String imageURL = snapshot_filterJobTitles.child("imageURL").getValue(String.class);
                                                            String displayPostTitle = snapshot_filterJobTitles.child("postTitle").getValue(String.class);
                                                            String displayCompanyName = snapshot_filterJobTitles.child("companyName").getValue(String.class);
                                                            String displayPostDate = snapshot_filterJobTitles.child("postDate").getValue(String.class);

                                                            String postID = snapshot_filterJobTitles.getKey();

                                                            PWD_AvailableJobs_Model pwd_Model = new PWD_AvailableJobs_Model(imageURL, displayPostTitle, displayCompanyName, displayPostDate, postID);
                                                            list.add(pwd_Model);
                                                            if(!list.isEmpty()){
                                                                recyclerView.setVisibility(View.VISIBLE);
                                                                mascot.setVisibility(View.GONE);
                                                                tv_noJobsAvailable.setVisibility(View.GONE);
                                                                textInputLayout_filterJobTitle.setVisibility(View.VISIBLE);
                                                                textInputLayout_filterSkillOrDisability.setVisibility(View.VISIBLE);
                                                            }else if(list.isEmpty()){
                                                                recyclerView.setVisibility(View.GONE);
                                                                mascot.setVisibility(View.VISIBLE);
                                                                tv_noJobsAvailable.setVisibility(View.VISIBLE);
                                                                textInputLayout_filterJobTitle.setVisibility(View.GONE);
                                                                textInputLayout_filterSkillOrDisability.setVisibility(View.GONE);
                                                            }
                                                            Collections.reverse(list);
                                                            myAdapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_3_VisualDisability.this, list);
                                                            recyclerView.setAdapter(myAdapter);
                                                            myAdapter.notifyDataSetChanged();

                                                        }

                                                    }else if(job_educationalAttainment.equals("Associate Level") && (job_educationalAttainment.equals(pwd_educationalAttainment)
                                                            || pwd_educationalAttainment.equals("Bachelor Level")
                                                            || pwd_educationalAttainment.equals("Master's Level")
                                                            || pwd_educationalAttainment.equals("Doctorate Level"))){

                                                        if(job_location.equals(pwd_location)){
                                                            String imageURL = snapshot_filterJobTitles.child("imageURL").getValue(String.class);
                                                            String displayPostTitle = snapshot_filterJobTitles.child("postTitle").getValue(String.class);
                                                            String displayCompanyName = snapshot_filterJobTitles.child("companyName").getValue(String.class);
                                                            String displayPostDate = snapshot_filterJobTitles.child("postDate").getValue(String.class);

                                                            String postID = snapshot_filterJobTitles.getKey();

                                                            PWD_AvailableJobs_Model pwd_Model = new PWD_AvailableJobs_Model(imageURL, displayPostTitle, displayCompanyName, displayPostDate, postID);
                                                            list.add(pwd_Model);
                                                            if(!list.isEmpty()){
                                                                recyclerView.setVisibility(View.VISIBLE);
                                                                mascot.setVisibility(View.GONE);
                                                                tv_noJobsAvailable.setVisibility(View.GONE);
                                                                textInputLayout_filterJobTitle.setVisibility(View.VISIBLE);
                                                                textInputLayout_filterSkillOrDisability.setVisibility(View.VISIBLE);
                                                            }else if(list.isEmpty()){
                                                                recyclerView.setVisibility(View.GONE);
                                                                mascot.setVisibility(View.VISIBLE);
                                                                tv_noJobsAvailable.setVisibility(View.VISIBLE);
                                                                textInputLayout_filterJobTitle.setVisibility(View.GONE);
                                                                textInputLayout_filterSkillOrDisability.setVisibility(View.GONE);
                                                            }
                                                            Collections.reverse(list);
                                                            myAdapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_3_VisualDisability.this, list);
                                                            recyclerView.setAdapter(myAdapter);
                                                            myAdapter.notifyDataSetChanged();

                                                        }

                                                    }else if(job_educationalAttainment.equals("Bachelor Level") && (job_educationalAttainment.equals(pwd_educationalAttainment)
                                                            || pwd_educationalAttainment.equals("Master's Level")
                                                            || pwd_educationalAttainment.equals("Doctorate Level"))){

                                                        if(job_location.equals(pwd_location)){
                                                            String imageURL = snapshot_filterJobTitles.child("imageURL").getValue(String.class);
                                                            String displayPostTitle = snapshot_filterJobTitles.child("postTitle").getValue(String.class);
                                                            String displayCompanyName = snapshot_filterJobTitles.child("companyName").getValue(String.class);
                                                            String displayPostDate = snapshot_filterJobTitles.child("postDate").getValue(String.class);

                                                            String postID = snapshot_filterJobTitles.getKey();

                                                            PWD_AvailableJobs_Model pwd_Model = new PWD_AvailableJobs_Model(imageURL, displayPostTitle, displayCompanyName, displayPostDate, postID);
                                                            list.add(pwd_Model);
                                                            if(!list.isEmpty()){
                                                                recyclerView.setVisibility(View.VISIBLE);
                                                                mascot.setVisibility(View.GONE);
                                                                tv_noJobsAvailable.setVisibility(View.GONE);
                                                                textInputLayout_filterJobTitle.setVisibility(View.VISIBLE);
                                                                textInputLayout_filterSkillOrDisability.setVisibility(View.VISIBLE);
                                                            }else if(list.isEmpty()){
                                                                recyclerView.setVisibility(View.GONE);
                                                                mascot.setVisibility(View.VISIBLE);
                                                                tv_noJobsAvailable.setVisibility(View.VISIBLE);
                                                                textInputLayout_filterJobTitle.setVisibility(View.GONE);
                                                                textInputLayout_filterSkillOrDisability.setVisibility(View.GONE);
                                                            }
                                                            Collections.reverse(list);
                                                            myAdapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_3_VisualDisability.this, list);
                                                            recyclerView.setAdapter(myAdapter);
                                                            myAdapter.notifyDataSetChanged();

                                                        }

                                                    }else if(job_educationalAttainment.equals("Master's Level")&& (job_educationalAttainment.equals(pwd_educationalAttainment)
                                                            || pwd_educationalAttainment.equals("Doctorate Level"))){

                                                        if(job_location.equals(pwd_location)){
                                                            String imageURL = snapshot_filterJobTitles.child("imageURL").getValue(String.class);
                                                            String displayPostTitle = snapshot_filterJobTitles.child("postTitle").getValue(String.class);
                                                            String displayCompanyName = snapshot_filterJobTitles.child("companyName").getValue(String.class);
                                                            String displayPostDate = snapshot_filterJobTitles.child("postDate").getValue(String.class);

                                                            String postID = snapshot_filterJobTitles.getKey();

                                                            PWD_AvailableJobs_Model pwd_Model = new PWD_AvailableJobs_Model(imageURL, displayPostTitle, displayCompanyName, displayPostDate, postID);
                                                            list.add(pwd_Model);
                                                            if(!list.isEmpty()){
                                                                recyclerView.setVisibility(View.VISIBLE);
                                                                mascot.setVisibility(View.GONE);
                                                                tv_noJobsAvailable.setVisibility(View.GONE);
                                                                textInputLayout_filterJobTitle.setVisibility(View.VISIBLE);
                                                                textInputLayout_filterSkillOrDisability.setVisibility(View.VISIBLE);
                                                            }else if(list.isEmpty()){
                                                                recyclerView.setVisibility(View.GONE);
                                                                mascot.setVisibility(View.VISIBLE);
                                                                tv_noJobsAvailable.setVisibility(View.VISIBLE);
                                                                textInputLayout_filterJobTitle.setVisibility(View.GONE);
                                                                textInputLayout_filterSkillOrDisability.setVisibility(View.GONE);
                                                            }
                                                            Collections.reverse(list);
                                                            myAdapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_3_VisualDisability.this, list);
                                                            recyclerView.setAdapter(myAdapter);
                                                            myAdapter.notifyDataSetChanged();

                                                        }

                                                    }else if(job_educationalAttainment.equals("Doctorate Level") && job_educationalAttainment.equals(pwd_educationalAttainment)){

                                                        if(job_location.equals(pwd_location)){
                                                            String imageURL = snapshot_filterJobTitles.child("imageURL").getValue(String.class);
                                                            String displayPostTitle = snapshot_filterJobTitles.child("postTitle").getValue(String.class);
                                                            String displayCompanyName = snapshot_filterJobTitles.child("companyName").getValue(String.class);
                                                            String displayPostDate = snapshot_filterJobTitles.child("postDate").getValue(String.class);

                                                            String postID = snapshot_filterJobTitles.getKey();

                                                            PWD_AvailableJobs_Model pwd_Model = new PWD_AvailableJobs_Model(imageURL, displayPostTitle, displayCompanyName, displayPostDate, postID);
                                                            list.add(pwd_Model);
                                                            if(!list.isEmpty()){
                                                                recyclerView.setVisibility(View.VISIBLE);
                                                                mascot.setVisibility(View.GONE);
                                                                tv_noJobsAvailable.setVisibility(View.GONE);
                                                                textInputLayout_filterJobTitle.setVisibility(View.VISIBLE);
                                                                textInputLayout_filterSkillOrDisability.setVisibility(View.VISIBLE);
                                                            }else if(list.isEmpty()){
                                                                recyclerView.setVisibility(View.GONE);
                                                                mascot.setVisibility(View.VISIBLE);
                                                                tv_noJobsAvailable.setVisibility(View.VISIBLE);
                                                                textInputLayout_filterJobTitle.setVisibility(View.GONE);
                                                                textInputLayout_filterSkillOrDisability.setVisibility(View.GONE);
                                                            }
                                                            Collections.reverse(list);myAdapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_3_VisualDisability.this, list);
                                                            recyclerView.setAdapter(myAdapter);
                                                            myAdapter.notifyDataSetChanged();

                                                        }

                                                    }
                                                }//checks if job educRequirement is set to true
                                                else{ // if educRequirement for a job post is not required, the system will not check pwd's educAttainment level.
                                                    if(job_location.equals(pwd_location)){
                                                        String imageURL = snapshot_filterJobTitles.child("imageURL").getValue(String.class);
                                                        String displayPostTitle = snapshot_filterJobTitles.child("postTitle").getValue(String.class);
                                                        String displayCompanyName = snapshot_filterJobTitles.child("companyName").getValue(String.class);
                                                        String displayPostDate = snapshot_filterJobTitles.child("postDate").getValue(String.class);

                                                        String postID = snapshot_filterJobTitles.getKey();

                                                        PWD_AvailableJobs_Model pwd_Model = new PWD_AvailableJobs_Model(imageURL, displayPostTitle, displayCompanyName, displayPostDate, postID);
                                                        list.add(pwd_Model);
                                                        if(!list.isEmpty()){
                                                            recyclerView.setVisibility(View.VISIBLE);
                                                            mascot.setVisibility(View.GONE);
                                                            tv_noJobsAvailable.setVisibility(View.GONE);
                                                            textInputLayout_filterJobTitle.setVisibility(View.VISIBLE);
                                                            textInputLayout_filterSkillOrDisability.setVisibility(View.VISIBLE);
                                                        }else if(list.isEmpty()){
                                                            recyclerView.setVisibility(View.GONE);
                                                            mascot.setVisibility(View.VISIBLE);
                                                            tv_noJobsAvailable.setVisibility(View.VISIBLE);
                                                            textInputLayout_filterJobTitle.setVisibility(View.GONE);
                                                            textInputLayout_filterSkillOrDisability.setVisibility(View.GONE);
                                                        }
                                                        Collections.reverse(list);
                                                        myAdapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_3_VisualDisability.this, list);
                                                        recyclerView.setAdapter(myAdapter);
                                                        myAdapter.notifyDataSetChanged();

                                                    }
                                                }
                                            }

                                        }
                                    }
                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                refForJobs.orderByChild("typeOfDisability2").equalTo("Partial Vision Disability").addValueEventListener(new ValueEventListener() { //checking Job_Offers
                    @Override
                    public void onDataChange(@NonNull DataSnapshot jobFetch_dataSnapshot1) {
                        if(jobFetch_dataSnapshot1.hasChild("Job_Offers")){
                            recyclerView.setVisibility(View.VISIBLE);
                            mascot.setVisibility(View.GONE);
                            tv_noJobsAvailable.setVisibility(View.GONE);
                            textInputLayout_filterSkillOrDisability.setVisibility(View.VISIBLE);
                            textInputLayout_filterJobTitle.setVisibility(View.VISIBLE);
                        }else{
                            recyclerView.setVisibility(View.GONE);
                            mascot.setVisibility(View.VISIBLE);
                            tv_noJobsAvailable.setVisibility(View.VISIBLE);
                            textInputLayout_filterSkillOrDisability.setVisibility(View.GONE);
                            textInputLayout_filterJobTitle.setVisibility(View.GONE);
                        }
                        list.clear();
                        for (DataSnapshot job_snapshot1 : jobFetch_dataSnapshot1.getChildren()) {
                            final String permission = job_snapshot1.child("permission").getValue(String.class);
                            final String job_skillCategory = job_snapshot1.child("skill").getValue().toString();
                            final String job_educationalAttainmentRequirement = job_snapshot1.child("educationalAttainmentRequirement").getValue().toString();
                            final String job_educationalAttainment = job_snapshot1.child("educationalAttainment").getValue().toString();
                            final String job_workExp = job_snapshot1.child("workExperience").getValue().toString();
                            final String job_location = job_snapshot1.child("city").getValue().toString();
                            // looks for approved job_offers hiring people with VisualDisability
                            if (permission.equals("Approved") && job_skillCategory.equals(pwd_SkillCategory)){
                                //logic is even if the company posts a job that does not require experience
                                //people with experience should still be able to see the job post.
                                if(job_workExp.equals("With Experience")){
                                    if(pwd_workExp.equals("With Experience")){ // strictly checking if pwd has work experience otherwise the data for the job post will not show.
                                        if(job_educationalAttainmentRequirement.equals("true")){
                                            //if educRequirement for a job post is required, the system will check pwd's educAttainment level.
                                            if(job_educationalAttainment.equals("Elementary Level")){// checks if job post educAttainment matches pwd's educAttainment
                                                //no need to check pwd_educAttainment
                                                if(job_location.equals(pwd_location)){
                                                    String imageURL = job_snapshot1.child("imageURL").getValue(String.class);
                                                    String displayPostTitle = job_snapshot1.child("postTitle").getValue(String.class);
                                                    String displayCompanyName = job_snapshot1.child("companyName").getValue(String.class);
                                                    String displayPostDate = job_snapshot1.child("postDate").getValue(String.class);

                                                    String postID = job_snapshot1.getKey();

                                                    PWD_AvailableJobs_Model pwd_Model = new PWD_AvailableJobs_Model(imageURL, displayPostTitle, displayCompanyName, displayPostDate, postID);
                                                    list.add(pwd_Model);
                                                    if(!list.isEmpty()){
                                                        recyclerView.setVisibility(View.VISIBLE);
                                                        mascot.setVisibility(View.GONE);
                                                        tv_noJobsAvailable.setVisibility(View.GONE);
                                                        textInputLayout_filterSkillOrDisability.setVisibility(View.VISIBLE);
                                                        textInputLayout_filterJobTitle.setVisibility(View.VISIBLE);
                                                    }else{
                                                        recyclerView.setVisibility(View.GONE);
                                                        mascot.setVisibility(View.VISIBLE);
                                                        tv_noJobsAvailable.setVisibility(View.VISIBLE);textInputLayout_filterSkillOrDisability.setVisibility(View.GONE);
                                                        textInputLayout_filterJobTitle.setVisibility(View.GONE);
                                                    }
                                                    Collections.reverse(list);
                                                    myAdapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_3_VisualDisability.this, list);
                                                    recyclerView.setAdapter(myAdapter);
                                                    myAdapter.notifyDataSetChanged();

                                                }

                                            }else if(job_educationalAttainment.equals("High School Level") && (job_educationalAttainment.equals(pwd_educationalAttainment)
                                                    || pwd_educationalAttainment.equals("Associate Level")
                                                    || pwd_educationalAttainment.equals("Bachelor Level")
                                                    || pwd_educationalAttainment.equals("Master's Level")
                                                    || pwd_educationalAttainment.equals("Doctorate Level"))){

                                                if(job_location.equals(pwd_location)){
                                                    String imageURL = job_snapshot1.child("imageURL").getValue(String.class);
                                                    String displayPostTitle = job_snapshot1.child("postTitle").getValue(String.class);
                                                    String displayCompanyName = job_snapshot1.child("companyName").getValue(String.class);
                                                    String displayPostDate = job_snapshot1.child("postDate").getValue(String.class);

                                                    String postID = job_snapshot1.getKey();

                                                    PWD_AvailableJobs_Model pwd_Model = new PWD_AvailableJobs_Model(imageURL, displayPostTitle, displayCompanyName, displayPostDate, postID);
                                                    list.add(pwd_Model);
                                                    if(!list.isEmpty()){
                                                        recyclerView.setVisibility(View.VISIBLE);
                                                        mascot.setVisibility(View.GONE);
                                                        tv_noJobsAvailable.setVisibility(View.GONE);
                                                        textInputLayout_filterSkillOrDisability.setVisibility(View.VISIBLE);
                                                        textInputLayout_filterJobTitle.setVisibility(View.VISIBLE);
                                                    }else{
                                                        recyclerView.setVisibility(View.GONE);
                                                        mascot.setVisibility(View.VISIBLE);
                                                        tv_noJobsAvailable.setVisibility(View.VISIBLE);textInputLayout_filterSkillOrDisability.setVisibility(View.GONE);
                                                        textInputLayout_filterJobTitle.setVisibility(View.GONE);
                                                    }
                                                    Collections.reverse(list);
                                                    myAdapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_3_VisualDisability.this, list);
                                                    recyclerView.setAdapter(myAdapter);
                                                    myAdapter.notifyDataSetChanged();

                                                }

                                            }else if(job_educationalAttainment.equals("Associate Level") && (job_educationalAttainment.equals(pwd_educationalAttainment)
                                                    || pwd_educationalAttainment.equals("Bachelor Level")
                                                    || pwd_educationalAttainment.equals("Master's Level")
                                                    || pwd_educationalAttainment.equals("Doctorate Level"))){

                                                if(job_location.equals(pwd_location)){
                                                    String imageURL = job_snapshot1.child("imageURL").getValue(String.class);
                                                    String displayPostTitle = job_snapshot1.child("postTitle").getValue(String.class);
                                                    String displayCompanyName = job_snapshot1.child("companyName").getValue(String.class);
                                                    String displayPostDate = job_snapshot1.child("postDate").getValue(String.class);

                                                    String postID = job_snapshot1.getKey();

                                                    PWD_AvailableJobs_Model pwd_Model = new PWD_AvailableJobs_Model(imageURL, displayPostTitle, displayCompanyName, displayPostDate, postID);
                                                    list.add(pwd_Model);
                                                    if(!list.isEmpty()){
                                                        recyclerView.setVisibility(View.VISIBLE);
                                                        mascot.setVisibility(View.GONE);
                                                        tv_noJobsAvailable.setVisibility(View.GONE);
                                                        textInputLayout_filterSkillOrDisability.setVisibility(View.VISIBLE);
                                                        textInputLayout_filterJobTitle.setVisibility(View.VISIBLE);
                                                    }else{
                                                        recyclerView.setVisibility(View.GONE);
                                                        mascot.setVisibility(View.VISIBLE);
                                                        tv_noJobsAvailable.setVisibility(View.VISIBLE);textInputLayout_filterSkillOrDisability.setVisibility(View.GONE);
                                                        textInputLayout_filterJobTitle.setVisibility(View.GONE);
                                                    }
                                                    Collections.reverse(list);
                                                    myAdapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_3_VisualDisability.this, list);
                                                    recyclerView.setAdapter(myAdapter);
                                                    myAdapter.notifyDataSetChanged();

                                                }

                                            }else if(job_educationalAttainment.equals("Bachelor Level") && (job_educationalAttainment.equals(pwd_educationalAttainment)
                                                    || pwd_educationalAttainment.equals("Master's Level")
                                                    || pwd_educationalAttainment.equals("Doctorate Level"))){

                                                if(job_location.equals(pwd_location)){
                                                    String imageURL = job_snapshot1.child("imageURL").getValue(String.class);
                                                    String displayPostTitle = job_snapshot1.child("postTitle").getValue(String.class);
                                                    String displayCompanyName = job_snapshot1.child("companyName").getValue(String.class);
                                                    String displayPostDate = job_snapshot1.child("postDate").getValue(String.class);

                                                    String postID = job_snapshot1.getKey();

                                                    PWD_AvailableJobs_Model pwd_Model = new PWD_AvailableJobs_Model(imageURL, displayPostTitle, displayCompanyName, displayPostDate, postID);
                                                    list.add(pwd_Model);
                                                    if(!list.isEmpty()){
                                                        recyclerView.setVisibility(View.VISIBLE);
                                                        mascot.setVisibility(View.GONE);
                                                        tv_noJobsAvailable.setVisibility(View.GONE);
                                                        textInputLayout_filterSkillOrDisability.setVisibility(View.VISIBLE);
                                                        textInputLayout_filterJobTitle.setVisibility(View.VISIBLE);
                                                    }else{
                                                        recyclerView.setVisibility(View.GONE);
                                                        mascot.setVisibility(View.VISIBLE);
                                                        tv_noJobsAvailable.setVisibility(View.VISIBLE);textInputLayout_filterSkillOrDisability.setVisibility(View.GONE);
                                                        textInputLayout_filterJobTitle.setVisibility(View.GONE);
                                                    }
                                                    Collections.reverse(list);
                                                    myAdapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_3_VisualDisability.this, list);
                                                    recyclerView.setAdapter(myAdapter);
                                                    myAdapter.notifyDataSetChanged();

                                                }

                                            }else if(job_educationalAttainment.equals("Master's Level")&& (job_educationalAttainment.equals(pwd_educationalAttainment)
                                                    || pwd_educationalAttainment.equals("Doctorate Level"))){

                                                if(job_location.equals(pwd_location)){
                                                    String imageURL = job_snapshot1.child("imageURL").getValue(String.class);
                                                    String displayPostTitle = job_snapshot1.child("postTitle").getValue(String.class);
                                                    String displayCompanyName = job_snapshot1.child("companyName").getValue(String.class);
                                                    String displayPostDate = job_snapshot1.child("postDate").getValue(String.class);

                                                    String postID = job_snapshot1.getKey();

                                                    PWD_AvailableJobs_Model pwd_Model = new PWD_AvailableJobs_Model(imageURL, displayPostTitle, displayCompanyName, displayPostDate, postID);
                                                    list.add(pwd_Model);
                                                    if(!list.isEmpty()){
                                                        recyclerView.setVisibility(View.VISIBLE);
                                                        mascot.setVisibility(View.GONE);
                                                        tv_noJobsAvailable.setVisibility(View.GONE);
                                                        textInputLayout_filterSkillOrDisability.setVisibility(View.VISIBLE);
                                                        textInputLayout_filterJobTitle.setVisibility(View.VISIBLE);
                                                    }else{
                                                        recyclerView.setVisibility(View.GONE);
                                                        mascot.setVisibility(View.VISIBLE);
                                                        tv_noJobsAvailable.setVisibility(View.VISIBLE);textInputLayout_filterSkillOrDisability.setVisibility(View.GONE);
                                                        textInputLayout_filterJobTitle.setVisibility(View.GONE);
                                                    }
                                                    Collections.reverse(list);
                                                    myAdapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_3_VisualDisability.this, list);
                                                    recyclerView.setAdapter(myAdapter);
                                                    myAdapter.notifyDataSetChanged();

                                                }

                                            }else if(job_educationalAttainment.equals("Doctorate Level") && job_educationalAttainment.equals(pwd_educationalAttainment)){

                                                if(job_location.equals(pwd_location)){
                                                    String imageURL = job_snapshot1.child("imageURL").getValue(String.class);
                                                    String displayPostTitle = job_snapshot1.child("postTitle").getValue(String.class);
                                                    String displayCompanyName = job_snapshot1.child("companyName").getValue(String.class);
                                                    String displayPostDate = job_snapshot1.child("postDate").getValue(String.class);

                                                    String postID = job_snapshot1.getKey();

                                                    PWD_AvailableJobs_Model pwd_Model = new PWD_AvailableJobs_Model(imageURL, displayPostTitle, displayCompanyName, displayPostDate, postID);
                                                    list.add(pwd_Model);
                                                    if(!list.isEmpty()){
                                                        recyclerView.setVisibility(View.VISIBLE);
                                                        mascot.setVisibility(View.GONE);
                                                        tv_noJobsAvailable.setVisibility(View.GONE);
                                                        textInputLayout_filterSkillOrDisability.setVisibility(View.VISIBLE);
                                                        textInputLayout_filterJobTitle.setVisibility(View.VISIBLE);
                                                    }else{
                                                        recyclerView.setVisibility(View.GONE);
                                                        mascot.setVisibility(View.VISIBLE);
                                                        tv_noJobsAvailable.setVisibility(View.VISIBLE);textInputLayout_filterSkillOrDisability.setVisibility(View.GONE);
                                                        textInputLayout_filterJobTitle.setVisibility(View.GONE);
                                                    }
                                                    Collections.reverse(list);
                                                    myAdapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_3_VisualDisability.this, list);
                                                    recyclerView.setAdapter(myAdapter);
                                                    myAdapter.notifyDataSetChanged();

                                                }
                                            }
                                        }//checks if job educRequirement is set to true
                                        else{ // if educRequirement for a job post is not required, the system will not check pwd's educAttainment level.
                                            if(job_location.equals(pwd_location)){
                                                String imageURL = job_snapshot1.child("imageURL").getValue(String.class);
                                                String displayPostTitle = job_snapshot1.child("postTitle").getValue(String.class);
                                                String displayCompanyName = job_snapshot1.child("companyName").getValue(String.class);
                                                String displayPostDate = job_snapshot1.child("postDate").getValue(String.class);

                                                String postID = job_snapshot1.getKey();

                                                PWD_AvailableJobs_Model pwd_Model = new PWD_AvailableJobs_Model(imageURL, displayPostTitle, displayCompanyName, displayPostDate, postID);
                                                list.add(pwd_Model);
                                                if(!list.isEmpty()){
                                                    recyclerView.setVisibility(View.VISIBLE);
                                                    mascot.setVisibility(View.GONE);
                                                    tv_noJobsAvailable.setVisibility(View.GONE);
                                                    textInputLayout_filterSkillOrDisability.setVisibility(View.VISIBLE);
                                                    textInputLayout_filterJobTitle.setVisibility(View.VISIBLE);
                                                }else{
                                                    recyclerView.setVisibility(View.GONE);
                                                    mascot.setVisibility(View.VISIBLE);
                                                    tv_noJobsAvailable.setVisibility(View.VISIBLE);textInputLayout_filterSkillOrDisability.setVisibility(View.GONE);
                                                    textInputLayout_filterJobTitle.setVisibility(View.GONE);
                                                }
                                                Collections.reverse(list);
                                                myAdapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_3_VisualDisability.this, list);
                                                recyclerView.setAdapter(myAdapter);
                                                myAdapter.notifyDataSetChanged();

                                            }
                                        }
                                    }//checks pwd if workExperience = With Experience
                                }else{ // Without work experience required the system will not check if pwd does not have work experience
                                    if(job_educationalAttainmentRequirement.equals("true")){
                                        //if educRequirement for a job post is required, the system will check pwd's educAttainment level.
                                        if(job_educationalAttainment.equals("Elementary Level")){// checks if job post educAttainment matches pwd's educAttainment
                                            //no need to check pwd_educAttainment
                                            if(job_location.equals(pwd_location)){
                                                String imageURL = job_snapshot1.child("imageURL").getValue(String.class);
                                                String displayPostTitle = job_snapshot1.child("postTitle").getValue(String.class);
                                                String displayCompanyName = job_snapshot1.child("companyName").getValue(String.class);
                                                String displayPostDate = job_snapshot1.child("postDate").getValue(String.class);

                                                String postID = job_snapshot1.getKey();

                                                PWD_AvailableJobs_Model pwd_Model = new PWD_AvailableJobs_Model(imageURL, displayPostTitle, displayCompanyName, displayPostDate, postID);
                                                list.add(pwd_Model);
                                                if(!list.isEmpty()){
                                                    recyclerView.setVisibility(View.VISIBLE);
                                                    mascot.setVisibility(View.GONE);
                                                    tv_noJobsAvailable.setVisibility(View.GONE);
                                                    textInputLayout_filterSkillOrDisability.setVisibility(View.VISIBLE);
                                                    textInputLayout_filterJobTitle.setVisibility(View.VISIBLE);
                                                }else{
                                                    recyclerView.setVisibility(View.GONE);
                                                    mascot.setVisibility(View.VISIBLE);
                                                    tv_noJobsAvailable.setVisibility(View.VISIBLE);textInputLayout_filterSkillOrDisability.setVisibility(View.GONE);
                                                    textInputLayout_filterJobTitle.setVisibility(View.GONE);
                                                }
                                                Collections.reverse(list);
                                                myAdapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_3_VisualDisability.this, list);
                                                recyclerView.setAdapter(myAdapter);
                                                myAdapter.notifyDataSetChanged();

                                            }

                                        }else if(job_educationalAttainment.equals("High School Level") && (job_educationalAttainment.equals(pwd_educationalAttainment)
                                                || pwd_educationalAttainment.equals("Associate Level")
                                                || pwd_educationalAttainment.equals("Bachelor Level")
                                                || pwd_educationalAttainment.equals("Master's Level")
                                                || pwd_educationalAttainment.equals("Doctorate Level"))){

                                            if(job_location.equals(pwd_location)){
                                                String imageURL = job_snapshot1.child("imageURL").getValue(String.class);
                                                String displayPostTitle = job_snapshot1.child("postTitle").getValue(String.class);
                                                String displayCompanyName = job_snapshot1.child("companyName").getValue(String.class);
                                                String displayPostDate = job_snapshot1.child("postDate").getValue(String.class);

                                                String postID = job_snapshot1.getKey();

                                                PWD_AvailableJobs_Model pwd_Model = new PWD_AvailableJobs_Model(imageURL, displayPostTitle, displayCompanyName, displayPostDate, postID);
                                                list.add(pwd_Model);
                                                if(!list.isEmpty()){
                                                    recyclerView.setVisibility(View.VISIBLE);
                                                    mascot.setVisibility(View.GONE);
                                                    tv_noJobsAvailable.setVisibility(View.GONE);
                                                    textInputLayout_filterSkillOrDisability.setVisibility(View.VISIBLE);
                                                    textInputLayout_filterJobTitle.setVisibility(View.VISIBLE);
                                                }else{
                                                    recyclerView.setVisibility(View.GONE);
                                                    mascot.setVisibility(View.VISIBLE);
                                                    tv_noJobsAvailable.setVisibility(View.VISIBLE);textInputLayout_filterSkillOrDisability.setVisibility(View.GONE);
                                                    textInputLayout_filterJobTitle.setVisibility(View.GONE);
                                                }
                                                Collections.reverse(list);
                                                myAdapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_3_VisualDisability.this, list);
                                                recyclerView.setAdapter(myAdapter);
                                                myAdapter.notifyDataSetChanged();

                                            }

                                        }else if(job_educationalAttainment.equals("Associate Level") && (job_educationalAttainment.equals(pwd_educationalAttainment)
                                                || pwd_educationalAttainment.equals("Bachelor Level")
                                                || pwd_educationalAttainment.equals("Master's Level")
                                                || pwd_educationalAttainment.equals("Doctorate Level"))){

                                            if(job_location.equals(pwd_location)){
                                                String imageURL = job_snapshot1.child("imageURL").getValue(String.class);
                                                String displayPostTitle = job_snapshot1.child("postTitle").getValue(String.class);
                                                String displayCompanyName = job_snapshot1.child("companyName").getValue(String.class);
                                                String displayPostDate = job_snapshot1.child("postDate").getValue(String.class);

                                                String postID = job_snapshot1.getKey();

                                                PWD_AvailableJobs_Model pwd_Model = new PWD_AvailableJobs_Model(imageURL, displayPostTitle, displayCompanyName, displayPostDate, postID);
                                                list.add(pwd_Model);
                                                if(!list.isEmpty()){
                                                    recyclerView.setVisibility(View.VISIBLE);
                                                    mascot.setVisibility(View.GONE);
                                                    tv_noJobsAvailable.setVisibility(View.GONE);
                                                    textInputLayout_filterSkillOrDisability.setVisibility(View.VISIBLE);
                                                    textInputLayout_filterJobTitle.setVisibility(View.VISIBLE);
                                                }else{
                                                    recyclerView.setVisibility(View.GONE);
                                                    mascot.setVisibility(View.VISIBLE);
                                                    tv_noJobsAvailable.setVisibility(View.VISIBLE);textInputLayout_filterSkillOrDisability.setVisibility(View.GONE);
                                                    textInputLayout_filterJobTitle.setVisibility(View.GONE);
                                                }
                                                Collections.reverse(list);
                                                myAdapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_3_VisualDisability.this, list);
                                                recyclerView.setAdapter(myAdapter);
                                                myAdapter.notifyDataSetChanged();

                                            }

                                        }else if(job_educationalAttainment.equals("Bachelor Level") && (job_educationalAttainment.equals(pwd_educationalAttainment)
                                                || pwd_educationalAttainment.equals("Master's Level")
                                                || pwd_educationalAttainment.equals("Doctorate Level"))){

                                            if(job_location.equals(pwd_location)){
                                                String imageURL = job_snapshot1.child("imageURL").getValue(String.class);
                                                String displayPostTitle = job_snapshot1.child("postTitle").getValue(String.class);
                                                String displayCompanyName = job_snapshot1.child("companyName").getValue(String.class);
                                                String displayPostDate = job_snapshot1.child("postDate").getValue(String.class);

                                                String postID = job_snapshot1.getKey();

                                                PWD_AvailableJobs_Model pwd_Model = new PWD_AvailableJobs_Model(imageURL, displayPostTitle, displayCompanyName, displayPostDate, postID);
                                                list.add(pwd_Model);
                                                if(!list.isEmpty()){
                                                    recyclerView.setVisibility(View.VISIBLE);
                                                    mascot.setVisibility(View.GONE);
                                                    tv_noJobsAvailable.setVisibility(View.GONE);
                                                    textInputLayout_filterSkillOrDisability.setVisibility(View.VISIBLE);
                                                    textInputLayout_filterJobTitle.setVisibility(View.VISIBLE);
                                                }else{
                                                    recyclerView.setVisibility(View.GONE);
                                                    mascot.setVisibility(View.VISIBLE);
                                                    tv_noJobsAvailable.setVisibility(View.VISIBLE);textInputLayout_filterSkillOrDisability.setVisibility(View.GONE);
                                                    textInputLayout_filterJobTitle.setVisibility(View.GONE);
                                                }
                                                Collections.reverse(list);
                                                myAdapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_3_VisualDisability.this, list);
                                                recyclerView.setAdapter(myAdapter);
                                                myAdapter.notifyDataSetChanged();

                                            }

                                        }else if(job_educationalAttainment.equals("Master's Level")&& (job_educationalAttainment.equals(pwd_educationalAttainment)
                                                || pwd_educationalAttainment.equals("Doctorate Level"))){

                                            if(job_location.equals(pwd_location)){
                                                String imageURL = job_snapshot1.child("imageURL").getValue(String.class);
                                                String displayPostTitle = job_snapshot1.child("postTitle").getValue(String.class);
                                                String displayCompanyName = job_snapshot1.child("companyName").getValue(String.class);
                                                String displayPostDate = job_snapshot1.child("postDate").getValue(String.class);

                                                String postID = job_snapshot1.getKey();

                                                PWD_AvailableJobs_Model pwd_Model = new PWD_AvailableJobs_Model(imageURL, displayPostTitle, displayCompanyName, displayPostDate, postID);
                                                list.add(pwd_Model);
                                                if(!list.isEmpty()){
                                                    recyclerView.setVisibility(View.VISIBLE);
                                                    mascot.setVisibility(View.GONE);
                                                    tv_noJobsAvailable.setVisibility(View.GONE);
                                                    textInputLayout_filterSkillOrDisability.setVisibility(View.VISIBLE);
                                                    textInputLayout_filterJobTitle.setVisibility(View.VISIBLE);
                                                }else{
                                                    recyclerView.setVisibility(View.GONE);
                                                    mascot.setVisibility(View.VISIBLE);
                                                    tv_noJobsAvailable.setVisibility(View.VISIBLE);textInputLayout_filterSkillOrDisability.setVisibility(View.GONE);
                                                    textInputLayout_filterJobTitle.setVisibility(View.GONE);
                                                }
                                                Collections.reverse(list);
                                                myAdapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_3_VisualDisability.this, list);
                                                recyclerView.setAdapter(myAdapter);
                                                myAdapter.notifyDataSetChanged();

                                            }

                                        }else if(job_educationalAttainment.equals("Doctorate Level") && job_educationalAttainment.equals(pwd_educationalAttainment)){

                                            if(job_location.equals(pwd_location)){
                                                String imageURL = job_snapshot1.child("imageURL").getValue(String.class);
                                                String displayPostTitle = job_snapshot1.child("postTitle").getValue(String.class);
                                                String displayCompanyName = job_snapshot1.child("companyName").getValue(String.class);
                                                String displayPostDate = job_snapshot1.child("postDate").getValue(String.class);

                                                String postID = job_snapshot1.getKey();

                                                PWD_AvailableJobs_Model pwd_Model = new PWD_AvailableJobs_Model(imageURL, displayPostTitle, displayCompanyName, displayPostDate, postID);
                                                list.add(pwd_Model);
                                                if(!list.isEmpty()){
                                                    recyclerView.setVisibility(View.VISIBLE);
                                                    mascot.setVisibility(View.GONE);
                                                    tv_noJobsAvailable.setVisibility(View.GONE);
                                                    textInputLayout_filterSkillOrDisability.setVisibility(View.VISIBLE);
                                                    textInputLayout_filterJobTitle.setVisibility(View.VISIBLE);
                                                }else{
                                                    recyclerView.setVisibility(View.GONE);
                                                    mascot.setVisibility(View.VISIBLE);
                                                    tv_noJobsAvailable.setVisibility(View.VISIBLE);textInputLayout_filterSkillOrDisability.setVisibility(View.GONE);
                                                    textInputLayout_filterJobTitle.setVisibility(View.GONE);
                                                }
                                                Collections.reverse(list);
                                                myAdapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_3_VisualDisability.this, list);
                                                recyclerView.setAdapter(myAdapter);
                                                myAdapter.notifyDataSetChanged();

                                            }

                                        }
                                    }//checks if job educRequirement is set to true
                                    else{ // if educRequirement for a job post is not required, the system will not check pwd's educAttainment level.
                                        if(job_location.equals(pwd_location)){
                                            String imageURL = job_snapshot1.child("imageURL").getValue(String.class);
                                            String displayPostTitle = job_snapshot1.child("postTitle").getValue(String.class);
                                            String displayCompanyName = job_snapshot1.child("companyName").getValue(String.class);
                                            String displayPostDate = job_snapshot1.child("postDate").getValue(String.class);

                                            String postID = job_snapshot1.getKey();

                                            PWD_AvailableJobs_Model pwd_Model = new PWD_AvailableJobs_Model(imageURL, displayPostTitle, displayCompanyName, displayPostDate, postID);
                                            list.add(pwd_Model);
                                            if(!list.isEmpty()){
                                                recyclerView.setVisibility(View.VISIBLE);
                                                mascot.setVisibility(View.GONE);
                                                tv_noJobsAvailable.setVisibility(View.GONE);
                                                textInputLayout_filterSkillOrDisability.setVisibility(View.VISIBLE);
                                                textInputLayout_filterJobTitle.setVisibility(View.VISIBLE);
                                            }else{
                                                recyclerView.setVisibility(View.GONE);
                                                mascot.setVisibility(View.VISIBLE);
                                                tv_noJobsAvailable.setVisibility(View.VISIBLE);textInputLayout_filterSkillOrDisability.setVisibility(View.GONE);
                                                textInputLayout_filterJobTitle.setVisibility(View.GONE);

                                            }
                                            Collections.reverse(list);
                                            myAdapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_3_VisualDisability.this, list);
                                            recyclerView.setAdapter(myAdapter);
                                            myAdapter.notifyDataSetChanged();

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

    private void filterByJobTitle(String userId){

        refUser = FirebaseDatabase.getInstance().getReference().child("PWD/" + userId);
        refUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot pwd_dataSnapshot) {
                final String pwd_SkillCategory = pwd_dataSnapshot.child("skill").getValue().toString();
                final String pwd_educationalAttainment = pwd_dataSnapshot.child("educationalAttainment").getValue().toString();
                final String pwd_workExp = pwd_dataSnapshot.child("workExperience").getValue().toString();
                final String pwd_location = pwd_dataSnapshot.child("city").getValue().toString();
                refForJobs = FirebaseDatabase.getInstance().getReference().child("Job_Offers");
                //Check Job Offer Info
                // checking PWD for type of disability
                Query query_filterToJobTitle = refForJobs.orderByChild("jobTitle").equalTo(autoComplete_filterJobTitles.getText().toString());
                query_filterToJobTitle.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        refForJobs.orderByChild("typeOfDisability2").equalTo("Partial Visual Disability").addValueEventListener(new ValueEventListener() { //checking Job_Offers
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onDataChange(@NonNull DataSnapshot jobFetch_dataSnapshot1) {
                                if(jobFetch_dataSnapshot1.hasChild("Job_Offers")){
                                    recyclerView.setVisibility(View.VISIBLE);
                                    mascot.setVisibility(View.GONE);
                                    tv_noJobsAvailable.setVisibility(View.GONE);
                                    textInputLayout_filterJobTitle.setVisibility(View.VISIBLE);
                                    //textInputLayout_filterSkillOrDisability.setVisibility(View.VISIBLE);
                                }else{
                                    recyclerView.setVisibility(View.GONE);
                                    mascot.setVisibility(View.VISIBLE);
                                    tv_noJobsAvailable.setVisibility(View.VISIBLE);
                                    textInputLayout_filterJobTitle.setVisibility(View.GONE);
                                    ////textInputLayout_filterSkillOrDisability.setVisibility(View.GONE);
                                }
                                list.clear();
                                for (DataSnapshot job_snapshot1 : jobFetch_dataSnapshot1.getChildren()) {
                                    final String permission = job_snapshot1.child("permission").getValue(String.class);
                                    final String job_skillCategory = job_snapshot1.child("skill").getValue().toString();
                                    final String job_educationalAttainmentRequirement = job_snapshot1.child("educationalAttainmentRequirement").getValue().toString();
                                    final String job_educationalAttainment = job_snapshot1.child("educationalAttainment").getValue().toString();
                                    final String job_workExp = job_snapshot1.child("workExperience").getValue().toString();
                                    final String job_location = job_snapshot1.child("city").getValue().toString();
                                    final String job_jobTitle = job_snapshot1.child("jobTitle").getValue().toString();

                                    // looks for approved job_offers hiring people with VisualDisability
                                    if (permission.equals("Approved") && job_skillCategory.equals(pwd_SkillCategory)
                                            && job_jobTitle.equals(autoComplete_filterJobTitles.getText().toString())){
                                        //logic is even if the company posts a job that does not require experience
                                        //people with experience should still be able to see the job post.
                                        if(job_workExp.equals("With Experience")){
                                            if(pwd_workExp.equals("With Experience")){ // strictly checking if pwd has work experience otherwise the data for the job post will not show.
                                                if(job_educationalAttainmentRequirement.equals("true")){
                                                    //if educRequirement for a job post is required, the system will check pwd's educAttainment level.
                                                    if(job_educationalAttainment.equals("Elementary Level")){// checks if job post educAttainment matches pwd's educAttainment
                                                        //no need to check pwd_educAttainment
                                                        if(job_location.equals(pwd_location)){
                                                            String imageURL = job_snapshot1.child("imageURL").getValue(String.class);
                                                            String displayPostTitle = job_snapshot1.child("jobTitle").getValue(String.class);
                                                            String displayCompanyName = job_snapshot1.child("companyName").getValue(String.class);
                                                            String displayPostDate = job_snapshot1.child("postDate").getValue(String.class);

                                                            String postID = job_snapshot1.getKey();

                                                            PWD_AvailableJobs_Model pwd_Model = new PWD_AvailableJobs_Model(imageURL, displayPostTitle, displayCompanyName, displayPostDate, postID);
                                                            list.add(pwd_Model);
                                                            if(!list.isEmpty()){
                                                                recyclerView.setVisibility(View.VISIBLE);
                                                                mascot.setVisibility(View.GONE);
                                                                tv_noJobsAvailable.setVisibility(View.GONE);
                                                                textInputLayout_filterJobTitle.setVisibility(View.VISIBLE);
                                                                //textInputLayout_filterSkillOrDisability.setVisibility(View.VISIBLE);
                                                            }else if(list.isEmpty()){
                                                                recyclerView.setVisibility(View.GONE);
                                                                mascot.setVisibility(View.VISIBLE);
                                                                tv_noJobsAvailable.setVisibility(View.VISIBLE);
                                                                textInputLayout_filterJobTitle.setVisibility(View.GONE);
                                                                //textInputLayout_filterSkillOrDisability.setVisibility(View.GONE);
                                                            }
                                                            Collections.reverse(list);
                                                            myAdapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_3_VisualDisability.this, list);
                                                            recyclerView.setAdapter(myAdapter);
                                                            myAdapter.notifyDataSetChanged();

                                                        }
                                                    }else if(job_educationalAttainment.equals("High School Level") && (job_educationalAttainment.equals(pwd_educationalAttainment)
                                                            || pwd_educationalAttainment.equals("Associate Level")
                                                            || pwd_educationalAttainment.equals("Bachelor Level")
                                                            || pwd_educationalAttainment.equals("Master's Level")
                                                            || pwd_educationalAttainment.equals("Doctorate Level"))){

                                                        if(job_location.equals(pwd_location)){
                                                            String imageURL = job_snapshot1.child("imageURL").getValue(String.class);
                                                            String displayPostTitle = job_snapshot1.child("jobTitle" +
                                                                    "").getValue(String.class);
                                                            String displayCompanyName = job_snapshot1.child("companyName").getValue(String.class);
                                                            String displayPostDate = job_snapshot1.child("postDate").getValue(String.class);

                                                            String postID = job_snapshot1.getKey();

                                                            PWD_AvailableJobs_Model pwd_Model = new PWD_AvailableJobs_Model(imageURL, displayPostTitle, displayCompanyName, displayPostDate, postID);
                                                            list.add(pwd_Model);
                                                            if(!list.isEmpty()){
                                                                recyclerView.setVisibility(View.VISIBLE);
                                                                mascot.setVisibility(View.GONE);
                                                                tv_noJobsAvailable.setVisibility(View.GONE);
                                                                textInputLayout_filterJobTitle.setVisibility(View.VISIBLE);
                                                                //textInputLayout_filterSkillOrDisability.setVisibility(View.VISIBLE);
                                                            }else if(list.isEmpty()){
                                                                recyclerView.setVisibility(View.GONE);
                                                                mascot.setVisibility(View.VISIBLE);
                                                                tv_noJobsAvailable.setVisibility(View.VISIBLE);
                                                                textInputLayout_filterJobTitle.setVisibility(View.GONE);
                                                                //textInputLayout_filterSkillOrDisability.setVisibility(View.GONE);
                                                            }
                                                            Collections.reverse(list);
                                                            myAdapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_3_VisualDisability.this, list);
                                                            recyclerView.setAdapter(myAdapter);
                                                            myAdapter.notifyDataSetChanged();

                                                        }

                                                    }else if(job_educationalAttainment.equals("Associate Level") && (job_educationalAttainment.equals(pwd_educationalAttainment)
                                                            || pwd_educationalAttainment.equals("Bachelor Level")
                                                            || pwd_educationalAttainment.equals("Master's Level")
                                                            || pwd_educationalAttainment.equals("Doctorate Level"))){

                                                        if(job_location.equals(pwd_location)){
                                                            String imageURL = job_snapshot1.child("imageURL").getValue(String.class);
                                                            String displayPostTitle = job_snapshot1.child("jobTitle" +
                                                                    "").getValue(String.class);
                                                            String displayCompanyName = job_snapshot1.child("companyName").getValue(String.class);
                                                            String displayPostDate = job_snapshot1.child("postDate").getValue(String.class);

                                                            String postID = job_snapshot1.getKey();

                                                            PWD_AvailableJobs_Model pwd_Model = new PWD_AvailableJobs_Model(imageURL, displayPostTitle, displayCompanyName, displayPostDate, postID);
                                                            list.add(pwd_Model);
                                                            if(!list.isEmpty()){
                                                                recyclerView.setVisibility(View.VISIBLE);
                                                                mascot.setVisibility(View.GONE);
                                                                tv_noJobsAvailable.setVisibility(View.GONE);
                                                                textInputLayout_filterJobTitle.setVisibility(View.VISIBLE);
                                                                //textInputLayout_filterSkillOrDisability.setVisibility(View.VISIBLE);
                                                            }else if(list.isEmpty()){
                                                                recyclerView.setVisibility(View.GONE);
                                                                mascot.setVisibility(View.VISIBLE);
                                                                tv_noJobsAvailable.setVisibility(View.VISIBLE);
                                                                textInputLayout_filterJobTitle.setVisibility(View.GONE);
                                                                //textInputLayout_filterSkillOrDisability.setVisibility(View.GONE);
                                                            }
                                                            Collections.reverse(list);
                                                            myAdapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_3_VisualDisability.this, list);
                                                            recyclerView.setAdapter(myAdapter);
                                                            myAdapter.notifyDataSetChanged();

                                                        }

                                                    }else if(job_educationalAttainment.equals("Bachelor Level") && (job_educationalAttainment.equals(pwd_educationalAttainment)
                                                            || pwd_educationalAttainment.equals("Master's Level")
                                                            || pwd_educationalAttainment.equals("Doctorate Level"))){

                                                        if(job_location.equals(pwd_location)){
                                                            String imageURL = job_snapshot1.child("imageURL").getValue(String.class);
                                                            String displayPostTitle = job_snapshot1.child("jobTitle" +
                                                                    "").getValue(String.class);
                                                            String displayCompanyName = job_snapshot1.child("companyName").getValue(String.class);
                                                            String displayPostDate = job_snapshot1.child("postDate").getValue(String.class);

                                                            String postID = job_snapshot1.getKey();

                                                            PWD_AvailableJobs_Model pwd_Model = new PWD_AvailableJobs_Model(imageURL, displayPostTitle, displayCompanyName, displayPostDate, postID);
                                                            list.add(pwd_Model);
                                                            if(!list.isEmpty()){
                                                                recyclerView.setVisibility(View.VISIBLE);
                                                                mascot.setVisibility(View.GONE);
                                                                tv_noJobsAvailable.setVisibility(View.GONE);
                                                                textInputLayout_filterJobTitle.setVisibility(View.VISIBLE);
                                                                //textInputLayout_filterSkillOrDisability.setVisibility(View.VISIBLE);
                                                            }else if(list.isEmpty()){
                                                                recyclerView.setVisibility(View.GONE);
                                                                mascot.setVisibility(View.VISIBLE);
                                                                tv_noJobsAvailable.setVisibility(View.VISIBLE);
                                                                textInputLayout_filterJobTitle.setVisibility(View.GONE);
                                                                //textInputLayout_filterSkillOrDisability.setVisibility(View.GONE);
                                                            }
                                                            Collections.reverse(list);
                                                            myAdapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_3_VisualDisability.this, list);
                                                            recyclerView.setAdapter(myAdapter);
                                                            myAdapter.notifyDataSetChanged();

                                                        }

                                                    }else if(job_educationalAttainment.equals("Master's Level")&& (job_educationalAttainment.equals(pwd_educationalAttainment)
                                                            || pwd_educationalAttainment.equals("Doctorate Level"))){

                                                        if(job_location.equals(pwd_location)){
                                                            String imageURL = job_snapshot1.child("imageURL").getValue(String.class);
                                                            String displayPostTitle = job_snapshot1.child("jobTitle" +
                                                                    "").getValue(String.class);
                                                            String displayCompanyName = job_snapshot1.child("companyName").getValue(String.class);
                                                            String displayPostDate = job_snapshot1.child("postDate").getValue(String.class);

                                                            String postID = job_snapshot1.getKey();

                                                            PWD_AvailableJobs_Model pwd_Model = new PWD_AvailableJobs_Model(imageURL, displayPostTitle, displayCompanyName, displayPostDate, postID);
                                                            list.add(pwd_Model);
                                                            if(!list.isEmpty()){
                                                                recyclerView.setVisibility(View.VISIBLE);
                                                                mascot.setVisibility(View.GONE);
                                                                tv_noJobsAvailable.setVisibility(View.GONE);
                                                                textInputLayout_filterJobTitle.setVisibility(View.VISIBLE);
                                                                //textInputLayout_filterSkillOrDisability.setVisibility(View.VISIBLE);
                                                            }else if(list.isEmpty()){
                                                                recyclerView.setVisibility(View.GONE);
                                                                mascot.setVisibility(View.VISIBLE);
                                                                tv_noJobsAvailable.setVisibility(View.VISIBLE);
                                                                textInputLayout_filterJobTitle.setVisibility(View.GONE);
                                                                //textInputLayout_filterSkillOrDisability.setVisibility(View.GONE);
                                                            }
                                                            Collections.reverse(list);
                                                            myAdapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_3_VisualDisability.this, list);
                                                            recyclerView.setAdapter(myAdapter);
                                                            myAdapter.notifyDataSetChanged();

                                                        }

                                                    }else if(job_educationalAttainment.equals("Doctorate Level") && job_educationalAttainment.equals(pwd_educationalAttainment)){

                                                        if(job_location.equals(pwd_location)){
                                                            String imageURL = job_snapshot1.child("imageURL").getValue(String.class);
                                                            String displayPostTitle = job_snapshot1.child("jobTitle" +
                                                                    "").getValue(String.class);
                                                            String displayCompanyName = job_snapshot1.child("companyName").getValue(String.class);
                                                            String displayPostDate = job_snapshot1.child("postDate").getValue(String.class);

                                                            String postID = job_snapshot1.getKey();

                                                            PWD_AvailableJobs_Model pwd_Model = new PWD_AvailableJobs_Model(imageURL, displayPostTitle, displayCompanyName, displayPostDate, postID);
                                                            list.add(pwd_Model);
                                                            if(!list.isEmpty()){
                                                                recyclerView.setVisibility(View.VISIBLE);
                                                                mascot.setVisibility(View.GONE);
                                                                tv_noJobsAvailable.setVisibility(View.GONE);
                                                                textInputLayout_filterJobTitle.setVisibility(View.VISIBLE);
                                                                //textInputLayout_filterSkillOrDisability.setVisibility(View.VISIBLE);
                                                            }else if(list.isEmpty()){
                                                                recyclerView.setVisibility(View.GONE);
                                                                mascot.setVisibility(View.VISIBLE);
                                                                tv_noJobsAvailable.setVisibility(View.VISIBLE);
                                                                textInputLayout_filterJobTitle.setVisibility(View.GONE);
                                                                //textInputLayout_filterSkillOrDisability.setVisibility(View.GONE);
                                                            }
                                                            Collections.reverse(list);
                                                            myAdapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_3_VisualDisability.this, list);
                                                            recyclerView.setAdapter(myAdapter);
                                                            myAdapter.notifyDataSetChanged();

                                                        }

                                                    }
                                                }//checks if job educRequirement is set to true
                                                else{ // if educRequirement for a job post is not required, the system will not check pwd's educAttainment level.
                                                    if(job_location.equals(pwd_location)){
                                                        String imageURL = job_snapshot1.child("imageURL").getValue(String.class);
                                                        String displayPostTitle = job_snapshot1.child("jobTitle" +
                                                                "").getValue(String.class);
                                                        String displayCompanyName = job_snapshot1.child("companyName").getValue(String.class);
                                                        String displayPostDate = job_snapshot1.child("postDate").getValue(String.class);

                                                        String postID = job_snapshot1.getKey();

                                                        PWD_AvailableJobs_Model pwd_Model = new PWD_AvailableJobs_Model(imageURL, displayPostTitle, displayCompanyName, displayPostDate, postID);
                                                        list.add(pwd_Model);
                                                        if(!list.isEmpty()){
                                                            recyclerView.setVisibility(View.VISIBLE);
                                                            mascot.setVisibility(View.GONE);
                                                            tv_noJobsAvailable.setVisibility(View.GONE);
                                                            textInputLayout_filterJobTitle.setVisibility(View.VISIBLE);
                                                            //textInputLayout_filterSkillOrDisability.setVisibility(View.VISIBLE);
                                                        }else if(list.isEmpty()){
                                                            recyclerView.setVisibility(View.GONE);
                                                            mascot.setVisibility(View.VISIBLE);
                                                            tv_noJobsAvailable.setVisibility(View.VISIBLE);
                                                            textInputLayout_filterJobTitle.setVisibility(View.GONE);
                                                            //textInputLayout_filterSkillOrDisability.setVisibility(View.GONE);
                                                        }
                                                        Collections.reverse(list);
                                                        myAdapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_3_VisualDisability.this, list);
                                                        recyclerView.setAdapter(myAdapter);
                                                        myAdapter.notifyDataSetChanged();

                                                    }
                                                }
                                            }//checks pwd if workExperience = With Experience
                                        }else{ // Without work experience required the system will not check if pwd does not have work experience
                                            if(job_educationalAttainmentRequirement.equals("true")){
                                                //if educRequirement for a job post is required, the system will check pwd's educAttainment level.
                                                if(job_educationalAttainment.equals("Elementary Level")){// checks if job post educAttainment matches pwd's educAttainment
                                                    //no need to check pwd_educAttainment
                                                    if(job_location.equals(pwd_location)){
                                                        String imageURL = job_snapshot1.child("imageURL").getValue(String.class);
                                                        String displayPostTitle = job_snapshot1.child("jobTitle" +
                                                                "").getValue(String.class);
                                                        String displayCompanyName = job_snapshot1.child("companyName").getValue(String.class);
                                                        String displayPostDate = job_snapshot1.child("postDate").getValue(String.class);

                                                        String postID = job_snapshot1.getKey();

                                                        PWD_AvailableJobs_Model pwd_Model = new PWD_AvailableJobs_Model(imageURL, displayPostTitle, displayCompanyName, displayPostDate, postID);
                                                        list.add(pwd_Model);
                                                        if(!list.isEmpty()){
                                                            recyclerView.setVisibility(View.VISIBLE);
                                                            mascot.setVisibility(View.GONE);
                                                            tv_noJobsAvailable.setVisibility(View.GONE);
                                                            textInputLayout_filterJobTitle.setVisibility(View.VISIBLE);
                                                            //textInputLayout_filterSkillOrDisability.setVisibility(View.VISIBLE);
                                                        }else if(list.isEmpty()){
                                                            recyclerView.setVisibility(View.GONE);
                                                            mascot.setVisibility(View.VISIBLE);
                                                            tv_noJobsAvailable.setVisibility(View.VISIBLE);
                                                            textInputLayout_filterJobTitle.setVisibility(View.GONE);
                                                            //textInputLayout_filterSkillOrDisability.setVisibility(View.GONE);
                                                        }
                                                        Collections.reverse(list);
                                                        myAdapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_3_VisualDisability.this, list);
                                                        recyclerView.setAdapter(myAdapter);
                                                        myAdapter.notifyDataSetChanged();

                                                    }

                                                }else if(job_educationalAttainment.equals("High School Level") && (job_educationalAttainment.equals(pwd_educationalAttainment)
                                                        || pwd_educationalAttainment.equals("Associate Level")
                                                        || pwd_educationalAttainment.equals("Bachelor Level")
                                                        || pwd_educationalAttainment.equals("Master's Level")
                                                        || pwd_educationalAttainment.equals("Doctorate Level"))){

                                                    if(job_location.equals(pwd_location)){
                                                        String imageURL = job_snapshot1.child("imageURL").getValue(String.class);
                                                        String displayPostTitle = job_snapshot1.child("jobTitle" +
                                                                "").getValue(String.class);
                                                        String displayCompanyName = job_snapshot1.child("companyName").getValue(String.class);
                                                        String displayPostDate = job_snapshot1.child("postDate").getValue(String.class);

                                                        String postID = job_snapshot1.getKey();

                                                        PWD_AvailableJobs_Model pwd_Model = new PWD_AvailableJobs_Model(imageURL, displayPostTitle, displayCompanyName, displayPostDate, postID);
                                                        list.add(pwd_Model);
                                                        if(!list.isEmpty()){
                                                            recyclerView.setVisibility(View.VISIBLE);
                                                            mascot.setVisibility(View.GONE);
                                                            tv_noJobsAvailable.setVisibility(View.GONE);
                                                            textInputLayout_filterJobTitle.setVisibility(View.VISIBLE);
                                                            //textInputLayout_filterSkillOrDisability.setVisibility(View.VISIBLE);
                                                        }else if(list.isEmpty()){
                                                            recyclerView.setVisibility(View.GONE);
                                                            mascot.setVisibility(View.VISIBLE);
                                                            tv_noJobsAvailable.setVisibility(View.VISIBLE);
                                                            textInputLayout_filterJobTitle.setVisibility(View.GONE);
                                                            //textInputLayout_filterSkillOrDisability.setVisibility(View.GONE);
                                                        }
                                                        Collections.reverse(list);
                                                        myAdapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_3_VisualDisability.this, list);
                                                        recyclerView.setAdapter(myAdapter);
                                                        myAdapter.notifyDataSetChanged();

                                                    }

                                                }else if(job_educationalAttainment.equals("Associate Level") && (job_educationalAttainment.equals(pwd_educationalAttainment)
                                                        || pwd_educationalAttainment.equals("Bachelor Level")
                                                        || pwd_educationalAttainment.equals("Master's Level")
                                                        || pwd_educationalAttainment.equals("Doctorate Level"))){

                                                    if(job_location.equals(pwd_location)){
                                                        String imageURL = job_snapshot1.child("imageURL").getValue(String.class);
                                                        String displayPostTitle = job_snapshot1.child("jobTitle" +
                                                                "").getValue(String.class);
                                                        String displayCompanyName = job_snapshot1.child("companyName").getValue(String.class);
                                                        String displayPostDate = job_snapshot1.child("postDate").getValue(String.class);

                                                        String postID = job_snapshot1.getKey();

                                                        PWD_AvailableJobs_Model pwd_Model = new PWD_AvailableJobs_Model(imageURL, displayPostTitle, displayCompanyName, displayPostDate, postID);
                                                        list.add(pwd_Model);
                                                        if(!list.isEmpty()){
                                                            recyclerView.setVisibility(View.VISIBLE);
                                                            mascot.setVisibility(View.GONE);
                                                            tv_noJobsAvailable.setVisibility(View.GONE);
                                                            textInputLayout_filterJobTitle.setVisibility(View.VISIBLE);
                                                            //textInputLayout_filterSkillOrDisability.setVisibility(View.VISIBLE);
                                                        }else if(list.isEmpty()){
                                                            recyclerView.setVisibility(View.GONE);
                                                            mascot.setVisibility(View.VISIBLE);
                                                            tv_noJobsAvailable.setVisibility(View.VISIBLE);
                                                            textInputLayout_filterJobTitle.setVisibility(View.GONE);
                                                            //textInputLayout_filterSkillOrDisability.setVisibility(View.GONE);
                                                        }
                                                        Collections.reverse(list);
                                                        myAdapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_3_VisualDisability.this, list);
                                                        recyclerView.setAdapter(myAdapter);
                                                        myAdapter.notifyDataSetChanged();

                                                    }

                                                }else if(job_educationalAttainment.equals("Bachelor Level") && (job_educationalAttainment.equals(pwd_educationalAttainment)
                                                        || pwd_educationalAttainment.equals("Master's Level")
                                                        || pwd_educationalAttainment.equals("Doctorate Level"))){

                                                    if(job_location.equals(pwd_location)){
                                                        String imageURL = job_snapshot1.child("imageURL").getValue(String.class);
                                                        String displayPostTitle = job_snapshot1.child("jobTitle" +
                                                                "").getValue(String.class);
                                                        String displayCompanyName = job_snapshot1.child("companyName").getValue(String.class);
                                                        String displayPostDate = job_snapshot1.child("postDate").getValue(String.class);

                                                        String postID = job_snapshot1.getKey();

                                                        PWD_AvailableJobs_Model pwd_Model = new PWD_AvailableJobs_Model(imageURL, displayPostTitle, displayCompanyName, displayPostDate, postID);
                                                        list.add(pwd_Model);
                                                        if(!list.isEmpty()){
                                                            recyclerView.setVisibility(View.VISIBLE);
                                                            mascot.setVisibility(View.GONE);
                                                            tv_noJobsAvailable.setVisibility(View.GONE);
                                                            textInputLayout_filterJobTitle.setVisibility(View.VISIBLE);
                                                            //textInputLayout_filterSkillOrDisability.setVisibility(View.VISIBLE);
                                                        }else if(list.isEmpty()){
                                                            recyclerView.setVisibility(View.GONE);
                                                            mascot.setVisibility(View.VISIBLE);
                                                            tv_noJobsAvailable.setVisibility(View.VISIBLE);
                                                            textInputLayout_filterJobTitle.setVisibility(View.GONE);
                                                            //textInputLayout_filterSkillOrDisability.setVisibility(View.GONE);
                                                        }
                                                        Collections.reverse(list);
                                                        myAdapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_3_VisualDisability.this, list);
                                                        recyclerView.setAdapter(myAdapter);
                                                        myAdapter.notifyDataSetChanged();

                                                    }

                                                }else if(job_educationalAttainment.equals("Master's Level")&& (job_educationalAttainment.equals(pwd_educationalAttainment)
                                                        || pwd_educationalAttainment.equals("Doctorate Level"))){

                                                    if(job_location.equals(pwd_location)){
                                                        String imageURL = job_snapshot1.child("imageURL").getValue(String.class);
                                                        String displayPostTitle = job_snapshot1.child("jobTitle" +
                                                                "").getValue(String.class);
                                                        String displayCompanyName = job_snapshot1.child("companyName").getValue(String.class);
                                                        String displayPostDate = job_snapshot1.child("postDate").getValue(String.class);

                                                        String postID = job_snapshot1.getKey();

                                                        PWD_AvailableJobs_Model pwd_Model = new PWD_AvailableJobs_Model(imageURL, displayPostTitle, displayCompanyName, displayPostDate, postID);
                                                        list.add(pwd_Model);
                                                        if(!list.isEmpty()){
                                                            recyclerView.setVisibility(View.VISIBLE);
                                                            mascot.setVisibility(View.GONE);
                                                            tv_noJobsAvailable.setVisibility(View.GONE);
                                                            textInputLayout_filterJobTitle.setVisibility(View.VISIBLE);
                                                            //textInputLayout_filterSkillOrDisability.setVisibility(View.VISIBLE);
                                                        }else if(list.isEmpty()){
                                                            recyclerView.setVisibility(View.GONE);
                                                            mascot.setVisibility(View.VISIBLE);
                                                            tv_noJobsAvailable.setVisibility(View.VISIBLE);
                                                            textInputLayout_filterJobTitle.setVisibility(View.GONE);
                                                            //textInputLayout_filterSkillOrDisability.setVisibility(View.GONE);
                                                        }
                                                        Collections.reverse(list);
                                                        myAdapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_3_VisualDisability.this, list);
                                                        recyclerView.setAdapter(myAdapter);
                                                        myAdapter.notifyDataSetChanged();

                                                    }

                                                }else if(job_educationalAttainment.equals("Doctorate Level") && job_educationalAttainment.equals(pwd_educationalAttainment)){

                                                    if(job_location.equals(pwd_location)){
                                                        String imageURL = job_snapshot1.child("imageURL").getValue(String.class);
                                                        String displayPostTitle = job_snapshot1.child("jobTitle" +
                                                                "").getValue(String.class);
                                                        String displayCompanyName = job_snapshot1.child("companyName").getValue(String.class);
                                                        String displayPostDate = job_snapshot1.child("postDate").getValue(String.class);

                                                        String postID = job_snapshot1.getKey();

                                                        PWD_AvailableJobs_Model pwd_Model = new PWD_AvailableJobs_Model(imageURL, displayPostTitle, displayCompanyName, displayPostDate, postID);
                                                        list.add(pwd_Model);
                                                        if(!list.isEmpty()){
                                                            recyclerView.setVisibility(View.VISIBLE);
                                                            mascot.setVisibility(View.GONE);
                                                            tv_noJobsAvailable.setVisibility(View.GONE);
                                                            textInputLayout_filterJobTitle.setVisibility(View.VISIBLE);
                                                            //textInputLayout_filterSkillOrDisability.setVisibility(View.VISIBLE);
                                                        }else if(list.isEmpty()){
                                                            recyclerView.setVisibility(View.GONE);
                                                            mascot.setVisibility(View.VISIBLE);
                                                            tv_noJobsAvailable.setVisibility(View.VISIBLE);
                                                            textInputLayout_filterJobTitle.setVisibility(View.GONE);
                                                            //textInputLayout_filterSkillOrDisability.setVisibility(View.GONE);
                                                        }
                                                        Collections.reverse(list);myAdapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_3_VisualDisability.this, list);
                                                        recyclerView.setAdapter(myAdapter);
                                                        myAdapter.notifyDataSetChanged();

                                                    }

                                                }
                                            }//checks if job educRequirement is set to true
                                            else{ // if educRequirement for a job post is not required, the system will not check pwd's educAttainment level.
                                                if(job_location.equals(pwd_location)){
                                                    String imageURL = job_snapshot1.child("imageURL").getValue(String.class);
                                                    String displayPostTitle = job_snapshot1.child("jobTitle" +
                                                            "").getValue(String.class);
                                                    String displayCompanyName = job_snapshot1.child("companyName").getValue(String.class);
                                                    String displayPostDate = job_snapshot1.child("postDate").getValue(String.class);

                                                    String postID = job_snapshot1.getKey();

                                                    PWD_AvailableJobs_Model pwd_Model = new PWD_AvailableJobs_Model(imageURL, displayPostTitle, displayCompanyName, displayPostDate, postID);
                                                    list.add(pwd_Model);
                                                    if(!list.isEmpty()){
                                                        recyclerView.setVisibility(View.VISIBLE);
                                                        mascot.setVisibility(View.GONE);
                                                        tv_noJobsAvailable.setVisibility(View.GONE);
                                                        textInputLayout_filterJobTitle.setVisibility(View.VISIBLE);
                                                        //textInputLayout_filterSkillOrDisability.setVisibility(View.VISIBLE);
                                                    }else if(list.isEmpty()){
                                                        recyclerView.setVisibility(View.GONE);
                                                        mascot.setVisibility(View.VISIBLE);
                                                        tv_noJobsAvailable.setVisibility(View.VISIBLE);
                                                        textInputLayout_filterJobTitle.setVisibility(View.GONE);
                                                        //textInputLayout_filterSkillOrDisability.setVisibility(View.GONE);
                                                    }
                                                    Collections.reverse(list);
                                                    myAdapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_3_VisualDisability.this, list);
                                                    recyclerView.setAdapter(myAdapter);
                                                    myAdapter.notifyDataSetChanged();

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
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }
}