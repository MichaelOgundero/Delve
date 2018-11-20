package com.sexybeast.michael.delve;

import com.sexybeast.michael.delve.model.Example;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieInterface {

    String BASE_URL = "https://api.themoviedb.org/3/";
    String API_KEY = "623eeab48528051330ddc3ca73959483";
    String moviename = "550";
    @GET("movie/"+moviename)
    Call<Example> getMovieSearch(
                @Query("api_key") String Key
               );

            //for trailer
    @GET("movie/"+moviename+"/videos")
    Call<Example> getTrailer(
            @Query("api_key") String Key);
}

//    @Query("include_adult") String value,
//    @Query("language") String language,
//    @Query("api_key") String Key,
//    @Query("query") String moviename);

//using GET/movie/movieID
//    @GET("movie/550")
//    Call<Example> getMovie(
//            @Query("api_key") String Key);
////
//        //for trailer
//    @GET("movie/550/videos")
//    Call<Example> getTrailer(
//            @Query("api_key") String Key);
