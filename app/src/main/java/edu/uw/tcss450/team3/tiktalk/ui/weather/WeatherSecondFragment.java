package edu.uw.tcss450.team3.tiktalk.ui.weather;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import edu.uw.tcss450.team3.tiktalk.R;
import edu.uw.tcss450.team3.tiktalk.databinding.FragmentWeatherSecondBinding;
import edu.uw.tcss450.team3.tiktalk.model.LocationViewModel;

/**
 * A simple {@link Fragment} subclass.
 * public class WeatherSecondFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener
 */
public class WeatherSecondFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private String coorWeatherURL = "https://tcss450-2022au-group3.herokuapp.com/weather/lat-lon/";
    private String zipcodeWeatherURL = "https://tcss450-2022au-group3.herokuapp.com/weather/zipcode/";

    // Hard coded for the location --> UWT
    private static final double HARD_CODED_LATITUDE = 47.2454;
    private static final double HARD_CODED_LONGITUDE = -122.4385;

    private double localLat;
    private double localLon;
    private String latitude;
    private String longitude;
    private GoogleMap mMap;
    boolean doubleBackToExitPressedOnce = false;

    View rootView;
    FragmentWeatherSecondBinding mBinding;
    WeatherSecondFragment weatherSecondFragment;


    public WeatherSecondFragment() {
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
        rootView =  inflater.inflate(R.layout.fragment_weather_second, container, false);
        return rootView;
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
                localLat = location.getLatitude();
                localLon = location.getLongitude();
            }
        });
        LatLng center = new LatLng(localLat, localLon);
//        LatLng center = new LatLng(HARD_CODED_LATITUDE, HARD_CODED_LONGITUDE);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title("My position");
        markerOptions.position(center);
        googleMap.addMarker(markerOptions);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(center, 15.0f);
        googleMap.animateCamera(cameraUpdate);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setAllGesturesEnabled(true);
        googleMap.getUiSettings().setScrollGesturesEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(false);
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

        mMap.setOnMapClickListener(this);

    }



    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        Log.d("LAT/LONG", latLng.toString());
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("New Marker"));

        // Get latitude and logitude from the marker
        String[] latlong = latLng.toString().substring(10, latLng.toString().length() - 1).split(",");
        latitude = String.valueOf(Double.parseDouble(latlong[0]));
        longitude = String.valueOf(Double.parseDouble(latlong[1]));

//        System.out.println("Lat: " + latitude);
//        System.out.println("Lon: " + longitude);

        Bundle bundle =  new Bundle();
        bundle.putString("latitude", latitude);
        bundle.putString("longitude", longitude);

        WeatherFromMapFragment weatherFromMapFragment = new WeatherFromMapFragment();
        weatherFromMapFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.weatherSecondFragmentMap, weatherFromMapFragment).commit();

    }
}
