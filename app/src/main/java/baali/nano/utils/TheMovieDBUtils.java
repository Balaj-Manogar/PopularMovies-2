package baali.nano.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;

import baali.nano.BuildConfig;
import baali.nano.R;
import baali.nano.config.MovieFetchOptions;
import baali.nano.model.Movie;
import baali.nano.model.provider.MovieContract.MovieEntry;

/**
 * Created by Balaji on 05/01/16.
 */
public class TheMovieDBUtils
{
    private final Resources resources;
    private  String sortBy;
    private Context context;

    private static final String TAG = TheMovieDBUtils.class.getSimpleName();


    public TheMovieDBUtils(Context context)
    {
        this.resources = context.getResources();
        this.sortBy = getStringResource(R.string.sort_desc);
    }

    // used to sort by ascending or descending using string resource value
    public void setSortBy(String sort)
    {

        this.sortBy = sort;
    }

    // builds movie url based on fetch option
    public  String buildURL(MovieFetchOptions option)
    {
        String baseURL = getStringResource(R.string.q_base_url);
        String paramApiKey = getStringResource(R.string.q_api_key);
        Uri movieApiUrl;

        if(option == MovieFetchOptions.Popular)
        {
            movieApiUrl = getPopularURL(baseURL, paramApiKey);
        }
        else
        {
            movieApiUrl = getRatedURL(baseURL, paramApiKey);
        }

        return (movieApiUrl != null) ? movieApiUrl.toString() : null;
    }

    public  String getStringResource(int resourceId)
    {
        return resources.getString(resourceId);
    }

    private Uri getRatedURL(String baseURL, String paramApiKey)
    {
        Uri movieApiUrl;
        String paramRated = getStringResource(R.string.q_param_sort_type);
        String paramRatedValue = getStringResource(R.string.q_rated)+ getStringResource(R.string.q_delim) + this.sortBy;
        movieApiUrl = Uri.parse(baseURL).buildUpon().appendQueryParameter(paramApiKey, BuildConfig.THE_MOVIE_DB_API_KEY)
                .appendQueryParameter(paramRated, paramRatedValue).build();

        return movieApiUrl;
    }


    private Uri getPopularURL(String baseURL, String paramApiKey)
    {
        Uri movieApiUrl;
        String paramPopular = getStringResource(R.string.q_param_sort_type);
        String paramPopularValue = getStringResource(R.string.q_popular)+ getStringResource(R.string.q_delim) + this.sortBy;
        movieApiUrl = Uri.parse(baseURL).buildUpon().appendQueryParameter(paramApiKey, BuildConfig.THE_MOVIE_DB_API_KEY)
                .appendQueryParameter(paramPopular, paramPopularValue).build();

        return movieApiUrl;
    }

    public String getSortingOrder(MovieFetchOptions option)
    {
        String order = null;
        if(option == MovieFetchOptions.Popular)
        {
            order = getStringResource(R.string.q_popular)+ getStringResource(R.string.q_delim) + this.sortBy;
        }
        else
        {
            order = getStringResource(R.string.q_rated)+ getStringResource(R.string.q_delim) + this.sortBy;
        }

        return order;
    }

    public static ContentValues parseMovieToContentValues(Movie m)
    {
        ContentValues movieValues = new ContentValues();
        movieValues.put(MovieEntry.MOVIE_ID, m.getId());
        movieValues.put(MovieEntry.ADULT, m.isAdult());
        movieValues.put(MovieEntry.ORIGINAL_TITLE, m.getOriginalTitle());
        movieValues.put(MovieEntry.POSTER_PATH, m.getPosterPath());
        movieValues.put(MovieEntry.OVERVIEW, m.getOverview());
        movieValues.put(MovieEntry.RELEASE_DATE, m.getReleaseDate());
        movieValues.put(MovieEntry.POPULARITY, m.getPopularity());
        movieValues.put(MovieEntry.VOTE_COUNT, m.getVoteCount());
        movieValues.put(MovieEntry.VOTE_AVERAGE, m.getVoteAverage());
        movieValues.put(MovieEntry.FAVOURITE, m.isFavourite());

        Log.d(TAG, "parseMovieToContentValues ins: " + movieValues);
        Log.d(TAG, "parseMovieToContentValues ins: " + m.isFavourite());
        return movieValues;
    }




}
