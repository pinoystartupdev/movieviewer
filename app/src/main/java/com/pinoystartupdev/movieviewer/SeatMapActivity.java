package com.pinoystartupdev.movieviewer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
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
            scheme.setChosenSeatBackgroundColor(Color.RED);
            scheme.setChosenSeatTextColor(Color.WHITE);

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
        Seat seats[][] = new Seat[5][10];
        for (int i = 0; i < 5; i++)
            for(int j = 0; j < 10; j++) {
                SeatExample seat = new SeatExample();
                seat.id = i * 10 + (j+1);
                seat.selectedSeatMarker = "\u2713";
                seat.status = HallScheme.SeatStatus.FREE;
                seats[i][j] = seat;
            }
        return seats;
    }

    public Seat[][] basicSchemeWithMarker() {
        Seat seats[][] = new Seat[12][18];
        int k = 0;
        for (int i = 0; i < 12; i++)
            for(int j = 0; j < 18; j++) {
                SeatExample seat = new SeatExample();
                seat.id = ++k;
                seat.selectedSeatMarker = String.valueOf(i+1);
                seat.status = HallScheme.SeatStatus.BUSY;
                if (j == 0 || j == 17) {
                    seat.status = HallScheme.SeatStatus.EMPTY;
                    if (i > 2 && i < 10) {
                        seat.marker = String.valueOf(i);
                        seat.status = HallScheme.SeatStatus.INFO;
                    }
                }
                if (((j > 0 && j < 3) || (j > 14 && j < 17)) && i == 0) {
                    seat.status = HallScheme.SeatStatus.EMPTY;
                    if (j == 2 || j == 15) {
                        seat.marker = String.valueOf(i+1);
                        seat.status = HallScheme.SeatStatus.INFO;
                    }
                }
                if (((j > 0 && j < 2) || (j > 15 && j < 17)) && i == 1) {
                    seat.status = HallScheme.SeatStatus.EMPTY;
                    if (j == 1 || j == 16) {
                        seat.marker = String.valueOf(i+1);
                        seat.status = HallScheme.SeatStatus.INFO;
                    }
                }
                if (i == 2)
                    seat.status = HallScheme.SeatStatus.EMPTY;
                if (i > 9 && (j == 1 || j == 16)) {
                    seat.status = HallScheme.SeatStatus.INFO;
                    seat.marker = String.valueOf(i);
                }
                seats[i][j] = seat;
            }
        return seats;
    }

    public class SeatExample implements Seat {

        public int id;
        public int color = Color.GRAY;
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
