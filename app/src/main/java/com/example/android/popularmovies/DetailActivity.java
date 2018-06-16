package com.example.android.popularmovies;

import android.content.Intent;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.utilities.NetworkUtils;
import com.example.android.popularmovies.utilities.OpenMovieJsonUtils;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


public class DetailActivity extends AppCompatActivity{

    private ImageView poster;
    private TextView title;
    private TextView release_date;
    private TextView popularity;
    private TextView vote_average;
    private TextView language;
    private TextView genre;
    private TextView overview;

    private String Movie_ID;
    private Movie mMovie;

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

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
                Movie_ID = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
            }
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                URL weatherRequestUrl = NetworkUtils.id_based_buildUrl(Movie_ID);
                String jsonMovieResponse = null;
                try {
                    jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl);
                    mMovie = OpenMovieJsonUtils
                            .getMovieDetailFromJson(DetailActivity.this, jsonMovieResponse);
                } catch (IOException e) {
                    e.printStackTrace();
                    mMovie = null;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        title.setText(mMovie.getTitle());
                        release_date.setText(mMovie.getReleaseDate());
                        popularity.setText(mMovie.getPopularity().toString());
                        vote_average.setText(mMovie.getVoteAverage().toString());
                        language.setText(mMovie.getOriginalLanguage());
                        genre.setText(mMovie.getGenres());
                        overview.setText("    " + mMovie.getOverview());
                        Picasso.get()
                                .load(mMovie.getPoster())
                                .into(poster);

                    }
                });

            }
        });
        thread.start();
    }

}
