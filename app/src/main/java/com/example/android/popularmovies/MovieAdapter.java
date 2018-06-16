package com.example.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder>{

    private ArrayList<String[]> mMovieData;
    private final MovieAdapterOnClickHandler mClickHandler;

    public interface MovieAdapterOnClickHandler {
        void onClick(String[] AMovie);
    }

    public MovieAdapter(MovieAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mMovieTextViewU;
        public final TextView mMovieTextViewL;
        public final ImageView mMovieImageView;

        public MovieAdapterViewHolder(View view) {
            super(view);
            mMovieImageView = (ImageView) view.findViewById(R.id.movie_image_view);
            mMovieTextViewU = (TextView) view.findViewById(R.id.name_view);
            mMovieTextViewL = (TextView) view.findViewById(R.id.description_view);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String[] Movie_ID = mMovieData.get(adapterPosition);
            mClickHandler.onClick(Movie_ID);
        }
    }

    @Override
    public MovieAdapter.MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapter.MovieAdapterViewHolder holder, int position) {
        String[] thisMovieData = mMovieData.get(position);
        holder.mMovieTextViewU.setText(thisMovieData[3]);
        holder.mMovieTextViewL.setText(thisMovieData[11]);
        Picasso.get()
                .load(thisMovieData[5])
                .into(holder.mMovieImageView);
    }

    @Override
    public int getItemCount() {
        if (mMovieData == null) return 0;
        return mMovieData.size();
    }

    public void setMovieData(ArrayList<String[]> movieData) {
        mMovieData = movieData;
        notifyDataSetChanged();
    }
}
