package baali.nano.services.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import baali.nano.model.provider.MovieContract;
import baali.nano.model.provider.MovieContract.MovieEntry;
import baali.nano.utils.provider.MovieProviderUtil;

/**
 * Created by Balaji on 27/02/16.
 */
public class MovieProvider extends ContentProvider
{
    static final int MOVIE = 1;
    static final int MOVIE_WITH_ID = 2;

    static final UriMatcher uriMatcher = buildUriMatcher();
    private MovieProviderUtil dbHelper;


    @Override
    public boolean onCreate()
    {
        dbHelper = new MovieProviderUtil(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int type = uriMatcher.match(uri);
        Cursor cursor = null;
        switch(type)
        {
            case MOVIE:
            {
                cursor = db.query(MovieEntry.TABLE_NAME, MovieEntry.PROJECTION_ALL
                        , null, null, null, null, null, "20");
                break;
            }
            case MOVIE_WITH_ID:
            {
                cursor = db.query(MovieEntry.TABLE_NAME, MovieEntry.PROJECTION_ALL
                        , MovieEntry.MOVIE_ID + "='" + ContentUris.parseId(uri)+ "'"
                        , selectionArgs, null, null, sortOrder);
                break;
            }
            default:
            {
                throw new UnsupportedOperationException("Provider query: " + uri);
            }

        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values)
    {
        int type = uriMatcher.match(uri);
        Uri insertedRowUri = null;
        switch (type)
        {
            case MOVIE:
            {
                long newRowId = dbHelper.getWritableDatabase().insert(MovieEntry.TABLE_NAME, null, values);
                if (newRowId > 0)
                {
                    insertedRowUri = MovieEntry.buildMovieUri(String.valueOf(newRowId));
                }
                else
                {
                    throw new SQLException("Movie is not added in db: " + uri);
                }
                break;
            }
            default:
            {
                throw new UnsupportedOperationException("Unknown Uri: " + uri);

            }
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return insertedRowUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        int type = uriMatcher.match(uri);
        int deletedRow = -1;
        switch (type)
        {
            case MOVIE:
            {
                deletedRow = dbHelper.getWritableDatabase().delete(MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default:
            {
                throw new UnsupportedOperationException("Delete Unknown Uri: " + uri);
            }
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return deletedRow;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
    {
        int type = uriMatcher.match(uri);
        int updatedRow = -1;
        switch (type)
        {
            case MOVIE:
            {
                updatedRow = dbHelper.getWritableDatabase().update(MovieEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            default:
            {
                throw new UnsupportedOperationException("update Unknown Uri: " + uri);
            }
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return updatedRow;
    }

    @Override
    public String getType(Uri uri)
    {
        int type = uriMatcher.match(uri);
        switch (type)
        {
            case MOVIE:
                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;
            case MOVIE_WITH_ID:
                return MovieContract.MovieEntry.CONTENT_TYPE;
        }
        return null;
    }

    static UriMatcher buildUriMatcher()
    {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        String authority = MovieContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, MovieContract.PATH_MOVIE, MOVIE);
        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/#", MOVIE_WITH_ID);
        return matcher;
    }
}
