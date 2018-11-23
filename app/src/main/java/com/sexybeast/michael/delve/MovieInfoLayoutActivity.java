package com.sexybeast.michael.delve;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.sexybeast.michael.delve.model.Example;

import com.sexybeast.michael.delve.modelTrailer.ExampleTrailer;
import com.sexybeast.michael.delve.modelTrailer.ResultTrailer;

import retrofit2.Call;

import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieInfoLayoutActivity extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener{
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
        String moviename = intent.getExtras().getString("Movie name");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MovieInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final MovieInterface movieInterface = retrofit.create(MovieInterface.class);
        Call<Example> callXXX = movieInterface.getMovieSearch(MovieInterface.API_KEY, intent.getExtras().getString("Movie name"));
        callXXX.enqueue(new Callback<Example>() {
            TextView movieTitle = (TextView) findViewById(R.id.title);
            TextView movieRelease = (TextView) findViewById(R.id.release);
            TextView movieGenres = (TextView) findViewById(R.id.genres);
            TextView movieDescription = (TextView) findViewById(R.id.description);
            ImageView moviePoster = (ImageView) findViewById(R.id.poster);
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                String poster = response.body().getResults().get(0).getPosterPath();
                Glide.with(MovieInfoLayoutActivity.this).load("http://image.tmdb.org/t/p/original"+poster).into(moviePoster);

                movieTitle.setText(response.body().getResults().get(0).getTitle());

                String[] year = response.body().getResults().get(0).getReleaseDate().split("-");
                movieRelease.setText("("+  year[0] + ")");

                StringBuilder stringBuilder = new StringBuilder();
                boolean appendSeparator = false;
                for(int i=0 ; i<response.body().getResults().get(0).getGenreIds().size();i++){
                        if(appendSeparator)
                            stringBuilder.append(" | ");
                        appendSeparator = true;

                        stringBuilder.append(movieGenre(response.body().getResults().get(0).getGenreIds().get(i)));
                }
                movieGenres.setText(stringBuilder.toString());
                movieDescription.setText(response.body().getResults().get(0).getOverview());
            }
            @Override
            public void onFailure(Call<Example> call, Throwable t) {

            }
        });

     ////////////////////////////////////////////////////////////////
        if(moviename.equals("Man of Steel")) {
            Call<ExampleTrailer> callasd = movieInterface.getManofSteel(MovieInterface.API_KEY);
            System.out.println(callasd.request().toString());
            callasd.enqueue(new Callback<ExampleTrailer>() {
                @Override
                public void onResponse(Call<ExampleTrailer> call, Response<ExampleTrailer> response) {

                    videoKey.cueVideo(response.body().getResults().get(0).getKey());

                }

                @Override
                public void onFailure(Call<ExampleTrailer> call, Throwable t) {

                }
            });
        } if(moviename.equals("Catch Me If You Can")){
            Call<ExampleTrailer> callasd = movieInterface.getCatchME(MovieInterface.API_KEY);
            System.out.println(callasd.request().toString());
            callasd.enqueue(new Callback<ExampleTrailer>() {
                @Override
                public void onResponse(Call<ExampleTrailer> call, Response<ExampleTrailer> response) {
                    videoKey.cueVideo(response.body().getResults().get(0).getKey());
                }
                @Override
                public void onFailure(Call<ExampleTrailer> call, Throwable t) {
                }
            });
        }
        if(moviename.equals("Lemony Snicket's A Series of Unfortunate Events")){
            Call<ExampleTrailer> callasd = movieInterface.getSeriesOF(MovieInterface.API_KEY);
            System.out.println(callasd.request().toString());
            callasd.enqueue(new Callback<ExampleTrailer>() {
                @Override
                public void onResponse(Call<ExampleTrailer> call, Response<ExampleTrailer> response) {
                    videoKey.cueVideo(response.body().getResults().get(0).getKey());
                }
                @Override
                public void onFailure(Call<ExampleTrailer> call, Throwable t) {
                }
            });
        }
        if(moviename.equals("Guardians of The Galaxy")){
            Call<ExampleTrailer> callasd = movieInterface.getGotG(MovieInterface.API_KEY);
            System.out.println(callasd.request().toString());
            callasd.enqueue(new Callback<ExampleTrailer>() {
                @Override
                public void onResponse(Call<ExampleTrailer> call, Response<ExampleTrailer> response) {
                    videoKey.cueVideo(response.body().getResults().get(0).getKey());
                }
                @Override
                public void onFailure(Call<ExampleTrailer> call, Throwable t) {
                }
            });
        }

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        if(!wasRestored){
            this.videoKey = youTubePlayer;

        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
            if(errorReason.isUserRecoverableError()){
                errorReason.getErrorDialog(this, RECOVERY_REQUEST).show();
            }else{
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


    public String movieGenre(int id){
        if(id == 28){
            return "Action";
        }
        if(id == 12){
            return "Adventure";
        }
        if(id == 16){
            return "Animation";
        }
        if(id == 35){
            return "Comedy";
        }
        if(id == 80){
            return "Crime";
        }
        if(id == 99){
            return "Documentary";
        }
        if(id == 18){
            return "Drama";
        }
        if(id == 10751){
            return "Family";
        }
        if(id == 14){
            return "Fantasy";
        }
        if(id == 36){
            return "History";
        }
        if(id == 27){
            return "Horror";
        }
        if(id == 10402){
            return "Music";
        }
        if(id == 9648){
            return "Mystery";
        }
        if(id == 10749){
            return "Romance";
        }
        if(id == 878){
            return "Science Fiction";
        }
        if(id == 10770){
            return "TV Movie";
        }
        if(id == 53){
            return "Thriller";
        }
        if(id == 10752){
            return "War";
        }
        if(id == 37){
            return "Western";
        }
        else{
            return "N/A";
        }
    }
}
