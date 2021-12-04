package com.philcode.equals.EMP;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Patterns;
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

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
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

public class EMP_ChangeProfile_Activity extends AppCompatActivity implements View.OnClickListener {

    //firebase auth object
    private FirebaseAuth firebaseAuth;
    FirebaseStorage storage;
    StorageReference storageReference;
    private DatabaseReference mDatabase;
    private Spinner spinnerCity;

    // Folder path for Firebase Storage.
    String Storage_Path = "Employer_Reg_Form/";
    private TextView textViewUserEmail, txtImage;
    private Button buttonLogout, buttonSave, btnUpload, buttonUploadEmpID;

    private EditText editCompanyName, editCompanyBackground, editContact, editEmail, editPassword,
            editFirstName, editLastName, editCompanyAddress, confirmPassword;
    private TextInputLayout editEmailError, editPasswordError, confirmPasswordError;
    private ImageView profilePicEMP, empValidID;
    private TextView emailAddressInUse;
    String password, stringConfirmPassword, emailCheck;
    int PICK_IMAGE_REQUEST = 7;
    private Uri filePath;
    //private Uri filePath2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emp_changeprofile);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Employers");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        //editEmailError = findViewById(R.id.textInputLayout3);
        editPasswordError = findViewById(R.id.textInputLayout4);
        confirmPasswordError = findViewById(R.id.textInputLayout5);
        txtImage = txtImage = findViewById(R.id.txtImage);
        txtImage.setVisibility(View.INVISIBLE);
        buttonUploadEmpID = (Button) findViewById(R.id.btn_emp_ID_upload);
        buttonSave = (Button) findViewById(R.id.btnSave);
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



        editFirstName = findViewById(R.id.editEmployerFirstName);
        editFirstName.setOnFocusChangeListener(new View.OnFocusChangeListener(){

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                final String firstname = editFirstName.getText().toString().trim();
                if (!hasFocus) {
                    if (firstname.length() == 0) {
                        editFirstName.setError("Please enter your first name");
                    } else if (!firstname.matches("[a-zA-Z ]+")) {
                        editFirstName.setError("Please enter alphabetical letters only");
                    } else {
                        editFirstName.setError(null);
                    }
                }
            }
        });
        editLastName = findViewById(R.id.editEmployerLastName);
        editLastName.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                final String lastname = editLastName.getText().toString().trim();
                if (!hasFocus) {
                    if (lastname.length() == 0) {
                        editLastName.setError("Please enter your last name");
                    } else if (!lastname.matches("[a-zA-Z ]+")) {
                        editLastName.setError("Please enter alphabetical letters only");
                    } else {
                        editLastName.setError(null);
                    }
                }
            }
        });
        editCompanyName = findViewById(R.id.editCompanyName);
        editCompanyName.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    final String companyName = editCompanyName.getText().toString().trim();
                    if (companyName.length() == 0) {
                        editCompanyName.setError("Please enter the company name");
                    } else {
                        editCompanyName.setError(null);
                    }
                }
            }
        });
        emailAddressInUse = findViewById(R.id.emailAddressInUse);
        editContact = findViewById(R.id.editContact);
        editContact.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    final String contact = editContact.getText().toString().trim();
                    if (contact.length() == 0) {
                        editContact.setError("Please enter contact");
                    } else {
                        editContact.setError(null);
                    }
                }
            }
        });
        editCompanyBackground = findViewById(R.id.editCompanyBackground);
        editCompanyBackground.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    final String companyBackground = editCompanyBackground.getText().toString().trim();
                    if (companyBackground.length() == 0) {
                        editCompanyBackground.setError("Please enter company background");
                    } else {
                        editCompanyBackground.setError(null);
                    }
                }
            }
        });

        spinnerCity = findViewById(R.id.spinnerCity);
        editCompanyAddress = findViewById(R.id.editTextAddress);
        editCompanyAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    final String companyAddress = editCompanyAddress.getText().toString().trim();
                    if (companyAddress.length() == 0) {
                        editCompanyAddress.setError("Please enter address");
                    } else {
                        editCompanyAddress.setError(null);
                    }
                }
            }
        });

        final FirebaseUser user = firebaseAuth.getCurrentUser();
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentFirebaseUser.getUid();
        final String userz = user.getUid();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("Employers").child(userz);
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String companyAddress = dataSnapshot.child("companyaddress").getValue().toString();
                String companybg = dataSnapshot.child("companybg").getValue().toString();
                String companycity = dataSnapshot.child("companycity").getValue().toString();
                String contact = dataSnapshot.child("contact").getValue().toString();
                String empValidID1 = dataSnapshot.child("empValidID").getValue().toString();
                String firstname = dataSnapshot.child("firstname").getValue().toString();
                String fullname = dataSnapshot.child("fullname").getValue().toString();
                String lastname = dataSnapshot.child("lastname").getValue().toString();
                String empvalidid = dataSnapshot.child("empValidID").getValue().toString();

                editCompanyAddress.setText(companyAddress);
                editCompanyBackground.setText(companybg);
                editContact.setText(contact);
                editFirstName.setText(firstname);
                editCompanyName.setText(fullname);
                editLastName.setText(lastname);
                //Glide.with(getApplicationContext()).load(empValidID1).into(empValidID);
                txtImage.setText(empvalidid);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            finish();
            startActivity(new Intent(getApplicationContext(), EMP_EditProfile_Activity.class));
        }
        return super.onKeyDown(keyCode, event);
    }


    private void uploadImage() {
        final Intent intent = new Intent(this, EMP_EditProfile_Activity.class);
        if (filePath != null) {
            final String firstname = editFirstName.getText().toString().trim();
            final String lastname = editLastName.getText().toString().trim();
            final String fullname = editCompanyName.getText().toString().trim();
            final String typeStatus = "EMPApproved";
            //  final String fullname = editCompanyName.getText().toString().trim();
            final String companybg = editCompanyBackground.getText().toString().trim();
            final String contact = editContact.getText().toString().trim();
            final String companyAddress = editCompanyAddress.getText().toString().trim();
            final String txtImage1 = txtImage.getText().toString().trim();
            final String companyCity = spinnerCity.getSelectedItem().toString().trim();

            if (firstname.length() == 0) {
                //           editFirstName.requestFocus();
                editFirstName.setError("Please enter your first name");
                editFirstName.requestFocus();
            } else if (!firstname.matches("[a-zA-Z ]+")) {
                //         editFirstName.requestFocus();
                editFirstName.setError("Please enter alphabetical letters only");
            } else if (lastname.length() == 0) {
                //       editLastName.requestFocus();
                editLastName.setError("Please enter your last name");
                editLastName.requestFocus();
            } else if (!lastname.matches("[a-zA-Z ]+")) {
                //     editLastName.requestFocus();
                editLastName.setError("Please enter alphabetical letters only");
            } else if (companyAddress.length() == 0) {
                //    editTextAddress.requestFocus();
                editCompanyAddress.setError("Please enter your address");
                editCompanyAddress.requestFocus();
            } else if (contact.length() == 0) {
                //  editContact.requestFocus();
                editContact.setError("Please enter your contact number");
                editContact.requestFocus();
            }else if(companybg.length()==0){
                editCompanyBackground.setError("Please enter your company overview");
                editCompanyBackground.requestFocus();
            } else if (TextUtils.isEmpty(companybg)) {
                Toast.makeText(this, "Please enter your company", Toast.LENGTH_LONG).show();
                return;
            } else if (TextUtils.isEmpty(contact)) {
                Toast.makeText(this, "Please enter your contact", Toast.LENGTH_LONG).show();
                return;
            } else if (TextUtils.isEmpty(firstname)) {
                Toast.makeText(this, "Please enter your first name", Toast.LENGTH_LONG).show();
                return;
            } else if (TextUtils.isEmpty(lastname)) {
                Toast.makeText(this, "Please enter your last name", Toast.LENGTH_LONG).show();
                return;
            } else {

                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.show();
                progressDialog.setCancelable(false);
                final StorageReference ref = storageReference.child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(filePath));
                ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {

                                progressDialog.dismiss();


                                final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                               /* EMP_EditProfile_Information EmpInfo = new EMP_EditProfile_Information( typeStatus, firstname, lastname, fullname, companybg,
                                        contact, empValidID, companyAddress, companyCity);*/
                                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("Employers").child(firebaseAuth.getCurrentUser().getUid());
                                final String empValidID11 = task.getResult().toString();

                                if(task.isSuccessful()){
                                    rootRef.child("empValidID").setValue(empValidID11);
                                    rootRef.child("companyaddress").setValue(companyAddress);
                                    rootRef.child("companybg").setValue(companybg);
                                    rootRef.child("companycity").setValue(companyCity);
                                    rootRef.child("contact").setValue(contact);
                                    rootRef.child("firstname").setValue(firstname);
                                    rootRef.child("fullname").setValue(fullname);
                                    rootRef.child("lastname").setValue(lastname);
                                }else{
                                    rootRef.child("empValidID").setValue(txtImage1);
                                    rootRef.child("companyaddress").setValue(companyAddress);
                                    rootRef.child("companybg").setValue(companybg);
                                    rootRef.child("companycity").setValue(companyCity);
                                    rootRef.child("contact").setValue(contact);
                                    rootRef.child("firstname").setValue(firstname);
                                    rootRef.child("fullname").setValue(fullname);
                                    rootRef.child("lastname").setValue(lastname);

                                }
                                Toast.makeText(EMP_ChangeProfile_Activity.this, "Profile is updated successfully.", Toast.LENGTH_SHORT).show();
                            }
                            // }
                        });
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        progressDialog.setMessage("Loading " + (int) progress + "%");
                        progressDialog.setCancelable(false);
                    }
                });
            }
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
    }
}