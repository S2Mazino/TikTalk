package edu.uw.tcss450.team3.tiktalk.ui.weather;

import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import edu.uw.tcss450.team3.tiktalk.R;
import edu.uw.tcss450.team3.tiktalk.adapter.WeatherDailyAdapter;
import edu.uw.tcss450.team3.tiktalk.adapter.WeatherHourlyAdapter;
import edu.uw.tcss450.team3.tiktalk.databinding.FragmentWeatherFirstBinding;
import edu.uw.tcss450.team3.tiktalk.model.LocationViewModel;
import edu.uw.tcss450.team3.tiktalk.model.WeatherRVModal;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFirstFragment extends Fragment {

    private LocationViewModel mModel;

    private String coorWeatherURL = "https://tcss450-2022au-group3.herokuapp.com/weather/lat-lon/";
    private String zipcodeWeatherURL= "https://tcss450-2022au-group3.herokuapp.com/weather/zipcode/";

    // Hard coded for the location --> UWT
    private static final String HARD_CODED_LATITUDE = "47.2454";
    private static final String HARD_CODED_LONGITUDE = "-122.4385";
    private static final String HARD_CODED_ZIPCODE = "98402";

    private WeatherViewModel mWeatherViewModel;
    private FragmentWeatherFirstBinding mBinding;

    private String latitude;
    private String longitude;

    private RelativeLayout homeRL;
    private ProgressBar loadingPB;
    private TextView cityNameTV, temperatureTV, conditionTV;
    private TextInputEditText cityEdt;
    private ImageView backIV,iconIV, searchIV;

    private RecyclerView mDailyWeatherForecast;
    private RecyclerView mHourlyWeatherForecast;
    private WeatherDailyAdapter mWeatherDailyAdapter;
    private WeatherHourlyAdapter mWeatherHourlyAdapter;
    private ArrayList<WeatherDailyForecastItem> dailyForecastArrayList;
    private ArrayList<WeatherRVModal> hourlyForecastArrayList;
    private RequestQueue mRequestDailyQueue;
    private RequestQueue mRequestHourlyQueue;
    private int PERMISSION_CODE = 1;




    public WeatherFirstFragment(){
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
//        mBinding = FragmentWeatherFirstBinding.inflate(inflater);
//        // Inflate the layout for this fragment
//        return mBinding.getRoot();
        return inflater.inflate(R.layout.fragment_weather_first, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentWeatherFirstBinding binding = FragmentWeatherFirstBinding.bind(getView());
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        homeRL = view.findViewById(R.id.idRLHome);
        loadingPB = view.findViewById(R.id.idPBLoading);
        cityNameTV = view.findViewById(R.id.idTVCityName);
        temperatureTV = view.findViewById(R.id.idTVTemperature);
        conditionTV = view.findViewById(R.id.idTVCondition);
        cityEdt = view.findViewById(R.id.idEdtCity);
        backIV = view.findViewById(R.id.idIVBack);
        iconIV = view.findViewById(R.id.idIVIcon);
        searchIV = view.findViewById(R.id.idIVSearch);

        mHourlyWeatherForecast = view.findViewById(R.id.idRVHourlyWeather);
        hourlyForecastArrayList = new ArrayList<>();
        mWeatherHourlyAdapter = new WeatherHourlyAdapter(getActivity(), hourlyForecastArrayList);
        mHourlyWeatherForecast.setAdapter(mWeatherHourlyAdapter);

        mDailyWeatherForecast = view.findViewById(R.id.idRVDailyWeather);
        dailyForecastArrayList = new ArrayList<>();
        mWeatherDailyAdapter = new WeatherDailyAdapter(getActivity(), dailyForecastArrayList);
        mDailyWeatherForecast.setAdapter(mWeatherDailyAdapter);


        // Get the lat/lon from the device access permission
        mModel = new ViewModelProvider(getActivity())
                .get(LocationViewModel.class);
        mModel.addLocationObserver(getViewLifecycleOwner(), location -> {
            getWeatherData(location);

            searchIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String zipcode = cityEdt.getText().toString();
                    if (zipcode.isEmpty()) {
                        Toast.makeText(getActivity(), "Please enter the zipcode", Toast.LENGTH_SHORT).show();
                    } else {
                        getZipcodeWeatherData(zipcode);
                    }
                }
            });

        });


    }

    private void getZipcodeWeatherData(String zipcode) {

    }


    private void getWeatherData(Location location) {

//        latitude = HARD_CODED_LATITUDE;
//        longitude = HARD_CODED_LONGITUDE;
        latitude = String.valueOf(location.getLatitude());
        longitude = String.valueOf(location.getLongitude());
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
                            String currIconURL = jsonCurrentObject.getString("icon");
                            Picasso.get().load(currIconURL).into(iconIV);

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