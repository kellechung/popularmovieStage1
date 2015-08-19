package com.example.kellychung.popularmovie;

/**
 * Object movie to store movie info
 */
public class movie implements android.os.Parcelable {

    protected movie(android.os.Parcel in) {
        original_title = in.readString();
        overview = in.readString();
        posterUrl = in.readString();
        release_Date = in.readString();
        vote_average = in.readString();
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
        dest.writeString(original_title);
        dest.writeString(overview);
        dest.writeString(posterUrl);
        dest.writeString(release_Date);
        dest.writeString(vote_average);
    }

    String original_title, overview, posterUrl, release_Date, vote_average;

    public movie(String original_title, String posterUrl, String overview, String releaseDate, String vote_average) {

        this.original_title = original_title;
        this.overview = overview;
        this.posterUrl = posterUrl;
        this.release_Date = releaseDate;
        this.vote_average = vote_average;
    }
}
