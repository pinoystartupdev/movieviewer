package com.pinoystartupdev.movieviewer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.pinoystartupdev.movieviewer.network.APIClient;
import com.pinoystartupdev.movieviewer.network.MovieViewerNetworkInterface;
import com.pinoystartupdev.movieviewer.pojo.MovieScheduleCinemaDetails;
import com.pinoystartupdev.movieviewer.pojo.MovieScheduleCinemas;
import com.pinoystartupdev.movieviewer.pojo.MovieScheduleDate;
import com.pinoystartupdev.movieviewer.pojo.MovieSchedule;
import com.pinoystartupdev.movieviewer.pojo.MovieScheduleTimes;
import com.pinoystartupdev.movieviewer.pojo.MovieScheduleTimesDetails;
import com.pinoystartupdev.movieviewer.pojo.SeatMap;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import by.anatoldeveloper.hallscheme.hall.HallScheme;
import by.anatoldeveloper.hallscheme.hall.Seat;
import by.anatoldeveloper.hallscheme.view.ZoomableImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SeatMapActivity extends AppCompatActivity {
    @BindView(R.id.spinnerMovieDates)
    Spinner spinnerMovieDates;

    @BindView(R.id.spinnerMovieCinemas)
    Spinner spinnerMovieCinemas;

    @BindView(R.id.spinnerMovieTimes)
    Spinner spinnerMovieTimes;

    List<MovieScheduleDate>  movieScheduleDateList;
    List<MovieScheduleCinemas> movieScheduleCinemasList;
    List<MovieScheduleTimes>  movieScheduleTimesList;
    MovieScheduleCinemas movieScheduleCinemas;
    MovieScheduleTimes movieScheduleTimes;

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

        movieScheduleDateList = new ArrayList<>();
        movieScheduleCinemasList = new ArrayList<>();
        movieScheduleTimesList = new ArrayList<>();

        MovieViewerNetworkInterface movieViewerNetworkInterface = APIClient.getClient().create(MovieViewerNetworkInterface.class);

        Call<MovieSchedule> movieScheduleCall = movieViewerNetworkInterface.getMovieSchedule();
        movieScheduleCall.enqueue(new Callback<MovieSchedule>() {
            @Override
            public void onResponse(Call<MovieSchedule> call, Response<MovieSchedule> response) {
                MovieSchedule movieSchedule = response.body();

                /*set DATE DROP DOWN*/

                List<String> movieScheduleDateLabelList = new ArrayList<>();

                movieScheduleDateList.addAll(movieSchedule.getMovieScheduleDateList());
                movieScheduleCinemasList.addAll(movieSchedule.getMovieScheduleCinemasList());
                movieScheduleTimesList.addAll(movieSchedule.getMovieScheduleTimesList());

                for (MovieScheduleDate movieScheduleDate : movieScheduleDateList) {
                    movieScheduleDateLabelList.add(movieScheduleDate.getLabel());
                }

                ArrayAdapter<String> movieScheduleDateArrayAdapter = new ArrayAdapter<String>(SeatMapActivity.this, android.R.layout.simple_spinner_item, movieScheduleDateLabelList);

                movieScheduleDateArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerMovieDates.setAdapter(movieScheduleDateArrayAdapter);
                spinnerMovieDates.setOnItemSelectedListener(new MySpinnerMovieDateOnItemSelectedListener());

                /*set CINEMA DROP DOWN*/

                List<String> movieScheduleCinemaDetailsLabelList = new ArrayList<>();

                for (MovieScheduleCinemas movieScheduleCinemas : movieScheduleCinemasList) {
                    if (movieScheduleDateList.get(0).getId().equals(movieScheduleCinemas.getParent())) {
                        SeatMapActivity.this.movieScheduleCinemas = movieScheduleCinemas;

                        for (MovieScheduleCinemaDetails movieScheduleCinemaDetails : SeatMapActivity.this.movieScheduleCinemas.getMovieScheduleCinemaDetailsList()) {
                            movieScheduleCinemaDetailsLabelList.add(movieScheduleCinemaDetails.getLabel());
                        }

                        break;
                    }
                }

                ArrayAdapter<String> movieScheduleCinemaDetailsArrayAdapter = new ArrayAdapter<String>(SeatMapActivity.this, android.R.layout.simple_spinner_item, movieScheduleCinemaDetailsLabelList);

                movieScheduleCinemaDetailsArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerMovieCinemas.setAdapter(movieScheduleCinemaDetailsArrayAdapter);

                spinnerMovieCinemas.setOnItemSelectedListener(new MySpinnerMovieCinemasOnItemSelectedListener());

                /*set TIME DROP DOWN*/

                List<String> movieScheduleTimeDetailsLabelList = new ArrayList<>();

                for (MovieScheduleTimes movieScheduleTimes : movieScheduleTimesList) {
                    for (MovieScheduleCinemas movieScheduleCinemas : movieScheduleCinemasList) {
                        if (movieScheduleDateList.get(0).getId().equals(movieScheduleCinemas.getParent())) {
                            for (MovieScheduleCinemaDetails movieScheduleCinemaDetails : movieScheduleCinemas.getMovieScheduleCinemaDetailsList()) {
                                if (movieScheduleCinemaDetails.getId().equals(movieScheduleTimes.getParent())) {
                                    SeatMapActivity.this.movieScheduleTimes =  movieScheduleTimes;

                                    for (MovieScheduleTimesDetails movieScheduleTimesDetails : SeatMapActivity.this.movieScheduleTimes.getMovieScheduleTimesDetailsList()) {
                                        movieScheduleTimeDetailsLabelList.add(movieScheduleTimesDetails.getLabel());
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }

                ArrayAdapter<String> movieScheduleTimeDetailsArrayAdapter = new ArrayAdapter<String>(SeatMapActivity.this, android.R.layout.simple_spinner_item, movieScheduleTimeDetailsLabelList);

                movieScheduleTimeDetailsArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerMovieTimes.setAdapter(movieScheduleTimeDetailsArrayAdapter);
            }

            @Override
            public void onFailure(Call<MovieSchedule> call, Throwable t) {
                Toast.makeText(SeatMapActivity.this, "Failed to load movie schedule.", Toast.LENGTH_SHORT).show();
            }
        });



        /*SEAT MAP HERE*/


        Call<SeatMap> seatMapCall = movieViewerNetworkInterface.getSeatMap();

        seatMapCall.enqueue(new Callback<SeatMap>() {
            @Override
            public void onResponse(Call<SeatMap> call, Response<SeatMap> response) {
                SeatMap seatMap = response.body();

                Log.e("ajkhsdf", String.valueOf(seatMap.getSeatAvailablility().getSeatCount()));

//                Log.e("ajkhsdf", "++++++++++++++++++++++++++++++++++++++++++++++");
//                Log.e("ajkhsdf", "+++++++++++++++ SEAT PLACEMENT +++++++++++++++");
//                Log.e("ajkhsdf", "++++++++++++++++++++++++++++++++++++++++++++++");
//
//                char alphabet = 'A';
//
//                for (List<String> row : seatMap.getSeatPlacementList()) {
//                    Log.e("ajkhsdf", "=============ROw "  + alphabet + "=============");
//
//                    for (String seatNumber : row) {
//                        switch (seatNumber) {
//                            case "a(30)":
//                                Log.e("ajkhsdf", "[AISLE]");
//                                break;
//                            case "b(20)":
//                                Log.e("ajkhsdf", "[AISLE]");
//                                break;
//                            default:
//                                Log.e("ajkhsdf", "SEAT_NUMBER>>>" + seatNumber);
//                        }
//                    }
//
//                    alphabet++;
//                }

                Log.e("ajkhsdf", "+++++++++++++++++++++++++++++++++++++++++++++++++");
                Log.e("ajkhsdf", "+++++++++++++++ SEAT AVAILABILITY +++++++++++++++");
                Log.e("ajkhsdf", "+++++++++++++++++++++++++++++++++++++++++++++++++");

                char alphabet = 'A';
                int availableSeats = 0;
                int reservedSeats = 0;
                int totalSeats = 0;
                int placeholder = 0;

                for (List<String> row : seatMap.getSeatPlacementList()) {
                    Log.e("ajkhsdf", "=============ROw "  + alphabet + "=============");

                    for (String seatNumber : row) {
                        switch (seatNumber) {
                            case "a(30)":
                            case "b(20)":
                            case "A33":
                                String left = "";
                                String right = "";

                                try {
                                    left = row.get(row.indexOf(seatNumber) - 1) + "<<<";
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                try {
                                    right = ">>>" + row.get(row.indexOf(seatNumber) + 1);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                Log.e("ajkhsdf", left  + "[AISLE]" + right);
                                placeholder++;
                                break;
                            default:
                                if (seatMap.getSeatAvailablility().getAvailableSeatsList().indexOf(seatNumber) != -1) {
                                    Log.e("ajkhsdf", seatNumber + " AVAILABLE");

                                    availableSeats ++;
                                } else {
                                    Log.e("ajkhsdf", seatNumber + " RESERVED");

                                    reservedSeats ++;
                                }

                                totalSeats ++;
                        }
                    }

                    alphabet++;
                }

                Log.e("ajkhsdf", totalSeats + "TOTAL SEATS");
                Log.e("ajkhsdf", availableSeats + "AVAILABLE SEATS");
                Log.e("ajkhsdf", reservedSeats + "AVAILABLE SEATS");
                Log.e("ajkhsdf", placeholder + "PLACEHOLDER");
            }

            @Override
            public void onFailure(Call<SeatMap> call, Throwable t) {
                Log.e("ajkhsdf", "FAILURE>>>" + t.getMessage());
            }
        });

        /*LAYOUT SEATMAP*/

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

    class MySpinnerMovieDateOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            /*set CINEMA DROP DOWN*/

            List<String> movieScheduleCinemaDetailsLabelList = new ArrayList<>();

            MovieScheduleDate movieScheduleDate = movieScheduleDateList.get(i);

            for (MovieScheduleCinemas movieScheduleCinemas : movieScheduleCinemasList) {
                if (movieScheduleDate.getId().equals(movieScheduleCinemas.getParent())) {
                    SeatMapActivity.this.movieScheduleCinemas = movieScheduleCinemas;

                    for (MovieScheduleCinemaDetails movieScheduleCinemaDetails : SeatMapActivity.this.movieScheduleCinemas.getMovieScheduleCinemaDetailsList()) {
                        movieScheduleCinemaDetailsLabelList.add(movieScheduleCinemaDetails.getLabel());
                    }

                    break;
                }
            }

            ArrayAdapter<String> movieScheduleCinemaDetailsArrayAdapter = new ArrayAdapter<String>(SeatMapActivity.this, android.R.layout.simple_spinner_item, movieScheduleCinemaDetailsLabelList);

            movieScheduleCinemaDetailsArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerMovieCinemas.setAdapter(null);
            spinnerMovieCinemas.setAdapter(movieScheduleCinemaDetailsArrayAdapter);

            /*set TIME DROP DOWN*/

            List<String> movieScheduleTimeDetailsLabelList = new ArrayList<>();

            for (MovieScheduleTimes movieScheduleTimes : movieScheduleTimesList) {
                for (MovieScheduleCinemas movieScheduleCinemas : movieScheduleCinemasList) {
                    if (movieScheduleDate.getId().equals(movieScheduleCinemas.getParent())) {
                        for (MovieScheduleCinemaDetails movieScheduleCinemaDetails : movieScheduleCinemas.getMovieScheduleCinemaDetailsList()) {
                            if (movieScheduleCinemaDetails.getId().equals(movieScheduleTimes.getParent())) {
                                SeatMapActivity.this.movieScheduleTimes =  movieScheduleTimes;

                                for (MovieScheduleTimesDetails movieScheduleTimesDetails : SeatMapActivity.this.movieScheduleTimes.getMovieScheduleTimesDetailsList()) {
                                    movieScheduleTimeDetailsLabelList.add(movieScheduleTimesDetails.getLabel());
                                }
                                break;
                            }
                        }
                    }
                }
            }

            ArrayAdapter<String> movieScheduleTimeDetailsArrayAdapter = new ArrayAdapter<String>(SeatMapActivity.this, android.R.layout.simple_spinner_item, movieScheduleTimeDetailsLabelList);

            movieScheduleTimeDetailsArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerMovieTimes.setAdapter(null);
            spinnerMovieTimes.setAdapter(movieScheduleTimeDetailsArrayAdapter);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    class MySpinnerMovieCinemasOnItemSelectedListener implements AdapterView.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            /*set TIME DROP DOWN*/

            MovieScheduleCinemaDetails movieScheduleCinemaDetails = movieScheduleCinemas.getMovieScheduleCinemaDetailsList().get(i);

            List<String> movieScheduleTimeDetailsLabelList = new ArrayList<>();

            for (MovieScheduleTimes movieScheduleTimes : movieScheduleTimesList) {
                if (movieScheduleCinemaDetails.getId().equals(movieScheduleTimes.getParent())) {
                    SeatMapActivity.this.movieScheduleTimes =  movieScheduleTimes;

                    for (MovieScheduleTimesDetails movieScheduleTimesDetails : SeatMapActivity.this.movieScheduleTimes.getMovieScheduleTimesDetailsList()) {
                        movieScheduleTimeDetailsLabelList.add(movieScheduleTimesDetails.getLabel());
                    }
                    break;
                }
            }

            ArrayAdapter<String> movieScheduleTimeDetailsArrayAdapter = new ArrayAdapter<String>(SeatMapActivity.this, android.R.layout.simple_spinner_item, movieScheduleTimeDetailsLabelList);

            movieScheduleTimeDetailsArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerMovieTimes.setAdapter(null);
            spinnerMovieTimes.setAdapter(movieScheduleTimeDetailsArrayAdapter);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }
}
