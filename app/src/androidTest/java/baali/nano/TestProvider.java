package baali.nano;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

import baali.nano.model.provider.MovieContract;
import baali.nano.model.provider.MovieContract.MovieEntry;
import baali.nano.services.provider.MovieProvider;
import baali.nano.utils.provider.MovieProviderUtil;

/**
 * Created by Balaji on 02/03/16.
 */
public class TestProvider extends AndroidTestCase
{

    private static final String TAG = TestProvider.class.getSimpleName();


    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        deleteAllRecordsFromProvider();
    }

    public void testProviderIsRegistered()
    {
        PackageManager pm = mContext.getPackageManager();
        ComponentName componentName = new ComponentName(mContext.getPackageName(), MovieProvider.class.getName());

        try
        {
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);
            assertEquals("Error: Registered authority is: " + providerInfo.authority, MovieContract.CONTENT_AUTHORITY,
                    providerInfo.authority);
        }
        catch (PackageManager.NameNotFoundException e)
        {
            assertTrue("Error: MovieProvider not registered at " + mContext.getPackageName(),
                    false);
            e.printStackTrace();
        }
    }

    public void testMovieGetType()
    {
        // content://baali.nano/movie/
        String type = mContext.getContentResolver().getType(MovieEntry.CONTENT_URI);
        assertEquals("Not returned expected uri", MovieEntry.CONTENT_TYPE, type);
    }

    public void testMovieWithIdGetType()
    {
        // content://baali.nano/movie/1234
        String id = "1234";
        String type = mContext.getContentResolver().getType(MovieEntry.buildMovieUri(id));
        assertEquals("Not returned expected uri", MovieEntry.CONTENT_ITEM_TYPE, type);
    }

    public void testQueryAllMovies()
    {
        deleteAllRecordsFromProvider();
        MovieProviderUtil dbHelper = new MovieProviderUtil(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues deadPoolValues = TestUtils.deadPoolMovieRecords();
        ContentValues madMaxValues = TestUtils.madMaxMovieRecords();

        Uri deadPoolUri = mContext.getContentResolver().insert(MovieEntry.CONTENT_URI, deadPoolValues);
        Uri madMaxUri = mContext.getContentResolver().insert(MovieEntry.CONTENT_URI, madMaxValues);

        long deadPoolStatus = ContentUris.parseId(deadPoolUri);
        long madMaxStatus = ContentUris.parseId(madMaxUri);

        assertTrue(deadPoolStatus != -1 && madMaxStatus != -1);

        Cursor cursor = mContext.getContentResolver().query(MovieEntry.CONTENT_URI, null, null, null, null);
        assertEquals("Inserted 2 records not found.", 2, cursor.getCount());

        cursor.close();
        db.close();
    }

    public void testQuerySingleMovie()
    {
        deleteAllRecordsFromProvider();
        MovieProviderUtil dbHelper = new MovieProviderUtil(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues deadPoolValues = TestUtils.deadPoolMovieRecords();
        Uri deadPoolUri = mContext.getContentResolver().insert(MovieEntry.CONTENT_URI, deadPoolValues);
        long deadPoolStatus = ContentUris.parseId(deadPoolUri);
        String movieId = deadPoolValues.getAsString(MovieEntry.MOVIE_ID);
        assertTrue(deadPoolStatus != -1);

        Cursor cursor = mContext.getContentResolver().query(deadPoolUri, MovieEntry.PROJECTION_ALL, null,
                new String[]{movieId},
                null);
        assertTrue(cursor.moveToFirst());

        String insertedTitle = cursor.getString(cursor.getColumnIndex(MovieEntry.ORIGINAL_TITLE));
        String valuesTitle = deadPoolValues.getAsString(MovieEntry.ORIGINAL_TITLE);

        assertEquals("Movie title not same..", valuesTitle, insertedTitle);
        Log.d(TAG, "testQuerySingleMovie: " + valuesTitle);
        cursor.close();
        db.close();
    }

    public void testDeleteSingleMovie()
    {
        deleteAllRecordsFromProvider();
        MovieProviderUtil dbHelper = new MovieProviderUtil(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues deadPoolValues = TestUtils.deadPoolMovieRecords();

        Uri deadPoolUri = mContext.getContentResolver().insert(MovieEntry.CONTENT_URI, deadPoolValues);

        long deadPoolStatus = ContentUris.parseId(deadPoolUri);

        assertTrue(deadPoolStatus != -1);
        String movieId = deadPoolValues.getAsString(MovieEntry.MOVIE_ID);
        int deleteStatus = mContext.getContentResolver().delete(deadPoolUri, MovieEntry.MOVIE_ID + "=?", new
                String[]{movieId});

        assertTrue("Not deleted", deleteStatus != -1);
        db.close();
    }

    void deleteAllRecordsFromProvider()
    {
        mContext.getContentResolver().delete(MovieEntry.CONTENT_URI, null, null);
        Cursor c = mContext.getContentResolver().query(MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null);

        assertEquals("Delete not performed", 0, c.getCount());
        c.close();
    }
}


