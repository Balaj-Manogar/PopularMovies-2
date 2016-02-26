package baali.nano.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import baali.nano.model.Movie;

public class JsonUtils
{

    public JsonUtils()
    {
    }



    public List<Movie> parseJsonArrayToList(JSONArray result, String posterPath, String backdropPath)
    {
        List<Movie> movies = new ArrayList<>();
        Movie movie;
        for (int i = 0; i < result.length(); i++) {
            movie = new Movie();
            try {
                JSONObject currentMovieDetail = result.getJSONObject(i);
                movie.setId(currentMovieDetail.getLong("id"));
                movie.setTitle(currentMovieDetail.getString("title"));
                movie.setOriginalTitle(currentMovieDetail.getString("original_title"));
                movie.setAdult(currentMovieDetail.getBoolean("adult"));
                movie.setPosterPath(currentMovieDetail.getString("poster_path"));
                movie.setBackdropPath(currentMovieDetail.getString("backdrop_path"));
                movie.setOverview(currentMovieDetail.getString("overview"));
                movie.setReleaseDate(currentMovieDetail.getString("release_date"));
                movie.setPopularity(currentMovieDetail.getDouble("popularity"));
                movie.setVoteCount(currentMovieDetail.getInt("vote_count"));
                movie.setVideo(currentMovieDetail.getBoolean("video"));
                movie.setVoteAverage(currentMovieDetail.getString("vote_average"));
                movies.add(movie);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return movies;
    }

    public JSONArray getMovieResultsInJsonArray(String data)
    {
        JSONArray resultArray = null;
        try {
            JSONObject movieJson = new JSONObject(data);
            resultArray = movieJson.getJSONArray("results");
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return resultArray;
    }
}