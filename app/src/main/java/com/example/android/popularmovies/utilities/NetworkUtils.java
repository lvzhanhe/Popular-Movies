package com.example.android.popularmovies.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String TMDB_BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String ID_QUERY_BASE_URL = "http://api.themoviedb.org/3/movie/";

    private static final String API_KEY = "265d1f71e67dd885bfb15ca37aa5e88d";

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
