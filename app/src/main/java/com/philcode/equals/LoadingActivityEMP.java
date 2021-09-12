package com.philcode.equals;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.philcode.equals.EMP.LoginActivity_emp;
import com.philcode.equals.EMP.a_EmployeeContentMainActivity;

public class LoadingActivityEMP extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private String group;
    private Button btn;
    public void setGroup(String group){
        this.group = group;
    }
    protected void onCreate(Bundle savedInstanceState) {

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //final DatabaseReference typeStatus = FirebaseDatabase.getInstance().getReference("PWD").child(firebaseAuth.getUid());
        /*btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

            }
        });*/

        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference rootRef1 = rootRef.child(firebaseAuth.getUid());
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.getValue().equals(null)) {
                    Toast.makeText(LoadingActivityEMP.this, "No user found",
                            Toast.LENGTH_LONG).show();
                }else{
                    DatabaseReference typeStatusEMP = FirebaseDatabase.getInstance().getReference("Employers");
                    typeStatusEMP.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.hasChild(firebaseAuth.getUid())) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        DatabaseReference typeStatusEMP1 = FirebaseDatabase.getInstance().getReference("Employers").child(firebaseAuth.getUid());

                        if (firebaseAuth.getCurrentUser() != null && firebaseAuth.getCurrentUser().isEmailVerified()) {
                            typeStatusEMP1.addValueEventListener(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                     //String status = dataSnapshot.child("typeStatus").getValue().toString();
                     //PWD_UserInformation currentProfile = dataSnapshot.child("typeStatus").getValue(PWD_UserInformation.class);

                     switch(dataSnapshot.child("typeStatus").getValue().toString()) {
                         case "EMPApproved":
                             Intent i = new Intent(LoadingActivityEMP.this, a_EmployeeContentMainActivity.class);
                             startActivity(i);
                             finish();
                             break;
                         case "EMPPending":
                           /*  Toast.makeText(LoadingActivityEMP.this, "Please wait for admin approval",
                                     Toast.LENGTH_LONG).show();*/
                             Intent ii = new Intent(LoadingActivityEMP.this, LoginActivity_emp.class);
                             startActivity(ii);
                             finish();

                             break;
                         default:
                             Toast.makeText(LoadingActivityEMP.this, "Not an Employer account",
                                     Toast.LENGTH_LONG).show();
                             Intent iii = new Intent(LoadingActivityEMP.this, LoginActivity_emp.class);
                             startActivity(iii);
                                    }

                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(LoadingActivityEMP.this, databaseError.getCode(),
                                            Toast.LENGTH_LONG).show();
                                }
                            });

                        } else if (firebaseAuth.getCurrentUser() == null) {
                            Toast.makeText(LoadingActivityEMP.this, "No user found.",
                                    Toast.LENGTH_LONG).show();
                        }



                    }

                }, 1000);
            }else{
                Toast.makeText(LoadingActivityEMP.this, "Invalid Account",
                        Toast.LENGTH_LONG).show();
                Intent iii = new Intent(LoadingActivityEMP.this, LoginActivity_emp.class);
                startActivity(iii);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
