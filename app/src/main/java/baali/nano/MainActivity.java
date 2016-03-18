package baali.nano;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.List;


public class MainActivity extends AppCompatActivity
{
    private boolean twoPane = false;

    private String TAG = MainActivity.class.getSimpleName();
    private static final String DF_TAG = "DFTAG";

    @Override
    protected void onStart()
    {
        super.onStart();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        twoPane = (findViewById(R.id.movie_detail_frame) != null) ? true : false;

        MainActivityFragment posterFragment = (MainActivityFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        posterFragment.setTwoPane(twoPane);
    }



    public interface DelegateMovieAdapterProcess<T>
    {
        void process(List<? extends T> movieList);
    }


}
