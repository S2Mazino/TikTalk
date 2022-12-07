package edu.uw.tcss450.team3.tiktalk.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

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

import edu.uw.tcss450.team3.tiktalk.R;
import edu.uw.tcss450.team3.tiktalk.databinding.FragmentHomeBinding;
import edu.uw.tcss450.team3.tiktalk.databinding.FragmentSignInBinding;
import edu.uw.tcss450.team3.tiktalk.model.LocationViewModel;
import edu.uw.tcss450.team3.tiktalk.model.UserInfoViewModel;
import edu.uw.tcss450.team3.tiktalk.model.WeatherRVModal;
import edu.uw.tcss450.team3.tiktalk.ui.auth.signin.SignInFragmentDirections;
import edu.uw.tcss450.team3.tiktalk.ui.weather.WeatherDailyForecastItem;

/*
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    private FragmentHomeBinding binding;
    private UserInfoViewModel mUserModel;


    // Current weather for the device location
    private String coorWeatherURL = "https://tcss450-2022au-group3.herokuapp.com/weather/lat-lon/";
    private String latitude;
    private String longitude;
    private TextView cityNameTV, temperatureTV, conditionTV;
    private ImageView iconIV;
    private RequestQueue mRequestWeatherQueue;
    private RelativeLayout homeRL;
    private ProgressBar loadingPB;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mUserModel = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
        binding.nickName.setText(mUserModel.getNickname());

        // Weather section
        cityNameTV = view.findViewById(R.id.idTVCityName);
        temperatureTV = view.findViewById(R.id.idTVTemperature);
        conditionTV = view.findViewById(R.id.idTVCondition);
        iconIV = view.findViewById(R.id.idIVIcon);
        homeRL = view.findViewById(R.id.idRLHome);
        loadingPB = view.findViewById(R.id.idPBLoading);


        LocationViewModel model = new ViewModelProvider(getActivity())
                .get(LocationViewModel.class);
        model.addLocationObserver(getViewLifecycleOwner(), location -> {
            if (location != null) {
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
                latitude = String.valueOf(location.getLatitude());
                longitude = String.valueOf(location.getLongitude());
                getWeatherData(location);
            }
        });

    }

    private void getWeatherData(Location location) {
        latitude = String.valueOf(location.getLatitude());
        longitude = String.valueOf(location.getLongitude());
        mRequestWeatherQueue = Volley.newRequestQueue(getActivity());
        String weatherURL = coorWeatherURL + latitude + "/" + longitude;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, weatherURL, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onResponse(JSONObject response) {
                        loadingPB.setVisibility(View.GONE);
                        homeRL.setVisibility(View.VISIBLE);
                        try {
                            String cityName = response.getString("city");
                            cityNameTV.setText(cityName);

                            System.out.println(cityName);

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
        mRequestWeatherQueue.add(request);
    }


}