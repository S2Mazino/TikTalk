package edu.uw.tcss450.team3.tiktalk.ui.connection;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.List;

import edu.uw.tcss450.team3.tiktalk.R;
import edu.uw.tcss450.team3.tiktalk.databinding.FragmentChatBinding;
import edu.uw.tcss450.team3.tiktalk.databinding.FragmentContactBinding;
import edu.uw.tcss450.team3.tiktalk.model.UserInfoViewModel;
import edu.uw.tcss450.team3.tiktalk.ui.chat.chatRoom.ChatRecyclerViewAdapter;
import edu.uw.tcss450.team3.tiktalk.ui.chat.chatRoom.ChatSendViewModel;
import edu.uw.tcss450.team3.tiktalk.ui.chat.chatRoom.ChatViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends Fragment {

    //The member ID for seeing contact used for testing
    private static final int HARD_CODED_MEMBER_ID = 73;

    private UserInfoViewModel mUserModel;
    private ContactListViewModel mContactListModel;
    private FragmentContactBinding mBinding;

    public ContactFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mUserModel = provider.get(UserInfoViewModel.class);
        mContactListModel = provider.get(ContactListViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentContactBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContactListModel.connectGet(HARD_CODED_MEMBER_ID, mUserModel.getmJwt());

        RecyclerView rv = mBinding.listRoot;

        mContactListModel.addContactListObserver(getViewLifecycleOwner(), contacts -> {
            rv.setAdapter(new ContactRecyclerViewAdapter(getActivity(), contacts));
        });
    }


}