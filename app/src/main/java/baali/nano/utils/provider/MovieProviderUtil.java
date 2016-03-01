package baali.nano.utils.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import baali.nano.model.provider.MovieContract.MovieEntry;

/**
 * Created by Balaji on 27/02/16.
 */
public class MovieProviderUtil extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "movie.db";

    public MovieProviderUtil(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        final String CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + "("
                + MovieEntry._ID + " INTEGER PRIMARY KEY," + MovieEntry.MOVIE_ID + " INTEGER UNIQUE NOT NULL, "
                + MovieEntry.ADULT + " TEXT NOT NULL, " + MovieEntry.ORIGINAL_TITLE + " TEXT NOT NULL,"
                + MovieEntry.POSTER_PATH + " TEXT, " + MovieEntry.BACKDROP_PATH + " TEXT, "
                + MovieEntry.OVERVIEW + " TEXT NOT NULL, " + MovieEntry.RELEASE_DATE + " TEXT NOT NULL, "
                + MovieEntry.POPULARITY + " REAL NOT NULL, " + MovieEntry.VOTE_COUNT + " INTEGER NOT NULL, "
                + MovieEntry.VOTE_AVERAGE + " TEXT"
                + ")";
        db.execSQL(CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
