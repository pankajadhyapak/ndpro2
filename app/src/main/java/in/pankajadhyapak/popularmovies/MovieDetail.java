package in.pankajadhyapak.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MovieDetail extends AppCompatActivity {

    private static final String TAG = MovieDetail.class.getSimpleName();
    @Bind(R.id.imageView2)
    ImageView imageView2;

    @Bind(R.id.rating)
    TextView rating;

    @Bind(R.id.releaseDate)
    TextView releaseDate;

    @Bind(R.id.synopsis)
    TextView synopsis;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Movie movie = (Movie) intent.getParcelableExtra("movie_detail");
        Log.e(TAG, "onCreate: " + movie.getTitle());

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(movie.getTitle());
        }

        if(movie.getPoster_path() != null) {
            String posterUrl = "http://image.tmdb.org/t/p/w500/" + movie.getPoster_path();
            Picasso.with(this).load(posterUrl)
                    .into(imageView2);
        } else {
            imageView2.setImageResource(R.drawable.empty_photo);
        }

        rating.setText(movie.getVotes());
        synopsis.setText(movie.getOverview());
        releaseDate.setText(movie.getRelease_date());

    }
}
