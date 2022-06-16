package com.philcode.equals.PWD;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
        return jobOffers_3_model.size();
    }

    private ImageView displayPostPic;
    private TextView displayPostTitle, displayCompanyName, displayPostDate;
    private Button btnViewPost;
    public class PWD_AvailableJobOffers_3_ViewHolder extends RecyclerView.ViewHolder {
        public PWD_AvailableJobOffers_3_ViewHolder(@NonNull View itemView) {
            super(itemView);
            displayPostPic = itemView.findViewById(R.id.displayPostPic);
            displayPostTitle = itemView.findViewById(R.id.displayPostTitle);
            displayCompanyName = itemView.findViewById(R.id.displayCompanyName);
            displayPostDate = itemView.findViewById(R.id.displayPostDate);
            btnViewPost =itemView.findViewById(R.id.btnViewPost);
        }
    }
}
