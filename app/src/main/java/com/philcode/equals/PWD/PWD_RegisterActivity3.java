package com.philcode.equals.PWD;

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

public class PWD_RegisterActivity3 extends Activity {

    //firebase auth object
    private FirebaseAuth firebaseAuth;
    FirebaseStorage storage;
    StorageReference storageReference;
    private DatabaseReference databaseReference, mDatabase;
    String Storage_Path = "PWD_Reg_Form/";
    private ImageView profilepicPWD;
    private Button choosePWDProfilePic, btnPWDsubmit;
    int PICK_IMAGE_REQUEST = 7;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pwd_activity_register3);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("PWD/");

        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, PWD_LoginActivity.class));
        }

        profilepicPWD = findViewById(R.id.imagePWD);
        choosePWDProfilePic = findViewById(R.id.btnChoosepwd_profilePic);
        btnPWDsubmit = findViewById(R.id.btn_pwd_submit);
        btnPWDsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPWDIDPic();
            }
        });
        choosePWDProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Please select image"), PICK_IMAGE_REQUEST);
            }

        });
    }

    public String GetFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void uploadPWDIDPic(){
        final Intent intent = new Intent(this, PWD_LoginActivity.class);
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
                            String pwdprofilepic = task.getResult().toString();
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Information saved. Please verify your e-mail and wait for the your account approval.", Toast.LENGTH_LONG).show();
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            String userId = user.getUid();
                            mDatabase = FirebaseDatabase.getInstance().getReference().child("PWD").child(userId);
                            mDatabase.child("pwdProfilePic").setValue(pwdprofilepic);
                        }
                        //   }
                    });
                    startActivity(intent);

                }
            }).addOnFailureListener(new OnFailureListener(){
                @Override
                public void onFailure(@NonNull Exception e){
                    progressDialog.dismiss();
                    Toast.makeText(PWD_RegisterActivity3.this, "Failed" +e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Loading "+(int)progress+"%");
                }
            });

        }
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {

            AlertDialog.Builder alert =  new AlertDialog.Builder(PWD_RegisterActivity3.this);
            alert.setMessage("By clicking Yes, you will return to filling up information.").setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            user.delete();
                            finish();
                            startActivity(new Intent(getApplicationContext(), PWD_RegisterActivity.class));
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
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                profilepicPWD.setImageBitmap(bitmap);

            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }
   /* public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }
*/
}