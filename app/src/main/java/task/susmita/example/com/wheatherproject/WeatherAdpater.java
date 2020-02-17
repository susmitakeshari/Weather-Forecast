package task.susmita.example.com.wheatherproject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import task.susmita.example.com.wheatherproject.info.TemperatureList;


public class WeatherAdpater extends BaseAdapter {
    private Context mCtx;
    private List<Map.Entry<Date, List<TemperatureList>>> waatherList;

    public WeatherAdpater(Context mCtx, List<Map.Entry<Date, List<TemperatureList>>> waatherList) {
        this.mCtx = mCtx;
        this.waatherList = waatherList;
    }

    @Override
    public int getCount() {
        return waatherList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private Map<Integer, Integer> imgMap = new TreeMap<Integer, Integer>() {
        {
            put(0, R.id.img_Morning);
            put(1, R.id.img_Afternoon);
            put(2, R.id.img_Evening);
            put(3, R.id.img_Night);
        }
    };
    private Map<Integer, Integer> txtMap = new TreeMap<Integer, Integer>() {
        {
            put(0, R.id.temp_Morning);
            put(1, R.id.temp_Afternoon);
            put(2, R.id.temp_Evening);
            put(3, R.id.temp_Night);
        }
    };

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);

        View view = inflater.inflate(R.layout.row_weather_forecast_data, null);
        List<TemperatureList> list = waatherList.get(position).getValue();
        for (int i = 0; i < list.size(); i++) {
            TemperatureList temperatureList = list.get(i);
            TextView textView = (TextView) view.findViewById(R.id.textView);
            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            TextView txt_date = (TextView) view.findViewById(R.id.txt_date);
            TextView des = (TextView) view.findViewById(R.id.des);
            ImageView img_mrng = (ImageView) view.findViewById(imgMap.get(i));
            TextView temp_mrng = (TextView) view.findViewById(txtMap.get(i));
            //des.setText(list.get(i).getWeather().get(0).getDescription());
            txt_date.setText(temperatureList.getDt_txt().split(" ")[0]);
            String temp = String.valueOf(temperatureList.getMain().getTemp());
            double temp_int = Double.parseDouble(temp);
            temp_mrng.setText(new DecimalFormat("##").format(temp_int) + (char) 0x00B0 + "c");
            Picasso.with(mCtx).load("http://api.openweathermap.org/img/w/" + temperatureList.getWeather().get(0).getIcon() + ".png").into(img_mrng);
            Log.i("imageff", "http://api.openweathermap.org/img/w/" + temperatureList.getWeather().get(0).getIcon() + ".png");

        }
        return view;
    }

}
