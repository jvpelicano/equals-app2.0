package com.philcode.equals.PWD;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.philcode.equals.R;

import java.util.List;

public class PWD_AvailableJobOffers_1_RVAdapter extends RecyclerView.Adapter<PWD_AvailableJobOffers_1_RVAdapter.PWD_AvailableJobsOffer_1_ViewHolder>{
    private Context context;
    private List<PWD_AvailableJobOffers_1_Model> jobOffers_1_model;

    public PWD_AvailableJobOffers_1_RVAdapter(Context c, List<PWD_AvailableJobOffers_1_Model> list ) {
        context = c;
        jobOffers_1_model = list;
    }

    @NonNull
    @Override
    public PWD_AvailableJobsOffer_1_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PWD_AvailableJobsOffer_1_ViewHolder(LayoutInflater.from(context).inflate(R.layout.pwd_availablejobs_cardview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PWD_AvailableJobsOffer_1_ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return jobOffers_1_model.size();
    }

    public class PWD_AvailableJobsOffer_1_ViewHolder extends RecyclerView.ViewHolder {
        public PWD_AvailableJobsOffer_1_ViewHolder(@NonNull View itemView) {
            super(itemView);

            //initialize variables here...
        }
    }
}
