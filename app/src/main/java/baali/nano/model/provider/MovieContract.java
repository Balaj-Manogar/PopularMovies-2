package baali.nano.model.provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Balaji on 27/02/16.
 */
public class MovieContract
{
    public static final String CONTENT_AUTHORITY = "baali.nano";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIE = "movie";

    public static final class MovieEntry implements BaseColumns
    {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIE).build();

        /**
         * The mime type of a directory of items.
         */
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/"
                + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        /**
         * The mime type of a single item.
         */
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/"
                + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static final String TABLE_NAME = "movie";

        //columns
        public static final String MOVIE_ID = "mId";
        public static final String ADULT = "adult";
        public static final String ORIGINAL_TITLE = "originalTitle";
        public static final String POSTER_PATH = "posterPath";
        public static final String BACKDROP_PATH = "backdropPath";
        public static final String OVERVIEW = "overview";
        public static final String RELEASE_DATE = "releaseDate";
        public static final String POPULARITY = "popularity";
        public static final String VOTE_COUNT = "voteCount";
        public static final String VOTE_AVERAGE = "voteAverage";
        public static final String FAVOURITE = "favourite";


        /**
         * A projection of all columns in the items table.
         */
        public static final String[] PROJECTION_ALL = {
                _ID, MOVIE_ID, ADULT, ORIGINAL_TITLE, POSTER_PATH, BACKDROP_PATH, OVERVIEW
                , RELEASE_DATE, POPULARITY, VOTE_COUNT, VOTE_AVERAGE, FAVOURITE
        };


        public static Uri buildMovieUri(String id) {
            return CONTENT_URI.buildUpon().appendPath(id).build();
        }

    }
}
