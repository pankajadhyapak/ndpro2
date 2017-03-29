package in.pankajadhyapak.popularmovies.activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.pankajadhyapak.popularmovies.models.Movie;
import in.pankajadhyapak.popularmovies.R;

public class MovieDetails extends AppCompatActivity {

    private static final String TAG = "MovieDetails";
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

        if(movie.getPoster_path() != null) {
            String posterUrl = "http://image.tmdb.org/t/p/w500/" + movie.getPoster_path();
            Picasso.with(this)
                    .load(posterUrl)
                    .placeholder(R.drawable.empty_photo)
                    .error(R.drawable.empty_photo)
                    .into(imageView);
        } else {
            imageView.setImageResource(R.drawable.empty_photo);
        }

        ratingValue.setText(movie.getVotes());
        summary.setText(movie.getOverview());
        releaseValue.setText(movie.getRelease_date());
    }

    @OnClick(R.id.fab)
    public void onViewClicked(View v) {
        Snackbar.make(v, "Favourite this in part 2", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
