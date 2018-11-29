package com.sexybeast.michael.delve;

import com.sexybeast.michael.delve.Model_ID.Example_ID;
import com.sexybeast.michael.delve.model.Example;
import com.sexybeast.michael.delve.modelCrew.ExampleCrew;
import com.sexybeast.michael.delve.modelTrailer.ExampleTrailer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface MovieInterface {

    String BASE_URL = "https://api.themoviedb.org/3/";


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

    //crew info
    @GET
    Call<ExampleCrew> getCrew(@Url String url);

}


