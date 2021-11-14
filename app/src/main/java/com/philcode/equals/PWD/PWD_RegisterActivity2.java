package com.philcode.equals.PWD;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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
import com.philcode.equals.EMP.LoginActivity_emp;
import com.philcode.equals.EMP.a_EmployeeContentMainActivity;
import com.philcode.equals.R;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PWD_RegisterActivity2 extends AppCompatActivity{

    //firebase auth object
    private FirebaseAuth firebaseAuth;
    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference work_databaseRef, mDatabase, pwdWorks;

    String x;
    final Calendar myCalendar = Calendar.getInstance();

    // Folder path for Firebase Storage.
    String Storage_Path = "PWD_Reg_Form/";
    // Root Database Name for Firebase Database.
    // String Database_Path = "PWD_Reg_Form";

    //view objects
    private TextView textViewUserEmail, skillSelected;
    private Button buttonAddWork, buttonSave;
    private Spinner primary_skillsCategory;
    private RadioGroup rgWorkExperience, rgEducAttainment;
    private RadioButton rbEduc, rbWorkExperience, rbWithExperience, rbWithoutExperience;
    private RecyclerView work_recyclerView;

    boolean[] checkedItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();
    ArrayList<Integer> title = new ArrayList<>();
    int count = 10;
    int count2 = 0;
    int count3 = 0;
    int wCount = 0;
    int countw = 0;
    int numberOfPrimarySkills = 0;

    private List<PWD_AddWorkInformation> work_list;
    private PWD_WorkExperienceAdapter work_adapter;

    String check_Ortho = "";
    String check_Vis = "";
    String check_Hear = "";
    String check_More = "";

    String job_1 = "";
    String job_2 = "";
    String job_3 = "";
    String job_4 = "";
    String job_5 = "";
    String job_6 = "";
    String job_7 = "";
    String job_8 = "";
    String job_9 = "";
    String job_10 = "";

    private String educAttainment = "";
    private String workExperience = "";

    String[] pwdPrimarySkills = new String[10];

    String m1 = "";
    String m2 = "";
    String m3 = "";
    String m4 = "";
    String m5 = "";
    String m6 = "";
    String m7 = "";
    String m8 = "";
    String m9 = "";
    String m10 = "";

    String primarySkill1;
    String primarySkill2;
    String primarySkill3;
    String primarySkill4;
    String primarySkill5;
    String primarySkill6;
    String primarySkill7;
    String primarySkill8;
    String primarySkill9;
    String primarySkill10;
    String primarySkillOther;

    private CheckBox checkOrtho, checkHear, checkVis, checkMore;
    private CheckBox job1, job2, job3, job4, job5, job6, job7, job8, job9, job10;
    int PICK_IMAGE_REQUEST = 7;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.pwd_activity_register2);
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("PWD");

        firebaseAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String userz = user.getUid();
        rbWithoutExperience = findViewById(R.id.radio_10);
        rbWithExperience = findViewById(R.id.radio_11);
        work_recyclerView = findViewById(R.id.workRecyclerView);
        work_recyclerView.setHasFixedSize(true);
        work_recyclerView.setLayoutManager(new LinearLayoutManager(PWD_RegisterActivity2.this));


        buttonAddWork = findViewById(R.id.add_work);

        final DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference().child("Category/");
        categoryRef.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {

                //  final List<String> skill = new ArrayList<String>();
                final List<String> category = new ArrayList<String>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Map<String, Object> data = (Map<String, Object>) snapshot.getValue();
                    category.add(data.get("skill").toString());
                }
                ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(PWD_RegisterActivity2.this, android.R.layout.simple_spinner_item, category) {
                    @Override
                    public View getDropDownView(int position, View convertView,
                                                ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);
                        TextView tv = (TextView) view;
                        if (position == 0) {
                            // Hide the second item from Spinner
                            tv.setVisibility(View.GONE);
                        } else {
                            tv.setVisibility(View.VISIBLE);
                        }
                        return view;
                    }
                };
                categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                primary_skillsCategory.setAdapter(categoryAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        buttonSave = (Button) findViewById(R.id.buttonSave2);


        primary_skillsCategory = findViewById(R.id.skillSpinner);
        skillSelected = findViewById(R.id.selectedSkills);
        rgEducAttainment = findViewById(R.id.rg_educ);
        rgWorkExperience = findViewById(R.id.workexperience);
        rgWorkExperience = findViewById(R.id.workexperience);
        final int selectExperience = rgWorkExperience.getCheckedRadioButtonId();
        rbWorkExperience = findViewById(selectExperience);
        rgWorkExperience.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (rbWithExperience.isChecked()) {
                    workExperience="With Experience";
                    buttonAddWork.setVisibility(View.VISIBLE);
                    work_recyclerView.setVisibility(View.VISIBLE);

                }
                if (rbWithoutExperience.isChecked()) {
                    workExperience="Without Experience";
                    buttonAddWork.setVisibility(View.GONE);
                    work_recyclerView.setVisibility(View.GONE);
                }
            }
        });
        if (skillSelected == null) {
            skillSelected.setVisibility(View.GONE);
        }

        checkOrtho = findViewById(R.id.checkOrtho);
        checkVis = findViewById(R.id.checkVis);
        checkHear = findViewById(R.id.checkHear);
        checkMore = findViewById(R.id.checkMore);

        //Skills
        job1 = findViewById(R.id.typeOfSkills1);
        job2 = findViewById(R.id.typeOfSkills2);
        job3 = findViewById(R.id.typeOfSkills3);
        job4 = findViewById(R.id.typeOfSkills4);
        job5 = findViewById(R.id.typeOfSkills5);
        job6 = findViewById(R.id.typeOfSkills6);
        job7 = findViewById(R.id.typeOfSkills7);
        job8 = findViewById(R.id.typeOfSkills8);
        job9 = findViewById(R.id.typeOfSkills9);
        job10 = findViewById(R.id.typeOfSkills10);


        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
                uploadImage();
            }
        });

        /*primary_skillsCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { //ON ITEM SELECTED should do nothing but store data.
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mUserItems.clear();

                final DatabaseReference primarySkillsRef = FirebaseDatabase.getInstance().getReference().child("Category");
                x = primary_skillsCategory.getSelectedItem().toString();
                if (x.equals("Click to select value")) {

                } else {
                    Toast.makeText(getApplicationContext(), "Selected: " + x, Toast.LENGTH_LONG).show();
                    *//*primarySkillsRef.orderByChild("skill").equalTo(x).addValueEventListener(new ValueEventListener() {
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                String parent = dataSnapshot1.getKey();
                                //     Toast.makeText(getApplicationContext(), parent, Toast.LENGTH_LONG).show();


                                final DatabaseReference parentRef = FirebaseDatabase.getInstance().getReference().child("Category").child(parent);
                                parentRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        count=10;
                                        count2=0;
                                        if(dataSnapshot.child("skill1").getValue(String.class).equals("")){
                                            count--;
                                            m1="";
                                        }
                                        else{
                                            m1 = dataSnapshot.child("skill1").getValue(String.class);
                                            count2++;

                                        }
                                        if(dataSnapshot.child("skill2").getValue(String.class).equals("")){
                                            count--;
                                            m2="";
                                        }else{
                                            m2 = dataSnapshot.child("skill2").getValue(String.class);
                                            count2++;
                                        }
                                        if(dataSnapshot.child("skill3").getValue(String.class).equals("")){
                                            count--;
                                            m3="";
                                        }else{
                                            m3 = dataSnapshot.child("skill3").getValue(String.class);
                                            count2++;
                                        }
                                        if(dataSnapshot.child("skill4").getValue(String.class).equals("")){
                                            count--;
                                            m4="";
                                        }else{
                                            m4 = dataSnapshot.child("skill4").getValue(String.class);
                                            count2++;
                                        }
                                        if(dataSnapshot.child("skill5").getValue(String.class).equals("")){
                                            count--;
                                            m5="";
                                        }else{
                                            m5 = dataSnapshot.child("skill5").getValue(String.class);
                                            count2++;
                                        }
                                        if(dataSnapshot.child("skill6").getValue(String.class).equals("")){
                                            count--;
                                            m6="";
                                        }else{
                                            m6 = dataSnapshot.child("skill6").getValue(String.class);
                                            count2++;
                                        }
                                        if(dataSnapshot.child("skill7").getValue(String.class).equals("")){
                                            count--;
                                            m7="";
                                        }else{
                                            m7 = dataSnapshot.child("skill7").getValue(String.class);
                                            count2++;
                                        }
                                        if(dataSnapshot.child("skill8").getValue(String.class).equals("")){
                                            count--;
                                            m8="";

                                        }else{
                                            m8 = dataSnapshot.child("skill8").getValue(String.class);
                                            count2++;
                                        }
                                        if(dataSnapshot.child("skill9").getValue(String.class).equals("")){
                                            count--;
                                            m9="";

                                        }else{
                                            m9 = dataSnapshot.child("skill9").getValue(String.class);
                                            count2++;
                                        }
                                        if(dataSnapshot.child("skill10").getValue(String.class).equals("")){
                                            count--;
                                            m10="";

                                        }else{
                                            m10 = dataSnapshot.child("skill10").getValue(String.class);
                                            count2++;
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
                    });*//* //Delete this???

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });*/


        buttonAddWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View view = LayoutInflater.from(PWD_RegisterActivity2.this).inflate(R.layout.working_experience,null);
                final EditText jobposition = view.findViewById(R.id.work_jobposition);
                final EditText companyname = view.findViewById(R.id.work_company);
                final EditText datestarted = view.findViewById(R.id.date_started);
                final EditText dateended = view.findViewById(R.id.date_ended);

                final TextView txtstart = view.findViewById(R.id.txtstart);
                final TextView txtend = view.findViewById(R.id.txtend);
                final TextView txtResult = view.findViewById(R.id.txtResult);

                final Button btnSelectstart = view.findViewById(R.id.btnSelectstart);
                final Button btnSelectend = view.findViewById(R.id.btnSelectend);
                final Spinner spinnercategory = view.findViewById(R.id.spinnerCategory);

                final DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference().child("Category/");
                categoryRef.addValueEventListener(new ValueEventListener() {
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        //  final List<String> skill = new ArrayList<String>();
                        final List<String> category = new ArrayList<String>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Map<String, Object> data = (Map<String, Object>) snapshot.getValue();
                            category.add(data.get("skill").toString());
                        }
                        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(PWD_RegisterActivity2.this, android.R.layout.simple_spinner_item, category) {
                            @Override
                            public View getDropDownView(int position, View convertView,
                                                        ViewGroup parent) {
                                View view = super.getDropDownView(position, convertView, parent);
                                TextView tv = (TextView) view;
                                if (position == 0) {
                                    // Hide the second item from Spinner
                                    tv.setVisibility(View.GONE);
                                } else {
                                    tv.setVisibility(View.VISIBLE);
                                }
                                return view;
                            }
                        };
                        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnercategory.setAdapter(categoryAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                AlertDialog.Builder alertWork = new AlertDialog.Builder(PWD_RegisterActivity2.this);
                alertWork.setView(view);
                {
                    final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear,
                                              int dayOfMonth) {
                            // TODO Auto-generated method stub
                            myCalendar.set(Calendar.YEAR, year);
                            myCalendar.set(Calendar.MONTH, monthOfYear);
                            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            String myFormat = "MMM/dd/yyyy"; //In which you need put here
                            String year1 = "yyyy"; //In which you need put here
                            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                            SimpleDateFormat yearformat = new SimpleDateFormat(year1, Locale.US);
                            datestarted.setText(sdf.format(myCalendar.getTime()));

                            int date1 = Integer.parseInt(yearformat.format(myCalendar.getTime()));
                            txtstart.setText(""+date1);

                        }

                    };
                    final DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {

                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear,
                                              int dayOfMonth) {
                            // TODO Auto-generated method stub
                            myCalendar.set(Calendar.YEAR, year);
                            myCalendar.set(Calendar.MONTH, monthOfYear);
                            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            String myFormat = "MMM/dd/yyyy"; //In which you need put here
                            String year1 = "yyyy"; //In which you need put here
                            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                            SimpleDateFormat yearformat = new SimpleDateFormat(year1, Locale.US);
                            dateended.setText(sdf.format(myCalendar.getTime()));

                            int date1 = Integer.parseInt(yearformat.format(myCalendar.getTime()));
                            txtend.setText(""+date1);
                        /*String textS = txtstart.getText().toString();
                        String textE = txtend.getText().toString();
                        if (textS!=null && textE != null) {
                            int textStart = Integer.parseInt(textS);
                            int textEnd = Integer.parseInt(textE);
                            txtResult.setText(textEnd - textStart);
                        }*/


                        }

                    };

                    btnSelectstart.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            new DatePickerDialog(PWD_RegisterActivity2.this, date, myCalendar
                                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();



                        }
                    });

                    btnSelectend.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            new DatePickerDialog(PWD_RegisterActivity2.this, date2, myCalendar
                                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                        }
                    });





                }

                alertWork.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        String job = jobposition.getText().toString();
                        String company = companyname.getText().toString();
                        String started = datestarted.getText().toString();
                        String ended = dateended.getText().toString();
                        String skill = spinnercategory.getSelectedItem().toString();

                        PWD_AddWorkInformation workInfo = new PWD_AddWorkInformation(job, company, skill, started, ended);
                        wCount++;
                        final String e = "w";
                        final String w = e + wCount;
                        mDatabase.child(userz).child("listOfWorks").child(w).setValue(workInfo);

                        DatabaseReference noice = FirebaseDatabase.getInstance().getReference().child("PWD").child(userz).child("listOfWorks");
                        Toast.makeText(PWD_RegisterActivity2.this, "List of Work Added", Toast.LENGTH_LONG).show();

                        noice.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                work_list = new ArrayList<>();
                                work_list.clear();
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    PWD_AddWorkInformation p = dataSnapshot1.getValue(PWD_AddWorkInformation.class);
                                    work_list.add(p);
                                }
                                Collections.reverse(work_list);
                                work_adapter = new PWD_WorkExperienceAdapter(PWD_RegisterActivity2.this, work_list);
                                work_recyclerView.setAdapter(work_adapter);
                                work_adapter.notifyDataSetChanged();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(PWD_RegisterActivity2.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });


                    }
                });
                alertWork.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertWork.setCancelable(false);

                AlertDialog alert = alertWork.create();
                alert.setTitle("Add Work");
                alert.show();

            }
        });

    }





    private void login() {
        if (checkOrtho.isChecked()) {
            check_Ortho = checkOrtho.getText().toString();
        }
        else{
            check_Ortho = "";
        }

        if (checkVis.isChecked() ) {
            check_Vis = checkVis.getText().toString();
        }
        else{
            check_Vis = "";
        }

        if (checkHear.isChecked() ) {
            check_Hear = checkHear.getText().toString();
        }
        else{
            check_Hear = "";
        }

        if (checkMore.isChecked() ) {
            check_More = checkMore.getText().toString();
        }
        else{
            check_More = "";
        }



        if (job1.isChecked()) {
            job_1 = job1.getText().toString();
        } else {
            job_1 = "";
        }
        if (job2.isChecked()) {
            job_2 = job2.getText().toString();
        } else {
            job_2 = "";
        }
        if (job3.isChecked()) {
            job_3 = job3.getText().toString();
        } else {
            job_3 = "";
        }
        if (job4.isChecked()) {
            job_4 = job4.getText().toString();
        } else {
            job_4 = "";
        }
        if (job5.isChecked()) {
            job_5 = job5.getText().toString();
        } else {
            job_5 = "";
        }
        if (job6.isChecked()) {
            job_6 = job6.getText().toString();
        } else {
            job_6 = "";
        }
        if (job7.isChecked()) {
            job_7 = job7.getText().toString();
        } else {
            job_7 = "";
        }
        if (job8.isChecked()) {
            job_8 = job8.getText().toString();
        } else {
            job_8 = "";
        }
        if (job9.isChecked()) {
            job_9 = job9.getText().toString();
        } else {
            job_9 = "";
        }
        if (job10.isChecked()) {
            job_10 = job10.getText().toString();
        } else {
            job_10 = "";
        }
    }

    //deleted callDialog function --------------------------------------------------------------


    private void uploadImage() {
        final Intent intent = new Intent(this, PWD_RegisterActivity3.class);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.show();
        final String job1 = job_1;
        final String job2 = job_2;
        final String job3 = job_3;
        final String job4 = job_4;
        final String job5 = job_5;
        final String job6 = job_6;
        final String job7 = job_7;
        final String job8 = job_8;
        final String job9 = job_9;
        final String job10 = job_10;

        final String categorySkill = primary_skillsCategory.getSelectedItem().toString();
        final String checkOrtho = check_Ortho;
        final String checkVis = check_Vis;
        final String checkHear = check_Hear;
        final String checkMore = check_More;

        progressDialog.dismiss();

        rgEducAttainment = findViewById(R.id.rg_educ);
        final int selectedId = rgEducAttainment.getCheckedRadioButtonId();
        rbEduc = findViewById(selectedId);
        educAttainment = rbEduc.getText().toString();

        /*for(int i=0;i<pwdPrimarySkills.length;i++){
            if(!(pwdPrimarySkills[i]==null)){
                countw++;
            }
        }*/
        //numberOfPrimarySkills=countw;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();

        CheckBox ortho = findViewById(R.id.checkOrtho);
        CheckBox visual = findViewById(R.id.checkVis);
        CheckBox hear = findViewById(R.id.checkHear);
        CheckBox more = findViewById(R.id.checkMore);

        primary_skillsCategory = findViewById(R.id.skillSpinner);

        TextView textview8 = findViewById(R.id.textView8);
        TextView textview7 = findViewById(R.id.textView7);

        if(!ortho.isChecked() && !visual.isChecked() && !hear.isChecked() && !more.isChecked()){
            textview7.setError("Please check your disability");
            textview7.requestFocus();
            Toast.makeText(PWD_RegisterActivity2.this, "Please check your disability", Toast.LENGTH_SHORT).show();
            return;
        }else{
            if(categorySkill.equals("Click to select value")){
                textview8.setError("Please select your skill category");
                textview8.requestFocus();
                Toast.makeText(PWD_RegisterActivity2.this, "Choose your skill category", Toast.LENGTH_SHORT).show();
                return;
            }else{
                mDatabase = FirebaseDatabase.getInstance().getReference().child("PWD").child(userId);
                mDatabase.child("educationalAttainment").setValue(educAttainment);
                mDatabase.child("skill").setValue(primary_skillsCategory.getSelectedItem().toString()); //working
                mDatabase.child("workExperience").setValue(workExperience);


                ArrayList jobskills = new ArrayList();
                jobskills.add(job1);
                jobskills.add(job2);
                jobskills.add(job3);
                jobskills.add(job4);
                jobskills.add(job5);
                jobskills.add(job6);
                jobskills.add(job7);
                jobskills.add(job8);
                jobskills.add(job9);
                jobskills.add(job10);

                ArrayList disabilities = new ArrayList();
                disabilities.add(checkOrtho);
                disabilities.add(checkVis);
                disabilities.add(checkHear);
                disabilities.add(checkMore);


                for(int i = 0; i < jobskills.size(); i++){
                    if((jobskills.get(i) != "")){
                        mDatabase.child("jobSkills" + i).setValue(jobskills.get(i));
                    }
                }
                for(int i = 0; i < disabilities.size(); i++){
                    if((disabilities.get(i) != "")){
                        mDatabase.child("typeOfDisability" + i).setValue(disabilities.get(i));
                    }
                }
                if(workExperience.equals("")){
                    mDatabase.child("workExperience").setValue("Without Experience");
                }else{
                    mDatabase.child("workExperience").setValue(workExperience);
                }

            }
        }


        startActivity(intent);

        }



    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            final Intent intent = new Intent(this, PWD_RegisterActivity.class);
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            androidx.appcompat.app.AlertDialog.Builder alert =  new androidx.appcompat.app.AlertDialog.Builder(PWD_RegisterActivity2.this);
            alert.setMessage("By clicking Yes, you will return to the first step deleting your account.").setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        /*    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("PWD");
                            dbRef.child(String.valueOf(user)).removeValue();*/
                            user.delete();
                            startActivity(intent);
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            androidx.appcompat.app.AlertDialog alertDialog = alert.create();
            alertDialog.setTitle("Return to first step?");
            alertDialog.show();
        }
        return super.onKeyDown(keyCode, event);
    }
}