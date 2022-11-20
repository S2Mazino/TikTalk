package edu.uw.tcss450.team3.tiktalk.ui.chat.chatList;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.team3.tiktalk.R;
import edu.uw.tcss450.team3.tiktalk.databinding.FragmentChatListBinding;
import edu.uw.tcss450.team3.tiktalk.model.UserInfoViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatListFragment extends Fragment {

    private ChatListViewModel mChatListViewModel;
    private UserInfoViewModel mUserInfoViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mChatListViewModel = new ViewModelProvider(getActivity()).get(ChatListViewModel.class);
        mUserInfoViewModel = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
        //mChatListViewModel.connectGet(mUserInfoViewModel.getmJwt());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      return inflater.inflate(R.layout.fragment_chat_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentChatListBinding binding = FragmentChatListBinding.bind(getView());

        mChatListViewModel.addChatListObserver(getViewLifecycleOwner(), chatList -> {
            binding.listRoot.setAdapter(new ChatListRecyclerViewAdapter(ChatRoomGenerator.getChatList()));
        });
    }
}