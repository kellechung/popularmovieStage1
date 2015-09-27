package com.example.kellychung.popularmovie.data;

import com.example.kellychung.popularmovie.data.MovieContract.movieEntry;
import com.example.kellychung.popularmovie.data.MovieContract.reviewEntry;
import com.example.kellychung.popularmovie.data.MovieContract.videoEntry;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kellychung on 9/26/15.
 */
public class MovieDBHelper extends SQLiteOpenHelper {

    //Setting the database version for now to be 1
    private static final int DATABASE_VERSION = 1;

    //This will be name of database used to store the favorite movie locally
    public static final String DATABASE_NAME = "movie.db";

    //Using the super constructor for our database and passing in movie parameters
    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //Building the movie table using a sql create statement inside a string.


        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + movieEntry.TABLE_NAME + " (" +

                //Using movie ID as primary key
                movieEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                movieEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, " +
                movieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                movieEntry.COLUMN_POSTER_URL + " TEXT NOT NULL, " +
                movieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                movieEntry.COLUMN_VOTE_AVERAGE + " TEXT NOT NULL" + " );";


        // Building the movieVideo table using a sql create statement inside a string.
        // It joins movie table through foreign key movie ID. Every row must be unique (movieID, videoKey)


        final String SQL_CREATE_VIDEO_TABLE = "CREATE TABLE " + videoEntry.TABLE_NAME + " ( " +

                videoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                videoEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                videoEntry.COLUMN_VIDEO_KEYS + " TEXT NOT NULL, " +
                "FOREIGN KEY (" + videoEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
                movieEntry.TABLE_NAME + " (" + movieEntry.COLUMN_MOVIE_ID + ") ON DELETE CASCADE) ;";



        //Building the movieReview table using a sql create statement inside a string.
        //Joins the movie table through foreign movie ID.
        //Here there might be identical reviews so rows might not be unique

        final String SQL_CREATE_REVIEW_TABLE = "CREATE TABLE " + reviewEntry.TABLE_NAME + " ( " +

                reviewEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                reviewEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                reviewEntry.COLUMN_MOVIE_REVIEWS + " TEXT NOT NULL, " +
                "FOREIGN KEY (" + reviewEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
                movieEntry.TABLE_NAME + " (" + movieEntry.COLUMN_MOVIE_ID + ") ON DELETE CASCADE) ;";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
        db.execSQL(SQL_CREATE_VIDEO_TABLE);
        db.execSQL(SQL_CREATE_REVIEW_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        final String DROP_STMT = "DROP TABLE IF EXISTS ";
        db.execSQL(DROP_STMT + movieEntry.TABLE_NAME);
        db.execSQL(DROP_STMT + videoEntry.TABLE_NAME);
        db.execSQL(DROP_STMT + reviewEntry.TABLE_NAME);
        onCreate(db);
    }
}
