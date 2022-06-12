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
    private String radioButton_educAttainment_text, radioButton_workSetUp_text;
    //int
    private int selected_educAttainment_ID, selected_workExpRg_ID, selected_workSetUp_ID;
    //boolean
    private boolean editTextsValid = true;
    private boolean autoCompleteValid = true;
    private boolean skillCategory_exists= true;
    private boolean jobTitle_exists = true;

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
        private TextInputLayout textInputLayout_degree, textInputLayout_jobTitle, textInputLayout_typeOfEmployment;
        //image view
        private ImageView imageView;
        //text input layout
        private TextInputLayout textInputLayout_otherDisabilitySpecific, textInputLayout_yearsOfExperience;
        //exposed dropdown list autocomplete text view
        private AutoCompleteTextView autoComplete_degree, autoComplete_jobTitle, autoComplete_typeOfEmployment;
        //text views
        private TextView txt_degree;
        private TextView txt_jobTitleError, txt_minYearsOfExpError , txt_skillCategoryError, txt_typeOfDisabilityOtherError;
        //edit texts
        private TextInputEditText textInputEditText_otherDisabilitySpecific, textInputEditText_postDescription, textInputEditText_maxNumberOfApplicants
                ,textInputEditText_yearsOfExperience;
        //check box
        private CheckBox checkBox_typeOfDisability_Other, checkBox_educAttainmentRequirement;
        //buttons
        private Button btn_saveJobPost, btn_chooseHeaderImage;
        //radio button
        private RadioButton radioButton_withWorkExp, radioButton_educAttainment, radioButton_workSetUp, radio_1, radio_2, radio_3, radio_4, radio_5, radio_6;
        //radio group
        private RadioGroup radioGroup_educ, radioGroup_workSetUp;
        //progress dialog
        private ProgressDialog progressDialog;
    //arrays
        //exposed dropdown list arrays
        private ArrayList <String> arrayList_degree;
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
                arrayList_degree = new ArrayList<>();
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
                exposedDropdownList_skillCategory_adapter =  new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arrayList_degree);
                exposedDropdownList_jobtitles_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arrayList_jobTitles);
                exposedDropdownList_typeOfEmployment_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,arrayList_typeOfEmployment);
                //autocomplete text view
                autoComplete_degree = findViewById(R.id.autoComplete_degree);
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
                //textInputEditText_maxNumberOfApplicants = findViewById(R.id.textInputEditText_maxNumberOfApplicants);
                textInputEditText_yearsOfExperience = findViewById(R.id.textInputEditText_yearsOfExperience);
                //image view
                imageView = findViewById(R.id.displayPostPic);
                //progress dialog
                progressDialog = new ProgressDialog(Emp_PostJob.this);
                //radio button
                radioButton_withWorkExp = findViewById(R.id.radio_11);
                radioButton_workSetUp = findViewById(R.id.radioButton_onSiteWork);
                radio_1 = findViewById(R.id.radio_1);
                radio_2 = findViewById(R.id.radio_2);
                radio_3 = findViewById(R.id.radio_3);
                radio_4 = findViewById(R.id.radio_4);
                radio_5 = findViewById(R.id.radio_5);
                radio_6 = findViewById(R.id.radio_6);
                //radio group
                radioGroup_educ = findViewById(R.id.radioGroup_educ);
                radioGroup_workSetUp = findViewById(R.id.radioGroup_workSetUp);
                //int
                selected_educAttainment_ID = radioGroup_educ.getCheckedRadioButtonId();
                selected_workSetUp_ID = radioGroup_workSetUp.getCheckedRadioButtonId();
                //text input layouts
                textInputLayout_jobTitle = findViewById(R.id.textInputLayout_jobTitle); //autocomplete
                textInputLayout_degree = findViewById(R.id.textInputLayout_degree);//autocomplete
                textInputLayout_typeOfEmployment = findViewById(R.id.textInputLayout_typeOfEmployment);//autocomplete
                textInputLayout_otherDisabilitySpecific  = findViewById(R.id.textInputLayout_otherDisabilitySpecific);
                textInputLayout_yearsOfExperience = findViewById(R.id.textInputLayout_yearsOfExperience);
                //text views
                txt_degree = findViewById(R.id.txt_degree);
                txt_jobTitleError = findViewById(R.id.txt_jobTitleError);
                txt_minYearsOfExpError = findViewById(R.id.txt_minYearsOfExp);
                txt_skillCategoryError = findViewById(R.id.txt_skillCategoryError);
                txt_typeOfDisabilityOtherError = findViewById(R.id.txt_typeOfDisabilityOtherError);

        //set exposed dropdown list
        setExposedDropdownListTypeofEmployment();
        setExposedDropdownListJobTitle();

        //set adapters
            //skill category
            autoComplete_degree.setAdapter(exposedDropdownList_skillCategory_adapter);
            //job titles
            autoComplete_jobTitle.setAdapter(exposedDropdownList_jobtitles_adapter);
            autoComplete_jobTitle.setThreshold(1);
            //type of employment
            autoComplete_typeOfEmployment.setAdapter(exposedDropdownList_typeOfEmployment_adapter);
        //listeners
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
        autoComplete_jobTitle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                arrayList_degree.clear();
                setExposedDropdownListSkillCategory();
            }
        });

        autoComplete_degree.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(!hasFocus){
                        skillCategory_exists = checkExposedDropdownListValue(arrayList_degree, autoComplete_degree.getText().toString());
                        if(!skillCategory_exists){
                            txt_skillCategoryError.setVisibility(View.VISIBLE);
                        }else{
                            txt_skillCategoryError.setVisibility(View.GONE);
                        }
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
            radio_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        txt_degree.setVisibility(View.GONE);
                        textInputLayout_degree.setVisibility(View.GONE);
                    }
                }
            });
            radio_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        txt_degree.setVisibility(View.GONE);
                        textInputLayout_degree.setVisibility(View.GONE);
                    }
                }
            });
            radio_3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        txt_degree.setVisibility(View.GONE);
                        textInputLayout_degree.setVisibility(View.GONE);
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

        radioButton_withWorkExp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    textInputLayout_yearsOfExperience.setVisibility(View.VISIBLE);

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
                    txt_minYearsOfExpError.setVisibility(View.GONE);
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

        btn_saveJobPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedSecondarySkills();
                    selectedTypeOfDisabilities();
                    getDataFromCurrentUser();

                    if(FilePathUri != null){ // upload data

                        if(checkBox_type_of_disabilities_checkIfEmpty.isEmpty() && !checkBox_typeOfDisability_Other.isChecked()){

                            Toast.makeText(Emp_PostJob.this, "Error", Toast.LENGTH_SHORT).show();

                        }else{
                            if(checkBox_typeOfDisability_Other.isChecked()){

                                if(!textInputEditText_otherDisabilitySpecific.getText().toString().isEmpty()){

                                    if(autoComplete_degree.getText().toString().isEmpty() || autoComplete_jobTitle.getText().toString().isEmpty()
                                    || autoComplete_typeOfEmployment.getText().toString().isEmpty() || checkBox_secondary_skills_checkIfEmpty.isEmpty()
                                            || textInputEditText_postDescription.getText().toString().isEmpty()
                                            || autoComplete_degree.getText().toString().equals("Click to select value")){ // error
                                            //|| textInputEditText_maxNumberOfApplicants.getText().toString().isEmpty()
                                        Toast.makeText(Emp_PostJob.this, "Please complete the form.", Toast.LENGTH_SHORT).show();

                                    }else{

                                        if(radioButton_withWorkExp.isChecked()){
                                            if(textInputEditText_yearsOfExperience.getText().toString().isEmpty()){
                                                Toast.makeText(Emp_PostJob.this, "Please complete the form.", Toast.LENGTH_SHORT).show();
                                            }else{
                                                uploadData();
                                            }

                                        }else{
                                            uploadData();
                                        }

                                    }

                                }else{
                                    Toast.makeText(Emp_PostJob.this, "Please complete the form.", Toast.LENGTH_SHORT).show();
                                }
                            }else{

                                if(autoComplete_degree.getText().toString().isEmpty() || autoComplete_jobTitle.getText().toString().isEmpty()
                                        || autoComplete_typeOfEmployment.getText().toString().isEmpty() || checkBox_secondary_skills_checkIfEmpty.isEmpty()
                                        || textInputEditText_postDescription.getText().toString().isEmpty()
                                        || autoComplete_degree.getText().toString().equals("Click to select value")){ // error
                                        //|| textInputEditText_maxNumberOfApplicants.getText().toString().isEmpty()
                                    Toast.makeText(Emp_PostJob.this, "Please complete the form.", Toast.LENGTH_SHORT).show();

                                }else{
                                    if(radioButton_withWorkExp.isChecked()){

                                        if(textInputEditText_yearsOfExperience.getText().toString().isEmpty()){//error
                                            Toast.makeText(Emp_PostJob.this, "Please complete the form.", Toast.LENGTH_SHORT).show();
                                        }else{
                                            uploadData();
                                        }

                                    }else{
                                        uploadData();
                                    }
                                }


                            }
                        }

                    }else{
                        Toast.makeText(Emp_PostJob.this, "Please complete the form.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

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

                                    final int selected_educAttainment_ID = radioGroup_educ.getCheckedRadioButtonId();

                                    hashMap_generalData.put("skill", autoComplete_degree.getText().toString());
                                    hashMap_generalData.put("jobTitle", autoComplete_jobTitle.getText().toString());
                                    hashMap_generalData.put("postDescription", textInputEditText_postDescription.getText().toString());
                                    hashMap_generalData.put("uid", uID);
                                    hashMap_generalData.put("postDate", postDate);
                                    hashMap_generalData.put("typeOfEmployment", autoComplete_typeOfEmployment.getText().toString());
                                    hashMap_generalData.put("workSetUp", radioButton_workSetUp_text);
                                    //hashMap_generalData.put("maxNumberApplicants", textInputEditText_maxNumberOfApplicants.getText().toString());
                                    hashMap_generalData.put("imageURL", imageURL);
                                    hashMap_generalData.put("typeOfDisabilityMore", textInputEditText_otherDisabilitySpecific.getText().toString());

                                    if(radioButton_withWorkExp.isChecked()){
                                        hashMap_generalData.put("workExperience", radioButton_withWorkExp.getText().toString());
                                        hashMap_generalData.put("yearsOfExperience", textInputEditText_yearsOfExperience.getText().toString());
                                    }else{
                                        hashMap_generalData.put("yearsOfExperience", "0");
                                        hashMap_generalData.put("workExperience", "Without Experience");
                                    }

                                    if(checkBox_educAttainmentRequirement.isChecked()){
                                        hashMap_generalData.put("educationalAttainmentRequirement", "true");

                                        radioButton_educAttainment = findViewById(selected_educAttainment_ID);
                                        hashMap_generalData.put("educationalAttainment", radioButton_educAttainment.getText().toString());
                                    }else{
                                        hashMap_generalData.put("educationalAttainmentRequirement", "false");
                                        hashMap_generalData.put("educationalAttainment", "Elementary Level");
                                    }

                                    if(radioButton_workSetUp.isChecked()){
                                        hashMap_generalData.put("workSetUp", radioButton_workSetUp.getText().toString());
                                    }else{
                                        hashMap_generalData.put("workSetUp", "Remote Work");
                                    }

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
            String chosenJobTitle = autoComplete_jobTitle.getText().toString();
            //Toast.makeText(Emp_PostJob.this, chosenSkillCategory, Toast.LENGTH_LONG).show();
            categories_root.orderByChild("jobtitle").equalTo(chosenJobTitle).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot snap_category_key : snapshot.getChildren()){
                        String parent = snap_category_key.getKey();

                        categories_root.child(parent).child("specialization").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot snap_jobTitles : snapshot.getChildren()){
                                    arrayList_degree.add(snap_jobTitles.getValue().toString());
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
            /*String chosenSkillCategory = autoComplete_skillCategory.getText().toString();
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
            });*/
            categories_root.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot snap_skillCategory : snapshot.getChildren()){
                        arrayList_jobTitles.add(snap_skillCategory.child("jobtitle").getValue().toString());
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

}