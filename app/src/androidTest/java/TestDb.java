import com.example.kellychung.popularmovie.data.MovieDBHelper;

import android.test.AndroidTestCase;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by kellychung on 9/26/15.
 */
public class TestDb extends AndroidTestCase {

    //private static final String LOG_TAG = TestDb.class.getSimpleName();

    public void testCreateDb() throws Throwable {

        mContext.deleteDatabase(MovieDBHelper.DATABASE_NAME);
        SQLiteDatabase db = new MovieDBHelper(getContext()).getWritableDatabase();

        //Checking if we can create database
        assertEquals(true, db.isOpen());
        db.close();


    }
}
