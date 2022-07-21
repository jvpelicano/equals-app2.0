package com.philcode.equals.PWD;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.philcode.equals.R;
import com.squareup.picasso.Picasso;

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
        int pos = position;
        Picasso.get().load(jobOffers_2_model.get(pos).getImageURL()).into(holder.displayPostPic);
        holder.displayPostTitle.setText(jobOffers_2_model.get(pos).getJobTitle());
        holder.displayCompanyName.setText(jobOffers_2_model.get(pos).getCompanyName());
        holder.displayPostDate.setText(jobOffers_2_model.get(pos).getPostDate());
        holder.btnViewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent view_JobPost = new Intent(context, PWD_AvailableJobs_View.class);
                view_JobPost.putExtra("POST_ID", jobOffers_2_model.get(pos).getPostJobId());
                view_JobPost.putExtra("POST_MATCH_STATUS", "SEMI_MATCHED");
                context.startActivity(view_JobPost);
            }
        });
    }

    @Override
    public int getItemCount() {
        return jobOffers_2_model.size();
    }

    public class PWD_AvailableJobOffers_2_ViewHolder extends RecyclerView.ViewHolder {
        private ImageView displayPostPic;
        private TextView displayPostTitle, displayCompanyName, displayPostDate;
        private Button btnViewPost;
        public PWD_AvailableJobOffers_2_ViewHolder(@NonNull View itemView) {
            super(itemView);
            displayPostPic = itemView.findViewById(R.id.displayPostPic);
            displayPostTitle = itemView.findViewById(R.id.displayPostTitle);
            displayCompanyName = itemView.findViewById(R.id.displayCompanyName);
            displayPostDate = itemView.findViewById(R.id.displayPostDate);
            btnViewPost =itemView.findViewById(R.id.btnViewPost);

            //initialize variables here...
        }
    }
}
