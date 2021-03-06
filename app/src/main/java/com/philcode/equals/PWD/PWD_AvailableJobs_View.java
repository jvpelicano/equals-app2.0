package com.philcode.equals.PWD;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.ValueIterator;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.protobuf.StringValue;
import com.philcode.equals.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class PWD_AvailableJobs_View extends AppCompatActivity {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userId = user.getUid();
    public static final String Name = "nameKey";
    FirebaseDatabase fDb;
    DatabaseReference jobOffersRef, pwdRef;
    TextView m_displayCompanyName, m_displayPostDescription, m_displayPostLocation,
            m_displayCategorySkill, m_displayJobSkillsList, m_displayEducationalAttainment,
            m_displayTotalWorkExperience, m_displayTypeOfDisabilitiesList, m_displayTypeOfDisabilityOthers, m_displayExpDate, m_displayPermission,
            m_displayPostTitle;
    Button m_sendResume;
    ImageView m_displayPostPic, m_displayCompanyLogo;
    private String companyLogoURL;
    private ProgressDialog progressDialog;
    private static final int PICK_FILE = 1 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pwd_availablejobs_view);

        progressDialog = new ProgressDialog(this);

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
        m_sendResume = findViewById(R.id.btnApply);
        m_displayPostPic = findViewById(R.id.displayPostPic);
        m_displayCompanyLogo = findViewById(R.id.displayCompanyLogo);

        final String postJobID = getIntent().getStringExtra("POST_ID");
        fDb = FirebaseDatabase.getInstance();

        jobOffersRef = fDb.getReference().child("Job_Offers").child(postJobID);
        jobOffersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final String postTitle = snapshot.child("postTitle").getValue().toString();
                final String companyName = snapshot.child("companyName").getValue().toString();
                final String postDescription = snapshot.child("postDescription").getValue().toString();
                final String postLoc = snapshot.child("postLocation").getValue().toString();
                final String skillCategory = snapshot.child("skill").getValue().toString();
                final String educationalAttainment = snapshot.child("educationalAttainment").getValue().toString();
                final String workExperience = snapshot.child("yearsOfExperience").getValue().toString();
                final String expDate = snapshot.child("expDate").getValue().toString();
                final String imageURL = snapshot.child("imageURL").getValue().toString();
                if(snapshot.hasChild("empProfilePic")){
                    companyLogoURL = snapshot.child("empProfilePic").getValue().toString();
                }else{
                    companyLogoURL = null;
                }
                ArrayList<String> jobSkillList = new ArrayList<>();
                ArrayList<String> typeOfDisabilityList = new ArrayList<>();
                for(int counter = 1; counter <= 10; counter++){
                    if(snapshot.hasChild("jobSkill" + counter) && !snapshot.child("jobSkill" + counter).getValue().toString().equals("")){
                        jobSkillList.add(snapshot.child("jobSkill" + counter).getValue(String.class));
                    }
                }

                for(int counter_a = 1; counter_a <= 3; counter_a++){
                    if(snapshot.hasChild("typeOfDisability" + counter_a) && !snapshot.child("typeOfDisability" + counter_a).getValue().toString().equals("")){
                        typeOfDisabilityList.add(snapshot.child("typeOfDisability" + counter_a).getValue(String.class));
                    }

                }
                if(snapshot.hasChild("typeOfDisabilityMore")){
                    typeOfDisabilityList.add(snapshot.child("typeOfDisabilityMore").getValue(String.class));
                }else{
                    String typeOfDisabilityMore = "";
                }
                setUserInfo(jobSkillList, typeOfDisabilityList, postTitle, companyName, postDescription, postLoc, skillCategory, educationalAttainment, workExperience,
                        expDate, imageURL, companyLogoURL);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        jobOffersRef.child("Resume").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(userId)){
                    m_sendResume.setText("Send updated resume.");
                    m_sendResume.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog alertDialog = new AlertDialog.Builder(PWD_AvailableJobs_View.this).create();
                            alertDialog.setTitle("Update Resume?");
                            alertDialog.setMessage("If you haven't already sent your updated resume, you can click \"OK\" to re-send your updated resume.");
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            jobOffersRef.child("Resume").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    String oldResumeFile = snapshot.child("resumeFile").getValue(String.class);
                                                    reSendResume(oldResumeFile);
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        }
                                    });
                            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            alertDialog.show();
                        }
                    });
                }else{
                    checkResume();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void setUserInfo(ArrayList<String> jobSkillList, ArrayList<String> typeOfDisabilityList, String postTitle, String companyName,
                            String postDescription, String postLoc, String skillCategory, String educationalAttainment, String workExperience, String expDate,
                            String imageURL, String companyLogoURL){
        m_displayPostTitle.setText(postTitle);
        m_displayCompanyName.setText(companyName);
        m_displayPostDescription.setText(postDescription);
        m_displayPostLocation.setText(postLoc);
        m_displayCategorySkill.setText(skillCategory);
        m_displayEducationalAttainment.setText(educationalAttainment);
        m_displayTotalWorkExperience.setText(workExperience + " years");
        m_displayExpDate.setText(expDate);

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
        if(companyLogoURL == null){
            m_displayCompanyLogo.setVisibility(View.GONE);
        }else{
            Glide.with(getApplicationContext()).load(companyLogoURL).into(m_displayCompanyLogo);
        }


    }
    public void checkResume(){
        pwdRef = fDb.getReference().child("PWD").child(userId);
        pwdRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("resumeFile").exists() && !snapshot.child("resumeFile").getValue().toString().isEmpty()){
                    m_sendResume.setText("Send Resume");
                    m_sendResume.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sendResume();
                        }
                    });
                }else{
                    m_sendResume.setText("Upload Resume");
                    m_sendResume.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder alert =  new AlertDialog.Builder(PWD_AvailableJobs_View.this);
                            alert.setMessage("Resume file format should be in PDF.").setCancelable(true)
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                            intent.setType("docx/*");
                                            intent.setType("doc/*");
                                            intent.setType("application/pdf");
                                            startActivityForResult(intent, PICK_FILE);
                                        }
                                    });
                            AlertDialog alertDialog = alert.create();
                            alertDialog.setTitle("Resume Upload File Format");
                            alertDialog.show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void sendResume() {
        pwdRef = fDb.getReference().child("PWD").child(userId);
        pwdRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String fname = snapshot.child("firstName").getValue().toString();
                String lname = snapshot.child("lastName").getValue().toString();
                String email = snapshot.child("email").getValue().toString();
                String contact = snapshot.child("contact").getValue().toString();
                String resume = snapshot.child("resumeFile").getValue().toString();
                String userID = user.getUid();
                //PWD_UserInformation currentProfile = dataSnapshot.child("typeStatus").getValue(PWD_UserInformation.class);
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("resumeFile", resume);
                hashMap.put("firstName", fname);
                hashMap.put("lastName", lname);
                hashMap.put("email", email);
                hashMap.put("contact", contact);
                hashMap.put("userID", userID);
                jobOffersRef.child("Resume").child(userID).setValue(hashMap);
                Toast.makeText(getApplicationContext(), "Resume submitted successfully.", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(getApplicationContext(), a_PWDContentMainActivity.class));
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void reSendResume(String oldResumeFile){
        pwdRef = fDb.getReference().child("PWD").child(userId);
        pwdRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String fname = snapshot.child("firstName").getValue().toString();
                String lname = snapshot.child("lastName").getValue().toString();
                String email = snapshot.child("email").getValue().toString();
                String contact = snapshot.child("contact").getValue().toString();
                String resume = snapshot.child("resumeFile").getValue().toString();
                String userID = user.getUid();
                //PWD_UserInformation currentProfile = dataSnapshot.child("typeStatus").getValue(PWD_UserInformation.class);
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("oldResumeFile", oldResumeFile);
                hashMap.put("resumeFile", resume);
                hashMap.put("firstName", fname);
                hashMap.put("lastName", lname);
                hashMap.put("email", email);
                hashMap.put("contact", contact);
                hashMap.put("userID", userID);
                jobOffersRef.child("Resume").child(userID).setValue(hashMap);
                Toast.makeText(getApplicationContext(), "Resume submitted successfully.", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(getApplicationContext(), a_PWDContentMainActivity.class));
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_FILE){
            if(resultCode == RESULT_OK){
                Uri FileUri = data.getData();
                StorageReference Folder = FirebaseStorage.getInstance().getReference().child("Resumes").child(userId);
                final StorageReference file_name = Folder.child("file"+FileUri.getLastPathSegment());
                progressDialog.setTitle("Uploading...");
                progressDialog.show();
                file_name.putFile(FileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        file_name.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(final Uri uri) {
                                final DatabaseReference resume = FirebaseDatabase.getInstance().getReference("PWD").child(userId);
                                resume.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        resume.child("resumeFile").setValue(String.valueOf(uri));
                                        Toast.makeText(PWD_AvailableJobs_View.this, "Resume successfully uploaded.", Toast.LENGTH_LONG).show();
                                        progressDialog.dismiss();
                                        pwdRef = fDb.getReference().child("PWD").child(userId);
                                        pwdRef.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if(snapshot.hasChild("resumeFile")){
                                                    sendResume();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Toast.makeText(PWD_AvailableJobs_View.this, databaseError.getCode(),
                                                Toast.LENGTH_LONG).show();
                                    }
                                });

                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(PWD_AvailableJobs_View.this, "Invalid file type", Toast.LENGTH_SHORT).show();
                    }
                })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                        .getTotalByteCount());
                                progressDialog.setMessage("Uploaded " + (int) progress + "%");
                            }
                        });
            }

        }

    }
}

