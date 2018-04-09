package com.pinoystartupdev.movieviewer.pojo;

import com.google.gson.annotations.SerializedName;

public class MovieScheduleTimesDetails {
    @SerializedName("id") String id;
    @SerializedName("label") String label;
    @SerializedName("schedule_id") String scheduleId;
    @SerializedName("popcorn_price") String popcornPrice;
    @SerializedName("popcorb_label") String popcornLabel;
    @SerializedName("seating_type") String reserevedSeating;
    @SerializedName("price") String price;
    @SerializedName("variant") String variant;

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public String getPopcornPrice() {
        return popcornPrice;
    }

    public String getPopcornLabel() {
        return popcornLabel;
    }

    public String getReserevedSeating() {
        return reserevedSeating;
    }

    public String getPrice() {
        return price;
    }

    public String getVariant() {
        return variant;
    }
}
