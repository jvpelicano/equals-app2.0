package com.philcode.equals;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.philcode.equals.EMP.EMP_EditProfile_Activity;
import com.philcode.equals.PWD.PWD_EditProfile;
import com.philcode.equals.PWD.PWD_RegisterActivity;

public class EMP_EditLoginCredentials_Activity extends AppCompatActivity {

    private TextInputEditText editEmail, editPassword, editconfirmPassword, editTextAddress;
    private TextInputLayout editEmailError, editPasswordError, confirmPasswordError;
    private TextView emailAddressInUse;

    boolean internetConnection = false;
    private String emailCheck, password, stringConfirmPassword, emailFromFb;
    private MaterialButton buttonSave;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emp_editlogincredentials_activity);

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        final String userz = user.getUid();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("PWD").child(userz);

        buttonSave = findViewById(R.id.buttonSaveChanges);
        editEmailError = findViewById(R.id.emp_enterEmail_layout);
        editPasswordError = findViewById(R.id.emp_editConfrimPassword_layout);
        confirmPasswordError = findViewById(R.id.emp_editConfrimPassword_layout);
        emailAddressInUse = findViewById(R.id.emailAddressInUse);

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

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = editPassword.getText().toString().trim();
                String confirmPassword = editconfirmPassword.getText().toString().trim();

                if(password.equals(confirmPassword)){
                    saveChanges(userz);
                }else{
                    Toast.makeText(EMP_EditLoginCredentials_Activity.this, "Password does not match.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveChanges(String userz) {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("PWD").child(userz);
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        String confirmPassword = editconfirmPassword.getText().toString().trim();

        if(email.isEmpty() || email.equals(emailFromFb)){
            //
        }else{
            updateEmail(email, rootRef);
        }

        if((!password.isEmpty() || !confirmPassword.isEmpty())){
            firebaseAuth.getCurrentUser().updatePassword(password);
            Toast.makeText(EMP_EditLoginCredentials_Activity.this, "Password is changed successfully.", Toast.LENGTH_SHORT).show();
        }
        startActivity(new Intent(EMP_EditLoginCredentials_Activity.this, EMP_EditProfile_Activity.class));
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
            AlertDialog.Builder alert = new AlertDialog.Builder(EMP_EditLoginCredentials_Activity.this);
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
    public void updateEmail(String email, DatabaseReference rootRef){
        firebaseAuth.getCurrentUser().updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        rootRef.child("email").setValue(email);
                        Toast.makeText(EMP_EditLoginCredentials_Activity.this, "Email is successfully updated.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}