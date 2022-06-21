package com.philcode.equals.PWD;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PWD_AvailableJobOffers_3_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PWD_AvailableJobOffers_3_Fragment extends Fragment {
    private View view;
    private DatabaseReference pwd_root, job_root;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    //recycler
    private List<PWD_AvailableJobOffers_3_Model> jobs_list;
    private PWD_AvailableJobOffers_3_RVAdapter jobs3_adapter;
    private RecyclerView jobs3_recycler;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PWD_AvailableJobOffers_3_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PWD_AvailableJobOffers_3_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PWD_AvailableJobOffers_3_Fragment newInstance(String param1, String param2) {
        PWD_AvailableJobOffers_3_Fragment fragment = new PWD_AvailableJobOffers_3_Fragment();
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
        view = inflater.inflate(R.layout.fragment_pwd_availablejoboffers_3, container, false);

        jobs3_recycler = view.findViewById(R.id.recycler_joboffers_3);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        jobs3_recycler.setLayoutManager(manager);
        jobs3_recycler.setHasFixedSize(true);
        // Inflate the layout for this fragment
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        job_root = FirebaseDatabase.getInstance().getReference().child("Job_Offers");
        pwd_root = FirebaseDatabase.getInstance().getReference().child("PWD");
        firebaseAuth = FirebaseAuth.getInstance();

        final String uID = firebaseAuth.getCurrentUser().getUid();
        pwd_root.child(uID);

        matchJobOffer();
    }
    public void getUserInfo(){

    }
    public void getJobPostInfo(){

    }
    public void matchJobOffer(){
        job_root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                jobs_list = new ArrayList<>();
                jobs_list.clear();
                for(DataSnapshot job_snapshot : snapshot.getChildren()){
                    PWD_AvailableJobOffers_3_Model model = job_snapshot.getValue(PWD_AvailableJobOffers_3_Model.class);
                    jobs_list.add(model);
                }
                Collections.reverse(jobs_list);
                jobs3_adapter = new PWD_AvailableJobOffers_3_RVAdapter(getContext(), jobs_list);
                jobs3_recycler.setAdapter(jobs3_adapter);
                jobs3_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}