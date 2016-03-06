package baali.nano;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import java.util.HashSet;

import baali.nano.model.provider.MovieContract;
import baali.nano.utils.provider.MovieProviderUtil;

/**
 * Created by Balaji on 29/02/16.
 */
public class TestDB extends AndroidTestCase
{
    private static final String TAG = TestDB.class.getSimpleName();

    @Override
    protected void setUp() throws Exception
    {
        deleteDataBase();
    }

    public void testCreateDB()
    {
        deleteDataBase();
        SQLiteDatabase db = new MovieProviderUtil(mContext).getWritableDatabase();
        assertEquals("db is not open", true, db.isOpen());

        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);


        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());
        c.close();
        db.close();

    }

    public void testIsMovieTableCreated()
    {
        deleteDataBase();
        SQLiteDatabase db = new MovieProviderUtil(mContext).getWritableDatabase();
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        String movieTableName = "";
        if(c.moveToFirst())
        {

            while (!c.isAfterLast())
            {
                // prints all tables
                Log.d(TAG, "testCreateDB: " + c.getString(0));
                if(c.getString(0).equals(MovieContract.MovieEntry.TABLE_NAME))
                {
                    movieTableName = c.getString(0);
                }
                c.moveToNext();
            }
        }

        assertEquals("Movie Table not found in db", MovieContract.MovieEntry.TABLE_NAME, movieTableName);
        c.close();
        db.close();
    }

    public void testIsAllColumnsCreated()
    {
        deleteDataBase();
        SQLiteDatabase db = new MovieProviderUtil(mContext).getWritableDatabase();
        Cursor c = db.rawQuery("PRAGMA table_info(" + MovieContract.MovieEntry.TABLE_NAME + ")",null);

        HashSet<String> movieColumns = new HashSet<>();
        // adding all column fields into set
        for(String column: MovieContract.MovieEntry.PROJECTION_ALL)
        {
            movieColumns.add(column);
        }
        //initial check
        assertEquals("Size not equal", MovieContract.MovieEntry.PROJECTION_ALL.length, movieColumns.size());
        String movieColumnName;
        if(c.moveToFirst())
        {
            // cursor columns - cid, name, type, notnull, dflt_value, pk
            int columnNameIndex = c.getColumnIndex("name");
            // remove all columns from table, should match to movieColumns set
            while(!c.isAfterLast())
            {
                movieColumnName = c.getString(columnNameIndex);
                movieColumns.remove(movieColumnName);
                c.moveToNext();
            }
        }
        assertTrue("Movie columns set not empty", movieColumns.isEmpty());
        c.close();
        db.close();
    }

    public void testInsertRecordDeadPool()
    {
        deleteDataBase();
        SQLiteDatabase db = new MovieProviderUtil(mContext).getWritableDatabase();
        long insertStatus = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, TestUtils.deadPoolMovieRecords());
        assertTrue(insertStatus != -1);
        // validate data
        Cursor c = db.query(MovieContract.MovieEntry.TABLE_NAME, null, null, null, null, null, null);
        assertTrue(c.moveToFirst());
        String movieTitle = c.getString(c.getColumnIndex(MovieContract.MovieEntry.ORIGINAL_TITLE));
        assertEquals("Title not matches ", TestUtils.deadPoolMovieRecords().get(MovieContract.MovieEntry.ORIGINAL_TITLE)
                .toString(), movieTitle);
        c.close();
        db.close();
    }

    public void testInsertRecordMadMax()
    {
        deleteDataBase();
        SQLiteDatabase db = new MovieProviderUtil(mContext).getWritableDatabase();
        long insertStatus = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, TestUtils.madMaxMovieRecords());
        assertTrue(insertStatus != -1);
        Cursor c = db.query(MovieContract.MovieEntry.TABLE_NAME, null, null, null, null, null, null);
        assertTrue(c.moveToFirst());
        String movieTitle = c.getString(c.getColumnIndex(MovieContract.MovieEntry.ORIGINAL_TITLE));
        assertEquals("Title not matches ", TestUtils.madMaxMovieRecords().get(MovieContract.MovieEntry.ORIGINAL_TITLE)
                .toString(), movieTitle);
        c.close();
        db.close();
    }

    public void testReadMultipleRecords()
    {
        deleteDataBase();
        SQLiteDatabase db = new MovieProviderUtil(mContext).getWritableDatabase();
        db.beginTransaction();
        long madMaxStatus = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, TestUtils.madMaxMovieRecords());
        long deadPoolStatus = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, TestUtils.deadPoolMovieRecords());


        assertTrue("issue on multiple row insertion", madMaxStatus != -1 && deadPoolStatus != -1);

        Cursor c = db.query(MovieContract.MovieEntry.TABLE_NAME, null, null, null, null, null, null);
        assertTrue(c.moveToFirst());
        assertTrue("Row is less than 2", c.getCount() == 2);
        db.endTransaction();
        c.close();
        db.close();
    }

    public void testInsertSameRecordTwice()
    {
        deleteDataBase();
        SQLiteDatabase db = new MovieProviderUtil(mContext).getWritableDatabase();
        long madMaxStatus = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, TestUtils.madMaxMovieRecords());
        // this should not be executed, because movie id is unique in db and throws SQLiteConstraintException.
        // returns -1
        long madMaxStatus2 = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, TestUtils.madMaxMovieRecords());

        assertFalse((madMaxStatus != -1 && madMaxStatus2 != -1));
        db.close();
    }

    public void testDeleteRecord()
    {
        deleteDataBase();
        SQLiteDatabase db = new MovieProviderUtil(mContext).getWritableDatabase();
        long madMaxStatus = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, TestUtils.madMaxMovieRecords());
        if (madMaxStatus != -1)
        {
            Cursor c = db.query(MovieContract.MovieEntry.TABLE_NAME, null, null, null, null, null, null);
            c.moveToFirst();
            int movieId = c.getInt(c.getColumnIndex(MovieContract.MovieEntry.MOVIE_ID));
            assertEquals("Delete movie id not matched..", TestUtils.madMaxMovieRecords().get(MovieContract.MovieEntry
                    .MOVIE_ID), movieId );
            //delete operation start
            int deleteStatus = db.delete(MovieContract.MovieEntry.TABLE_NAME, MovieContract.MovieEntry.MOVIE_ID + "=?",
                    new String[]{String.valueOf(movieId)});
            assertTrue("Data not deleted...", deleteStatus > 0);
            c.close();
        }

        db.close();




    }

    private void deleteDataBase()
    {
        mContext.deleteDatabase(MovieProviderUtil.DATABASE_NAME);
    }
}
