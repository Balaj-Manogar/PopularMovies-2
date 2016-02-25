package baali.nano.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import baali.nano.R;
import baali.nano.model.Movie;

/**
 * Created by Balaji on 09/01/16.
 */
public class MoviePosterAdapter extends ArrayAdapter<Movie>
{
    private final String TAG = MoviePosterAdapter.class.getSimpleName();

    private final Context context;
    private int layoutResourceId;
    private List<Movie> moviesList;

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
        View row = convertView;
        Movie movie = moviesList.get(position);
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
        }

        ImageView imageView = (ImageView) row.findViewById(R.id.img_poster);
        Picasso.with(this.context).load(movie.getPosterPath()).placeholder(R.drawable.main_default_poster_drawable).error
                (R.drawable.main_error_poster_drawable).into(imageView);
        Log.d(TAG, "getView: " + row);

        //ImageView imageView = (ImageView) convertView.findViewById(R.id.main_poster_img);
        //Picasso.with(getContext()).load(movie.getPosterPath()).into(imageView);
        return row;
    }
}
