package com.philcode.equals.EMP;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.philcode.equals.R;

import java.io.IOException;

public class EMP_Profile extends AppCompatActivity implements View.OnClickListener {

    //firebase auth object
    private FirebaseAuth firebaseAuth;
    FirebaseStorage storage;
    StorageReference storageReference;
    private DatabaseReference mDatabase;

    // Folder path for Firebase Storage.
    String Storage_Path = "Employer_Reg_Form/";
    private TextView textViewUserEmail;
    private Button buttonLogout, buttonSave, btnUpload, buttonUploadEmpID;

    private EditText  editFullname, editTextAddress, editContact;
    private Spinner ddspinner;
    private ImageView profilePicEMP, empValidID;
    int PICK_IMAGE_REQUEST = 7;
    private Uri filePath;
    //private Uri filePath2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emp_profile);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Employers");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null){
            //closing this activity
            finish();
            //starting login activity
            startActivity(new Intent(this, LoginActivity_emp.class));
        }
        buttonUploadEmpID = (Button) findViewById(R.id.btn_emp_ID_upload);
       
        buttonSave = (Button) findViewById(R.id.btnEditProfile);
        empValidID = findViewById(R.id.emp_ID);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
        buttonUploadEmpID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creating intent.
                Intent intent = new Intent();

                // Setting intent type as image to select image from phone storage.
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Please Select Image"), PICK_IMAGE_REQUEST);
            }
        });
        editFullname = findViewById(R.id.editCompanyName);
        editContact = findViewById(R.id.editContact);
        editTextAddress = findViewById(R.id.editTextAddress);

        final DatabaseReference typeStatusEMP = FirebaseDatabase.getInstance().getReference("Employers").child(firebaseAuth.getUid());
        typeStatusEMP.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String fullname = dataSnapshot.child("fullname").getValue().toString();
                if(fullname.length()==0){
                    editFullname.requestFocus();
                    editFullname.setError("Please enter a full name");
                }
                else if(!fullname.matches("[a-zA-Z ]+")){
                    editFullname.requestFocus();
                    editFullname.setError("Please enter alphabetical letters only");
                }


                String address = dataSnapshot.child("address").getValue().toString();
                if(address.length()==0){
                    editTextAddress.requestFocus();
                    editTextAddress.setError("Please enter an address");
                }

                String contact = dataSnapshot.child("contact").getValue().toString();
                if(contact.length()==0){
                    editContact.requestFocus();
                    editContact.setError("Please enter a contact");
                }

                String empValidID1 = dataSnapshot.child("empValidID").getValue().toString();

                editFullname.setText(fullname);
                editTextAddress.setText(address);
                editContact.setText(contact);
                //Glide.with(getApplicationContext()).load(empValidID1).into(empValidID);

               /* Glide.with(PWD_EditProfile.this)
                        .asBitmap()
                        .load(pwdPic)
                        .into(imagePWD);*/
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(EMP_Profile.this, databaseError.getCode(),
                        Toast.LENGTH_LONG).show();
            }
        });
//        String[] fields1 = {"Others",
//                "Accountancy, Banking and Finance", "Broadcasting Media",
//                "Business Consulting & Management",
//                "Charity & Voluntary",
//                "Creative Arts & Design",
//                "Education",
//                "Energy & Utilities",
//                "Health care",
//                "Hospitality & Events Management",
//                "Information Technology",
//                "Manufacturing",
//                "Marketing & Public Relations",
//                "Property Construction",
//                "Purchasing",
//                "Public Governance",
//                "Real Estate Development",
//                "Recruitment & HR",
//                "Retail",
//                "Sales & Marketing",
//                "Science and Pharmaceuticals",
//                "Social Care",
//                "Sports",
//                "Tourism",
//                "Transport & Logistics"};
//        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, fields1);
//        ddspinner.setAdapter(adapter1);
    }
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            finish();
            startActivity(new Intent(getApplicationContext(), a_EmployeeContentMainActivity.class));
        }
        return super.onKeyDown(keyCode, event);
    }

    private void uploadImage(){
        final Intent intent = new Intent(this, a_EmployeeContentMainActivity.class);
        if(filePath  != null){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.show();
            final StorageReference ref = storageReference.child(Storage_Path+ System.currentTimeMillis() + "." + GetFileExtension(filePath));
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            String user_id = firebaseAuth.getCurrentUser().getUid();
                            DatabaseReference current_user_db = mDatabase.child(user_id);
                            String typeStatus = "EMPApproved";
                            String fullname = editFullname.getText().toString().trim();
                            String address = editTextAddress.getText().toString().trim();
                            String specialization = ddspinner.getSelectedItem().toString().trim();
                            String contact = editContact.getText().toString().trim();
                            String empValidID = task.getResult().toString();
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Information saved", Toast.LENGTH_LONG).show();
                            @SuppressWarnings("VisibleForTests")
                            EmployeeInformation2 EmpInfo = new EmployeeInformation2(typeStatus, fullname, address,
                                    contact, empValidID);
                            //Current User
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            mDatabase.child(user.getUid()).setValue(EmpInfo);
                        }
                    });
                    startActivity(intent);
                }
            }).addOnFailureListener(new OnFailureListener(){
                @Override
                public void onFailure(@NonNull Exception e){
                    progressDialog.dismiss();
                    Toast.makeText(EMP_Profile.this, "Failed" +e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Loading "+(int)progress+"%");
                }
            });
        }
    }
    protected  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data !=null && data.getData() != null){
            filePath = data.getData();
            try{
                Bitmap bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                empValidID.setImageBitmap(bitmap1);

            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onClick(View view) {
        //if logout is pressed
        if(view == buttonLogout){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity_emp.class));
        }
    }
}