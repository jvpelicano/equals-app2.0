package com.philcode.equals.EMP;

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
        holder.displayPostTitle.setText(availablejobsinfos.get(position).getPostTitle());
        holder.displayCompanyName.setText(availablejobsinfos.get(position).getCompanyName());
        holder.displayPermission.setText(availablejobsinfos.get(position).getPermission());
        holder.btnViewPost.setVisibility(View.VISIBLE);
        holder.onClick(position);
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

        public void onClick(final int position) {
            btnViewPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, EMP_AvailableJobs_View.class);
                    i.putExtra("imageURL", availablejobsinfos.get(position).getImageURL());
                    i.putExtra("postTitle",availablejobsinfos.get(position).getPostTitle());
                    i.putExtra("companyName", availablejobsinfos.get(position).getCompanyName());
                    i.putExtra("postDescription",availablejobsinfos.get(position).getPostDescription());
                    i.putExtra("postLocation",availablejobsinfos.get(position).getPostLocation());
                    i.putExtra("typeOfDisability1",availablejobsinfos.get(position).getTypeOfDisability1());
                    i.putExtra("typeOfDisability2",availablejobsinfos.get(position).getTypeOfDisability2());
                    i.putExtra("typeOfDisability3",availablejobsinfos.get(position).getTypeOfDisability3());
                    i.putExtra("typeOfDisabilityMore",availablejobsinfos.get(position).getTypeOfDisabilityMore());
                    i.putExtra("expDate",availablejobsinfos.get(position).getExpDate());
                    i.putExtra("postDate",availablejobsinfos.get(position).getPostDate());
                    i.putExtra("permission", availablejobsinfos.get(position).getPermission());
                    i.putExtra("jobSkill1",availablejobsinfos.get(position).getJobSkill1());
                    i.putExtra("jobSkill2",availablejobsinfos.get(position).getJobSkill2());
                    i.putExtra("jobSkill3",availablejobsinfos.get(position).getJobSkill3());
                    i.putExtra("jobSkill4",availablejobsinfos.get(position).getJobSkill4());
                    i.putExtra("jobSkill5",availablejobsinfos.get(position).getJobSkill5());
                    i.putExtra("jobSkill6",availablejobsinfos.get(position).getJobSkill6());
                    i.putExtra("jobSkill7",availablejobsinfos.get(position).getJobSkill7());
                    i.putExtra("jobSkill8",availablejobsinfos.get(position).getJobSkill8());
                    i.putExtra("jobSkill9",availablejobsinfos.get(position).getJobSkill9());
                    i.putExtra("jobSkill10",availablejobsinfos.get(position).getJobSkill10());
                   // i.putExtra("ImageID", availablejobsinfos.get(position).getImageID());//------------------
                    //added
                    i.putExtra("educationalAttainment", availablejobsinfos.get(position).getEducationalAttainment());
                    i.putExtra("skill", availablejobsinfos.get(position).getSkill());
                    i.putExtra("workExperience", availablejobsinfos.get(position).getWorkExperience());

                    i.putExtra("city", availablejobsinfos.get(position).getCity());
                    context.startActivity(i);
                }
            });
        }
    }
}