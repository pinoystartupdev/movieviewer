package com.pinoystartupdev.movieviewer.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieSchedule {
    @SerializedName("dates")
    List<MovieScheduleDate> movieScheduleDateList;
    @SerializedName("cinemas")
    List<MovieScheduleCinemas> movieScheduleCinemasList;
    @SerializedName("times")
    List<MovieScheduleTimes> movieScheduleTimesList;

    public List<MovieScheduleDate> getMovieScheduleDateList() {
        return movieScheduleDateList;
    }

    public List<MovieScheduleCinemas> getMovieScheduleCinemasList() {
        return movieScheduleCinemasList;
    }

    public List<MovieScheduleTimes> getMovieScheduleTimesList() {
        return movieScheduleTimesList;
    }
}
