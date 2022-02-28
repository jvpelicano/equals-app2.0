package com.philcode.equals.EMP;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.philcode.equals.PWD.PWD_LoginActivity;
import com.philcode.equals.PWD.a_PWDContentMainActivity;
import com.philcode.equals.Post_HomeInformation;
import com.philcode.equals.Post_Home_MyAdapter;
import com.philcode.equals.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class a_EmployeeContentMainActivity extends AppCompatActivity  {
    //recycler
    private DatabaseReference home_databaseref;
    private RecyclerView home_recyclerView;
    private List<Post_HomeInformation> home_list;
    private Post_Home_MyAdapter home_adapter;

    private DrawerLayout eDrawerLayout;
    private ActionBarDrawerToggle eToggle;

    private FirebaseAuth firebaseAuth;
    private Toolbar eToolbar;
    private TextView profile_email;
    private NavigationView eNavigation;
    private LinearLayoutManager mLayoutmanager;

    TextView fullname, pwd_email;
    ImageView imgProfile = null;
    String dp1;
    String companyName;
    protected void onCreate(Bundle savedInstanceState) {
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentFirebaseUser.getUid();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("Employers/" + uid);
        final String email = currentFirebaseUser.getEmail().toString();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emp_activity_home);
//https://www.flaticon.com/free-icon/picture_25666
        rootRef.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {

                eNavigation = findViewById(R.id.navigation_view_emp);
                eNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        profile_email = (TextView) findViewById(R.id.profile_email);
                        profile_email.setText(user.getEmail());

                        int id = menuItem.getItemId();
                        switch (id) {
                            case R.id.nav_home_emp:
                                Intent i = new Intent(a_EmployeeContentMainActivity.this, a_EmployeeContentMainActivity.class);
                                startActivity(i);
                                break;
                            case R.id.nav_logo:
                                Intent logo = new Intent(a_EmployeeContentMainActivity.this, EMP_ChangeLogo.class);
                                startActivity(logo);
                                break;
                            case R.id.nav_profile_emp:
                                Intent i2 = new Intent(a_EmployeeContentMainActivity.this, EMP_EditProfile_Activity.class);
                                startActivity(i2);
                                break;
                            case R.id.nav_post_job_emp:
                                Intent ii = new Intent(a_EmployeeContentMainActivity.this, Emp_PostJob.class);
                                startActivity(ii);
                                break;
                            case R.id.nav_manage_job_emp:
                                Intent i4 = new Intent(a_EmployeeContentMainActivity.this, EMP_ManageJobs.class);
                                startActivity(i4);
                                break;
                            case R.id.nav_website_emp:
                                Intent EqualsSite = new Intent(android.content.Intent.ACTION_VIEW);
                                EqualsSite.setData(Uri.parse("http://www.equals.org.ph"));
                                startActivity(EqualsSite);
                                break;

                            case R.id.nav_logout_emp:

                                AlertDialog.Builder alert =  new AlertDialog.Builder(a_EmployeeContentMainActivity.this);
                                alert.setMessage("By clicking Yes, you will return to the login screen.").setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                FirebaseAuth.getInstance().signOut();
                                                //closing activity
                                                finish();
                                                //starting login activity
                                                startActivity(new Intent(a_EmployeeContentMainActivity.this, LoginActivity_emp.class));
                                            }
                                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                AlertDialog alertDialog = alert.create();
                                alertDialog.setTitle("Log Out?");
                                alertDialog.show();

                                break;
                        }
                        return false;
                    }
                });

                dp1 = snapshot.child("empProfilePic").getValue().toString();
                companyName = snapshot.child("fullname").getValue().toString();
                View hView = eNavigation.inflateHeaderView(R.layout.emp_navigation_header);
                imgProfile = hView.findViewById(R.id.profile_pic_emp);
                Glide.with(getApplicationContext()).load(dp1).into(imgProfile);
                fullname = hView.findViewById(R.id.profile_name_emp);
                fullname.setText(companyName);
                pwd_email = hView.findViewById(R.id.profile_email);
                pwd_email.setText(email);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        home_list = new ArrayList<>();

        mLayoutmanager = new LinearLayoutManager(a_EmployeeContentMainActivity.this);
        mLayoutmanager.setReverseLayout(true);
        mLayoutmanager.setStackFromEnd(true);
        home_recyclerView = findViewById(R.id.homeRecyclerView);
        home_recyclerView.setHasFixedSize(true);
        home_recyclerView.setLayoutManager(mLayoutmanager);
        home_recyclerView.setLayoutManager(new LinearLayoutManager(this));


        home_databaseref = FirebaseDatabase.getInstance().getReference("home_content");
        home_databaseref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    Post_HomeInformation p = dataSnapshot1.getValue(Post_HomeInformation.class);
                    home_list.add(p);
                }
                Collections.reverse(home_list);
                home_adapter = new Post_Home_MyAdapter(a_EmployeeContentMainActivity.this, home_list);
                home_recyclerView.setAdapter(home_adapter);
                home_adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(a_EmployeeContentMainActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        eToolbar = findViewById(R.id.nav_action_bar_emp);
        setSupportActionBar(eToolbar);

        eDrawerLayout = findViewById(R.id.drawerLayout_home_emp);
        eToggle = new ActionBarDrawerToggle(this,eDrawerLayout, R.string.open_emp, R.string.close_emp);

        eDrawerLayout.addDrawerListener(eToggle);
        eToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    //back
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(eToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}