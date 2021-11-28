package com.philcode.equals.EMP;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.philcode.equals.EMP_ViewResumePDF_Activity;
import com.philcode.equals.R;

import java.util.ArrayList;
import java.util.Collections;

public class EMP_ViewResume extends AppCompatActivity {
    Context context;
    DatabaseReference dbRef, current;
    EMP_ViewResume_Adapter adapter;


    private DrawerLayout pDrawerLayout;
    private ActionBarDrawerToggle pToggle;

    private FirebaseAuth firebaseAuth;


    private Toolbar pToolbar;
    private NavigationView eNavigation;

    TextView fullname, pwd_email;
    ImageView imgProfile = null;
    String dp1;
    String companyName;
    private TextView profile_email;


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emp_jobs_list);
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentFirebaseUser.getUid();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("Employers/" + uid);
        final String email = currentFirebaseUser.getEmail().toString();

        final ArrayList<EMP_ViewResume_Information> list = new ArrayList<>();
        final RecyclerView recyclerView;
        // Toast.makeText(getApplicationContext(), getParent().toString(), Toast.LENGTH_LONG).show();
        dbRef = FirebaseDatabase.getInstance().getReference("Job_Offers");

        // current = dbRef.child(user.);

        String postJobID = getIntent().getStringExtra("POST_ID");
        recyclerView = findViewById(R.id.theRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        recyclerView.addOnItemTouchListener(
                new EMP_ResumeRecyclerItemClickListener(context, recyclerView, new EMP_ResumeRecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(EMP_ViewResume.this);
                        alert.setMessage("Let them know you are interested to reach them.").setCancelable(false)
                                .setPositiveButton("Send Email", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i = new Intent(Intent.ACTION_SEND);
                                        i.setData(Uri.parse("email"));
                                        TextView email = findViewById(R.id.editEmail);
                                        String[] emailP = {email.getText().toString()};
                                        i.putExtra(Intent.EXTRA_EMAIL, emailP);
                                        i.putExtra(Intent.EXTRA_SUBJECT, "Invitation for Interview");
                                        i.putExtra(Intent.EXTRA_TEXT, "Put your details here.");
                                        i.setType("message/rfc822");
                                        Intent chooser = Intent.createChooser(i, "Launch Email");
                                        startActivity(chooser);

                                    }
                                }).setNegativeButton("Call", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(Intent.ACTION_CALL);
                                TextView contact = findViewById(R.id.editContact);
                                String contactNum = contact.getText().toString();
                                i.setData(Uri.parse("tel:" + contactNum));
                                if (ActivityCompat.checkSelfPermission(EMP_ViewResume.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    requestPermission();
                                } else {
                                    startActivity(i);
                                }
                            }

                            private void requestPermission() {
                                ActivityCompat.requestPermissions(EMP_ViewResume.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                            }

                        }).setNeutralButton("View Resume", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(EMP_ViewResume.this, EMP_ViewResumePDF_Activity.class);
                                intent.putExtra("PDF_Uri", list.get(position).getResumeFile());
                                //Toast.makeText(EMP_ViewResume.this, list.get(position).getResumeFile(), Toast.LENGTH_SHORT).show();
                                //EMP_ViewResume_Information emp = new EMP_ViewResume_Information();
                                /*Intent intent = new Intent();
                                intent.setType(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(list.get(position).getResumeFile()));*/
                                startActivity(intent);
                            }
                        });
                        AlertDialog alertDialog = alert.create();
                        alertDialog.setTitle("Choose Action");
                        alertDialog.show();
                        alertDialog.setCancelable(true);


                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })

        );


        dbRef.child(postJobID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Resumes > Resume ; Deleted for loop
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Job_Offers/" + postJobID).child("Resume");
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                                EMP_ViewResume_Information p = dataSnapshot2.getValue(EMP_ViewResume_Information.class);
                                list.add(p);
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

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        rootRef.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {

                eNavigation = findViewById(R.id.navigation_view_emp);
                eNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        profile_email = findViewById(R.id.profile_email);
                        profile_email.setText(user.getEmail());

                        int id = menuItem.getItemId();
                        switch (id) {
                            case R.id.nav_home_emp:
                                Intent i = new Intent(EMP_ViewResume.this, a_EmployeeContentMainActivity.class);
                                startActivity(i);
                                break;
                            case R.id.nav_profile_emp:
                                Intent i2 = new Intent(EMP_ViewResume.this, EMP_EditProfile_Activity.class);
                                startActivity(i2);
                                break;
                            case R.id.nav_post_job_emp:
                                Intent ii = new Intent(EMP_ViewResume.this, Emp_PostJob.class);
                                startActivity(ii);
                                break;
                            case R.id.nav_manage_job_emp:
                                Intent i4 = new Intent(EMP_ViewResume.this, EMP_ManageJobs.class);
                                startActivity(i4);
                                break;
                            case R.id.nav_website_emp:
                                Intent EqualsSite = new Intent(android.content.Intent.ACTION_VIEW);
                                EqualsSite.setData(Uri.parse("http://www.equals.org.ph"));
                                startActivity(EqualsSite);
                                break;

                            case R.id.nav_logout_emp:

                                AlertDialog.Builder alert = new AlertDialog.Builder(EMP_ViewResume.this);
                                alert.setMessage("By clicking Yes, you will return to the login screen.").setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                FirebaseAuth.getInstance().signOut();
                                                //closing activity
                                                finish();
                                                //starting login activity
                                                startActivity(new Intent(EMP_ViewResume.this, LoginActivity_emp.class));
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
        }); //drawer



        pToolbar = findViewById(R.id.nav_action_bar_emp);
        setSupportActionBar(pToolbar);
        pDrawerLayout = findViewById(R.id.drawerLayout_emp);
        pToggle = new ActionBarDrawerToggle(this, pDrawerLayout, R.string.open_emp, R.string.close_emp);

        pDrawerLayout.addDrawerListener(pToggle);
        pToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {

        if (pToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
}