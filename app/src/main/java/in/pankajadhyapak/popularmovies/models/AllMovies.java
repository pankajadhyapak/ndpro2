package in.pankajadhyapak.popularmovies.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by pankaj on 27/03/17.
 */

public class AllMovies {

    @SerializedName("page")
    private int page;

    @SerializedName("results")
    private ArrayList<Movie> results;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public ArrayList<Movie> getResults() {
        return results;
    }

    public void setResults(ArrayList<Movie> results) {
        this.results = results;
    }
}
