package baali.nano;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import baali.nano.model.Movie;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment
{
    private final String TAG = MovieDetailActivityFragment.class.getSimpleName();

    Movie movie;

    @Bind(R.id.movie_detail_poster_img)
    ImageView poster;
    @Bind(R.id.movie_detail_title)
    TextView title;
    @Bind(R.id.movie_detail_release)
    TextView releaseDate;
    @Bind(R.id.movie_detail_vote_average)
    RatingBar voteAverage;
    @Bind(R.id.movie_detail_synopsis)
    TextView synopsis;

    public MovieDetailActivityFragment()
    {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View fragmentLayout = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        ButterKnife.bind(this, fragmentLayout);
        movie = getMovie();
        // initializeViews(fragmentLayout.findViewById(R.id.movie_scroll).findViewById(R.id.movie_container));
        putMovieDataIntoViews();
        Log.d(TAG, "onCreateView: " + movie.getBackdropPath());
        // Toast.makeText(getContext(), ((Movie)getActivity().getIntent().getParcelableExtra("movie")).toString(), Toast
        // .LENGTH_LONG).show();
        return fragmentLayout;
    }


    private Movie getMovie()
    {
        Bundle b = getActivity().getIntent().getExtras();
        Movie movie = b.getParcelable("movie");
        Log.d(TAG, "getMovie: " + movie);
        return movie;
    }

    /*private void initializeViews(View layout)
    {
        poster = (ImageView)getViewById(R.id.movie_detail_poster_img, layout);
        title = (TextView) getViewById(R.id.movie_detail_title, layout);
        releaseDate = (TextView) getViewById(R.id.movie_detail_release, layout);
        voteAverage = (RatingBar) getViewById(R.id.movie_detail_vote_average, layout);
        synopsis = (TextView) getViewById(R.id.movie_detail_synopsis, layout);
    }*/

    private void putMovieDataIntoViews()
    {

        Log.d(TAG, "putMovieDataIntoViews: " + movie);
        // download image and map to view
        Picasso.with(getContext()).load(movie.getBackdropPath()).into(poster);
        title.setText(movie.getTitle());
        releaseDate.setText(movie.getReleaseDate());
        voteAverage.setRating(Float.valueOf(movie.getVoteAverage()) / 2f);
        synopsis.setText(movie.getOverview());
    }

    private View getViewById(int id, View layout)
    {
        return layout.findViewById(id);
    }
}
