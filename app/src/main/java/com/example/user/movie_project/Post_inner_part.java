package com.example.user.movie_project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Post_inner_part extends AppCompatActivity {

    private ImageView imageView;
    private TextView post_title;
    private TextView post_desc;
    private DatabaseReference mDatabase;
    private String mPostKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_inner_part);

        mPostKey = getIntent().getExtras().getString("blog_id");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Community");

        imageView = (ImageView)findViewById(R.id.image_post);
        post_title =  (TextView)findViewById(R.id.inner_post_title);
        post_desc = (TextView)findViewById(R.id.inner_post_desc);

        mDatabase.child(mPostKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String title_view = (String)dataSnapshot.child("Title").getValue();
                String title_desc = (String) dataSnapshot.child("Description").getValue();
                String post_image= (String)dataSnapshot.child("Image").getValue();

                post_title.setText(title_view);
                post_desc.setText(title_desc);
                Picasso.with(Post_inner_part.this).load(post_image).into(imageView);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
