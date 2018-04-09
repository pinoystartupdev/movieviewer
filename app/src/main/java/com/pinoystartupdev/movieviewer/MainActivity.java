package com.pinoystartupdev.movieviewer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pinoystartupdev.movieviewer.network.APIClient;
import com.pinoystartupdev.movieviewer.network.APIInterface;
import com.pinoystartupdev.movieviewer.network.MovieViewerNetworkInterface;
import com.pinoystartupdev.movieviewer.pojo.Movie;
import com.pinoystartupdev.movieviewer.pojo.MultipleResource;
import com.pinoystartupdev.movieviewer.pojo.SeatMap;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.imageViewPosterLandscape)
    ImageView imageViewPosterLandscape;

    @BindView(R.id.imageViewPoster)
    ImageView imageViewPoster;

    @BindView(R.id.textViewCanonicalTitle)
    TextView textViewCanonicalTitle;

    @BindView(R.id.textViewGenre)
    TextView textViewGenre;

    @BindView(R.id.textViewAdvisoryRating)
    TextView textViewAdvisoryRating;

    @BindView(R.id.textViewRuntime)
    TextView textViewRuntime;

    @BindView(R.id.textViewReleaseDate)
    TextView textViewReleaseDate;

    @BindView(R.id.textViewSynopsis)
    TextView textViewSynopsis;

    @BindView(R.id.textViewCast)
    TextView textViewCast;

    @OnClick(R.id.textViewViewSeatMap)
    public void gotoSeatMapScreen() {
        Intent intent = SeatMapActivity.newInstance(MainActivity.this);

        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        MovieViewerNetworkInterface movieViewerNetworkInterface = APIClient.getClient().create(MovieViewerNetworkInterface.class);

        Call<Movie> movieCall = movieViewerNetworkInterface.getMovieDetails();

        movieCall.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                Movie movie = response.body();

                textViewCanonicalTitle.setText(movie.getCanonicalTitle());
                textViewGenre.setText(movie.getGenre());
                textViewAdvisoryRating.setText(movie.getAdvisoryRating());

                float runtime = Float.valueOf(movie.getRuntimeMins());
                int hour = (int) runtime / 60;
                int minutes = (int) (runtime % 60 );

                String formattedHour =  (hour > 1) ? hour + "hrs" : "hr";
                String formattedMinutes = (minutes > 1) ? minutes + "mins" : minutes + "min";

                String formattedRunTime = formattedHour + " " + formattedMinutes;

                textViewRuntime.setText(formattedRunTime);

                SimpleDateFormat simpleDateFormatInput = new SimpleDateFormat("yyyy-mm-dd");
                SimpleDateFormat simpleDateFormatOutput = new SimpleDateFormat("MMM dd, yyyy");

                try {
                    Date date = simpleDateFormatInput.parse(movie.getReleaseDate());
                    textViewReleaseDate.setText(simpleDateFormatOutput.format(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                textViewSynopsis.setText(movie.getSynopsis());

                StringBuilder casts = new StringBuilder();

                for (String castname : movie.getCast()) {
                    casts.append(castname.concat("\n"));
                }
                textViewCast.setText(casts);

                Picasso.get().load(movie.getPosterLandscape()).into(imageViewPosterLandscape);
                Picasso.get().load(movie.getPoster()).into(imageViewPoster);
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to load movie details.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
