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
            String primarySkill1 = getIntent().getStringExtra("primarySkill1");
            String primarySkill2 = getIntent().getStringExtra("primarySkill2");
            String primarySkill3 = getIntent().getStringExtra("primarySkill3");
            String primarySkill4 = getIntent().getStringExtra("primarySkill4");
            String primarySkill5 = getIntent().getStringExtra("primarySkill5");
            String primarySkill6 = getIntent().getStringExtra("primarySkill6");
            String primarySkill7 = getIntent().getStringExtra("primarySkill7");
            String primarySkill8 = getIntent().getStringExtra("primarySkill8");
            String primarySkill9 = getIntent().getStringExtra("primarySkill9");
            String primarySkill10 = getIntent().getStringExtra("primarySkill10");
            String primarySkillOther = getIntent().getStringExtra("primarySkillOther");

            setImage(imageURL,postTitle, postDescription,postLocation, postCompanyCity
                    ,typeOfDisability1,typeOfDisability2,typeOfDisability3, typeOfDisabilityMore,
                    postDate, expDate, permission, companyName, jobSkill1, jobSkill2, jobSkill3, jobSkill4,
                    jobSkill5, jobSkill6, jobSkill7, jobSkill8, jobSkill9, jobSkill10, educationalAttainment, categorySkill,
                    workExperience, primarySkill1, primarySkill2, primarySkill3, primarySkill4, primarySkill5, primarySkill6,primarySkill7
                    ,primarySkill8, primarySkill9, primarySkill10, primarySkillOther);
        }
    }


    private void setImage(String imageURL, String postTitle, String postDescription,
                          String postLocation, String postCompanyCity, String typeOfDisability1,
                          String typeOfDisability2, String typeOfDisability3, String typeOfDisabilityMore,
                          String postDate, String expDate, String permission, String companyName,
                          String jobSkill1,String  jobSkill2, String jobSkill3,
                          String jobSkill4, String jobSkill5, String jobSkill6,String  jobSkill7, String jobSkill8,
                          String jobSkill9, String jobSkill10,String educationalAttainment, String categorySkill,
                          String workExperience,String primarySkill1,String primarySkill2, String primarySkill3, String primarySkill4, String primarySkill5,String primarySkill6,
                          String primarySkill7,String primarySkill8, String primarySkill9,String primarySkill10, String primarySkillOther){
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

//        TextView displayPostDate = findViewById(R.id.displayPostDate);
//        displayPostDate.setText(postDate);

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
        final String Skill1 = "Active Listening";
        final String Skill2 = "Communication";
        final String Skill3 = "Computer Skills";
        final String Skill4 =  "Customer Service";
        final String Skill5 =  "Interpersonal Skills";
        final String Skill6 =   "Leadership";
        final String Skill7 =  "Management Skills";
        final String Skill8 =   "Problem-Solving";
        final String Skill9 =  "Time Management";
        final String Skill10 = "Transferable Skills";

        TextView displayPrimarySkill1 = findViewById(R.id.displayPrimarySkill1);
        TextView displayPrimarySkill2 = findViewById(R.id.displayPrimarySkill2);
        TextView displayPrimarySkill3 = findViewById(R.id.displayPrimarySkill3);
        TextView displayPrimarySkill4 = findViewById(R.id.displayPrimarySkill4);
        TextView displayPrimarySkill5 = findViewById(R.id.displayPrimarySkill5);
        TextView displayPrimarySkill6 = findViewById(R.id.displayPrimarySkill6);
        TextView displayPrimarySkill7 = findViewById(R.id.displayPrimarySkill7);
        TextView displayPrimarySkill8 = findViewById(R.id.displayPrimarySkill8);
        TextView displayPrimarySkill9 = findViewById(R.id.displayPrimarySkill9);
        TextView displayPrimarySkill10 = findViewById(R.id.displayPrimarySkill10);
        TextView displayPrimarySkillOther = findViewById(R.id.displayPrimarySkillOther);


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


        if(jobSkill1.equals(Skill1)) {
            displaySkill1.setText(jobSkill1);
        }else{
            displaySkill1.setVisibility(View.GONE);
        }

        if(jobSkill2.equals(Skill2)) {
            displaySkill2.setText(jobSkill2);
        }else{
            displaySkill2.setVisibility(View.GONE);
        }

        if(jobSkill3.equals(Skill3)) {
            displaySkill3.setText(jobSkill3);
        }else{
            displaySkill3.setVisibility(View.GONE);
        }

        if(jobSkill4.equals(Skill4)) {
            displaySkill4.setText(jobSkill4);
        }else{
            displaySkill4.setVisibility(View.GONE);
        }

        if(jobSkill5.equals(Skill5)) {
            displaySkill5.setText(jobSkill5);
        }else{
            displaySkill5.setVisibility(View.GONE);
        }

        if(jobSkill6.equals(Skill6)) {
            displaySkill6.setText(jobSkill6);
        }else{
            displaySkill6.setVisibility(View.GONE);
        }

        if(jobSkill7.equals(Skill7)) {
            displaySkill7.setText(jobSkill7);
        }else{
            displaySkill7.setVisibility(View.GONE);
        }

        if(jobSkill8.equals(Skill8)) {
            displaySkill8.setText(jobSkill8);
        }else{
            displaySkill8.setVisibility(View.GONE);
        }

        if(jobSkill9.equals(Skill9)) {
            displaySkill9.setText(jobSkill9);
        }else{
            displaySkill9.setVisibility(View.GONE);
        }

        if(jobSkill10.equals(Skill10)) {
            displaySkill10.setText(jobSkill10);
        }else{
            displaySkill10.setVisibility(View.GONE);
        }

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


        if(primarySkill1 == null||primarySkill1.equals("")){
            displayPrimarySkill1.setVisibility(View.GONE);
        }else {
            displayPrimarySkill1.setText(primarySkill1);
        }

        if(primarySkill2 == null||primarySkill2.equals("")){
            displayPrimarySkill2.setVisibility(View.GONE);
        }else {
            displayPrimarySkill2.setText(primarySkill2);
        }

        if(primarySkill3 == null||primarySkill3.equals("")){
            displayPrimarySkill3.setVisibility(View.GONE);
        }else {
            displayPrimarySkill3.setText(primarySkill3);
        }

        if(primarySkill4 == null||primarySkill4.equals("")){
            displayPrimarySkill4.setVisibility(View.GONE);
        }else {
            displayPrimarySkill4.setText(primarySkill4);
        }

        if(primarySkill5 == null||primarySkill5.equals("")){
            displayPrimarySkill5.setVisibility(View.GONE);
        }else {
            displayPrimarySkill5.setText(primarySkill5);
        }

        if(primarySkill6 == null||primarySkill6.equals("")){
            displayPrimarySkill6.setVisibility(View.GONE);
        }else {
            displayPrimarySkill6.setText(primarySkill6);
        }

        if(primarySkill7 == null||primarySkill7.equals("")){
            displayPrimarySkill7.setVisibility(View.GONE);
        }else {
            displayPrimarySkill7.setText(primarySkill7);
        }

        if(primarySkill8 == null||primarySkill8.equals("")){
            displayPrimarySkill8.setVisibility(View.GONE);
        }else {
            displayPrimarySkill8.setText(primarySkill8);
        }

        if(primarySkill9 == null||primarySkill9.equals("")){
            displayPrimarySkill9.setVisibility(View.GONE);
        }else {
            displayPrimarySkill9.setText(primarySkill9);
        }

        if(primarySkill10 == null||primarySkill10.equals("")){
            displayPrimarySkill10.setVisibility(View.GONE);
        }else {
            displayPrimarySkill10.setText(primarySkill10);
        }

        if(primarySkillOther == null||primarySkillOther.equals("")){
            displayPrimarySkillOther.setVisibility(View.GONE);
        }else {
            displayPrimarySkillOther.setText(primarySkillOther);
        }
    }

}