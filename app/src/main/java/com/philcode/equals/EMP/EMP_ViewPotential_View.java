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
    DatabaseReference reference;

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
        getIncomingIntent();

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

    private void getIncomingIntent() {
        if (getIntent().hasExtra("pwdProfilePic")
                && getIntent().hasExtra("firstName"+" "+"lastName")
                && getIntent().hasExtra("contact")
                || getIntent().hasExtra("typeofDisability1")
                || getIntent().hasExtra("typeofDisability2")
                || getIntent().hasExtra("typeofDisability3")
                || getIntent().hasExtra("typeofDisabilityMore")
                || getIntent().hasExtra("jobSkill1")
                || getIntent().hasExtra("jobSkill2")
                || getIntent().hasExtra("jobSkill3")
                || getIntent().hasExtra("jobSkill5")
                || getIntent().hasExtra("jobSkill5")
                || getIntent().hasExtra("jobSkill6")
                || getIntent().hasExtra("jobSkill7")
                || getIntent().hasExtra("jobSkill8")
                || getIntent().hasExtra("jobSkill9")
                || getIntent().hasExtra("jobSkill10")
                || getIntent().hasExtra("typeStatus")
                || getIntent().hasExtra("address")
                || getIntent().hasExtra("city")
                || getIntent().hasExtra("pwdIdCardNum")
                || getIntent().hasExtra("email")
        ){
            //Log.d(TAG, "getIncomingIntent: found intent extras.");

            String typeStatus = getIntent().getStringExtra("typeStatus");
            String firstName = getIntent().getStringExtra("firstName");
            String lastName = getIntent().getStringExtra("lastName");
            String email = getIntent().getStringExtra("email");
            String address = getIntent().getStringExtra("address");
            String city = getIntent().getStringExtra("city");
            String pwdcard = getIntent().getStringExtra("pwdIdCardNum");
            String contact = getIntent().getStringExtra("contact");
            String typeOfDisability1 = getIntent().getStringExtra("typeOfDisability1");
            String typeOfDisability2 = getIntent().getStringExtra("typeOfDisability2");
            String typeOfDisability3 = getIntent().getStringExtra("typeOfDisability3");
            String typeOfDisabilityMore = getIntent().getStringExtra("typeOfDisabilityMore");

            String jobSkill1 = getIntent().getStringExtra("jobSkill1");
            String jobSkill2 = getIntent().getStringExtra("jobSkill2");
            String jobSkill3 = getIntent().getStringExtra("jobSkill3");
            String jobSkill4 = getIntent().getStringExtra("jobSkill4");
            String jobSkill5 = getIntent().getStringExtra("jobSkill5");
            String jobSkill6 = getIntent().getStringExtra("jobSkill6");
            String jobSkill7 = getIntent().getStringExtra("jobSkill7");
            String jobSkill8 = getIntent().getStringExtra("jobSkill8");
            String jobSkill9 = getIntent().getStringExtra("jobSkill9");
            String jobSkill10 = getIntent().getStringExtra("jobSkill10");


            //added
            String educationalAttainment = getIntent().getStringExtra("educationalAttainment");
            String categorySkill = getIntent().getStringExtra("skill");
            String workExperience = getIntent().getStringExtra("workExperience");
            String primarySkill1 = getIntent().getStringExtra("primarySkill1");
            String primarySkill2 = getIntent().getStringExtra("primarySkill2");
            String primarySkill3 = getIntent().getStringExtra("primarySkill3");
            String primarySkill4 = getIntent().getStringExtra("primarySkill4");
            String primarySkill5 = getIntent().getStringExtra("primarySkill5");
            String primarySkill6 = getIntent().getStringExtra("primarySkill6");
            String primarySkill7 = getIntent().getStringExtra("primarySkill7");
            String primarySkill8 = getIntent().getStringExtra("primarySkill8");
            String primarySkill9 = getIntent().getStringExtra("primarySkill9");
            String primarySkill10 = getIntent().getStringExtra("primarySkill10");
            String primarySkillOther = getIntent().getStringExtra("primarySkillOther");

            String pwdProfilePic= getIntent().getStringExtra("pwdProfilePic");


            setImage(typeStatus, firstName,lastName,email, address,city,contact, typeOfDisability1, typeOfDisability2, typeOfDisability3,typeOfDisabilityMore,
                    jobSkill1, jobSkill2, jobSkill3, jobSkill4, jobSkill5, jobSkill6, jobSkill7, jobSkill8,
                    jobSkill9, jobSkill10,
                    pwdProfilePic, educationalAttainment, categorySkill,
                    workExperience, primarySkill1, primarySkill2, primarySkill3, primarySkill4, primarySkill5, primarySkill6,primarySkill7
                    ,primarySkill8, primarySkill9, primarySkill10, primarySkillOther);

        }
    }

    private void setImage(String typeStatus, String firstName, String lastName, String email, String address, String city,
                          String contact , String typeOfDisability1, String typeOfDisability2,
                          String typeOfDisability3, String typeOfDisabilityMore,
                          String jobSkill1, String jobSkill2, String jobSkill3,
                          String jobSkill4, String jobSkill5, String jobSkill6,
                          String jobSkill7, String jobSkill8, String jobSkill9, String jobSkill10,String pwdProfilePic, String educationalAttainment, String categorySkill,
                          String workExperience,String primarySkill1,String primarySkill2, String primarySkill3, String primarySkill4, String primarySkill5,String primarySkill6,
                          String primarySkill7,String primarySkill8, String primarySkill9,String primarySkill10, String primarySkillOther){


        TextView displayTypeOfDisability1 = findViewById(R.id.displayTypeOfDisability1);
        TextView displayTypeOfDisability2 = findViewById(R.id.displayTypeOfDisability2);
        TextView displayTypeOfDisability3 = findViewById(R.id.displayTypeOfDisability3);
        TextView displayTypeOfDisabilityMore = findViewById(R.id.displayTypeOfDisabilityMore);


        TextView displaySkill1 = findViewById(R.id.displaySkill1);
        TextView displaySkill2 = findViewById(R.id.displaySkill2);
        TextView displaySkill3 = findViewById(R.id.displaySkill3);
        TextView displaySkill4 = findViewById(R.id.displaySkill4);
        TextView displaySkill5 = findViewById(R.id.displaySkill5);
        TextView displaySkill6 = findViewById(R.id.displaySkill6);
        TextView displaySkill7 = findViewById(R.id.displaySkill7);
        TextView displaySkill8 = findViewById(R.id.displaySkill8);
        TextView displaySkill9 = findViewById(R.id.displaySkill9);
        TextView displaySkill10 = findViewById(R.id.displaySkill10);

        ImageView images = findViewById(R.id.pwdProfilePic);



        Glide.with(this)
                .asBitmap()
                .load(pwdProfilePic)
                .into(images);


        TextView displayName = findViewById(R.id.displayName);
        TextView displayEmail = findViewById(R.id.displayEmail);
        TextView displayAddress = findViewById(R.id.displayAddress);
        TextView displayContact = findViewById(R.id.displayContact);
       // TextView displayCity = findViewById(R.id.displayCity);

        TextView displayEducationalAttainment = findViewById(R.id.displayEducationalAttainment);
        displayEducationalAttainment.setText(educationalAttainment);
        TextView displayCategorySkill = findViewById(R.id.displayCategorySkill);
        displayCategorySkill.setText(categorySkill);
        TextView displayTotalWorkExperience = findViewById(R.id.displayTotalWorkExperience);
        displayTotalWorkExperience.setText(workExperience);

        TextView displaypwdpic = findViewById(R.id.profile_pic_pwd);

        TextView displayPrimarySkill1 = findViewById(R.id.displayPrimarySkill1);
        TextView displayPrimarySkill2 = findViewById(R.id.displayPrimarySkill2);
        TextView displayPrimarySkill3 = findViewById(R.id.displayPrimarySkill3);
        TextView displayPrimarySkill4 = findViewById(R.id.displayPrimarySkill4);
        TextView displayPrimarySkill5 = findViewById(R.id.displayPrimarySkill5);
        TextView displayPrimarySkill6 = findViewById(R.id.displayPrimarySkill6);
        TextView displayPrimarySkill7 = findViewById(R.id.displayPrimarySkill7);
        TextView displayPrimarySkill8 = findViewById(R.id.displayPrimarySkill8);
        TextView displayPrimarySkill9 = findViewById(R.id.displayPrimarySkill9);
        TextView displayPrimarySkill10 = findViewById(R.id.displayPrimarySkill10);
        TextView displayPrimarySkillOther = findViewById(R.id.displayPrimarySkillOther);


        displayName.setText(firstName+" "+lastName);
        displayEmail.setText(email);
        displayAddress.setText(address + ", "+ city);

        displayContact.setText(contact);
        displayTypeOfDisability1.setText(typeOfDisability1);
        displayTypeOfDisability2.setText(typeOfDisability2);
        displayTypeOfDisability3.setText(typeOfDisability3);
        displayTypeOfDisabilityMore.setText(typeOfDisabilityMore);

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

        if(primarySkill1 == null||primarySkill1.equals("")){
            displayPrimarySkill1.setVisibility(View.GONE);
        }else {
            displayPrimarySkill1.setText(primarySkill1);
        }

        if(primarySkill2 == null||primarySkill2.equals("")){
            displayPrimarySkill2.setVisibility(View.GONE);
        }else {
            displayPrimarySkill2.setText(primarySkill2);
        }

        if(primarySkill3 == null||primarySkill3.equals("")){
            displayPrimarySkill3.setVisibility(View.GONE);
        }else {
            displayPrimarySkill3.setText(primarySkill3);
        }

        if(primarySkill4 == null||primarySkill4.equals("")){
            displayPrimarySkill4.setVisibility(View.GONE);
        }else {
            displayPrimarySkill4.setText(primarySkill4);
        }

        if(primarySkill5 == null||primarySkill5.equals("")){
            displayPrimarySkill5.setVisibility(View.GONE);
        }else {
            displayPrimarySkill5.setText(primarySkill5);
        }

        if(primarySkill6 == null||primarySkill6.equals("")){
            displayPrimarySkill6.setVisibility(View.GONE);
        }else {
            displayPrimarySkill6.setText(primarySkill6);
        }

        if(primarySkill7 == null||primarySkill7.equals("")){
            displayPrimarySkill7.setVisibility(View.GONE);
        }else {
            displayPrimarySkill7.setText(primarySkill7);
        }

        if(primarySkill8 == null||primarySkill8.equals("")){
            displayPrimarySkill8.setVisibility(View.GONE);
        }else {
            displayPrimarySkill8.setText(primarySkill8);
        }

        if(primarySkill9 == null||primarySkill9.equals("")){
            displayPrimarySkill9.setVisibility(View.GONE);
        }else {
            displayPrimarySkill9.setText(primarySkill9);
        }

        if(primarySkill10 == null||primarySkill10.equals("")){
            displayPrimarySkill10.setVisibility(View.GONE);
        }else {
            displayPrimarySkill10.setText(primarySkill10);
        }

        if(primarySkillOther == null||primarySkillOther.equals("")){
            displayPrimarySkillOther.setVisibility(View.GONE);
        }else {
            displayPrimarySkillOther.setText(primarySkillOther);
        }


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
