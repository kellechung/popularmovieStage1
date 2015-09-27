package com.example.kellychung.popularmovie.data;

import android.provider.BaseColumns;

/**
 * Created by kellychung on 9/24/15.
 */
public class MovieContract {


    //Setting the content authority to be equal to package name
    public static final String CONTENT_AUTHORITY = "com.example.kellychung.popularmovie";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact

    public static final android.net.Uri BASE_CONTENT_URI = android.net.Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_FAV_MOVIES = "fav_movies";


    //Building the movie table for database to store favorite movies info locally

    public class movieEntry implements BaseColumns {

        public static final String TABLE_NAME = "movie";

        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static final String COLUMN_ORIGINAL_TITLE = "original_title";

        public static final String COLUMN_OVERVIEW = "overview";

        public static final String COLUMN_POSTER_URL = "posterUrl";

        public static final String COLUMN_RELEASE_DATE = "releaseDate";

        public static final String COLUMN_VOTE_AVERAGE = "vote_average";


    }

    //Building a separate table to store movie videos. This table will join movie table through movie_id.
    //Using separate tables keep data normalized

    public class videoEntry implements BaseColumns {

        public static final String TABLE_NAME = "movieVideos";

        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static final String COLUMN_VIDEO_KEYS = "movieVideoKeys";

    }

    //Building a separate table to store movie reviews. Join movie table through movie id

    public class reviewEntry implements BaseColumns {

        public static final String TABLE_NAME = "movieReviews";

        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static final String COLUMN_MOVIE_REVIEWS = "movieReviews";
    }

}








