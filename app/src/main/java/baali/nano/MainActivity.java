package baali.nano;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


public class MainActivity extends AppCompatActivity
{
    // this value is used to track onCreate state
    private AtomicBoolean passedOnCreateState = new AtomicBoolean(false);
    private boolean twoPane = false;

    private String TAG = MainActivity.class.getSimpleName();
    private static final String DF_TAG = "DFTAG";

    @Override
    protected void onStart()
    {
        Log.d(TAG, "onStart: " + twoPane);

        super.onStart();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (findViewById(R.id.movie_detail_frame) != null)
        {
            twoPane = true;
//            if (savedInstanceState == null)
//            {
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.movie_detail_frame, MovieDetailWideFragment.newInstance(), DF_TAG)
//                        .commit();
//
//            }
            Log.d(TAG, "onCreate: sw600dp");
        }
        else
        {
            twoPane = false;
        }
        MainActivityFragment posterFragment = (MainActivityFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        posterFragment.setTwoPane(twoPane);


        Log.d(TAG, "onCreate: " + BuildConfig.THE_MOVIE_DB_API_KEY);
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/
/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/


    public interface DelegateMovieAdapterProcess<T>
    {
        void process(List<? extends T> movieList);
    }


}
