package com.philcode.equals.EMP;

import android.app.Notification;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.philcode.equals.R;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EMP_PotentialApplicant_BasicInfo_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EMP_PotentialApplicant_BasicInfo_Fragment extends Fragment {

    private TextView m_fullName, m_email, m_address, m_educationalAttainment, m_contact, m_skill, m_displayJobSkillList, m_displayTypeOfDisability,
            m_displayTypeOfEmployment, m_displayWorkSetUp, m_displayJobTitle;
    private TextView m_degree;
    private FirebaseDatabase fdb;
    private DatabaseReference pwd_reference;
    private CardView m_degree_card;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public EMP_PotentialApplicant_BasicInfo_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EMP_PotentialApplicant_BasicInfo_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EMP_PotentialApplicant_BasicInfo_Fragment newInstance(String param1, String param2) {
        EMP_PotentialApplicant_BasicInfo_Fragment fragment = new EMP_PotentialApplicant_BasicInfo_Fragment();
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
        return inflater.inflate(R.layout.fragment_emp_pwdbasicinfo, container, false);
    }

   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
       super.onViewCreated(view, savedInstanceState);
       m_fullName = view.findViewById(R.id.displayName);
       m_email = view.findViewById(R.id.displayEmail);
       m_address = view.findViewById(R.id.displayAddress);
       m_educationalAttainment = view.findViewById(R.id.displayEducationalAttainment);
       m_contact = view.findViewById(R.id.displayContact);
       m_displayJobSkillList = view.findViewById(R.id.displaySkill1);
       m_displayTypeOfDisability = view.findViewById(R.id.displayTypeOfDisability1);

       m_degree = view.findViewById(R.id.txtLbl2);
       m_skill = view.findViewById(R.id.displayCategorySkill);
       m_degree_card = view.findViewById(R.id.degree_card);

       //new data
       m_displayJobTitle = view.findViewById(R.id.displayJobTitle);
       m_displayTypeOfEmployment = view.findViewById(R.id.displayTypeOfEmployment);
       m_displayWorkSetUp = view.findViewById(R.id.displayWorkSetUp);

       getApplicantInfo();


    }
    public void getApplicantInfo(){
        pwd_reference = FirebaseDatabase.getInstance().getReference().child("PWD");
        String pwd_AuthID = getActivity().getIntent().getStringExtra("PWD_ID");
        pwd_reference.child(pwd_AuthID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String firstName = snapshot.child("firstName").getValue().toString();
                String lastName = snapshot.child("lastName").getValue().toString();
                String email = snapshot.child("email").getValue().toString();
                String address = snapshot.child("address").getValue().toString();
                String educationalAttainment = snapshot.child("educationalAttainment").getValue().toString();
                String workExperience = snapshot.child("workExperience").getValue().toString();
                String contact = snapshot.child("contact").getValue().toString();
                String skill = snapshot.child("skill").getValue().toString();
                String city = snapshot.child("city").getValue().toString();

                //new data
                String jobTitle = snapshot.child("jobTitle").getValue().toString();
                String typeOfEmployment = snapshot.child("typeOfEmployment").getValue().toString();
                String workSetUp = snapshot.child("workSetUp").getValue().toString();

                ArrayList<String> jobSkillList = new ArrayList<>();
                ArrayList<String> typeOfDisabilityList = new ArrayList<>();
                for(int counter = 0; counter <= 10; counter++){
                    if(snapshot.hasChild("jobSkills" + counter) && !snapshot.child("jobSkills" + counter).getValue().toString().equals("")){
                        jobSkillList.add(snapshot.child("jobSkills" + counter).getValue(String.class));
                    }
                }
                for(int counter_a = 0; counter_a <= 3; counter_a++){
                    if(snapshot.hasChild("typeOfDisability" + counter_a) && !snapshot.child("typeOfDisability" + counter_a).getValue().toString().equals("")){
                        typeOfDisabilityList.add(snapshot.child("typeOfDisability" + counter_a).getValue(String.class));
                    }

                }

                setApplicantInfo(jobSkillList, typeOfDisabilityList, firstName, lastName, email, address, educationalAttainment, workExperience, contact, skill,
                        jobTitle, typeOfEmployment, workSetUp, city);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void setApplicantInfo(ArrayList<String> jobSkillList, ArrayList<String> typeOfDisabilityList, String firstName, String lastName,  String email, String address, String educationalAttainment,
                                 String workExperience, String contact, String skill, String jobTitle, String typeOfEmployment, String workSetUp, String city){

        m_fullName.setText(firstName.concat(" ").concat(lastName));
        m_email.setText(email);
        m_address.setText(address + ", " + city + " City");
        m_educationalAttainment.setText(educationalAttainment);
        m_contact.setText(contact);
        m_skill.setText(skill);
        StringBuilder jobSkillList_builder = new StringBuilder();
        for(String jobSkillList1 : jobSkillList){
            jobSkillList_builder.append(jobSkillList1 + "\n");
        }
        m_displayJobSkillList.setText(jobSkillList_builder.toString());

        StringBuilder typeOfDisability_builder = new StringBuilder();
        for(String typeOfDisabilityList1 : typeOfDisabilityList) {
            typeOfDisability_builder.append(typeOfDisabilityList1 + "\n");
        }
        m_displayTypeOfDisability.setText(typeOfDisability_builder.toString());

        //add new data
        if(educationalAttainment.equals("Bachelor Level") || educationalAttainment.equals("Master's Level") || educationalAttainment.equals("Doctorate Level")){
            m_degree.setVisibility(View.VISIBLE);
            m_skill.setVisibility(View.VISIBLE);
            m_degree_card.setVisibility(View.VISIBLE);
            m_skill.setText(skill);
        }else if(educationalAttainment.equals("Elementary Level") || educationalAttainment.equals("High School Level") || educationalAttainment.equals("Associate Level")){
            m_degree.setVisibility(View.GONE);
            m_degree_card.setVisibility(View.GONE);
            m_skill.setVisibility(View.GONE);
        }else{
            m_degree.setVisibility(View.GONE);
            m_degree_card.setVisibility(View.GONE);
            m_skill.setVisibility(View.GONE);
        }

        m_displayJobTitle.setText(jobTitle);
        m_displayTypeOfEmployment.setText(typeOfEmployment);
        m_displayWorkSetUp.setText(workSetUp);


    }
}