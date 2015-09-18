package com.example.kellychung.popularmovie;

/**
 * Object movie to store movie info
 */
public class movie implements android.os.Parcelable {

    protected movie(android.os.Parcel in) {
        movieID = in.readString();
        original_title = in.readString();
        overview = in.readString();
        posterUrl = in.readString();
        release_Date = in.readString();
        vote_average = in.readString();
        movieVideoKeys = in.createStringArray();
        movieReviews = in.createStringArray();



    }

    public static final Creator<movie> CREATOR = new android.os.Parcelable.Creator<movie>() {
        @Override
        public movie createFromParcel(android.os.Parcel in) {
            return new movie(in);
        }

        @Override
        public movie[] newArray(int size) {
            return new movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(movieID);
        dest.writeString(original_title);
        dest.writeString(overview);
        dest.writeString(posterUrl);
        dest.writeString(release_Date);
        dest.writeString(vote_average);
        dest.writeStringArray(movieVideoKeys);
        dest.writeStringArray(movieReviews);
    }

    String original_title, overview, posterUrl, release_Date, vote_average, movieID;
    String[] movieVideoKeys, movieReviews;



    public movie(String movieID, String original_title, String posterUrl, String overview, String releaseDate, String vote_average, String[] movieVideoKeys, String[] movieReviews)
    {
        this.movieID = movieID;
        this.original_title = original_title;
        this.overview = overview;
        this.posterUrl = posterUrl;
        this.release_Date = releaseDate;
        this.vote_average = vote_average;
        this.movieVideoKeys = movieVideoKeys;
        this.movieReviews = movieReviews;}

    public String getPosterUrl() {return this.posterUrl;}

    public void setMovieVideoKey(String[] movieVideoKeys) {

        this.movieVideoKeys = movieVideoKeys;
    }

    public void setMovieReviews(String[] movieReviews) {
        this.movieReviews = movieReviews;
    }
}
