package com.pinoystartupdev.movieviewer.pojo;

import com.google.gson.annotations.SerializedName;

public class MovieScheduleCinemaDetails {
    @SerializedName("id") String id;
    @SerializedName("cinema_id") String cinemaId;
    @SerializedName("label") String label;

    public String getId() {
        return id;
    }

    public String getCinemaId() {
        return cinemaId;
    }

    public String getLabel() {
        return label;
    }
}
