package com.pinoystartupdev.movieviewer.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieScheduleTimes {
    @SerializedName("parent") String parent;
    @SerializedName("times")
    List<MovieScheduleTimesDetails> movieScheduleTimesDetailsList;

    public String getParent() {
        return parent;
    }

    public List<MovieScheduleTimesDetails> getMovieScheduleTimesDetailsList() {
        return movieScheduleTimesDetailsList;
    }
}
