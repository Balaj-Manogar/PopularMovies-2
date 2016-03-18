package baali.nano.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import baali.nano.MainActivityFragment;
import baali.nano.MovieDetailActivityFragment;
import baali.nano.ReviewFragment;
import baali.nano.TrailerFragment;
import baali.nano.model.Movie;

/**
 * Created by Balaji on 13/03/16.
 */
public class MoviePagerAdapter extends FragmentStatePagerAdapter
{

    Movie movie;

    public MoviePagerAdapter(FragmentManager fm, Movie movie)
    {
        super(fm);
        this.movie = movie;
    }

    @Override
    public Fragment getItem(int position)
    {
        switch (position)
        {

            case 0:
                return MovieDetailActivityFragment.newInstance(movie);
            case 1:
                return ReviewFragment.newInstance(movie);
            case 2:
                return TrailerFragment.newInstance(movie);
            default:
                return MainActivityFragment.newInstance();
        }
    }

    @Override
    public int getCount()
    {
        return 3;
    }

    @Override
    public int getItemPosition(Object object)
    {
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        switch (position)
        {
            case 0:
                return "Overview";
            case 1:
                return "Reviews";
            case 2:
                return "Trailers";
        }
        return "Tab " + (position + 1);
    }


}
