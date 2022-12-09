package edu.uw.tcss450.team3.tiktalk.ui.chat.chatList;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.ArrayList;
import java.util.List;

import edu.uw.tcss450.team3.tiktalk.R;
import edu.uw.tcss450.team3.tiktalk.databinding.FragmentChatBinding;

import edu.uw.tcss450.team3.tiktalk.databinding.FragmentChatListBinding;
import edu.uw.tcss450.team3.tiktalk.databinding.FragmentContactBinding;
import edu.uw.tcss450.team3.tiktalk.model.UserInfoViewModel;
import edu.uw.tcss450.team3.tiktalk.ui.chat.ChatListListener;
import edu.uw.tcss450.team3.tiktalk.ui.chat.chatRoom.ChatFragment;
import edu.uw.tcss450.team3.tiktalk.ui.chat.chatRoom.ChatRoom;
import edu.uw.tcss450.team3.tiktalk.ui.connection.ContactListViewModel;
import edu.uw.tcss450.team3.tiktalk.ui.weather.WeatherFromMapFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatListFragment extends Fragment implements ChatListListener {

    private UserInfoViewModel mUserModel;
    private ChatListViewModel mChatListViewModel;
    private FragmentChatListBinding mBinding;
    private List<ChatRoom> mChatRoomsList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mUserModel = provider.get(UserInfoViewModel.class);
        mChatListViewModel = provider.get(ChatListViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentChatListBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mChatListViewModel.connectGet(mUserModel.getmJwt());

        RecyclerView rv = mBinding.chatroomListRoot;

        mChatListViewModel.addChatListObserver(getViewLifecycleOwner(), chatList -> {
            rv.setAdapter(new ChatListRecyclerViewAdapter(getActivity(), chatList, mUserModel.getmJwt(), this));
        });

        mBinding.buttonAddChatroom.setOnClickListener(button -> {
            mChatListViewModel.addChatroom(mUserModel.getmJwt(), mBinding.textSearchChatroom.getText().toString());
            mBinding.textSearchChatroom.setText("");
        });
    }

    @Override
    public void onItemClick(ChatRoom chatRoom) {
        ChatFragment chatFragment = new ChatFragment();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.chat_list_layout_root, chatFragment).commit();
    }
}