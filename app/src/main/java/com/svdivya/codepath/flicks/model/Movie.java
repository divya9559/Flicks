package com.ajasuja.codepath.flicks.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ajasuja on 3/10/17.
 */

@Parcel
public class Movie {

//    private static final String IMAGE_ROOT_PATH = "https://image.tmdb.org/t/p/w342";
    private static final String IMAGE_ROOT_PATH = "https://image.tmdb.org/t/p/";
    private static final String POSTER_WIDTH = "w342";
    private static final String BACKDROP_WIDTH = "w780";

    private String id;
    private String posterPath;
    private String backdropPath;
    private String title;
    private String overview;
    private List<String> backdropPaths = new ArrayList<>();
    private double voteAverage;

    public Movie() {
        // empty constructor needed by the Parceler library
    }

    public Movie(JSONObject jsonObject) throws JSONException {
        this.id = jsonObject.getString("id");
        this.posterPath = jsonObject.getString("poster_path");
        this.backdropPath = jsonObject.getString("backdrop_path");
        this.title = jsonObject.getString("original_title");
        this.overview = jsonObject.getString("overview");
        this.voteAverage = jsonObject.getDouble("vote_average");
    }

    public String getId() {
        return id;
    }


    public String getPosterPath() {
        return getPosterPath(POSTER_WIDTH);
    }

    public String getPosterPath(String width) {
        return IMAGE_ROOT_PATH + width + posterPath;
    }

    public String getBackdropPath() {
        return IMAGE_ROOT_PATH + BACKDROP_WIDTH + backdropPath;
    }

    public String getBackdropPath(String width) {
        return IMAGE_ROOT_PATH + width + backdropPath;
    }

    public List<String> getBackdropPaths() {
        List<String> backdropFullPaths = new ArrayList<>();
        for (String backdropPath : backdropPaths) {
            backdropFullPaths.add(getBackdropPath(BACKDROP_WIDTH));
        }
        return backdropFullPaths;
    }

    public void addBackdropPath(String backdropPath) {
        this.backdropPaths.add(backdropPath);
    }

    public void setBackdropPaths(List<String> backdropPaths) {
        this.backdropPaths = backdropPaths;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public static List<Movie> fromJsonArray(JSONArray movieJsonArray) throws JSONException {
        List<Movie> movies = new ArrayList<>();
        for (int i=0; i< movieJsonArray.length(); i++) {
            movies.add(new Movie(movieJsonArray.getJSONObject(i)));
        }
        return movies;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    @Override
    public String toString() {
        return title;
    }
}
