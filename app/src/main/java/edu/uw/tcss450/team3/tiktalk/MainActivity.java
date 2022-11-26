package edu.uw.tcss450.team3.tiktalk;
//comment
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import edu.uw.tcss450.team3.tiktalk.model.UserInfoViewModel;
import edu.uw.tcss450.team3.tiktalk.ui.chat.chatRoom.ChatFragment;
import edu.uw.tcss450.team3.tiktalk.ui.chat.chatList.ChatListFragment;
import edu.uw.tcss450.team3.tiktalk.ui.connection.ConnectionFragment;
import edu.uw.tcss450.team3.tiktalk.ui.connection.ContactFragment;
import edu.uw.tcss450.team3.tiktalk.ui.home.HomeFragment;
import edu.uw.tcss450.team3.tiktalk.ui.weather.WeatherFirstFragment;
import edu.uw.tcss450.team3.tiktalk.ui.weather.WeatherSecondFragment;

public class MainActivity extends AppCompatActivity {

    //all varibles

    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment = new HomeFragment();
    ConnectionFragment connectionFragment = new ConnectionFragment();
    ContactFragment contactFragment = new ContactFragment();
    ChatListFragment chatListFragment = new ChatListFragment();
    WeatherFirstFragment weatherFirstFragment = new WeatherFirstFragment();
    ChatFragment chatFragment = new ChatFragment();
    WeatherSecondFragment weatherSecondFragment = new WeatherSecondFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivityArgs args = MainActivityArgs.fromBundle(getIntent().getExtras());
        new ViewModelProvider(this,
                new UserInfoViewModel.UserInfoViewModelFactory(args.getEmail(), args.getJwt(), args.getNickname(), args.getFirstname(), args.getLastname())
        ).get(UserInfoViewModel.class);


        setContentView(R.layout.activity_main);

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
                        // getSupportFragmentManager().beginTransaction().replace(R.id.layout_container, chatFragment).commit();
                        getSupportFragmentManager().beginTransaction().replace(R.id.layout_container, chatListFragment).commit();
                        return true;
                    case R.id.weatherIcon:
                        // uncomment when working on the first weather fragement
                        // getSupportFragmentManager().beginTransaction().replace(R.id.layout_container, weatherFirstFragment).commit();
                        getSupportFragmentManager().beginTransaction().replace(R.id.layout_container, weatherSecondFragment).commit();
                        return true;
                    default:
                        getSupportFragmentManager().beginTransaction().replace(R.id.layout_container, homeFragment).commit();
                        return true;
                }
            }
        });
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
        return super.onOptionsItemSelected(item);
    }

    private void signOut() {
        SharedPreferences prefs =
                getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);
        prefs.edit().remove(getString(R.string.keys_prefs_jwt)).apply();
        //End the app completely
        finishAndRemoveTask();
    }

}