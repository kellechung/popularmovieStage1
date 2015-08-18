package com.example.kellychung.popularmovie;

/**
 * Object movie to store movie info
 */



public class movie {

    String original_title, overview, posterUrl, release_Date, vote_average;

    public movie(String original_title, String posterUrl, String overview, String releaseDate, String vote_average) {

        this.original_title = original_title;
        this.overview = overview;
        this.posterUrl = posterUrl;
        this.release_Date = releaseDate;
        this.vote_average = vote_average;
    }
}
