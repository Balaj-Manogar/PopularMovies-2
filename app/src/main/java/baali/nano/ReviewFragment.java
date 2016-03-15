package baali.nano;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import baali.nano.adapter.ReviewAdapter;
import baali.nano.model.Movie;
import baali.nano.model.MovieAPIReviewResponse;
import baali.nano.model.MovieReview;
import baali.nano.services.rest.TheMovieAPIService;
import baali.nano.utils.HttpUtils;
import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReviewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReviewFragment extends Fragment
{

    private static final String TAG = ReviewFragment.class.getSimpleName();

    @Nullable
    @BindString(R.string.q_retrofit_base_url)
    String baseUrl;

    @Nullable
    @Bind(R.id.review_list_view)
    ListView reviewListView;

    private ArrayAdapter<MovieReview> reviewAdapter;
    private List<MovieReview> reviews;


    private OnFragmentInteractionListener mListener;

    public ReviewFragment()
    {
        // Required empty public constructor
    }

    public static ReviewFragment newInstance()
    {
        ReviewFragment fragment = new ReviewFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.review_fragment, container, false);
        ButterKnife.bind(this, rootView);
        if (savedInstanceState != null)
        {
            reviews = savedInstanceState.getParcelableArrayList("MovieReviewList");

            reviewAdapter = new ReviewAdapter(getContext(), R.layout.list_movie_review, reviews);
            reviewListView.setAdapter(reviewAdapter);
            Log.d(TAG, "onCreateView: from saved instance");
        }
        else
        {
            getMovieReviewUsingRetrofit();
        }


        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        outState.putParcelableArrayList("MovieReviewList", (ArrayList<? extends Parcelable>) reviews);
        super.onSaveInstanceState(outState);
    }

    public void getMovieReviewUsingRetrofit()
    {
        // retrofit debug purpose
        OkHttpClient.Builder httpClient = HttpUtils.getDebugBuilder();
        Movie m = getMovie();

        Retrofit apiService = new Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();


        TheMovieAPIService initAPIService = apiService.create(TheMovieAPIService.class);
        final Call<MovieAPIReviewResponse> apiCall = initAPIService.fetchMovieReview(String.valueOf(m.getId()), BuildConfig
                .THE_MOVIE_DB_API_KEY);

        apiCall.enqueue(new Callback<MovieAPIReviewResponse>()
        {
            @Override
            public void onResponse(Call<MovieAPIReviewResponse> call, Response<MovieAPIReviewResponse> response)
            {
                List<MovieReview> apiMovieReviewsList = response.body().apiMovieReviewsList;
                int reviewCount = apiMovieReviewsList.size();
                Log.d(TAG, "onResponse: Retro: " + reviewCount);
                if (reviewCount > 0)
                {

                    reviews = new ArrayList<MovieReview>(apiMovieReviewsList);
                    reviewAdapter = new ReviewAdapter(getContext(), R.layout.list_movie_review, reviews);
                    reviewListView.setAdapter(reviewAdapter);
                    Log.d(TAG, "onResponse: list size: " + reviews.size());

                }
                else
                {
                    Toast.makeText(getContext(), "No reviews found for this movie...", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<MovieAPIReviewResponse> call, Throwable t)
            {
                Log.d(TAG, "onFailure: Retro: " + t.getMessage());
            }
        });
    }

    private Movie getMovie()
    {
        Bundle b = getActivity().getIntent().getExtras();
        Movie movie = b.getParcelable("movie");
        Log.d(TAG, "getMovie: " + movie);
        return movie;
    }


//    @Override
//    public void onAttach(Context context)
//    {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener)
//        {
//            mListener = (OnFragmentInteractionListener) context;
//        }
//        else
//        {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
