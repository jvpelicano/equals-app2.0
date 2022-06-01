package com.philcode.equals.EMP;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.philcode.equals.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class EMP_ViewResumePDF_Activity extends AppCompatActivity {
    private PDFView pdfView;
    private FloatingActionButton fab_main, fab1_downloadPDF, fab2_manageApplication;
    private String postJobID;
    private TextView textview_downloadPDF, textview_manageApplication;
    private Animation fab_open, fab_close, fab_clock, fab_anticlock;
    private Boolean isOpen = false;
    private DatabaseReference job_reference, pwd_reference, emp_reference;
    private String url, applicant_ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emp_view_resume_pdf_activity);
        //intents
        applicant_ID = getIntent().getStringExtra("applicant_ID");
        postJobID = getIntent().getStringExtra("POST_ID");
        url = getIntent().getStringExtra("PDF_Uri");

        //initialize references
        job_reference = FirebaseDatabase.getInstance().getReference().child("Job_Offers/" + postJobID);
        pwd_reference = FirebaseDatabase.getInstance().getReference().child("PWD");
        emp_reference = FirebaseDatabase.getInstance().getReference().child("Employers");

        //layout
            //initialize floating action button
            fab_main = findViewById(R.id.fab);
            fab1_downloadPDF = findViewById(R.id.fab1);
            fab2_manageApplication = findViewById(R.id.fab2);
            textview_downloadPDF = findViewById(R.id.textview_downloadPDF);
            textview_manageApplication = findViewById(R.id.textview_manageApplication);
            fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
            fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
            fab_clock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_clock);
            fab_anticlock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_anticlock);

            //pdf
            pdfView = findViewById(R.id.pdfView);

        new RetrivePdfStream().execute(url);

        //listeners
        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isOpen) {
                    textview_downloadPDF.setVisibility(View.INVISIBLE);
                    textview_manageApplication.setVisibility(View.INVISIBLE);
                    fab1_downloadPDF.startAnimation(fab_close);
                    fab2_manageApplication.setClickable(false);
                    fab1_downloadPDF.setClickable(false);
                    job_reference.child("Resume").child(applicant_ID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild("applicationStatus")){
                                fab2_manageApplication.setVisibility(View.INVISIBLE);
                            }else{
                                fab2_manageApplication.startAnimation(fab_close);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    isOpen = false;
                } else{
                    job_reference.child("Resume").child(applicant_ID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild("applicationStatus")){
                                textview_manageApplication.setVisibility(View.INVISIBLE);
                                fab2_manageApplication.setVisibility(View.INVISIBLE);
                                fab2_manageApplication.setClickable(false);

                            }else{
                                textview_manageApplication.setVisibility(View.VISIBLE);
                                fab2_manageApplication.startAnimation(fab_open);
                                fab2_manageApplication.setClickable(true);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    textview_downloadPDF.setVisibility(View.VISIBLE);
                    fab1_downloadPDF.startAnimation(fab_open);
                    fab1_downloadPDF.setClickable(true);
                    isOpen = true;
                }

            }
        });

        fab1_downloadPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadFile(url);
            }
        });

        fab2_manageApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(EMP_ViewResumePDF_Activity.this);
                alert.setMessage("Please let us know how the application went.")
                                .setPositiveButton("Hired", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        job_reference.child(postJobID).child("Resume").child(applicant_ID).child("applicationStatus").setValue("applicationPending");
                                        Toast.makeText(EMP_ViewResumePDF_Activity.this, "Please wait for the applicant's confirmation...", Toast.LENGTH_SHORT).show();
                                    }
                                }).setNegativeButton("Cancelled", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                job_reference.child(postJobID).child("Resume").child(applicant_ID).child("applicationStatus").setValue("applicationCancelled");
                                Toast.makeText(EMP_ViewResumePDF_Activity.this, "Application is cancelled.", Toast.LENGTH_SHORT).show();
                            }
                        });

                AlertDialog alertDialog = alert.create();
                alertDialog.setTitle("Choose Action");
                alertDialog.show();
                alertDialog.setCancelable(true);

            }
        });
    }



    private void DownloadFile(String url) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        String title = URLUtil.guessFileName(url, null, null);
        request.setTitle(title);
        request.setDescription("Downloading File please wait...");
        String cookie = CookieManager.getInstance().getCookie(url);
        request.addRequestHeader("cookie", cookie);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title);

        DownloadManager downloadManager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
        downloadManager.enqueue(request);

        Toast.makeText(EMP_ViewResumePDF_Activity.this, "Downloading Started.", Toast.LENGTH_SHORT).show();
    }

    private class RetrivePdfStream extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;
            try {
                URL url = new URL (strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                if(urlConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }

            }catch (IOException e){
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            pdfView.fromStream(inputStream)
                    .swipeHorizontal(true)
                    .enableSwipe(true)
                    .defaultPage(0)
                    .enableDoubletap(true)
                    .pageSnap(true)
                    .load();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        /*Intent intent = new Intent(EMP_ViewResumePDF_Activity.this, EMP_ViewResume.class);
        intent.putExtra("POST_ID", postJobID);
        startActivity(intent);*/
    }
}