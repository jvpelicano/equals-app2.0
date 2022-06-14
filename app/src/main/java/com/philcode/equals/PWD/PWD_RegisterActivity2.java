package com.philcode.equals.PWD;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.philcode.equals.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class PWD_RegisterActivity2 extends AppCompatActivity{

    //firebase auth object
    private FirebaseAuth firebaseAuth;
    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference mDatabase, categories_root;
    private String work_UUID;

    final Calendar myCalendar = Calendar.getInstance();

    String Storage_Path = "PWD_Reg_Form/";

    //view objects
    private TextView textViewUserEmail, skillSelected, txt_degree;
    private Button buttonAddWork, buttonSave;
    private Spinner primary_skillsCategory;
    private RadioGroup rgWorkExperience, rgEducAttainment;
    private RadioButton rbEduc, rbWorkExperience, rbWithExperience, rbWithoutExperience;
    private RecyclerView work_recyclerView;

    int wCount = 0;

    private List<PWD_AddWorkInformation> work_list;
    private PWD_WorkExperienceAdapter work_adapter;

    String check_Ortho = "";
    String check_Vis = "";
    String check_Hear = "";
    String check_More = "";
    String check_Speech = "";

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
    String job_11 = "";
    String job_12 = "";
    String job_13 = "";
    String job_14 = "";



    private String educAttainment = "";
    private String workExperience = "";


    private CheckBox checkOrtho, checkHear, checkVis, checkMore, checkSpeech;
    private CheckBox job1, job2, job3, job4, job5, job6, job7, job8, job9, job10, job11, job12, job13, job14;
    private RadioButton radio_1, radio_2, radio_3, radio_4, radio_5, radio_6;
    int PICK_IMAGE_REQUEST = 7;
    private Uri filePath;
    private TextInputLayout textInputLayout_otherDisabilitySpecific, textInputLayout_jobTitle, textInputLayout_degree;
    private TextInputEditText textInputEditText_otherDisabilitySpecific;
    //skill category
    private ArrayAdapter<String> exposedDropdownList_skillCategory_adapter, ExposedDropdownList_jobtitle_adapter;
    private ArrayList arrayList_skillCategory, arrayList_jobtitle;
    private AutoCompleteTextView autoComplete_degree, autoComplete_jobTitle;
    private HashMap hashMap_jobSkills;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.pwd_activity_register2);
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("PWD");
        categories_root = FirebaseDatabase.getInstance().getReference().child("Category");

        firebaseAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String userz = user.getUid();
        rbWithoutExperience = findViewById(R.id.radio_10);
        rbWithExperience = findViewById(R.id.radio_11);

        hashMap_jobSkills = new HashMap();

        textInputEditText_otherDisabilitySpecific = findViewById(R.id.textInputEditText_otherDisabilitySpecific);
        textInputLayout_otherDisabilitySpecific = findViewById(R.id.textInputLayout_otherDisabilitySpecific);
        textInputLayout_jobTitle = findViewById(R.id.textInputLayout_jobTitle);

        textInputLayout_degree = findViewById(R.id.textInputLayout_degree);
        txt_degree = findViewById(R.id.textView8);

        radio_1 = findViewById(R.id.radio_1);
        radio_2 = findViewById(R.id.radio_2);
        radio_3 = findViewById(R.id.radio_3);
        radio_4 = findViewById(R.id.radio_4);
        radio_5 = findViewById(R.id.radio_5);
        radio_6 = findViewById(R.id.radio_6);


        buttonSave = (Button) findViewById(R.id.buttonSave2);

        arrayList_skillCategory = new ArrayList();
        arrayList_jobtitle = new ArrayList();

        autoComplete_degree = findViewById(R.id.autoComplete_skillCategory);
        autoComplete_jobTitle = findViewById(R.id.autoComplete_jobTitle);

        exposedDropdownList_skillCategory_adapter =  new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arrayList_skillCategory);
        ExposedDropdownList_jobtitle_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arrayList_jobtitle);
        autoComplete_degree.setAdapter(exposedDropdownList_skillCategory_adapter);
        autoComplete_jobTitle.setAdapter(ExposedDropdownList_jobtitle_adapter);

        // = findViewById(R.id.skillSpinner);
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
        /*if (skillSelected == null) {
            skillSelected.setVisibility(View.GONE);
        }*/

        checkOrtho = findViewById(R.id.checkOrtho);
        checkVis = findViewById(R.id.checkVis);
        checkHear = findViewById(R.id.checkHear);
        checkMore = findViewById(R.id.checkMore);
        checkSpeech = findViewById(R.id.checkSpeech);

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
        job11 = findViewById(R.id.typeOfSkills11);
        job12 = findViewById(R.id.typeOfSkills12);
        job13 = findViewById(R.id.typeOfSkills13);
        job14 = findViewById(R.id.typeOfSkills14);
        setExposedDropdownListJobTitle();

        radio_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    txt_degree.setVisibility(View.GONE);
                    textInputLayout_degree.setVisibility(View.GONE);
                    autoComplete_degree.setText("");
                }
            }
        });
        radio_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    txt_degree.setVisibility(View.GONE);
                    textInputLayout_degree.setVisibility(View.GONE);
                    autoComplete_degree.setText("");
                }
            }
        });
        radio_3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    txt_degree.setVisibility(View.GONE);
                    textInputLayout_degree.setVisibility(View.GONE);
                    autoComplete_degree.setText("");
                }
            }
        });
        radio_4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    txt_degree.setVisibility(View.VISIBLE);
                    textInputLayout_degree.setVisibility(View.VISIBLE);
                }
            }
        });
        radio_5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    txt_degree.setVisibility(View.VISIBLE);
                    textInputLayout_degree.setVisibility(View.VISIBLE);
                }
            }
        });
        radio_6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    txt_degree.setVisibility(View.VISIBLE);
                    textInputLayout_degree.setVisibility(View.VISIBLE);
                }
            }
        });

        autoComplete_jobTitle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                arrayList_skillCategory.clear();
                setExposedDropdownListSkillCategory();
            }
        });

        checkMore.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    textInputLayout_otherDisabilitySpecific.setVisibility(View.VISIBLE);
                }else{
                    textInputLayout_otherDisabilitySpecific.setVisibility(View.GONE);
                }
            }
        });



        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkMore.isChecked() && textInputEditText_otherDisabilitySpecific.getText().toString().isEmpty()){
                    Toast.makeText(PWD_RegisterActivity2.this, "Please specify other disability.", Toast.LENGTH_SHORT).show();
                }else{
                    login();
                    uploadImage();
                }
            }
        });

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

        if (checkSpeech.isChecked() ) {
            check_Speech = checkSpeech.getText().toString();
        }
        else{
            check_Speech = "";
        }

        if (checkMore.isChecked() ) {
            check_More = textInputEditText_otherDisabilitySpecific.getText().toString();
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
        if(job11.isChecked()){
            job_11 = job11.getText().toString();
        }else{
            job_11 = "";
        }
        if(job12.isChecked()){
            job_12 = job12.getText().toString();
        }else{
            job_11 = "";
        }
        if(job13.isChecked()){
            job_13 = job13.getText().toString();
        }else{
            job_13 = "";
        }
        if(job14.isChecked()){
            job_14 = job14.getText().toString();
        }else{
            job_14 = "";
        }
    }

    //deleted callDialog function --------------------------------------------------------------


    private void uploadImage() {
        final Intent intent = new Intent(this, PWD_RegisterActivity3.class);
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
        final String job11 = job_11;
        final String job12 = job_12;
        final String job13 = job_13;
        final String job14 = job_14;

        //final String categorySkill = primary_skillsCategory.getSelectedItem().toString();
        final String checkOrtho = check_Ortho;
        final String checkVis = check_Vis;
        final String checkHear = check_Hear;
        final String checkMore = check_More;
        final String checkSpeech = check_Speech;

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

        //primary_skillsCategory = findViewById(R.id.skillSpinner);

        TextView textview8 = findViewById(R.id.textView8);
        TextView textview7 = findViewById(R.id.textView7);

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
        jobskills.add(job11);
        jobskills.add(job12);
        jobskills.add(job13);
        jobskills.add(job14);

        if(!ortho.isChecked() && !visual.isChecked() && !hear.isChecked() && !more.isChecked()){
            textview7.setError("Please check your disability");
            textview7.requestFocus();
            Toast.makeText(PWD_RegisterActivity2.this, "Please check your disability", Toast.LENGTH_SHORT).show();
            return;
        }else {

                ArrayList<String> checkedJobSkills = new ArrayList<String>();
                for (int i = 0; i < jobskills.size(); i++) {
                    if ((jobskills.get(i) != "")) {
                        checkedJobSkills.add(jobskills.get(i).toString());
                    }
                }

                if(!checkedJobSkills.isEmpty() && !autoComplete_degree.getText().toString().isEmpty()
                        && (((radio_4.isChecked() || radio_5.isChecked() || radio_6.isChecked()) && !autoComplete_degree.getText().toString().isEmpty())
                ) || (radio_1.isChecked() || radio_2.isChecked() || radio_3.isChecked() && autoComplete_degree.getText().toString().isEmpty())){

                    mDatabase = FirebaseDatabase.getInstance().getReference().child("PWD").child(userId);
                    mDatabase.child("educationalAttainment").setValue(educAttainment);
                    mDatabase.child("skill").setValue(autoComplete_degree.getText().toString()); //working
                    mDatabase.child("workExperience").setValue(workExperience);

                    ArrayList disabilities = new ArrayList();
                    disabilities.add(checkOrtho);
                    disabilities.add(checkVis);
                    disabilities.add(checkHear);
                    disabilities.add(checkSpeech);
                    disabilities.add(checkMore);


                    for(int i = 0; i < checkedJobSkills.size(); i++){
                        mDatabase.child("jobSkills" + i).setValue(checkedJobSkills.get(i));
                    }

                    for (int i = 0; i < disabilities.size(); i++) {
                        if ((disabilities.get(i) != "")) {
                            mDatabase.child("typeOfDisability" + i).setValue(disabilities.get(i));
                        }
                    }
                    if (workExperience.equals("")) {
                        mDatabase.child("workExperience").setValue("Without Experience");
                    } else {
                        mDatabase.child("workExperience").setValue(workExperience);
                    }

                    startActivity(intent);
                }else{
                    Toast.makeText(PWD_RegisterActivity2.this, "Please select job skills.", Toast.LENGTH_SHORT).show();
                }


            }

        }

    //set data
    private void setExposedDropdownListSkillCategory(){
        String chosenJobTitle = autoComplete_jobTitle.getText().toString();
        categories_root.orderByChild("jobtitle").equalTo(chosenJobTitle).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap_category_key : snapshot.getChildren()){
                    String parent = snap_category_key.getKey();

                    categories_root.child(parent).child("specialization").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot snap_jobTitles : snapshot.getChildren()){
                                arrayList_skillCategory.add(snap_jobTitles.getValue().toString());
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
    private void setExposedDropdownListJobTitle(){
        categories_root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap_skillCategory : snapshot.getChildren()){
                    arrayList_jobtitle.add(snap_skillCategory.child("jobtitle").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            final Intent intent = new Intent(this, PWD_RegisterActivity.class);
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            androidx.appcompat.app.AlertDialog.Builder alert =  new androidx.appcompat.app.AlertDialog.Builder(PWD_RegisterActivity2.this);
            alert.setMessage("By clicking Yes, you will return to the first step deleting your account.").setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        /*    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("PWD");
                            dbRef.child(String.valueOf(user)).removeValue();*/
                            user.delete();
                            startActivity(intent);
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            androidx.appcompat.app.AlertDialog alertDialog = alert.create();
            alertDialog.setTitle("Return to first step?");
            alertDialog.show();
        }
        return super.onKeyDown(keyCode, event);
    }
}