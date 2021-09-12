package com.philcode.equals;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;

public class PrivacyPolicyPDFViewer extends AppCompatActivity {

    PDFView privacypolicy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacy_policy);

        privacypolicy = findViewById(R.id.pdfView);

        privacypolicy.fromAsset("equals_privacy_policy.pdf").load();
    }
}
