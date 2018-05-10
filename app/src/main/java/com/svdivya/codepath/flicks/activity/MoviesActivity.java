package com.ajasuja.codepath.flicks.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ajasuja.codepath.flicks.R;
import com.ajasuja.codepath.flicks.adapter.MoviesAdapter;
import com.ajasuja.codepath.flicks.model.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cz.msebera.android.httpclient.Header;

import static butterknife.ButterKnife.bind;
import static com.ajasuja.codepath.flicks.model.Movie.fromJsonArray;

public class MoviesActivity extends AppCompatActivity {

    //data
    private List<Movie> movies;

    //adapter
    private MoviesAdapter moviesAdapter;

    //view
    @BindView(R.id.listViewMovies) ListView moviesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        // init data
        this.movies = new ArrayList<>();
        fetchMovies();

        bind(this);
        // init adapter
        moviesAdapter = new MoviesAdapter(this, movies);

        // attach data & view via adapter
        moviesListView.setAdapter(moviesAdapter);

        // move to other activity
        moviesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("movie details for postion ... " + position);
                final Movie movie = movies.get(position);
                AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                String movieImagesUrl = String.format("https://api.themoviedb.org/3/movie/%s/images?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed", movie.getId());
                asyncHttpClient.get(movieImagesUrl, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            JSONArray backdropsJsonArray = response.getJSONArray("backdrops");
                            for (int i=0; i< backdropsJsonArray.length(); i++) {
                                JSONObject backdropJsonObject = backdropsJsonArray.getJSONObject(i);
                                movie.addBackdropPath(backdropJsonObject.getString("file_path"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }
                });
                Intent movie2MovieDetailsIntent = new Intent(MoviesActivity.this, MovieDetailsActivity.class);
                movie2MovieDetailsIntent.putExtra("movie", Parcels.wrap(movie));
                startActivity(movie2MovieDetailsIntent);
            }
        });
    }

    private void fetchMovies() {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        String moviesUrl= "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
        asyncHttpClient.get(moviesUrl, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                JSONArray moviesJsonArray = null;
                try {
                    moviesJsonArray = response.getJSONArray("results");
                    movies.addAll(fromJsonArray(moviesJsonArray));
                    moviesAdapter.notifyDataSetChanged();
                    Log.d("DEBUG", moviesJsonArray.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });

    }
}
