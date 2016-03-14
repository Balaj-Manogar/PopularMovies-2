package baali.nano;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import baali.nano.adapter.MoviePagerAdapter;

public class MovieDetailActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
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


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
