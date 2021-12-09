package com.philcode.equals.PWD;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.philcode.equals.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PWD_BasicInfo_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PWD_BasicInfo_Fragment extends Fragment {
    DatabaseReference rootRef;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView displayName,displayTypeOfDisability1, displayTypeOfDisability2,displayTypeOfDisability3,displayTypeOfDisabilityMore
            ,displaySkill1,displaySkill2,displaySkill3,displaySkill4,displaySkill5,displaySkill6,displaySkill7,displaySkill8,displaySkill9,displaySkill10
            ,displayEmail,displayAddress,displayContact,displayEducationalAttainment,displayCategorySkill;

    public PWD_BasicInfo_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PWD_BasicInfo_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PWD_BasicInfo_Fragment newInstance(String param1, String param2) {
        PWD_BasicInfo_Fragment fragment = new PWD_BasicInfo_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pwd_basicinfo, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        displayTypeOfDisability1 = view.findViewById(R.id.displayTypeOfDisability1);
        displayTypeOfDisability2 = view.findViewById(R.id.displayTypeOfDisability2);
        displayTypeOfDisability3 = view.findViewById(R.id.displayTypeOfDisability3);
        displayTypeOfDisabilityMore = view.findViewById(R.id.displayTypeOfDisabilityMore);

        displaySkill1 = view.findViewById(R.id.displaySkill1);
        displaySkill2 = view.findViewById(R.id.displaySkill2);
        displaySkill3 = view.findViewById(R.id.displaySkill3);
        displaySkill4 = view.findViewById(R.id.displaySkill4);
        displaySkill5 = view.findViewById(R.id.displaySkill5);
        displaySkill6 = view.findViewById(R.id.displaySkill6);
        displaySkill7 = view.findViewById(R.id.displaySkill7);
        displaySkill8 = view.findViewById(R.id.displaySkill8);
        displaySkill9 = view.findViewById(R.id.displaySkill9);
        displaySkill10 = view.findViewById(R.id.displaySkill10);

        displayName = view.findViewById(R.id.displayName);
        displayEmail = view.findViewById(R.id.displayEmail);
        displayAddress = view.findViewById(R.id.displayAddress);
        displayContact = view.findViewById(R.id.displayContact);


        displayEducationalAttainment = view.findViewById(R.id.displayEducationalAttainment);
        displayCategorySkill = view.findViewById(R.id.displayCategorySkill);

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = currentFirebaseUser.getUid();
        rootRef = FirebaseDatabase.getInstance().getReference().child("PWD").child(uid);
        getUserInfo();
    }
    public void getUserInfo(){
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String firstname = dataSnapshot.child("firstName").getValue().toString(); // getting data correctly
                String lastname = dataSnapshot.child("lastName").getValue().toString();
                String email = dataSnapshot.child("email").getValue().toString();
                String address = dataSnapshot.child("address").getValue().toString();
                String city = dataSnapshot.child("city").getValue().toString();
                String skill = dataSnapshot.child("skill").getValue().toString();
                
                displayName.setText(firstname.concat(" ").concat(lastname));
                displayEmail.setText(email);
                displayAddress.setText(address+" "+city);
                displayCategorySkill.setText(skill);

                if(dataSnapshot.child("typeOfDisability0").exists()) {
                    String typeOfDisability1 = dataSnapshot.child("typeOfDisability0").getValue().toString();
                    displayTypeOfDisability1.setText(typeOfDisability1);
                }else{
                    displayTypeOfDisability1.setVisibility(View.GONE);
                }
                if(dataSnapshot.child("typeOfDisability1").exists()) {
                    String typeOfDisability2 = dataSnapshot.child("typeOfDisability1").getValue().toString();
                    displayTypeOfDisability2.setText(typeOfDisability2);
                }else{
                    displayTypeOfDisability2.setVisibility(View.GONE);
                }
                if(dataSnapshot.child("typeOfDisability2").exists()) {
                    String typeOfDisability3 = dataSnapshot.child("typeOfDisability2").getValue().toString();
                    displayTypeOfDisability3.setText(typeOfDisability3);
                }else{
                    displayTypeOfDisability3.setVisibility(View.GONE);
                }
                if(dataSnapshot.child("typeOfDisability3").exists()) {
                    String typeOfDisabilityMore = dataSnapshot.child("typeOfDisability3").getValue().toString();
                    displayTypeOfDisabilityMore.setText(typeOfDisabilityMore);
                }else{
                    displayTypeOfDisabilityMore.setVisibility(View.GONE);
                }

                /////////////////////////////////// Secondary skills
                if(dataSnapshot.child("jobSkills0").exists()){
                    displaySkill1.setText(dataSnapshot.child("jobSkills0").getValue().toString());
                }else{
                    displaySkill1.setVisibility(View.GONE);
                }

                if(dataSnapshot.child("jobSkills1").exists()){
                    displaySkill2.setText(dataSnapshot.child("jobSkills1").getValue().toString());
                }else{
                    displaySkill2.setVisibility(View.GONE);

                }
                if(dataSnapshot.child("jobSkills2").exists()){
                    displaySkill3.setText(dataSnapshot.child("jobSkills2").getValue().toString());
                }else{
                    displaySkill3.setVisibility(View.GONE);
                }

                if(dataSnapshot.child("jobSkills3").exists()){
                    displaySkill4.setText(dataSnapshot.child("jobSkills3").getValue().toString());
                }else{
                    displaySkill4.setVisibility(View.GONE);

                }

                if(dataSnapshot.child("jobSkills4").exists()){
                    displaySkill5.setText(dataSnapshot.child("jobSkills4").getValue().toString());
                }else{
                    displaySkill5.setVisibility(View.GONE);

                }

                if(dataSnapshot.child("jobSkills5").exists()){
                    displaySkill6.setText(dataSnapshot.child("jobSkills5").getValue().toString());
                }else{
                    displaySkill6.setVisibility(View.GONE);

                }

                if(dataSnapshot.child("jobSkills6").exists()){
                    displaySkill7.setText(dataSnapshot.child("jobSkills6").getValue().toString());
                }else{
                    displaySkill7.setVisibility(View.GONE);

                }

                if(dataSnapshot.child("jobSkills7").exists()){
                    displaySkill8.setText(dataSnapshot.child("jobSkills7").getValue().toString());
                }else{
                    displaySkill8.setVisibility(View.GONE);
                }

                if(dataSnapshot.child("jobSkills8").exists()){
                    displaySkill9.setText(dataSnapshot.child("jobSkills8").getValue().toString());
                }else{
                    displaySkill9.setVisibility(View.GONE);
                }
                if(dataSnapshot.child("jobSkills9").exists()){
                    displaySkill10.setText(dataSnapshot.child("jobSkills9").getValue().toString());
                }else{
                    displaySkill10.setVisibility(View.GONE);

                }
                /////////////////////////////////// Secondary skills


                String educationalAttainment = dataSnapshot.child("educationalAttainment").getValue().toString();
                String workExperience = dataSnapshot.child("workExperience").getValue().toString();
                String contact = dataSnapshot.child("contact").getValue().toString();


                //PWD Work Experience Fragment. Include Database Reference in the new Fragment to make this work.
                displayEducationalAttainment.setText(educationalAttainment);

                displayContact.setText(contact);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}