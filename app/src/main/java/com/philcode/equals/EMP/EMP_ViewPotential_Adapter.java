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

import com.philcode.equals.EMP_PWD_Information;

import com.philcode.equals.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EMP_ViewPotential_Adapter extends RecyclerView.Adapter<EMP_ViewPotential_Adapter.MyViewHolder> {
    Context context;
    ArrayList<EMP_PWD_Information> availablejobsinfos;
    public EMP_ViewPotential_Adapter(Context c, ArrayList<EMP_PWD_Information> p) {
        context = c;
        availablejobsinfos = p;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.emp_viewpotential_cardview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get().load(availablejobsinfos.get(position).getPwdProfilePic()).into(holder.displayPic);

        holder.displayName.setText(String.format("%s %s", availablejobsinfos.get(position).getFirstName(), availablejobsinfos.get(position).getLastName()));
        holder.displayContact.setText(availablejobsinfos.get(position).getContact());
        holder.displayEmail.setText(availablejobsinfos.get(position).getEmail());
        holder.btnViewPost.setVisibility(View.VISIBLE);
        holder.onClick(position);
    }

    @Override
    public int getItemCount() {
        return availablejobsinfos.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView displayName, displayEmail,displayContact;
        ImageView displayPic;

        Button btnViewPost;

        public MyViewHolder(View itemView) {
            super(itemView);
            displayPic = itemView.findViewById(R.id.pwd_user_imageview);

            displayName = itemView.findViewById(R.id.displayName);
            displayContact = itemView.findViewById(R.id.displayContact);
            displayEmail = itemView.findViewById(R.id.displayEmail);
            btnViewPost = itemView.findViewById(R.id.btnViewCategory);

        }

        public void onClick(final int position) {
            btnViewPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, EMP_ViewPotential_View.class);
                    i.putExtra("email",availablejobsinfos.get(position).getEmail());
                    i.putExtra("address", availablejobsinfos.get(position).getAddress());
                    i.putExtra("contact",availablejobsinfos.get(position).getContact());
                    i.putExtra("firstName",availablejobsinfos.get(position).getFirstName());
                    i.putExtra("lastName",availablejobsinfos.get(position).getLastName());
                    i.putExtra("typeOfDisability1",availablejobsinfos.get(position).getTypeOfDisability1());
                    i.putExtra("typeOfDisability2",availablejobsinfos.get(position).getTypeOfDisability2());
                    i.putExtra("typeOfDisability3",availablejobsinfos.get(position).getTypeOfDisability3());
                    i.putExtra("typeOfDisabilityMore",availablejobsinfos.get(position).getTypeOfDisabilityMore());
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
                    i.putExtra("pwdProfilePic",availablejobsinfos.get(position).getPwdProfilePic());
                    i.putExtra("pwdIdCardNum",availablejobsinfos.get(position).getPwdIdCardNum());
                    i.putExtra("typeStatus",availablejobsinfos.get(position).getTypeStatus());

                    //added
                    i.putExtra("educationalAttainment", availablejobsinfos.get(position).getEducationalAttainment());
                    i.putExtra("skill", availablejobsinfos.get(position).getSkill());
                    i.putExtra("workExperience", availablejobsinfos.get(position).getWorkExperience());
                    i.putExtra("primarySkill1", availablejobsinfos.get(position).getPrimarySkill1());
                    i.putExtra("primarySkill2", availablejobsinfos.get(position).getPrimarySkill2());
                    i.putExtra("primarySkill3", availablejobsinfos.get(position).getPrimarySkill3());
                    i.putExtra("primarySkill4", availablejobsinfos.get(position).getPrimarySkill4());
                    i.putExtra("primarySkill5", availablejobsinfos.get(position).getPrimarySkill5());
                    i.putExtra("primarySkill6", availablejobsinfos.get(position).getPrimarySkill6());
                    i.putExtra("primarySkill7", availablejobsinfos.get(position).getPrimarySkill7());
                    i.putExtra("primarySkill8", availablejobsinfos.get(position).getPrimarySkill8());
                    i.putExtra("primarySkill9", availablejobsinfos.get(position).getPrimarySkill9());
                    i.putExtra("primarySkill10", availablejobsinfos.get(position).getPrimarySkill10());
                    i.putExtra("primarySkillOther", availablejobsinfos.get(position).getPrimarySkillOther());
                    i.putExtra("city", availablejobsinfos.get(position).getCity());
                    context.startActivity(i);
                }
            });
        }
    }
}