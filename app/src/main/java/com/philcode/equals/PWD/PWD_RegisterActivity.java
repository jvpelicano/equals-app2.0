package com.philcode.equals.PWD;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Patterns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.philcode.equals.EMP.LoginActivity_emp;
import com.philcode.equals.EMP.a_EmployeeContentMainActivity;
import com.philcode.equals.PrivacyPolicyPDFViewer;
import com.philcode.equals.R;

import java.io.IOException;


public class PWD_RegisterActivity extends AppCompatActivity implements View.OnClickListener {

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
    private Button buttonSave, btn_pwd_ID_upload;
    private Spinner spinnerCity;

    private TextInputEditText editFirstName, editLastName, editContact, editEmail, editPassword, confirmPassword, editTextAddress;
    private TextInputLayout editEmailError, editPasswordError, confirmPasswordError;
    private TextView emailAddressInUse;
    private ImageView imagePWD;
    int PICK_IMAGE_REQUEST = 7;
    private Uri filePath;
    boolean valid;
    private CheckBox checkPrivacy;

    boolean internetConnection = false;
    String password, stringConfirmPassword, emailCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pwd_activity_register);
        firebaseAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("PWD");
        firebaseAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();


        //btnUpload = (Button) findViewById(R.id.btn_pwd_choose_idcard);
        buttonSave = (Button) findViewById(R.id.buttonSave);
        editEmailError = findViewById(R.id.textInputLayout3);
        editPasswordError = findViewById(R.id.textInputLayout4);
        confirmPasswordError = findViewById(R.id.textInputLayout5);
        emailAddressInUse = findViewById(R.id.emailAddressInUse);
        imagePWD = findViewById(R.id.pwd_idcardPic);
        editTextAddress = findViewById(R.id.editTextAddress);
        spinnerCity = findViewById(R.id.spinnerCity);
        editContact = findViewById(R.id.editContact);
        btn_pwd_ID_upload = findViewById(R.id.btn_pwd_ID_upload);

        btn_pwd_ID_upload.setOnClickListener(new View.OnClickListener() {
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
        editEmail = findViewById(R.id.editEmail);
        editEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                /* When focus is lost check that the text field
                 * has valid values.
                 */
                if (!hasFocus) {
                    String email = editEmail.getText().toString();
                    if (!(email == null || email.equals(""))) {
                        if (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                            checkEmailExistsOrNot(email);
                        } else {
                            editEmailError.setError("Invalid email");
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


        confirmPassword = findViewById(R.id.confirmPassword);
        confirmPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                /* When focus is lost check that the text field
                 * has valid values.
                 */
                if (!hasFocus) {
                    stringConfirmPassword = confirmPassword.getText().toString().trim();
                    if (!(stringConfirmPassword.equals(password))) {
                        confirmPasswordError.setError("Password doesn't match");
                    } else {
                        confirmPasswordError.setError(null);
                    }
                }
            }
        });

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


        //Privacy Policy
        checkPrivacy = findViewById(R.id.privacypolicy);
        checkPrivacy.setChecked(false);


        String text = "I have read and agree to the Equals Privacy Policy";
        SpannableString ss = new SpannableString(text);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent = new Intent(PWD_RegisterActivity.this, PrivacyPolicyPDFViewer.class);
                startActivity(intent);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.BLUE);
            }
        };

        ss.setSpan(clickableSpan,29,50, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        checkPrivacy.setText(ss);
        checkPrivacy.setMovementMethod(LinkMovementMethod.getInstance());

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });


    }

    public String GetFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
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
                        editEmailError.setError(null);
                        emailCheck = "huhuz";
                    } else {
                        editEmailError.setError("Email address is already in use");
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
            AlertDialog.Builder alert = new AlertDialog.Builder(PWD_RegisterActivity.this);
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


    private void uploadImage() {
        final Intent intent = new Intent(this, PWD_RegisterActivity2.class);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            internetConnection = true;
        } else
            internetConnection = false;
        if (internetConnection == true) {
            if (filePath != null) {
//                final String typeStatus = "PWDApproved";  // for testing app 09.12.2021
                final String typeStatus = "PWDPending"; //original status when registering 09.12.2021
                final String email = editEmail.getText().toString().trim();
                final String firstname = editFirstName.getText().toString().trim();
                final String lastname = editLastName.getText().toString().trim();
                final String address = editTextAddress.getText().toString().trim();
                final String city = spinnerCity.getSelectedItem().toString().trim();
                final String contact = editContact.getText().toString().trim();
                final ProgressDialog progressDialog = new ProgressDialog(this);
                final String stringConfirmPassword1 = confirmPassword.getText().toString().trim();
                if (emailCheck == "hehez") {
                    Toast.makeText(getApplicationContext(), emailCheck, Toast.LENGTH_LONG);
                    editEmailError.setError("Email address is already in use");
                } else if (email.length() == 0) {
//                editEmail.requestFocus();
                    editEmail.setError("Please enter your email");
                } else if (password.length() == 0) {
                    //               editPassword.requestFocus();
                    editPassword.setError("Please enter a password");
                } else if (password.length() <= 5) {
                    //             editPassword.requestFocus();
                    editPassword.setError("Your password must contain at least 6 characters");
                } else if (!(stringConfirmPassword1.equals(password))) {
                    confirmPasswordError.setError("Password doesn't match");
                } else if (firstname.length() == 0) {
                    //           editFirstName.requestFocus();
                    editFirstName.setError("Please enter your first name");
                } else if (!firstname.matches("[a-zA-Z ]+")) {
                    //         editFirstName.requestFocus();
                    editFirstName.setError("Please enter alphabetical letters only");
                } else if (lastname.length() == 0) {
                    //       editLastName.requestFocus();
                    editLastName.setError("Please enter your last name");
                } else if (!lastname.matches("[a-zA-Z ]+")) {
                    //     editLastName.requestFocus();
                    editLastName.setError("Please enter alphabetical letters only");
                } else if (address.length() == 0) {
                    //    editTextAddress.requestFocus();
                    editTextAddress.setError("Please enter your address");
                } else if (contact.length() == 0) {
                    //  editContact.requestFocus();
                    editContact.setError("Please enter your contact number");
                } else if (TextUtils.isEmpty(firstname)) {
                    Toast.makeText(PWD_RegisterActivity.this, "Please enter your first name", Toast.LENGTH_LONG).show();
                    return;
                } else if (TextUtils.isEmpty(lastname)) {
                    Toast.makeText(PWD_RegisterActivity.this, "Please enter your last name", Toast.LENGTH_LONG).show();
                    return;
                } else if (TextUtils.isEmpty(contact)) {
                    Toast.makeText(PWD_RegisterActivity.this, "Please enter your contact", Toast.LENGTH_LONG).show();
                    return;
                } else if (TextUtils.isEmpty(address)) {
                    Toast.makeText(PWD_RegisterActivity.this, "Please enter your address", Toast.LENGTH_LONG).show();
                    return;
                } else if (TextUtils.isEmpty(email)) {
                    Toast.makeText(PWD_RegisterActivity.this, "Please enter email", Toast.LENGTH_LONG).show();
                    return;
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(PWD_RegisterActivity.this, "Please enter password", Toast.LENGTH_LONG).show();
                    return;
                }else if(!checkPrivacy.isChecked()) {
                    checkPrivacy.setError("Please check the checkbox first");
                    checkPrivacy.requestFocus();
                    Toast.makeText(PWD_RegisterActivity.this, "Please confirm that you have read the Equals Privacy Policy", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    final StorageReference ref = storageReference.child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(filePath));
                                    ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Uri> task_URI) {
                                                    final String pwdID_URI = task_URI.getResult().toString();
                                                    if (task.isSuccessful()) {
                                                        currentUser = firebaseAuth.getCurrentUser().getUid();
                                                        PWD_UserInformation PWDInfo = new PWD_UserInformation(email, typeStatus, firstname, lastname, address, city, contact, pwdID_URI);
                                                        FirebaseDatabase.getInstance().getReference("PWD").child(currentUser).setValue(PWDInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                startActivity(intent);
                                                            }
                                                        });
                                                    } else {
                                                        Toast.makeText(PWD_RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                                }
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
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(PWD_RegisterActivity.this, "Failed:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }else{
            AlertDialog.Builder alert =  new AlertDialog.Builder(PWD_RegisterActivity.this);
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


    protected  void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imagePWD.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onClick(View view) {
    }

}