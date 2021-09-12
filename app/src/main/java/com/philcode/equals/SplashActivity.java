package com.philcode.equals;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.view.KeyEvent;
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
import com.philcode.equals.EMP.EMP_ViewResume;
import com.philcode.equals.EMP.EMP_ViewResume_Adapter;
import com.philcode.equals.EMP.EMP_ViewResume_Information;
import com.philcode.equals.EMP.LoginActivity_emp;
import com.philcode.equals.EMP.a_EmployeeContentMainActivity;
import com.philcode.equals.PWD.PWD_LoginActivity;
import com.philcode.equals.PWD.a_PWDContentMainActivity;

import java.util.Collections;


public class SplashActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            finish();
            System.exit(0);
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        final FirebaseAuth firebaseAuth;
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               /* Intent intent = new Intent(SplashActivity.this, SelectionScreenActivity.class);
                startActivity(intent);*/
                if(firebaseAuth.getCurrentUser()== null){
                    Intent intent = new Intent(SplashActivity.this, SelectionScreenActivity.class);
                    startActivity(intent);

                }else{
                    Intent intent = new Intent(SplashActivity.this, SelectionScreenActivity.class);
                    startActivity(intent);
                    /*DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                    ref.orderByChild("uid").equalTo(user.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                String parent = dataSnapshot1.getKey();
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Job_Offers/" + parent).child("Resumes");
                                ref.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                                            EMP_ViewResume_Information p = dataSnapshot2.getValue(EMP_ViewResume_Information.class);
                                            list.add(p);
                               *//* String hi = dataSnapshot.child("First Name").toString();
                                Toast.makeText(EMP_ViewResume.this, hi,
                                        Toast.LENGTH_LONG).show();*//*
                                        }
                                        Collections.reverse(list);
                                        adapter = new EMP_ViewResume_Adapter(EMP_ViewResume.this, list);
                                        recyclerView.setAdapter(adapter);
                                        adapter.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Toast.makeText(EMP_ViewResume.this, "Error", Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

*/

                    /*if(firebaseAuth.getCurrentUser().isEmailVerified()){
                        if (FirebaseDatabase.getInstance().getReference("Employers").child(firebaseAuth.getCurrentUser().getUid()).toString() == user.getUid()) {
                                DatabaseReference typeStatusEMP1 = FirebaseDatabase.getInstance().getReference("Employers").child(firebaseAuth.getCurrentUser().getUid());
                                typeStatusEMP1.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        switch(dataSnapshot.child("typeStatus").getValue().toString()) {
                                            case "EMPApproved":
                                                Intent i = new Intent(SplashActivity.this, a_EmployeeContentMainActivity.class);
                                                startActivity(i);
                                                finish();
                                                break;
                                            case "EMPPending":
                                                Intent ii = new Intent(SplashActivity.this, LoginActivity_emp.class);
                                                startActivity(ii);
                                                finish();
                                                break;
                                            default:
                                                Intent intent = new Intent(SplashActivity.this, SelectionScreenActivity.class);
                                                startActivity(intent);
                                        }

                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Toast.makeText(SplashActivity.this, databaseError.getCode(),
                                                Toast.LENGTH_LONG).show();
                                    }
                                });


                        }
                        else if (FirebaseDatabase.getInstance().getReference("PWD").child(firebaseAuth.getCurrentUser().getUid()).toString() == user.getUid()){

                            DatabaseReference typeStatusPWD1 = FirebaseDatabase.getInstance().getReference("PWD").child(firebaseAuth.getUid());
                            if (firebaseAuth.getCurrentUser() != null && firebaseAuth.getCurrentUser().isEmailVerified()) {
                                typeStatusPWD1.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        //String status = dataSnapshot.child("typeStatus").getValue().toString();
                                        //PWD_UserInformation currentProfile = dataSnapshot.child("typeStatus").getValue(PWD_UserInformation.class);

                                        switch(dataSnapshot.child("typeStatus").getValue().toString()) {
                                            case "PWDApproved":
                                                Intent i = new Intent(SplashActivity.this, a_PWDContentMainActivity.class);
                                                startActivity(i);
                                                finish();
                                                break;
                                            case "PWDPending":
                                                Toast.makeText(SplashActivity.this, "Please wait for admin approval",
                                                        Toast.LENGTH_LONG).show();
                                                Intent ii = new Intent(SplashActivity.this, PWD_LoginActivity.class);
                                                startActivity(ii);
                                                finish();
                                                break;
                                            default:
                                                Toast.makeText(SplashActivity.this, "Not a PWD account",
                                                        Toast.LENGTH_LONG).show();
                                                Intent iii = new Intent(SplashActivity.this, PWD_LoginActivity.class);
                                                startActivity(iii);
                                        }

                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Toast.makeText(SplashActivity.this, databaseError.getCode(),
                                                Toast.LENGTH_LONG).show();
                                    }
                                });
                            }



                        }else{
                            Intent intent = new Intent(SplashActivity.this, SelectionScreenActivity.class);
                            startActivity(intent);
                        }

                    } else{
                        Intent intent = new Intent(SplashActivity.this, SelectionScreenActivity.class);
                        startActivity(intent);
                        Toast.makeText(SplashActivity.this, "Check your email for verification.", Toast.LENGTH_SHORT).show();
                    }*/

                    }


                }


        }, 2000);
    }
}
