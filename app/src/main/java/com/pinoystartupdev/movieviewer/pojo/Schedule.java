package com.pinoystartupdev.movieviewer.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Schedule {
    @SerializedName("dates")
    List<MovieScheduleDate> movieScheduleDateList;
}
