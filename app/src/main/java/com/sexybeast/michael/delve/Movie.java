package com.sexybeast.michael.delve;

import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sexybeast.michael.delve.model.Example;

import java.util.ArrayList;
import java.util.Random;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class Movie extends RealmObject {

    @PrimaryKey
    @Required
    private String movieID;




    private String name;
    private String tmdbID;


    public Movie(){ }


    public Movie(String tmdbID) {
        this.tmdbID = tmdbID;
    }

    public String getTmdbID() {
        return tmdbID;
    }

    public void setTmdbID(String tmdbID) {
        this.tmdbID = tmdbID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
