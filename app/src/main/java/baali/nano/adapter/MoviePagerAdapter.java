package baali.nano.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import baali.nano.Fragment2;
import baali.nano.MainActivityFragment;
import baali.nano.MovieDetailActivityFragment;

/**
 * Created by Balaji on 13/03/16.
 */
public class MoviePagerAdapter extends FragmentStatePagerAdapter
{
    public MoviePagerAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int position)
    {
        switch (position)
        {

            case 0:
                return MovieDetailActivityFragment.newInstance();
            case 1:
                return Fragment2.newInstance();
            case 2:
                return Fragment2.newInstance();
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
    public CharSequence getPageTitle(int position)
    {
        return "Tab " + (position + 1);
    }
}
