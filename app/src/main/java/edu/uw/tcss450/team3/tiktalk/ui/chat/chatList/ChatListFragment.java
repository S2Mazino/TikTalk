package edu.uw.tcss450.team3.tiktalk.ui.chat.chatList;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.team3.tiktalk.R;
import edu.uw.tcss450.team3.tiktalk.databinding.FragmentChatBinding;
import edu.uw.tcss450.team3.tiktalk.databinding.FragmentChatListBinding;
import edu.uw.tcss450.team3.tiktalk.databinding.FragmentContactBinding;
import edu.uw.tcss450.team3.tiktalk.model.UserInfoViewModel;
import edu.uw.tcss450.team3.tiktalk.ui.connection.ContactListViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatListFragment extends Fragment {

    private UserInfoViewModel mUserModel;
    private ChatListViewModel mChatListViewModel;
    private FragmentChatListBinding mBinding;

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

        RecyclerView rv = mBinding.listRoot;

        mChatListViewModel.addChatListObserver(getViewLifecycleOwner(), chatList -> {
            rv.setAdapter(new ChatListRecyclerViewAdapter(getActivity(), chatList, mUserModel.getmJwt()));
        });
    }
}