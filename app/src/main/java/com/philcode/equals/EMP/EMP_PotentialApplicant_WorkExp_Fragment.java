package com.philcode.equals.EMP;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.philcode.equals.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EMP_PotentialApplicant_WorkExp_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EMP_PotentialApplicant_WorkExp_Fragment extends Fragment {
    private RecyclerView work_recyclerView;
    private List<EMPToPWD_WokExperienceModel> work_list;
    private EMPToPWD_WorkExperienceAdapter work_adapter;
    DatabaseReference pwd_reference;

    TextView  m_workExperience;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EMP_PotentialApplicant_WorkExp_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EMP_PotentialApplicant_WorkExp_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EMP_PotentialApplicant_WorkExp_Fragment newInstance(String param1, String param2) {
        EMP_PotentialApplicant_WorkExp_Fragment fragment = new EMP_PotentialApplicant_WorkExp_Fragment();
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
        return inflater.inflate(R.layout.fragment_emp_pwdworkexp, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //initialize layout components
        m_workExperience = view.findViewById(R.id.displayTotalWorkExperience);
        work_recyclerView = view.findViewById(R.id.workRecyclerView);

        work_recyclerView.setHasFixedSize(true);
        work_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        pwd_reference = FirebaseDatabase.getInstance().getReference().child("PWD");

        String pwd_AuthID = getActivity().getIntent().getStringExtra("PWD_ID");
        pwd_reference.child(pwd_AuthID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String workExperience = snapshot.child("workExperience").getValue().toString();
                if(workExperience.equals("With Experience")){
                    String pwd_AuthID = getActivity().getIntent().getStringExtra("PWD_ID");
                    pwd_reference = FirebaseDatabase.getInstance().getReference().child("PWD").child(pwd_AuthID).child("listOfWorks");
                    work_recyclerView.setVisibility(View.VISIBLE);
                    pwd_reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            work_list = new ArrayList<>();
                            work_list.clear();
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                EMPToPWD_WokExperienceModel p = dataSnapshot1.getValue(EMPToPWD_WokExperienceModel.class);
                                work_list.add(p);
                            }
                            Collections.reverse(work_list);
                            work_adapter = new EMPToPWD_WorkExperienceAdapter(getContext(), work_list);
                            work_recyclerView.setAdapter(work_adapter);
                            work_adapter.notifyDataSetChanged();
                            m_workExperience.setText(workExperience + "\n" + "Scroll down to view work experience list.");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
                    m_workExperience.setText("Scroll down to view work experience list.");
                }else{
                    m_workExperience.setText(workExperience);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}