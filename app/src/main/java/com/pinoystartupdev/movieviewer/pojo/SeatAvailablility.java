package com.pinoystartupdev.movieviewer.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SeatAvailablility {
    @SerializedName("seats")
    List<String> availableSeatsList;
    @SerializedName("seat_count") int seatCount;

    public int getSeatCount() {
        return seatCount;
    }

    public List<String> getAvailableSeatsList() {
        return availableSeatsList;
    }
}
