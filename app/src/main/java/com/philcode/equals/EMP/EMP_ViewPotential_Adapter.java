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
        String pwd_AuthID = availablejobsinfos.get(position).getKey();
        holder.onClick(position, pwd_AuthID);
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

        public void onClick(final int position, String pwd_AuthID) {
            btnViewPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, EMP_ViewPotential_View.class);
                    i.putExtra("PWD_ID", pwd_AuthID);
                    i.putExtra("INT_POS", position);
                    context.startActivity(i);
                }
            });
        }
    }
}