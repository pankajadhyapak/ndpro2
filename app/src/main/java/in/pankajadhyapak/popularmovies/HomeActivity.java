package in.pankajadhyapak.popularmovies;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity {

    private static final String API_URL = "http://api.themoviedb.org/3/discover/";
    private static final String MOVIE_DB_KEY = "movieList";
    private static final String TAG = HomeActivity.class.getSimpleName();
    public static final String POPULARITY_DESC = "popularity.desc";
    public static final String VOTE_AVERAGE_DESC = "vote_average.desc";

    @Bind(R.id.rv_list)
    RecyclerView movieRecylerView;

    private ArrayList<Movie> allMovies = new ArrayList<>();
    private RecyclerView.Adapter mMovieAdapter;
    private ProgressDialog progress;

    public void showLoadingDialog() {

        if (progress == null) {
            progress = new ProgressDialog(this);
            progress.setTitle("Please Wait !!");
            progress.setMessage("Getting Data");
        }
        progress.show();
    }

    public void dismissLoadingDialog() {

        if (progress != null && progress.isShowing()) {
            progress.dismiss();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        movieRecylerView.setLayoutManager(new GridLayoutManager(this, 2));
        movieRecylerView.setHasFixedSize(true);


        if(savedInstanceState == null || !savedInstanceState.containsKey(MOVIE_DB_KEY)) {
            Log.d(TAG,"MovieList not available in instancestate");
            if(isNetworkAvailable()) {
                getMovies(POPULARITY_DESC);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Cannot fetch movies")
                        .setMessage("Please connect to wifi or enable cellular data!")
                        .create()
                        .show();
            }
        }
        else {
            allMovies.clear();
            allMovies = savedInstanceState.getParcelableArrayList(MOVIE_DB_KEY);
            mMovieAdapter = new MovieAdapter(HomeActivity.this, allMovies);
            movieRecylerView.setAdapter(mMovieAdapter);
            mMovieAdapter.notifyDataSetChanged();
            Log.d(TAG,"MovieList retrieved with size : " + allMovies.size());
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void getMovies(String sort) {
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
        Call<ApiResponse> call = api.getMovies(sort, "5c31fe4e68537ffcffdacd5bf0e89ca3");

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Log.e("Inresponse", "onResponse: " + response.body().getResults().size());
                allMovies.clear();
                allMovies.addAll(response.body().getResults());
                mMovieAdapter = new MovieAdapter(HomeActivity.this, allMovies);
                movieRecylerView.setAdapter(mMovieAdapter);
                mMovieAdapter.notifyDataSetChanged();
                dismissLoadingDialog();
                Log.e("Inresponse", "all movies size: " + allMovies.size());
                updateUi();
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("onFailure", "onFailure: " + t.getMessage());
                dismissLoadingDialog();
            }
        });
    }

    private void updateUi() {
        Log.e(TAG, "updateUi: ");
        mMovieAdapter.notifyDataSetChanged();
        mMovieAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            mMovieAdapter.notifyDataSetChanged();
            return true;
        }

        if (id == R.id.action_popular_movies) {
            if(isNetworkAvailable()) {
                getMovies(POPULARITY_DESC);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Cannot fetch movies")
                        .setMessage("Please connect to wifi or enable cellular data!")
                        .create()
                        .show();
            }
            return true;
        } else {
            if(isNetworkAvailable()) {
                getMovies(VOTE_AVERAGE_DESC);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Cannot fetch movies")
                        .setMessage("Please connect to wifi or enable cellular data!")
                        .create()
                        .show();
            }
        }

        return super.onOptionsItemSelected(item);
    }
}