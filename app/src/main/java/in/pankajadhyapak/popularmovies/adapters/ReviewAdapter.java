package in.pankajadhyapak.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import in.pankajadhyapak.popularmovies.R;
import in.pankajadhyapak.popularmovies.models.Review;



public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private ArrayList<Review> mTrailers;
    private Context mContext;

    public ReviewAdapter(Context mContext, ArrayList<Review> movies) {
        this.mTrailers = movies;
        this.mContext = mContext;
        Log.e("Adapter", "ReviewAdapter: " + mTrailers.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_review, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Review trailer = mTrailers.get(position);
        Log.e("ReviewAdapter", "onBindViewHolder: "+ trailer.getAuthor());
        holder.mAuthor.setText(trailer.getAuthor()+" says...");
        holder.mReview.setText(trailer.getContent());
    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView mAuthor;
        TextView mReview;

        public ViewHolder(View itemView) {
            super(itemView);
            mAuthor = (TextView) itemView.findViewById(R.id.reviewAuthor);
            mReview = (TextView) itemView.findViewById(R.id.reviewText);
        }
    }
}
