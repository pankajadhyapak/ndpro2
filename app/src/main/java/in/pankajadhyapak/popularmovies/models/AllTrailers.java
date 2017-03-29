package in.pankajadhyapak.popularmovies.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by pankaj on 27/03/17.
 */

public class AllTrailers {

    @SerializedName("results")
    private ArrayList<Trailer> results;

    public ArrayList<Trailer> getResults() {
        return results;
    }

    public void setResults(ArrayList<Trailer> results) {
        this.results = results;
    }
}
