package in.pankajadhyapak.popularmovies.Api;

import in.pankajadhyapak.popularmovies.models.ApiResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieApi {
    @GET("movie")
    Call<ApiResponse> getMovies(@Query("sort_by") String sort,
                                @Query("api_key") String apiKey);
}
