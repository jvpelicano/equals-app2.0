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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.philcode.equals.R;
import java.util.HashMap;

public class PWD_AvailableJobs_View extends AppCompatActivity {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userId = user.getUid();
    public static String myPreferences = "MyPrefs";
    private static final String TAG = "PWD_AvailableJobs_View";
    Button sendResume;
    private ProgressDialog progressDialog;
    private static final int PICK_FILE = 1;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Name = "nameKey";
    SharedPreferences sharedpreferences;
    String name;

    private DatabaseReference databaseReference, checkResume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        final FirebaseAuth firebaseAuth;
        firebaseAuth = FirebaseAuth.getInstance();
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        String imageURL = getIntent().getStringExtra("imageURL");
        setContentView(R.layout.pwd_availablejobs_view);

        progressDialog = new ProgressDialog(this);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Job_Offers");
        ref.orderByChild("imageURL").equalTo(imageURL).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String parent = childSnapshot.getKey();
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Job_Offers").child(parent).child("Resumes").child(userId);
                    checkResume = FirebaseDatabase.getInstance().getReference().child("Job_Offers").child(parent).child("Resumes");
                    checkResume.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.hasChild(userId)) {
                                // run some code
                                sendResume.setText("Re-submit resume");
                            }
                            else{
                                sendResume.setText("Submit resume");
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
        getIncomingIntent();
        sendResume = findViewById(R.id.btnApply);


        sendResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadResume();
            }
        });
    }


    private void getIncomingIntent() {
        Log.d(TAG, "getIncomingIntent: checking for incoming intents.");

        if (getIntent().hasExtra("imageURL")
                && getIntent().hasExtra("postTitle")
                && getIntent().hasExtra("fullname")
                && getIntent().hasExtra("postDescription")
                || getIntent().hasExtra("postLocation")
                || getIntent().hasExtra("expDate")
                || getIntent().hasExtra("postDate")
                || getIntent().hasExtra("typeofDisability1")
                || getIntent().hasExtra("typeofDisability2")
                || getIntent().hasExtra("typeofDisability3")
                || getIntent().hasExtra("typeofDisabilityMore")) {
            Log.d(TAG, "getIncomingIntent: found intent extras.");
            String imageURL = getIntent().getStringExtra("imageURL");
            String postLocation = getIntent().getStringExtra("postLocation");
            String postCompanyCity = getIntent().getStringExtra("city");
            String postTitle = getIntent().getStringExtra("postTitle");
            String companyName = getIntent().getStringExtra("companyName");
            String typeOfDisability1 = getIntent().getStringExtra("typeOfDisability1");
            String typeOfDisability2 = getIntent().getStringExtra("typeOfDisability2");
            String typeOfDisability3 = getIntent().getStringExtra("typeOfDisability3");
            String typeofDisabilityMore = getIntent().getStringExtra("typeOfDisabilityMore");
            String postDescription = getIntent().getStringExtra("postDescription");
            String expDate = getIntent().getStringExtra("expDate");
            String postDate = getIntent().getStringExtra("postDate");
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

            setImage(imageURL, postTitle, companyName, postDescription,
                    postLocation, postCompanyCity, typeOfDisability1,
                    typeOfDisability2, typeOfDisability3, typeofDisabilityMore,
                    expDate, postDate, jobSkill1, jobSkill2, jobSkill3, jobSkill4,
                    jobSkill5, jobSkill6, jobSkill7, jobSkill8, jobSkill9, jobSkill10, educationalAttainment, categorySkill,
                    workExperience, primarySkill1, primarySkill2, primarySkill3, primarySkill4, primarySkill5, primarySkill6, primarySkill7
                    , primarySkill8, primarySkill9, primarySkill10, primarySkillOther);
        }

    }


    private void setImage(String imageURL, String postTitle, String companyName, String postDescription,
                          String postLocation, String postCompanyCity, String typeOfDisability1,
                          String typeOfDisability2, String typeOfDisability3, String typeofDisabilityMore,
                          String expDate, String postDate, String jobSkill1, String jobSkill2, String jobSkill3,
                          String jobSkill4, String jobSkill5, String jobSkill6, String jobSkill7, String jobSkill8,
                          String jobSkill9, String jobSkill10, String educationalAttainment, String categorySkill,
                          String workExperience, String primarySkill1, String primarySkill2, String primarySkill3, String primarySkill4, String primarySkill5, String primarySkill6,
                          String primarySkill7, String primarySkill8, String primarySkill9, String primarySkill10, String primarySkillOther) {
        Log.d(TAG, "setImage: setting te image and name to widgets.");
        TextView displayTypeOfDisability1 = findViewById(R.id.displayTypeOfDisability1);
        TextView displayTypeOfDisability2 = findViewById(R.id.displayTypeOfDisability2);
        TextView displayTypeOfDisability3 = findViewById(R.id.displayTypeOfDisability3);
        TextView displayTypeOfDisabilityMore = findViewById(R.id.displayTypeOfDisabilityMore);
        TextView displayPostLocation = findViewById(R.id.displayPostLocation);
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
        //added
        TextView displayEducationalAttainment = findViewById(R.id.displayEducationalAttainment);
        displayEducationalAttainment.setText(educationalAttainment);
        TextView displayCategorySkill = findViewById(R.id.displayCategorySkill);
        displayCategorySkill.setText(categorySkill);
        TextView displayTotalWorkExperience = findViewById(R.id.displayTotalWorkExperience);
        displayTotalWorkExperience.setText(workExperience);


        ImageView images = findViewById(R.id.displayPostPic);
        Glide.with(this)
                .asBitmap()
                .load(imageURL)
                .into(images);
        TextView displayPostTitle = findViewById(R.id.displayPostTitle);
        displayPostTitle.setText(postTitle);


        TextView displayCompanyName = findViewById(R.id.displayCompanyName);
        displayCompanyName.setText(companyName);


        TextView displayExpDate = findViewById(R.id.displayExpDate);
        displayExpDate.setText(expDate);

        TextView displayPostDate = findViewById(R.id.displayPostDate);
        //displayPostDate.setText(postDate);

        TextView displayPostDescription = findViewById(R.id.displayPostDescription);
        displayPostDescription.setText(postDescription);
        displayPostLocation.setText(String.format("%s, %s", postLocation, postCompanyCity));
        final String d1 = "Orthopedic Disability";
        final String d2 = "Partial Vision Disability";
        final String d3 = "Hearing Disability";
        final String d4 = "Other Disabilities";
        final String Skill1 = "Active Listening";
        final String Skill2 = "Communication";
        final String Skill3 = "Computer Skills";
        final String Skill4 = "Customer Service";
        final String Skill5 = "Interpersonal Skills";
        final String Skill6 = "Leadership";
        final String Skill7 = "Management Skills";
        final String Skill8 = "Problem-Solving";
        final String Skill9 = "Time Management";
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

        if (typeOfDisability1.equals(d1)) {
            displayTypeOfDisability1.setText(typeOfDisability1);
        } else {
            displayTypeOfDisability1.setVisibility(View.GONE);
        }
        if (typeOfDisability2.equals(d2)) {
            displayTypeOfDisability2.setText(typeOfDisability2);
        } else {
            displayTypeOfDisability2.setVisibility(View.GONE);
        }
        if (typeOfDisability3.equals(d3)) {
            displayTypeOfDisability3.setText(typeOfDisability3);
        } else {
            displayTypeOfDisability3.setVisibility(View.GONE);
        }
        if (typeofDisabilityMore.equals(d4)) {
            displayTypeOfDisabilityMore.setText(typeofDisabilityMore);
        } else {
            displayTypeOfDisabilityMore.setVisibility(View.GONE);
        }


        if (jobSkill1.equals(Skill1)) {
            displaySkill1.setText(jobSkill1);
        } else {
            displaySkill1.setVisibility(View.GONE);
        }

        if (jobSkill2.equals(Skill2)) {
            displaySkill2.setText(jobSkill2);
        } else {
            displaySkill2.setVisibility(View.GONE);
        }

        if (jobSkill3.equals(Skill3)) {
            displaySkill3.setText(jobSkill3);
        } else {
            displaySkill3.setVisibility(View.GONE);
        }

        if (jobSkill4.equals(Skill4)) {
            displaySkill4.setText(jobSkill4);
        } else {
            displaySkill4.setVisibility(View.GONE);
        }

        if (jobSkill5.equals(Skill5)) {
            displaySkill5.setText(jobSkill5);
        } else {
            displaySkill5.setVisibility(View.GONE);
        }

        if (jobSkill6.equals(Skill6)) {
            displaySkill6.setText(jobSkill6);
        } else {
            displaySkill6.setVisibility(View.GONE);
        }

        if (jobSkill7.equals(Skill7)) {
            displaySkill7.setText(jobSkill7);
        } else {
            displaySkill7.setVisibility(View.GONE);
        }

        if (jobSkill8.equals(Skill8)) {
            displaySkill8.setText(jobSkill8);
        } else {
            displaySkill8.setVisibility(View.GONE);
        }

        if (jobSkill9.equals(Skill9)) {
            displaySkill9.setText(jobSkill9);
        } else {
            displaySkill9.setVisibility(View.GONE);
        }

        if (jobSkill10.equals(Skill10)) {
            displaySkill10.setText(jobSkill10);
        } else {
            displaySkill10.setVisibility(View.GONE);
        }


        if (primarySkill1 == null || primarySkill1.equals("")) {
            displayPrimarySkill1.setVisibility(View.GONE);
        } else {
            displayPrimarySkill1.setText(primarySkill1);
        }

        if (primarySkill2 == null || primarySkill2.equals("")) {
            displayPrimarySkill2.setVisibility(View.GONE);
        } else {
            displayPrimarySkill2.setText(primarySkill2);
        }

        if (primarySkill3 == null || primarySkill3.equals("")) {
            displayPrimarySkill3.setVisibility(View.GONE);
        } else {
            displayPrimarySkill3.setText(primarySkill3);
        }

        if (primarySkill4 == null || primarySkill4.equals("")) {
            displayPrimarySkill4.setVisibility(View.GONE);
        } else {
            displayPrimarySkill4.setText(primarySkill4);
        }

        if (primarySkill5 == null || primarySkill5.equals("")) {
            displayPrimarySkill5.setVisibility(View.GONE);
        } else {
            displayPrimarySkill5.setText(primarySkill5);
        }

        if (primarySkill6 == null || primarySkill6.equals("")) {
            displayPrimarySkill6.setVisibility(View.GONE);
        } else {
            displayPrimarySkill6.setText(primarySkill6);
        }

        if (primarySkill7 == null || primarySkill7.equals("")) {
            displayPrimarySkill7.setVisibility(View.GONE);
        } else {
            displayPrimarySkill7.setText(primarySkill7);
        }

        if (primarySkill8 == null || primarySkill8.equals("")) {
            displayPrimarySkill8.setVisibility(View.GONE);
        } else {
            displayPrimarySkill8.setText(primarySkill8);
        }

        if (primarySkill9 == null || primarySkill9.equals("")) {
            displayPrimarySkill9.setVisibility(View.GONE);
        } else {
            displayPrimarySkill9.setText(primarySkill9);
        }

        if (primarySkill10 == null || primarySkill10.equals("")) {
            displayPrimarySkill10.setVisibility(View.GONE);
        } else {
            displayPrimarySkill10.setText(primarySkill10);
        }

        if (primarySkillOther == null || primarySkillOther.equals("")) {
            displayPrimarySkillOther.setVisibility(View.GONE);
        } else {
            displayPrimarySkillOther.setText(primarySkillOther);
        }

    }


    protected void uploadResume() {
        AlertDialog.Builder alert = new AlertDialog.Builder(PWD_AvailableJobs_View.this);
        alert.setMessage("Do you want to submit your resume? \nYou cannot undo this action").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                        final DatabaseReference typeStatus = FirebaseDatabase.getInstance().getReference("PWD").child(firebaseAuth.getUid());
                        typeStatus.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String fname = dataSnapshot.child("firstName").getValue().toString();
                                String lname = dataSnapshot.child("lastName").getValue().toString();
                                String email = dataSnapshot.child("email").getValue().toString();
                                String contact = dataSnapshot.child("contact").getValue().toString();
                                String resume = dataSnapshot.child("resumeFile").getValue().toString();
                                String userID = user.getUid();
                                //PWD_UserInformation currentProfile = dataSnapshot.child("typeStatus").getValue(PWD_UserInformation.class);
                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("resumeFile", resume);
                                hashMap.put("firstName", fname);
                                hashMap.put("lastName", lname);
                                hashMap.put("email", email);
                                hashMap.put("contact", contact);
                                hashMap.put("userID", userID);
                                databaseReference.setValue(hashMap);
                                Toast.makeText(getApplicationContext(), "Resume submitted successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), a_PWDContentMainActivity.class));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(PWD_AvailableJobs_View.this, databaseError.getCode(),
                                        Toast.LENGTH_LONG).show();
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
        alertDialog.setTitle("Send Resume");
        alertDialog.show();


    }
}

