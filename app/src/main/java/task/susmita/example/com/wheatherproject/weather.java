package task.susmita.example.com.wheatherproject;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.AdapterViewFlipper;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.stream.Collectors;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import task.susmita.example.com.wheatherproject.info.TemperatureList;
import task.susmita.example.com.wheatherproject.info.WeatherData;
import task.susmita.example.com.wheatherproject.RestAPI.WeatherService;


public class weather extends AppCompatActivity {
    private static final int locationAccess = 11;
    private static String currlat, currLong, city;
    String API_KEY="74a46b05e04b12391db72f52d35f9460";
    private static final String URL = "http://api.openweathermap.org/data/2.5/";
    TextView viewCity, viewTempearture, viewTime;
    ImageView viewMainImg;
    TextView txt_date;
    ProgressDialog pDialog;
    public java.util.List weatherList;
    public java.util.List weatherdat;
    String  CurrentDate;
    private AdapterViewFlipper adapterViewFlipper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        viewCity = (TextView) findViewById(R.id.txtCityName);
        viewTempearture = (TextView) findViewById(R.id.txtTemperature);
        viewTime = (TextView) findViewById(R.id.txtTime);
        viewMainImg = (ImageView) findViewById(R.id.imgWeatherType);
        adapterViewFlipper = (AdapterViewFlipper) findViewById(R.id.adapterViewFlipper);
        txt_date=(TextView)findViewById(R.id.txt_date);


        setNextDays();

        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setCurrentTime();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        t.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please wait..");
        pDialog.show();
        getCityName();
        setCurrentTime();
    }

    private void getCityName() {
        if (hasPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            try {
                GPSTracker gpsTracker = new GPSTracker(this);
                if (gpsTracker.getIsGPSTrackingEnabled()) {
                    currlat = String.valueOf(gpsTracker.latitude);
                    currLong = String.valueOf(gpsTracker.longitude);
                    city = gpsTracker.getLocality(this);
                    Log.d("currlat", currlat + " currLong" + currLong + " city" + city);
                    if (city != null && city != "")
                        viewCity.setText(city.toUpperCase());
                    // Toast.makeText(this, "Lat " + currlat + "long " + currlat + "city " + city, Toast.LENGTH_LONG).show();
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    WeatherService weatherervice = retrofit.create(WeatherService.class);
                    Call<WeatherData> call = weatherervice.getCurrentWeather(city+",in","json",API_KEY,"metric");

                    call.enqueue(new Callback<WeatherData>() {
                        @Override
                        public void onResponse(retrofit.Response<WeatherData> response, Retrofit retrofit) {
                            //  Toast.makeText(weather.this, "success"+response.toString(), Toast.LENGTH_LONG).show();
                            pDialog.dismiss();
                            if(response.body() != null) {
                                Log.d("status", response.message());
                                Log.d("message", response.body().getMessage());
                                viewCity.setText(response.body().getCity().getName());
                                txt_date.setText(response.body().getList().get(0).getDt_txt().split(" ")[0]);
                                String climateDesciption = response.body().getList().get(0).getWeather().get(0).getDescription();
                                Picasso.with(weather.this).load("http://api.openweathermap.org/img/w/"+response.body().getList().get(0).getWeather().get(0).getIcon()+".png").into(viewMainImg);
                                String temp=String.valueOf(response.body().getList().get(0).getMain().getTemp());
                                double temp_int=Double.parseDouble(temp);
                                viewTempearture.setText(new DecimalFormat("##").format(temp_int)+(char) 0x00B0+"c");
                                weatherList = new ArrayList<>();
                                java.util.List<TemperatureList> datalist = response.body().getList();
                                Log.i("jjjj", String.valueOf(datalist.size()));
                                final Map<Date,List<TemperatureList>> map = new TreeMap<>();
                                List<TemperatureList> list = new ArrayList<>();
                                final Calendar calendar = Calendar.getInstance();
                                for (TemperatureList temperatureList:datalist) {
                                    calendar.setTime(temperatureList.getDate());
                                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                                    boolean isCandidate = hour == 6 || hour ==12 || hour == 18 || hour == 21;
                                    calendar.set(Calendar.HOUR_OF_DAY,0);
                                    calendar.set(Calendar.MINUTE,0);
                                    calendar.set(Calendar.SECOND,0);
                                    calendar.set(Calendar.MILLISECOND,0);
                                    Date date  = calendar.getTime();
                                    if(!map.containsKey(date))
                                        map.put(date,new ArrayList<TemperatureList>());
                                    if(isCandidate)
                                        map.get(date).add(temperatureList);
                                }
                                Iterator<Map.Entry<Date,List<TemperatureList>>> iterator = map.entrySet().iterator();
                                while (iterator.hasNext()){
                                    List<TemperatureList> tmp = iterator.next().getValue();
                                    tmp.sort(new Comparator<TemperatureList>() {
                                        @Override
                                        public int compare(TemperatureList o1, TemperatureList o2) {
                                            return o1.getDate().compareTo(o2.getDate());
                                        }
                                    });
                                    list.add(tmp.get(0));
                                }
                                //list.remove(0);
                                List<Map.Entry<java.util.Date,List<TemperatureList>>> xyz = new ArrayList<>(map.entrySet());
                                xyz.remove(0);
                                WeatherAdpater adapter = new WeatherAdpater(getApplicationContext(), xyz);
                                //adding it to adapterview flipper
                                adapterViewFlipper.setAdapter(adapter);
                                adapterViewFlipper.setFlipInterval(10000);
                                adapterViewFlipper.startFlipping();
                            }
                        }
                        @Override
                        public void onFailure(Throwable t) {
                            Log.d("Error", t.toString());
                        }
                    });


                } else {
                    gpsTracker.showSettingsAlert();
                }
            } catch (Exception e) {
                String clickmessage = e.getMessage();
                Toast.makeText(this, clickmessage, Toast.LENGTH_SHORT).show();
            }
        } else {
            this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, locationAccess);
        }
    }

    private void setNextDays(){
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        CurrentDate = inputFormat.format(new Date());
        Log.d("DATE",CurrentDate);
        String nextDays[] = new String[4];
        Date todaysDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(todaysDate);
        String day = "";
        for(int j = 0;j<4;j++) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            int i = calendar.get(Calendar.DAY_OF_WEEK);
            if(i==1){
                day = "Sunday";
            }else if(i==2){
                day = "Monday";
            }else if(i==3){
                day = "Tuesday";
            }else if(i==4){
                day = "Wednesday";
            }else if(i==5){
                day = "Thursday";
            }else if(i==6){
                day = "Friday";
            }else if(i==7){
                day = "Saturday";
            }
            nextDays[j] = day;
            Log.d("next day", "" + day);
        }
        //  viewDay1.setText(nextDays[0]);
        // viewDay2.setText(nextDays[1]);
        // viewDay3.setText(nextDays[2]);
        //viewDay4.setText(nextDays[3]);
    }
    private void setCurrentTime() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+05:30"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("HH:mm a");
        date.setTimeZone(TimeZone.getTimeZone("GMT+05:30"));
        String localTime = date.format(currentLocalTime);
        viewTime.setText(localTime.toUpperCase());
        //SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    }

    public static boolean hasPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case locationAccess:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        GPSTracker gpsTracker = new GPSTracker(this);
                        if (gpsTracker.getIsGPSTrackingEnabled()) {
                            currlat = String.valueOf(gpsTracker.latitude);
                            currLong = String.valueOf(gpsTracker.longitude);
                            city = gpsTracker.getLocality(this);
                            Log.d("currlat", currlat + " currLong" + currLong + " city" + city);
                        } else {
                            gpsTracker.showSettingsAlert();
                        }
                    } catch (Exception e) {
                        String clickmessage = "OnClickevent Exception" + e.getMessage();
                        Toast.makeText(this, clickmessage, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    new AlertDialog.Builder(this)
                            .setTitle("Permission Needed")
                            .setMessage("Please grant the permission")
                            .setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    goToSettings();
                                }

                            })
                            .setNegativeButton("Denied", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }

                            })
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void goToSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.EMPTY.fromParts("package", this.getPackageName(), null));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
