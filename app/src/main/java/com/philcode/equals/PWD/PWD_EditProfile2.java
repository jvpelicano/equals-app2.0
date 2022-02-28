package com.philcode.equals.PWD;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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
import com.philcode.equals.EMP.LoginActivity_emp;
import com.philcode.equals.EMP.RegisterActivity_emp;
import com.philcode.equals.EMP.a_EmployeeContentMainActivity;
import com.philcode.equals.R;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class PWD_EditProfile2 extends AppCompatActivity{
    //firebase auth object
    private FirebaseAuth firebaseAuth;
    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference mDatabase;

    final Calendar myCalendar = Calendar.getInstance();

    String Storage_Path = "PWD_Reg_Form/";

    //view objects
    private TextView textViewUserEmail, skillSelected;
    private Button buttonAddWork, buttonSave;
    private Spinner primary_skillsCategory;
    private RadioGroup rgWorkExperience, rgEducAttainment;
    private RadioButton rbEduc, rbWorkExperience, rbWithExperience, rbWithoutExperience;
    private RecyclerView work_recyclerView;
    private String work_UUID;

    int wCount = 0;

    private List<PWD_AddWorkInformation> work_list;
    private PWD_WorkExperienceAdapter work_adapter;

    String check_Ortho = "";
    String check_Vis = "";
    String check_Hear = "";
    String check_More = "";

    String job_1 = "";
    String job_2 = "";
    String job_3 = "";
    String job_4 = "";
    String job_5 = "";
    String job_6 = "";
    String job_7 = "";
    String job_8 = "";
    String job_9 = "";
    String job_10 = "";

    private String educAttainment = "";
    private String workExperience = "";


    private CheckBox checkOrtho, checkHear, checkVis, checkMore;
    private CheckBox job1, job2, job3, job4, job5, job6, job7, job8, job9, job10;
    int PICK_IMAGE_REQUEST = 7;
    private Uri filePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.pwd_editprofile2);
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("PWD");

        firebaseAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String userz = user.getUid();
        rbWithoutExperience = findViewById(R.id.radio_10);
        rbWithExperience = findViewById(R.id.radio_11);

        firebaseAuth = FirebaseAuth.getInstance();

        final DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference().child("Category/");
        categoryRef.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {

                //  final List<String> skill = new ArrayList<String>();
                final List<String> category = new ArrayList<String>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Map<String, Object> data = (Map<String, Object>) snapshot.getValue();
                    category.add(data.get("skill").toString());
                }
                ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(PWD_EditProfile2.this, android.R.layout.simple_spinner_item, category) {
                    @Override
                    public View getDropDownView(int position, View convertView,
                                                ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);
                        TextView tv = (TextView) view;
                        if (position == 0) {
                            // Hide the second item from Spinner
                            tv.setVisibility(View.GONE);
                        } else {
                            tv.setVisibility(View.VISIBLE);
                        }
                        return view;
                    }
                };
                categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                primary_skillsCategory.setAdapter(categoryAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        buttonSave = (Button) findViewById(R.id.buttonSave2);


        primary_skillsCategory = findViewById(R.id.skillSpinner);
        skillSelected = findViewById(R.id.selectedSkills);
        rgEducAttainment = findViewById(R.id.rg_educ);
        rgWorkExperience = findViewById(R.id.workexperience);
        rgWorkExperience = findViewById(R.id.workexperience);
        final int selectExperience = rgWorkExperience.getCheckedRadioButtonId();
        rbWorkExperience = findViewById(selectExperience);
        rgWorkExperience.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (rbWithExperience.isChecked()) {
                    workExperience="With Experience";
                }
                if (rbWithoutExperience.isChecked()) {
                    workExperience="Without Experience";
                }
            }
        });
        if (skillSelected == null) {
            skillSelected.setVisibility(View.GONE);
        }

        checkOrtho = findViewById(R.id.checkOrtho);
        checkVis = findViewById(R.id.checkVis);
        checkHear = findViewById(R.id.checkHear);
        checkMore = findViewById(R.id.checkMore);

        //Skills
        job1 = findViewById(R.id.typeOfSkills1);
        job2 = findViewById(R.id.typeOfSkills2);
        job3 = findViewById(R.id.typeOfSkills3);
        job4 = findViewById(R.id.typeOfSkills4);
        job5 = findViewById(R.id.typeOfSkills5);
        job6 = findViewById(R.id.typeOfSkills6);
        job7 = findViewById(R.id.typeOfSkills7);
        job8 = findViewById(R.id.typeOfSkills8);
        job9 = findViewById(R.id.typeOfSkills9);
        job10 = findViewById(R.id.typeOfSkills10);


        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PWD_EditProfile2.this);
                builder.setMessage("Warning! This action will re-write all of your data that has already been saved.");
                builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeCurrentData();
                        login();
                        uploadImage();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.setTitle("Do you want to continue?");
                dialog.show();
                dialog.setCancelable(false);

            }
        });


    }

    private void removeCurrentData() {
        FirebaseUser currentFirebaseUser = firebaseAuth.getInstance().getCurrentUser();
        String uid = currentFirebaseUser.getUid();
        mDatabase.child(uid).child("jobSkills0").removeValue();
        mDatabase.child(uid).child("jobSkills1").removeValue();
        mDatabase.child(uid).child("jobSkills2").removeValue();
        mDatabase.child(uid).child("jobSkills3").removeValue();
        mDatabase.child(uid).child("jobSkills4").removeValue();
        mDatabase.child(uid).child("jobSkills5").removeValue();
        mDatabase.child(uid).child("jobSkills6").removeValue();
        mDatabase.child(uid).child("jobSkills7").removeValue();
        mDatabase.child(uid).child("jobSkills8").removeValue();
        mDatabase.child(uid).child("jobSkills9").removeValue();

        mDatabase.child(uid).child("typeOfDisability0").removeValue();
        mDatabase.child(uid).child("typeOfDisability1").removeValue();
        mDatabase.child(uid).child("typeOfDisability2").removeValue();
        mDatabase.child(uid).child("typeOfDisability3").removeValue();

        mDatabase.child(uid).child("skill").removeValue();
        mDatabase.child(uid).child("workExperience").removeValue();
        mDatabase.child(uid).child("educationalAttainment").removeValue();
    }

    private void login() {
        if (checkOrtho.isChecked()) {
            check_Ortho = checkOrtho.getText().toString();
        }
        else{
            check_Ortho = "";
        }

        if (checkVis.isChecked() ) {
            check_Vis = checkVis.getText().toString();
        }
        else{
            check_Vis = "";
        }

        if (checkHear.isChecked() ) {
            check_Hear = checkHear.getText().toString();
        }
        else{
            check_Hear = "";
        }

        if (checkMore.isChecked() ) {
            check_More = checkMore.getText().toString();
        }
        else{
            check_More = "";
        }



        if (job1.isChecked()) {
            job_1 = job1.getText().toString();
        } else {
            job_1 = "";
        }
        if (job2.isChecked()) {
            job_2 = job2.getText().toString();
        } else {
            job_2 = "";
        }
        if (job3.isChecked()) {
            job_3 = job3.getText().toString();
        } else {
            job_3 = "";
        }
        if (job4.isChecked()) {
            job_4 = job4.getText().toString();
        } else {
            job_4 = "";
        }
        if (job5.isChecked()) {
            job_5 = job5.getText().toString();
        } else {
            job_5 = "";
        }
        if (job6.isChecked()) {
            job_6 = job6.getText().toString();
        } else {
            job_6 = "";
        }
        if (job7.isChecked()) {
            job_7 = job7.getText().toString();
        } else {
            job_7 = "";
        }
        if (job8.isChecked()) {
            job_8 = job8.getText().toString();
        } else {
            job_8 = "";
        }
        if (job9.isChecked()) {
            job_9 = job9.getText().toString();
        } else {
            job_9 = "";
        }
        if (job10.isChecked()) {
            job_10 = job10.getText().toString();
        } else {
            job_10 = "";
        }
    }
    private void uploadImage() {
        final Intent intent = new Intent(this, PWD_EditProfile_ViewActivity.class);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.show();
        final String job1 = job_1;
        final String job2 = job_2;
        final String job3 = job_3;
        final String job4 = job_4;
        final String job5 = job_5;
        final String job6 = job_6;
        final String job7 = job_7;
        final String job8 = job_8;
        final String job9 = job_9;
        final String job10 = job_10;

        final String categorySkill = primary_skillsCategory.getSelectedItem().toString();
        final String checkOrtho = check_Ortho;
        final String checkVis = check_Vis;
        final String checkHear = check_Hear;
        final String checkMore = check_More;

        progressDialog.dismiss();

        rgEducAttainment = findViewById(R.id.rg_educ);
        final int selectedId = rgEducAttainment.getCheckedRadioButtonId();
        rbEduc = findViewById(selectedId);
        educAttainment = rbEduc.getText().toString();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();

        CheckBox ortho = findViewById(R.id.checkOrtho);
        CheckBox visual = findViewById(R.id.checkVis);
        CheckBox hear = findViewById(R.id.checkHear);
        CheckBox more = findViewById(R.id.checkMore);

        primary_skillsCategory = findViewById(R.id.skillSpinner);

        TextView textview8 = findViewById(R.id.textView8);
        TextView textview7 = findViewById(R.id.textView7);

        if(!ortho.isChecked() && !visual.isChecked() && !hear.isChecked() && !more.isChecked()){
            textview7.setError("Please check your disability");
            textview7.requestFocus();
            Toast.makeText(PWD_EditProfile2.this, "Please check your disability", Toast.LENGTH_SHORT).show();
            return;
        }else{
            if(categorySkill.equals("Click to select value")){
                textview8.setError("Please select your skill category");
                textview8.requestFocus();
                Toast.makeText(PWD_EditProfile2.this, "Choose your skill category", Toast.LENGTH_SHORT).show();
                return;
            }else{
                mDatabase = FirebaseDatabase.getInstance().getReference().child("PWD").child(userId);
                mDatabase.child("educationalAttainment").setValue(educAttainment);
                mDatabase.child("skill").setValue(primary_skillsCategory.getSelectedItem().toString()); //working
                mDatabase.child("workExperience").setValue(workExperience);


                ArrayList jobskills = new ArrayList();
                jobskills.add(job1);
                jobskills.add(job2);
                jobskills.add(job3);
                jobskills.add(job4);
                jobskills.add(job5);
                jobskills.add(job6);
                jobskills.add(job7);
                jobskills.add(job8);
                jobskills.add(job9);
                jobskills.add(job10);

                ArrayList disabilities = new ArrayList();
                disabilities.add(checkOrtho);
                disabilities.add(checkVis);
                disabilities.add(checkHear);
                disabilities.add(checkMore);


                for(int i = 0; i < jobskills.size(); i++){
                    if((jobskills.get(i) != "")){
                        mDatabase.child("jobSkills" + i).setValue(jobskills.get(i));
                    }
                }
                for(int i = 0; i < disabilities.size(); i++){
                    if((disabilities.get(i) != "")){
                        mDatabase.child("typeOfDisability" + i).setValue(disabilities.get(i));
                    }
                }
                if(workExperience.equals("")){
                    mDatabase.child("workExperience").setValue("Without Experience");
                }else{
                    mDatabase.child("workExperience").setValue(workExperience);
                }
                Toast.makeText(PWD_EditProfile2.this, "Changes successfully saved.", Toast.LENGTH_SHORT).show();
            }
        }

        startActivity(intent);

    }





}