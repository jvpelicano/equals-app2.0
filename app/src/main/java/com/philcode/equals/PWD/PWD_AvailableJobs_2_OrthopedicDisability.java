package com.philcode.equals.PWD;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.switchmaterial.SwitchMaterial;
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

public class PWD_AvailableJobs_2_OrthopedicDisability extends AppCompatActivity {
    DatabaseReference refForJobs, refUser;

    RecyclerView recyclerView;
    PWD_AvailableJobs_MyAdapter myAdapter;

    ArrayList<PWD_AvailableJobs_Model> list;
    SwitchMaterial switchPriority;
    TextView tv_noJobsAvailable;
    ImageView mascot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pwd_available_jobs);
        list = new ArrayList<>();

        recyclerView = findViewById(R.id.myRecycler);
        tv_noJobsAvailable = findViewById(R.id.tv_noJobsAvailable);
        mascot = findViewById(R.id.mascot);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        switchPriority = findViewById(R.id.switchPriority);
        getWindow().getDecorView().post(new Runnable() {

            @Override
            public void run() {
                mascot.setVisibility(View.GONE);
                tv_noJobsAvailable.setVisibility(View.GONE);
            }

        });
        refUser = FirebaseDatabase.getInstance().getReference().child("PWD/" + userId);
        refUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot pwd_dataSnapshot) {
                final String pwd_SkillCategory = pwd_dataSnapshot.child("skill").getValue().toString();
                final String pwd_educationalAttainment = pwd_dataSnapshot.child("educationalAttainment").getValue().toString();
                final String pwd_workExp = pwd_dataSnapshot.child("workExperience").getValue().toString();
                final String pwd_location = pwd_dataSnapshot.child("city").getValue().toString();
                //Check Job Offer Info
                // checking PWD for type of disability
                refForJobs = FirebaseDatabase.getInstance().getReference().child("Job_Offers");
                refForJobs.orderByChild("typeOfDisability1").equalTo("Orthopedic Disability").addValueEventListener(new ValueEventListener() { //checking Job_Offers
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot jobFetch_dataSnapshot1) {
                        if(jobFetch_dataSnapshot1.hasChild("Job_Offers")){
                            recyclerView.setVisibility(View.VISIBLE);
                            mascot.setVisibility(View.GONE);
                            tv_noJobsAvailable.setVisibility(View.GONE);
                        }else{
                            recyclerView.setVisibility(View.GONE);
                            mascot.setVisibility(View.VISIBLE);
                            tv_noJobsAvailable.setVisibility(View.VISIBLE);
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
                                                    }else if(list.isEmpty()){
                                                        recyclerView.setVisibility(View.GONE);
                                                        mascot.setVisibility(View.VISIBLE);
                                                        tv_noJobsAvailable.setVisibility(View.VISIBLE);
                                                    }
                                                    Collections.reverse(list);
                                                    myAdapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_2_OrthopedicDisability.this, list);
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
                                                    }else if(list.isEmpty()){
                                                        recyclerView.setVisibility(View.GONE);
                                                        mascot.setVisibility(View.VISIBLE);
                                                        tv_noJobsAvailable.setVisibility(View.VISIBLE);
                                                    }
                                                    Collections.reverse(list);
                                                    myAdapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_2_OrthopedicDisability.this, list);
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
                                                    }else if(list.isEmpty()){
                                                        recyclerView.setVisibility(View.GONE);
                                                        mascot.setVisibility(View.VISIBLE);
                                                        tv_noJobsAvailable.setVisibility(View.VISIBLE);
                                                    }
                                                    Collections.reverse(list);
                                                    myAdapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_2_OrthopedicDisability.this, list);
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
                                                    }else if(list.isEmpty()){
                                                        recyclerView.setVisibility(View.GONE);
                                                        mascot.setVisibility(View.VISIBLE);
                                                        tv_noJobsAvailable.setVisibility(View.VISIBLE);
                                                    }
                                                    Collections.reverse(list);
                                                    myAdapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_2_OrthopedicDisability.this, list);
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
                                                    }else if(list.isEmpty()){
                                                        recyclerView.setVisibility(View.GONE);
                                                        mascot.setVisibility(View.VISIBLE);
                                                        tv_noJobsAvailable.setVisibility(View.VISIBLE);
                                                    }
                                                    Collections.reverse(list);
                                                    myAdapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_2_OrthopedicDisability.this, list);
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
                                                    }else if(list.isEmpty()){
                                                        recyclerView.setVisibility(View.GONE);
                                                        mascot.setVisibility(View.VISIBLE);
                                                        tv_noJobsAvailable.setVisibility(View.VISIBLE);
                                                    }
                                                    Collections.reverse(list);
                                                    myAdapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_2_OrthopedicDisability.this, list);
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
                                                }else if(list.isEmpty()){
                                                    recyclerView.setVisibility(View.GONE);
                                                    mascot.setVisibility(View.VISIBLE);
                                                    tv_noJobsAvailable.setVisibility(View.VISIBLE);
                                                }
                                                Collections.reverse(list);
                                                myAdapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_2_OrthopedicDisability.this, list);
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
                                                }else if(list.isEmpty()){
                                                    recyclerView.setVisibility(View.GONE);
                                                    mascot.setVisibility(View.VISIBLE);
                                                    tv_noJobsAvailable.setVisibility(View.VISIBLE);
                                                }
                                                Collections.reverse(list);
                                                myAdapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_2_OrthopedicDisability.this, list);
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
                                                }else if(list.isEmpty()){
                                                    recyclerView.setVisibility(View.GONE);
                                                    mascot.setVisibility(View.VISIBLE);
                                                    tv_noJobsAvailable.setVisibility(View.VISIBLE);
                                                }
                                                Collections.reverse(list);
                                                myAdapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_2_OrthopedicDisability.this, list);
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
                                                }else if(list.isEmpty()){
                                                    recyclerView.setVisibility(View.GONE);
                                                    mascot.setVisibility(View.VISIBLE);
                                                    tv_noJobsAvailable.setVisibility(View.VISIBLE);
                                                }
                                                Collections.reverse(list);
                                                myAdapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_2_OrthopedicDisability.this, list);
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
                                                }else if(list.isEmpty()){
                                                    recyclerView.setVisibility(View.GONE);
                                                    mascot.setVisibility(View.VISIBLE);
                                                    tv_noJobsAvailable.setVisibility(View.VISIBLE);
                                                }
                                                Collections.reverse(list);
                                                myAdapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_2_OrthopedicDisability.this, list);
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
                                                }else if(list.isEmpty()){
                                                    recyclerView.setVisibility(View.GONE);
                                                    mascot.setVisibility(View.VISIBLE);
                                                    tv_noJobsAvailable.setVisibility(View.VISIBLE);
                                                }
                                                Collections.reverse(list);
                                                myAdapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_2_OrthopedicDisability.this, list);
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
                                                }else if(list.isEmpty()){
                                                    recyclerView.setVisibility(View.GONE);
                                                    mascot.setVisibility(View.VISIBLE);
                                                    tv_noJobsAvailable.setVisibility(View.VISIBLE);
                                                }
                                                Collections.reverse(list);myAdapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_2_OrthopedicDisability.this, list);
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
                                            }else if(list.isEmpty()){
                                                recyclerView.setVisibility(View.GONE);
                                                mascot.setVisibility(View.VISIBLE);
                                                tv_noJobsAvailable.setVisibility(View.VISIBLE);
                                            }
                                            Collections.reverse(list);
                                            myAdapter = new PWD_AvailableJobs_MyAdapter(PWD_AvailableJobs_2_OrthopedicDisability.this, list);
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
}
