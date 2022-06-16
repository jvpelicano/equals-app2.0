package com.philcode.equals.PWD;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.philcode.equals.R;

import java.util.List;

public class PWD_AvailableJobOffers_3_RVAdapter extends RecyclerView.Adapter<PWD_AvailableJobOffers_3_RVAdapter.PWD_AvailableJobOffers_3_ViewHolder> {
    private Context context;
    private List<PWD_AvailableJobOffers_3_Model> jobOffers_3_model;

    public PWD_AvailableJobOffers_3_RVAdapter(Context c, List<PWD_AvailableJobOffers_3_Model> list) {
        context = c;
        jobOffers_3_model = list;

    }

    @NonNull
    @Override
    public PWD_AvailableJobOffers_3_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PWD_AvailableJobOffers_3_RVAdapter.PWD_AvailableJobOffers_3_ViewHolder(LayoutInflater.from(context).inflate(R.layout.pwd_availablejobs_cardview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PWD_AvailableJobOffers_3_ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class PWD_AvailableJobOffers_3_ViewHolder extends RecyclerView.ViewHolder {
        public PWD_AvailableJobOffers_3_ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
