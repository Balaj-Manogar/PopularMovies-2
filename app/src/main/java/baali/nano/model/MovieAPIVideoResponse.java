package baali.nano.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Balaji on 25/02/16.
 */
public class MovieAPIVideoResponse
{
    @SerializedName("results")
    public List<MovieVideo> apiMovieVideosList = new ArrayList<>();
}
