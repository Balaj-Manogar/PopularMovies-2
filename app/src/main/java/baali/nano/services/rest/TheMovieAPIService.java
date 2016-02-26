package baali.nano.services.rest;

import baali.nano.model.MovieAPIResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Balaji on 25/02/16.
 */
public interface TheMovieAPIService
{
    @GET("3/discover/movie")
    Call<MovieAPIResponse> fetchMovies(@Query("api_key") String apiKey, @Query("sort_by") String sort);
}
