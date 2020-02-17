package task.susmita.example.com.wheatherproject.RestAPI;



import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;
import task.susmita.example.com.wheatherproject.info.WeatherData;

public interface WeatherService {
    @GET("forecast")
    Call<WeatherData> getCurrentWeather(@Query("q") String city,
                                        @Query("mode") String mode,
                                        @Query("appid") String apikey,
                                        @Query("units") String unit);
}
