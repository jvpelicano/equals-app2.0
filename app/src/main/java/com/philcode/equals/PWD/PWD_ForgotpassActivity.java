package com.philcode.equals.PWD;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.philcode.equals.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.philcode.equals.SelectionScreenActivity;


public class PWD_ForgotpassActivity extends AppCompatActivity {

    Toolbar toolbar;
    ProgressBar progressBar;
    EditText txtEmail;
    Button btnSend;

    FirebaseAuth firebaseAuth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pwd_activity_forgotpass);
        firebaseAuth = FirebaseAuth.getInstance();

        txtEmail = (EditText) findViewById(R.id.txtEmail);
        btnSend = (Button) findViewById(R.id.btnSend);
        /*toolbar = (Toolbar) findViewById(R.id.toolbar);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);*/
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                String useremail = txtEmail.getText().toString();

                if(TextUtils.isEmpty(useremail)){
                    Toast.makeText(PWD_ForgotpassActivity.this, "Please write your valid email address"
                            , Toast.LENGTH_LONG).show();
                }
                else{
                    firebaseAuth.sendPasswordResetEmail(useremail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Snackbar.make(view, "Please check your email account"
                                        , Snackbar.LENGTH_LONG).show();
                            /*    Toast.makeText(PWD_ForgotpassActivity.this, "Please check your email account"
                                        , Toast.LENGTH_LONG).show();*/
                                startActivity(new Intent(PWD_ForgotpassActivity.this, PWD_LoginActivity.class));
                            }else{
                                String message = task.getException().getMessage();
                                Toast.makeText(PWD_ForgotpassActivity.this, message
                                        , Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }
    private void reset(){
        firebaseAuth.sendPasswordResetEmail(txtEmail.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                         //   Toast.makeText(PWD_ForgotpassActivity.this, "Check your email for password reset", Toast.LENGTH_LONG).show();

                        }
                        else{
                            Toast.makeText(PWD_ForgotpassActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                        }

                    }
                });
    }

    public void onClick(View view) {
        if(view == btnSend){
            reset();
        }

    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            startActivity(new Intent(PWD_ForgotpassActivity.this, SelectionScreenActivity.class));

        }
        return super.onKeyDown(keyCode, event);
    }
}
