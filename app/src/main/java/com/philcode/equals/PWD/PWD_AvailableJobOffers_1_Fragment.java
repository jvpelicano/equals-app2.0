package com.philcode.equals.PWD;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

    private String uid;

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


        pwdQualification();
    }

    public void pwdQualification(){
        pwdInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final String jobTitle = snapshot.child("jobTitle").getValue().toString();
                final String pwdCategory = snapshot.child("skill").getValue().toString();
                final String edAttainment = snapshot.child("educationalAttainment").getValue().toString();
                final String workExperience = snapshot.child("workExperience").getValue().toString();
                final String typeOfEmployment = snapshot.child("typeOfEmployment").getValue().toString();


//                Toast.makeText(getContext(), "pwdTItle :" + jobTitle, Toast.LENGTH_LONG).show();
                matchJobOffer(jobTitle, pwdCategory, edAttainment);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    //String jobTitle, String category, String edAttainment, String permission, String disability, String workExp, String [] skill

    public void matchJobOffer(String pwd_jobTitle, String category, String pwd_edAttainment){
        job_root.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                jobs_list = new ArrayList<>();
                jobs_list.clear();
                for(DataSnapshot job_snapshot : snapshot.getChildren()){
                    final String permission = job_snapshot.child("permission").getValue().toString();
                    final String job_expDate = job_snapshot.child("expDate").getValue().toString();
                    final String job_title = job_snapshot.child("jobTitle").getValue().toString();
                    final String job_skillCategory = job_snapshot.child("skill").getValue().toString();
                    final String job_educationalAttainmentRequirement = job_snapshot.child("educationalAttainmentRequirement").getValue().toString();
                    final String job_educationalAttainment = job_snapshot.child("educationalAttainment").getValue().toString();
                    final String job_workExp = job_snapshot.child("workExperience").getValue().toString();
                    final String typeOfEmploymentRequired = job_snapshot.child("typeOfEmploymentRequired").getValue().toString();
                    final String job_workSetUp = job_snapshot.child("workSetUp").getValue().toString();
                    final String job_workSetUpRequired = job_snapshot.child("workSetUpRequired").getValue().toString();
                    final String job_city = job_snapshot.child("city").getValue().toString();


                    Date currentDate = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("MMMM dd, yyyy");
                    String curr_Date = df.format(currentDate);

                    Date expDate = convertDate(job_expDate);
                    Date currDate = convertDate(curr_Date);

                    //for Not Expired Job Post and Approved Job Post
                    if((currDate.before(expDate) || currDate.equals(expDate)) && permission.equals("Approved")){
                        //for matched jobTitle
                        if (job_title.equals(pwd_jobTitle)){
                            //for matched Category
                            if (job_skillCategory.equals(category)){

                                if (job_educationalAttainmentRequirement.equalsIgnoreCase("true") &&
                                        job_educationalAttainment.equals(pwd_edAttainment)){

                                    PWD_AvailableJobOffers_1_Model model = job_snapshot.getValue(PWD_AvailableJobOffers_1_Model.class);
                                    jobs_list.add(model);
                                }
                            }
                        }
                    }


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
}