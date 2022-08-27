package com.philcode.equals.PWD;

import static androidx.constraintlayout.widget.StateSet.TAG;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.philcode.equals.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PWD_AvailableJobOffers_1_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PWD_AvailableJobOffers_1_Fragment extends Fragment {
    private View view;
    private DatabaseReference pwd_root, job_root, pwdInfo;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    //recycler
    private List<PWD_AvailableJobOffers_1_Model> jobs_list;
    private PWD_AvailableJobOffers_1_RVAdapter jobs1_adapter;
    private RecyclerView jobs1_recycler;

    //secondary skills
    private ArrayList<String> pwd_secondary_skills;
    private ArrayList<String> job_secondary_skills;

    private ArrayList<String> pwd_disability;
    private ArrayList<String> job_disability;

    private String uid;
    private int pwdRequiredPoints = 0;
    private int pwdOptionPoints = 0;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PWD_AvailableJobOffers_1_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PWD_AvailableJobOffer_1_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PWD_AvailableJobOffers_1_Fragment newInstance(String param1, String param2) {
        PWD_AvailableJobOffers_1_Fragment fragment = new PWD_AvailableJobOffers_1_Fragment();
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
        view = inflater.inflate(R.layout.fragment_pwd_availablejoboffers_1, container, false);
        jobs1_recycler = view.findViewById(R.id.recycler_joboffers_1);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        jobs1_recycler.setLayoutManager(manager);
        jobs1_recycler.setHasFixedSize(true);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        job_root = FirebaseDatabase.getInstance().getReference().child("Job_Offers");
        pwd_root = FirebaseDatabase.getInstance().getReference().child("PWD");
        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getCurrentUser().getUid();
        pwdInfo = pwd_root.child(uid);

        pwd_secondary_skills = new ArrayList<>();
        job_secondary_skills = new ArrayList<>();
        pwd_disability = new ArrayList<>();
        job_disability = new ArrayList<>();

        pwdQualification();
    }

    public void pwdQualification(){
        pwdInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final String jobTitle = snapshot.child("jobTitle").getValue().toString();
                final String pwdCategory = snapshot.child("skill").getValue().toString();
                final String edAttainment = snapshot.child("educationalAttainment").getValue().toString();
                final String pwdWorkSetUp = snapshot.child("workSetUp").getValue().toString();
                final String pwdWorkExperience = snapshot.child("workExperience").getValue().toString();
                final String pwdTypeOfEmployment = snapshot.child("typeOfEmployment").getValue().toString();
                final String pwd_location = snapshot.child("city").getValue().toString();
                final String pwdDisability = getActivity().getIntent().getStringExtra("PWD_DISABILITY");

                Log.d("TAG",pwdDisability);

                for(int countSecondarySkills = 0; countSecondarySkills < 5; countSecondarySkills++){
                    if(snapshot.hasChild("jobSkills" + countSecondarySkills)){
                        pwd_secondary_skills.add(snapshot.child("jobSkills" + countSecondarySkills).getValue().toString());
                    }
                }


                matchJobOffer(jobTitle, pwdCategory, edAttainment, pwd_secondary_skills, pwdWorkExperience, pwdWorkSetUp,
                        pwdTypeOfEmployment, pwdDisability);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    public void matchJobOffer(String pwd_jobTitle, String pwd_category, String pwd_edAttainment,ArrayList<String> m_pwd_secondary_skills,
                              String pwd_workExp, String pwd_workSetUp, String pwd_typeOfEmp, String pwdDisability){
        job_root.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                jobs_list = new ArrayList<>();
                jobs_list.clear();
                for(DataSnapshot job_snapshot : snapshot.getChildren()){

                    final String job_educationalAttainment, job_typeOfEmployment;

                    final String permission = job_snapshot.child("permission").getValue().toString();
                    final String job_expDate = job_snapshot.child("expDate").getValue().toString();
                    final String job_title = job_snapshot.child("jobTitle").getValue().toString();
                    final String job_skillCategory = job_snapshot.child("skill").getValue().toString();
                    final String job_workExp = job_snapshot.child("workExperience").getValue().toString();
                    final String job_workSetUp = job_snapshot.child("workSetUp").getValue().toString();
//                    final String jobQualification = job_snapshot.child("QualifiedCriteria").getValue().toString();


//                    System.out.print("Object" + jobQualification);


                    //Checkbox for Required Criteria
                    final String job_educationalAttainmentRequirement = job_snapshot.child("educationalAttainmentRequirement").getValue().toString();
                    final String job_workSetUpRequired = job_snapshot.child("workSetUpRequired").getValue().toString();
                    final String job_workExpRequired = job_snapshot.child("workExpRequired").getValue().toString();
                    final String job_typeOfEmploymentRequired = job_snapshot.child("typeOfEmploymentRequired").getValue().toString();

                    //Required Score and Optional Score
                    final int job_requiredScore = Integer.parseInt(job_snapshot.child("jobRequiredScore").getValue().toString());

                    //get all typeOfDisability available in the list of job offers
                    String job_typeOfDisability1 = (job_snapshot.hasChild("typeOfDisability1") ? job_snapshot.child("typeOfDisability1").getValue().toString() : "");
                    String job_typeOfDisability2 = (job_snapshot.hasChild("typeOfDisability2") ? job_snapshot.child("typeOfDisability2").getValue().toString() : "");
                    String job_typeOfDisability3 = (job_snapshot.hasChild("typeOfDisability3") ? job_snapshot.child("typeOfDisability3").getValue().toString() : "");
                    String job_typeOfDisability4 = (job_snapshot.hasChild("typeOfDisability4") ? job_snapshot.child("typeOfDisability4").getValue().toString() : "");
                    String job_typeOfDisabilityMore = (job_snapshot.hasChild("typeOfDisabilityMore") ? job_snapshot.child("typeOfDisabilityMore").getValue().toString() : "");

                    if (job_snapshot.hasChild("educationalAttainment"))
                        job_educationalAttainment = job_snapshot.child("educationalAttainment").getValue().toString();
                    else job_educationalAttainment = "";

                    if (job_snapshot.hasChild("typeOfEmployment"))
                        job_typeOfEmployment = job_snapshot.child("typeOfEmployment").getValue().toString();
                    else job_typeOfEmployment = "";


                    String jobSkill1 = (job_snapshot.hasChild("jobSkill1") ? job_snapshot.child("jobSkill1").getValue().toString() : "");
                    String jobSkill2 = (job_snapshot.hasChild("jobSkill2") ? job_snapshot.child("jobSkill2").getValue().toString() : "");
                    String jobSkill3 = (job_snapshot.hasChild("jobSkill3") ? job_snapshot.child("jobSkill3").getValue().toString() : "");
                    String jobSkill4 = (job_snapshot.hasChild("jobSkill4") ? job_snapshot.child("jobSkill4").getValue().toString() : "");
                    String jobSkill5 = (job_snapshot.hasChild("jobSkill5") ? job_snapshot.child("jobSkill5").getValue().toString() : "");

                    if(!jobSkill1.isEmpty()){
                        job_secondary_skills.add(jobSkill1);
                    }
                    if(!jobSkill2.isEmpty()){
                        job_secondary_skills.add(jobSkill2);
                    }
                    if(!jobSkill3.isEmpty()){
                        job_secondary_skills.add(jobSkill3);
                    }
                    if(!jobSkill4.isEmpty()){
                        job_secondary_skills.add(jobSkill4);
                    }
                    if(!jobSkill5.isEmpty()){
                        job_secondary_skills.add(jobSkill5);
                    }


                    ArrayList<String> matched_secondary_skills = new ArrayList<>();
                    //get matching secondary skills from both sides
                    int job_secondary_skills_length = job_secondary_skills.size();

                    if(m_pwd_secondary_skills.size() > 0){
                        if(job_secondary_skills.contains(m_pwd_secondary_skills.get(0))){
                            matched_secondary_skills.add(m_pwd_secondary_skills.get(0));
                        }
                    }
                    if(m_pwd_secondary_skills.size() > 1){
                        if(job_secondary_skills.contains(m_pwd_secondary_skills.get(1))){
                            matched_secondary_skills.add(m_pwd_secondary_skills.get(1));
                        }
                    }
                    if(m_pwd_secondary_skills.size() > 2){
                        if(job_secondary_skills.contains(m_pwd_secondary_skills.get(2))){
                            matched_secondary_skills.add(m_pwd_secondary_skills.get(2));
                        }
                    }
                    if(m_pwd_secondary_skills.size() > 3){
                        if(job_secondary_skills.contains(m_pwd_secondary_skills.get(3))){
                            matched_secondary_skills.add(m_pwd_secondary_skills.get(3));
                        }
                    }
                    if(m_pwd_secondary_skills.size() > 4){
                        if(job_secondary_skills.contains(m_pwd_secondary_skills.get(4))){
                            matched_secondary_skills.add(m_pwd_secondary_skills.get(4));
                        }
                    }
                    if(m_pwd_secondary_skills.size() > 5) {
                        if (job_secondary_skills.contains(m_pwd_secondary_skills.get(5))) {
                            matched_secondary_skills.add(m_pwd_secondary_skills.get(5));
                        }
                    }

                    ArrayList<String> jobRequired = new ArrayList<>();
                    jobRequired.add(job_educationalAttainmentRequirement);
                    jobRequired.add(job_workSetUpRequired);
                    jobRequired.add(job_workExpRequired);
                    jobRequired.add(job_typeOfEmploymentRequired);

                    ArrayList<String> jobQualification = new ArrayList<>();
                    jobQualification.add(job_educationalAttainment);
                    jobQualification.add(job_workSetUp);
                    jobQualification.add(job_workExp);
                    jobQualification.add(job_typeOfEmployment);

                    ArrayList<String> pwdQualification = new ArrayList<>();
                    pwdQualification.add(pwd_edAttainment);
                    pwdQualification.add(pwd_workSetUp);
                    pwdQualification.add(pwd_workExp);
                    pwdQualification.add(pwd_typeOfEmp);

                    Date expDate = convertDate(job_expDate);
                    Date currDate = convertDate(getCurrentDate());

                    if((currDate.before(expDate) || currDate.equals(expDate)) && permission.equals("Approved")){

                        int pwdPoint = 0;
                        int jobReq = 0;
                        int jobCri = 0;

                            while (pwdPoint != job_requiredScore){
                                if (jobRequired.get(jobReq).equalsIgnoreCase("true")){

                                    if (jobQualification.get(jobReq).equalsIgnoreCase(pwdQualification.get(jobReq))){
                                        jobCri++;
                                    }

                                    pwdPoint++;
                                }else{
                                    jobReq++;
                                }

                            }

                            if (jobCri == job_requiredScore){
                                if(job_title.equalsIgnoreCase(pwd_jobTitle)){
                                    if(job_skillCategory.equalsIgnoreCase(pwd_category)){
                                        PWD_AvailableJobOffers_1_Model model = job_snapshot.getValue(PWD_AvailableJobOffers_1_Model.class);
                                        jobs_list.add(model);
                                    }

                                }




                            }

                    }



//                    if(job_typeOfDisability1.equals(pwdDisability) || job_typeOfDisability2.equals(pwdDisability)
//                            || job_typeOfDisability3.equals(pwdDisability) || job_typeOfDisability4.equals(pwdDisability)
//                            || job_typeOfDisabilityMore.equals(pwdDisability) ){
//                        if((currDate.before(expDate) || currDate.equals(expDate)) && permission.equals("Approved")){
//                            if(job_title.equalsIgnoreCase(pwd_jobTitle)){
//                                if(job_skillCategory.equalsIgnoreCase(pwd_category)){
//                                    if(job_educationalAttainmentRequirement.equalsIgnoreCase("true")){
//                                        if(job_educationalAttainment.equalsIgnoreCase(pwd_edAttainment)){
//                                            if(job_workExpRequired.equalsIgnoreCase("true")){
//                                                if(job_workExp.equalsIgnoreCase(pwd_workExp)){
//                                                    if(job_typeOfEmploymentRequired.equalsIgnoreCase("true")){
//                                                        if(job_typeOfEmployment.equalsIgnoreCase(pwd_typeOfEmp)){
//                                                            if(job_workSetUpRequired.equalsIgnoreCase("true")){
//                                                                if(job_workSetUp.equalsIgnoreCase(pwd_workSetUp)){
//                                                                    PWD_AvailableJobOffers_1_Model model = job_snapshot.getValue(PWD_AvailableJobOffers_1_Model.class);
//                                                                    jobs_list.add(model);
//                                                                }
//                                                            }
//                                                            else{
//                                                                if(job_workSetUp.equalsIgnoreCase(pwd_workSetUp)){
//                                                                    PWD_AvailableJobOffers_1_Model model = job_snapshot.getValue(PWD_AvailableJobOffers_1_Model.class);
//                                                                    jobs_list.add(model);
//                                                                }
//                                                            }
//
//                                                        }
//                                                    }
//                                                    else{
//                                                        if(job_typeOfEmployment.equalsIgnoreCase(pwd_typeOfEmp)){
//                                                            if(job_workSetUpRequired.equalsIgnoreCase("true")){
//                                                                if(job_workSetUp.equalsIgnoreCase(pwd_workSetUp)){
//                                                                    PWD_AvailableJobOffers_1_Model model = job_snapshot.getValue(PWD_AvailableJobOffers_1_Model.class);
//                                                                    jobs_list.add(model);
//                                                                }
//                                                            }
//                                                            else{
//                                                                if(job_workSetUp.equalsIgnoreCase(pwd_workSetUp)){
//                                                                    PWD_AvailableJobOffers_1_Model model = job_snapshot.getValue(PWD_AvailableJobOffers_1_Model.class);
//                                                                    jobs_list.add(model);
//                                                                }
//                                                            }
//
//                                                        }
//
//                                                    }
//                                                }
//                                            }
//                                            if(job_workExp.equalsIgnoreCase(pwd_workExp)){
//                                                if(job_typeOfEmploymentRequired.equalsIgnoreCase("true")){
//                                                    if(job_typeOfEmployment.equalsIgnoreCase(pwd_typeOfEmp)){
//                                                        if(job_workSetUpRequired.equalsIgnoreCase("true")){
//                                                            if(job_workSetUp.equalsIgnoreCase(pwd_workSetUp)){
//                                                                PWD_AvailableJobOffers_1_Model model = job_snapshot.getValue(PWD_AvailableJobOffers_1_Model.class);
//                                                                jobs_list.add(model);
//                                                            }
//                                                        }
//                                                        else{
//                                                            if(job_workSetUp.equalsIgnoreCase(pwd_workSetUp)){
//                                                                PWD_AvailableJobOffers_1_Model model = job_snapshot.getValue(PWD_AvailableJobOffers_1_Model.class);
//                                                                jobs_list.add(model);
//                                                            }
//                                                        }
//
//                                                    }
//                                                }
//                                                else{
//                                                    if(job_typeOfEmployment.equalsIgnoreCase(pwd_typeOfEmp)){
//                                                        if(job_workSetUpRequired.equalsIgnoreCase("true")){
//                                                            if(job_workSetUp.equalsIgnoreCase(pwd_workSetUp)){
//                                                                PWD_AvailableJobOffers_1_Model model = job_snapshot.getValue(PWD_AvailableJobOffers_1_Model.class);
//                                                                jobs_list.add(model);
//                                                            }
//                                                        }
//                                                        else{
//                                                            if(job_workSetUp.equalsIgnoreCase(pwd_workSetUp)){
//                                                                PWD_AvailableJobOffers_1_Model model = job_snapshot.getValue(PWD_AvailableJobOffers_1_Model.class);
//                                                                jobs_list.add(model);
//                                                            }
//                                                        }
//
//                                                    }
//
//                                                }
//                                            }
//                                        }
//
//                                    }else{
//                                        if(job_educationalAttainment.equalsIgnoreCase(pwd_edAttainment)){
//                                            if(job_workExpRequired.equalsIgnoreCase("true")){
//                                                if(job_workExp.equalsIgnoreCase(pwd_workExp)){
//                                                    if(job_typeOfEmploymentRequired.equalsIgnoreCase("true")){
//                                                        if(job_typeOfEmployment.equalsIgnoreCase(pwd_typeOfEmp)){
//                                                            if(job_workSetUpRequired.equalsIgnoreCase("true")){
//                                                                if(job_workSetUp.equalsIgnoreCase(pwd_workSetUp)){
//                                                                    PWD_AvailableJobOffers_1_Model model = job_snapshot.getValue(PWD_AvailableJobOffers_1_Model.class);
//                                                                    jobs_list.add(model);
//                                                                }
//                                                            }
//                                                            else{
//                                                                if(job_workSetUp.equalsIgnoreCase(pwd_workSetUp)){
//                                                                    PWD_AvailableJobOffers_1_Model model = job_snapshot.getValue(PWD_AvailableJobOffers_1_Model.class);
//                                                                    jobs_list.add(model);
//                                                                }
//                                                            }
//
//                                                        }
//                                                    }
//                                                    else{
//                                                        if(job_typeOfEmployment.equalsIgnoreCase(pwd_typeOfEmp)){
//                                                            if(job_workSetUpRequired.equalsIgnoreCase("true")){
//                                                                if(job_workSetUp.equalsIgnoreCase(pwd_workSetUp)){
//                                                                    PWD_AvailableJobOffers_1_Model model = job_snapshot.getValue(PWD_AvailableJobOffers_1_Model.class);
//                                                                    jobs_list.add(model);
//                                                                }
//                                                            }
//                                                            else{
//                                                                if(job_workSetUp.equalsIgnoreCase(pwd_workSetUp)){
//                                                                    PWD_AvailableJobOffers_1_Model model = job_snapshot.getValue(PWD_AvailableJobOffers_1_Model.class);
//                                                                    jobs_list.add(model);
//                                                                }
//                                                            }
//
//                                                        }
//
//                                                    }
//                                                }
//                                            }
//                                            if(job_workExp.equalsIgnoreCase(pwd_workExp)){
//                                                if(job_typeOfEmploymentRequired.equalsIgnoreCase("true")){
//                                                    if(job_typeOfEmployment.equalsIgnoreCase(pwd_typeOfEmp)){
//                                                        if(job_workSetUpRequired.equalsIgnoreCase("true")){
//                                                            if(job_workSetUp.equalsIgnoreCase(pwd_workSetUp)){
//                                                                PWD_AvailableJobOffers_1_Model model = job_snapshot.getValue(PWD_AvailableJobOffers_1_Model.class);
//                                                                jobs_list.add(model);
//                                                            }
//                                                        }
//                                                        else{
//                                                            if(job_workSetUp.equalsIgnoreCase(pwd_workSetUp)){
//                                                                PWD_AvailableJobOffers_1_Model model = job_snapshot.getValue(PWD_AvailableJobOffers_1_Model.class);
//                                                                jobs_list.add(model);
//                                                            }
//                                                        }
//
//                                                    }
//                                                }
//                                                else{
//                                                    if(job_typeOfEmployment.equalsIgnoreCase(pwd_typeOfEmp)){
//                                                        if(job_workSetUpRequired.equalsIgnoreCase("true")){
//                                                            if(job_workSetUp.equalsIgnoreCase(pwd_workSetUp)){
//                                                                PWD_AvailableJobOffers_1_Model model = job_snapshot.getValue(PWD_AvailableJobOffers_1_Model.class);
//                                                                jobs_list.add(model);
//                                                            }
//                                                        }
//                                                        else{
//                                                            if(job_workSetUp.equalsIgnoreCase(pwd_workSetUp)){
//                                                                PWD_AvailableJobOffers_1_Model model = job_snapshot.getValue(PWD_AvailableJobOffers_1_Model.class);
//                                                                jobs_list.add(model);
//                                                            }
//                                                        }
//
//                                                    }
//
//                                                }
//                                            }
//                                        }
//                                    }
//
//                                }
//                            }
//                        }
//
//                    }




//                    see if the passed pwd_typeOfdisability matched any kind of the job_typeOfDisability
//                    if(job_typeOfDisability1.equals(pwdDisability) || job_typeOfDisability2.equals(pwdDisability)
//                            || job_typeOfDisability3.equals(pwdDisability) || job_typeOfDisability4.equals(pwdDisability)
//                            || job_typeOfDisabilityMore.equals(pwdDisability) ){
//                        //for Not Expired Job Post and Approved Job Post
//                        if((currDate.before(expDate) || currDate.equals(expDate)) && permission.equals("Approved")){
//                            //for matched educ attainment requirement
//                            if(job_educationalAttainmentRequirement.equalsIgnoreCase("true")){
//                                //for matched educ attainment
//                                if(job_educationalAttainment.equalsIgnoreCase(pwd_edAttainment)){
//                                    pwdRequiredPoints++;
//                                    //for matched educ attainment
//                                    if(job_title.equalsIgnoreCase(pwd_jobTitle)){
//                                        pwdRequiredPoints++;
//                                        //for matched educ attainment
//                                        if(job_skillCategory.equalsIgnoreCase(pwd_category)){
//                                            pwdRequiredPoints++;
//                                            //for matched secondary skills
//                                            if(matched_secondary_skills.size() == job_secondary_skills.size()){
//                                                //for matched without work exp
//                                                if(job_workExp.equals("Without Experience")){
//                                                    //for matched type of employment requirement
//                                                    if(job_typeOfEmploymentRequired.equalsIgnoreCase("true")){
//                                                        //for matched type of employment
//                                                        if(job_typeOfEmployment.equalsIgnoreCase(pwd_typeOfEmp)){
//                                                            //for matched work setup req
//                                                            if(job_workSetUpRequired.equalsIgnoreCase("true")){
//                                                                if(job_workSetUp.equalsIgnoreCase(pwd_workSetUp)){
//                                                                    PWD_AvailableJobOffers_1_Model model = job_snapshot.getValue(PWD_AvailableJobOffers_1_Model.class);
//                                                                    jobs_list.add(model);
//                                                                }
//                                                            }else{
//                                                                //display here
//                                                                PWD_AvailableJobOffers_1_Model model = job_snapshot.getValue(PWD_AvailableJobOffers_1_Model.class);
//                                                                jobs_list.add(model);
//                                                            }//work set up
//                                                        }//type of emp
//                                                    }else{
//                                                        //for matched work setup req
//                                                        if(job_workSetUpRequired.equalsIgnoreCase("true")){
//                                                            if(job_workSetUp.equalsIgnoreCase(pwd_workSetUp)){
//                                                                PWD_AvailableJobOffers_1_Model model = job_snapshot.getValue(PWD_AvailableJobOffers_1_Model.class);
//                                                                jobs_list.add(model);
//                                                            }
//                                                        }else{
//                                                            //display here
//                                                            PWD_AvailableJobOffers_1_Model model = job_snapshot.getValue(PWD_AvailableJobOffers_1_Model.class);
//                                                            jobs_list.add(model);
//                                                        }//work set up
//                                                    }//type of employment req
//                                                }else{
//                                                    //for matched with work exp
//                                                    if(job_workExp.equals("With Experience")){
//                                                        //for matched with work exp pwd
//                                                        if(pwd_workExp.equalsIgnoreCase("With Experience")){
//                                                            //for matched with work exp pwd
//                                                            if(job_typeOfEmploymentRequired.equalsIgnoreCase("true")){
//                                                                //for matched type of employment
//                                                                if(job_typeOfEmployment.equalsIgnoreCase(pwd_typeOfEmp)){
//                                                                    //for matched work setup req
//                                                                    if(job_workSetUpRequired.equalsIgnoreCase("true")){
//                                                                        if(job_workSetUp.equalsIgnoreCase(pwd_workSetUp)){
//                                                                            PWD_AvailableJobOffers_1_Model model = job_snapshot.getValue(PWD_AvailableJobOffers_1_Model.class);
//                                                                            jobs_list.add(model);
//                                                                        }
//                                                                    }else{
//                                                                        //display here
//                                                                        PWD_AvailableJobOffers_1_Model model = job_snapshot.getValue(PWD_AvailableJobOffers_1_Model.class);
//                                                                        jobs_list.add(model);
//                                                                    }//work set up
//                                                                }//type of emp
//                                                            }else{
//                                                                //for matched work setup req
//                                                                if(job_workSetUpRequired.equalsIgnoreCase("true")){
//                                                                    if(job_workSetUp.equalsIgnoreCase(pwd_workSetUp)){
//                                                                        PWD_AvailableJobOffers_1_Model model = job_snapshot.getValue(PWD_AvailableJobOffers_1_Model.class);
//                                                                        jobs_list.add(model);
//                                                                    }
//                                                                }else{
//                                                                    //display here
//                                                                    PWD_AvailableJobOffers_1_Model model = job_snapshot.getValue(PWD_AvailableJobOffers_1_Model.class);
//                                                                    jobs_list.add(model);
//                                                                }//work set up
//                                                            }//type of employment req
//                                                        }// if pwd with work exp
//                                                    }// with work experience
//                                                }// without work experience
//                                            }//secondary skills
//                                        }// degree
//                                    }//job title
//                                }//educ attainment
//                            }else{
//                                //Educational Attainment not required
//                                    //for matched job title
//                                    if(job_title.equalsIgnoreCase(pwd_jobTitle)){
//                                        //for matched degree
//                                        if(job_skillCategory.equalsIgnoreCase(pwd_category)){
//                                            //for matched secondary skills
//                                            if(matched_secondary_skills.size() == job_secondary_skills.size()){
//                                                //for matched without work exp
//                                                if(job_workExp.equals("Without Experience")){
//                                                    //for matched type of employment requirement
//                                                    if(job_typeOfEmploymentRequired.equalsIgnoreCase("true")){
//                                                        //for matched type of employment
//                                                        if(job_typeOfEmployment.equalsIgnoreCase(pwd_typeOfEmp)){
//                                                            //for matched work setup req
//                                                            if(job_workSetUpRequired.equalsIgnoreCase("true")){
//                                                                if(job_workSetUp.equalsIgnoreCase(pwd_workSetUp)){
//                                                                    PWD_AvailableJobOffers_1_Model model = job_snapshot.getValue(PWD_AvailableJobOffers_1_Model.class);
//                                                                    jobs_list.add(model);
//                                                                }
//                                                            }else{
//                                                                //display here
//                                                                PWD_AvailableJobOffers_1_Model model = job_snapshot.getValue(PWD_AvailableJobOffers_1_Model.class);
//                                                                jobs_list.add(model);
//                                                            }//work set up
//                                                        }//type of emp
//                                                    }else{
//                                                        //for matched work setup req
//                                                        if(job_workSetUpRequired.equalsIgnoreCase("true")){
//                                                            if(job_workSetUp.equalsIgnoreCase(pwd_workSetUp)){
//                                                                PWD_AvailableJobOffers_1_Model model = job_snapshot.getValue(PWD_AvailableJobOffers_1_Model.class);
//                                                                jobs_list.add(model);
//                                                            }
//                                                        }else{
//                                                            //display here
//                                                            PWD_AvailableJobOffers_1_Model model = job_snapshot.getValue(PWD_AvailableJobOffers_1_Model.class);
//                                                            jobs_list.add(model);
//                                                        }//work set up
//                                                    }//type of employment req
//                                                }else{
//                                                    //for matched with work exp
//                                                    if(job_workExp.equals("With Experience")){
//                                                        //for matched with work exp pwd
//                                                        if(pwd_workExp.equalsIgnoreCase("With Experience")){
//                                                            //for matched with work exp pwd
//                                                            if(job_typeOfEmploymentRequired.equalsIgnoreCase("true")){
//                                                                //for matched type of employment
//                                                                if(job_typeOfEmployment.equalsIgnoreCase(pwd_typeOfEmp)){
//                                                                    //for matched work setup req
//                                                                    if(job_workSetUpRequired.equalsIgnoreCase("true")){
//                                                                        if(job_workSetUp.equalsIgnoreCase(pwd_workSetUp)){
//                                                                            PWD_AvailableJobOffers_1_Model model = job_snapshot.getValue(PWD_AvailableJobOffers_1_Model.class);
//                                                                            jobs_list.add(model);
//                                                                        }
//                                                                    }else{
//                                                                        //display here
//                                                                        PWD_AvailableJobOffers_1_Model model = job_snapshot.getValue(PWD_AvailableJobOffers_1_Model.class);
//                                                                        jobs_list.add(model);
//                                                                    }//work set up
//                                                                }//type of emp
//                                                            }else{
//                                                                //for matched work setup req
//                                                                if(job_workSetUpRequired.equalsIgnoreCase("true")){
//                                                                    if(job_workSetUp.equalsIgnoreCase(pwd_workSetUp)){
//                                                                        PWD_AvailableJobOffers_1_Model model = job_snapshot.getValue(PWD_AvailableJobOffers_1_Model.class);
//                                                                        jobs_list.add(model);
//                                                                    }
//                                                                }else{
//                                                                    //display here
//                                                                    PWD_AvailableJobOffers_1_Model model = job_snapshot.getValue(PWD_AvailableJobOffers_1_Model.class);
//                                                                    jobs_list.add(model);
//                                                                }//work set up
//                                                            }//type of employment req
//                                                        }// if pwd with work exp
//                                                    }// with work experience
//                                                }// without work experience
//                                            }//secondary skills
//                                        }// degree
//                                    }//job title
//                            }//educ attainment req
//                        }//exp date and status
//                    }//typeofdisability
//
                }

                Collections.reverse(jobs_list);
                jobs1_adapter = new PWD_AvailableJobOffers_1_RVAdapter(getContext(), jobs_list);
                jobs1_recycler.setAdapter(jobs1_adapter);
                jobs1_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
    }

    public Date convertDate(String expDate) {
        DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        Date date = null;
        try {
            date = format.parse(expDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public String getCurrentDate(){
        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("MMMM dd, yyyy");
        String curr_Date = df.format(currentDate);

        return curr_Date;
    }

//    public static String toString(String qualification){
//        if (qualification == null) return null;
//
//        int iMax = qualification.length -1;
//        if(iMax == -1) return "[]";
//
//        StringBuilder b = new StringBuilder();
//        b.append('[');
//        for (int i=0; ; i++){
//            StringBuilder append = b.append(String.valueOf(qualification[i]));
//            if (i == qualification.length() -1) return b.append(']').toString();
//            b.append(",");
//        }
//    }

    public Boolean isCriteriaMatch(String pwdRequirement, String criteria){
        if (pwdRequirement.equalsIgnoreCase(criteria)){
            return true;
        }
        else{
            return false;
        }
    }
}