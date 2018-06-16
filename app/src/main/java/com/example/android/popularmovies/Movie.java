package com.example.android.popularmovies;

public class Movie {
    private boolean adult;
    private String backdrop_path;
    private int budget;
    private String genres;
    private String homepage;
    private String original_language;
    private String original_title;
    private String overview;
    private Double popularity;
    private String poster_path;
    private String[][] production_companies;
    private String production_countries;
    private String release_date;
    private int revenue;
    private String status;
    private String title;
    private Double vote_average;
    private int vote_count;

    public void setPoster(String poster) {
        this.poster_path = poster;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setReleaseDate(String release_date) {
        this.release_date = release_date;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public void setVoteAverage(Double vote_average) {
        this.vote_average = vote_average;
    }

    public void setOriginalLanguage(String original_language) {
        this.original_language = original_language;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPoster() {
        return this.poster_path;
    }

    public String getTitle() {
        return this.title;
    }

    public String getReleaseDate() {
        return this.release_date;
    }

    public Double getPopularity() {
        return this.popularity;
    }

    public Double getVoteAverage() {
        return vote_average;
    }

    public String getOriginalLanguage() {
        return original_language;
    }

    public String getGenres() {
        return this.genres;
    }

    public String getOverview() {
        return this.overview;
    }

}
