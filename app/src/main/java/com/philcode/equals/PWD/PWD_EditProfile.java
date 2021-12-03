package com.philcode.equals.PWD;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


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


public class PWD_EditProfile extends AppCompatActivity  {

    //firebase auth object
    private FirebaseAuth firebaseAuth;
    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference databaseReference, mDatabase;
    String currentUser;
    String x;

    // Folder path for Firebase Storage.
    String Storage_Path = "PWD_Reg_Form/";
    // Root Database Name for Firebase Database.


    //view objects
    private Button buttonSave, btnUpload;
    private Spinner spinnerCity;

    private EditText editFirstName, editLastName, editTextAddress, editContact;
    TextView editEmail;


    boolean valid;

    boolean internetConnection = false;
    String password, stringConfirmPassword, emailCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pwd_editprofile1);
        firebaseAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("PWD");
        firebaseAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();


        btnUpload = (Button) findViewById(R.id.btn_pwd_choose_idcard);
        buttonSave = (Button) findViewById(R.id.buttonSave);


        editEmail = findViewById(R.id.editEmail);

        editFirstName = findViewById(R.id.editFirstName);
        editFirstName.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    final String firstname = editFirstName.getText().toString().trim();
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
        editLastName = findViewById(R.id.editLastName);
        editLastName.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    final String lastname = editLastName.getText().toString().trim();
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

        editTextAddress = findViewById(R.id.editTextAddress);
        spinnerCity = findViewById(R.id.spinnerCity);
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

        FirebaseAuth firebaseAuth;
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentFirebaseUser.getUid();
        final String userz = user.getUid();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("PWD").child(userz);
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String email = dataSnapshot.child("email").getValue().toString();
                String firstName = dataSnapshot.child("firstName").getValue().toString();
                String lastName = dataSnapshot.child("lastName").getValue().toString();
                String contact = dataSnapshot.child("contact").getValue().toString();
                String address = dataSnapshot.child("address").getValue().toString();

                editEmail.setText(email);
                editFirstName.setText(firstName);
                editLastName.setText(lastName);
                editContact.setText(contact);
                editTextAddress.setText(address);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("PWD").child(userz);
                String firstname = editFirstName.getText().toString();
                String lastname = editLastName.getText().toString();
                String contact = editContact.getText().toString();
                String address = editTextAddress.getText().toString();
                String spinner = spinnerCity.getSelectedItem().toString();

                rootRef.child("firstName").setValue(firstname);
                rootRef.child("lastName").setValue(lastname);
                rootRef.child("contact").setValue(contact);
                rootRef.child("address").setValue(address);
                rootRef.child("city").setValue(spinner);


            }
        });




    }

/*
    private void uploadImage() {
        final Intent intent = new Intent(this, PWD_EditProfile_ViewActivity.class);
        currentUser = firebaseAuth.getCurrentUser().getUid();
        PWD_UserInformation PWDInfo = new PWD_UserInformation(email, typeStatus, firstname, lastname, address, city, contact, pwdIDnum);
    }*/

}