package com.example.user.movie_project;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class localMainActivity extends AppCompatActivity {

    private RecyclerView mBloglist;
    private DatabaseReference mDatabase;

    private RecyclerView mBloglist2;
    private DatabaseReference mDatabase2;

    private EditText mSearchField;
    private ImageButton mSearchBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.local_activity_main);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#37474e")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Community");
        mDatabase2 = FirebaseDatabase.getInstance().getReference().child("Community");

        mBloglist = (RecyclerView)findViewById(R.id.blog_list);
        mBloglist.setHasFixedSize(true);
        mBloglist.setLayoutManager(new LinearLayoutManager(this));

        mBloglist2 = (RecyclerView)findViewById(R.id.blog_list);
        mBloglist2.setHasFixedSize(true);
        mBloglist2.setLayoutManager(new LinearLayoutManager(this));

        mSearchField = (EditText) findViewById(R.id.search_field);
        mSearchBtn = (ImageButton) findViewById(R.id.search_btn);

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = mSearchField.getText().toString();

                firebaseUserSearch(searchText);

            }
        });

    }


    private void firebaseUserSearch(String searchText) {

        Toast.makeText(localMainActivity.this, "Started Search", Toast.LENGTH_LONG).show();

        Query firebaseSearchQuery = mDatabase2.orderByChild("Title").startAt(searchText).endAt(searchText + "\uf8ff");

        FirebaseRecyclerAdapter<localBlog, BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<localBlog, BlogViewHolder>(localBlog.class,
                R.layout.local_blog_row,
                BlogViewHolder.class,
                firebaseSearchQuery) {

            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, localBlog model, int position) {
                final String post_key = getRef(position).getKey();

                viewHolder.setTitle(model.getTitle());
                //viewHolder.setDesc(model.getDescription());
                viewHolder.setGenre(model.getGenre());
                viewHolder.setRating(model.getRating());
                viewHolder.setRelease_year(model.getRelease_year());
                viewHolder.setImage(getApplicationContext(), model.getImage());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(MainActivity.this, post_key, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(localMainActivity.this, localPost_inner_part.class);
                        intent.putExtra("blog_id", post_key);
                        startActivity(intent);
                    }
                });
            }
        };
        mBloglist2.setAdapter(firebaseRecyclerAdapter);

        /*FirebaseRecyclerAdapter<Users, UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users, UsersViewHolder>(

                Users.class,
                R.layout.list_layout,
                UsersViewHolder.class,
                firebaseSearchQuery

        ) {
            @Override
            protected void populateViewHolder(UsersViewHolder viewHolder, Users model, int position) {


                viewHolder.setDetails(getApplicationContext(), model.getName(), model.getStatus(), model.getImage());

            }
        };

        mResultList.setAdapter(firebaseRecyclerAdapter);*/

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<localBlog, BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<localBlog, BlogViewHolder>(localBlog.class,
                R.layout.local_blog_row,
                BlogViewHolder.class,
                mDatabase) {

            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, localBlog model, int position) {
                final String post_key = getRef(position).getKey();

                viewHolder.setTitle(model.getTitle());
                //viewHolder.setDesc(model.getDescription());
                viewHolder.setGenre(model.getGenre());
                viewHolder.setRating(model.getRating());
                viewHolder.setRelease_year(model.getRelease_year());
                viewHolder.setImage(getApplicationContext(), model.getImage());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(MainActivity.this, post_key, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(localMainActivity.this, localPost_inner_part.class);
                        intent.putExtra("blog_id", post_key);
                        startActivity(intent);
                    }
                });
            }


        };
        mBloglist.setAdapter(firebaseRecyclerAdapter);
    }





    //Set title, genre etc
    public static class BlogViewHolder extends ViewHolder {
        View mView;

        public BlogViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setTitle(String title){
            TextView post_title = (TextView)mView.findViewById(R.id.item_movie_title);
            post_title.setText(title);
        }
        public void setRating(String rating) {
            TextView mRating = (TextView)mView.findViewById(R.id.item_movie_rating);
            mRating.setText(rating);
        }

        public void setGenre(String genre) {
            TextView mgenre = (TextView)mView.findViewById(R.id.item_movie_genre);
            mgenre.setText(genre);
        }

        public void setRelease_year(String release_year) {
            TextView rel_year = (TextView)mView.findViewById(R.id.item_movie_release_date);
            rel_year.setText(release_year);
        }

        public void setImage(Context ctx, String Image){
            ImageView post_image = (ImageView)mView.findViewById(R.id.item_movie_poster);
            Picasso.get().load(Image).into(post_image);
        }
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.add_button){
            startActivity(new Intent(localMainActivity.this, localPostActivity.class));
        }
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}