package com.sexybeast.michael.delve;

import com.sexybeast.michael.delve.Model_ID.Example_ID;
import com.sexybeast.michael.delve.model.Example;
import com.sexybeast.michael.delve.modelTrailer.ExampleTrailer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface MovieInterface {

    String BASE_URL = "https://api.themoviedb.org/3/";
    String API_KEY = "623eeab48528051330ddc3ca73959483";
    String manofsteel = "49521";
    String catchmeifyoucan = "640";
    String SeriesOF = "11774";
    String GotG = "118340";

    @GET("search/movie")
    Call<Example> getMovieSearch(
                    @Query("api_key") String Key,
                    @Query("query") String movie
            );

    //dynamic url////////////////////////////////////////////////////////////////////////////////////////
    //movie details
    @GET
    Call<Example_ID> getMovieDetails(@Url String url);

    //trailers
    @GET
    Call<ExampleTrailer> getMovieTrailer(@Url String url);

    //popular movies
    @GET
    Call<Example> getPopularMovies(@Url String url);

}


