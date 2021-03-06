package com.example.user.movie_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String sortBy = MoviesRepository.POPULAR;
    private RecyclerView moviesList;
    private MoviesAdapter adapter;

    private MoviesRepository moviesRepository;

    private List<Genre> movieGenres;
    private ArrayList<Movie> favMovie = new ArrayList<>();
    private DrawerLayout mDrawerLayout;


    private boolean isFetchingMovies;
    private int currentPage = 1;
    private FirebaseAuth firebaseAuth;


    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_drawer);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

         username = user.getEmail();
         username = username.substring(0,username.length()-4);


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        switch (menuItem.getItemId()) {

                            case R.id.movie: {
                                //Toast.makeText(MainActivity.this,"iiii",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                startActivity(intent);

                                break;
                            }
                            case R.id.profile: {
                                //Toast.makeText(MainActivity.this,"iiii",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, Profile_Activity.class);
                                startActivity(intent);

                                break;
                            }

                            case R.id.cinemanearme: {
                                //Toast.makeText(MainActivity.this,"iiii",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                                startActivity(intent);

                                break;
                            }

                            case R.id.localmovie: {
                                //Toast.makeText(MainActivity.this,"iiii",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, localMainActivity.class);
                                startActivity(intent);

                                break;
                            }

                            case R.id.forum: {
                                //Toast.makeText(MainActivity.this,"iiii",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, BlogActivity.class);
                                startActivity(intent);

                                break;
                            }
                            case R.id.favourites: {
                                //Toast.makeText(MainActivity.this,"iiii",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, FavouritesActivity.class);
                                startActivity(intent);

                                break;
                            }
                        }
                        //close navigation drawer
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    }
                });



        moviesRepository = MoviesRepository.getInstance();

        moviesList = findViewById(R.id.movies_list);
        moviesList.setLayoutManager(new LinearLayoutManager(this));

        setupOnScrollListener();

        getGenres();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movies, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort:
                showSortMenu();
                return true;
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void showSortMenu() {
        PopupMenu sortMenu = new PopupMenu(this, findViewById(R.id.sort));
        sortMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                currentPage = 1;

                switch (item.getItemId()) {
                    case R.id.popular:
                        sortBy = MoviesRepository.POPULAR;
                        getMovies(currentPage);
                        return true;
                    case R.id.top_rated:
                        sortBy = MoviesRepository.TOP_RATED;
                        getMovies(currentPage);
                        return true;
                    case R.id.upcoming:
                        sortBy = MoviesRepository.UPCOMING;
                        getMovies(currentPage);
                        return true;

                    case R.id.now_playing:
                        sortBy = MoviesRepository.NOW_PLAYING;
                        getMovies(currentPage);
                        return true;
                    default:
                        return false;
                }
            }
        });
        sortMenu.inflate(R.menu.menu_movies_sort);
        sortMenu.show();
    }


    private void setupOnScrollListener() {
        final LinearLayoutManager manager = new LinearLayoutManager(this);
        moviesList.setLayoutManager(manager);
        moviesList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int totalItemCount = manager.getItemCount();
                int visibleItemCount = manager.getChildCount();
                int firstVisibleItem = manager.findFirstVisibleItemPosition();

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    if (!isFetchingMovies) {
                        getMovies(currentPage + 1);
                    }
                }
            }
        });
    }

    private void getGenres() {
                    moviesRepository.getGenres(new OnGetGenresCallback() {
                        @Override
                        public void onSuccess(List<Genre> genres) {
                            movieGenres = genres;
                            getMovies(currentPage);
                        }

                        @Override
                        public void onError() {
                            showError();
                        }
                    });
                }

                private void getMovies(int page) {
                    isFetchingMovies = true;
                    moviesRepository.getMovies(page, sortBy, new OnGetMoviesCallback() {
                        @Override
                        public void onSuccess(int page, List<Movie> movies) {
                            if (adapter == null) {
                                adapter = new MoviesAdapter(movies, movieGenres,callback,username);
                                moviesList.setAdapter(adapter);
                            } else {
                                if (page == 1) {
                                    adapter.clearMovies();
                                }
                                adapter.appendMovies(movies);
                            }
                            currentPage = page;
                isFetchingMovies = false;
                setTitle();
            }

            @Override
            public void onError() {
                showError();
            }
        });
    }
    OnMoviesClickCallback callback = new OnMoviesClickCallback() {
        @Override
        public void onClick(Movie movie) {
            Intent intent = new Intent(MainActivity.this, MovieActivity.class);
            intent.putExtra(MovieActivity.MOVIE_ID, movie.getId());
            startActivity(intent);
        }
    };

    private void setTitle() {
        switch (sortBy) {
            case MoviesRepository.POPULAR:
                setTitle(getString(R.string.popular));
                break;
            case MoviesRepository.TOP_RATED:
                setTitle(getString(R.string.top_rated));
                break;
            case MoviesRepository.UPCOMING:
                setTitle(getString(R.string.upcoming));
                break;

            case MoviesRepository.NOW_PLAYING:
                setTitle(getString(R.string.now_playing));
                break;

        }
    }

    private void showError() {
        Toast.makeText(MainActivity.this, "Please Check Your Internet Connection.", Toast.LENGTH_SHORT).show();
    }
}
