package baali.nano.adapter;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import baali.nano.R;
import baali.nano.config.FavouriteInsertStatus;
import baali.nano.model.Favourite;
import baali.nano.model.Movie;
import baali.nano.model.provider.MovieContract.MovieEntry;
import baali.nano.utils.TheMovieDBUtils;
import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Balaji on 09/01/16.
 */
public class MoviePosterAdapter extends ArrayAdapter<Movie>
{
    private final String TAG = MoviePosterAdapter.class.getSimpleName();

    private final Context context;
    private int layoutResourceId;
    private List<Movie> moviesList;
    private int currentPosition;

    private static final String FAV = "fav";

    @BindString(R.string.img_backdrop_url)
    String mainBackdropPrefix;
    @Bind(R.id.img_poster)
    ImageView imageView;
    @Bind(R.id.poster_favourite)
    ImageView favImage;

    public MoviePosterAdapter(Context context, int resource, List<Movie> moviesList)
    {
        super(context, R.layout.grid_main_poster, moviesList);
        this.context = context;
        this.layoutResourceId = resource;
        this.moviesList = moviesList;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        currentPosition = position;
        View row = convertView;
        Movie movie = moviesList.get(position);
        if (row == null)
        {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
        }
        ButterKnife.bind(this, row);
        favImage.setTag(position);
        //ImageView imageView = (ImageView) row.findViewById(R.id.img_poster);

        String posterPath = mainBackdropPrefix + movie.getPosterPath();
        Picasso.with(this.context).load(posterPath)
                .placeholder(R.drawable.main_default_poster_drawable)
                .error(R.drawable.main_error_poster_drawable)
                .into(imageView);
        Log.d(TAG, "getView: " + row);

        //ImageView imageView = (ImageView) convertView.findViewById(R.id.main_poster_img);
        //Picasso.with(getContext()).load(movie.getPosterPath()).into(imageView);
        return row;
    }

    @OnClick(R.id.poster_favourite)
    void favouriteClick(final View view)
    {
        final View currentView = view;
        final int position = (int) (view.getTag());
        final Movie currentMovie = moviesList.get(position);
        Log.d(TAG, "favouriteClick: " + currentMovie);
        ImageView fav = (ImageView) view.findViewById(R.id.poster_favourite);

        addFavouriteMovieAsync(currentView, currentMovie, fav);
    }

    private void addFavouriteMovieAsync(final View currentView, final Movie currentMovie, ImageView fav)
    {
        AsyncTask<View, Void, Favourite> task = new AsyncTask<View, Void, Favourite>()
        {
            @Override
            protected Favourite doInBackground(View... params)
            {
                Favourite model = new Favourite();
                model.view = params[0];
                model.status = addToFavourite(currentView, currentMovie);
                return model;
            }

            @Override
            protected void onPostExecute(Favourite favModel)
            {
                ImageView iv = (ImageView) favModel.view;

                switch (favModel.status)
                {
                    case Sucess:
                    {
                        iv.setImageResource(R.drawable.fav_filled);
                        break;
                    }
                    case Duplicate:
                    {
                        Toast.makeText(getContext(), "Already favourite movie", Toast.LENGTH_LONG).show();
                        break;
                    }
                    case Failure:
                    {
                        Toast.makeText(getContext(), "Error: tap again", Toast.LENGTH_LONG).show();
                    }
                }

            }
        };

        task.execute(fav);
    }

    private FavouriteInsertStatus addToFavourite(View view, Movie currentMovie)
    {
        FavouriteInsertStatus status = checkAndInsertFavourite(currentMovie);
        if ( status == FavouriteInsertStatus.Sucess)
        {
            status = FavouriteInsertStatus.Sucess;
        }

        return status;
    }

    public FavouriteInsertStatus checkAndInsertFavourite(Movie m)
    {
        FavouriteInsertStatus status;
        if (getFavouriteStatus(m) == FavouriteInsertStatus.NoDuplicate)
        {
            status = insertFavouriteMovie(m);
        }
        else
        {
            //all ui must be handled in UI thread but I m not so it will throw exception
            //Toast.makeText(getContext(), "This is already your favourite movie", Toast.LENGTH_SHORT).show();
            status = FavouriteInsertStatus.Duplicate;
        }
        return status;
    }

    private FavouriteInsertStatus getFavouriteStatus(Movie currentMovie)
    {
        String currentMovieId = String.valueOf(currentMovie.getId());
        String selection = MovieEntry.MOVIE_ID + "=?";
        Uri currentMovieUri = MovieEntry.buildMovieUri(currentMovieId);
        Cursor cursor = context.getContentResolver().query(currentMovieUri, MovieEntry.PROJECTION_ALL, selection,
                new String[]{currentMovieId},
                null);
        return cursor.getCount() > 0 ? FavouriteInsertStatus.Duplicate : FavouriteInsertStatus.NoDuplicate;
    }

    private FavouriteInsertStatus insertFavouriteMovie(Movie currentMovie)
    {
        FavouriteInsertStatus status = FavouriteInsertStatus.Failure;
        currentMovie.setFavourite(true);
        ContentValues favouriteValues = TheMovieDBUtils.parseMovieToContentValues(currentMovie);
        ContentResolver contentResolver = context.getContentResolver();
        Uri favUri = contentResolver.insert(MovieEntry.CONTENT_URI, favouriteValues);

        long favUriStatus = ContentUris.parseId(favUri);
        String id = String.valueOf(currentMovie.getId());
        Cursor cursor = contentResolver.query(favUri, null, null, new String[]{id}, null);

        if (cursor.moveToFirst())
        {
            long listId = currentMovie.getId();
            long dbId = cursor.getLong(cursor.getColumnIndex(MovieEntry.MOVIE_ID));
            status = (listId == dbId) ? FavouriteInsertStatus.Sucess : FavouriteInsertStatus.Failure;
        }
        cursor.close();
        return status;
    }


}


