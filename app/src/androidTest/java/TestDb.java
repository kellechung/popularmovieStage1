import com.example.kellychung.popularmovie.data.MovieContract.movieEntry;
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

        //Next test video table
        



        db.close();
    }


}

