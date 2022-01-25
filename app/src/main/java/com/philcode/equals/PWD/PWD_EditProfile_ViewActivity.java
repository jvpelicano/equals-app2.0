package com.philcode.equals.PWD;

import static android.view.View.GONE;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.philcode.equals.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PWD_EditProfile_ViewActivity extends AppCompatActivity {

    private FloatingActionButton fab_main, fab1_personalInfo, fab2_jobInfo, fab3_addWorkExpInfo;
    private Animation fab_open, fab_close, fab_clock, fab_anticlock;
    TextView textview_personalInfo, textview_jobInfo, textview_AddWorkInfo;
    Button changePhoto;

    Boolean isOpen = false;


    //fragments
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private PWD_ProfileView_FragmentAdapter fragmentAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pwd_editprofile_view);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.pwd_viewpager2);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentAdapter = new PWD_ProfileView_FragmentAdapter(fragmentManager, getLifecycle());
        viewPager2.setAdapter(fragmentAdapter);

        tabLayout.addTab(tabLayout.newTab().setText("Basic Information"));
        tabLayout.addTab(tabLayout.newTab().setText("Work Experience"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });



        final ImageView images = findViewById(R.id.pwdProfilePic);


        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = currentFirebaseUser.getUid();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("PWD").child(uid);

        changePhoto = findViewById(R.id.changePhoto);
        
        //animation
        fab_main = findViewById(R.id.fab);
        fab1_personalInfo = findViewById(R.id.fab1);
        fab2_jobInfo = findViewById(R.id.fab2);
        fab3_addWorkExpInfo = findViewById(R.id.fab3);
        textview_personalInfo = findViewById(R.id.textview_personalInfo);
        textview_jobInfo = findViewById(R.id.textview_jobQualifications);
        textview_AddWorkInfo = findViewById(R.id.textview_AddWork);
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
                    textview_AddWorkInfo.setVisibility(View.INVISIBLE);
                    fab3_addWorkExpInfo.startAnimation(fab_close);
                    fab2_jobInfo.startAnimation(fab_close);
                    fab1_personalInfo.startAnimation(fab_close);
                    fab_main.startAnimation(fab_anticlock);
                    fab3_addWorkExpInfo.setClickable(false);
                    fab2_jobInfo.setClickable(false);
                    fab1_personalInfo.setClickable(false);
                    isOpen = false;
                } else {
                    textview_jobInfo.setVisibility(View.VISIBLE);
                    textview_personalInfo.setVisibility(View.VISIBLE);
                    textview_AddWorkInfo.setVisibility(View.VISIBLE);
                    fab3_addWorkExpInfo.startAnimation(fab_open);
                    fab2_jobInfo.startAnimation(fab_open);
                    fab1_personalInfo.startAnimation(fab_open);
                    fab_main.startAnimation(fab_clock);
                    fab3_addWorkExpInfo.setClickable(true);
                    fab2_jobInfo.setClickable(true);
                    fab1_personalInfo.setClickable(true);
                    isOpen = true;
                }

            }
        });

        fab3_addWorkExpInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PWD_EditProfile_ViewActivity.this, PWD_AddWorkExperience.class);
                startActivity(intent);
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
                /////////////////////////////////// Secondary skills

                String pwdProfilePic = dataSnapshot.child("pwdProfilePic").getValue().toString();
                Glide.with(getApplicationContext()).load(pwdProfilePic).into(images);


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