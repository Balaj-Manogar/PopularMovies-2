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
    }

    private void deleteDataBase()
    {
        mContext.deleteDatabase(MovieProviderUtil.DATABASE_NAME);
    }
}
