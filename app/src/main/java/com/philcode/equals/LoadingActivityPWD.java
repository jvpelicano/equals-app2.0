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

import com.philcode.equals.PWD.PWD_LoginActivity;
import com.philcode.equals.PWD.a_PWDContentMainActivity;

public class LoadingActivityPWD extends AppCompatActivity {

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

        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference rootRef1 = rootRef.child(firebaseAuth.getUid());
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.getValue().equals(null)) {
                    Toast.makeText(LoadingActivityPWD.this, "No user found",
                            Toast.LENGTH_LONG).show();
                }else{
           DatabaseReference typeStatusPWD = FirebaseDatabase.getInstance().getReference("PWD");
           typeStatusPWD.addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(DataSnapshot snapshot) {
                   if (snapshot.hasChild(firebaseAuth.getUid())) {
                       new Handler().postDelayed(new Runnable() {
                           @Override
                                    public void run() {
                               DatabaseReference typeStatusPWD1 = FirebaseDatabase.getInstance().getReference("PWD").child(firebaseAuth.getUid());
                               if (firebaseAuth.getCurrentUser() != null && firebaseAuth.getCurrentUser().isEmailVerified()) {
                                   typeStatusPWD1.addValueEventListener(new ValueEventListener() {
                                       @Override
                                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                           //String status = dataSnapshot.child("typeStatus").getValue().toString();
                                           //PWD_UserInformation currentProfile = dataSnapshot.child("typeStatus").getValue(PWD_UserInformation.class);

                                           switch(dataSnapshot.child("typeStatus").getValue().toString()) {
                                               case "PWDApproved":
                                                   Intent i = new Intent(LoadingActivityPWD.this, a_PWDContentMainActivity.class);
                                                   startActivity(i);
                                                   finish();
                                                   break;
                                               case "PWDPending":
                                                   Toast.makeText(LoadingActivityPWD.this, "Please wait for admin approval",
                                                           Toast.LENGTH_LONG).show();
                                                   Intent ii = new Intent(LoadingActivityPWD.this, PWD_LoginActivity.class);
                                                   startActivity(ii);
                                                   finish();
                                                   break;
                                               default:
                                                   Toast.makeText(LoadingActivityPWD.this, "Not a PWD account",
                                                           Toast.LENGTH_LONG).show();
                                                   Intent iii = new Intent(LoadingActivityPWD.this, PWD_LoginActivity.class);
                                                   startActivity(iii);
                                           }

                                       }
                                       @Override
                                       public void onCancelled(@NonNull DatabaseError databaseError) {
                                           Toast.makeText(LoadingActivityPWD.this, databaseError.getCode(),
                                                   Toast.LENGTH_LONG).show();
                                       }
                                   });


                               } else if (firebaseAuth.getCurrentUser() == null) {
                                   Toast.makeText(LoadingActivityPWD.this, "No user found.",
                                           Toast.LENGTH_LONG).show();
                               }

                     }


                 }, 1000);
             }else{
                 Toast.makeText(LoadingActivityPWD.this, "Invalid Account",
                         Toast.LENGTH_LONG).show();
                 Intent iii = new Intent(LoadingActivityPWD.this, PWD_LoginActivity.class);
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
