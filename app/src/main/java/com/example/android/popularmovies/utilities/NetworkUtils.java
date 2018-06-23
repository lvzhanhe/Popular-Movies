package com.example.android.popularmovies.utilities;

import android.net.Uri;
import android.util.Log;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.content.Context;
import android.app.Activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import com.example.android.popularmovies.BuildConfig;

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String TMDB_BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String ID_QUERY_BASE_URL = "http://api.themoviedb.org/3/movie/";
    private static final String GET_REVIEWS_BASE_URL = "http://api.themoviedb.org/3/movie/";
    private static final String GET_VIDEOS_BASE_URL = "http://api.themoviedb.org/3/movie/";
    private static final String GET_YOUTUBE_BASE_URL = "https://www.youtube.com/watch";

    private static final String API_KEY = BuildConfig.API_KEY;

    final static String SORT_BY_PARAM = "sort_by";
    final static String API_KEY_PARAM = "api_key";
    final static String PAGE_PARAM = "page";

    public static URL main_buildUrl(String sortMethod, String page) {

        Uri builtUri = Uri.parse(TMDB_BASE_URL + sortMethod).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .appendQueryParameter(PAGE_PARAM, page)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static URL id_based_buildUrl(String id) {
        Uri builtUri = Uri.parse(ID_QUERY_BASE_URL + id).buildUpon()
                        .appendQueryParameter(API_KEY_PARAM, API_KEY)
                        .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static URL get_reviews_buildUrl(String id) {
        Uri builtUri = Uri.parse(GET_REVIEWS_BASE_URL + id + "/reviews").buildUpon()
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static URL get_videos_buildUrl(String id) {
        Uri builtUri = Uri.parse(GET_VIDEOS_BASE_URL + id + "/videos").buildUpon()
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
