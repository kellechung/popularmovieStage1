import com.example.kellychung.popularmovie.data.MovieContract.movieEntry;
import com.example.kellychung.popularmovie.data.MovieContract.reviewEntry;
import com.example.kellychung.popularmovie.data.MovieContract.videoEntry;
import com.example.kellychung.popularmovie.data.MovieDBHelper;

import java.util.Map;
import java.util.Set;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

/**
 * Created by kellychung on 9/26/15.
 */
public class TestDb extends AndroidTestCase {

    private static final String LOG_TAG = TestDb.class.getSimpleName();

    static void validateCursor(Cursor valueCursor, ContentValues expectedValues) {

        assertTrue(valueCursor.moveToFirst());

        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();

        for (Map.Entry<String, Object> entry : valueSet) {

            int index = valueCursor.getColumnIndex(entry.getKey());
            assertTrue(index != -1);

            String expectedValue = valueCursor.getString(index);
            assertTrue(expectedValue.equalsIgnoreCase(entry.getValue().toString()));
        }

        valueCursor.close();

    }

    static ContentValues createMovieTestData() {

        //Testing movie data insertion in database
        final String test_movie_id = "movie_id";
        final String test_original_title = "movie_title";
        final String test_overview = "movie_overview";
        final String test_posterUrl = "movie_url";
        final String test_releaseDate = "movie_release_date";
        final String test_vote_average = "movie_vote_average";

        ContentValues contentValues = new ContentValues();
        contentValues.put(movieEntry.COLUMN_MOVIE_ID, test_movie_id);
        contentValues.put(movieEntry.COLUMN_ORIGINAL_TITLE, test_original_title);
        contentValues.put(movieEntry.COLUMN_OVERVIEW, test_overview);
        contentValues.put(movieEntry.COLUMN_POSTER_URL, test_posterUrl);
        contentValues.put(movieEntry.COLUMN_RELEASE_DATE, test_releaseDate);
        contentValues.put(movieEntry.COLUMN_VOTE_AVERAGE, test_vote_average);


        return contentValues;
    }

    static ContentValues createVideoTestData() {

        //Data to be used
        final String test_vidMovie_id = "movie_id";
        final String test_first_video = "video1";


        //Inserting a video
        ContentValues contentValues = new ContentValues();
        contentValues.put(videoEntry.COLUMN_MOVIE_ID, test_vidMovie_id);
        contentValues.put(videoEntry.COLUMN_VIDEO_KEYS, test_first_video);

        return contentValues;

    }

    static ContentValues createReviewTestData() {

        //Data to be used
        final String test_review_movieId = "movie_id";
        final String test_review = "review";

        //Inserting a review
        ContentValues contentValues = new ContentValues();
        contentValues.put(reviewEntry.COLUMN_MOVIE_ID, test_review_movieId);
        contentValues.put(reviewEntry.COLUMN_MOVIE_REVIEWS, test_review);

        return contentValues;

    }

    public void testCreateDb() throws Throwable {

        mContext.deleteDatabase(MovieDBHelper.DATABASE_NAME);
        SQLiteDatabase db = new MovieDBHelper(getContext()).getWritableDatabase();

        //Checking if we can create database
        assertEquals(true, db.isOpen());
        db.close();

    }

    public void testInsertReadDb() {

        MovieDBHelper mDbHelper = new MovieDBHelper(getContext());
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Testing movie data insert in table
        ContentValues contentValues = createMovieTestData();
        long rowId = db.insert(movieEntry.TABLE_NAME, null, contentValues);

        //Checking whether data insertion passed
        assertTrue(rowId != -1);

        //Reading and comparing each data individually
        Cursor cursor = db.query(movieEntry.TABLE_NAME, null, null, null, null, null, null);
        validateCursor(cursor, contentValues);


        //Testing video data entry
        ContentValues v1ContentValues = createVideoTestData();
        long firstVidRowId = db.insert(videoEntry.TABLE_NAME, null, v1ContentValues);
        assertTrue(firstVidRowId != -1 && firstVidRowId == 1);

        Cursor videoCursor = db.query(videoEntry.TABLE_NAME, null, null, null, null, null, null);
        validateCursor(videoCursor, v1ContentValues);


        //Testing data insertion in the review rows
        //Next test video table
        ContentValues reviewValues = createReviewTestData();
        long reviewRowId = db.insert(reviewEntry.TABLE_NAME, null, reviewValues);
        assertTrue(reviewRowId != -1);

        Cursor reviewsCursor = db.query(reviewEntry.TABLE_NAME, null, null, null, null, null, null);
        validateCursor(reviewsCursor, reviewValues);

        db.close();
    }



}




