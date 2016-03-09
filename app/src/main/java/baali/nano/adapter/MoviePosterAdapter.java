package baali.nano.adapter;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import baali.nano.R;
import baali.nano.config.FavouriteInsertStatus;
import baali.nano.model.Favourite;
import baali.nano.model.Movie;
import baali.nano.model.provider.MovieContract.MovieEntry;
import baali.nano.services.task.SaveFavouriteImageToDisk;
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
        ImageView fav = (ImageView) view.findViewById(R.id.poster_favourite);
        addOrRemoveFavouriteMovieAsync(currentView, currentMovie, fav);
        Log.d(TAG, "favouriteClicktest: after" + currentMovie);
        //moviesList.get(position).favourite
    }


    private void addOrRemoveFavouriteMovieAsync(final View currentView, final Movie currentMovie, ImageView fav)
    {
        final ImageView imageView = (ImageView) currentView;
        AsyncTask<Void, Void, Favourite> task = new AsyncTask<Void, Void, Favourite>()
        {

            @Override
            protected Favourite doInBackground(Void... params)
            {
                Favourite model = new Favourite();
//                model.view = imageView;
                model.status = addToFavourite(currentMovie);
                model.movie = currentMovie;

                return model;
            }

            @Override
            protected void onPostExecute(Favourite favModel)
            {
                favModel.view = currentView;
                ImageView iv = (ImageView) favModel.view;
                Log.d(TAG, "onPostExecuteequals: " + moviesList.contains(favModel.movie));
                favourite(favModel, iv);

            }
        };

        task.execute();
    }

    private void favourite(Favourite favModel, ImageView iv)
    {
        switch (favModel.status)
        {
            case Sucess:
            {
                iv.setImageResource(R.drawable.fav_filled);
                storeFavouriteImages(favModel);
                Toast.makeText(getContext(), "Added to favourite", Toast.LENGTH_LONG).show();
                break;
            }
            case Remove:
            {
                removeFavouriteImages(favModel);
                iv.setImageResource(R.drawable.fav_outline);
                Toast.makeText(getContext(), "Removed from favourite", Toast.LENGTH_LONG).show();
                break;
            }
            case Failure:
            {
                Toast.makeText(getContext(), "Error: tap again", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void removeFavouriteImages(Favourite favModel)
    {
        final String[] files = {favModel.movie.getPosterPath(), favModel.movie.getBackdropPath()};
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                for (String file : files)
                {
                    File f = new File(Environment.getDataDirectory().getPath()
                            + "/data/" + getContext().getApplicationContext().getPackageName()
                            + "/" + context.getString(R.string.offline_directory) + file);


                    if (f.exists())
                    {
                        f.delete();
                    }
                }

            }
        }).start();
    }

    private void storeFavouriteImages(Favourite favModel)
    {
        final Movie m = favModel.movie;
        String backdropPath = mainBackdropPrefix + m.getBackdropPath();
        String posterPath = mainBackdropPrefix + m.getPosterPath();
        Picasso.with(getContext()).load(posterPath).into(new SaveFavouriteImageToDisk(getContext(), m.getPosterPath()));
        Picasso.with(getContext()).load(backdropPath).into(new SaveFavouriteImageToDisk(getContext(), m.getBackdropPath()));

    }

    private FavouriteInsertStatus addToFavourite(Movie currentMovie)
    {
        FavouriteInsertStatus status = checkAndInsertFavourite(currentMovie);
        if (status == FavouriteInsertStatus.Sucess)
        {
            // write downloading image option here
            status = FavouriteInsertStatus.Sucess;
        }

        return status;
    }

    public FavouriteInsertStatus checkAndInsertFavourite(final Movie m)
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
            status = removeFavouriteMovie(m);
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
        int index = moviesList.indexOf(currentMovie);
        Log.d(TAG, "insertFavouriteMovie: " + index);
        currentMovie.setFavourite(true);
        ContentValues favouriteValues = TheMovieDBUtils.parseMovieToContentValues(currentMovie);
        Log.d(TAG, "insertFavouriteMovie bfins: " + favouriteValues);
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


    private FavouriteInsertStatus removeFavouriteMovie(Movie currentMovie)
    {
        FavouriteInsertStatus status = FavouriteInsertStatus.Failure;
        currentMovie.setFavourite(false);
        int index = moviesList.indexOf(currentMovie);
        moviesList.get(index).setFavourite(false);
//
//        notifyDataSetChanged();
        ContentResolver contentResolver = context.getContentResolver();

        Uri favUri = MovieEntry.buildMovieUri(String.valueOf(currentMovie.getId()));
        int deleteStatus = contentResolver.delete(favUri, MovieEntry.MOVIE_ID + "=?", new
                String[]{String.valueOf(currentMovie.getId())});

        status = (deleteStatus != -1) ? FavouriteInsertStatus.Remove : FavouriteInsertStatus.Failure;

        return status;

    }

}


