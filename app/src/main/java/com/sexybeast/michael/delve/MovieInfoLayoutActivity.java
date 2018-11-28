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
import com.sexybeast.michael.delve.Model_ID.Example_ID;
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
        String movieID = intent.getExtras().getString("Movie id");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MovieInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final MovieInterface movieInterface = retrofit.create(MovieInterface.class);
        Call<Example_ID> callUSD = movieInterface.getMovieDetails("movie/"+movieID+"?api_key=623eeab48528051330ddc3ca73959483");
        callUSD.enqueue(new Callback<Example_ID>() {

            TextView movieTitle = (TextView) findViewById(R.id.title);
            TextView movieRelease = (TextView) findViewById(R.id.release);
            TextView movieGenres = (TextView) findViewById(R.id.genres);
            TextView movieDescription = (TextView) findViewById(R.id.description);
            ImageView moviePoster = (ImageView) findViewById(R.id.poster);

            @Override
            public void onResponse(Call<Example_ID> call, Response<Example_ID> response) {
                if(response.isSuccessful()){
                    //movie name
                    movieTitle.setText(response.body().getTitle());

                    //movie year
                    String[] year = response.body().getReleaseDate().split("-");
                    movieRelease.setText("("+  year[0] + ")");

                    //movie genres
                    StringBuilder stringBuilder = new StringBuilder();
                    boolean appendSeparator = false;
                    for(int i=0 ; i<response.body().getGenres().size();i++){
                        if(appendSeparator)
                            stringBuilder.append(" | ");
                        appendSeparator = true;

                        stringBuilder.append(response.body().getGenres().get(i).getName());
                     }
                    movieGenres.setText(stringBuilder.toString());

                    //movie description
                    movieDescription.setText(response.body().getOverview());

                    //movie poster
                    String poster = response.body().getPosterPath();
                     Glide.with(MovieInfoLayoutActivity.this).load("http://image.tmdb.org/t/p/original"+poster).into(moviePoster);

                }
            }

            @Override
            public void onFailure(Call<Example_ID> call, Throwable t) {

            }
        });


     ////////////////////////////////////////////////////////////////

            Call<ExampleTrailer> callasd = movieInterface.getMovieTrailer("movie/"+movieID+"/videos?api_key=623eeab48528051330ddc3ca73959483");
            System.out.println(callasd.request().toString());
            callasd.enqueue(new Callback<ExampleTrailer>() {
                @Override
                public void onResponse(Call<ExampleTrailer> call, Response<ExampleTrailer> response) {
                    if(response.isSuccessful()) {
                        if(response.body().getResults().size() == 0) {
                        }else{
                            videoKey.cueVideo(response.body().getResults().get(0).getKey());
                        }
                    }
                }
                @Override
                public void onFailure(Call<ExampleTrailer> call, Throwable t) {

                }
            });

        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
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
