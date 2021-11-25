package com.philcode.equals.EMP;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.philcode.equals.EMP_PWD_Information;

import com.philcode.equals.R;

import java.util.ArrayList;
import java.util.Collections;

public class EMP_ViewPotential_All extends AppCompatActivity {

    DatabaseReference reference, refForEmp;
    RecyclerView recyclerView;
    EMP_ViewPotential_Adapter adapter;

    private FirebaseAuth firebaseAuth;
    private DrawerLayout pDrawerLayout;
    private ActionBarDrawerToggle pToggle;

    private Toolbar pToolbar;
    private NavigationView eNavigation;

    TextView fullname, pwd_email;
    ImageView imgProfile = null;
    String dp1;
    String companyName;
    private TextView profile_email;

    //as of Oct 29, 2019
    int pwdNumberOfPrimarySkill;
    SwitchMaterial switchPriority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentFirebaseUser.getUid();
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("Employers/" + uid);
        final String email = currentFirebaseUser.getEmail().toString();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emp_potentialapplicants_recycler);

        switchPriority = findViewById(R.id.switchPriority);

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
                                Intent i = new Intent(EMP_ViewPotential_All.this, a_EmployeeContentMainActivity.class);
                                startActivity(i);
                                break;
                            case R.id.nav_profile_emp:
                                Intent i2 = new Intent(EMP_ViewPotential_All.this, EMP_Profile.class);
                                startActivity(i2);
                                break;
                            case R.id.nav_post_job_emp:
                                Intent ii = new Intent(EMP_ViewPotential_All.this, Emp_PostJob.class);
                                startActivity(ii);
                                break;
                            case R.id.nav_manage_job_emp:
                                Intent i4 = new Intent(EMP_ViewPotential_All.this, EMP_ManageJobs.class);
                                startActivity(i4);
                                break;
                            case R.id.nav_website_emp:
                                Intent EqualsSite = new Intent(android.content.Intent.ACTION_VIEW);
                                EqualsSite.setData(Uri.parse("http://www.equals.org.ph"));
                                startActivity(EqualsSite);
                                break;

                            case R.id.nav_logout_emp:

                                AlertDialog.Builder alert =  new AlertDialog.Builder(EMP_ViewPotential_All.this);
                                alert.setMessage("By clicking Yes, you will return to the login screen.").setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                FirebaseAuth.getInstance().signOut();
                                                //closing activity
                                                finish();
                                                //starting login activity
                                                startActivity(new Intent(EMP_ViewPotential_All.this, LoginActivity_emp.class));
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

                dp1 = snapshot.child("empValidID").getValue().toString();
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


        recyclerView = findViewById(R.id.userRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        pToolbar = findViewById(R.id.nav_action_bar_emp);
        setSupportActionBar(pToolbar);

        pDrawerLayout = findViewById(R.id.drawerLayout_availablejobs_pwd);
        pToggle = new ActionBarDrawerToggle(this, pDrawerLayout, R.string.open_pwd, R.string.close_pwd);

        pDrawerLayout.addDrawerListener(pToggle);
        pToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        reference = FirebaseDatabase.getInstance().getReference().child("PWD");
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("PWD");
        reference1.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<EMP_PWD_Information> list = new ArrayList<>();
                list.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String email = dataSnapshot1.child("email").getValue().toString();
                    String typeStatus = dataSnapshot1.child("typeStatus").getValue().toString();
                    String firstName = dataSnapshot1.child("firstName").getValue().toString();
                    String lastName = dataSnapshot1.child("lastName").getValue().toString();
                    String address = dataSnapshot1.child("address").getValue().toString();
                    String city = dataSnapshot1.child("city").getValue().toString();
                    String contact = dataSnapshot1.child("contact").getValue().toString();
                    String pwdProfilePic = dataSnapshot1.child("pwdProfilePic").getValue().toString();

                    String key = dataSnapshot1.getKey().toString();
                    EMP_PWD_Information pwd = new EMP_PWD_Information(email, typeStatus, firstName, lastName, address, city, pwdProfilePic, key, contact);
                    list.add(pwd);
                }
                Collections.reverse(list);
                adapter = new EMP_ViewPotential_Adapter(EMP_ViewPotential_All.this, list);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(EMP_ViewPotential_All.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

        switchPriority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchPriority.isChecked()){
                    rootRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String city = dataSnapshot.child("companycity").getValue().toString();
                            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("PWD");
                            reference1.orderByChild("city").equalTo(city).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    ArrayList<EMP_PWD_Information> list = new ArrayList<>();
                                    list.clear();
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        String email = dataSnapshot1.child("email").getValue().toString();
                                        String typeStatus = dataSnapshot1.child("typeStatus").getValue().toString();
                                        String firstName = dataSnapshot1.child("firstName").getValue().toString();
                                        String lastName = dataSnapshot1.child("lastName").getValue().toString();
                                        String address = dataSnapshot1.child("address").getValue().toString();
                                        String city = dataSnapshot1.child("city").getValue().toString();
                                        String pwdProfilePic = dataSnapshot1.child("pwdProfilePic").getValue().toString();
                                        String contact = dataSnapshot1.child("contact").getValue().toString();

                                        String key = dataSnapshot1.getKey().toString();
                                        EMP_PWD_Information pwd = new EMP_PWD_Information(email, typeStatus, firstName, lastName, address, city, pwdProfilePic, key, contact);
                                        list.add(pwd);
                                    }
                                    Collections.reverse(list);
                                    adapter = new EMP_ViewPotential_Adapter(EMP_ViewPotential_All.this, list);
                                    recyclerView.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(EMP_ViewPotential_All.this, "Error", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }else {

                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("PWD");
                    reference1.addValueEventListener(new ValueEventListener() {


                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ArrayList<EMP_PWD_Information> list = new ArrayList<>();
                            list.clear();
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                String email = dataSnapshot1.child("email").getValue().toString();
                                String typeStatus = dataSnapshot1.child("typeStatus").getValue().toString();
                                String firstName = dataSnapshot1.child("firstName").getValue().toString();
                                String lastName = dataSnapshot1.child("lastName").getValue().toString();
                                String address = dataSnapshot1.child("address").getValue().toString();
                                String city = dataSnapshot1.child("city").getValue().toString();
                                String pwdProfilePic = dataSnapshot1.child("pwdProfilePic").getValue().toString();
                                String contact = dataSnapshot1.child("contact").getValue().toString();

                                String key = dataSnapshot1.getKey().toString();
                                EMP_PWD_Information pwd = new EMP_PWD_Information(email, typeStatus, firstName, lastName, address, city, pwdProfilePic, key, contact);
                                list.add(pwd);
                            }
                            Collections.reverse(list);
                            adapter = new EMP_ViewPotential_Adapter(EMP_ViewPotential_All.this, list);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(EMP_ViewPotential_All.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(pToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}