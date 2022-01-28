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
import android.widget.ArrayAdapter;
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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
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
import java.util.ArrayList;


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
    private MaterialButton buttonSave, btnUpload;
    private Spinner spinnerCity;

    private TextInputEditText editFirstName, editLastName, editTextAddress, editContact,  editEmail, editPassword, editconfirmPassword;
    private TextInputLayout pwd_enterEmail_layout, confirmPasswordError, editPasswordError;
    private TextView emailAddressInUse;
    private String emailFromFb;
    private String[] cities;


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

        buttonSave = (MaterialButton) findViewById(R.id.buttonSave);
        pwd_enterEmail_layout = findViewById(R.id.pwd_enterEmail_layout);
        emailAddressInUse = findViewById(R.id.emailAddressInUse);
        confirmPasswordError = findViewById(R.id.textInputLayout5);
        editPasswordError = findViewById(R.id.textInputLayout4);
        spinnerCity = findViewById(R.id.spinnerCity);
        cities = new String[146];

        cities = getResources().getStringArray(R.array.City);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, cities);
        spinnerCity.setAdapter(adapter);

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

        editEmail = findViewById(R.id.editEmail);
        editEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String email = editEmail.getText().toString();
                    if (!(email == null || email.equals(""))) {
                        if (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                            checkEmailExistsOrNot(email);
                        } else {
                            pwd_enterEmail_layout.setError("Invalid email");
                        }
                    }
                }
            }
        });

        editPassword = findViewById(R.id.editPassword);
        editPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                /* When focus is lost check that the text field
                 * has valid values.
                 */
                if (!hasFocus) {
                    password = editPassword.getText().toString().trim();
                    if (password.length() == 0) {
                        editPasswordError.setError("Please enter a password");
                    } else {
                        editPasswordError.setError(null);
                    }
                    if (password.length() <= 5) {
                        editPasswordError.setError("Your password must contain at least 6 characters");
                    } else {
                        editPasswordError.setError(null);
                    }
                }
            }
        });

        editconfirmPassword = findViewById(R.id.confirmPassword);
        editconfirmPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                /* When focus is lost check that the text field
                 * has valid values.
                 */
                if (!hasFocus) {
                    stringConfirmPassword = editconfirmPassword.getText().toString().trim();
                    if (!(stringConfirmPassword.equals(password))) {
                        confirmPasswordError.setError("Password doesn't match");
                    } else {
                        confirmPasswordError.setError(null);
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
                emailFromFb = dataSnapshot.child("email").getValue().toString();
                String firstName = dataSnapshot.child("firstName").getValue().toString();
                String lastName = dataSnapshot.child("lastName").getValue().toString();
                String contact = dataSnapshot.child("contact").getValue().toString();
                String address = dataSnapshot.child("address").getValue().toString();
                String city = dataSnapshot.child("city").getValue().toString();

                editFirstName.setText(firstName);
                editLastName.setText(lastName);
                editContact.setText(contact);
                editTextAddress.setText(address);
                spinnerCity.setSelection(adapter.getPosition(city));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = editPassword.getText().toString().trim();
                String confirmPassword = editconfirmPassword.getText().toString().trim();

                if(password.equals(confirmPassword)){
                    saveChanges(userz);
                }else{
                    Toast.makeText(PWD_EditProfile.this, "Password does not match.", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void updateEmail(String email, DatabaseReference rootRef){
        firebaseAuth.getCurrentUser().updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        rootRef.child("email").setValue(email);
                        Toast.makeText(PWD_EditProfile.this, "Email is successfully updated.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    public void checkEmailExistsOrNot(String emails) {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            internetConnection = true;
        } else
            internetConnection = false;

        if (internetConnection == true) {
            final FirebaseAuth firebaseauth = FirebaseAuth.getInstance();
            firebaseauth.fetchSignInMethodsForEmail(emails).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                @Override
                public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                    if (task.getResult().getSignInMethods().size() == 0) {
                        emailAddressInUse.setVisibility(View.GONE);
                        pwd_enterEmail_layout.setError(null);
                        emailCheck = "huhuz";
                    } else {
                        emailAddressInUse.setError("Email address is already in use");
                        emailCheck = "hehez";
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                }
            });
        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(PWD_EditProfile.this);
            alert.setMessage("Please check your internet connection and try again").setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final Intent intent = new Intent(getApplicationContext(), PWD_RegisterActivity.class);
                            startActivity(intent);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        }
                    });
            AlertDialog alertDialog = alert.create();
            alertDialog.setTitle("Network Connection");
            alertDialog.show();
        }

    }
    public void saveChanges(String userz){
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("PWD").child(userz);
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        String confirmPassword = editconfirmPassword.getText().toString().trim();
        String firstname = editFirstName.getText().toString();
        String lastname = editLastName.getText().toString();
        String contact = editContact.getText().toString();
        String address = editTextAddress.getText().toString();
        String spinner = spinnerCity.getSelectedItem().toString();

        if(email.isEmpty() || email.equals(emailFromFb)){
            //
        }else{
            updateEmail(email, rootRef);
        }

        if((!password.isEmpty() || !confirmPassword.isEmpty())){
                firebaseAuth.getCurrentUser().updatePassword(password);
                Toast.makeText(PWD_EditProfile.this, "Password is changed successfully.", Toast.LENGTH_SHORT).show();
        }

        rootRef.child("firstName").setValue(firstname);
        rootRef.child("lastName").setValue(lastname);
        rootRef.child("contact").setValue(contact);
        rootRef.child("address").setValue(address);
        rootRef.child("city").setValue(spinner);

        startActivity(new Intent(PWD_EditProfile.this, PWD_EditProfile_ViewActivity.class));
        Toast.makeText(PWD_EditProfile.this, "Changes is successfully saved.", Toast.LENGTH_SHORT).show();
    }
/*
    private void uploadImage() {
        final Intent intent = new Intent(this, PWD_EditProfile_ViewActivity.class);
        currentUser = firebaseAuth.getCurrentUser().getUid();
        PWD_UserInformation PWDInfo = new PWD_UserInformation(email, typeStatus, firstname, lastname, address, city, contact, pwdIDnum);
    }*/

}