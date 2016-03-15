package baali.nano;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import baali.nano.adapter.MoviePagerAdapter;
import baali.nano.model.Movie;
import baali.nano.model.MovieAPIVideoResponse;
import baali.nano.services.rest.TheMovieAPIService;
import baali.nano.utils.HttpUtils;
import butterknife.BindString;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDetailActivity extends AppCompatActivity
{
    @BindString(R.string.q_retrofit_base_url)
    String baseUrl;
    private static final String TAG = MovieDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*Tab layout for movies*/
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        PagerAdapter pagerAdapter = new MoviePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        // Setup the Tabs
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        getMovieVideoUsingRetrofit();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    public void getMovieVideoUsingRetrofit()
    {
        // retrofit debug purpose
        OkHttpClient.Builder httpClient = HttpUtils.getDebugBuilder();
        Movie m = getMovie();

        Retrofit apiService = new Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();


        TheMovieAPIService initAPIService = apiService.create(TheMovieAPIService.class);
        final Call<MovieAPIVideoResponse> apiCall = initAPIService.fetchMovieTrailer(String.valueOf(m.getId()), BuildConfig
                .THE_MOVIE_DB_API_KEY);

        apiCall.enqueue(new Callback<MovieAPIVideoResponse>()
        {
            @Override
            public void onResponse(Call<MovieAPIVideoResponse> call, Response<MovieAPIVideoResponse> response)
            {
                Log.d(TAG, "onResponse: Retro: " + response.body().apiMovieVideosList.get(0).getId());
            }

            @Override
            public void onFailure(Call<MovieAPIVideoResponse> call, Throwable t)
            {
                Log.d(TAG, "onFailure: Retro: " + t.getMessage());
            }
        });
    }

    private Movie getMovie()
    {
        Bundle b = getIntent().getExtras();
        Movie movie = b.getParcelable("movie");
        Log.d(TAG, "getMovie: " + movie);
        return movie;
    }

}
