package com.example.android.popularmovies;

import android.content.res.Configuration;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.example.android.popularmovies.database.AppDatabase;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.example.android.popularmovies.utilities.OpenMovieJsonUtils;
import com.example.android.popularmovies.database.MovieEntry;

public class MainActivity extends AppCompatActivity implements
        MovieAdapter.MovieAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<ArrayList<String[]>> {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;

    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    private static final int MOVIE_LOADER_ID = 0;
    private static final String DEFAULT_SORT_METHOD = "popular";

    private AppDatabase mDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movie);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        mDb = AppDatabase.getInstance(getApplicationContext());

        int spanCount = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 4 : 2;
        StaggeredGridLayoutManager layoutManager
                = new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);

        int loaderId = MOVIE_LOADER_ID;
        LoaderManager.LoaderCallbacks<ArrayList<String[]>> callback = MainActivity.this;
        Bundle bundleForLoader = new Bundle();
        bundleForLoader.putString("sort_by", DEFAULT_SORT_METHOD);
        getSupportLoaderManager().initLoader(loaderId, bundleForLoader, callback);
    }

    @Override
    public Loader<ArrayList<String[]>> onCreateLoader(int id, final Bundle args) {

        return new AsyncTaskLoader<ArrayList<String[]>>(this) {

            ArrayList<String[]> mMovieData = null;
            String sort_method = args.getString("sort_by",DEFAULT_SORT_METHOD);

            @Override
            protected void onStartLoading() {
                if (mMovieData != null) {
                    deliverResult(mMovieData);
                } else {
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public ArrayList<String[]> loadInBackground() {
                ArrayList<String[]> finalJsonMovieData = new ArrayList<String[]>();

                try {
                    for (int i = 1; i < 6; i++) {
                        URL weatherRequestUrl = NetworkUtils.main_buildUrl(sort_method, Integer.toString(i));
                        String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl);
                        ArrayList<String[]> simpleJsonMovieData = OpenMovieJsonUtils
                                .getSimpleMovieStringsFromJson(MainActivity.this, jsonMovieResponse);
                        finalJsonMovieData.addAll(simpleJsonMovieData);
                    }
                    return finalJsonMovieData;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            /**
             * Sends the result of the load to the registered listener.
             *
             * @param data The result of the load
             */
            public void deliverResult(ArrayList<String[]> data) {
                mMovieData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<String[]>> loader, ArrayList<String[]> data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mMovieAdapter.setMovieData(data);
        if (data == null) {
            showErrorMessage();
        } else {
            showMovieDataView();
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<String[]>> loader) {

    }

    @Override
    public void onClick(String[] Movie_ID) {
        Intent intentToStartDetailActivity = new Intent(this, DetailActivity.class);
        intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, Movie_ID[1]);
        startActivity(intentToStartDetailActivity);
    }

    private void showMovieDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movies, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sort_popular) {
            loadPagePopular();
            return true;
        }

        if (id == R.id.action_sort_rate) {
            loadPageRate();
            return true;
        }

        if (id == R.id.action_my_favorite) {
            loadPageFavorite();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void loadPagePopular() {
        mMovieAdapter.setMovieData(null);
        Bundle bundleForLoader = new Bundle();
        bundleForLoader.putString("sort_by", DEFAULT_SORT_METHOD);
        getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, bundleForLoader, this);
    }

    public void loadPageRate() {
        mMovieAdapter.setMovieData(null);
        Bundle bundleForLoader = new Bundle();
        bundleForLoader.putString("sort_by", "top_rated");
        getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, bundleForLoader, this);
    }

    public void loadPageFavorite() {
        mMovieAdapter.setMovieData(null);
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getMovies().observe(this, new Observer<List<MovieEntry>>() {
            @Override
            public void onChanged(@Nullable List<MovieEntry> movieEntries) {
                Log.d(TAG, "Receiving database update from LiveData");
                ArrayList<String[]> favorite_data = new ArrayList<>();
                for (int i = 0; i < movieEntries.size(); i++) {
                    MovieEntry preFavorite = movieEntries.get(i);
                    String[] favorite = {"",Integer.toString(preFavorite.getMovie_id()),"",preFavorite.getMovie_title(),"",
                            preFavorite.getMovie_poster(), "","","","","",preFavorite.getMovie_release_date()};
                    favorite_data.add(favorite);
                }
                mMovieAdapter.setMovieData(favorite_data);
            }
        });
        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }
}
