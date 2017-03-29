package in.pankajadhyapak.popularmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import in.pankajadhyapak.popularmovies.R;
import in.pankajadhyapak.popularmovies.activites.MovieDetails;
import in.pankajadhyapak.popularmovies.models.Movie;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private ArrayList<Movie> mMovies;
    private Context mContext;

    public MovieAdapter(Context mContext, ArrayList<Movie> movies) {
        this.mMovies = movies;
        this.mContext = mContext;
        Log.e("Adapter", "MovieAdapter: " + mMovies.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_movie, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Movie movie = mMovies.get(position);
        Log.e("MovieAdapter", "onBindViewHolder: "+ movie.getTitle());
        if(movie.getPosterPath() != null) {
            String posterUrl = "http://image.tmdb.org/t/p/w500/" + movie.getPosterPath();
            Picasso.with(mContext)
                    .load(posterUrl)
                    .placeholder(R.drawable.empty_photo)
                    .error(R.drawable.empty_photo)
                    .into(holder.mPoster);
        } else {
            holder.mPoster.setImageResource(R.drawable.empty_photo);
        }

        holder.mPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MovieDetails.class);
                intent.putExtra("movie_detail", movie);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView mPoster;

        public ViewHolder(View itemView) {
            super(itemView);
            mPoster = (ImageView) itemView.findViewById(R.id.moviePoster);
        }
    }
}
