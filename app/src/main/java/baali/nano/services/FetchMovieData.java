package baali.nano.services;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;

import baali.nano.MainActivity;
import baali.nano.model.Movie;
import baali.nano.utils.HttpUtils;
import baali.nano.utils.JsonUtils;

/**
 * Created by Balaji on 05/01/16.
 */
public class FetchMovieData extends AsyncTask<String, Void, List<Movie>>
{

    private final HttpUtils httpUtils;
    private final JsonUtils jsonUtils;
    private final String TAG = FetchMovieData.class.getSimpleName();

    private MainActivity.DelegateMovieAdapterProcess movieDelegate;



    public FetchMovieData(){
        httpUtils = new HttpUtils();
        jsonUtils = new JsonUtils();
    }

    public FetchMovieData(MainActivity.DelegateMovieAdapterProcess delegate){
        this.movieDelegate = delegate;
        httpUtils = new HttpUtils();
        jsonUtils = new JsonUtils();
    }

    public void setMovieDelegate(MainActivity.DelegateMovieAdapterProcess movieDelegate)
    {
        this.movieDelegate = movieDelegate;
    }



    @Override
    protected List<Movie> doInBackground(String... params)
    {
        String location = params[0];
        String data = null;
        data = getData(location);

        JSONArray result = jsonUtils.getMovieResultsInJsonArray(data);
        List<Movie> movies = jsonUtils.parseJsonArrayToList(result, params[1], params[2]);

        Log.d(TAG, "Movies size: " + movies.size());


        return movies;
    }

    @Override
    protected void onPostExecute(List<Movie> movies)
    {

        movieDelegate.process(movies);
    }

    private String getData(String location)
    {
        String jsonString = null;
        HttpURLConnection connection;
        BufferedReader reader = null;
        connection = httpUtils.getConnection(location);

        InputStream is = httpUtils.getInputStream(connection);

        if (is != null) {
            reader = httpUtils.getReader(is);
            jsonString = readData(reader);

        }

        httpUtils.closeConnection(connection);
        httpUtils.closeReader(reader);
        return jsonString;
    }


    
    private String readData(BufferedReader reader)
    {
        String line;
        StringBuffer buffer = new StringBuffer();
        try {
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        if (buffer.length() == 0) {
            return null;
        }

        String jsonString = buffer.toString();
        return jsonString;
    }

}
