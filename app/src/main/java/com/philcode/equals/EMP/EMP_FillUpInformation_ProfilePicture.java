package com.philcode.equals.EMP;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.philcode.equals.R;

import java.io.IOException;

public class EMP_FillUpInformation_ProfilePicture extends Activity {

    //firebase auth object
    private FirebaseAuth firebaseAuth;
    FirebaseStorage storage;
    StorageReference storageReference;
    private DatabaseReference databaseReference, mDatabase;
    String Storage_Path = "Employer_Reg_Form/";
    private ImageView profilePicEMP;
    private Button chooseEmpPic, btnEmpSubmit;
    int PICK_IMAGE_REQUEST = 7;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emp_updateprofile2);
        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Employers");
        firebaseAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        if(firebaseAuth.getCurrentUser() == null){
            //closing this activity
            finish();
            //starting login activity
            startActivity(new Intent(this, LoginActivity_emp.class));
        }

        profilePicEMP = findViewById(R.id.emp_profilePic);
        chooseEmpPic = findViewById(R.id.btn_emp_choose_profilepic);
        btnEmpSubmit = findViewById(R.id.btn_emp_submit);
        btnEmpSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadProfilePic();
            }
        });
        chooseEmpPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Please Select Image"), PICK_IMAGE_REQUEST);
            }

        });
    }
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    public void uploadProfilePic(){
        final Intent intent = new Intent(this, LoginActivity_emp.class);
        if(filePath  != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Finalizing...");
            progressDialog.show();

            final StorageReference ref = storageReference.child(Storage_Path+ System.currentTimeMillis() + "." + GetFileExtension(filePath));
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            String user_id = firebaseAuth.getCurrentUser().getUid();
                            DatabaseReference current_user_db = mDatabase.child(user_id);
                            String emp_ProfilePic = task.getResult().toString();
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Information saved. Please verify your email and wait for the your account approval.", Toast.LENGTH_LONG).show();
                            @SuppressWarnings("VisibleForTests")
                            Emp_ProfilePicInformation EmpInfo = new Emp_ProfilePicInformation(emp_ProfilePic);
                            //Current User
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            mDatabase.child(user.getUid()).child("empProfilePic").setValue(emp_ProfilePic);
                        }
                        //   }
                    });
                    startActivity(intent);
                }
            }).addOnFailureListener(new OnFailureListener(){
                @Override
                public void onFailure(@NonNull Exception e){
                    progressDialog.dismiss();
                    Toast.makeText(EMP_FillUpInformation_ProfilePicture.this, "Failed" +e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Please Wait "+(int)progress+"%");
                    progressDialog.setCancelable(false);
                }
            });

        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            AlertDialog.Builder alert =  new AlertDialog.Builder(EMP_FillUpInformation_ProfilePicture.this);
            alert.setMessage("By clicking Yes, you will return to filling up information.").setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            finish();
                            startActivity(new Intent(getApplicationContext(), RegisterActivity_emp.class));
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = alert.create();
            alertDialog.setTitle("Go Back");
            alertDialog.show();
        }
        return super.onKeyDown(keyCode, event);
    }


    protected  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data !=null && data.getData() != null){
            filePath = data.getData();
            try{
                Bitmap bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                profilePicEMP.setImageBitmap(bitmap1);

            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
