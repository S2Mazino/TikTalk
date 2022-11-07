package edu.uw.tcss450.team3.tiktalk.ui.connection;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

import edu.uw.tcss450.team3.tiktalk.R;
import edu.uw.tcss450.team3.tiktalk.adapter.ViewPagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConnectionFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;

    public ConnectionFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_connection, container, false);
        addFragment(view);
        return view;
    }

    private void addFragment(View view) {
        tabLayout = view.findViewById(R.id.connectionTabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new ContactFragment(), "Contact");
        adapter.addFragment(new AddFriendFragment(), "Add");
        adapter.addFragment(new RequestInFragment(), "Request");
        adapter.addFragment(new RequestOutFragment(), "Sent");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}