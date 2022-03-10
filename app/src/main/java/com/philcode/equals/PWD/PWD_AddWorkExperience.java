package com.philcode.equals.PWD;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.philcode.equals.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class PWD_AddWorkExperience extends AppCompatActivity {
    final Calendar myCalendar = Calendar.getInstance();
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pwd_addworkexperience);
        View view = LayoutInflater.from(PWD_AddWorkExperience.this).inflate(R.layout.working_experience, null);
        //Layout
        final EditText jobposition = view.findViewById(R.id.work_jobposition);
        final EditText companyname = view.findViewById(R.id.work_company);
        final EditText datestarted = view.findViewById(R.id.date_started);
        final EditText dateended = view.findViewById(R.id.date_ended);

        final TextView txtstart = view.findViewById(R.id.txtstart);
        final TextView txtend = view.findViewById(R.id.txtend);

        final Button btnSelectstart = view.findViewById(R.id.btnSelectstart);
        final Button btnSelectend = view.findViewById(R.id.btnSelectend);
        final Spinner spinnercategory = view.findViewById(R.id.spinnerCategory);

        final String uid = getIntent().getStringExtra("uID");

        //setting up spinner
        final DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference().child("Category/");
        categoryRef.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {

                final List<String> category = new ArrayList<String>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Map<String, Object> data = (Map<String, Object>) snapshot.getValue();
                    category.add(data.get("skill").toString());
                }
                ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(PWD_AddWorkExperience.this, android.R.layout.simple_spinner_item, category) {
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

        AlertDialog.Builder alertWork = new AlertDialog.Builder(PWD_AddWorkExperience.this);
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
                    txtstart.setText("" + date1);

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
                    txtend.setText("" + date1);


                }

            };

            btnSelectstart.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    new DatePickerDialog(PWD_AddWorkExperience.this, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();


                }
            });

            btnSelectend.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    new DatePickerDialog(PWD_AddWorkExperience.this, date2, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });

        }

        alertWork.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Database References
                mDatabase = FirebaseDatabase.getInstance().getReference().child("PWD");
                String skill = spinnercategory.getSelectedItem().toString();
                if (skill.equals("Click to select value")) {
                    Toast.makeText(PWD_AddWorkExperience.this, "Please select a skill category.", Toast.LENGTH_SHORT).show();
                }else if(jobposition.getText().toString().isEmpty() || companyname.getText().toString().isEmpty() || datestarted.getText().toString().isEmpty()
                        || dateended.getText().toString().isEmpty()){
                    Toast.makeText(PWD_AddWorkExperience.this, "Please fill out the form completely.", Toast.LENGTH_SHORT).show();
                }else{
                    //startActivity(new Intent(PWD_AddWorkExperience.this, PWD_EditProfile_ViewActivity.class));
                    String job = jobposition.getText().toString();
                    String company = companyname.getText().toString();
                    String started = datestarted.getText().toString();
                    String ended = dateended.getText().toString();

                    final String w = UUID.randomUUID().toString();
                    PWD_AddWorkInformation workInfo = new PWD_AddWorkInformation(job, company, skill, started, ended, w);
                    mDatabase.child(uid).child("listOfWorks").child(w).setValue(workInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            startActivity(new Intent(PWD_AddWorkExperience.this, PWD_EditProfile_ViewActivity.class));
                        }
                    });
                    Toast.makeText(PWD_AddWorkExperience.this, "Work Experience Added.", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        alertWork.setCancelable(false);
        AlertDialog alert = alertWork.create();
        alert.setTitle("Add Work");
        alert.show();


        }
}