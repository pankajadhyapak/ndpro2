package in.pankajadhyapak.popularmovies.activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.pankajadhyapak.popularmovies.R;
import in.pankajadhyapak.popularmovies.adapters.ReviewAdapter;
import in.pankajadhyapak.popularmovies.adapters.TrailerAdapter;
import in.pankajadhyapak.popularmovies.api.MovieApi;
import in.pankajadhyapak.popularmovies.models.AllReviews;
import in.pankajadhyapak.popularmovies.models.AllTrailers;
import in.pankajadhyapak.popularmovies.models.Movie;
import in.pankajadhyapak.popularmovies.models.Review;
import in.pankajadhyapak.popularmovies.models.Trailer;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDetails extends AppCompatActivity {

    private static final String TAG = "MovieDetails";

    private static final String API_URL = "http://api.themoviedb.org/3/movie/";

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.fab)
    FloatingActionButton fab;

    @Bind(R.id.imageView)
    ImageView imageView;

    @Bind(R.id.summary)
    TextView summary;

    @Bind(R.id.ratingValue)
    TextView ratingValue;

    @Bind(R.id.releaseValue)
    TextView releaseValue;

    @Bind(R.id.trailer_rv)
    RecyclerView trailerRv;

    @Bind(R.id.review_rv)
    RecyclerView reviewRv;

    private ArrayList<Trailer> allTrailer = new ArrayList<>();
    private RecyclerView.Adapter mTrailerAdapter;

    private ArrayList<Review> allReviews = new ArrayList<>();
    private RecyclerView.Adapter mReviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Movie movie = (Movie) intent.getParcelableExtra("movie_detail");
        Log.e(TAG, "onCreate: " + movie.getTitle());

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(movie.getTitle());
        }

        if (movie.getPosterPath() != null) {
            String posterUrl = "http://image.tmdb.org/t/p/w500/" + movie.getPosterPath();
            Picasso.with(this)
                    .load(posterUrl)
                    .placeholder(R.drawable.empty_photo)
                    .error(R.drawable.empty_photo)
                    .into(imageView);
        } else {
            imageView.setImageResource(R.drawable.empty_photo);
        }

        ratingValue.setText(String.format("Rating : %s", movie.getVoteAverage()));
        summary.setText(movie.getOverview());
        releaseValue.setText(String.format("Release : %s", movie.getReleaseDate()));

        trailerRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mTrailerAdapter = new TrailerAdapter(this, allTrailer);
        trailerRv.setAdapter(mTrailerAdapter);

        reviewRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        reviewRv.setNestedScrollingEnabled(false);
        mReviewAdapter = new ReviewAdapter(this, allReviews);
        reviewRv.setAdapter(mReviewAdapter);

        getTrailers(movie.getId());
        getReviews(movie.getId());
    }

    private void getReviews(Integer id) {
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
        Call<AllReviews> call = api.getReviews(id, getString(R.string.api_key));
        call.enqueue(new Callback<AllReviews>() {
            @Override
            public void onResponse(Call<AllReviews> call, Response<AllReviews> response) {
                Log.e(TAG, "onResponse: " + response.body().getResults().size());
                allReviews.clear();
                allReviews.addAll(response.body().getResults());
                mReviewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<AllReviews> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void getTrailers(Integer id) {
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
        Call<AllTrailers> call = api.getTrailers(id, getString(R.string.api_key));
        call.enqueue(new Callback<AllTrailers>() {
            @Override
            public void onResponse(Call<AllTrailers> call, Response<AllTrailers> response) {
                Log.e(TAG, "onResponse: " + response.body().getResults().size());
                allTrailer.clear();
                allTrailer.addAll(response.body().getResults());
                mTrailerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<AllTrailers> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    @OnClick(R.id.fab)
    public void onViewClicked(View v) {
        Snackbar.make(v, "Favourite this in part 2", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
