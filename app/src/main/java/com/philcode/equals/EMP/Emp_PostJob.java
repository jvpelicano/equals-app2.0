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
    private DatabaseReference job_offers_root, categories_root;
    private StorageReference job_offers_storage_root;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    //layout
        //adapters
        private ArrayAdapter<String> spinner_skillCategory_adapter;
        //spinners
        private TextInputLayout textInputLayout_typeOfDisability, textInputLayout_skillCategory, textInputLayout_jobRole;
        //autocomplete
        private AutoCompleteTextView autoComplete_typeOfDisability, autoComplete_skillCategory, autoComplete_jobRole;
        //text views
        private TextView txt_jobRole;
    //arrays
        //spinner arrays
        private ArrayList <String> arrayList_skillCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emp_postjob);
        //initialize firebase connection
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
            //references
            categories_root = firebaseDatabase.getReference("Categories");
            job_offers_root = firebaseDatabase.getReference("Job_Offers");
            job_offers_storage_root = firebaseStorage.getReference("Posts/");
        //arrays
            //spinner arrays
            arrayList_skillCategory = new ArrayList<>();
        //layout
            //adapters
            spinner_skillCategory_adapter =  new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arrayList_skillCategory);
            //spinners
            setSpinnerSkillCategory();
            //autocomplete text view
                //skill category
                autoComplete_skillCategory = findViewById(R.id.autoComplete_skillCategory);
                autoComplete_skillCategory.setAdapter(spinner_skillCategory_adapter);
                autoComplete_skillCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(Emp_PostJob.this, autoComplete_skillCategory.getText().toString(), Toast.LENGTH_LONG).show();
                    }
                });


    }
    private void setSpinnerSkillCategory(){
        categories_root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    arrayList_skillCategory.add(snapshot1.child("skill").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}