package com.pinoystartupdev.movieviewer.pojo;

import com.google.gson.annotations.SerializedName;

public class MovieScheduleDate {
    @SerializedName("id") String id;
    @SerializedName("label") String label;
    @SerializedName("date") String date;

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public String getDate() {
        return date;
    }
}
