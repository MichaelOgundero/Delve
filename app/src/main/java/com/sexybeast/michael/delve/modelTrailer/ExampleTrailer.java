package com.sexybeast.michael.delve.modelTrailer;


import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExampleTrailer {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<ResultTrailer> results = new ArrayList<ResultTrailer>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<ResultTrailer> getResults() {
        return results;
    }

    public void setResults(List<ResultTrailer> results) {
        this.results = results;
    }

}