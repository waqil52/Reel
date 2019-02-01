package com.example.user.movie_project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class PostActivity extends AppCompatActivity {

    private ImageButton mSelectImage;
    private static final int GALLERY_REQUEST = 1;

    private Uri imageUri = null;
    private EditText mPostTitle;
    private EditText mPostDesc;

    private Button mSubmitBtn;

    private ProgressDialog mProgress;

    private StorageReference mStorageReference;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mStorageReference = FirebaseStorage.getInstance().getReference();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("blog");


        mSelectImage = findViewById(R.id.imageButton1);
        mPostTitle = findViewById(R.id.edit_text);
        mPostDesc = findViewById(R.id.edit_text2);
        mSubmitBtn = findViewById(R.id.id_submit);



        mProgress = new ProgressDialog(this);

        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            imageUri = data.getData();
            mSelectImage.setImageURI(imageUri);
        }
    }

    public void startPosting(final View view) {
        mProgress.setMessage("Posting to Blog...");
        final String title = mPostTitle.getText().toString().trim();
        final String desc = mPostDesc.getText().toString().trim();

        if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(desc) && imageUri != null) {
            mProgress.show();
            final StorageReference filepath = mStorageReference.child("Blog").child(imageUri.getLastPathSegment());
            filepath.putFile(imageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return filepath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();

                        DatabaseReference newPost = mDatabaseReference.push();

                        newPost.child("title").setValue(title);
                        newPost.child("description").setValue(desc);
                        newPost.child("image").setValue(downloadUri.toString());

                        mProgress.dismiss();



                        Snackbar.make(view.getRootView(), "Successfully Posted", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();

                        Intent intent = null;
                        intent = new Intent(PostActivity.this,BlogActivity.class);
                        startActivity(intent);

                    }
                }
            });
        }
    }
}
