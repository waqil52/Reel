package com.example.user.movie_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class BlogActivity extends AppCompatActivity {
    private RecyclerView mBlogList;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);
        Toolbar toolbar = findViewById(R.id.toolbarblog);
        setSupportActionBar(toolbar);

        mDatabase=FirebaseDatabase.getInstance().getReference().child("blog");

        mBlogList=(RecyclerView) findViewById(R.id.recyclerviewblog);
        //mBlogList.setHasFixedSize(true);
        mBlogList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Blog")
                .limitToLast(50);
        FirebaseRecyclerOptions<Blog> options =
                new FirebaseRecyclerOptions.Builder<Blog>()
                        .setQuery(query, Blog.class)
                        .build();

        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(options) {
            @Override
            public BlogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.blog_row, parent, false);

                return new BlogViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(BlogViewHolder holder, int position, Blog model) {
                holder.setTitle(model.getTitle());
                holder.setDesc(model.getDescription());

            }
        };
        adapter.startListening();
        mBlogList.setAdapter(adapter);

    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public BlogViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
        }

        public void setTitle(String title){
            TextView post_title= (TextView) mView.findViewById(R.id.posttitle);
            post_title.setText(title);
        }

        public void setDesc(String description){
            TextView post_desc= (TextView) mView.findViewById(R.id.postdescr);
            post_desc.setText(description);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.blogmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_add){
            startActivity(new Intent(BlogActivity.this,PostActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
