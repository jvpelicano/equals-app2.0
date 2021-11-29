package com.philcode.equals.EMP;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
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
import com.philcode.equals.PWD.PWD_RegisterActivity2;
import com.philcode.equals.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Emp_PostJob extends AppCompatActivity {
    //Removed selection of primary skills
    TextInputEditText postTitle, postDescription, yearsOfExperience;
    Button empBtnPost;
    CheckBox typeOfDisability1, typeOfDisability2, typeOfDisability3, typeOfDisabilityMore;
    Spinner setExpDate;
    Button empBtnChoose;
    private RadioGroup rgWorkExperience, rgEducAttainment;
    private RadioButton rbEduc, rbWorkExperience, rbWithExperience, rbWithoutExperience;

    String typeOfDisability_1 = "";
    String typeOfDisability_2 = "";
    String typeOfDisability_3 = "";
    String typeOfDisability_More = "";
    private Spinner primary_skillsCategory;
    boolean[] checkedItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();
    String categorySkill;
    String x;
    String Address;
    String city;
    String week1="1 week";
    String week2="2 weeks";
    String week3="3 weeks";
    String month1="1 month";
    String month2="2 months";
    String month3="3 months";
    String month4="4 months";
    String month5="5 months";
    String month6="6 months";
    String month7="7 months";
    String month8="8 months";
    String month9="9 months";
    String month10="10 months";
    String month11="11 months";
    String year1="1 year";
    String unli="Unlimited";
    public String expDate1;
    int years =0;
    private TextView skillSelected;


    private String educAttainment = "";
    private String workExperience = "";

    String zz;


    Calendar cal = Calendar.getInstance();
    Date currentDate = Calendar.getInstance().getTime();
    SimpleDateFormat df = new SimpleDateFormat("MMMM-dd-yyyy");
    //String formattedDate = df.format(c);
    SimpleDateFormat format = new SimpleDateFormat("MMMM dd, yyyy");


    String permission;
    CheckBox jobSkill1, jobSkill2, jobSkill3, jobSkill4, jobSkill5, jobSkill6, jobSkill7,
            jobSkill8, jobSkill9, jobSkill10, checkEducRequirement;

    // Folder path for Firebase Storage.
    String Storage_Path = "Job_Offers/";
    // Root Database Name for Firebase Database.
    String Database_Path = "Job_Offers";
    // Creating ImageView.
    private ImageView ImageView;
    // Creating URI.
    private Uri FilePathUri;

    // Image request code for onActivityResult() .
    int Image_Request_Code = 7;

    // Creating StorageReference and DatabaseReference object.
    StorageReference storageReference;
    DatabaseReference databaseReference;

    private final int PICK_IMAGE_REQUEST = 7;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        rgWorkExperience = findViewById(R.id.workexperience);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emp_postjob);
        FirebaseUser cbf = FirebaseAuth.getInstance().getCurrentUser();
        final String cu = cbf.getUid();
        // Assign FirebaseStorage instance to storageReference.
        storageReference = FirebaseStorage.getInstance().getReference();

        final DatabaseReference locRef = FirebaseDatabase.getInstance().getReference().child("Employers").child(cu);
        locRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Address = dataSnapshot.child("companyaddress").getValue(String.class);
                city = dataSnapshot.child("companycity").getValue(String.class);
     //           Toast.makeText(getApplicationContext(), cu+Address+city, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Assign FirebaseDatabase instance with root database name.
        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);
        skillSelected = findViewById(R.id.selectedSkills);
        empBtnChoose = findViewById(R.id.empBtnChoose);
        empBtnPost = findViewById(R.id.empBtnPost);
        ImageView = findViewById(R.id.displayPostPic);
        rgEducAttainment = findViewById(R.id.rg_educ);
        rgWorkExperience = findViewById(R.id.workexperience);
        checkEducRequirement=(CheckBox) findViewById(R.id.educAttainmentRequirement);
        checkEducRequirement.setVisibility(View.GONE);
        progressDialog = new ProgressDialog(Emp_PostJob.this);
        empBtnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Creating intent.
                Intent intent = new Intent();

                // Setting intent type as image to select image from phone storage.
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Please Select Image"), Image_Request_Code);
            }
        });
        final int selectExperience = rgWorkExperience.getCheckedRadioButtonId();

        yearsOfExperience = findViewById(R.id.yearsofExperience);
        rbWithExperience = findViewById(R.id.radio_11);
        rbWithoutExperience = findViewById(R.id.radio_10);
        rgWorkExperience = findViewById(R.id.workexperience);
        rbWorkExperience = findViewById(selectExperience);
        workExperience = rbWorkExperience.getText().toString();
        rgWorkExperience.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (rbWithExperience.isChecked()) {
                    yearsOfExperience.setVisibility(View.VISIBLE);

                }
                if (rbWithoutExperience.isChecked()) {
                    yearsOfExperience.setVisibility(View.GONE);
                }
            }
        });
        primary_skillsCategory = findViewById(R.id.skillSpinner);
        //DELETED WHOLE BLOCK -----------------------------------------------------------------------


        postTitle = findViewById(R.id.postTitle);
        postDescription = findViewById(R.id.postDescription);
        // postLocation = findViewById(R.id.postLocation);
        typeOfDisability1 = (CheckBox) findViewById(R.id.typeOfDisability1);
        typeOfDisability2 = (CheckBox) findViewById(R.id.typeOfDisability2);
        typeOfDisability3 = (CheckBox) findViewById(R.id.typeOfDisability3);
        typeOfDisabilityMore = (CheckBox) findViewById(R.id.typeOfDisabilityOther);
        jobSkill1 = findViewById(R.id.typeOfSkills1);
        jobSkill2 = findViewById(R.id.typeOfSkills2);
        jobSkill3 = findViewById(R.id.typeOfSkills3);
        jobSkill4 = findViewById(R.id.typeOfSkills4);
        jobSkill5 = findViewById(R.id.typeOfSkills5);
        jobSkill6 = findViewById(R.id.typeOfSkills6);
        jobSkill7 = findViewById(R.id.typeOfSkills7);
        jobSkill8 = findViewById(R.id.typeOfSkills8);
        jobSkill9 = findViewById(R.id.typeOfSkills9);
        jobSkill10 = findViewById(R.id.typeOfSkills10);
        setExpDate = findViewById(R.id.spinnerExpDate);

        String[] spinnerExpDate = new String[]{"1 week","2 weeks", "3 weeks","1 month", "2 months",
                "3 months","4 months","5 months","6 months","7 months","8 months","9 months","10 months",
                "11 months","1 year", "Unlimited"};

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerExpDate);
        setExpDate.setAdapter(adapter2);
        final DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference().child("Category/");
        categoryRef.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {

                final List<String> category = new ArrayList<String>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Map<String, Object> data = (Map<String, Object>) snapshot.getValue();
                    category.add(data.get("skill").toString());
                }
                ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(Emp_PostJob.this, android.R.layout.simple_spinner_item, category) {
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
        permission = "pending";
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        final String uid = currentFirebaseUser.getUid();
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("Employers/"+uid);
        empBtnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rootRef.addValueEventListener(new ValueEventListener() {
                    public void onDataChange(DataSnapshot snapshot) {
                        String companyName = snapshot.child("fullname").getValue().toString();
                        login();
                        uploadImage(companyName, uid);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Image_Request_Code && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            FilePathUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);
                ImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    // Creating Method to get the selected image file Extension from File Path URI.
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void login() {
        if (typeOfDisability1.isChecked() && typeOfDisability2.isChecked() && typeOfDisability3.isChecked() && typeOfDisabilityMore.isChecked()) {
            typeOfDisability_1 = typeOfDisability1.getText().toString();
            typeOfDisability_2 = typeOfDisability2.getText().toString();
            typeOfDisability_3 = typeOfDisability3.getText().toString();
            typeOfDisability_More = typeOfDisability3.getText().toString();
        } else if (typeOfDisability1.isChecked() && typeOfDisability2.isChecked() && typeOfDisabilityMore.isChecked()) {
            typeOfDisability_1 = typeOfDisability1.getText().toString();
            typeOfDisability_2 = typeOfDisability2.getText().toString();
            typeOfDisability_More = typeOfDisabilityMore.getText().toString();
            typeOfDisability_3 = "";
        } else if (typeOfDisability1.isChecked() && typeOfDisability3.isChecked() && typeOfDisabilityMore.isChecked()) {
            typeOfDisability_1 = typeOfDisability1.getText().toString();
            typeOfDisability_2 = "";
            typeOfDisability_More = typeOfDisabilityMore.getText().toString();
            typeOfDisability_3 = typeOfDisability3.getText().toString();
        } else if (typeOfDisability2.isChecked() && typeOfDisability3.isChecked()&& typeOfDisabilityMore.isChecked()) {
            typeOfDisability_1 = "";
            typeOfDisability_2 = typeOfDisability2.getText().toString();
            typeOfDisability_More =typeOfDisabilityMore.getText().toString();
            typeOfDisability_3 = typeOfDisability3.getText().toString();
        } else if (typeOfDisability2.isChecked() && typeOfDisability3.isChecked()&& typeOfDisability1.isChecked()) {
            typeOfDisability_More = "";
            typeOfDisability_1 = typeOfDisability1.getText().toString();
            typeOfDisability_2 = typeOfDisability2.getText().toString();
            typeOfDisability_3 = typeOfDisability3.getText().toString();
        } else if (typeOfDisability1.isChecked() && typeOfDisability2.isChecked()) {
            typeOfDisability_1 = typeOfDisability1.getText().toString();
            typeOfDisability_2 = typeOfDisability2.getText().toString();
            typeOfDisability_More = "";
            typeOfDisability_3 = "";
        } else if (typeOfDisability1.isChecked() && typeOfDisability3.isChecked()) {
            typeOfDisability_1 = typeOfDisability1.getText().toString();
            typeOfDisability_2 = "";
            typeOfDisability_More = "";
            typeOfDisability_3 = typeOfDisability3.getText().toString();
        } else if (typeOfDisability1.isChecked()&& typeOfDisabilityMore.isChecked()) {
            typeOfDisability_1 = typeOfDisability1.getText().toString();
            typeOfDisability_2 = "";
            typeOfDisability_More = typeOfDisabilityMore.getText().toString();
            typeOfDisability_3 = "";


        } else if (typeOfDisabilityMore.isChecked() && typeOfDisability2.isChecked()) {
            typeOfDisability_1 = "";
            typeOfDisability_2 = typeOfDisability2.getText().toString();
            typeOfDisability_More = typeOfDisabilityMore.getText().toString();
            typeOfDisability_3 = "";
        } else if (typeOfDisabilityMore.isChecked() && typeOfDisability3.isChecked()) {
            typeOfDisability_More = typeOfDisabilityMore.getText().toString();
            typeOfDisability_3 =typeOfDisability3.getText().toString();
            typeOfDisability_1 = "";
            typeOfDisability_2 = "";


        } else if (typeOfDisability3.isChecked() && typeOfDisability2.isChecked()) {
            typeOfDisability_1 = "";
            typeOfDisability_2 = typeOfDisability2.getText().toString();
            typeOfDisability_More = "";
            typeOfDisability_3 =typeOfDisability3.getText().toString();


        } else if (typeOfDisability1.isChecked()) {
            typeOfDisability_1 = typeOfDisability1.getText().toString();
            typeOfDisability_2 = "";
            typeOfDisability_3 = "";
        } else if (typeOfDisability2.isChecked()) {
            typeOfDisability_2 = typeOfDisability2.getText().toString();
            typeOfDisability_1 = "";
            typeOfDisability_3 = "";
        } else if (typeOfDisabilityMore.isChecked()) {
            typeOfDisability_More = typeOfDisabilityMore.getText().toString();
            typeOfDisability_2 = "";
            typeOfDisability_1 = "";
            typeOfDisability_3 = "";
        } else if (typeOfDisability3.isChecked()) {
            typeOfDisability_3 = typeOfDisability3.getText().toString();
            typeOfDisability_1 = "";
            typeOfDisability_2 = "";

        }
    }

    private void uploadImage(final String companyName, final String uid) {
        if (checkEducRequirement.isChecked()){
            zz="true";
        }else{
            zz="false";
        }
        final Intent intent = new Intent(this, a_EmployeeContentMainActivity.class);
        if (FilePathUri != null) {

            //final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Posting...");
            progressDialog.show();

            final StorageReference ref = storageReference.child(Storage_Path+ System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));

            ref.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    String profileImageUrl=task.getResult().toString();
                                    String tempPostTitle = postTitle.getText().toString().trim();
                                    String tempPostDescription = postDescription.getText().toString();
                                    String tempPostLocation = Address;
                                    String ImageUploadId = databaseReference.push().getKey();
                                    rgEducAttainment = findViewById(R.id.rg_educ);
                                    final int selectedId = rgEducAttainment.getCheckedRadioButtonId();
                                    rbEduc = findViewById(selectedId);
                                    educAttainment = rbEduc.getText().toString();

                                    rgWorkExperience = findViewById(R.id.workexperience);
                                    final int selectExperience = rgWorkExperience.getCheckedRadioButtonId();
                                    rbWorkExperience = findViewById(selectExperience);
                                    workExperience = rbWorkExperience.getText().toString();

                                    final Spinner dropdown1 = findViewById(R.id.spinnerExpDate);

                                    String expDate = dropdown1.getSelectedItem().toString();

                                    String tempPermission = permission;

                                    progressDialog.dismiss();

                                    Date currentDate = Calendar.getInstance().getTime();
                                    SimpleDateFormat df = new SimpleDateFormat("MMMM dd, yyyy");
                                    String postDate = df.format(currentDate);
                                    categorySkill = primary_skillsCategory.getSelectedItem().toString();

                                    //Deleted Primary skills and Job Skills from Model Emp_PostJob_Information
                                    Emp_PostJob_Information postJobInfo = new Emp_PostJob_Information(profileImageUrl, tempPostTitle,
                                            tempPostDescription,tempPostLocation,tempPermission, companyName, uid, postDate, city, educAttainment, workExperience, categorySkill, zz);
                                    databaseReference.child(ImageUploadId).setValue(postJobInfo);
                                    databaseReference.child(ImageUploadId).child("yearsOfExperience").setValue(years);
                                    databaseReference.child(ImageUploadId).child("postJobId").setValue(ImageUploadId);

                                    if(workExperience.equals("With Experience")){
                                        years = Integer.parseInt(yearsOfExperience.getText().toString());
                                        databaseReference.child(ImageUploadId).child("yearsOfExperience").setValue(years);
                                    }

                                    if(expDate.equals(week1)) { // working
                                        cal.add(Calendar.WEEK_OF_YEAR, 1);
                                        format.format(cal.getTime());
                                        expDate1 = format.format(cal.getTime());

                                    }else if(expDate.equals(week2)) {
                                        cal.add(Calendar.WEEK_OF_YEAR, 2);
                                        format.format(cal.getTime());
                                        expDate1 = format.format(cal.getTime());

                                    }else if(expDate.equals(week3)) {
                                        cal.add(Calendar.WEEK_OF_YEAR, 3);
                                        format.format(cal.getTime());
                                        expDate1 = format.format(cal.getTime());

                                    }else if(expDate.equals(month1)) {
                                        cal.add(Calendar.MONTH, 1);
                                        format.format(cal.getTime());
                                        expDate1 = format.format(cal.getTime());

                                    }else if(expDate.equals(month2)) {
                                        cal.add(Calendar.MONTH, 2);
                                        format.format(cal.getTime());
                                        expDate1 = format.format(cal.getTime());

                                    } else if(expDate.equals(month3)) {
                                        cal.add(Calendar.MONTH, 3);
                                        format.format(cal.getTime());
                                        expDate1 = format.format(cal.getTime());

                                    } else if(expDate.equals(month4)) {
                                        cal.add(Calendar.MONTH, 4);
                                        format.format(cal.getTime());
                                        expDate1 = format.format(cal.getTime());

                                    } else if(expDate.equals(month5)) {
                                        cal.add(Calendar.MONTH, 5);
                                        format.format(cal.getTime());
                                        expDate1 = format.format(cal.getTime());

                                    } else if(expDate.equals(month6)) {
                                        cal.add(Calendar.MONTH, 6);
                                        format.format(cal.getTime());
                                        expDate1 = format.format(cal.getTime());

                                    } else if(expDate.equals(month7)) {
                                        cal.add(Calendar.MONTH, 7);
                                        format.format(cal.getTime());
                                        expDate1 = format.format(cal.getTime());

                                    } else if(expDate.equals(month8)) {
                                        cal.add(Calendar.MONTH, 8);
                                        format.format(cal.getTime());
                                        expDate1 = format.format(cal.getTime());

                                    } else if(expDate.equals(month9)) {
                                        cal.add(Calendar.MONTH, 9);
                                        format.format(cal.getTime());
                                        expDate1 = format.format(cal.getTime());

                                    } else if(expDate.equals(month10)) {
                                        cal.add(Calendar.MONTH, 10);
                                        format.format(cal.getTime());
                                        expDate1 = format.format(cal.getTime());

                                    } else if(expDate.equals(month11)) {
                                        cal.add(Calendar.MONTH, 11);
                                        format.format(cal.getTime());
                                        expDate1 = format.format(cal.getTime());

                                    }else if(expDate.equals(year1)) {
                                        cal.add(Calendar.MONTH, 12);
                                        format.format(cal.getTime());
                                        expDate1 = format.format(cal.getTime());

                                    }else if(expDate.equals(unli)) {
                                        expDate1 = "unlimited";
                                    }

                                    databaseReference.child(ImageUploadId).child("expDate").setValue(expDate1);

                                    //TYPE OF DISABILITY
                                    if(typeOfDisability1.isChecked()){
                                        databaseReference.child(ImageUploadId).child("typeOfDisability1").setValue(typeOfDisability1.getText().toString());
                                    }
                                    if(typeOfDisability2.isChecked()){
                                        databaseReference.child(ImageUploadId).child("typeOfDisability2").setValue(typeOfDisability2.getText().toString());
                                    }
                                    if(typeOfDisability3.isChecked()){
                                        databaseReference.child(ImageUploadId).child("typeOfDisability3").setValue(typeOfDisability3.getText().toString());
                                    }
                                    if(typeOfDisabilityMore.isChecked()){
                                        databaseReference.child(ImageUploadId).child("typeOfDisabilityMore").setValue(typeOfDisabilityMore.getText().toString());
                                    }
                                    //JOB SKILL
                                    if (jobSkill1.isChecked()){
                                        databaseReference.child(ImageUploadId).child("jobSkill1").setValue(jobSkill1.getText().toString());
                                    }
                                    if (jobSkill2.isChecked()){
                                        databaseReference.child(ImageUploadId).child("jobSkill2").setValue(jobSkill2.getText().toString());
                                    }
                                    if (jobSkill3.isChecked()){
                                        databaseReference.child(ImageUploadId).child("jobSkill3").setValue(jobSkill3.getText().toString());
                                    }
                                    if (jobSkill4.isChecked()){
                                        databaseReference.child(ImageUploadId).child("jobSkill4").setValue(jobSkill4.getText().toString());
                                    }
                                    if (jobSkill5.isChecked()){
                                        databaseReference.child(ImageUploadId).child("jobSkill5").setValue(jobSkill5.getText().toString());
                                    }
                                    if (jobSkill6.isChecked()){
                                        databaseReference.child(ImageUploadId).child("jobSkill6").setValue(jobSkill6.getText().toString());
                                    }
                                    if (jobSkill7.isChecked()){
                                        databaseReference.child(ImageUploadId).child("jobSkill7").setValue(jobSkill7.getText().toString());
                                    }
                                    if (jobSkill8.isChecked()){
                                        databaseReference.child(ImageUploadId).child("jobSkill8").setValue(jobSkill8.getText().toString());
                                    }
                                    if (jobSkill9.isChecked()){
                                        databaseReference.child(ImageUploadId).child("jobSkill9").setValue(jobSkill9.getText().toString());
                                    }
                                    if (jobSkill10.isChecked()){
                                        databaseReference.child(ImageUploadId).child("jobSkill0").setValue(jobSkill10.getText().toString());
                                    }
                                }
                            });
                            startActivity(intent);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Emp_PostJob.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                            progressDialog.setCancelable(false);
                        }
                    });
        }
    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }

}