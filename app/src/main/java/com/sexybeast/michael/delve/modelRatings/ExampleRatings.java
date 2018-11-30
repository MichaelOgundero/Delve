package com.sexybeast.michael.delve.modelRatings;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExampleRatings {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<ResultRatings> results = new ArrayList<ResultRatings>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<ResultRatings> getResults() {
        return results;
    }

    public void setResults(List<ResultRatings> results) {
        this.results = results;
    }

}