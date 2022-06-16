package com.philcode.equals.PWD;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.philcode.equals.R;

import java.util.List;

public class PWD_AvailableJobOffers_2_RVAdapter extends RecyclerView.Adapter<PWD_AvailableJobOffers_2_RVAdapter.PWD_AvailableJobOffers_2_ViewHolder> {
    private Context context;
    private List<PWD_AvailableJobOffers_2_Model> jobOffers_2_model;

    public PWD_AvailableJobOffers_2_RVAdapter(Context context, List<PWD_AvailableJobOffers_2_Model> jobOffers_2_model) {
        this.context = context;
        this.jobOffers_2_model = jobOffers_2_model;
    }
    @NonNull
    @Override
    public PWD_AvailableJobOffers_2_RVAdapter.PWD_AvailableJobOffers_2_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PWD_AvailableJobOffers_2_ViewHolder(LayoutInflater.from(context).inflate(R.layout.pwd_availablejobs_cardview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PWD_AvailableJobOffers_2_RVAdapter.PWD_AvailableJobOffers_2_ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return jobOffers_2_model.size();
    }

    public class PWD_AvailableJobOffers_2_ViewHolder extends RecyclerView.ViewHolder {
        public PWD_AvailableJobOffers_2_ViewHolder(@NonNull View itemView) {
            super(itemView);

            //initialize variables here...
        }
    }
}
