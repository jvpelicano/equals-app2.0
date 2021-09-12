package com.philcode.equals;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class Home_View extends AppCompatActivity {
    private Toolbar dToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_view);

        getIncomingIntent();
/*
        dToolbar = findViewById(R.id.nav_action_bar_del);
        setSupportActionBar(dToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

    }

    private void getIncomingIntent() {
        Log.d(TAG, "getIncomingIntent: checking for incoming intents.");

        if(getIntent().hasExtra("postImage")
                && getIntent().hasExtra("postContentTitle")
                && getIntent().hasExtra("postContentDescription")
                && getIntent().hasExtra("formattedDate"))
        {
            Log.d(TAG, "getIncomingIntent: found intent extras.");

            String postImage = getIntent().getStringExtra("postImage");
            String postContentTitle = getIntent().getStringExtra("postContentTitle");
            String postContentDescription = getIntent().getStringExtra("postContentDescription");
            String formattedDate = "Posted on " + getIntent().getStringExtra("formattedDate");
            setImage(postImage,postContentTitle, postContentDescription, formattedDate);
        }
    }

    private void setImage(String postImage, String postContentTitle, String postContentDescription, String formattedDate) {

        ImageView images = findViewById(R.id.displayBlogPic);
        Glide.with(this)
                .asBitmap()
                .load(postImage)
                .into(images);

        TextView title = findViewById(R.id.displayBlogTitle);
        title.setText(postContentTitle);

        TextView desc = findViewById(R.id.displayBlogDescription);
        desc.setText(postContentDescription);

        TextView date = findViewById(R.id.displayformattedDate);
        date.setText(formattedDate);
    }
}