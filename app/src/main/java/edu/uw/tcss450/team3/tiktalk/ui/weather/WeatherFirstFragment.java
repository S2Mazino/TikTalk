package edu.uw.tcss450.team3.tiktalk.ui.weather;

import static android.content.Context.LOCATION_SERVICE;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
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
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.uw.tcss450.team3.tiktalk.R;
import edu.uw.tcss450.team3.tiktalk.adapter.WeatherAdapter;
import edu.uw.tcss450.team3.tiktalk.databinding.FragmentWeatherFirstBinding;
import edu.uw.tcss450.team3.tiktalk.model.WeatherViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFirstFragment extends Fragment {


    private String coorWeatherURL = "https://tiktalk-app-web-service.herokuapp.com/weather/lat-lon/";
    private String zipcodeWeatherURL = "https://tiktalk-app-web-service.herokuapp.com/weather/zipcode/";

    // Hard coded for the location --> UWT
    private static final String HARD_CODED_LATITUDE = "47.2454";
    private static final String HARD_CODED_LONGITUDE = "-122.4385";
    private static final String HARD_CODED_ZIPCODE = "98402";

    private FragmentWeatherFirstBinding mBinding;

    private RelativeLayout weatherBasedURL;
    private ProgressBar loadingPB;
    private TextView cityNameTV, temperatureTV, conditionTV;
    private RecyclerView hourlyWeatherRV, dailyWeatherRV;
    private TextInputEditText zipcodeEdit;
    private ImageView backIV, iconIV, searchIV;
    private ArrayList<WeatherViewModel> weatherViewModelArrayList;
    private WeatherAdapter weatherAdapter;
    private LocationManager locationManager;
    private int PERMISSION_CODE = 1;
    private double latitude;
    private double longitude;
    private String zipcode;

    LocationManager mLocationManager;
    Location myLocation;


    public WeatherFirstFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Using Location Manager .getSystemService in fragment
    // https://stackoverflow.com/questions/13306254/how-to-get-a-reference-to-locationmanager-inside-a-fragment
    // Using getWindow in fragment
    // https://stackoverflow.com/questions/7600858/android-fragment-and-getwindow
    // findviewbyid-in-fragment
    // https://stackoverflow.com/questions/6495898/findviewbyid-in-fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather_first, container, false);

        requireActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        weatherBasedURL = (RelativeLayout) view.findViewById(R.id.idRLHome);
        loadingPB = (ProgressBar) view.findViewById(R.id.idPBLoading);
        cityNameTV = (TextView) view.findViewById(R.id.idTVCityName);
        temperatureTV = (TextView) view.findViewById(R.id.idTVTemperature);
        conditionTV = (TextView) view.findViewById(R.id.idTVCondition);
        hourlyWeatherRV = (RecyclerView) view.findViewById(R.id.idRVWeather);
        dailyWeatherRV = (RecyclerView) view.findViewById(R.id.idRVDailyWeather);
        zipcodeEdit = (TextInputEditText) view.findViewById(R.id.idEditCity);
        backIV = (ImageView) view.findViewById(R.id.idIVBack);
        iconIV = (ImageView) view.findViewById(R.id.idIVIcon);
        searchIV = (ImageView) view.findViewById(R.id.idIVSearch);

        weatherViewModelArrayList = new ArrayList<>();
        weatherAdapter = new WeatherAdapter(getActivity(), weatherViewModelArrayList);
        hourlyWeatherRV.setAdapter(weatherAdapter);

//        locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
//        if (ActivityCompat.checkSelfPermission(getActivity(),
//                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(getActivity(),
//                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
//                    Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_CODE);
//        }

//        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        myLocation = getLastKnownLocation();
        latitude = myLocation.getLatitude();
        longitude = myLocation.getLongitude();

        getCoorWeatherdata(latitude, longitude);

        searchIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String zip = zipcodeEdit.getText().toString();
                if (zip.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter zipcode", Toast.LENGTH_SHORT).show();
                } else {
                    zipcode = zip;
                    getZipWeatherData(zipcode);
                }
            }
        });
        return view;
    }

    // finish activity in fragment
    // https://stackoverflow.com/questions/7907900/finishing-current-activity-from-a-fragment
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "Permission granted...", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Please provide the permissions", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        }
    }

    private void getCoorWeatherdata(double lat, double lon) {
        String url = coorWeatherURL + lat + "/" + lon;
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadingPB.setVisibility(View.GONE);
                weatherBasedURL.setVisibility(View.VISIBLE);
                weatherViewModelArrayList.clear();

                try {
                    String cityName = response.getString("city");
                    cityNameTV.setText(cityName);
                    String temperature = response.getJSONObject("current").getString("tempF");
                    temperatureTV.setText(temperature + "\u00B0");
                    String condition = response.getJSONObject("current").getString("condition");
                    conditionTV.setText(condition);
                    String conditionIcon = response.getJSONObject("current").getString("icon");
                    Picasso.get().load(conditionIcon).into(iconIV);

                    JSONArray hourlyArray = response.getJSONArray("hourly");

                    for (int i = 0; i < hourlyArray.length(); i++) {
                        JSONObject hourlyObj = hourlyArray.getJSONObject(i);
                        String hTime = hourlyObj.getString("hours");
                        String hTempF = String.valueOf(hourlyObj.getInt("tempF"));
                        String hIcon = hourlyObj.getString("icon");

                        weatherViewModelArrayList.add(new WeatherViewModel(hTime, hTempF, hIcon));
                    }

                    weatherAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Please enter valid zipcode...", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    private void getZipWeatherData(String zipcode) {
        String url = zipcodeWeatherURL + zipcode;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private Location getLastKnownLocation() {
        mLocationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_CODE);
            }
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() > bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }
}