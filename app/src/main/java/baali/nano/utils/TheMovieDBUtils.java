package baali.nano.utils;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;

import baali.nano.BuildConfig;
import baali.nano.R;
import baali.nano.config.MovieFetchOptions;

/**
 * Created by Balaji on 05/01/16.
 */
public class TheMovieDBUtils
{
    private final Resources resources;
    private  String sortBy;
    private Context context;


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
}
