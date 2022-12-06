package edu.uw.tcss450.team3.tiktalk.ui.weather;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import edu.uw.tcss450.team3.tiktalk.R;
import edu.uw.tcss450.team3.tiktalk.databinding.FragmentWeatherSecondBinding;
import edu.uw.tcss450.team3.tiktalk.model.LocationViewModel;

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
public class WeatherSecondFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private String coorWeatherURL = "https://tcss450-2022au-group3.herokuapp.com/weather/lat-lon/";
    private String zipcodeWeatherURL= "https://tcss450-2022au-group3.herokuapp.com/weather/zipcode/";

    // Hard coded for the location --> UWT
    private static final double HARD_CODED_LATITUDE = 47.2454;
    private static final double HARD_CODED_LONGITUDE = -122.4385;
    private static final String HARD_CODED_ZIPCODE = "98402";

    private WeatherViewModel mWeatherViewModel;
    private FragmentWeatherFirstBinding mBinding;

    private String latitude;
    private String longitude;
    private LocationViewModel mModel;
    private GoogleMap mMap;


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

    public WeatherSecondFragment(){
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        mBinding = FragmentWeatherFirstBinding.inflate(inflater);
//        // Inflate the layout for this fragment
//        return mBinding.getRoot();
        return inflater.inflate(R.layout.fragment_weather_second, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentWeatherSecondBinding binding = FragmentWeatherSecondBinding.bind(getView());
        //getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        //add this fragment as the OnMapReadyCallback -> See onMapReady()
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        LocationViewModel model = new ViewModelProvider(getActivity())
                .get(LocationViewModel.class);
        model.addLocationObserver(getViewLifecycleOwner(), location -> {
            if (location != null) {
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                googleMap.setMyLocationEnabled(true);
                final LatLng c = new LatLng(location.getLatitude(), location.getLongitude());

                latitude = String.valueOf(location.getLatitude());
                longitude = String.valueOf(location.getLongitude());

                //Zoom levels are from 2.0f (zoomed out) to 21.f (zoomed in)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(c, 15.0f));
            }
        });
        mMap.setOnMapClickListener(this);

    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        Log.d("LAT/LONG", latLng.toString());
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("New Marker"));
        mMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                        latLng, mMap.getCameraPosition().zoom));

    }

    private void getWeatherData(Location location) {

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
                                String iconURL = hourlyData.getString("icon");
                                String temperature = String.valueOf(hourlyData.getInt("tempF"));
                                hourlyForecastArrayList.add(new WeatherRVModal(time, iconURL, temperature));
                            }

                            JSONArray jsonDailyArray = response.getJSONArray("daily");
                            for(int i = 0; i < jsonDailyArray.length(); i++) {
                                JSONObject dailyData = jsonDailyArray.getJSONObject(i);
                                String day = dailyData.getString("day");
                                String iconURL = dailyData.getString("icon");
                                String maxTemp = dailyData.getString("maxTempF");
                                String minTemp = dailyData.getString("minTempF");
                                String temp = maxTemp + "°" + " / " + minTemp + "°";
                                dailyForecastArrayList.add(new WeatherDailyForecastItem(day, iconURL, temp));
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
