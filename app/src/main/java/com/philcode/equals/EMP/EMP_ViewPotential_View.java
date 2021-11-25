package com.philcode.equals.EMP;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.philcode.equals.EMP.EMP_ViewPotential_All;
import com.philcode.equals.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class EMP_ViewPotential_View extends AppCompatActivity{

    private FloatingActionButton fab_main, fab1_call, fab2_mail;
    private Animation fab_open, fab_close, fab_clock, fab_anticlock;
    TextView textview_call, textview_mail;

    Boolean isOpen = false;


    // Folder path for Firebase Storage.
    String Storage_Path = "Job_Offers/";
    // Root Database Name for Firebase Database.
    String Database_Path = "Job_Offers";
    String huhu, text;
    private Uri FilePathUri;
    StorageReference storageReference;
    DatabaseReference pwd_reference;

    // Assign FirebaseStorage instance to storageReference.

    Calendar cal = Calendar.getInstance();
    Calendar currentCal = Calendar.getInstance();
    Date currentDate = Calendar.getInstance().getTime();
    SimpleDateFormat df = new SimpleDateFormat("MMMM-dd-yyyy");
    String formattedDate = df.format(currentDate);
    String date = new SimpleDateFormat("MMMM-dd-yyyy", Locale.getDefault()).format(new Date());
    //String formattedDate = df.format(c);
    SimpleDateFormat format = new SimpleDateFormat("MMMM dd, yyyy");
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

    private static final String TAG = "EMP_ViewPotential_View";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentFirebaseUser.getUid();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("Employers/" + uid);
        final String email = currentFirebaseUser.getEmail().toString();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.emp_viewpotential_view);
        //animation
        fab_main = findViewById(R.id.fab);
        fab1_call = findViewById(R.id.fab1);
        fab2_mail = findViewById(R.id.fab2);
        textview_call = findViewById(R.id.textview_call);
        textview_mail = findViewById(R.id.textview_sendEmail);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_clock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_clock);
        fab_anticlock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_anticlock);

        //call get Info here...

        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isOpen) {

                    textview_mail.setVisibility(View.INVISIBLE);
                    textview_call.setVisibility(View.INVISIBLE);
                    fab2_mail.startAnimation(fab_close);
                    fab1_call.startAnimation(fab_close);
                    fab_main.startAnimation(fab_anticlock);
                    fab2_mail.setClickable(false);
                    fab1_call.setClickable(false);
                    isOpen = false;
                } else {
                    textview_mail.setVisibility(View.VISIBLE);
                    textview_call.setVisibility(View.VISIBLE);
                    fab2_mail.startAnimation(fab_open);
                    fab1_call.startAnimation(fab_open);
                    fab_main.startAnimation(fab_clock);
                    fab2_mail.setClickable(true);
                    fab1_call.setClickable(true);
                    isOpen = true;
                }

            }
        });


        fab2_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Intent.ACTION_SEND);
                i.setData(Uri.parse("email"));
                TextView email = findViewById(R.id.displayEmail);
                String [] emailP = {email.getText().toString()};
                i.putExtra(Intent.EXTRA_EMAIL, emailP);
                i.putExtra(Intent.EXTRA_SUBJECT, "Invitation for Job Interview");
                i.putExtra(Intent.EXTRA_TEXT, "Put your details here.");
                i.setType("message/rfc822");
                Intent chooser = Intent.createChooser(i,"Choose Application");
                startActivity(chooser);

            }
        });

        fab1_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_CALL);
                TextView contact = findViewById(R.id.displayContact);
                String contactNum = contact.getText().toString();
                i.setData(Uri.parse("tel:"+contactNum));
                if(ActivityCompat.checkSelfPermission(EMP_ViewPotential_View.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                    requestPermission();
                }else{
                    startActivity(i);
                }
            }
            private void requestPermission(){
                ActivityCompat.requestPermissions(EMP_ViewPotential_View.this, new String[] {Manifest.permission.CALL_PHONE},1);
            }

        });

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
                                Intent i = new Intent(EMP_ViewPotential_View.this, a_EmployeeContentMainActivity.class);
                                startActivity(i);
                                break;
                            case R.id.nav_profile_emp:
                                Intent i2 = new Intent(EMP_ViewPotential_View.this, EMP_EditProfile_Activity.class);
                                startActivity(i2);
                                break;
                            case R.id.nav_post_job_emp:
                                Intent ii = new Intent(EMP_ViewPotential_View.this, Emp_PostJob.class);
                                startActivity(ii);
                                break;
                            case R.id.nav_manage_job_emp:
                                Intent i4 = new Intent(EMP_ViewPotential_View.this, EMP_ManageJobs.class);
                                startActivity(i4);
                                break;
                            case R.id.nav_website_emp:
                                Intent EqualsSite = new Intent(android.content.Intent.ACTION_VIEW);
                                EqualsSite.setData(Uri.parse("http://www.equals.org.ph"));
                                startActivity(EqualsSite);
                                break;

                            case R.id.nav_logout_emp:

                                AlertDialog.Builder alert =  new AlertDialog.Builder(EMP_ViewPotential_View.this);
                                alert.setMessage("By clicking Yes, you will return to the login screen.").setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                FirebaseAuth.getInstance().signOut();
                                                //closing activity
                                                finish();
                                                //starting login activity
                                                startActivity(new Intent(EMP_ViewPotential_View.this, LoginActivity_emp.class));
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



        pToolbar = findViewById(R.id.nav_action_bar_emp);
        setSupportActionBar(pToolbar);

        pDrawerLayout = findViewById(R.id.drawerPWDUser);
        pToggle = new ActionBarDrawerToggle(this, pDrawerLayout, R.string.open_pwd, R.string.close_pwd);

        pDrawerLayout.addDrawerListener(pToggle);
        pToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }
    public void getApplicantInfo(){
        pwd_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void setApplicantInfo(){

    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){

        if (pToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickBtnApprove(View view) {
        return;
    }
}
