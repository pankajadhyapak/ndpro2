package in.pankajadhyapak.popularmovies.api;

import in.pankajadhyapak.popularmovies.models.ApiResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieApi {

    @GET("top_rated")
    Call<ApiResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("popular")
    Call<ApiResponse> getPopularMovies(@Query("api_key") String apiKey);

}
