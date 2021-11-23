package com.philcode.equals.PWD;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

        final String postJobID = getIntent().getStringExtra("POST_ID");
        //Toast.makeText(this, postJobID, Toast.LENGTH_SHORT).show();
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
                setUserInfo(jobSkillList, typeOfDisabilityList, postTitle, companyName, postDescription, postLoc, skillCategory, educationalAttainment, workExperience, postExpDate);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        checkResume();
        if(m_sendResume.getText().equals("Send Resume")){
            m_sendResume.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendResume();
                }
            });
        }else{
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
    }

    private void sendResume() {
        pwdRef = fDb.getReference().child("PWD").child(userId);
        pwdRef.addValueEventListener(new ValueEventListener() {
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
                Toast.makeText(getApplicationContext(), "Resume submitted successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), a_PWDContentMainActivity.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void setUserInfo(ArrayList<String> jobSkillList, ArrayList<String> typeOfDisabilityList, String postTitle, String companyName,
                            String postDescription, String postLoc, String skillCategory, String educationalAttainment, String workExperience, String postExpDate){
        m_displayPostTitle.setText(postTitle);
        m_displayCompanyName.setText(companyName);
        m_displayPostDescription.setText(postDescription);
        m_displayPostLocation.setText(postLoc);
        m_displayCategorySkill.setText(skillCategory);
        m_displayEducationalAttainment.setText(educationalAttainment);
        m_displayTotalWorkExperience.setText(workExperience);
        m_displayExpDate.setText(postExpDate);

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

    }
    public void checkResume(){
        pwdRef = fDb.getReference().child("PWD").child(userId);
        pwdRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("resumeFile").exists() && !snapshot.child("resumeFile").getValue().toString().isEmpty()){
                    m_sendResume.setText("Send Resume");
                }else{
                    m_sendResume.setText("Upload Resume");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final Intent i = new Intent(PWD_AvailableJobs_View.this, a_PWDContentMainActivity.class);
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
                                        startActivity(i);
                                        progressDialog.dismiss();
                                        resume.child("resumeFile").setValue(String.valueOf(uri));
                                        startActivity(new Intent(getApplicationContext(), a_PWDContentMainActivity.class));
                                        Toast.makeText(PWD_AvailableJobs_View.this, "Resume Uploaded", Toast.LENGTH_SHORT).show();

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

