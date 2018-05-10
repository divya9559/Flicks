package com.ajasuja.codepath.flicks.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ajasuja.codepath.flicks.R;
import com.ajasuja.codepath.flicks.model.Movie;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import static butterknife.ButterKnife.bind;
import static org.parceler.Parcels.unwrap;

/**
 * Created by ajasuja on 3/11/17.
 */

public class MovieDetailsActivity extends AppCompatActivity {

    // ----------------- data --------------
    private Movie movie;

    // ----------------- view --------------
    @BindView(R.id.imageViewMovieImage) ImageView imageViewMovieImage;
    @BindView(R.id.textViewMovieTitle) TextView textViewMovieTitle;
    @BindView(R.id.textViewMovieOverview) TextView textViewMovieOverview;
    @BindView(R.id.ratingBarMovieRatings) RatingBar ratingBarMovieRatings;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        // ----------------- view --------------
        bind(this);
        // ----------------- data --------------
        movie = unwrap(getIntent().getParcelableExtra("movie"));
        // ------------- data into view --------------
        Picasso.with(getApplicationContext())
                .load(movie.getBackdropPath("w1280"))
                .transform(new RoundedCornersTransformation(10, 10))
                .into(imageViewMovieImage);
        textViewMovieTitle.setText(movie.getTitle());
        textViewMovieOverview.setText(movie.getOverview());
        ratingBarMovieRatings.setRating((float)movie.getVoteAverage() / 2);
    }
}
