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
import edu.uw.tcss450.team3.tiktalk.model.WeatherViewModel;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {
    private Context context;

    public WeatherAdapter(Context context, ArrayList<WeatherViewModel> weatherViewModelArrayList) {
        this.context = context;
        this.weatherViewModelArrayList = weatherViewModelArrayList;
    }

    private ArrayList<WeatherViewModel> weatherViewModelArrayList;


    @NonNull
    @Override
    public WeatherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.hourly_weather_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherAdapter.ViewHolder holder, int position) {
        // degree symbols
        // For Celsius degree: "\u2103"
        // For Fahrenheit degree: "\u2109"
        // For only degree symbol without C or F: "\u00B0"

        WeatherViewModel model = weatherViewModelArrayList.get(position);
        holder.temperatureTV.setText(model.getTemperature() + "\u00B0");
        Picasso.get().load(model.getIcon()).into(holder.iconIV);
        holder.timeTV.setText(model.getTime());
    }

    @Override
    public int getItemCount() {
        return weatherViewModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView timeTV, temperatureTV;
        private ImageView iconIV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            timeTV = itemView.findViewById(R.id.idTVTime);
            temperatureTV = itemView.findViewById(R.id.idTVTemperature);
            iconIV = itemView.findViewById(R.id.idIVIcon);
        }
    }
}
