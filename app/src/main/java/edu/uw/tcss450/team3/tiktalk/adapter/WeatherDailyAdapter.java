package edu.uw.tcss450.team3.tiktalk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import edu.uw.tcss450.team3.tiktalk.R;
import edu.uw.tcss450.team3.tiktalk.ui.weather.WeatherDailyForecastItem;

public class WeatherDailyAdapter extends RecyclerView.Adapter<WeatherDailyAdapter.ViewHolder> {

    private Context context;
    private ArrayList<WeatherDailyForecastItem> weatherDailyArrayList;

    public WeatherDailyAdapter(Context context, ArrayList<WeatherDailyForecastItem> weatherDailyArrayList) {
        this.context = context;
        this.weatherDailyArrayList = weatherDailyArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.weather_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WeatherDailyForecastItem weatherDailyForecastItem = weatherDailyArrayList.get(position);
        holder.timeTV.setText(weatherDailyForecastItem.getDailyForecastDay());
        holder.temperatureTV.setText(weatherDailyForecastItem.getDailyForecastTemp());

        String icon = weatherDailyForecastItem.getDailyForecastIcon();
        switch (icon) {
            case "01d":
                holder.conditionIV.setImageResource(R.drawable._01d);
                break;
            case "02d":
                holder.conditionIV.setImageResource(R.drawable._02d);
                break;
            case "03d":
                holder.conditionIV.setImageResource(R.drawable._03d);
                break;
            case "04d":
                holder.conditionIV.setImageResource(R.drawable._04d);
                break;
            case "09d":
                holder.conditionIV.setImageResource(R.drawable._09d);
                break;
            case "10d":
                holder.conditionIV.setImageResource(R.drawable._10d);
                break;
            case "11d":
                holder.conditionIV.setImageResource(R.drawable._11d);
                break;
            case "13d":
                holder.conditionIV.setImageResource(R.drawable._13d);
                break;
            case "50d":
                holder.conditionIV.setImageResource(R.drawable._50d);
                break;
            case "01n":
                holder.conditionIV.setImageResource(R.drawable._01n);
                break;
            case "02n":
                holder.conditionIV.setImageResource(R.drawable._02n);
                break;
            case "03n":
                holder.conditionIV.setImageResource(R.drawable._03n);
                break;
            case "04n":
                holder.conditionIV.setImageResource(R.drawable._04n);
                break;
            case "09n":
                holder.conditionIV.setImageResource(R.drawable._09n);
                break;
            case "10n":
                holder.conditionIV.setImageResource(R.drawable._10n);
                break;
            case "11n":
                holder.conditionIV.setImageResource(R.drawable._11n);
                break;
            case "13n":
                holder.conditionIV.setImageResource(R.drawable._13n);
                break;
            case "50n":
                holder.conditionIV.setImageResource(R.drawable._50n);
                break;
        }



    }

    @Override
    public int getItemCount() {
        return weatherDailyArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView timeTV, temperatureTV;
        private ImageView conditionIV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            timeTV = itemView.findViewById(R.id.idTVTime);
            temperatureTV = itemView.findViewById(R.id.idTVTemperature);
            conditionIV = itemView.findViewById(R.id.idIVCondition);
        }

    }
}
