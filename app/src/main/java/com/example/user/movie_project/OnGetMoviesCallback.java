package com.example.user.movie_project;

import java.util.List;

public interface OnGetMoviesCallback {

    void onSuccess(int page,List<Movie> movies);

    void onError();
}