package com.philcode.equals.EMP;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import com.github.mmin18.widget.RealtimeBlurView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    Boolean isOpen = false;
    RelativeLayout mainLayout;

    // Folder path for Firebase Storage.
    String Storage_Path = "Employer_Reg_Form/";


    private TextView editCompanyName, editCompanyBackground, editContact, editEmail, editPassword,
            editFirstName, editLastName, editCompanyAddress, confirmPassword, textview_personalInfo, textview_jobInfo;
    private TextInputLayout editEmailError, editPasswordError, confirmPasswordError;
    private ImageView profilePicEMP, empValidID;
    private TextView emailAddressInUse;
    private FloatingActionButton fab_main, fab1_personalInfo, fab2_jobInfo, fab3_addWorkExpInfo;
    private Animation fab_open, fab_close, fab_clock, fab_anticlock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emp_view_profile);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Employers");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        editPasswordError = findViewById(R.id.textInputLayout4);
        confirmPasswordError = findViewById(R.id.textInputLayout5);
        empValidID = findViewById(R.id.emp_ID);
        mainLayout = findViewById(R.id.mainLayout);


        //animation
        fab_main = findViewById(R.id.fab);
        fab1_personalInfo = findViewById(R.id.btnEditProfile);
        fab2_jobInfo = findViewById(R.id.btnEditLoginCredentials);
        textview_personalInfo = findViewById(R.id.textview_personalInfo);
        textview_jobInfo = findViewById(R.id.textview_jobQualifications);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_clock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_clock);
        fab_anticlock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_anticlock);


        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isOpen) {
                    textview_jobInfo.setVisibility(View.INVISIBLE);
                    textview_personalInfo.setVisibility(View.INVISIBLE);
                    fab2_jobInfo.startAnimation(fab_close);
                    fab1_personalInfo.startAnimation(fab_close);
                    fab_main.startAnimation(fab_anticlock);
                    fab2_jobInfo.setClickable(false);
                    fab1_personalInfo.setClickable(false);
                    isOpen = false;

                } else {
                    textview_jobInfo.setVisibility(View.VISIBLE);
                    textview_personalInfo.setVisibility(View.VISIBLE);
                    fab2_jobInfo.startAnimation(fab_open);
                    fab1_personalInfo.startAnimation(fab_open);
                    fab_main.startAnimation(fab_clock);
                    fab2_jobInfo.setClickable(true);
                    fab1_personalInfo.setClickable(true);
                    isOpen = true;

                }


            }
        });
        fab1_personalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EMP_EditProfile_Activity.this, EMP_ChangeProfile_Activity.class);
                startActivity(intent);
            }
        });
        fab2_jobInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EMP_EditProfile_Activity.this, EMP_EditLoginCredentials_Activity.class));
            }
        });

        editFirstName = findViewById(R.id.editEmployerFirstName);
        editFirstName.setKeyListener(null);

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

                editCompanyAddress.setText(companyAddress+" "+companycity);
                editCompanyBackground.setText(companybg);
                editContact.setText(contact);
                editFirstName.setText(firstname + " " + lastname);
                editCompanyName.setText(fullname);
                Glide.with(getApplicationContext()).load(empValidID1).into(empValidID);


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