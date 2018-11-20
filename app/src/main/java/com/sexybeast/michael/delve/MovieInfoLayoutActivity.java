package com.sexybeast.michael.delve;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.sexybeast.michael.delve.model.Example;
import com.sexybeast.michael.delve.model.Result;

import java.util.List;

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

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MovieInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final MovieInterface movieInterface = retrofit.create(MovieInterface.class);
        Call<Example> callXXX = movieInterface.getMovieSearch(MovieInterface.API_KEY);
        callXXX.enqueue(new Callback<Example>() {
            TextView aaa = (TextView) findViewById(R.id.textView);
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                Log.d("a", "aAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
                aaa.setText(response.body().getBudget().toString());
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {

            }
        });

     ////////////////////////////////////////////////////////////////
        Call<Example> callasd = movieInterface.getTrailer(MovieInterface.API_KEY);
        callasd.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                for(int i=0;i<response.body().getResults().size();i++) {
                    videoKey.cueVideo(response.body().getResults().get(i).getKey());
                }
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {

            }
        });

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
}
