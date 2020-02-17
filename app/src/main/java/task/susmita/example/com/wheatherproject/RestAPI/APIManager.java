package task.susmita.example.com.wheatherproject.RestAPI;




import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;
import task.susmita.example.com.wheatherproject.info.WeatherData;


public class APIManager {

    private static Weatherervice weatherervice;
    private static final String URL = "http://api.openweathermap.org/data/2.5";


    public interface Weatherervice {

        /*@GET("/forecast")
        void getWeatherInfo (@Query("q") String city,
                             @Query("appid") String appid,
                             Callback<WeatherData> cb);*/
        @GET("/forecast")
        Call<WeatherData> getCurrentWeather(@Query("q") String city,
                                            @Query("appid") String apikey);
    }

   /* public static Weatherervice getApiService () {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .build();

        weatherervice = retrofit.create(Weatherervice.class);

        return weatherervice;

    }*/

}