package edu.uw.tcss450.team3.tiktalk.ui.weather;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.uw.tcss450.team3.tiktalk.R;
import edu.uw.tcss450.team3.tiktalk.adapter.WeatherDailyAdapter;
import edu.uw.tcss450.team3.tiktalk.adapter.WeatherHourlyAdapter;
import edu.uw.tcss450.team3.tiktalk.model.WeatherRVModal;


public class WeatherFromMapFragment extends Fragment {

    private String coorWeatherURL = "https://tcss450-2022au-group3.herokuapp.com/weather/lat-lon/";
    private String latitude;
    private String longitude;

    private RelativeLayout homeRL;
    private ProgressBar loadingPB;
    private TextView cityNameTV, temperatureTV, conditionTV;
    private ImageView backIV,iconIV;



    private RecyclerView mDailyWeatherForecast;
    private RecyclerView mHourlyWeatherForecast;
    private WeatherDailyAdapter mWeatherDailyAdapter;
    private WeatherHourlyAdapter mWeatherHourlyAdapter;
    private ArrayList<WeatherDailyForecastItem> dailyForecastArrayList;
    private ArrayList<WeatherRVModal> hourlyForecastArrayList;
    private RequestQueue mRequestDailyQueue;
    private RequestQueue mRequestHourlyQueue;
    private int PERMISSION_CODE = 1;
    private WeatherViewModel mWeatherViewModel;

    View rootView;

    public WeatherFromMapFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mWeatherViewModel = provider.get(WeatherViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_weather_from_map, container, false);
        Bundle bundle = this.getArguments();
        latitude = bundle.getString("latitude");
        longitude = bundle.getString("longitude");
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        homeRL = view.findViewById(R.id.idRLHome);
        loadingPB = view.findViewById(R.id.idPBLoading);
        cityNameTV = view.findViewById(R.id.idTVCityName);
        temperatureTV = view.findViewById(R.id.idTVTemperature);
        conditionTV = view.findViewById(R.id.idTVCondition);
        backIV = view.findViewById(R.id.idIVBackToMap);
        iconIV = view.findViewById(R.id.idIVIcon);

        mHourlyWeatherForecast = view.findViewById(R.id.idRVHourlyWeather);
        hourlyForecastArrayList = new ArrayList<>();
        mWeatherHourlyAdapter = new WeatherHourlyAdapter(getActivity(), hourlyForecastArrayList);
        mHourlyWeatherForecast.setAdapter(mWeatherHourlyAdapter);

        mDailyWeatherForecast = view.findViewById(R.id.idRVDailyWeather);
        dailyForecastArrayList = new ArrayList<>();
        mWeatherDailyAdapter = new WeatherDailyAdapter(getActivity(), dailyForecastArrayList);
        mDailyWeatherForecast.setAdapter(mWeatherDailyAdapter);

        getLatLonWeatherData(latitude, longitude);

        backIV.setOnClickListener(new View.OnClickListener() {

            WeatherSecondFragment weatherSecondFragment = new WeatherSecondFragment();
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.CurrentWeatherMap, weatherSecondFragment).commit();
            }
        });

    }

    private void getLatLonWeatherData(String latitude, String longitude) {

        String weatherURL = coorWeatherURL + latitude + "/" + longitude;
        mRequestDailyQueue = Volley.newRequestQueue(getActivity());
        mRequestHourlyQueue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, weatherURL, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onResponse(JSONObject response) {
                        loadingPB.setVisibility(View.GONE);
                        homeRL.setVisibility(View.VISIBLE);
                        hourlyForecastArrayList.clear();
                        dailyForecastArrayList.clear();
                        try {

                            String cityName = response.getString("city");
                            cityNameTV.setText(cityName);

                            JSONObject jsonCurrentObject = response.getJSONObject("current");
                            String currTemp = jsonCurrentObject.getString("tempF");
                            temperatureTV.setText(currTemp + "°");
                            String currCondition = jsonCurrentObject.getString("condition");
                            conditionTV.setText(currCondition);
                            String currIconValue = jsonCurrentObject.getString("iconValue");

                            switch (currIconValue) {
                                case "01d":
                                    iconIV.setImageResource(R.drawable._01d);
                                    break;
                                case "02d":
                                    iconIV.setImageResource(R.drawable._02d);
                                    break;
                                case "03d":
                                    iconIV.setImageResource(R.drawable._03d);
                                    break;
                                case "04d":
                                    iconIV.setImageResource(R.drawable._04d);
                                    break;
                                case "09d":
                                    iconIV.setImageResource(R.drawable._09d);
                                    break;
                                case "10d":
                                    iconIV.setImageResource(R.drawable._10d);
                                    break;
                                case "11d":
                                    iconIV.setImageResource(R.drawable._11d);
                                    break;
                                case "13d":
                                    iconIV.setImageResource(R.drawable._13d);
                                    break;
                                case "50d":
                                    iconIV.setImageResource(R.drawable._50d);
                                    break;
                                case "01n":
                                    iconIV.setImageResource(R.drawable._01n);
                                    break;
                                case "02n":
                                    iconIV.setImageResource(R.drawable._02n);
                                    break;
                                case "03n":
                                    iconIV.setImageResource(R.drawable._03n);
                                    break;
                                case "04n":
                                    iconIV.setImageResource(R.drawable._04n);
                                    break;
                                case "09n":
                                    iconIV.setImageResource(R.drawable._09n);
                                    break;
                                case "10n":
                                    iconIV.setImageResource(R.drawable._10n);
                                    break;
                                case "11n":
                                    iconIV.setImageResource(R.drawable._11n);
                                    break;
                                case "13n":
                                    iconIV.setImageResource(R.drawable._13n);
                                    break;
                                case "50n":
                                    iconIV.setImageResource(R.drawable._50n);
                                    break;
                            }

                            JSONArray jsonHourlyArray = response.getJSONArray("hourly");
                            for(int i = 0; i < jsonHourlyArray.length(); i++) {
                                JSONObject hourlyData = jsonHourlyArray.getJSONObject(i);
                                String time = hourlyData.getString("hours");
                                String iconValue = hourlyData.getString("iconValue");
                                String temperature = String.valueOf(hourlyData.getInt("tempF"));
                                hourlyForecastArrayList.add(new WeatherRVModal(time, iconValue, temperature));
                            }

                            JSONArray jsonDailyArray = response.getJSONArray("daily");
                            for(int i = 0; i < jsonDailyArray.length(); i++) {
                                JSONObject dailyData = jsonDailyArray.getJSONObject(i);
                                String day = dailyData.getString("day");
                                String iconValue = dailyData.getString("iconValue");
                                String maxTemp = dailyData.getString("maxTempF");
                                String minTemp = dailyData.getString("minTempF");
                                String temp = maxTemp + "°" + " / " + minTemp + "°";
                                dailyForecastArrayList.add(new WeatherDailyForecastItem(day, iconValue, temp));
                            }
                            mWeatherHourlyAdapter.notifyDataSetChanged();
                            mWeatherDailyAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Invalid location", Toast.LENGTH_SHORT).show();
            }
        });
        mRequestHourlyQueue.add(request);
        mRequestDailyQueue.add(request);
    }
}