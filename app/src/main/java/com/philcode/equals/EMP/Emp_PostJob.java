package com.philcode.equals.EMP;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Emp_PostJob extends AppCompatActivity {
    //firebase connection
    private DatabaseReference job_offers_root, categories_root, emp_user_root;
    private StorageReference job_offers_storage_root;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    //string
    private String uID;
    private String pushKey;
    private String calculated_postExpDate;
    //calendar
    private Calendar cal;
    private SimpleDateFormat format;
    //layout
        //adapters
        private ArrayAdapter<String> exposedDropdownList_skillCategory_adapter;
        private ArrayAdapter<String> exposedDropdownList_jobtitles_adapter;
        private ArrayAdapter<String> exposedDropdownList_typeOfEmployment_adapter;
        //exposed dropdown list text input layout
        private TextInputLayout textInputLayout_skillCategory, textInputLayout_jobTitle, textInputLayout_typeOfEmployment;
        //text input layout
        private TextInputLayout textInputLayout_otherDisabilitySpecific, textInputLayout_yearsOfExperience;
        //exposed dropdown list autocomplete text view
        private AutoCompleteTextView autoComplete_skillCategory, autoComplete_jobTitle, autoComplete_typeOfEmployment;
        //text views
        private TextView txt_jobTitle;
        private TextView txt_jobTitleError;
        //edit texts
        private TextInputEditText textInputEditText_otherDisabilitySpecific, textInputEditText_postDescription;
        //check box
        private CheckBox checkBox_typeOfDisability_Other, checkBox_educAttainmentRequirement;
        //buttons
        private Button btn_saveJobPost;
        //radio button
        private RadioButton radioButton_withWorkExp;
        //radio group
        private RadioGroup radioGroup_educ;
    //arrays
        //exposed dropdown list arrays
        private ArrayList <String> arrayList_skillCategory;
        private ArrayList <String> arrayList_jobTitles;
        private ArrayList <String> arrayList_typeOfEmployment;
        //check boxes list
        private CheckBox[] checkBoxes_type_of_disabilities;
        private CheckBox[] checkBoxes_secondary_skills;
        //ID array list
        private Integer[] checkboxIDs_type_of_disabilities; //initialized on methods
        private Integer[] checkboxIDs_secondary_skills;
    //hashMaps
        private HashMap<String, String> hashMap_disability, hashMap_secondary_skills, hashMap_generalData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emp_postjob);
        //initialize firebase connection
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
            //references
            categories_root = firebaseDatabase.getReference("Category"); //reference for spinners
            job_offers_root = firebaseDatabase.getReference("Job_Offers"); //where data will be stored
            emp_user_root = firebaseDatabase.getReference("Employers"); // get some data
            job_offers_storage_root = firebaseStorage.getReference("Posts/"); //where image will be stored
            //user ID
            uID = firebaseUser.getUid();

        //initialize
            //date
            Date currentDate = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("MMMM dd, yyyy");
            //arrays
                //exposed dropdown list arrays
                arrayList_skillCategory = new ArrayList<>();
                arrayList_jobTitles = new ArrayList<>();
                arrayList_typeOfEmployment = new ArrayList<>();
            //hashMaps
                hashMap_disability = new HashMap<>();
                hashMap_secondary_skills = new HashMap<>();
                hashMap_generalData = new HashMap<>();
            //layout
                //adapters
                exposedDropdownList_skillCategory_adapter =  new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arrayList_skillCategory);
                exposedDropdownList_jobtitles_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arrayList_jobTitles);
                exposedDropdownList_typeOfEmployment_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,arrayList_typeOfEmployment);
                //autocomplete text view
                autoComplete_skillCategory = findViewById(R.id.autoComplete_skillCategory);
                autoComplete_jobTitle = findViewById(R.id.autoComplete_jobTitle);
                autoComplete_typeOfEmployment = findViewById(R.id.autoComplete_typeOfEmployment);
                //button
                btn_saveJobPost = findViewById(R.id.btn_saveJobPost);
                //check box
                checkBox_typeOfDisability_Other = findViewById(R.id.typeOfDisabilityOther);
                checkBox_educAttainmentRequirement = findViewById(R.id.checkBox_educAttainmentRequirement);
                //edit texts
                textInputEditText_postDescription = findViewById(R.id.textInputEditText_postDescription);
                //radio button
                radioButton_withWorkExp = findViewById(R.id.radio_11);
                //radio group
                radioGroup_educ = findViewById(R.id.radioGroup_educ);
                //text input layouts
                textInputLayout_jobTitle = findViewById(R.id.textInputLayout_jobTitle); //autocomplete
                textInputLayout_skillCategory = findViewById(R.id.textInputLayout_skillCategory);//autocomplete
                textInputLayout_typeOfEmployment = findViewById(R.id.textInputLayout_typeOfEmployment);//autocomplete
                textInputLayout_otherDisabilitySpecific  = findViewById(R.id.textInputLayout_otherDisabilitySpecific);
                textInputLayout_yearsOfExperience = findViewById(R.id.textInputLayout_yearsOfExperience);
                //text views
                txt_jobTitle = findViewById(R.id.txt_jobTitle);
                txt_jobTitleError = findViewById(R.id.txt_jobTitleError);

        //set exposed dropdown list
        setExposedDropdownListSkillCategory();
        setExposedDropdownListTypeofEmployment();

        //set adapters
            //skill category
            autoComplete_skillCategory.setAdapter(exposedDropdownList_skillCategory_adapter);
            //job titles
            autoComplete_jobTitle.setAdapter(exposedDropdownList_jobtitles_adapter);
            autoComplete_jobTitle.setThreshold(1);
            //type of employment
            autoComplete_typeOfEmployment.setAdapter(exposedDropdownList_typeOfEmployment_adapter);
        //listeners
            autoComplete_skillCategory.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(!hasFocus){
                        //Toast.makeText(Emp_PostJob.this, checkExposedDropdownListValue(arrayList_spinner_jobTitles, autoComplete_jobTitle.getText().toString()).toString(), Toast.LENGTH_LONG).show();
                        Boolean exists = checkExposedDropdownListValue(arrayList_skillCategory, autoComplete_skillCategory.getText().toString());
                        if(!exists){
                            txt_jobTitleError.setVisibility(View.VISIBLE);
                        }else{
                            txt_jobTitleError.setVisibility(View.GONE);
                        }
                    }
                }
            });
            autoComplete_skillCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    txt_jobTitle.setVisibility(View.VISIBLE);
                    textInputLayout_jobTitle.setVisibility(View.VISIBLE);
                    arrayList_jobTitles.clear();
                    setExposedDropdownListJobTitle();
                    autoComplete_jobTitle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        }
                    });
                }
            });
            autoComplete_jobTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() { //checks if the value entered on Job Title is in the system.
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(!hasFocus){
                        //Toast.makeText(Emp_PostJob.this, checkExposedDropdownListValue(arrayList_spinner_jobtitles, autoComplete_jobTitle.getText().toString()).toString(), Toast.LENGTH_LONG).show();
                        Boolean exists = checkExposedDropdownListValue(arrayList_jobTitles, autoComplete_jobTitle.getText().toString());
                        if(!exists){
                            txt_jobTitleError.setVisibility(View.VISIBLE);
                        }else{
                            txt_jobTitleError.setVisibility(View.GONE);
                        }
                    }
                }
            });

            //check if type of disability Others is ticked
            checkBox_typeOfDisability_Other.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        textInputLayout_otherDisabilitySpecific.setVisibility(View.VISIBLE);
                        hashMap_generalData.put("specificTypeOfDisability", textInputEditText_otherDisabilitySpecific.getText().toString());
                    }else{
                        textInputLayout_otherDisabilitySpecific.setVisibility(View.GONE);
                    }
                }
            });
            //check if educational attainment is ticked
            checkBox_educAttainmentRequirement.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        radioGroup_educ.setVisibility(View.VISIBLE);
                        hashMap_generalData.put("educationalAttainmentRequirement", "true");
                    }else{
                        radioGroup_educ.setVisibility(View.GONE);
                        hashMap_generalData.put("educationalAttainmentRequirement", "false");
                    }
                }
            });
            radioButton_withWorkExp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        textInputLayout_yearsOfExperience.setVisibility(View.VISIBLE);
                    }else{
                        textInputLayout_yearsOfExperience.setVisibility(View.GONE);
                    }
                }
            });

            //save data
            btn_saveJobPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pushKey = job_offers_root.push().getKey();

                    hashMap_generalData.put("skill", autoComplete_skillCategory.getText().toString());
                    hashMap_generalData.put("jobTitle", autoComplete_jobTitle.getText().toString());
                    hashMap_generalData.put("postDescription", textInputEditText_postDescription.getText().toString());
                    hashMap_generalData.put("uid", uID);
                    hashMap_generalData.put("postJobID", pushKey);

                    cal.add(Calendar.MONTH, 12);
                    format.format(cal.getTime());
                    calculated_postExpDate = format.format(cal.getTime());

                    //new key "timestamp"
                    hashMap_generalData.put("permission", "pending");


                }
            });

    }

    //methods---------------------------------------------------------------------------------------
        //get data from current user
        private void getDataFromCurrentUser(){
            //profile pic url
            //company name
            //location
            //city

        }
        //set data
        private void setExposedDropdownListSkillCategory(){
            categories_root.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot snap_skillCategory : snapshot.getChildren()){
                        arrayList_skillCategory.add(snap_skillCategory.child("skill").getValue().toString());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        private void setExposedDropdownListJobTitle(){
            String chosenSkillCategory = autoComplete_skillCategory.getText().toString();
            //Toast.makeText(Emp_PostJob.this, chosenSkillCategory, Toast.LENGTH_LONG).show();
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
        private void setExposedDropdownListTypeofEmployment() {
            arrayList_typeOfEmployment.add("Regular Employment");
            arrayList_typeOfEmployment.add("Project Employment");
            arrayList_typeOfEmployment.add("Seasonal Employment");
            arrayList_typeOfEmployment.add("Casual Employment");
            arrayList_typeOfEmployment.add("Fixed Term Employment");
            arrayList_typeOfEmployment.add("Probationary Employment");
        }
        //These methods are for checking if checkBox is checked.
        private void selectedSecondarySkills(){
            checkboxIDs_type_of_disabilities = new Integer[]{R.id.typeOfSkills1, R.id.typeOfSkills2,
                    R.id.typeOfSkills3, R.id.typeOfSkills4, R.id.typeOfSkills5, R.id.typeOfSkills6, R.id.typeOfSkills7
                    ,R.id.typeOfSkills8, R.id.typeOfSkills9, R.id.typeOfSkills10};

            checkBoxes_secondary_skills = new CheckBox[checkboxIDs_type_of_disabilities.length];

            for(int i = 0; i < checkboxIDs_type_of_disabilities.length; i++){
                checkBoxes_secondary_skills[i] = (CheckBox) findViewById(checkboxIDs_type_of_disabilities[i]);
                if(checkBoxes_secondary_skills[i].isChecked()){
                    int i2 = i+1;
                    hashMap_secondary_skills.put("jobSkill" + i2, checkBoxes_secondary_skills[i].getText().toString().trim());
                }
            }
        }
        private void selectedTypeOfDisabilities() {
           checkboxIDs_type_of_disabilities = new Integer[]{R.id.typeOfDisability1, R.id.typeOfDisability2, R.id.typeOfDisability3};

           checkBoxes_type_of_disabilities = new CheckBox[checkboxIDs_type_of_disabilities.length];

            for (int count_typeOfDisabilities = 0; count_typeOfDisabilities < checkboxIDs_type_of_disabilities.length; count_typeOfDisabilities++) {
                checkBoxes_type_of_disabilities[count_typeOfDisabilities] = (CheckBox) findViewById(checkboxIDs_type_of_disabilities[count_typeOfDisabilities]);
                if (checkBoxes_type_of_disabilities[count_typeOfDisabilities].isChecked()) {
                    int j2 = count_typeOfDisabilities + 1;
                    hashMap_disability.put("typeOfDisability" + j2, checkBoxes_type_of_disabilities[count_typeOfDisabilities].getText().toString().trim());
                }
            }
            if(checkBox_typeOfDisability_Other.isChecked()){
                hashMap_disability.put("typeOfDisabilityMore", "Other Disabilities");
            }
        }
        //validate fields
        private Boolean checkExposedDropdownListValue(ArrayList arrayListToCheck, String exposedDropdownListValue){
            if (arrayListToCheck.contains(exposedDropdownListValue)){
                return true;
            }else{
                return false;
            }
        }

        //post duration?

}