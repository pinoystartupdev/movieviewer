package com.pinoystartupdev.movieviewer.pojo;

import com.google.gson.annotations.SerializedName;

public class Movie {
    @SerializedName("movie_id") String movieId;
    @SerializedName("canonical_title") String canonicalTitle;
    @SerializedName("genre") String genre;
    @SerializedName("advisory_rating") String advisoryRating;
    @SerializedName("runtime_mins") String runtimeMins;
    @SerializedName("release_date") String releaseDate;
    @SerializedName("synopsis") String synopsis;
    @SerializedName("poster") String poster;
    @SerializedName("poster_landscape") String posterLandscape;

    public String getMovieId() {
        return movieId;
    }

    public String getCanonicalTitle() {
        return canonicalTitle;
    }

    public String getGenre() {
        return genre;
    }

    public String getAdvisoryRating() {
        return advisoryRating;
    }

    public String getRuntimeMins() {
        return runtimeMins;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public String getPoster() {
        return poster;
    }

    public String getPosterLandscape() {
        return posterLandscape;
    }
}
