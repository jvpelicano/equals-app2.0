package com.philcode.equals.PWD;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
 * Use the {@link PWD_WorkExperience_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PWD_WorkExperience_Fragment extends Fragment {

    private List<PWD_AddWorkInformation> work_list;
    private PWD_WorkExperienceAdapter work_adapter;
    private RecyclerView work_recyclerView;
    private FirebaseUser currentFirebaseUser;
    private String uid;
    TextView displayTotalWorkExperience;
    DatabaseReference rootRef;
    private View view;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PWD_WorkExperience_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PWD_WorkExperience_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PWD_WorkExperience_Fragment newInstance(String param1, String param2) {
        PWD_WorkExperience_Fragment fragment = new PWD_WorkExperience_Fragment();
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
        view  = inflater.inflate(R.layout.fragment_pwd_workexperience, container, false);

        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = currentFirebaseUser.getUid();
        work_recyclerView = view.findViewById(R.id.workRecyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        work_recyclerView.setLayoutManager(manager);
        work_recyclerView.setHasFixedSize(true);



        // Inflate the layout for this fragment
        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rootRef = FirebaseDatabase.getInstance().getReference().child("PWD").child(uid);
        displayTotalWorkExperience = view.findViewById(R.id.displayTotalWorkExperience);
        getUserWorkInfo(uid);
    }

    //Delete on swipe function
    /*ItemTouchHelper.SimpleCallback itemTouchHelper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };*/
/*    public void deleteWorkExp(RecyclerView.ViewHolder holder){
        String workUUID = work_list.get(holder.getAdapterPosition()).getWorkID();
        rootRef.child(workUUID).removeValue();
        work_list.remove(holder.getAdapterPosition());
        work_adapter.notifyDataSetChanged();
    }*/



    public void getUserWorkInfo(String uid){
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String workExperience = dataSnapshot.child("workExperience").getValue().toString();
                if(dataSnapshot.hasChild("listOfWorks") && workExperience.equals("With Experience")){
                    work_recyclerView.setVisibility(View.VISIBLE);
                    displayTotalWorkExperience.setText(workExperience + "\n" + "Scroll down to view work experience list.");
                    DatabaseReference noice = FirebaseDatabase.getInstance().getReference().child("PWD").child(uid).child("listOfWorks");
                    noice.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            work_list = new ArrayList<>();
                            work_list.clear();
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                PWD_AddWorkInformation p = dataSnapshot1.getValue(PWD_AddWorkInformation.class);

                                work_list.add(p);
                            }
                            //Collections.reverse(work_list);
                            work_adapter = new PWD_WorkExperienceAdapter(getContext(), work_list);
                            work_recyclerView.setAdapter(work_adapter);
                            work_adapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
                }else{
                    if(workExperience.equals("With Experience") && !dataSnapshot.hasChild("listOfWorks")){
                        work_recyclerView.setVisibility(View.VISIBLE);
                        displayTotalWorkExperience.setText(workExperience + ", but no previous works information listed.");
                    }else{
                        displayTotalWorkExperience.setText(workExperience);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}