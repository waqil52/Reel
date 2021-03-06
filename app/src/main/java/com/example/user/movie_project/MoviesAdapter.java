package com.example.user.movie_project;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private String IMG_BASE_URL = "http://image.tmdb.org/t/p/w500";
    private List<Movie> movies;
    private List<Genre> allGenres;
    private String username;
    private OnMoviesClickCallback callback;
    private String key;

    public MoviesAdapter(List<Movie> movies, List<Genre> allGenres,OnMoviesClickCallback callback,String favMovies) {
        this.callback = callback;
        this.movies = movies;
        this.allGenres = allGenres;
        this.username = favMovies;
    }


    public void clearMovies() {
        movies.clear();
        notifyDataSetChanged();
    }

    public void appendMovies(List<Movie> moviesToAppend) {
        movies.addAll(moviesToAppend);
        notifyDataSetChanged();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bind(movies.get(position));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView releaseDate;
        TextView title;
        TextView rating;
        TextView genres;
        ImageView poster;
        ImageView imageView;
        ImageView imageView1;
        Movie movie;

        TextView textView2;

        public MovieViewHolder(View itemView) {
            super(itemView);
            textView2=itemView.findViewById(R.id.textView2);
            releaseDate = itemView.findViewById(R.id.item_movie_release_date);
            title = itemView.findViewById(R.id.item_movie_title);
            rating = itemView.findViewById(R.id.item_movie_rating);
            genres = itemView.findViewById(R.id.item_movie_genre);
            poster = itemView.findViewById(R.id.item_movie_poster);
            imageView= itemView.findViewById(R.id.imageView);
            imageView1= itemView.findViewById(R.id.imageView1);


            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // callback.onClick(movie);
                        imageView1.setVisibility(View.VISIBLE);
                        imageView.setVisibility(View.GONE);
                        Movie m = movies.get(getAdapterPosition());
                        DatabaseReference def = FirebaseDatabase.getInstance().getReference();

                        String key=def.child("fav").child(username).push().getKey();
                        def.child("fav").child(username).child(key).setValue(m);



                }
            });

            imageView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageView.setVisibility(View.VISIBLE);
                    imageView1.setVisibility(View.GONE);
                }
            });

            textView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onClick(movie);
                }
            });
            releaseDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onClick(movie);
                }
            });
            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onClick(movie);
                }
            });
            rating.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onClick(movie);
                }
            });
            genres.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onClick(movie);
                }
            });
            poster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onClick(movie);
                }
            });

        }


        public void bind(Movie movie) {
            this.movie = movie;
            releaseDate.setText(movie.getReleaseDate().split("-")[0]);
            title.setText(movie.getTitle());
            rating.setText(String.valueOf(movie.getRating()));
            genres.setText(getGenres(movie.getGenreIds()));
            imageView.setVisibility(View.VISIBLE);
            imageView1.setVisibility(View.GONE);

            Glide.with(itemView)
                    .load(IMG_BASE_URL + movie.getPosterPath())
                    .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                    .into(poster);
        }

        public String getGenres(List<Integer> genreIds) {
            List<String> movieGenres = new ArrayList<>();
            for (Integer genreId : genreIds) {
                for (Genre genre : allGenres) {
                    if (genre.getId() == genreId) {
                        movieGenres.add(genre.getName());
                        break;
                    }
                }
            }
            return TextUtils.join(", ", movieGenres);
        }
    }
    }