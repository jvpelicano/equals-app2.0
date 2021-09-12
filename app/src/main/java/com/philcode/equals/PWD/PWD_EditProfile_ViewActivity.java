package com.philcode.equals.PWD;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.philcode.equals.R;

public class PWD_EditProfile_ViewActivity extends Activity{

    private FloatingActionButton fab_main, fab1_personalInfo, fab2_jobInfo;
    private Animation fab_open, fab_close, fab_clock, fab_anticlock;
    TextView textview_personalInfo, textview_jobInfo;
    Button changePhoto;

    Boolean isOpen = false;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pwd_editprofile_view);

        final TextView displayTypeOfDisability1 = findViewById(R.id.displayTypeOfDisability1);
        final TextView displayTypeOfDisability2 = findViewById(R.id.displayTypeOfDisability2);
        final TextView displayTypeOfDisability3 = findViewById(R.id.displayTypeOfDisability3);
        final TextView displayTypeOfDisabilityMore = findViewById(R.id.displayTypeOfDisabilityMore);

        final TextView displaySkill1 = findViewById(R.id.displaySkill1);
        final TextView displaySkill2 = findViewById(R.id.displaySkill2);
        final TextView displaySkill3 = findViewById(R.id.displaySkill3);
        final TextView displaySkill4 = findViewById(R.id.displaySkill4);
        final TextView displaySkill5 = findViewById(R.id.displaySkill5);
        final TextView displaySkill6 = findViewById(R.id.displaySkill6);
        final TextView displaySkill7 = findViewById(R.id.displaySkill7);
        final TextView displaySkill8 = findViewById(R.id.displaySkill8);
        final TextView displaySkill9 = findViewById(R.id.displaySkill9);
        final TextView displaySkill10 = findViewById(R.id.displaySkill10);

        final ImageView images = findViewById(R.id.pwdProfilePic);


        final TextView displayName = findViewById(R.id.displayName);
        final TextView displayEmail = findViewById(R.id.displayEmail);
        final TextView displayAddress = findViewById(R.id.displayAddress);
        final TextView displayContact = findViewById(R.id.displayContact);


        final TextView displayEducationalAttainment = findViewById(R.id.displayEducationalAttainment);
        final TextView displayCategorySkill = findViewById(R.id.displayCategorySkill);
        final TextView displayTotalWorkExperience = findViewById(R.id.displayTotalWorkExperience);
        TextView displaypwdpic = findViewById(R.id.profile_pic_pwd);


        final TextView displayPrimarySkill1 = findViewById(R.id.displayPrimarySkill1);
        final TextView displayPrimarySkill2 = findViewById(R.id.displayPrimarySkill2);
        final TextView displayPrimarySkill3 = findViewById(R.id.displayPrimarySkill3);
        final TextView displayPrimarySkill4 = findViewById(R.id.displayPrimarySkill4);
        final TextView displayPrimarySkill5 = findViewById(R.id.displayPrimarySkill5);
        final TextView displayPrimarySkill6 = findViewById(R.id.displayPrimarySkill6);
        final TextView displayPrimarySkill7 = findViewById(R.id.displayPrimarySkill7);
        final TextView displayPrimarySkill8 = findViewById(R.id.displayPrimarySkill8);
        final TextView displayPrimarySkill9 = findViewById(R.id.displayPrimarySkill9);
        final TextView displayPrimarySkill10 = findViewById(R.id.displayPrimarySkill10);
        final TextView displayPrimarySkillOther = findViewById(R.id.displayPrimarySkillOther);

        FirebaseAuth firebaseAuth;
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentFirebaseUser.getUid();
        final String userz = user.getUid();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("PWD").child(userz);

        changePhoto = findViewById(R.id.changePhoto);

        //animation
        fab_main = findViewById(R.id.fab);
        fab1_personalInfo = findViewById(R.id.fab1);
        fab2_jobInfo = findViewById(R.id.fab2);
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

        fab2_jobInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PWD_EditProfile_ViewActivity.this, PWD_EditProfile2.class);
                startActivity(intent);


            }
        });


        fab1_personalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PWD_EditProfile_ViewActivity.this, PWD_EditProfile.class);
                startActivity(intent);

            }
        });

        changePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PWD_EditProfile_ViewActivity.this, PWD_EditProfile3.class);
                startActivity(intent);
            }
        });


        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String firstname = dataSnapshot.child("firstName").getValue().toString();
                String lastname = dataSnapshot.child("lastName").getValue().toString();
                String email = dataSnapshot.child("email").getValue().toString();
                String address = dataSnapshot.child("address").getValue().toString();
                String city = dataSnapshot.child("city").getValue().toString();

                 displayName.setText(firstname+" "+lastname);
                 displayEmail.setText(email);
                 displayAddress.setText(address+" "+city);

                String typeOfDisability1 = dataSnapshot.child("typeOfDisability1").getValue().toString();
                String typeOfDisability2 = dataSnapshot.child("typeOfDisability2").getValue().toString();
                String typeOfDisability3 = dataSnapshot.child("typeOfDisability3").getValue().toString();
                String typeOfDisabilityMore = dataSnapshot.child("typeOfDisabilityMore").getValue().toString();

                final String d1 = "Orthopedic Disability";
                final String d2 = "Partial Vision Disability";
                final String d3 = "Hearing Disability";
                final String d4 = "Other Disability/s";

                if(typeOfDisability1.equals(d1)) {
                    displayTypeOfDisability1.setText(typeOfDisability1);
                }else{
                    displayTypeOfDisability1.setVisibility(View.GONE);
                }
                if(typeOfDisability2.equals(d2)) {
                    displayTypeOfDisability2.setText(typeOfDisability2);
                }else{
                    displayTypeOfDisability2.setVisibility(View.GONE);
                }
                if(typeOfDisability3.equals(d3)) {
                    displayTypeOfDisability3.setText(typeOfDisability3);
                }else{
                    displayTypeOfDisability3.setVisibility(View.GONE);
                }
                if(typeOfDisabilityMore.equals(d4)) {
                    displayTypeOfDisabilityMore.setText(typeOfDisabilityMore);
                }else{
                    displayTypeOfDisabilityMore.setVisibility(View.GONE);
                }


                String category = dataSnapshot.child("skill").getValue().toString();
                displayCategorySkill.setText(category);

                try{
                    if(dataSnapshot.child("primarySkill1").getValue().toString()!=null) {
                        String primarySkill1 = dataSnapshot.child("primarySkill1").getValue().toString();
                        displayPrimarySkill1.setText(primarySkill1);
                    }else{
                        displayPrimarySkill1.setVisibility(View.GONE);

                    }
                }catch(NullPointerException e){
                    displayPrimarySkill1.setVisibility(View.GONE);

                }

                try{
                    if(dataSnapshot.child("primarySkill2").getValue().toString()!=null) {
                        String primarySkill2 = dataSnapshot.child("primarySkill2").getValue().toString();
                        displayPrimarySkill2.setText(primarySkill2);
                    }else{
                        displayPrimarySkill2.setVisibility(View.GONE);
                    }
                }catch(NullPointerException e){
                    displayPrimarySkill2.setVisibility(View.GONE);
                }


                try{
                    if(dataSnapshot.child("primarySkill3").getValue().toString()!=null) {
                        String primarySkill3 = dataSnapshot.child("primarySkill3").getValue().toString();
                        displayPrimarySkill3.setText(primarySkill3);
                    }else{
                        displayPrimarySkill3.setVisibility(View.GONE);
                    }
                }catch (NullPointerException e){
                    displayPrimarySkill3.setVisibility(View.GONE);
                }

                try{
                    if(dataSnapshot.child("primarySkill4").getValue().toString()!=null) {
                        String primarySkill4 = dataSnapshot.child("primarySkill4").getValue().toString();
                        displayPrimarySkill4.setText(primarySkill4);
                    }else{
                        displayPrimarySkill4.setVisibility(View.GONE);
                    }
                }catch (NullPointerException e){
                    displayPrimarySkill4.setVisibility(View.GONE);
                }

                try{
                    if(dataSnapshot.child("primarySkill5").getValue().toString()!=null) {
                        String primarySkill5 = dataSnapshot.child("primarySkill5").getValue().toString();
                        displayPrimarySkill5.setText(primarySkill5);
                    }else{
                        displayPrimarySkill5.setVisibility(View.GONE);
                    }
                }catch (NullPointerException e){
                    displayPrimarySkill5.setVisibility(View.GONE);
                }


                try{
                    if(dataSnapshot.child("primarySkill6").getValue().toString()!=null) {
                        String primarySkill6 = dataSnapshot.child("primarySkill6").getValue().toString();
                        displayPrimarySkill6.setText(primarySkill6);
                    }else{
                        displayPrimarySkill6.setVisibility(View.GONE);
                    }
                }catch (NullPointerException e){
                    displayPrimarySkill6.setVisibility(View.GONE);
                }

                try{
                    if(dataSnapshot.child("primarySkill7").getValue().toString()!=null) {
                        String primarySkill7 = dataSnapshot.child("primarySkill7").getValue().toString();
                        displayPrimarySkill7.setText(primarySkill7);
                    }else{
                        displayPrimarySkill7.setVisibility(View.GONE);
                    }
                }catch (NullPointerException e){
                    displayPrimarySkill7.setVisibility(View.GONE);
                }

                try{
                    if(dataSnapshot.child("primarySkill8").getValue().toString()!=null) {
                        String primarySkill8 = dataSnapshot.child("primarySkill8").getValue().toString();
                        displayPrimarySkill8.setText(primarySkill8);
                    }else{
                        displayPrimarySkill8.setVisibility(View.GONE);
                    }
                }catch (NullPointerException e){
                    displayPrimarySkill8.setVisibility(View.GONE);
                }



                try{
                    if(dataSnapshot.child("primarySkill9").getValue().toString()!=null) {
                        String primarySkill9 = dataSnapshot.child("primarySkill9").getValue().toString();
                        displayPrimarySkill9.setText(primarySkill9);
                    }else{
                        displayPrimarySkill9.setVisibility(View.GONE);
                    }
                }catch (NullPointerException e){
                    displayPrimarySkill9.setVisibility(View.GONE);
                }

                try{
                    if(dataSnapshot.child("primarySkill10").getValue().toString()!=null) {
                        String primarySkill10 = dataSnapshot.child("primarySkill10").getValue().toString();
                        displayPrimarySkill10.setText(primarySkill10);
                    }else{
                        displayPrimarySkill10.setVisibility(View.GONE);
                    }
                }catch (NullPointerException e){
                    displayPrimarySkill10.setVisibility(View.GONE);
                }

                try{
                    if(dataSnapshot.child("primarySkillOther").getValue().toString()!=null) {
                        String primarySkillOther = dataSnapshot.child("primarySkillOther").getValue().toString();
                        displayPrimarySkillOther.setText(primarySkillOther);
                    }else{
                        displayPrimarySkillOther.setVisibility(View.GONE);
                    }
                }catch (NullPointerException e){
                    displayPrimarySkillOther.setVisibility(View.GONE);
                }

                String jobSkill1 = dataSnapshot.child("jobSkill1").getValue().toString();
                String jobSkill2 = dataSnapshot.child("jobSkill2").getValue().toString();
                String jobSkill3 = dataSnapshot.child("jobSkill3").getValue().toString();
                String jobSkill4 = dataSnapshot.child("jobSkill4").getValue().toString();
                String jobSkill5 = dataSnapshot.child("jobSkill5").getValue().toString();
                String jobSkill6 = dataSnapshot.child("jobSkill6").getValue().toString();
                String jobSkill7 = dataSnapshot.child("jobSkill7").getValue().toString();
                String jobSkill8= dataSnapshot.child("jobSkill8").getValue().toString();
                String jobSkill9 = dataSnapshot.child("jobSkill9").getValue().toString();
                String jobSkill10 = dataSnapshot.child("jobSkill10").getValue().toString();

                final String s1 = "Active Listening";
                final String s2 = "Communication";
                final String s3 = "Computer Skills";
                final String s4 = "Customer Service";
                final String s5 = "Interpersonal Skills";
                final String s6 = "Leadership";
                final String s7 = "Management Skills";
                final String s8 = "Problem-Solving";
                final String s9 = "Time Management";
                final String s10 = "Transferable Skills";

                if(jobSkill1.equals(s1)) {
                    displaySkill1.setText(jobSkill1);
                }else{
                    displaySkill1.setVisibility(View.GONE);
                }

                if(jobSkill2.equals(s2)) {
                    displaySkill2.setText(jobSkill2);
                }else{
                    displaySkill2.setVisibility(View.GONE);
                }

                if(jobSkill3.equals(s3)) {
                    displaySkill3.setText(jobSkill3);
                }else{
                    displaySkill3.setVisibility(View.GONE);
                }

                if(jobSkill4.equals(s4)) {
                    displaySkill4.setText(jobSkill4);
                }else{
                    displaySkill4.setVisibility(View.GONE);
                }

                if(jobSkill5.equals(s5)) {
                    displaySkill5.setText(jobSkill5);
                }else{
                    displaySkill5.setVisibility(View.GONE);
                }

                if(jobSkill6.equals(s6)) {
                    displaySkill6.setText(jobSkill6);
                }else{
                    displaySkill6.setVisibility(View.GONE);
                }

                if(jobSkill7.equals(s7)) {
                    displaySkill7.setText(jobSkill7);
                }else{
                    displaySkill7.setVisibility(View.GONE);
                }

                if(jobSkill8.equals(s8)) {
                    displaySkill8.setText(jobSkill8);
                }else{
                    displaySkill8.setVisibility(View.GONE);
                }

                if(jobSkill9.equals(s9)) {
                    displaySkill9.setText(jobSkill9);
                }else{
                    displaySkill9.setVisibility(View.GONE);
                }

                if(jobSkill10.equals(s10)) {
                    displaySkill10.setText(jobSkill10);
                }else{
                    displaySkill10.setVisibility(View.GONE);
                }


                String pwdProfilePic = dataSnapshot.child("pwdProfilePic").getValue().toString();
                Glide.with(getApplicationContext()).load(pwdProfilePic).into(images);


                String educationalAttainment = dataSnapshot.child("educationalAttainment").getValue().toString();
                String workExperience = dataSnapshot.child("workExperience").getValue().toString();
                String contact = dataSnapshot.child("contact").getValue().toString();


                displayEducationalAttainment.setText(educationalAttainment);
                displayTotalWorkExperience.setText(workExperience);
                displayContact.setText(contact);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            final Intent intent = new Intent(this, a_PWDContentMainActivity.class);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }
}