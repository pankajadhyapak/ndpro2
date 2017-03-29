package in.pankajadhyapak.popularmovies.activites;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.pankajadhyapak.popularmovies.adapters.MovieAdapter;
import in.pankajadhyapak.popularmovies.R;
import in.pankajadhyapak.popularmovies.api.MovieApi;
import in.pankajadhyapak.popularmovies.models.AllMovies;
import in.pankajadhyapak.popularmovies.models.Movie;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity {

    public static final int POPULAR = 1;
    public static final int TOP_RATED = 2;
    private static final String API_URL = "http://api.themoviedb.org/3/movie/";
    private static final String MOVIE_DB_KEY = "movieList";
    private static final String TAG = HomeActivity.class.getSimpleName();
    private ArrayList<Movie> allMovies = new ArrayList<>();
    private RecyclerView.Adapter mMovieAdapter;
    private ProgressDialog progress;

    @Bind(R.id.rv_list)
    RecyclerView movieRecylerView;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        movieRecylerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns()));
        mMovieAdapter = new MovieAdapter(HomeActivity.this, allMovies);
        movieRecylerView.setAdapter(mMovieAdapter);

        if (savedInstanceState == null || !savedInstanceState.containsKey(MOVIE_DB_KEY)) {
            Log.d(TAG, "Movie List not available in instance");
            if (isNetworkAvailable()) {
                getMovies(POPULAR);
            } else {
                showNetworkError();
            }
        } else {
            allMovies.clear();
            allMovies = savedInstanceState.getParcelableArrayList(MOVIE_DB_KEY);
            mMovieAdapter = new MovieAdapter(HomeActivity.this, allMovies);
            movieRecylerView.setAdapter(mMovieAdapter);
            Log.d(TAG, "Movie List retrieved from instance with size : " + allMovies.size());
        }
    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthDivider = 600;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2;
        return nColumns;
    }

    private void showNetworkError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cannot fetch movies")
                .setMessage("Please connect to wifi or enable cellular data!")
                .create()
                .show();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void getMovies(int type) {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieApi api = retrofit.create(MovieApi.class);
        showLoadingDialog();

        Call<AllMovies> call = null;
        if (type == POPULAR) {
            call = api.getPopularMovies(getString(R.string.api_key));
        } else {
            call = api.getTopRatedMovies(getString(R.string.api_key));
        }

        call.enqueue(new Callback<AllMovies>() {
            @Override
            public void onResponse(Call<AllMovies> call, Response<AllMovies> response) {
                Log.e(TAG, "onResponse: " + response.body().getResults().size());
                allMovies.clear();
                allMovies.addAll(response.body().getResults());
                dismissLoadingDialog();
                mMovieAdapter.notifyDataSetChanged();
                Log.e(TAG, "all movies size: " + allMovies.size());
            }

            @Override
            public void onFailure(Call<AllMovies> call, Throwable t) {
                Log.e("onFailure", "onFailure: " + t.getMessage());
                dismissLoadingDialog();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MOVIE_DB_KEY, allMovies);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_popular_movies) {
            if (isNetworkAvailable()) {
                getMovies(POPULAR);
            } else {
                showNetworkError();
            }
            return true;
        } else {
            if (isNetworkAvailable()) {
                getMovies(TOP_RATED);
            } else {
                showNetworkError();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void showLoadingDialog() {
        if (progress == null) {
            progress = new ProgressDialog(this);
            progress.setTitle("Please Wait !!");
            progress.setMessage("Getting Your Data...");
        }
        progress.show();
    }

    public void dismissLoadingDialog() {
        if (progress != null && progress.isShowing()) {
            progress.dismiss();
        }
    }
}