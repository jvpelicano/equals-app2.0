package com.philcode.equals.EMP;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.philcode.equals.R;

import java.util.ArrayList;

public class Emp_PostJob extends AppCompatActivity {
    //firebase connection
    private DatabaseReference job_offers_root, categories_root, typeOfDisabilities_root;
    private StorageReference job_offers_storage_root;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    //layout
        //adapters
        private ArrayAdapter<String> spinner_skillCategory_adapter;
        private ArrayAdapter<String> spinner_typeOfDisability_adapter;
        private ArrayAdapter<String> spinner_jobtitles_adapter;
        //spinners
        private TextInputLayout textInputLayout_typeOfDisability, textInputLayout_skillCategory, textInputLayout_jobTitle;
        //autocomplete
        private AutoCompleteTextView autoComplete_typeOfDisability, autoComplete_skillCategory, autoComplete_jobTitle;
        //text views
        private TextView txt_jobTitle;
    //arrays
        //spinner arrays
        private ArrayList <String> arrayList_skillCategory;
        private ArrayList <String> arrayList_typeOfDisability;
        private ArrayList <String> arrayList_spinner_jobtitles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emp_postjob);
        //initialize firebase connection
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
            //references
            typeOfDisabilities_root = firebaseDatabase.getReference("Disabilities");//reference for spinners
            categories_root = firebaseDatabase.getReference("Category"); //reference for spinners
            job_offers_root = firebaseDatabase.getReference("Job_Offers"); //where data will be stored
            job_offers_storage_root = firebaseStorage.getReference("Posts/"); //where image will be stored
        //arrays
            //spinner arrays
            arrayList_skillCategory = new ArrayList<>();
            arrayList_typeOfDisability = new ArrayList<>();
            arrayList_spinner_jobtitles = new ArrayList<>();
        //layout
            //text input layouts
            textInputLayout_jobTitle = findViewById(R.id.textInputLayout_jobTitle); //used
            textInputLayout_typeOfDisability = findViewById(R.id.textInputLayout_typeOfDisability);
            textInputLayout_skillCategory = findViewById(R.id.textInputLayout_skillCategory);
            //text views
            txt_jobTitle = findViewById(R.id.txt_jobTitle);
            //adapters
            spinner_skillCategory_adapter =  new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arrayList_skillCategory);
            spinner_typeOfDisability_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arrayList_typeOfDisability);
            spinner_jobtitles_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arrayList_spinner_jobtitles);
            //set spinners
            setSpinnerSkillCategory();
            setSpinnerTypeOfDisability();

            //autocomplete text view
                //skill category
                autoComplete_skillCategory = findViewById(R.id.autoComplete_skillCategory);
                autoComplete_skillCategory.setAdapter(spinner_skillCategory_adapter);
                //type of disability
                autoComplete_typeOfDisability = findViewById(R.id.autoComplete_typeOfDisability);
                autoComplete_typeOfDisability.setAdapter(spinner_typeOfDisability_adapter);
                autoComplete_typeOfDisability.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //Toast.makeText(Emp_PostJob.this, autoComplete_skillCategory.getText().toString(), Toast.LENGTH_LONG).show();
                    }
                });
                //job titles
                autoComplete_jobTitle = findViewById(R.id.autoComplete_jobTitle);
                autoComplete_jobTitle.setAdapter(spinner_jobtitles_adapter);
                    autoComplete_skillCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    txt_jobTitle.setVisibility(View.VISIBLE);
                    textInputLayout_jobTitle.setVisibility(View.VISIBLE);
                    arrayList_spinner_jobtitles.clear();
                    setSpinnerJobTitle();
                    autoComplete_jobTitle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //Toast.makeText(Emp_PostJob.this, autoComplete_jobTitle.getText().toString(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
        });



    }
    private void setSpinnerSkillCategory(){
        categories_root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap_skillCategory : snapshot.getChildren()){
                    arrayList_skillCategory.add(snap_skillCategory.child("skill").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void setSpinnerJobTitle(){
        String chosenSkillCategory = autoComplete_skillCategory.getText().toString();
        //Toast.makeText(Emp_PostJob.this, chosenSkillCategory, Toast.LENGTH_LONG).show();
        categories_root.orderByChild("skill").equalTo(chosenSkillCategory).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap_category_key : snapshot.getChildren()){
                    String parent = snap_category_key.getKey();

                    categories_root.child(parent).child("jobtitles").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot snap_jobTitles : snapshot.getChildren()){
                                arrayList_spinner_jobtitles.add(snap_jobTitles.getValue().toString());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void setSpinnerTypeOfDisability(){
        typeOfDisabilities_root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap_disabilities : snapshot.getChildren()){
                    arrayList_typeOfDisability.add(snap_disabilities.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}