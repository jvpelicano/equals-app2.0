package com.philcode.equals.EMP;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.philcode.equals.R;

import java.util.ArrayList;
import java.util.HashMap;

public class Emp_PostJob extends AppCompatActivity {
    //firebase connection
    private DatabaseReference job_offers_root, categories_root, typeOfDisabilities_root;
    private StorageReference job_offers_storage_root;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    //layout
        //adapters
        private ArrayAdapter<String> spinner_skillCategory_adapter;
        private ArrayAdapter<String> spinner_jobtitles_adapter;
        //exposed dropdown list text input layout
        private TextInputLayout textInputLayout_skillCategory, textInputLayout_jobTitle;
        //text input layout
        private TextInputLayout textInputLayout_otherDisabilitySpecific;
        //exposed dropdown list autocomplete text view
        private AutoCompleteTextView autoComplete_skillCategory, autoComplete_jobTitle;
        //text views
        private TextView txt_jobTitle;
        private TextView txt_jobTitleError;
        //edit texts
        private TextInputEditText textInputEditText_otherDisabilitySpecific, textInputEditText_postDescription;
        //check box
        private CheckBox checkBox_typeOfDisability_Other, checkBox_educAttainmentRequirement;
        //buttons
        private Button btn_test;
        //radio group
        private RadioGroup radioGroup_educ;
    //arrays
        //exposed dropdown list arrays
        private ArrayList <String> arrayList_skillCategory;
        private ArrayList <String> arrayList_spinner_jobtitles;
        //check boxes list
        private CheckBox[] checkBoxes_type_of_disabilities;
        private CheckBox[] checkBoxes_secondary_skills;
        //ID array list
        private Integer[] checkboxIDs_type_of_disabilities;
        private Integer[] checkboxIDs_secondary_skills;
    //hashMaps
        private HashMap<String, String> hashMap_disability, hashMap_secondary_skills, hashMap_generalData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emp_postjob);
        //initialize firebase connection
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
            //references
            categories_root = firebaseDatabase.getReference("Category"); //reference for spinners
            job_offers_root = firebaseDatabase.getReference("Job_Offers"); //where data will be stored
            job_offers_storage_root = firebaseStorage.getReference("Posts/"); //where image will be stored
        //initialize
            //arrays
                //exposed dropdown list arrays
                arrayList_skillCategory = new ArrayList<>();
                arrayList_spinner_jobtitles = new ArrayList<>();
            //hashMaps
                hashMap_disability = new HashMap<>();
                hashMap_secondary_skills = new HashMap<>();
                hashMap_generalData = new HashMap<>();

            //layout
                //adapters
                spinner_skillCategory_adapter =  new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arrayList_skillCategory);
                spinner_jobtitles_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arrayList_spinner_jobtitles);
                //autocomplete text view
                autoComplete_skillCategory = findViewById(R.id.autoComplete_skillCategory);
                autoComplete_jobTitle = findViewById(R.id.autoComplete_jobTitle);
                //check box
                checkBox_typeOfDisability_Other = findViewById(R.id.typeOfDisabilityOther);
                checkBox_educAttainmentRequirement = findViewById(R.id.checkBox_educAttainmentRequirement);
                //edit texts
                textInputEditText_postDescription = findViewById(R.id.textInputEditText_postDescription);
                //radio group
                radioGroup_educ = findViewById(R.id.radioGroup_educ);
                //text input layouts
                textInputLayout_jobTitle = findViewById(R.id.textInputLayout_jobTitle); //used
                textInputLayout_skillCategory = findViewById(R.id.textInputLayout_skillCategory);
                textInputLayout_otherDisabilitySpecific  = findViewById(R.id.textInputLayout_otherDisabilitySpecific);
                //text views
                txt_jobTitle = findViewById(R.id.txt_jobTitle);
                txt_jobTitleError = findViewById(R.id.txt_jobTitleError);

        //set exposed dropdown list
        setExposedDropdownListSkillCategory();
        //set adapters
        //skill category
        autoComplete_skillCategory.setAdapter(spinner_skillCategory_adapter);
        //job titles
        autoComplete_jobTitle.setAdapter(spinner_jobtitles_adapter);
        autoComplete_jobTitle.setThreshold(1);

        //listeners
            autoComplete_skillCategory.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(!hasFocus){
                        //Toast.makeText(Emp_PostJob.this, checkExposedDropdownListValue(arrayList_spinner_jobtitles, autoComplete_jobTitle.getText().toString()).toString(), Toast.LENGTH_LONG).show();
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
                    arrayList_spinner_jobtitles.clear();
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
                        Boolean exists = checkExposedDropdownListValue(arrayList_spinner_jobtitles, autoComplete_jobTitle.getText().toString());
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
                    }else{
                        radioGroup_educ.setVisibility(View.GONE);
                    }
                }
            });
    }
    //methods
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
                                arrayList_spinner_jobtitles.add(snap_jobTitles.getValue().toString());
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
    private Boolean checkExposedDropdownListValue(ArrayList arrayListToCheck, String exposedDropdownListValue){
        if (arrayListToCheck.contains(exposedDropdownListValue)){
            return true;
        }else{
            return false;
        }
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

}