package baali.nano;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import baali.nano.adapter.MoviePosterAdapter;
import baali.nano.config.MovieFetchOptions;
import baali.nano.model.Movie;
import baali.nano.services.FetchMovieData;
import baali.nano.utils.TheMovieDBUtils;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment  implements MainActivity.DelegateMovieAdapterProcess<Movie>
        , AdapterView.OnItemClickListener
{
    private final String TAG = MainActivityFragment.class.getSimpleName();
    private  List<Movie> moviesList;
    private ArrayAdapter movieAdapter;
    private GridView gridMoviePoster;

    public MainActivityFragment()
    {
        moviesList = new ArrayList<>();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        //FetchMovieData movieData = new FetchMovieData();

    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        bridgeGridViewWithAdapter(rootView);

        checkBundleAndProcess(savedInstanceState);

        gridMoviePoster.setOnItemClickListener(this);
        return rootView;
    }

    private void bridgeGridViewWithAdapter(View rootView)
    {
        gridMoviePoster = (GridView) rootView.findViewById(R.id.grid_poster);
        movieAdapter = getPosterAdapter();
        gridMoviePoster.setAdapter(movieAdapter);
    }

    private void checkBundleAndProcess(Bundle savedInstanceState)
    {
        if(savedInstanceState != null)
        {
            Log.d(TAG, "Bundled: " + savedInstanceState.getParcelableArrayList("MovieList"));
            moviesList = savedInstanceState.getParcelableArrayList("MovieList");
            process(moviesList);
        }
        else {

            populateGridView(MovieFetchOptions.Popular);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int menuId = item.getItemId();
        switch (menuId)
        {
            case R.id.action_popular:
            {
                populateGridView(MovieFetchOptions.Popular);
                break;
            }
            case R.id.action_rating:
            {
                populateGridView(MovieFetchOptions.Rating);
                break;
            }

        }
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {

        outState.putParcelableArrayList("MovieList", (ArrayList<? extends Parcelable>) moviesList);
        super.onSaveInstanceState(outState);
    }

    private void populateGridView(MovieFetchOptions option)
    {
        TheMovieDBUtils movieUtil = new TheMovieDBUtils(getContext());
        String requestUrl = movieUtil.buildURL(option);

        String posterBasePath = movieUtil.getStringResource(R.string.img_poster_url);
        String backdropBasePath = movieUtil.getStringResource(R.string.img_backdrop_url);


        FetchMovieData movieData = new FetchMovieData();
        movieData.setMovieDelegate(this);
        movieData.execute(requestUrl, posterBasePath, backdropBasePath);

        Log.d(TAG, "init: " + movieUtil.buildURL(option));
        //Toast.makeText(getActivity(), movieUtil.buildURL(option), Toast.LENGTH_LONG).show();

    }

    private ArrayAdapter getPosterAdapter()
    {
        return new MoviePosterAdapter(getContext(), R.layout.grid_main_poster, moviesList);
    }


    @Override
    public void process(List<? extends Movie> movieList)
    {
        if(movieList.size() > 0)
        {
            movieAdapter.clear();
            movieAdapter.addAll(movieList);

        }
        else
        {
//            handle here for no movies
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
//          Toast.makeText(getContext(), ((Movie)moviesList.get(position)).toString() , Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
        intent.putExtra("movie", moviesList.get(position));
        startActivity(intent);


    }
}
