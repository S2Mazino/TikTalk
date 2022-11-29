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
        View view = LayoutInflater.from(context).inflate(R.layout.daily_weather_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WeatherDailyForecastItem weatherDailyForecastItem = weatherDailyArrayList.get(position);
        holder.dailyForecastDay.setText(weatherDailyForecastItem.getDailyForecastDay());
        Picasso.get().load(weatherDailyForecastItem.getDailyForecastIcon()).fit().centerInside().into(holder.dailyForecastIcon);
        holder.dailyForecastTemp.setText(weatherDailyForecastItem.getDailyForecastTemp());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView dailyForecastDay, dailyForecastTemp;
        private ImageView dailyForecastIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dailyForecastIcon = itemView.findViewById(R.id.dailyForecastIcon);
            dailyForecastDay = itemView.findViewById(R.id.dailyForecastDay);
            dailyForecastTemp = itemView.findViewById(R.id.dailyForecastTemp);

        }
    }
}
