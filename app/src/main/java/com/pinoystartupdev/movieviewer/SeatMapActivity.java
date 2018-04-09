package com.pinoystartupdev.movieviewer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pinoystartupdev.movieviewer.adapter.SelectedSeatRecyclerViewAdapter;
import com.pinoystartupdev.movieviewer.network.APIClient;
import com.pinoystartupdev.movieviewer.network.MovieViewerNetworkInterface;
import com.pinoystartupdev.movieviewer.pojo.MovieScheduleCinemaDetails;
import com.pinoystartupdev.movieviewer.pojo.MovieScheduleCinemas;
import com.pinoystartupdev.movieviewer.pojo.MovieScheduleDate;
import com.pinoystartupdev.movieviewer.pojo.MovieSchedule;
import com.pinoystartupdev.movieviewer.pojo.MovieScheduleTimes;
import com.pinoystartupdev.movieviewer.pojo.MovieScheduleTimesDetails;
import com.pinoystartupdev.movieviewer.pojo.SeatMap;
import com.pinoystartupdev.movieviewer.util.SeatMapUtilities;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import by.anatoldeveloper.hallscheme.hall.HallScheme;
import by.anatoldeveloper.hallscheme.hall.ScenePosition;
import by.anatoldeveloper.hallscheme.hall.Seat;
import by.anatoldeveloper.hallscheme.hall.SeatListener;
import by.anatoldeveloper.hallscheme.view.ZoomableImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.pinoystartupdev.movieviewer.util.SeatMapUtilities.generateSeatmap;

public class SeatMapActivity extends AppCompatActivity {
    @BindView(R.id.spinnerMovieDates)
    Spinner spinnerMovieDates;

    @BindView(R.id.spinnerMovieCinemas)
    Spinner spinnerMovieCinemas;

    @BindView(R.id.spinnerMovieTimes)
    Spinner spinnerMovieTimes;

    @BindView(R.id.textViewColorSelected) View textViewColorSelected;
    @BindView(R.id.textViewColorReserved) View textViewColorReserved;
    @BindView(R.id.textViewColorAvailable) View textViewColorAvailable;

    @BindView(R.id.recyclerViewSelectedSeats)
    RecyclerView recyclerViewSelectedSeats;

    @BindView(R.id.textViewTotalPrice)
    TextView textViewTotalPrice;

    @BindViews({ R.id.textViewSelectedSeatsLabel, R.id.recyclerViewSelectedSeats, R.id.textViewTotalPriceLabel, R.id.textViewTotalPrice })
    List<View> viewsOfListsForSelectionDisplay;

    List<MovieScheduleDate>  movieScheduleDateList;
    List<MovieScheduleCinemas> movieScheduleCinemasList;
    List<MovieScheduleTimes>  movieScheduleTimesList;

    List<MovieScheduleCinemaDetails> movieScheduleCinemaDetailsList;
    List<MovieScheduleTimesDetails> movieScheduleTimesDetailsList;

    MovieScheduleTimesDetails currentMovieScheduleTimeDetails;

    List<Pair<Integer, String>> selectedSeatList;
    List<String> seatNumberManifest;

    @BindView(R.id.imageView)
    ZoomableImageView imageView;

    static final ButterKnife.Action<View> INVISIBLE = new ButterKnife.Action<View>() {
        @Override public void apply(View view, int index) {
            view.setVisibility(View.INVISIBLE);
        }
    };
    static final ButterKnife.Action<View> VISIBLE = new ButterKnife.Action<View>() {
        @Override public void apply(View view, int index) {
            view.setVisibility(View.VISIBLE);
        }
    };

    public static Intent newInstance(Context context) {
        return new Intent(context, SeatMapActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_map);
        ButterKnife.bind(this);

        ButterKnife.apply(viewsOfListsForSelectionDisplay, INVISIBLE);

        movieScheduleDateList = new ArrayList<>();
        movieScheduleCinemasList = new ArrayList<>();
        movieScheduleTimesList = new ArrayList<>();

        movieScheduleCinemaDetailsList = new ArrayList<>();
        movieScheduleTimesDetailsList = new ArrayList<>();

        selectedSeatList = new ArrayList<>();
        seatNumberManifest = new ArrayList<>();

        textViewColorAvailable.setBackgroundColor(Color.GRAY);
        textViewColorReserved.setBackgroundColor(Color.BLUE);
        textViewColorSelected.setBackgroundColor(Color.RED);
        ((TextView) textViewColorSelected).setText(" \u2713");

        LinearLayoutManager linearLayoutManagerForWildCard = new LinearLayoutManager(SeatMapActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewSelectedSeats.setLayoutManager(linearLayoutManagerForWildCard);
        recyclerViewSelectedSeats.setAdapter(new SelectedSeatRecyclerViewAdapter(SeatMapActivity.this, selectedSeatList));


        final MovieViewerNetworkInterface movieViewerNetworkInterface = APIClient.getClient().create(MovieViewerNetworkInterface.class);

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
                        movieScheduleCinemaDetailsList.addAll(movieScheduleCinemas.getMovieScheduleCinemaDetailsList());

                        for (MovieScheduleCinemaDetails movieScheduleCinemaDetails : movieScheduleCinemaDetailsList) {
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
                    for (MovieScheduleCinemaDetails movieScheduleCinemaDetails : movieScheduleCinemaDetailsList) {
                        if (movieScheduleCinemaDetails.getId().equals(movieScheduleTimes.getParent())) {
                            movieScheduleTimesDetailsList.addAll(movieScheduleTimes.getMovieScheduleTimesDetailsList());

                            currentMovieScheduleTimeDetails = movieScheduleTimesDetailsList.get(0);

                            for (MovieScheduleTimesDetails movieScheduleTimesDetails : movieScheduleTimesDetailsList) {
                                movieScheduleTimeDetailsLabelList.add(movieScheduleTimesDetails.getLabel());
                            }
                            break;
                        }
                    }
                }

                ArrayAdapter<String> movieScheduleTimeDetailsArrayAdapter = new ArrayAdapter<String>(SeatMapActivity.this, android.R.layout.simple_spinner_item, movieScheduleTimeDetailsLabelList);

                movieScheduleTimeDetailsArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerMovieTimes.setAdapter(movieScheduleTimeDetailsArrayAdapter);

                spinnerMovieTimes.setOnItemSelectedListener(new MySpinnerMovieTimeOnItemSelectedListener());

                        /*ASSEMBLE SEAT MAP HERE*/


                Call<SeatMap> seatMapCall = movieViewerNetworkInterface.getSeatMap();

                seatMapCall.enqueue(new Callback<SeatMap>() {
                    @Override
                    public void onResponse(Call<SeatMap> call, Response<SeatMap> response) {
                        SeatMap seatMap = response.body();

                /*LAYOUT SEATMAP*/

                        generateSeatmap(seatMap, new SeatMapUtilities.MySeatMapCallback() {
                            @Override
                            public void generateSuccess(Seat[][] seats, final List<Pair<Integer, String>> seatNumberManifest) {
                                if (imageView != null) {
                                    HallScheme scheme = new HallScheme(imageView, seats, SeatMapActivity.this);
                                    scheme.setChosenSeatBackgroundColor(Color.RED);
                                    scheme.setChosenSeatTextColor(Color.WHITE);
                                    scheme.setBackgroundColor(Color.WHITE);
                                    scheme.setUnavailableSeatBackgroundColor(Color.BLUE);
                                    scheme.setScenePosition(ScenePosition.NORTH);
                                    scheme.setSceneName("Movie Screen");

                                    scheme.setSeatListener(new SeatListener() {

                                        @Override
                                        public void selectSeat(int id) {
                                            if (currentMovieScheduleTimeDetails != null) {
                                                Pair<Integer, String> seatNumber = new SeatMapUtilities().getSeatNumber(seatNumberManifest, id);

                                                if (seatNumber != null) {
                                                    selectedSeatList.add(seatNumber);

                                                    ButterKnife.apply(viewsOfListsForSelectionDisplay, VISIBLE);

                                                    recyclerViewSelectedSeats.getAdapter().notifyDataSetChanged();

                                                    updatePrice();
                                                } else {
                                                    Toast.makeText(SeatMapActivity.this, "can not select seat " + id, Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toast.makeText(SeatMapActivity.this, "currentMovieScheduleTimeDetails IS NULL", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void unSelectSeat(int id) {
                                            try {
                                                Pair<Integer, String> seatNumber = new SeatMapUtilities().getSeatNumber(seatNumberManifest, id);

                                                if (seatNumber != null) {
                                                    selectedSeatList.remove(seatNumber);

                                                    if (!(selectedSeatList.size() > 0)) {
                                                        ButterKnife.apply(viewsOfListsForSelectionDisplay, INVISIBLE);
                                                    }

                                                    recyclerViewSelectedSeats.getAdapter().notifyDataSetChanged();

                                                    updatePrice();
                                                } else {
                                                    Toast.makeText(SeatMapActivity.this, "can not unSelect seat " + id, Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (IndexOutOfBoundsException e) {
                                                e.printStackTrace();

                                                Toast.makeText(SeatMapActivity.this, "can not unSelect seat " + id, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<SeatMap> call, Throwable t) {
                        Toast.makeText(SeatMapActivity.this, "Failed to load seat map.", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<MovieSchedule> call, Throwable t) {
                Toast.makeText(SeatMapActivity.this, "Failed to load movie schedule.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updatePrice() {
        int currentPrice = Integer.valueOf(currentMovieScheduleTimeDetails.getPrice()) * selectedSeatList.size();

        textViewTotalPrice.setText("PHP".concat(String.valueOf(currentPrice)));
    }

    class MySpinnerMovieDateOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            /*set CINEMA DROP DOWN*/

            List<String> movieScheduleCinemaDetailsLabelList = new ArrayList<>();
            movieScheduleCinemaDetailsList.clear();

            MovieScheduleDate movieScheduleDate = movieScheduleDateList.get(i);

            for (MovieScheduleCinemas movieScheduleCinemas : movieScheduleCinemasList) {
                if (movieScheduleDate.getId().equals(movieScheduleCinemas.getParent())) {
                    movieScheduleCinemaDetailsList.addAll(movieScheduleCinemas.getMovieScheduleCinemaDetailsList());

                    for (MovieScheduleCinemaDetails movieScheduleCinemaDetails : movieScheduleCinemaDetailsList) {
                        movieScheduleCinemaDetailsLabelList.add(movieScheduleCinemaDetails.getLabel());
                    }

                    if (selectedSeatList.size() > 0) {
                        updatePrice();
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
            movieScheduleTimesDetailsList.clear();

            for (MovieScheduleTimes movieScheduleTimes : movieScheduleTimesList) {
                for (MovieScheduleCinemaDetails movieScheduleCinemaDetails : movieScheduleCinemaDetailsList) {
                    if (movieScheduleCinemaDetails.getId().equals(movieScheduleTimes.getParent())) {
                        movieScheduleTimesDetailsList.addAll(movieScheduleTimes.getMovieScheduleTimesDetailsList());
                        currentMovieScheduleTimeDetails = movieScheduleTimesDetailsList.get(0);

                        for (MovieScheduleTimesDetails movieScheduleTimesDetails : movieScheduleTimesDetailsList) {
                            movieScheduleTimeDetailsLabelList.add(movieScheduleTimesDetails.getLabel());
                        }
                        break;
                    }
                }
            }

            Log.e("jhagdfs", "movieScheduleTimeDetailsLabelList:" + movieScheduleTimeDetailsLabelList.size());

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

            MovieScheduleCinemaDetails movieScheduleCinemaDetails = movieScheduleCinemaDetailsList.get(i);

            List<String> movieScheduleTimeDetailsLabelList = new ArrayList<>();
            movieScheduleTimesDetailsList.clear();

            for (MovieScheduleTimes movieScheduleTimes : movieScheduleTimesList) {
                if (movieScheduleCinemaDetails.getId().equals(movieScheduleTimes.getParent())) {
                    movieScheduleTimesDetailsList.addAll(movieScheduleTimes.getMovieScheduleTimesDetailsList());
                    currentMovieScheduleTimeDetails = movieScheduleTimesDetailsList.get(0);

                    for (MovieScheduleTimesDetails movieScheduleTimesDetails : movieScheduleTimesDetailsList) {
                        movieScheduleTimeDetailsLabelList.add(movieScheduleTimesDetails.getLabel());
                    }

                    if (selectedSeatList.size() > 0) {
                        updatePrice();
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

    class MySpinnerMovieTimeOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            currentMovieScheduleTimeDetails = movieScheduleTimesDetailsList.get(i);

            if (selectedSeatList.size() > 0) {
                updatePrice();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }
}
