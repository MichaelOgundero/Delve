package com.sexybeast.michael.delve;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class RealmHelper {
    Realm realm;

    public RealmHelper(Realm realm){
        this.realm = realm;
    }

    //write
    public void save(final Movie movie){
        realm.executeTransaction(new Realm.Transaction(){
            @Override
            public void execute(Realm realm) {
                  Movie m = realm.copyToRealm(movie);
                }
             }
        );
    }

    //read
    public List<Movie> retrieve(){
        List<Movie> moviesObj = new ArrayList<>();
        RealmResults<Movie> movies = realm.where(Movie.class).findAll();

        for(Movie m:movies){
            moviesObj.add(m);
        }

        return moviesObj;
    }
}
