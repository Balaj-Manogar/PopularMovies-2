package baali.nano.adapter;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
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

import baali.nano.MainActivity;
import baali.nano.R;
import baali.nano.config.AppFetchStatus;
import baali.nano.config.FavouriteInsertStatus;
import baali.nano.config.MovieFetchOptions;
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

    private int currentPosition = 0;
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

    public List<Movie> getMoviesList()
    {
        return moviesList;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        if (row == null)
        {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
        }
        Movie movie = moviesList.get(position);
        ButterKnife.bind(this, row);

        favImage.setTag(position);

        modifyIconForFavouriteMovies(row);

        String posterPath = mainBackdropPrefix + movie.getPosterPath();
        Picasso.with(this.context).load(posterPath)
                .placeholder(R.drawable.main_default_poster_drawable)
                .error(R.drawable.main_error_poster_drawable)
                .into(imageView);

        return row;
    }

    private void modifyIconForFavouriteMovies(View row)
    {
        int tag = (int) favImage.getTag();

        Movie scrollMovie = moviesList.get(tag);
        if (scrollMovie.isFavourite())
        {
            ImageView iv = (ImageView) row.findViewById(R.id.poster_favourite);
            iv.setImageResource(R.drawable.fav_filled);
        }
        else
        {
            ImageView iv = (ImageView) row.findViewById(R.id.poster_favourite);
            iv.setImageResource(R.drawable.fav_outline);
        }
    }

    @OnClick(R.id.poster_favourite)
    void favouriteClick(final View view)
    {
        final View currentView = view;
        final int position = (int) (view.getTag());
        final Movie currentMovie = moviesList.get(position);
        ImageView fav = (ImageView) view.findViewById(R.id.poster_favourite);
        toggleFavourite(currentView, currentMovie, fav);
    }


    private void toggleFavourite(final View currentView, final Movie currentMovie, ImageView fav)
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
                executeByFavouriteState(favModel, iv);
            }
        };

        task.execute();
    }

    private void executeByFavouriteState(Favourite favModel, ImageView iv)
    {
        switch (favModel.status)
        {
            case Sucess:
            {
                iv.setImageResource(R.drawable.fav_filled);
                downloadImagesAndSaveToDisk(favModel);
                Toast.makeText(getContext(), "Added to favourite", Toast.LENGTH_LONG).show();
                break;
            }
            case Remove:
            {
                removeFavouriteImages(favModel);
                Toast.makeText(getContext(), "Removed from favourite", Toast.LENGTH_LONG).show();
                if (AppFetchStatus.getState() == MovieFetchOptions.Favourite)
                {
                    int favMovieIndex = moviesList.indexOf(favModel.movie);
                    if (favMovieIndex != -1)
                    {
                        moviesList.remove(favMovieIndex);
                        Log.d(TAG, "executeByFavouriteState: " + moviesList.size());
                        notifyDataSetChanged();
                        redirectIfEmpty(favModel);
                    }
                }
                else
                {
                    iv.setImageResource(R.drawable.fav_outline);
                }
                break;
            }
            case Failure:
            {
                Toast.makeText(getContext(), "Error: tap again", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void redirectIfEmpty(Favourite favModel)
    {
        if(moviesList.size() < 1)
        {
            // I dont know whether this is correct or not, please give feedback
            Log.d(TAG, "executeByFavouriteState: ");
            Intent intent = new Intent(favModel.view.getContext(), MainActivity.class);
            favModel.view.getContext().startActivity(intent);
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

    private void downloadImagesAndSaveToDisk(Favourite favModel)
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


    private FavouriteInsertStatus removeFavouriteMovie(Movie currentMovie)
    {
        FavouriteInsertStatus status = FavouriteInsertStatus.Failure;
        currentMovie.setFavourite(false);
        int index = moviesList.indexOf(currentMovie);
        moviesList.get(index).setFavourite(false);

        ContentResolver contentResolver = context.getContentResolver();

        Uri favUri = MovieEntry.buildMovieUri(String.valueOf(currentMovie.getId()));
        int deleteStatus = contentResolver.delete(favUri, MovieEntry.MOVIE_ID + "=?", new
                String[]{String.valueOf(currentMovie.getId())});

        status = (deleteStatus != -1) ? FavouriteInsertStatus.Remove : FavouriteInsertStatus.Failure;

        return status;

    }

}


