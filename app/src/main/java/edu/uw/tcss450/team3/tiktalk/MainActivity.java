package edu.uw.tcss450.team3.tiktalk;
//comment
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import edu.uw.tcss450.team3.tiktalk.model.LocationViewModel;
import edu.uw.tcss450.team3.tiktalk.model.PushyTokenViewModel;
import edu.uw.tcss450.team3.tiktalk.model.UserInfoViewModel;
import edu.uw.tcss450.team3.tiktalk.ui.auth.changepassword.ChangePasswordFragment;
import edu.uw.tcss450.team3.tiktalk.ui.chat.chatRoom.ChatFragment;
import edu.uw.tcss450.team3.tiktalk.ui.chat.chatList.ChatListFragment;
import edu.uw.tcss450.team3.tiktalk.ui.connection.ConnectionFragment;
import edu.uw.tcss450.team3.tiktalk.ui.connection.ContactFragment;
import edu.uw.tcss450.team3.tiktalk.ui.home.HomeFragment;
import edu.uw.tcss450.team3.tiktalk.ui.weather.WeatherFirstFragment;
import edu.uw.tcss450.team3.tiktalk.ui.weather.WeatherFragment;
import edu.uw.tcss450.team3.tiktalk.ui.weather.WeatherSecondFragment;

public class MainActivity extends AppCompatActivity {

    // Variable for the location permission

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    // A constant int for the permissions request code. Must be a 16 bit number
    private static final int MY_PERMISSIONS_LOCATIONS = 8414;
    private LocationRequest mLocationRequest;
    //Use a FusedLocationProviderClient to request the location
    private FusedLocationProviderClient mFusedLocationClient;
    // Will use this call back to decide what to do when a location change is detected
    private LocationCallback mLocationCallback;
    //The ViewModel that will store the current location
    private LocationViewModel mLocationModel;

    private UserInfoViewModel mUserInfoModel;

    //all varibles

    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment = new HomeFragment();
    ConnectionFragment connectionFragment = new ConnectionFragment();
    ContactFragment contactFragment = new ContactFragment();
    ChatListFragment chatListFragment = new ChatListFragment();
    WeatherFirstFragment weatherFirstFragment = new WeatherFirstFragment();
    ChatFragment chatFragment = new ChatFragment();
    WeatherSecondFragment weatherSecondFragment = new WeatherSecondFragment();
    WeatherFragment weatherFragment = new WeatherFragment();
    ChangePasswordFragment changePasswordFragment = new ChangePasswordFragment();

    AppBarConfiguration mAppBarConfiguration;

    // Saving state of our app
    // using SharedPreferences
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    boolean isDarkModeOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // setting default theme based on user's phone settings
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.TikTalkDark);
        } else {
            setTheme(R.style.TikTalkLight);
        }

        // Saving state of our app
        // using SharedPreferences
        sharedPreferences = getSharedPreferences(
                "sharedPrefs", MODE_PRIVATE);
        editor
                = sharedPreferences.edit();

        editor.putBoolean("isDarkModeOn", true);
        editor.commit();
        isDarkModeOn
                = sharedPreferences
                .getBoolean(
                        "isDarkModeOn", true);


        MainActivityArgs args = MainActivityArgs.fromBundle(getIntent().getExtras());
        mUserInfoModel = new ViewModelProvider(this,
                new UserInfoViewModel.UserInfoViewModelFactory(args.getJwt()))
                .get(UserInfoViewModel.class);


        setContentView(R.layout.activity_main);

//        BottomNavigationView navView = findViewById(R.id.bottom_navigation);
//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.homeIcon, R.id.connectionIcon).build();
//
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navView, navController);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.layout_container, homeFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.homeIcon:
                        getSupportFragmentManager().beginTransaction().replace(R.id.layout_container, homeFragment).commit();
                        return true;
                    case R.id.connectionIcon:
                        getSupportFragmentManager().beginTransaction().replace(R.id.layout_container, connectionFragment).commit();
                        return true;
                    case R.id.chatIcon:
                        getSupportFragmentManager().beginTransaction().replace(R.id.layout_container, chatListFragment).commit();
                        //getSupportFragmentManager().beginTransaction().replace(R.id.layout_container, chatFragment).commit();
                        return true;
                    case R.id.weatherIcon:
                        getSupportFragmentManager().beginTransaction().replace(R.id.layout_container, weatherFragment).commit();
                        // getSupportFragmentManager().beginTransaction().replace(R.id.layout_container, weatherFirstFragment).commit();
                         //getSupportFragmentManager().beginTransaction().replace(R.id.layout_container, weatherSecondFragment).commit();
                        return true;
                    default:
                        getSupportFragmentManager().beginTransaction().replace(R.id.layout_container, homeFragment).commit();
                        return true;
                }
            }
        });

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION
                            , Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_LOCATIONS);
        } else {
            //The user has already allowed the use of Locations. Get the current location.
            requestLocation();
        }

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    // ...
                    Log.d("LOCATION UPDATE!", location.toString());
                    if (mLocationModel == null) {
                        mLocationModel = new ViewModelProvider(MainActivity.this)
                                .get(LocationViewModel.class);
                    }
                    mLocationModel.setLocation(location);
                }
            };
        };
        createLocationRequest();
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_LOCATIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // locations-related task you need to do.
                    requestLocation();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.d("PERMISSION DENIED", "Nothing to see or do here.");

                    //Shut down the app. In production release, you would let the user
                    //know why the app is shutting down...maybe ask for permission again?
                    finishAndRemoveTask();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void requestLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d("REQUEST LOCATION", "User did NOT allow permission to request location!");
        } else {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                Log.d("LOCATION", location.toString());
                                if (mLocationModel == null) {
                                    mLocationModel = new ViewModelProvider(MainActivity.this)
                                            .get(LocationViewModel.class);
                                }
                                mLocationModel.setLocation(location);
                            } else {
                                Log.d("LOCATION", "No Location retrieved. Hopefully this is" +
                                        " resolved in a later step");
                            }
                        }
                    });
        }
    }

    /**
     * Create and configure a Location Request used when retrieving location updates
     */
    private void createLocationRequest() {
        mLocationRequest = LocationRequest.create();
        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //startLocationUpdates();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //stopLocationUpdates();
    }

    /**
     * Requests location updates from the FusedLocationApi.
     */
    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback,
                    null /* Looper */);
        }
    }
    /**
     * Removes location updates from the FusedLocationApi.
     */
    private void stopLocationUpdates() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drop_down, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sign_out) {
            signOut();
            return true;
        }
        if (id == R.id.action_change_password) {
            Bundle bundle = new Bundle();
            bundle.putInt("memberID", mUserInfoModel.getmMemberId());
            changePasswordFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.layout_container, changePasswordFragment,null).commit();
            return true;
        }
        if (id == R.id.action_change_theme) {
            if (isDarkModeOn) {
                // if dark mode is on it
                // will turn it off
                AppCompatDelegate
                        .setDefaultNightMode(
                                AppCompatDelegate
                                        .MODE_NIGHT_NO);
                setTheme(R.style.TikTalkLight);
                //recreate();
                // it will set isDarkModeOn
                // boolean to false
                isDarkModeOn = false;
                editor.putBoolean(
                        "isDarkModeOn", false);
            } else if (!isDarkModeOn) {
                // if dark mode is off
                // it will turn it on
                AppCompatDelegate
                        .setDefaultNightMode(
                                AppCompatDelegate
                                        .MODE_NIGHT_YES);
                setTheme(R.style.TikTalkDark);
                //recreate();
                // it will set isDarkModeOn
                // boolean to true
                isDarkModeOn = true;
                editor.putBoolean(
                        "isDarkModeOn", true);

            }
            editor.commit();
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void signOut() {
        SharedPreferences prefs =
                getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);
        prefs.edit().remove(getString(R.string.keys_prefs_jwt)).apply();
        //End the app completely
        // finishAndRemoveTask();
        PushyTokenViewModel model = new ViewModelProvider(this)
                .get(PushyTokenViewModel.class);
        //when we hear back from the web service quit
        model.addResponseObserver(this, result -> finishAndRemoveTask());
        model.deleteTokenFromWebservice(
                new ViewModelProvider(this)
                        .get(UserInfoViewModel.class)
                        .getmJwt()
        );
    }

}