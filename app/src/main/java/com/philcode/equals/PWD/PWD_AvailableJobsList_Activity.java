package com.philcode.equals.PWD;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.app.ProgressDialog;
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

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.philcode.equals.R;

public class PWD_AvailableJobsList_Activity extends AppCompatActivity {

    private TabLayout tabLayout_availableJobs;
    private ViewPager2 viewpager_availableJobs;
    private PWD_AvailableJobs_FragmentAdapter availableJobs_fragmentAdapter;

    private static final String TAG = "PWD_AvailableJobs_View";
    private FirebaseAuth firebaseAuth;
    private DrawerLayout pDrawerLayout;
    private FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    private String userId = user.getUid();
    private ActionBarDrawerToggle pToggle;
    private Toolbar pToolbar;
    private NavigationView pNavigation;
    TextView fullname, pwd_email;
    Menu menu;
    ImageView imgProfile = null;

    private static final int PICK_FILE = 1 ;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pwd_pwdavailablejobslist_view);
        //drawer layout
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentFirebaseUser.getUid();
        final String email = currentFirebaseUser.getEmail().toString();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("PWD/" + uid);

        progressDialog = new ProgressDialog(this);
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {

                pNavigation = findViewById(R.id.navigation_view_pwd);
                menu = pNavigation.getMenu();

                if (snapshot.child("typeOfDisability0").exists()) {
                    //Kung may laman daw
                    MenuItem target1 = menu.findItem(R.id.nav_job_orthopedic);
                    target1.setVisible(true);
                }else {
                    MenuItem target1 = menu.findItem(R.id.nav_job_orthopedic);
                    target1.setVisible(false);
                }

                if (snapshot.child("typeOfDisability1").exists()) {
                    //Kung may laman daw
                    MenuItem target2 = menu.findItem(R.id.nav_job_visual);
                    target2.setVisible(true);
                }else {
                    MenuItem target3 = menu.findItem(R.id.nav_job_visual);
                    target3.setVisible(false);
                }

                if (snapshot.child("typeOfDisability2").exists()) {
                    //Kung may laman daw
                    MenuItem target3 = menu.findItem(R.id.nav_job_hearing);
                    target3.setVisible(true);
                }else{
                    MenuItem target1 = menu.findItem(R.id.nav_job_hearing);
                    target1.setVisible(false);
                }

                if (snapshot.child("typeOfDisability3").exists()) {
                    //Kung may laman daw
                    MenuItem target4 = menu.findItem(R.id.nav_job_speech);
                    target4.setVisible(true);
                }else{
                    MenuItem target4 = menu.findItem(R.id.nav_job_speech);
                    target4.setVisible(false);
                }
                //add for speech impediment
                if (snapshot.child("typeOfDisability4").exists()) {
                    //Kung may laman daw
                    MenuItem target4 = menu.findItem(R.id.nav_job_more);
                    target4.setVisible(true);
                }else{
                    MenuItem target4 = menu.findItem(R.id.nav_job_more);
                    target4.setVisible(false);
                }

                if(snapshot.hasChild("resumeFile")){
                    MenuItem target5 = menu.findItem(R.id.nav_view_resume);
                    target5.setVisible(true);
                }else{
                    MenuItem target5 = menu.findItem(R.id.nav_view_resume);
                    target5.setVisible(false);
                }

                if(snapshot.hasChild("resumeFile")){
                    MenuItem target5 = menu.findItem(R.id.nav_upload_resume);
                    target5.setTitle("Re-Upload Resume");
                }else if(!snapshot.hasChild("resumeFile")){
                    MenuItem target5 = menu.findItem(R.id.nav_upload_resume);
                    target5.setTitle("Upload Resume");
                }

                pNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                        int id = menuItem.getItemId();
                        if (id == R.id.nav_home_pwd) {
                            Intent i = new Intent(PWD_AvailableJobsList_Activity.this, a_PWDContentMainActivity.class);
                            startActivity(i);
                        } else if (id == R.id.nav_profile_pwd) {
                            Intent i2 = new Intent(PWD_AvailableJobsList_Activity.this, PWD_EditProfile_ViewActivity.class);
                            startActivity(i2);
                        }else if(id == R.id.nav_view_resume){
                            final String resumeFile = snapshot.child("resumeFile").getValue().toString();
                            Intent viewResume_intent = new Intent(PWD_AvailableJobsList_Activity.this, PWD_ViewResume_Activity.class);
                            viewResume_intent.putExtra("RESUME_URL", resumeFile);
                            startActivity(viewResume_intent);

                        }else if (id == R.id.nav_upload_resume) {
                            AlertDialog.Builder alert =  new AlertDialog.Builder(PWD_AvailableJobsList_Activity.this);
                            alert.setMessage("Resume file format should be in PDF.").setCancelable(false)
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                            intent.setType("docx/*");
                                            intent.setType("doc/*");
                                            intent.setType("application/pdf");
                                            startActivityForResult(intent, PICK_FILE);
                                        }
                                    });
                            AlertDialog alertDialog = alert.create();
                            alertDialog.setTitle("Resume Upload File Format");
                            alertDialog.show();
                        }else if (id == R.id.nav_job_orthopedic) {
                            Intent j1 = new Intent(PWD_AvailableJobsList_Activity.this, PWD_AvailableJobsList_Activity.class);
                            j1.putExtra("PWD_DISABILITY", "Orthopedic Disability");
                            startActivity(j1);
                        } else if (id == R.id.nav_job_visual) {
                            Intent j1 = new Intent(PWD_AvailableJobsList_Activity.this, PWD_AvailableJobsList_Activity.class);
                            j1.putExtra("PWD_DISABILITY", "Partial Visual Disability");
                            startActivity(j1);
                        } else if (id == R.id.nav_job_hearing) {
                            Intent j1 = new Intent(PWD_AvailableJobsList_Activity.this, PWD_AvailableJobsList_Activity.class);
                            j1.putExtra("PWD_DISABILITY", "Hearing Disability");
                            startActivity(j1);
                        } else if(id == R.id.nav_job_speech){
                            Intent j1 = new Intent(PWD_AvailableJobsList_Activity.this, PWD_AvailableJobsList_Activity.class);
                            j1.putExtra("PWD_DISABILITY", "Speech Impediment");
                            startActivity(j1);
                        } else if (id == R.id.nav_job_more) {
                            Intent j1 = new Intent(PWD_AvailableJobsList_Activity.this, PWD_AvailableJobsList_Activity.class);
                            j1.putExtra("PWD_DISABILITY", "Other Disability");
                            startActivity(j1);
                        }
                        else if (id == R.id.nav_logout_pwd) {

                            AlertDialog.Builder alert =  new AlertDialog.Builder(PWD_AvailableJobsList_Activity.this);
                            alert.setMessage("By clicking Yes, you will return to the login screen.").setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            FirebaseAuth.getInstance().signOut();
                                            //starting login activity
                                            startActivity(new Intent(PWD_AvailableJobsList_Activity.this, PWD_LoginActivity.class));
                                            //closing activity
                                            finish();
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

                        } else if (id == R.id.nav_website_pwd) {
                            Intent EqualsSite = new Intent(android.content.Intent.ACTION_VIEW);
                            EqualsSite.setData(Uri.parse("http://www.equals.org.ph"));
                            startActivity(EqualsSite);
                        }
                        return false;
                    }
                });
                final String dp1 = snapshot.child("pwdProfilePic").getValue().toString();
                final String firstName  = snapshot.child("firstName").getValue().toString();
                final String lastName  = snapshot.child("lastName").getValue().toString();

                View hView =pNavigation.inflateHeaderView(R.layout.pwd_navigation_header);
                imgProfile = hView.findViewById(R.id.profile_pic_pwd);
                Glide.with(getApplicationContext()).load(dp1).into(imgProfile);
                fullname = hView.findViewById(R.id.profile_name_pwd);
                fullname.setText(firstName + " " +lastName);
                pwd_email = hView.findViewById(R.id.profile_email);
                pwd_email.setText(email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        pToolbar = findViewById(R.id.nav_action_bar_emp);
        setSupportActionBar(pToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tabLayout_availableJobs = findViewById(R.id.tabLayout_availableJobs);
        viewpager_availableJobs = findViewById(R.id.viewpager_availableJobs);
        availableJobs_fragmentAdapter = new PWD_AvailableJobs_FragmentAdapter(getSupportFragmentManager(), getLifecycle());
        viewpager_availableJobs.setAdapter(availableJobs_fragmentAdapter);

        pDrawerLayout = findViewById(R.id.drawerLayout_availablejobs_pwd);
        pToggle = new ActionBarDrawerToggle(this, pDrawerLayout, R.string.open_pwd, R.string.close_pwd);

        pDrawerLayout.addDrawerListener(pToggle);
        pToggle.syncState();

        tabLayout_availableJobs.addTab(tabLayout_availableJobs.newTab().setText("Fully matched"));
        tabLayout_availableJobs.addTab(tabLayout_availableJobs.newTab().setText("Semi matched"));
        tabLayout_availableJobs.addTab(tabLayout_availableJobs.newTab().setText("Unmatched"));

        tabLayout_availableJobs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewpager_availableJobs.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewpager_availableJobs.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout_availableJobs.selectTab(tabLayout_availableJobs.getTabAt(position));
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final Intent i = new Intent(PWD_AvailableJobsList_Activity.this, PWD_AvailableJobsList_Activity.class);
        if(requestCode == PICK_FILE){
            if(resultCode == RESULT_OK){
                Uri FileUri = data.getData();
                StorageReference Folder = FirebaseStorage.getInstance().getReference().child("Resumes").child(userId);
                final StorageReference file_name = Folder.child("file"+FileUri.getLastPathSegment());
                progressDialog.setTitle("Uploading...");
                progressDialog.show();
                file_name.putFile(FileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                file_name.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(final Uri uri) {
                                        final DatabaseReference resume = FirebaseDatabase.getInstance().getReference("PWD").child(userId);
                                        resume.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                startActivity(i);
                                                progressDialog.dismiss();
                                                resume.child("resumeFile").setValue(String.valueOf(uri));
                                                startActivity(new Intent(getApplicationContext(), PWD_AvailableJobsList_Activity.class));
                                                Toast.makeText(PWD_AvailableJobsList_Activity.this, "Resume Uploaded", Toast.LENGTH_SHORT).show();
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                Toast.makeText(PWD_AvailableJobsList_Activity.this, databaseError.getCode(),
                                                        Toast.LENGTH_LONG).show();
                                            }
                                        });

                                    }
                                });

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(PWD_AvailableJobsList_Activity.this, "Invalid file type", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                        .getTotalByteCount());
                                progressDialog.setMessage("Uploaded " + (int) progress + "%");
                            }
                        });
            }

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (pToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}