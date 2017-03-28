package in.pankajadhyapak.popularmovies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface MovieApi {
    @GET("movie")
    Call<ApiResponse> getMovies(@Query("sort_by") String sort,
                                @Query("api_key") String apiKey);
}
