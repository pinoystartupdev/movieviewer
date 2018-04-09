package com.pinoystartupdev.movieviewer.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SeatMap {
    @SerializedName("seatmap") List<List<String>> seatPlacementList;
    @SerializedName("available") SeatAvailablility seatAvailablility;

    public SeatAvailablility getSeatAvailablility() {
        return seatAvailablility;
    }

    public List<List<String>> getSeatPlacementList() {
        return seatPlacementList;
    }
}
