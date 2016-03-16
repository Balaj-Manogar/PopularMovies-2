package baali.nano.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import baali.nano.R;
import baali.nano.model.MovieVideo;
import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;

/**
 * Created by Balaji on 15/03/16.
 */
public class TrailerAdapter extends ArrayAdapter<MovieVideo>
{

    private List<MovieVideo> trailers;
    private Context context;
    private final int layoutId;


    public TrailerAdapter(Context context, int resource, List<MovieVideo> trailers)
    {
        super(context, resource, trailers);
        this.context = context;
        this.layoutId = resource;
        this.trailers = trailers;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view;
        TrailerHolder trailerHolder;
        if (convertView == null)
        {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(layoutId, parent, false);
            trailerHolder = new TrailerHolder(view);
            view.setTag(trailerHolder);
        }
        else
        {
            view = convertView;
            trailerHolder = (TrailerHolder) view.getTag();
        }


        MovieVideo trailer = trailers.get(position);
        String trailerPosterPath = trailerHolder.trailerPosterPrefix + trailer.getKey() + trailerHolder.trailerPosterSuffix;
        trailerHolder.trailerTitle.setText(trailer.getName());
        new Picasso.Builder(context)
                .loggingEnabled(true)
                .build()
                .load(trailerPosterPath)
                .placeholder(R.drawable.main_default_poster_drawable)
                .error(R.drawable.main_error_poster_drawable)
                .into(trailerHolder.trailerYoutubePoster);

        return view;
    }

    static class TrailerHolder
    {
        @Bind(R.id.trailer_youtube_poster)
        ImageView trailerYoutubePoster;

        @Bind(R.id.trailer_youtube_title)
        TextView trailerTitle;

        @BindString(R.string.trailer_poster_prefix)
        String trailerPosterPrefix;

        @BindString(R.string.trailer_poster_suffix)
        String trailerPosterSuffix;


        TrailerHolder(View view)
        {
            ButterKnife.bind(this, view);
        }
    }
}
