package com.philcode.equals.EMP;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.philcode.equals.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Emp_PostJob extends AppCompatActivity {
    //firebase connection
    private DatabaseReference job_offers_root, categories_root, emp_user_root;
    private StorageReference ref, job_offers_storage_root;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    //string
    private String uID;
    private String pushKey;
    private String calculated_postExpDate;
    private String postDate;
    private String radioButton_educAttainment_text;
    //int
    private int selected_educAttainment_ID, selected_workExpRg_ID;
    //boolean
    private Boolean editTextsValid, autoCompleteValid, skillCategory_exists, jobTitle_exists;

    //calendar
    private Calendar cal;
    private SimpleDateFormat format;
    //request codes
    private int Image_Request_Code = 7;
    //uri
    private Uri FilePathUri;
    //layout
        //adapters
        private ArrayAdapter<String> exposedDropdownList_skillCategory_adapter;
        private ArrayAdapter<String> exposedDropdownList_jobtitles_adapter;
        private ArrayAdapter<String> exposedDropdownList_typeOfEmployment_adapter;
        //exposed dropdown list text input layout
        private TextInputLayout textInputLayout_skillCategory, textInputLayout_jobTitle, textInputLayout_typeOfEmployment;
        //image view
        private ImageView imageView;
        //text input layout
        private TextInputLayout textInputLayout_otherDisabilitySpecific, textInputLayout_yearsOfExperience;
        //exposed dropdown list autocomplete text view
        private AutoCompleteTextView autoComplete_skillCategory, autoComplete_jobTitle, autoComplete_typeOfEmployment;
        //text views
        private TextView txt_jobTitle;
        private TextView txt_jobTitleError, txt_minYearsOfExpError , txt_skillCategoryError, txt_typeOfDisabilityOtherError;
        //edit texts
        private TextInputEditText textInputEditText_otherDisabilitySpecific, textInputEditText_postDescription, textInputEditText_maxNumberOfApplicants
                ,textInputEditText_yearsOfExperience;
        //check box
        private CheckBox checkBox_typeOfDisability_Other, checkBox_educAttainmentRequirement;
        //buttons
        private Button btn_saveJobPost, btn_chooseHeaderImage;
        //radio button
        private RadioButton radioButton_withWorkExp, radioButton_educAttainment;
        //radio group
        private RadioGroup radioGroup_educ;
        //progress dialog
        private ProgressDialog progressDialog;
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
        //String array
        private ArrayList <String> checkBox_secondary_skills_checkIfEmpty;
        private ArrayList <String> checkBox_type_of_disabilities_checkIfEmpty;
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
            job_offers_root = firebaseDatabase.getReference("Job_Offers/"); //where data will be stored
            emp_user_root = firebaseDatabase.getReference("Employers"); // get some data
            job_offers_storage_root = firebaseStorage.getReference(); //where image will be stored
            //user ID
            uID = firebaseUser.getUid();

        //initialize
            //date
            Date currentDate = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("MMMM dd, yyyy");
            postDate = df.format(currentDate);
            cal = Calendar.getInstance();
            format  = new SimpleDateFormat("MMMM dd, yyyy");
            //arrays
                //exposed dropdown list arrays
                arrayList_skillCategory = new ArrayList<>();
                arrayList_jobTitles = new ArrayList<>();
                arrayList_typeOfEmployment = new ArrayList<>();
                //hashMaps
                hashMap_disability = new HashMap<>();
                hashMap_secondary_skills = new HashMap<>();
                hashMap_generalData = new HashMap<>();
                //string arrays
                checkBox_secondary_skills_checkIfEmpty = new ArrayList<>();
                checkBox_type_of_disabilities_checkIfEmpty = new ArrayList<>();

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
                btn_chooseHeaderImage = findViewById(R.id.btn_chooseHeaderImage);
                //check box
                checkBox_typeOfDisability_Other = findViewById(R.id.typeOfDisabilityOther);
                checkBox_educAttainmentRequirement = findViewById(R.id.checkBox_educAttainmentRequirement);
                //edit texts
                textInputEditText_postDescription = findViewById(R.id.textInputEditText_postDescription);
                textInputEditText_otherDisabilitySpecific = findViewById(R.id.textInputEditText_otherDisabilitySpecific);
                textInputEditText_maxNumberOfApplicants = findViewById(R.id.textInputEditText_maxNumberOfApplicants);
                textInputEditText_yearsOfExperience = findViewById(R.id.textInputEditText_yearsOfExperience);
                //image view
                imageView = findViewById(R.id.displayPostPic);
                //progress dialog
                progressDialog = new ProgressDialog(Emp_PostJob.this);
                //radio button
                radioButton_withWorkExp = findViewById(R.id.radio_11);
                //radio group
                radioGroup_educ = findViewById(R.id.radioGroup_educ);
                //int
                selected_educAttainment_ID = radioGroup_educ.getCheckedRadioButtonId();
                //text input layouts
                textInputLayout_jobTitle = findViewById(R.id.textInputLayout_jobTitle); //autocomplete
                textInputLayout_skillCategory = findViewById(R.id.textInputLayout_skillCategory);//autocomplete
                textInputLayout_typeOfEmployment = findViewById(R.id.textInputLayout_typeOfEmployment);//autocomplete
                textInputLayout_otherDisabilitySpecific  = findViewById(R.id.textInputLayout_otherDisabilitySpecific);
                textInputLayout_yearsOfExperience = findViewById(R.id.textInputLayout_yearsOfExperience);
                //text views
                txt_jobTitle = findViewById(R.id.txt_jobTitle);
                txt_jobTitleError = findViewById(R.id.txt_jobTitleError);
                txt_minYearsOfExpError = findViewById(R.id.txt_minYearsOfExp);
                txt_skillCategoryError = findViewById(R.id.txt_skillCategoryError);
                txt_typeOfDisabilityOtherError = findViewById(R.id.txt_typeOfDisabilityOtherError);

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
                        skillCategory_exists = checkExposedDropdownListValue(arrayList_skillCategory, autoComplete_skillCategory.getText().toString());
                        if(!skillCategory_exists){
                            txt_skillCategoryError.setVisibility(View.VISIBLE);
                        }else{
                            txt_skillCategoryError.setVisibility(View.GONE);
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
                        jobTitle_exists = checkExposedDropdownListValue(arrayList_jobTitles, autoComplete_jobTitle.getText().toString());
                        if(!jobTitle_exists){
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
                        hashMap_generalData.put("typeOfDisabilityMore", textInputEditText_otherDisabilitySpecific.getText().toString());
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
                        hashMap_generalData.put("workExperience", radioButton_withWorkExp.getText().toString());
                        hashMap_generalData.put("yearsOfExperience", textInputEditText_yearsOfExperience.getText().toString());
                        textInputEditText_yearsOfExperience.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                if(!hasFocus){
                                    if(textInputEditText_yearsOfExperience.getText().toString().isEmpty()){
                                        txt_minYearsOfExpError.setVisibility(View.VISIBLE);
                                    }else{
                                        txt_minYearsOfExpError.setVisibility(View.GONE);
                                    }
                                }
                            }
                        });
                    }else{
                        textInputLayout_yearsOfExperience.setVisibility(View.GONE);
                        hashMap_generalData.put("workExperience", "Without Experience");
                        hashMap_generalData.put("yearsOfExperience", "0");
                        txt_minYearsOfExpError.setVisibility(View.GONE);
                    }
                }
            });
            btn_chooseHeaderImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    // Setting intent type as image to select image from phone storage.
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Please Select Image"), Image_Request_Code);
                }
            });

            //save data
            btn_saveJobPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(FilePathUri != null ){
                        selectedSecondarySkills();
                        selectedTypeOfDisabilities();
                        getDataFromCurrentUser();
                        checkEditTextFields(textInputEditText_maxNumberOfApplicants);
                        checkEditTextFields(textInputEditText_postDescription);
                        checkAutoCompleteFields(autoComplete_skillCategory);
                        checkAutoCompleteFields(autoComplete_jobTitle);
                        checkAutoCompleteFields(autoComplete_typeOfEmployment);

                        if(editTextsValid && autoCompleteValid && !checkBox_secondary_skills_checkIfEmpty.isEmpty()
                                && (!checkBox_type_of_disabilities_checkIfEmpty.isEmpty()
                                || checkBox_typeOfDisability_Other.isChecked())
                                && skillCategory_exists
                                && jobTitle_exists){ // if this returns true

                                if(checkBox_typeOfDisability_Other.isChecked() && textInputEditText_otherDisabilitySpecific.getText().toString().isEmpty() //check this
                                        || radioButton_withWorkExp.isChecked() && textInputEditText_yearsOfExperience.getText().toString().isEmpty()){

                                        Toast.makeText(Emp_PostJob.this, "Please complete the form.", Toast.LENGTH_SHORT).show();
                                }else{

                                    uploadData();

                                }

                        } else{
                            Toast.makeText(Emp_PostJob.this, "Please complete the form.", Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        Toast.makeText(Emp_PostJob.this, "Please complete the form.", Toast.LENGTH_SHORT).show();
                    }

                }
            });// end of button save

    }

    //methods---------------------------------------------------------------------------------------
        //get data from current user
        private void getDataFromCurrentUser(){
            //profile pic url-
            //company name-
            //location-
            //city-
            emp_user_root.child(uID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    hashMap_generalData.put("postLocation", snapshot.child("companyaddress").getValue().toString());
                    hashMap_generalData.put("city", snapshot.child("companycity").getValue().toString());
                    hashMap_generalData.put("empProfilePic", snapshot.child("empProfilePic").getValue().toString());
                    hashMap_generalData.put("companyName", snapshot.child("fullname").getValue().toString());
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
        //upload
        private void uploadData(){
            progressDialog.setTitle("Posting...");
            progressDialog.show();
            ref = job_offers_storage_root.child("Job_Offers/" + System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));
            ref.putFile(FilePathUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl()
                            .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    progressDialog.dismiss();
                                    final String imageURL = task.getResult().toString();
                                    pushKey = job_offers_root.push().getKey();

                                    radioButton_educAttainment = findViewById(selected_educAttainment_ID);
                                    radioButton_educAttainment_text = radioButton_educAttainment.getText().toString();

                                    hashMap_generalData.put("skill", autoComplete_skillCategory.getText().toString());
                                    hashMap_generalData.put("jobTitle", autoComplete_jobTitle.getText().toString());
                                    hashMap_generalData.put("postDescription", textInputEditText_postDescription.getText().toString());
                                    hashMap_generalData.put("uid", uID);
                                    hashMap_generalData.put("postDate", postDate);
                                    hashMap_generalData.put("educationalAttainment", radioButton_educAttainment_text);
                                    hashMap_generalData.put("typeOfEmployment", autoComplete_typeOfEmployment.getText().toString());
                                    hashMap_generalData.put("maxNumberApplicants", textInputEditText_maxNumberOfApplicants.getText().toString());
                                    hashMap_generalData.put("imageURL", imageURL);

                                    //reference automatic deletion after 12 months
                                    cal.add(Calendar.MONTH, 12);
                                    format.format(cal.getTime());
                                    calculated_postExpDate = format.format(cal.getTime());

                                    hashMap_generalData.put("expDate", calculated_postExpDate);

                                    //new key "timestamp"
                                    hashMap_generalData.put("permission", "pending");

                                    job_offers_root.child(pushKey).setValue(hashMap_generalData);
                                    Toast.makeText(Emp_PostJob.this, "Uploaded Successfully!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Emp_PostJob.this, a_EmployeeContentMainActivity.class));
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(Emp_PostJob.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(Emp_PostJob.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0 * snapshot.getBytesTransferred() / snapshot
                            .getTotalByteCount());
                    progressDialog.setMessage("Uploaded " + (int) progress + "%");
                    progressDialog.setCancelable(false);
                }
            });
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
                    ,R.id.typeOfSkills8, R.id.typeOfSkills9, R.id.typeOfSkills10, R.id.typeOfSkills11, R.id.typeOfSkills12,
                    R.id.typeOfSkills13, R.id.typeOfSkills14};

            checkBoxes_secondary_skills = new CheckBox[checkboxIDs_type_of_disabilities.length];

            for(int i = 0; i < checkboxIDs_type_of_disabilities.length; i++){
                checkBoxes_secondary_skills[i] = (CheckBox) findViewById(checkboxIDs_type_of_disabilities[i]);
                if(checkBoxes_secondary_skills[i].isChecked()){
                    int i2 = i+1;
                    hashMap_generalData.put("jobSkill" + i2, checkBoxes_secondary_skills[i].getText().toString().trim());
                    checkBox_secondary_skills_checkIfEmpty.add(checkBoxes_secondary_skills[i].getText().toString().trim());
                }
            }
        }
        private void selectedTypeOfDisabilities() {
           checkboxIDs_type_of_disabilities = new Integer[]{R.id.typeOfDisability1, R.id.typeOfDisability2, R.id.typeOfDisability3, R.id.typeOfDisability4};

           checkBoxes_type_of_disabilities = new CheckBox[checkboxIDs_type_of_disabilities.length];

            for (int count_typeOfDisabilities = 0; count_typeOfDisabilities < checkboxIDs_type_of_disabilities.length; count_typeOfDisabilities++) {
                checkBoxes_type_of_disabilities[count_typeOfDisabilities] = (CheckBox) findViewById(checkboxIDs_type_of_disabilities[count_typeOfDisabilities]);
                if (checkBoxes_type_of_disabilities[count_typeOfDisabilities].isChecked()) {
                    int j2 = count_typeOfDisabilities + 1;
                    hashMap_generalData.put("typeOfDisability" + j2, checkBoxes_type_of_disabilities[count_typeOfDisabilities].getText().toString().trim());
                    checkBox_type_of_disabilities_checkIfEmpty.add(checkBoxes_type_of_disabilities[count_typeOfDisabilities].getText().toString().trim());
                }
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
        private Boolean checkEditTextFields(TextInputEditText textInputEditTextToCheck){
            if(!textInputEditTextToCheck.toString().isEmpty()){
                editTextsValid = true;
            }else{
                editTextsValid = false;
            }
            return editTextsValid;
        }
        private Boolean checkAutoCompleteFields(AutoCompleteTextView autoCompleteTextViewToCheck){
            if(!autoCompleteTextViewToCheck.getText().toString().isEmpty()){
                autoCompleteValid = true;
            }else{
                autoCompleteValid = false;
            }
            return autoCompleteValid;
        }


        //post duration?
            /*private void calculateExpDate(String selected_postExpDate) {
                if(selected_postExpDate.equals("1 week")) { // working
                    cal.add(Calendar.WEEK_OF_YEAR, 1);
                    format.format(cal.getTime());
                    calculated_postExpDate = format.format(cal.getTime());

                }else if(selected_postExpDate.equals("2 weeks")) {
                    cal.add(Calendar.WEEK_OF_YEAR, 2);
                    format.format(cal.getTime());
                    calculated_postExpDate = format.format(cal.getTime());

                }else if(selected_postExpDate.equals("3 weeks")) {
                    cal.add(Calendar.WEEK_OF_YEAR, 3);
                    format.format(cal.getTime());
                    calculated_postExpDate = format.format(cal.getTime());

                }else if(selected_postExpDate.equals("1 month")) {
                    cal.add(Calendar.MONTH, 1);
                    format.format(cal.getTime());
                    calculated_postExpDate = format.format(cal.getTime());

                }else if(selected_postExpDate.equals("2 months")) {
                    cal.add(Calendar.MONTH, 2);
                    format.format(cal.getTime());
                    calculated_postExpDate = format.format(cal.getTime());

                } else if(selected_postExpDate.equals("3 months")) {
                    cal.add(Calendar.MONTH, 3);
                    format.format(cal.getTime());
                    calculated_postExpDate = format.format(cal.getTime());

                } else if(selected_postExpDate.equals("4 months")) {
                    cal.add(Calendar.MONTH, 4);
                    format.format(cal.getTime());
                    calculated_postExpDate = format.format(cal.getTime());

                } else if(selected_postExpDate.equals("5 months")) {
                    cal.add(Calendar.MONTH, 5);
                    format.format(cal.getTime());
                    calculated_postExpDate = format.format(cal.getTime());

                } else if(selected_postExpDate.equals("6 months")) {
                    cal.add(Calendar.MONTH, 6);
                    format.format(cal.getTime());
                    calculated_postExpDate = format.format(cal.getTime());

                } else if(selected_postExpDate.equals("7 months")) {
                    cal.add(Calendar.MONTH, 7);
                    format.format(cal.getTime());
                    calculated_postExpDate = format.format(cal.getTime());

                } else if(selected_postExpDate.equals("8 months")) {
                    cal.add(Calendar.MONTH, 8);
                    format.format(cal.getTime());
                    calculated_postExpDate = format.format(cal.getTime());

                } else if(selected_postExpDate.equals("9 months")) {
                    cal.add(Calendar.MONTH, 9);
                    format.format(cal.getTime());
                    calculated_postExpDate = format.format(cal.getTime());

                } else if(selected_postExpDate.equals("10 months")) {
                    cal.add(Calendar.MONTH, 10);
                    format.format(cal.getTime());
                    calculated_postExpDate = format.format(cal.getTime());

                } else if(selected_postExpDate.equals("11 months")) {
                    cal.add(Calendar.MONTH, 11);
                    format.format(cal.getTime());
                    calculated_postExpDate = format.format(cal.getTime());

                }else if(selected_postExpDate.equals("1 year")) {
                    cal.add(Calendar.MONTH, 12);
                    format.format(cal.getTime());
                    calculated_postExpDate = format.format(cal.getTime());

                }else if(selected_postExpDate.equals("Unlimited")) {
                    calculated_postExpDate = "unlimited";
                }
            }*/

        //choose image
        public String GetFileExtension(Uri uri) {
            ContentResolver contentResolver = getContentResolver();

            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

            // Returning the file Extension.
            return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
        }
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == Image_Request_Code && resultCode == RESULT_OK
                    && data != null && data.getData() != null) {
                FilePathUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);
                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

}