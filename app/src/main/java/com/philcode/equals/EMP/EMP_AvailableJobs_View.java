package com.philcode.equals.EMP;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.philcode.equals.R;

public class EMP_AvailableJobs_View extends AppCompatActivity {

    private FloatingActionButton fab_main, fab1_resumes, fab2_potential, fab3_delete;
    private Animation fab_open, fab_close, fab_clock, fab_anticlock;
    TextView textview_resume, textview_potential, textView_delete;

    Boolean isOpen = false;

    DatabaseReference refForJobs;
    private static final String TAG = "PWD_AvailableJobs_View";
    Button sendResume;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emp_availablejobs_view);
        Log.d(TAG, "onCreate: started.");

        getIncomingIntent();


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


        fab3_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder alert =  new AlertDialog.Builder(EMP_AvailableJobs_View.this);
                alert.setMessage("This post will be deleted and you won't be able to find it anymore.").setCancelable(false)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final String imageURL = getIntent().getStringExtra("imageURL");
                                refForJobs = FirebaseDatabase.getInstance().getReference().child("Job_Offers");
                                refForJobs.orderByChild("imageURL").equalTo(imageURL).addListenerForSingleValueEvent(new ValueEventListener() {

                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                            String parent = dataSnapshot1.getKey();
                                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Job_Offers");
                                            ref.child(parent).removeValue();
                                            startActivity(new Intent(EMP_AvailableJobs_View.this, a_EmployeeContentMainActivity.class));

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


        fab2_potential.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(EMP_AvailableJobs_View.this, EMP_ViewPotential_All.class);
                startActivity(i);

            }
        });


        fab1_resumes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String imageURL = getIntent().getStringExtra("imageURL");

                refForJobs = FirebaseDatabase.getInstance().getReference().child("Job_Offers");
                refForJobs.orderByChild("imageURL").equalTo(imageURL).addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            String parent = dataSnapshot1.getKey();
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Job_Offers/" + parent);
                            ref.addListenerForSingleValueEvent(new ValueEventListener() {

                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChild("Resumes")) {

                                        // Toast.makeText(EMP_AvailableJobs_View.this, "No Resume", Toast.LENGTH_SHORT).show();

                                        Intent i = new Intent(getApplicationContext(), EMP_ViewResume.class);
                                        i.putExtra("imageURL", imageURL);
                                        startActivity(i);
                                        return;
                                    } else {
                                        AlertDialog.Builder alert =  new AlertDialog.Builder(EMP_AvailableJobs_View.this);
                                        alert.setMessage("You haven't received resume from applicants yet.").setCancelable(false)
                                                .setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
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
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

        });





    }



    private void getIncomingIntent(){
        Log.d(TAG, "getIncomingIntent: checking for incoming intents.");

        if(getIntent().hasExtra("imageURL")
                && getIntent().hasExtra("postTitle")
                && getIntent().hasExtra("postDescription")
                || getIntent().hasExtra("postLocation")
                || getIntent().hasExtra("typeofDisability1")
                || getIntent().hasExtra("typeofDisability2")
                || getIntent().hasExtra("typeofDisability3")
                || getIntent().hasExtra("typeofDisabilityMore")
        )
        {
            Log.d(TAG, "getIncomingIntent: found intent extras.");
            String imageURL = getIntent().getStringExtra("imageURL");
            String postTitle = getIntent().getStringExtra("postTitle");
            String postLocation = getIntent().getStringExtra("postLocation");
            String postCompanyCity = getIntent().getStringExtra("city");
            String typeOfDisability1 = getIntent().getStringExtra("typeOfDisability1");
            String typeOfDisability2 = getIntent().getStringExtra("typeOfDisability2");
            String typeOfDisability3 = getIntent().getStringExtra("typeOfDisability3");
            String typeOfDisabilityMore = getIntent().getStringExtra("typeOfDisabilityMore");
            String postDescription = getIntent().getStringExtra("postDescription");
            String postDate = getIntent().getStringExtra("postDate");
            String expDate = getIntent().getStringExtra("expDate");
            String permission = getIntent().getStringExtra("permission");
            String companyName = getIntent().getStringExtra("companyName");

            String jobSkill1 = getIntent().getStringExtra("jobSkill1");
            String jobSkill2 = getIntent().getStringExtra("jobSkill2");
            String jobSkill3 = getIntent().getStringExtra("jobSkill3");
            String jobSkill4 = getIntent().getStringExtra("jobSkill4");
            String jobSkill5 = getIntent().getStringExtra("jobSkill5");
            String jobSkill6 = getIntent().getStringExtra("jobSkill6");
            String jobSkill7 = getIntent().getStringExtra("jobSkill7");
            String jobSkill8 = getIntent().getStringExtra("jobSkill8");
            String jobSkill9 = getIntent().getStringExtra("jobSkill9");
            String jobSkill10 = getIntent().getStringExtra("jobSkill10");

            //added
            String educationalAttainment = getIntent().getStringExtra("educationalAttainment");
            String categorySkill = getIntent().getStringExtra("skill");
            String workExperience = getIntent().getStringExtra("workExperience");

            setImage(imageURL,postTitle, postDescription,postLocation, postCompanyCity
                    ,typeOfDisability1,typeOfDisability2,typeOfDisability3, typeOfDisabilityMore,
                    postDate, expDate, permission, companyName,educationalAttainment, categorySkill,
                    workExperience);
        }
    }


    private void setImage(String imageURL, String postTitle, String postDescription,
                          String postLocation, String postCompanyCity, String typeOfDisability1,
                          String typeOfDisability2, String typeOfDisability3, String typeOfDisabilityMore,
                          String postDate, String expDate, String permission, String companyName,
                          String educationalAttainment, String categorySkill,
                          String workExperience){
        Log.d(TAG, "setImage: setting te image and name to widgets.");

        TextView displayTypeOfDisability1 = findViewById(R.id.displayTypeOfDisability1);
        TextView displayTypeOfDisability2 =findViewById(R.id.displayTypeOfDisability2);
        TextView displayTypeOfDisability3 = findViewById(R.id.displayTypeOfDisability3);
        TextView displayTypeOfDisabilityMore = findViewById(R.id.displayTypeOfDisabilityMore);
        TextView displayPostLocation = findViewById(R.id.displayPostLocation);


        ImageView images = findViewById(R.id.displayPostPic);
        Glide.with(this)
                .asBitmap()
                .load(imageURL)
                .into(images);
        TextView displayPostTitle = findViewById(R.id.displayPostTitle);
        displayPostTitle.setText(postTitle);

        TextView displayPostDescription = findViewById(R.id.displayPostDescription);
        displayPostDescription.setText(postDescription);
        displayPostLocation.setText(String.format("%s, %s", postLocation, postCompanyCity));
        displayTypeOfDisability1.setText(typeOfDisability1);
        displayTypeOfDisability2.setText(typeOfDisability2);
        displayTypeOfDisability3.setText(typeOfDisability3);
        displayTypeOfDisabilityMore.setText(typeOfDisabilityMore);
        TextView displaySkill1 = findViewById(R.id.displaySkill1);
        TextView displaySkill2 = findViewById(R.id.displaySkill2);
        TextView displaySkill3 = findViewById(R.id.displaySkill3);
        TextView displaySkill4 = findViewById(R.id.displaySkill4);
        TextView displaySkill5 = findViewById(R.id.displaySkill5);
        TextView displaySkill6 = findViewById(R.id.displaySkill6);
        TextView displaySkill7 = findViewById(R.id.displaySkill7);
        TextView displaySkill8 = findViewById(R.id.displaySkill8);
        TextView displaySkill9 = findViewById(R.id.displaySkill9);
        TextView displaySkill10 = findViewById(R.id.displaySkill10);
        TextView displayExpDate = findViewById(R.id.displayExpDate);
        displayExpDate.setText(expDate);

        TextView displayPermission = findViewById(R.id.displayPermission);
        displayPermission.setText(permission);

        TextView displayCompanyName = findViewById(R.id.displayCompanyName);
        displayCompanyName.setText(companyName);

        TextView displayEducationalAttainment = findViewById(R.id.displayEducationalAttainment);
        displayEducationalAttainment.setText(educationalAttainment);
        TextView displayCategorySkill = findViewById(R.id.displayCategorySkill);
        displayCategorySkill.setText(categorySkill);
        TextView displayTotalWorkExperience = findViewById(R.id.displayTotalWorkExperience);
        displayTotalWorkExperience.setText(workExperience);

        final String d1 = "Orthopedic Disability";
        final String d2 = "Partial Vision Disability";
        final String d3 = "Hearing Disability";
        final String d4 = "Other Disabilities";


        if(typeOfDisability1.equals(d1)) {
            displayTypeOfDisability1.setText(typeOfDisability1);
        }else{
            displayTypeOfDisability1.setVisibility(View.GONE);
        }
        if(typeOfDisability2.equals(d2)) {
            displayTypeOfDisability2.setText(typeOfDisability2);
        }else{
            displayTypeOfDisability2.setVisibility(View.GONE);
        }
        if(typeOfDisability3.equals(d3)) {
            displayTypeOfDisability3.setText(typeOfDisability3);
        }else{
            displayTypeOfDisability3.setVisibility(View.GONE);
        }
        if(typeOfDisabilityMore.equals(d4)) {
            displayTypeOfDisabilityMore.setText(typeOfDisabilityMore);
        }else{
            displayTypeOfDisabilityMore.setVisibility(View.GONE);
        }

//------------
        String postJobID = getIntent().getStringExtra("ImageUploadID"); //not getting intent
        DatabaseReference db_JobSkill = FirebaseDatabase.getInstance().getReference("Job_Offers").child(postJobID);
        db_JobSkill.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("jobSkill1")){
                    displaySkill1.setText(snapshot.child("jobSkill1").getValue().toString());
                }

                if(snapshot.hasChild("jobSkill2")){
                    displaySkill2.setText(snapshot.child("jobSkill2").getValue().toString());
                }

                if(snapshot.hasChild("jobSkill3")){
                    displaySkill3.setText(snapshot.child("jobSkill3").getValue().toString());
                }

                if(snapshot.hasChild("jobSkill4")){
                    displaySkill4.setText(snapshot.child("jobSkill4").getValue().toString());
                }

                if(snapshot.hasChild("jobSkill5")){
                    displaySkill5.setText(snapshot.child("jobSkill5").getValue().toString());
                }

                if(snapshot.hasChild("jobSkill6")){
                    displaySkill6.setText(snapshot.child("jobSkill6").getValue().toString());
                }

                if(snapshot.hasChild("jobSkill7")){
                    displaySkill7.setText(snapshot.child("jobSkill7").getValue().toString());
                }

                if(snapshot.hasChild("jobSkill8")){
                    displaySkill8.setText(snapshot.child("jobSkill8").getValue().toString());
                }

                if(snapshot.hasChild("jobSkill9")){
                    displaySkill9.setText(snapshot.child("jobSkill9").getValue().toString());
                }

                if(snapshot.hasChild("jobSkill10")){
                    displaySkill10.setText(snapshot.child("jobSkill10").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if(typeOfDisability1.equals(d1)) {
            displayTypeOfDisability1.setText(typeOfDisability1);
        }else{
            displayTypeOfDisability1.setVisibility(View.GONE);
        }
        if(typeOfDisability2.equals(d2)) {
            displayTypeOfDisability2.setText(typeOfDisability2);
        }else{
            displayTypeOfDisability2.setVisibility(View.GONE);
        }
        if(typeOfDisability3.equals(d3)) {
            displayTypeOfDisability3.setText(typeOfDisability3);
        }else {
            displayTypeOfDisability3.setVisibility(View.GONE);
        }
        if(typeOfDisabilityMore.equals(d4)) {
            displayTypeOfDisabilityMore.setText(typeOfDisabilityMore);
        }else {
            displayTypeOfDisabilityMore.setVisibility(View.GONE);
        }

    }

}