package com.philcode.equals;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;

public class Post_Home_MyAdapter extends RecyclerView.Adapter<Post_Home_MyAdapter.PWD_Home_ViewHolder> {

   Context context;
   private List<Post_HomeInformation> homeInfos;

   public Post_Home_MyAdapter(Context c, List<Post_HomeInformation> p ){
      context = c;
      homeInfos = p;
   }

   @NonNull
   @Override
   public PWD_Home_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      return new PWD_Home_ViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_home_template_pwd, parent, false));
   }
   @Override
   public void onBindViewHolder(@NonNull PWD_Home_ViewHolder holder, int position) {
      String timeStamp = homeInfos.get(position).getFormattedDate();

      Picasso.get().load(homeInfos.get(position).getPostImage()).fit().centerCrop().into(holder.picfeed);
      holder.titlefeed.setText(homeInfos.get(position).getPostContentTitle());
      holder.descfeed.setText(homeInfos.get(position).getPostDescription());

      Calendar calendar = Calendar.getInstance();
      calendar.setTimeInMillis(Long.parseLong(timeStamp));
      String currentDate  = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

      holder.datefeed.setText(("Posted on " + currentDate));

      holder.picfeed.setVisibility(View.VISIBLE);
      holder.onClick(position);
   }

   @Override
   public int getItemCount() {
      return homeInfos.size();
   }

   class PWD_Home_ViewHolder extends RecyclerView.ViewHolder{
      TextView titlefeed,descfeed, datefeed;
       ImageView picfeed;

      public PWD_Home_ViewHolder(View itemView){
         super(itemView);
         titlefeed = itemView.findViewById(R.id.home_feed_title);
         descfeed = itemView.findViewById(R.id.home_feed_desc);
         picfeed = itemView.findViewById(R.id.home_feed_pic);
         datefeed = itemView.findViewById(R.id.home_date);
        // cardView = itemView.findViewById(R.id.homeLayout);
      }

      public void onClick(final int position) {
         picfeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Intent i = new Intent(context, Home_View.class);
               i.putExtra("postImage", homeInfos.get(position).getPostImage());
               i.putExtra("postContentTitle", homeInfos.get(position).getPostContentTitle());
               i.putExtra("postContentDescription", homeInfos.get(position).getPostDescription());
               i.putExtra("formattedDate", homeInfos.get(position).getFormattedDate());
               context.startActivity(i);
            }
         });
      }
   }

}
