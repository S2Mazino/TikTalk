package edu.uw.tcss450.team3.tiktalk.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import edu.uw.tcss450.team3.tiktalk.ui.connection.AddFriendFragment;
import edu.uw.tcss450.team3.tiktalk.ui.connection.ConnectionFragment;
import edu.uw.tcss450.team3.tiktalk.ui.connection.ContactFragment;
import edu.uw.tcss450.team3.tiktalk.ui.connection.RequestInFragment;
import edu.uw.tcss450.team3.tiktalk.ui.connection.RequestOutFragment;
import edu.uw.tcss450.team3.tiktalk.ui.home.HomeFragment;

public class Adapter extends FragmentStateAdapter {


    public Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ContactFragment();
            case 1:
                return new AddFriendFragment();
            case 2:
                return new RequestInFragment();
            case 3:
                return new RequestOutFragment();
            default:
                return new ContactFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
