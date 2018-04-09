package com.pinoystartupdev.movieviewer.network;

import com.pinoystartupdev.movieviewer.pojo.SeatMap;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MovieViewerNetworkInterface {
    @GET("/seatmap.json")
    Call<SeatMap> doGetListResources();
}
