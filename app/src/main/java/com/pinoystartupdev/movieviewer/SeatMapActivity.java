package com.pinoystartupdev.movieviewer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import by.anatoldeveloper.hallscheme.hall.HallScheme;
import by.anatoldeveloper.hallscheme.hall.Seat;
import by.anatoldeveloper.hallscheme.hall.SeatListener;
import by.anatoldeveloper.hallscheme.view.ZoomableImageView;

public class SeatMapActivity extends AppCompatActivity {
    @BindView(R.id.imageView)
    ZoomableImageView imageView;

    public static Intent newInstance(Context context) {
        return new Intent(context, SeatMapActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_map);
        ButterKnife.bind(this);

        Seat seats[][] = basicScheme();

        if (seats[0][0] != null) {
            Log.e("asfdad", "seats IS NOT NULL");
        } else {
            Log.e("asfdad", "seats IS NULL");
        }


        if (imageView != null) {
            Log.e("asfdad", "imageView IS NOT NULL");
            HallScheme scheme = new HallScheme(imageView, seats, SeatMapActivity.this);

//            scheme.setSeatListener(new SeatListener() {
//
//                @Override
//                public void selectSeat(int id) {
//                    Toast.makeText(SeatMapActivity.this, "select seat " + id, Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void unSelectSeat(int id) {
//                    Toast.makeText(SeatMapActivity.this, "unSelect seat " + id, Toast.LENGTH_SHORT).show();
//                }
//
//            });
        } else {
            Log.e("asfdad", "imageView IS NULL");
        }


    }

    public Seat[][] basicScheme() {
        Seat seats[][] = new Seat[10][10];
        for (int i = 0; i < 10; i++)
            for(int j = 0; j < 10; j++) {
                SeatExample seat = new SeatExample();
                seat.id = i * 10 + (j+1);
                seat.selectedSeatMarker = String.valueOf(j+1);
                seat.status = HallScheme.SeatStatus.FREE;
                seats[i][j] = seat;
            }
        return seats;
    }

    public class SeatExample implements Seat {

        public int id;
        public int color = Color.RED;
        public String marker;
        public String selectedSeatMarker;
        public HallScheme.SeatStatus status;

        @Override
        public int id() {
            return id;
        }

        @Override
        public int color() {
            return color;
        }

        @Override
        public String marker() {
            return marker;
        }

        @Override
        public String selectedSeat() {
            return selectedSeatMarker;
        }

        @Override
        public HallScheme.SeatStatus status() {
            return status;
        }

        @Override
        public void setStatus(HallScheme.SeatStatus status) {
            this.status = status;
        }

    }
}
