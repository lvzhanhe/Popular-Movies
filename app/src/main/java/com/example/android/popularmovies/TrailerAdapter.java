package com.example.android.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder>{

    private ArrayList<String[]> mTrailerData;
    private final TrailerAdapterOnClickHandler mClickHandler;

    public TrailerAdapter(TrailerAdapter.TrailerAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public interface TrailerAdapterOnClickHandler {
        void onClick(String video);
    }

    public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public final ImageView play_button;
        public final TextView trailer_title;
        public String video_link;

        public TrailerAdapterViewHolder(View itemView) {
            super(itemView);
            play_button = (ImageView) itemView.findViewById(R.id.trailer_icon);
            trailer_title = (TextView) itemView.findViewById(R.id.trailer_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String Video_ID = mTrailerData.get(adapterPosition)[0];
            mClickHandler.onClick(video_link);
        }
    }

    @Override
    public TrailerAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.trailer_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerAdapterViewHolder holder, int position) {
        String[] thisMovieData = mTrailerData.get(position);
        holder.trailer_title.setText(thisMovieData[0]);
        holder.video_link = thisMovieData[1];
    }

    @Override
    public int getItemCount() {
        if (mTrailerData == null) {
            return 0;
        } else {
            return mTrailerData.size();
        }
    }

    public void setTrailerData(ArrayList<String[]> trailerData) {
        mTrailerData = trailerData;
        notifyDataSetChanged();
    }
}
