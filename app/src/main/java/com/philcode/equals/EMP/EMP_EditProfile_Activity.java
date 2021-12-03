package com.philcode.equals.EMP;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
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

public class EMP_EditProfile_Activity extends AppCompatActivity {

    //firebase auth object
    private FirebaseAuth firebaseAuth;
    FirebaseStorage storage;
    StorageReference storageReference;
    private DatabaseReference mDatabase;
    private EditText spinnerCity;

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
        setContentView(R.layout.emp_editprofile);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Employers");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        //editEmailError = findViewById(R.id.textInputLayout3);
        editPasswordError = findViewById(R.id.textInputLayout4);
        confirmPasswordError = findViewById(R.id.textInputLayout5);
        txtImage = txtImage = findViewById(R.id.txtImage);
        txtImage.setVisibility(View.INVISIBLE);
        buttonSave = (Button) findViewById(R.id.btnEditProfile);
        empValidID = findViewById(R.id.emp_ID);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EMP_EditProfile_Activity.this, EMP_ChangeProfile_Activity.class);
                startActivity(intent);
            }
        });




        editFirstName = findViewById(R.id.editEmployerFirstName);
        editFirstName.setKeyListener(null);

        editLastName = findViewById(R.id.editEmployerLastName);
        editLastName.setKeyListener(null);

        editCompanyName = findViewById(R.id.editCompanyName);
        editCompanyName.setKeyListener(null);

        emailAddressInUse = findViewById(R.id.emailAddressInUse);
        editContact = findViewById(R.id.editContact);
        editContact.setKeyListener(null);

        editCompanyBackground = findViewById(R.id.editCompanyBackground);
        editCompanyBackground.setKeyListener(null);


        //spinnerCity = findViewById(R.id.spinnerCity);
        editCompanyAddress = findViewById(R.id.editTextAddress);
        editCompanyAddress.setKeyListener(null);


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

                editCompanyAddress.setText(companyAddress+" "+companycity);
                editCompanyBackground.setText(companybg);
                editContact.setText(contact);
                editFirstName.setText(firstname);
                editCompanyName.setText(fullname);
                editLastName.setText(lastname);
                //spinnerCity.setText(companycity);
                Glide.with(getApplicationContext()).load(empValidID1).into(empValidID);
                txtImage.setText(empvalidid);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            finish();
            startActivity(new Intent(getApplicationContext(), a_EmployeeContentMainActivity.class));
        }
        return super.onKeyDown(keyCode, event);
    }



}