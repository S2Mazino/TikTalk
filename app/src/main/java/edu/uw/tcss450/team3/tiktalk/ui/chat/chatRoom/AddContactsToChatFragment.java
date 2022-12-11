package edu.uw.tcss450.team3.tiktalk.ui.chat.chatRoom;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import edu.uw.tcss450.team3.tiktalk.R;
import edu.uw.tcss450.team3.tiktalk.databinding.FragmentAddContactsToChatBinding;
import edu.uw.tcss450.team3.tiktalk.model.UserInfoViewModel;
import edu.uw.tcss450.team3.tiktalk.ui.chat.chatList.ChatListFragment;

public class AddContactsToChatFragment extends Fragment {

    private UserInfoViewModel mUserModel;
    private AddContactsToChatViewModel mAddContactModel;
    private FragmentAddContactsToChatBinding mBinding;
    private int mChatID;
    private ImageView backIV;

    public AddContactsToChatFragment(int chatID) {
        this.mChatID = chatID;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mUserModel = provider.get(UserInfoViewModel.class);
        mAddContactModel = provider.get(AddContactsToChatViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentAddContactsToChatBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAddContactModel.connectGet(mUserModel.getmJwt());
        backIV = view.findViewById(R.id.idIVBackToChatroom);

        RecyclerView rv = mBinding.listContacts;
        mAddContactModel.addUserContactListObserver(getViewLifecycleOwner(), contacts -> {
            if(!contacts.isEmpty()) {
                rv.setAdapter(new ChatUsersRecyclerViewAdapter(getActivity(), contacts, mUserModel.getmJwt(), mChatID));
            }
        });

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatFragment chatFragment = new ChatFragment(mChatID);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.chat_list_layout_root, chatFragment).commit();
            }
        });
    }

}