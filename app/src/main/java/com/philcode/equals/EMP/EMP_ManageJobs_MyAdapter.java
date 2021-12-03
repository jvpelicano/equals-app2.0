package com.philcode.equals.EMP;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.philcode.equals.PWD.PWD_AvailableJobs_View;
import com.philcode.equals.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EMP_ManageJobs_MyAdapter extends RecyclerView.Adapter<EMP_ManageJobs_MyAdapter.MyViewHolder> {
    Context context;
    ArrayList<EMP_ManageJobs_Information> availablejobsinfos;
    public EMP_ManageJobs_MyAdapter(Context c, ArrayList<EMP_ManageJobs_Information> p) {
        context = c;
        availablejobsinfos = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.emp_availablejobs_cardview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get().load(availablejobsinfos.get(position).getImageURL()).into(holder.displayPostPic);
        holder.displayPostTitle.setText(availablejobsinfos.get(position).getDisplayPostTitle());
        holder.displayCompanyName.setText(availablejobsinfos.get(position).getDisplayCompanyName());
        holder.displayPermission.setText(availablejobsinfos.get(position).getPermission());
        holder.btnViewPost.setVisibility(View.VISIBLE);

        String jobPostID_bind = availablejobsinfos.get(position).getPostJobID();
        holder.onClick(position, jobPostID_bind);
    }

    @Override
    public int getItemCount() {
        return availablejobsinfos.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView displayPostTitle, displayCompanyName, displayPermission;
        ImageView displayPostPic;

        Button btnViewPost;

        public MyViewHolder(View itemView) {
            super(itemView);
            displayPostPic = (ImageView) itemView.findViewById(R.id.displayPostPic);
            displayPostTitle = itemView.findViewById(R.id.displayPostTitle);
            displayCompanyName = itemView.findViewById(R.id.displayCompanyName);

            displayPermission = itemView.findViewById(R.id.displayPermission);
            btnViewPost = itemView.findViewById(R.id.btnViewPost);

        }

        public void onClick(final int position, String jobPostID) {
            btnViewPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, EMP_AvailableJobs_View.class);
                    i.putExtra("POST_ID", jobPostID);
                    i.putExtra("INT_POS", position);
                    context.startActivity(i);
                }
            });
        }
    }
}