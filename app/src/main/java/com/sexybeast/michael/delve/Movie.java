package com.sexybeast.michael.delve;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Movie extends RealmObject {

    @PrimaryKey
    @Required
    private String movieID;

    private String name;
    private String genre;


    public Movie(){}

    public Movie(String name, String genre) {
        this.name = name;
        this.genre = genre;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
