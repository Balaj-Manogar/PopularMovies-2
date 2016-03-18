package baali.nano;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import baali.nano.adapter.MoviePagerAdapter;
import baali.nano.model.Movie;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailWideFragment extends Fragment
{
    private static final String TAG = MovieDetailWideFragment.class.getSimpleName();
    private View rootView;

    public MovieDetailWideFragment()
    {
        // Required empty public constructor
    }

    public static MovieDetailWideFragment newInstance()
    {
        MovieDetailWideFragment fragment = new MovieDetailWideFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_movie_wide_detail, container, false);
        this.rootView = view;
        Log.d(TAG, "WideFragT: " + getActivity().findViewById(R.id.movie_detail_frame));
        if (getActivity().findViewById(R.id.movie_detail_frame) != null)
        {
            Log.d(TAG, "WideFragT:  expects null at relaunch: " + getMovie());
            if(getMovie() != null)
            {
//                MovieDetailActivityFragment overviewFragment =
                Log.d(TAG, "onCreateView: not empty: " + getMovie());
                ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.view_pager);
                PagerAdapter pagerAdapter = new MoviePagerAdapter(getActivity().getSupportFragmentManager(), getMovie());
                viewPager.setAdapter(pagerAdapter);


                // Setup the Tabs
                TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout);
                tabLayout.setupWithViewPager(viewPager);
                int tabCount = tabLayout.getTabCount();
                Log.d(TAG, "onCreateView: tabcou: " + tabCount);
            }
        }

        Log.d(TAG, "onCrateView: from mmmmm" + this.getArguments());
        return view;
    }

    public void createTabLayout(View rootView)
    {
        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.view_pager);
        PagerAdapter pagerAdapter = new MoviePagerAdapter(getActivity().getSupportFragmentManager(), getMovie());
        viewPager.setAdapter(pagerAdapter);
        pagerAdapter.notifyDataSetChanged();

        // Setup the Tabs
        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }

    private Movie getMovie()
    {
        Bundle extras = getActivity().getIntent().getExtras();
        Bundle b = (extras != null && extras.getParcelable("movie") != null ) ? extras : this.getArguments();

        Movie movie = null;
        if (b != null)
        {
            movie = b.getParcelable("movie");
        }
        return movie;
    }

}
