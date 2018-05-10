package com.ajasuja.codepath.flicks.adapter;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ajasuja.codepath.flicks.R;
import com.ajasuja.codepath.flicks.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import static com.ajasuja.codepath.flicks.R.id.textViewMovieOverview;
import static com.ajasuja.codepath.flicks.R.id.textViewMovieTitle;

/**
 * Created by ajasuja on 3/10/17.
 */

public class MoviesAdapter extends ArrayAdapter<Movie> {

    private boolean isLandscape = false;

    private static class NonPopularViewHolder {
        private ImageView imageViewMovieImage;
        private TextView textViewMovieTitle;
        private TextView textViewMovieOverview;
    }

    private static class PopularMovieViewHolder {
        private ImageView imageViewMovieImage;
    }

    public MoviesAdapter(Context context, List<Movie> movies) {
        super(context, android.R.layout.simple_list_item_1, movies);
        checkOrientation();
    }

    private void checkOrientation() {
        int orientation = getContext().getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            isLandscape = false;
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            isLandscape = true;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2; // popular and non-popular
    }

    @Override
    public int getItemViewType(int position) {
        Movie movie = getItem(position);
        return isPopular(movie) ?  0 : 1;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie = getItem(position);
        NonPopularViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            if (getItemViewType(position) == 0) {
                convertView = inflater.inflate(R.layout.item_movie_popular, parent, false);
                PopularMovieViewHolder popularMovieViewHolder = new PopularMovieViewHolder();
                popularMovieViewHolder.imageViewMovieImage = (ImageView) convertView.findViewById(R.id.imageViewMovieImage);
                convertView.setTag(popularMovieViewHolder);
            } else {
                convertView = inflater.inflate(R.layout.item_movie, parent, false);
                viewHolder = new NonPopularViewHolder();
                viewHolder.imageViewMovieImage = (ImageView) convertView.findViewById(R.id.imageViewMovieImage);
                viewHolder.textViewMovieTitle = (TextView) convertView.findViewById(textViewMovieTitle);
                viewHolder.textViewMovieOverview = (TextView) convertView.findViewById(textViewMovieOverview);
                convertView.setTag(viewHolder);
            }
        } else {
            if (getItemViewType(position) == 0) {
                PopularMovieViewHolder popularMovieViewHolder = (PopularMovieViewHolder) convertView.getTag();
//                popularMovieViewHolder.imageViewMovieImage.setImageResource(0);
                if (isLandscape) {
                    Picasso.with(getContext())
                            .load(movie.getBackdropPath("original"))
                            .transform(new RoundedCornersTransformation(10, 10))
                            .into(popularMovieViewHolder.imageViewMovieImage);
                } else {
                    Picasso.with(getContext())
                            .load(movie.getBackdropPath("w1280"))
                            .transform(new RoundedCornersTransformation(10, 10))
                            .into(popularMovieViewHolder.imageViewMovieImage);
                }
            } else {
                viewHolder = (NonPopularViewHolder) convertView.getTag();
//                viewHolder.imageViewMovieImage.setImageResource(0); // clear out image from convertview
                if (isLandscape) {
                    Picasso.with(getContext())
                            .load(movie.getBackdropPath())
                            .transform(new RoundedCornersTransformation(10, 10))
                            .into(viewHolder.imageViewMovieImage);
                } else {
                    Picasso.with(getContext())
                            .load(movie.getPosterPath())
                            .transform(new RoundedCornersTransformation(10, 10))
                            .into(viewHolder.imageViewMovieImage);
                }
                viewHolder.textViewMovieTitle.setText(movie.getTitle());
                viewHolder.textViewMovieOverview.setText(movie.getOverview());
            }
        }
        return convertView;
    }

    private boolean isPopular(Movie movie) {
        return movie.getVoteAverage() > 7;
    }
}
