package com.sexybeast.michael.delve;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.sexybeast.michael.delve.Model_ID.Example_ID;
import com.sexybeast.michael.delve.model.Example;

import com.sexybeast.michael.delve.modelCrew.ExampleCrew;
import com.sexybeast.michael.delve.modelRatings.ExampleRatings;
import com.sexybeast.michael.delve.modelTrailer.ExampleTrailer;
import com.sexybeast.michael.delve.modelTrailer.ResultTrailer;

import org.w3c.dom.Text;

import java.util.ArrayList;

import retrofit2.Call;

import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieInfoLayoutActivity extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener {
    Intent intent;

    private static final int RECOVERY_REQUEST = 1;
    private YouTubePlayerView youTubeView;
    private YouTubePlayer videoKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_info_layout);


        //youtube
        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.initialize(Config.YOUTUBE_API_KEY, this);


        intent = getIntent();
        String movieID = intent.getExtras().getString("Movie id");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MovieInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final MovieInterface movieInterface = retrofit.create(MovieInterface.class);
        Call<Example_ID> callUSD = movieInterface.getMovieDetails("movie/" + movieID + "?api_key=623eeab48528051330ddc3ca73959483");
        callUSD.enqueue(new Callback<Example_ID>() {

            TextView movieTitle = (TextView) findViewById(R.id.title);
            TextView movieRelease = (TextView) findViewById(R.id.release);
            TextView movieGenres = (TextView) findViewById(R.id.genres);
            TextView movieDescription = (TextView) findViewById(R.id.description);
            TextView movieRunTime = (TextView) findViewById(R.id.runtime);
            TextView movieBudget = (TextView) findViewById(R.id.budget);
            TextView movieRevenue = (TextView) findViewById(R.id.revenue);
            TextView movieProductionCompany = (TextView) findViewById(R.id.productioncompanies);
            TextView movieReleaseDate = (TextView) findViewById(R.id.releaseDate);
            ImageView moviePoster = (ImageView) findViewById(R.id.poster);

            @Override
            public void onResponse(Call<Example_ID> call, Response<Example_ID> response) {
                if (response.isSuccessful()) {
                    //movie name
                    movieTitle.setText(response.body().getTitle());

                    //movie year
                    String[] year = response.body().getReleaseDate().split("-");
                    movieRelease.setText("(" + year[0] + ")");

                    //movie release date
                    movieReleaseDate.setText(response.body().getReleaseDate());

                    //movie genres
                    StringBuilder stringBuilder = new StringBuilder();
                    boolean appendSeparator = false;
                    for (int i = 0; i < response.body().getGenres().size(); i++) {
                        if (appendSeparator)
                            stringBuilder.append(" | ");
                        appendSeparator = true;

                        stringBuilder.append(response.body().getGenres().get(i).getName());
                    }
                    movieGenres.setText(stringBuilder.toString());

                    //movie description
                    movieDescription.setText(response.body().getOverview());

                    //movie poster
                    String poster = response.body().getPosterPath();
                    Glide.with(MovieInfoLayoutActivity.this).load("http://image.tmdb.org/t/p/original" + poster).into(moviePoster);

                    //movie runtime
                    if (response.body().getRuntime() == null) {
                        movieRunTime.setText("N/A");
                    } else {
                        movieRunTime.setText(response.body().getRuntime().toString() + " " + "min");
                    }

                    //budget revenue
                    movieBudget.setText("$" + response.body().getBudget().toString());

                    //movie revenue
                    if (response.body().getRevenue() > response.body().getBudget()) {
                        movieRevenue.setTextColor(Color.GREEN);
                        movieRevenue.setText("$" + response.body().getRevenue().toString());
                    } else if (response.body().getRevenue() < response.body().getBudget()) {
                        movieRevenue.setTextColor(Color.RED);
                        movieRevenue.setText("$" + response.body().getRevenue().toString());
                    }

                    //production companies
                    StringBuilder stringBuilder1 = new StringBuilder();
                    boolean appendSeparator1 = false;
                    for (int i = 0; i < response.body().getProductionCompanies().size(); i++) {
                        if (appendSeparator1)
                            stringBuilder1.append("\n");
                        appendSeparator1 = true;

                        stringBuilder1.append(response.body().getProductionCompanies().get(i).getName());
                    }
                    movieProductionCompany.setText(stringBuilder1.toString());


                }
            }

            @Override
            public void onFailure(Call<Example_ID> call, Throwable t) {

            }
        });


        ////////////////////////////////////////////////////////////////

        Call<ExampleTrailer> callasd = movieInterface.getMovieTrailer("movie/" + movieID + "/videos?api_key=623eeab48528051330ddc3ca73959483");
        System.out.println(callasd.request().toString());
        callasd.enqueue(new Callback<ExampleTrailer>() {
            @Override
            public void onResponse(Call<ExampleTrailer> call, Response<ExampleTrailer> response) {
                if (response.isSuccessful()) {
                    if (response.body().getResults().size() == 0) {
                    } else {
                        videoKey.cueVideo(response.body().getResults().get(0).getKey());
                    }
                }
            }

            @Override
            public void onFailure(Call<ExampleTrailer> call, Throwable t) {

            }
        });

        /////////////////////////////////////////////////////////////////////////
        Call<ExampleCrew> callcrew = movieInterface.getCrew("movie/" + movieID + "/credits?api_key=623eeab48528051330ddc3ca73959483");
        callcrew.enqueue(new Callback<ExampleCrew>() {
            TextView movieDirector = (TextView) findViewById(R.id.director);
            TextView movieCast = (TextView) findViewById(R.id.cast);
            TextView movieWriters = (TextView) findViewById(R.id.writers);

            @Override
            public void onResponse(Call<ExampleCrew> call, Response<ExampleCrew> response) {
                if (response.isSuccessful()) {

                    //movieDirector
                    ArrayList<String> director = new ArrayList<>();
                    for (int i = 0; i < response.body().getCrew().size(); i++) {
                        if (response.body().getCrew().get(i).getJob().equals("Director")) {
                            director.add(response.body().getCrew().get(i).getName());
                        }
                    }
                    for (int i = 0; i < director.size(); i++) {
                        movieDirector.setText(director.get(i));
                    }

                    //movie writers
                    ArrayList<String> writers = new ArrayList<>();
                    for (int i = 0; i < response.body().getCrew().size(); i++) {
                        if (response.body().getCrew().get(i).getJob().equals("Writer")) {
                            writers.add(response.body().getCrew().get(i).getName());
                        }
                    }
                    StringBuilder stringBuilder1 = new StringBuilder();
                    boolean appendSeparator1 = false;
                    if (writers.isEmpty()) {
                        movieWriters.setText("N/A");
                    } else {

                        for (int i = 0; i < writers.size(); i++) {

                            if (appendSeparator1)
                                stringBuilder1.append("\n");
                            appendSeparator1 = true;

                            stringBuilder1.append(writers.get(i));

                        }
                        movieWriters.setText(stringBuilder1.toString());
                    }

                    //movie cast
                    StringBuilder stringBuilder = new StringBuilder();
                    boolean appendSeparator = false;

                    if (response.body().getCast().size() < 10) {
                        for (int i = 0; i < response.body().getCast().size(); i++) {
                            if (appendSeparator) {
                                stringBuilder.append("\n");
                            }
                            appendSeparator = true;

                            stringBuilder.append(response.body().getCast().get(i).getName());
                        }
                        movieCast.setText(stringBuilder.toString());
                    } else if (response.body().getCast().size() > 10) {
                        for (int i = 0; i < 10; i++) {
                            if (appendSeparator) {
                                stringBuilder.append("\n");
                            }
                            appendSeparator = true;

                            stringBuilder.append(response.body().getCast().get(i).getName());
                        }
                        movieCast.setText(stringBuilder.toString());
                    }


                }
            }

            @Override
            public void onFailure(Call<ExampleCrew> call, Throwable t) {

            }
        });

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////
        Call<ExampleRatings> callRatings = movieInterface.getRatings("movie/" + movieID + "/release_dates?api_key=623eeab48528051330ddc3ca73959483");
        callRatings.enqueue(new Callback<ExampleRatings>() {
            TextView movieRating = (TextView) findViewById(R.id.rating);

            @Override
            public void onResponse(Call<ExampleRatings> call, Response<ExampleRatings> response) {
                if (response.isSuccessful()) {
                    ArrayList<String> rating = new ArrayList<>();
                    for (int i = 0; i < response.body().getResults().size(); i++) {
                        if (response.body().getResults().get(i).getIso31661().equals("")) {
                            rating.add("N/A");
                        } else if (response.body().getResults().get(i).getIso31661().equals("US")) {
                            if (response.body().getResults().get(i).getReleaseDates().get(0).getCertification().equals("")) {
                                rating.add("N/A");
                            } else {
                                rating.add(response.body().getResults().get(i).getReleaseDates().get(0).getCertification());
                            }
                        }

                    }

                    for (int i = 0; i < rating.size(); i++) {
                        if (rating.get(i).equals("R")) {
                            movieRating.setTextColor(Color.RED);
                            movieRating.setText(rating.get(i));
                        } else if (rating.get(i).contains("PG")) {
                            movieRating.setTextColor(Color.GREEN);
                            movieRating.setText(rating.get(i));
                        } else {
                            movieRating.setTextColor(Color.BLUE);
                            movieRating.setText(rating.get(i));
                        }

                    }
                }

            }

            @Override
            public void onFailure(Call<ExampleRatings> call, Throwable t) {

            }
        });

        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        if (!wasRestored) {
            this.videoKey = youTubePlayer;

        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            String error = String.format(getString(R.string.player_error),
                    errorReason.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        getYoutubePlayerProvider().initialize(Config.YOUTUBE_API_KEY, this);
    }

    protected YouTubePlayer.Provider getYoutubePlayerProvider() {
        return youTubeView;
    }


}
