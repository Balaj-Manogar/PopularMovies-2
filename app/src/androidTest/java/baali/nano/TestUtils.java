package baali.nano;

import android.content.ContentValues;
import android.test.AndroidTestCase;

import baali.nano.model.provider.MovieContract.MovieEntry;

/**
 * Created by Balaji on 29/02/16.
 */
public class TestUtils extends AndroidTestCase
{
    public static ContentValues deadPoolMovieRecords()
    {
        ContentValues values = new ContentValues();
        values.put(MovieEntry.MOVIE_ID, 293660);
        values.put(MovieEntry.ADULT, "false");
        values.put(MovieEntry.ORIGINAL_TITLE, "Deadpool");
        values.put(MovieEntry.POSTER_PATH, "/inVq3FRqcYIRl2la8iZikYYxFNR.jpg");
        values.put(MovieEntry.BACKDROP_PATH, "/n1y094tVDFATSzkTnFxoGZ1qNsG.jpg");
        values.put(MovieEntry.OVERVIEW, "Deadpool description");
        values.put(MovieEntry.RELEASE_DATE, "2016-02-09");
        values.put(MovieEntry.POPULARITY, 64.655808);
        values.put(MovieEntry.VOTE_COUNT, 1546);
        values.put(MovieEntry.VOTE_AVERAGE, "7.37");
        values.put(MovieEntry.FAVOURITE, false);
        return values;
    }

    public static ContentValues madMaxMovieRecords()
    {

        ContentValues values = new ContentValues();
        values.put(MovieEntry.MOVIE_ID, 76341);
        values.put(MovieEntry.ADULT, "false");
        values.put(MovieEntry.ORIGINAL_TITLE, "Mad Max: Fury Road");
        values.put(MovieEntry.POSTER_PATH, "/kqjL17yufvn9OVLyXYpvtyrFfak.jpg");
        values.put(MovieEntry.BACKDROP_PATH, "/tbhdm8UJAb4ViCTsulYFL3lxMCd.jpg");
        values.put(MovieEntry.OVERVIEW, "Mad Max description");
        values.put(MovieEntry.RELEASE_DATE, "2015-05-13");
        values.put(MovieEntry.POPULARITY, 52.655808);
        values.put(MovieEntry.VOTE_COUNT, 1546);
        values.put(MovieEntry.VOTE_AVERAGE, "7.37");
        values.put(MovieEntry.FAVOURITE, false);
        return values;
    }
}
