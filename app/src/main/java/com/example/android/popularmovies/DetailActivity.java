package com.example.android.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.design.widget.FloatingActionButton;
import android.widget.Toast;
import android.util.Log;

import com.example.android.popularmovies.database.AppDatabase;
import com.example.android.popularmovies.database.MovieEntry;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.example.android.popularmovies.utilities.OpenMovieJsonUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


public class DetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerAdapterOnClickHandler{

    private ImageView poster;
    private TextView title;
    private TextView release_date;
    private TextView popularity;
    private TextView vote_average;
    private TextView language;
    private TextView genre;
    private TextView overview;
    private TextView review;

    private String Movie_ID;
    private Movie mMovie;

    private AppDatabase mDb;

    private RecyclerView mTrailerRecyclerView;
    private TrailerAdapter mTrailerAdapter;
    private ArrayList<String[]> simpleJsonVideosData;
    private String simpleJsonReviewData;

    private static String ADD_SUCCESS_TOAST = "Movie added to your favorite list~";
    private static String ADD_FAIL_TOAST = "Movie deleted from your favorite list~";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        poster = (ImageView) findViewById(R.id.posterImageView);
        title = (TextView) findViewById(R.id.idTextView);
        release_date = (TextView) findViewById(R.id.releaseDateTextView);
        popularity = (TextView) findViewById(R.id.popularityTextView);
        vote_average = (TextView) findViewById(R.id.voteAverageTextView);
        language = (TextView) findViewById(R.id.languageTextView);
        genre = (TextView) findViewById(R.id.genreTextView);
        overview = (TextView) findViewById(R.id.DetailTextView);
        review = (TextView) findViewById(R.id.review_TextView);

        mDb = AppDatabase.getInstance(getApplicationContext());

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
                Movie_ID = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
            }
        }

        mTrailerRecyclerView = (RecyclerView) findViewById(R.id.trailerRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mTrailerRecyclerView.setLayoutManager(layoutManager);
        mTrailerAdapter = new TrailerAdapter(this);
        mTrailerRecyclerView.setAdapter(mTrailerAdapter);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                URL weatherRequestUrl = NetworkUtils.id_based_buildUrl(Movie_ID);
                String jsonMovieResponse = null;
                try {
                    jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl);
                    mMovie = OpenMovieJsonUtils
                            .getMovieDetailFromJson(DetailActivity.this, jsonMovieResponse);

                    URL video_url = NetworkUtils.get_videos_buildUrl(Movie_ID);
                    String jsonVideoResponse = NetworkUtils.getResponseFromHttpUrl(video_url);
                    simpleJsonVideosData = OpenMovieJsonUtils
                            .getVideosFromJson(DetailActivity.this, jsonVideoResponse);

                    URL review_url = NetworkUtils.get_reviews_buildUrl(Movie_ID);
                    String jsonReviewResponse = NetworkUtils.getResponseFromHttpUrl(review_url);
                    simpleJsonReviewData = OpenMovieJsonUtils
                            .getReviewFromJson(DetailActivity.this, jsonReviewResponse);

                } catch (IOException e) {
                    e.printStackTrace();
                    mMovie = null;
                    mTrailerAdapter.setTrailerData(null);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        title.setText(mMovie.getTitle());
                        release_date.setText(mMovie.getReleaseDate());
                        popularity.setText(mMovie.getPopularity().toString());
                        vote_average.setText(mMovie.getVoteAverage().toString()+"/10");
                        language.setText(mMovie.getOriginalLanguage());
                        genre.setText(mMovie.getGenres());
                        overview.setText("    " + mMovie.getOverview());
                        Picasso.get()
                                .load(mMovie.getPoster())
                                .into(poster);
                        mTrailerAdapter.setTrailerData(simpleJsonVideosData);
                        review.setText(simpleJsonReviewData);
                    }
                });

            }
        });

        FloatingActionButton fabButton = findViewById(R.id.fab);
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_favorite(Movie_ID);
            }
        });
    }

    protected void add_favorite(final String add_movie_id) {

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            public void run() {
                String title_content = mMovie.getTitle();
                String release_date_content = mMovie.getReleaseDate();
                String poster_content = mMovie.getPoster();
                String movie_id_content = add_movie_id;

                MovieEntry movieEntry = new MovieEntry(Integer.parseInt(movie_id_content),title_content,
                        poster_content,release_date_content);
                try {
                    // if the movie does not exist in the database.
                    if (mDb.movieDao().loadMovieById(Integer.parseInt(movie_id_content)) == null) {
                        Log.v(".","the movie does not exist in the database");
                        mDb.movieDao().insertMovie(movieEntry);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), ADD_SUCCESS_TOAST,
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                    // if the movie exists in the database.
                        Log.v(".","the movie exists in the database");
                        mDb.movieDao().deleteMovie(Integer.parseInt(movie_id_content));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), ADD_FAIL_TOAST,
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (Exception e) {
                    return;
                }
            }
        });
    }

    @Override
    public void onClick(String video_key) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + video_key));
        intent.putExtra("VIDEO_ID", video_key);
        startActivity(intent);

    }
}
