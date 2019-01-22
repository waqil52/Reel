package com.example.user.movie_project;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class PostActivity extends AppCompatActivity {
    private ImageButton mSelectImage;
    private static final int  GALLERY_REQUEST=1;
    private EditText mPostTitle;
    private EditText mPostDesc;
    private Button mSubmitBtn;
    private Uri mImageUri;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private ProgressDialog mProgress;
    private static final int PERMISSIONS_REQUEST_READ_STORAGE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        mStorage=FirebaseStorage.getInstance().getReference();
        mDatabase=FirebaseDatabase.getInstance().getReference().child("blog");
        mPostTitle=(EditText) findViewById(R.id.title);
        mPostDesc=(EditText) findViewById(R.id.description);
        mSubmitBtn=(Button) findViewById(R.id.buttonpost);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSIONS_REQUEST_READ_STORAGE);

        mProgress=new ProgressDialog(this);

        mSelectImage=(ImageButton) findViewById(R.id.imageselected);
        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent= new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);
            }
        });

        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPosting();
            }
        });
    }

    private void startPosting(){

        mProgress.setMessage("Posting to Blog... ");
        mProgress.show();
        final String title_val= mPostTitle.getText().toString().trim();
        final String desc_val= mPostDesc.getText().toString().trim();
        if(!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(desc_val) && mImageUri!=null ){
            StorageReference filepath=mStorage.child("Blog Images").child(mImageUri.getLastPathSegment());
            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    Toast.makeText(PostActivity.this,"Posted Successfully",Toast.LENGTH_SHORT).show();
                    DatabaseReference newPost=mDatabase.push();

                    newPost.child("title").setValue(title_val);
                    newPost.child("description").setValue(desc_val);
                    newPost.child("image").setValue(urlTask.toString());

                    mProgress.dismiss();
                    startActivity(new Intent (PostActivity.this,BlogActivity.class));
                    while (!urlTask.isSuccessful());
                    Uri downloadUrl = urlTask.getResult();


                    final String sdownload_url = String.valueOf(downloadUrl);
                  ////  Uri downloadUrl= taskSnapshot.getDownloadUrl();
                }
            });

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_REQUEST &&  resultCode==RESULT_OK){
            mImageUri= data.getData();
            mSelectImage.setImageURI(mImageUri);
        }
    }
}
