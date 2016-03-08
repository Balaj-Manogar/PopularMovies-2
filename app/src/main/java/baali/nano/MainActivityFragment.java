package baali.nano;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
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
import baali.nano.model.MovieAPIResponse;
import baali.nano.services.FetchMovieData;
import baali.nano.services.rest.TheMovieAPIService;
import baali.nano.utils.TheMovieDBUtils;
import butterknife.BindString;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment  implements MainActivity.DelegateMovieAdapterProcess<Movie>
        , AdapterView.OnItemClickListener
{
    @BindString(R.string.q_retrofit_base_url)
    String baseUrl;
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
        ButterKnife.bind(this, rootView);
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
        String sortOrder = movieUtil.getSortingOrder(option);
        getMovieDataUsingRetrofit(sortOrder);

       //getMovieDataUsingAsyncTask(option, movieUtil);

        Log.d(TAG, "init: " + movieUtil.buildURL(option));
        //Toast.makeText(getActivity(), movieUtil.buildURL(option), Toast.LENGTH_LONG).show();

    }

    private void getMovieDataUsingAsyncTask(MovieFetchOptions option, TheMovieDBUtils movieUtil)
    {
        String requestUrl = movieUtil.buildURL(option);


        String backdropBasePath = movieUtil.getStringResource(R.string.img_backdrop_url);
        String posterBasePath = movieUtil.getStringResource(R.string.img_poster_url);

        FetchMovieData movieData = new FetchMovieData();
        movieData.setMovieDelegate(this);
        movieData.execute(requestUrl, posterBasePath, backdropBasePath);
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
            for (Movie m : movieList)
            {
                Log.d(TAG, "process: " + m);
            }
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

    public void getMovieDataUsingRetrofit(String order)
    {
        // retrofit debug purpose
        OkHttpClient.Builder httpClient = getDebugBuilder();

        Retrofit apiService = new Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        String sortingOrder = order;

        TheMovieAPIService initAPIService = apiService.create(TheMovieAPIService.class);
        final Call<MovieAPIResponse> apiCall = initAPIService.fetchMovies(BuildConfig.THE_MOVIE_DB_API_KEY, order);

        apiCall.enqueue(new Callback<MovieAPIResponse>()
        {
            @Override
            public void onResponse(Call<MovieAPIResponse> call, Response<MovieAPIResponse> response)
            {
                Log.d(TAG, "onResponse: Retro: " + response.body().apiMoviesList.get(0));
                process(response.body().apiMoviesList);
            }

            @Override
            public void onFailure(Call<MovieAPIResponse> call, Throwable t)
            {
                Log.d(TAG, "onFailure: Retro: " + t.getMessage());
                // display a dialog here about failure
            }
        });
    }

    @NonNull
    private OkHttpClient.Builder getDebugBuilder()
    {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(logging);
        return httpClient;
    }
}
