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
import java.util.ArrayList;

public class PWD_AvailableJobs_MyAdapter extends RecyclerView.Adapter<PWD_AvailableJobs_MyAdapter.MyViewHolder> {
    Context context;
    ArrayList<PWD_Recycler_AvailableJobs_Model> availablejobsinfos;
    public PWD_AvailableJobs_MyAdapter(Context c, ArrayList<PWD_Recycler_AvailableJobs_Model> p) {
        context = c;
        availablejobsinfos = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.pwd_availablejobs_cardview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get().load(availablejobsinfos.get(position).getImageURL()).into(holder.displayPostPic);
        holder.displayPostTitle.setText(availablejobsinfos.get(position).getDisplayPostTitle());
        holder.displayCompanyName.setText(availablejobsinfos.get(position).getDisplayCompanyName());
        holder.displayPostDate.setText(availablejobsinfos.get(position).getDisplayPostDate());

        holder.btnViewPost.setVisibility(View.VISIBLE);
        holder.onClick(position);
    }

    @Override
    public int getItemCount() {
        return availablejobsinfos.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView displayPostTitle, displayCompanyName, displayPostDate, displayExpDate;
        ImageView displayPostPic;

        Button btnViewPost;

        public MyViewHolder(View itemView) {
            super(itemView);
            displayPostPic = (ImageView) itemView.findViewById(R.id.displayPostPic);
            displayPostTitle = itemView.findViewById(R.id.displayPostTitle);
            displayCompanyName = itemView.findViewById(R.id.displayCompanyName);
            displayPostDate = itemView.findViewById(R.id.displayPostDate);
            btnViewPost = itemView.findViewById(R.id.btnViewPost);

        }
        public void onClick(final int position) {
            btnViewPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, PWD_AvailableJobs_View.class);
                    context.startActivity(i);
                }
            });
        }
    }
}