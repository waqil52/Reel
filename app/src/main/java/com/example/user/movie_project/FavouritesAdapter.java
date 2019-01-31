package com.example.user.movie_project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.MyViewHolder>{

    private String IMG_BASE_URL = "http://image.tmdb.org/t/p/w500";

     List<FavouritesActivity> favour;
    private Context cnt;
    private List<Genre> allGenres;
    List<Movie> movies;
    TextView releaseDate;
    TextView title;
    TextView rating;
    TextView genres;
    ImageView poster;
    ImageView imageView;
    ImageView imageView1;
    TextView textView2;
    ImageButton delete;
    private String username;
    public int temp;
    public String fin;

    public FavouritesAdapter(List<Movie> movies,Context cnt,List<FavouritesActivity> favour,String favMovies) {

        this.movies = movies;
        this.cnt = cnt;
        this.favour=favour;
        this.username=favMovies;
//        this.allGenres = allGenres;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fav_movie, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouritesAdapter.MyViewHolder myViewHolder, int i) {
        Movie m = movies.get(i);
        final int ii = i;
//        myViewHolder.genres.setText(m.getId());
        myViewHolder.releaseDate.setText(m.getReleaseDate());
        myViewHolder.title.setText(m.getTitle());
        myViewHolder.rating.setText(String.valueOf(m.getRating()));
        myViewHolder.delete.setTag(i);
        //myViewHolder.genres.setText(String.valueOf(m.getGenreIds()));


        Picasso.get()
                .load(IMG_BASE_URL + m.getPosterPath())
                .into(myViewHolder.poster);

////       Movie m= movies.get(getAdapterPosition());

        myViewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Movie m = movies.get(ii);

                temp=m.getId();
                final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                database.child("fav").child(username).orderByChild("id").equalTo(temp).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        fin=dataSnapshot.getKey().toString();
                        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("fav").child(username).child(fin);
                        db.removeValue();
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
//                database.child("fav").child(username).child(fin).removeValue();





                Intent intent = ((Activity) cnt).getIntent();
                ((Activity)cnt).finish();
                ((Activity)cnt).startActivity(intent);
//                Toast.makeText(cnt,""+ii+"   "+temp,Toast.LENGTH_LONG).show();
            }
        });

    }



    @Override
    public int getItemCount() {
        return movies.size();}

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView releaseDate;
        TextView title;
        TextView rating;
        TextView genres;
        ImageView poster;
        ImageView imageView;
        ImageView imageView1;
        TextView textView2;
        ImageButton delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView2=itemView.findViewById(R.id.textView2);
            releaseDate = itemView.findViewById(R.id.item_movie_release_date);
            title = itemView.findViewById(R.id.item_movie_title);
            rating = itemView.findViewById(R.id.item_movie_rating);
            genres = itemView.findViewById(R.id.item_movie_genre);
            poster = itemView.findViewById(R.id.item_movie_poster);
            imageView= itemView.findViewById(R.id.imageView);
            imageView1= itemView.findViewById(R.id.imageView1);
            delete=itemView.findViewById(R.id.delete);

//            Movie m= movies.get(getAdapterPosition());
//            temp=m.getId();

        }
    }




}

