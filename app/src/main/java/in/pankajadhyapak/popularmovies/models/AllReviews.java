package in.pankajadhyapak.popularmovies.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by pankaj on 27/03/17.
 */

public class AllReviews {

    @SerializedName("results")
    private ArrayList<Review> results;

    public ArrayList<Review> getResults() {
        return results;
    }

    public void setResults(ArrayList<Review> results) {
        this.results = results;
    }
}
