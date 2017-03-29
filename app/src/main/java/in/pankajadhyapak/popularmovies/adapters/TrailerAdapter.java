package in.pankajadhyapak.popularmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import in.pankajadhyapak.popularmovies.R;
import in.pankajadhyapak.popularmovies.models.Trailer;


public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    private ArrayList<Trailer> mTrailers;
    private Context mContext;

    public TrailerAdapter(Context mContext, ArrayList<Trailer> movies) {
        this.mTrailers = movies;
        this.mContext = mContext;
        Log.e("TrailerAdapter", "MovieAdapter: " + mTrailers.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_trailer, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Trailer trailer = mTrailers.get(position);
        Log.e("TrailerAdapter", "onBindViewHolder: "+ trailer.getName());
        holder.mTrailerName.setText(trailer.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + mTrailers.get(holder.getAdapterPosition()).getKey()));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView mTrailerName;

        public ViewHolder(View itemView) {
            super(itemView);
            mTrailerName = (TextView) itemView.findViewById(R.id.trailerName);
        }
    }
}
