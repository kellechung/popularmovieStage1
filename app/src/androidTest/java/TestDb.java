import com.example.kellychung.popularmovie.data.MovieContract.movieEntry;
import com.example.kellychung.popularmovie.data.MovieContract.videoEntry;
import com.example.kellychung.popularmovie.data.MovieContract.reviewEntry;
import com.example.kellychung.popularmovie.data.MovieDBHelper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

/**
 * Created by kellychung on 9/26/15.
 */
public class TestDb extends AndroidTestCase {

    private static final String LOG_TAG = TestDb.class.getSimpleName();

    public void testCreateDb() throws Throwable {

        mContext.deleteDatabase(MovieDBHelper.DATABASE_NAME);
        SQLiteDatabase db = new MovieDBHelper(getContext()).getWritableDatabase();

        //Checking if we can create database
        assertEquals(true, db.isOpen());
        db.close();

    }

    public void testInsertReadDb() {

        //Testing movie data insertion in database
        final String test_movie_id = "movie_id";
        final String test_original_title = "movie_title";
        final String test_overview = "movie_overview";
        final String test_posterUrl = "movie_url";
        final String test_releaseDate = "movie_release_date";
        final String test_vote_average = "movie_vote_average";

        MovieDBHelper mDbHelper = new MovieDBHelper(getContext());
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(movieEntry.COLUMN_MOVIE_ID, test_movie_id);
        contentValues.put(movieEntry.COLUMN_ORIGINAL_TITLE, test_original_title);
        contentValues.put(movieEntry.COLUMN_OVERVIEW, test_overview);
        contentValues.put(movieEntry.COLUMN_POSTER_URL, test_posterUrl);
        contentValues.put(movieEntry.COLUMN_RELEASE_DATE, test_releaseDate);
        contentValues.put(movieEntry.COLUMN_VOTE_AVERAGE, test_vote_average);

        long rowId;
        rowId = db.insert(movieEntry.TABLE_NAME, null, contentValues);

        //Checking whether data insertion passed
        assertTrue(rowId != -1);

        //Checking whether data entered is correct using a query statement

        String[] movieColumns = {movieEntry.COLUMN_MOVIE_ID,
                movieEntry.COLUMN_ORIGINAL_TITLE,
                movieEntry.COLUMN_OVERVIEW,
                movieEntry.COLUMN_POSTER_URL,
                movieEntry.COLUMN_RELEASE_DATE,
                movieEntry.COLUMN_VOTE_AVERAGE};

        //Reading and comparing each data individually
        Cursor cursor = db.query(movieEntry.TABLE_NAME, movieColumns, null, null, null, null, null);

        if (cursor.moveToFirst()) {

            for (int i = 0; i < movieColumns.length; i++) {

                int colIndex = cursor.getColumnIndex(movieColumns[i]);
                String movieColVal = cursor.getString(colIndex);
                assertEquals(contentValues.get(movieColumns[i]), movieColVal);


            }
        } else {
            fail("Empty cursor, Null first row");
        }

        cursor.close();

        //Next test video table
        final String test_vidMovie_id = "movie_id";
        final String test_first_video = "video1";
        final String test_second_video = "video1";

        //Testing multiple row entries for movies

        //Inserting a first video for a movie
        ContentValues v1ContentValues = new ContentValues();
        v1ContentValues.put(videoEntry.COLUMN_MOVIE_ID, test_vidMovie_id);
        v1ContentValues.put(videoEntry.COLUMN_VIDEO_KEYS, test_first_video);

        long firstVidRowId;
        firstVidRowId = db.insert(videoEntry.TABLE_NAME, null, v1ContentValues);
        assertTrue(firstVidRowId != -1 && firstVidRowId == 1);

        //inserting a second video for the same movie
        ContentValues v2ContentValues = new ContentValues();
        v2ContentValues.put(videoEntry.COLUMN_MOVIE_ID, test_vidMovie_id);
        v2ContentValues.put(videoEntry.COLUMN_VIDEO_KEYS, test_second_video);

        long secondVidRowId;
        secondVidRowId = db.insert(videoEntry.TABLE_NAME, null, v2ContentValues);
        assertTrue(secondVidRowId != -1 && secondVidRowId == 2);

        //Checking the data in the video table
        String[] videoColumns = {videoEntry.COLUMN_MOVIE_ID, videoEntry.COLUMN_VIDEO_KEYS};
        Cursor videoCursor = db.query(videoEntry.TABLE_NAME,videoColumns, null, null, null, null, null);



            if (!videoCursor.moveToFirst()) {
                fail("No video data returned!");
            }

            else {
                int colIndex = videoCursor.getColumnIndex(videoColumns[0]);
                String videoColVal = videoCursor.getString(colIndex);
                assertEquals(v1ContentValues.get(videoColumns[0]), videoColVal);
            }

        if (!videoCursor.moveToLast()) {
            fail("No video data returned!");
        }

        else {
            int colIndex = videoCursor.getColumnIndex(videoColumns[1]);
            String videoColVal = videoCursor.getString(colIndex);
            assertEquals(v2ContentValues.get(videoColumns[1]), videoColVal);
        }

        videoCursor.close();

        //Testing data insertion in the review rows
        //Next test video table
        final String test_review_movieId= "movie_id";
        final String test_review = "review";

        String[] reviewColumns = {reviewEntry.COLUMN_MOVIE_ID, reviewEntry.COLUMN_MOVIE_REVIEWS};
        ContentValues reviewValues = new ContentValues();
        reviewValues.put(reviewEntry.COLUMN_MOVIE_ID, test_review_movieId);
        reviewValues.put(reviewEntry.COLUMN_MOVIE_REVIEWS, test_review);


        long reviewRowId = db.insert(reviewEntry.TABLE_NAME, null, reviewValues);
        assertTrue(reviewRowId != -1);

        Cursor reviewsCursor = db.query(reviewEntry.TABLE_NAME, reviewColumns, null, null, null, null, null);

        if (!reviewsCursor.moveToFirst())  {fail("No review data is returned");}


        else {

            for (int i = 0; i < reviewColumns.length; i++) {

                int colIndex = reviewsCursor.getColumnIndex(reviewColumns[i]);
                String reviewColVal = reviewsCursor.getString(colIndex);
                assertEquals(reviewValues.get(reviewColumns[i]), reviewColVal);

            }

            reviewsCursor.close();

        }


        db.close();
    }


}

