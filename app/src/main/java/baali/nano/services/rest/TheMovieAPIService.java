package baali.nano.services.rest;

import baali.nano.model.MovieAPIResponse;
import baali.nano.model.MovieVideo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Balaji on 25/02/16.
 */
public interface TheMovieAPIService
{
    @GET("3/discover/movie")
    Call<MovieAPIResponse> fetchMovies(@Query("api_key") String apiKey, @Query("sort_by") String sort);

    // http://api.themoviedb.org/3/movie/83542/reviews?api_key=f33370f35e7da08f21743dd3cf447830
    @GET("3/discover/movie/{movieId}/reviews")
    Call<MovieAPIResponse> fetchMovieReview( @Path("movieId") int movieId, @Query("api_key") String apiKey);

    //http://api.themoviedb.org/3/movie/550/videos?api_key=f33370f35e7da08f21743dd3cf447830
    @GET("3/discover/movie/{movieId}/reviews")
    Call<MovieVideo> fetchMovieTrailer(@Path("movieId") int movieId, @Query("api_key") String apiKey);
}
