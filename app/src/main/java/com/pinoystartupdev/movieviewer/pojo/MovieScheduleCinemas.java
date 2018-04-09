package com.pinoystartupdev.movieviewer.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieScheduleCinemas {
    @SerializedName("parent") String parent;
    @SerializedName("cinemas")
    List<MovieScheduleCinemaDetails> movieScheduleCinemaDetailsList;

    public String getParent() {
        return parent;
    }

    public List<MovieScheduleCinemaDetails> getMovieScheduleCinemaDetailsList() {
        return movieScheduleCinemaDetailsList;
    }
}
