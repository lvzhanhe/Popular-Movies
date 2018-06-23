package com.example.android.popularmovies.utilities;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;
import org.json.JSONArray;

import java.util.ArrayList;
import com.example.android.popularmovies.Movie;

public class OpenMovieJsonUtils {
    public static ArrayList<String[]> getSimpleMovieStringsFromJson(Context context, String JsonStr) {

        final String VOTE_COUNT_LABEL = "vote_count";
        final String ID_LABEL = "id";
        final String VOTE_AVERAGE_LABEL = "vote_average";
        final String TITLE_LABEL = "title";
        final String POPULARITY_LABEL = "popularity";
        final String POSTER_PATH_LABEL = "poster_path";
        final String ORIGINAL_LANGUAGE_LABEL = "original_language";
        final String ORIGINAL_TITLE_LABEL = "original_title";
        final String GENERE_IDS_LABEL = "genre_ids";
        final String OVERVIEW_LABEL = "overview";
        final String RELEASE_DATE_LABEL = "release_date";

        final String POSTER_BASE_PATH = "https://image.tmdb.org/t/p/w500";

        try {
            JSONObject moviesJson = new JSONObject(JsonStr);
            JSONArray results = moviesJson.getJSONArray("results");
            ArrayList<String[]> result_list = new ArrayList<>();

            for (int i = 0; i < results.length(); i++) {
                JSONObject movie = results.getJSONObject(i);
                String vote_count = Integer.toString(movie.getInt(VOTE_COUNT_LABEL));
                String id = Integer.toString(movie.getInt(ID_LABEL));
                String vote_average = Double.toString(movie.getDouble(VOTE_AVERAGE_LABEL));
                String title = movie.getString(TITLE_LABEL);
                String popularity = Double.toString(movie.getDouble(POPULARITY_LABEL));
                String poster_path = POSTER_BASE_PATH + movie.getString(POSTER_PATH_LABEL);
                String original_language = movie.getString(ORIGINAL_LANGUAGE_LABEL);
                String original_title = movie.getString(ORIGINAL_TITLE_LABEL);
                String genre_ids = movie.getJSONArray(GENERE_IDS_LABEL).toString();
                String overview = movie.getString(OVERVIEW_LABEL);
                String release_date = movie.getString(RELEASE_DATE_LABEL);

                String[] result = {vote_count,id,vote_average,title,popularity,poster_path,original_language,
                        original_title, genre_ids,"",overview,release_date};
                result_list.add(result);
            }
            return result_list;

        } catch (Exception e) {
            return null;
        }
    }

    public static Movie getMovieDetailFromJson(Context context, String JsonStr) {
        Movie result = new Movie();
        final String POSTER_BASE_PATH = "https://image.tmdb.org/t/p/w500";

        try {
            JSONObject moviesJson = new JSONObject(JsonStr);

            JSONArray genres_array = moviesJson.getJSONArray("genres");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < genres_array.length()-1; i++){
                JSONObject genre = (JSONObject) genres_array.get(i);
                sb.append(genre.getString("name"));
                sb.append(", ");
            }

            JSONObject genre = (JSONObject) genres_array.get(genres_array.length()-1);
            sb.append(genre.getString("name"));
            String genres = sb.toString();

            String original_language = moviesJson.getString("original_language");
            String overview = moviesJson.getString("overview");
            Double popularity = moviesJson.getDouble("popularity");
            String title = moviesJson.getString("title");
            String release_date = moviesJson.getString("release_date");
            Double vote_average = moviesJson.getDouble("vote_average");
            String poster = POSTER_BASE_PATH + moviesJson.getString("poster_path");

            result.setGenres(genres);
            result.setOriginalLanguage(original_language);
            result.setOverview(overview);
            result.setPopularity(popularity);
            result.setTitle(title);
            result.setVoteAverage(vote_average);
            result.setReleaseDate(release_date);
            result.setPoster(poster);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return result;
    }

    public static ArrayList<String[]> getVideosFromJson(Context context, String JsonStr) {
        ArrayList<String[]> results = new ArrayList<>();
        try {
            JSONObject videosJson = new JSONObject(JsonStr);
            JSONArray videos_array = videosJson.getJSONArray("results");
            for (int i = 0; i < videos_array.length(); i++) {
                JSONObject entry = (JSONObject) videos_array.get(i);
                String name = entry.getString("name");
                String website = entry.getString("site");
                String key = entry.getString("key");
                String[] result = {name, key};
                if (website.equals("YouTube")) {
                    results.add(result);
                }
            }
        } catch (Exception e) {
            return null;
        }
        return results;
    }

    public static String getReviewFromJson(Context context, String JsonStr) {
        StringBuilder sb = new StringBuilder();
        try {
            JSONObject reviewJson = new JSONObject(JsonStr);
            JSONArray review_array = reviewJson.getJSONArray("results");
            for (int i = 0; i < review_array.length(); i++) {
                JSONObject entry = (JSONObject) review_array.get(i);
                sb.append("->REVIEWS BY " + entry.getString("author"));
                sb.append(":\n");
                sb.append(entry.getString("content"));
                sb.append("\n\n\n");
            }
        } catch (Exception e) {

        }
        return sb.toString();
    }
}
