package com.example.android.popularmovies.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Index;

@Entity(tableName = "movie", indices = @Index(value = {"movie_id"}, unique = true))
public class MovieEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int movie_id;
    private String movie_title;
    private String movie_poster;
    private String movie_release_date;

    @Ignore
    public MovieEntry(int id, int movie_id, String movie_title, String movie_poster, String movie_release_date) {
        this.id = id;
        this.movie_id = movie_id;
        this.movie_title = movie_title;
        this.movie_poster = movie_poster;
        this.movie_release_date = movie_release_date;
    }

    public MovieEntry(int movie_id, String movie_title, String movie_poster, String movie_release_date) {
        this.movie_id = movie_id;
        this.movie_title = movie_title;
        this.movie_poster = movie_poster;
        this.movie_release_date = movie_release_date;
    }

    public int getId() {
        return this.id;
    }

    public int getMovie_id() {
        return this.movie_id;
    }

    public String getMovie_title() {
        return this.movie_title;
    }

    public String getMovie_poster() {
        return this.movie_poster;
    }

    public String getMovie_release_date() {
        return movie_release_date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMovie_id(int Movie_id) {
        this.movie_id = Movie_id;
    }

    public void setMovie_title(String Movie_title) {
        this.movie_title = Movie_title;
    }

    public void setMovie_poster(String Movie_poster) {
        this.movie_poster = Movie_poster;
    }

    public void setMovie_release_date(String Movie_release_date) {
        this.movie_release_date = Movie_release_date;
    }


}
