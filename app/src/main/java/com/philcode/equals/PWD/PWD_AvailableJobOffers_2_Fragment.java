package com.philcode.equals.PWD;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PWD_AvailableJobOffers_2_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PWD_AvailableJobOffers_2_Fragment extends Fragment {
    private View view;
    private DatabaseReference pwd_root, job_root, pwdInfo;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    //recycler
    private List<PWD_AvailableJobOffers_2_Model> jobs_list;
    private PWD_AvailableJobOffers_2_RVAdapter jobs2_adapter;
    private RecyclerView jobs2_recycler;

    //secondary skills
    private ArrayList<String> pwd_secondary_skills;
    private ArrayList<String> job_secondary_skills;

    //String
    private String uid;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PWD_AvailableJobOffers_2_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PWD_AvailableJobOffers_2_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PWD_AvailableJobOffers_2_Fragment newInstance(String param1, String param2) {
        PWD_AvailableJobOffers_2_Fragment fragment = new PWD_AvailableJobOffers_2_Fragment();
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
        view = inflater.inflate(R.layout.fragment_pwd_availablejoboffers_2, container, false);
        jobs2_recycler = view.findViewById(R.id.recycler_joboffers_2);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        jobs2_recycler.setLayoutManager(manager);
        jobs2_recycler.setHasFixedSize(true);
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
    public void getJobPostInfo(){}
    public void matchJobOffer(String pwd_jobTitle, String pwd_category, String pwd_edAttainment,ArrayList<String> m_pwd_secondary_skills,
                              String pwd_workExp, String pwd_workSetUp, String pwd_typeOfEmp, String pwdDisability){
        job_root.addValueEventListener(new ValueEventListener() {
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
                    final String job_educationalAttainmentRequirement = job_snapshot.child("educationalAttainmentRequirement").getValue().toString();
                    final String job_workExp = job_snapshot.child("workExperience").getValue().toString();
                    final String job_workSetUp = job_snapshot.child("workSetUp").getValue().toString();
                    final String job_workSetUpRequired = job_snapshot.child("workSetUpRequired").getValue().toString();
                    final String job_typeOfEmploymentRequired = job_snapshot.child("typeOfEmploymentRequired").getValue().toString();

                    //get all typeOfDisability available in the list of job offers
                    String job_typeOfDisability1 = (job_snapshot.hasChild("typeOfDisability1") ? job_snapshot.child("typeOfDisability1").getValue().toString() : "");
                    String job_typeOfDisability2 = (job_snapshot.hasChild("typeOfDisability2") ? job_snapshot.child("typeOfDisability2").getValue().toString() : "");
                    String job_typeOfDisability3 = (job_snapshot.hasChild("typeOfDisability3") ? job_snapshot.child("typeOfDisability3").getValue().toString() : "");
                    String job_typeOfDisability4 = (job_snapshot.hasChild("typeOfDisability4") ? job_snapshot.child("typeOfDisability4").getValue().toString() : "");
                    String job_typeOfDisabilityMore = (job_snapshot.hasChild("typeOfDisabilityMore") ? job_snapshot.child("typeOfDisabilityMore").getValue().toString() : "");

                    if (job_snapshot.hasChild("educationalAttainment"))
                        job_educationalAttainment = job_snapshot.child("educationalAttainment").getValue().toString();
                    else job_educationalAttainment = null;

                    if (job_snapshot.hasChild("typeOfEmployment"))
                        job_typeOfEmployment = job_snapshot.child("typeOfEmployment").getValue().toString();
                    else job_typeOfEmployment = null;

                    for(int countSecondarySkills = 0; countSecondarySkills < 5; countSecondarySkills++){
                        if(snapshot.hasChild("jobSkills" + countSecondarySkills)){
                            pwd_secondary_skills.add(snapshot.child("jobSkills" + countSecondarySkills).getValue().toString());
                        }
                    }

                    //job qualification second skills
                    for(int countSecondarySkills = 0; countSecondarySkills <= 5; countSecondarySkills++){
                        if(job_snapshot.hasChild("jobSkill" + countSecondarySkills+1)){

                            job_secondary_skills.add(job_snapshot.child("jobSkill" + countSecondarySkills+1).getValue().toString());
                        }
                    }

                    //get matching secondary skills from both sides
                    int job_secondary_skills_length = job_secondary_skills.size();
                    ArrayList<String> matched_secondary_skills = new ArrayList<>();
                    for(int count = 0; count <= job_secondary_skills_length ; count++){
                        if(job_secondary_skills.contains(m_pwd_secondary_skills.get(count))) {
                            matched_secondary_skills.add(m_pwd_secondary_skills.get(count));
                        }
                    }

                    Date expDate = convertDate(job_expDate);
                    Date currDate = convertDate(getCurrentDate());

                    PWD_AvailableJobOffers_2_Model model = job_snapshot.getValue(PWD_AvailableJobOffers_2_Model.class);
                    jobs_list.add(model);

                    if(job_typeOfDisability1.equals(pwdDisability) || job_typeOfDisability2.equals(pwdDisability)
                            || job_typeOfDisability3.equals(pwdDisability) || job_typeOfDisability4.equals(pwdDisability)
                            || job_typeOfDisabilityMore.equals(pwdDisability) ){

                        if((currDate.before(expDate) || currDate.equals(expDate)) && permission.equals("Approved")){

                            if(job_educationalAttainmentRequirement.equalsIgnoreCase("true")){

                                if(job_educationalAttainment.equalsIgnoreCase(pwd_edAttainment)){

                                    if(job_title.equalsIgnoreCase(pwd_jobTitle)){

                                        if(job_skillCategory.equalsIgnoreCase(pwd_category)){

                                        }


                                    }//jobtitle
                                }//educ attainment
                            }else{
                                if(job_title.equalsIgnoreCase(pwd_jobTitle)){

                                    if(job_skillCategory.equalsIgnoreCase(pwd_category)){

                                    }
                                }//jobtitle
                            }//educ attainment requirement
                        }//expdate and status
                    }//type of disability

                }
                Collections.reverse(jobs_list);
                jobs2_adapter = new PWD_AvailableJobOffers_2_RVAdapter(getContext(), jobs_list);
                jobs2_recycler.setAdapter(jobs2_adapter);
                jobs2_adapter.notifyDataSetChanged();
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
}