package com.sexybeast.michael.delve;

import com.sexybeast.michael.delve.model.Example;
import com.sexybeast.michael.delve.modelTrailer.ExampleTrailer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

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

            //for trailer
    @GET("movie/"+manofsteel+"/videos")
    Call<ExampleTrailer> getManofSteel(
            @Query("api_key") String Key);

    @GET("movie/"+catchmeifyoucan+"/videos")
    Call<ExampleTrailer> getCatchME(
            @Query("api_key") String Key);


    @GET("movie/"+SeriesOF+"/videos")
    Call<ExampleTrailer> getSeriesOF(
            @Query("api_key") String Key);

    @GET("movie/"+GotG+"/videos")
    Call<ExampleTrailer> getGotG(
            @Query("api_key") String Key);

}


