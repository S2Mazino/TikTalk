package edu.uw.tcss450.team3.tiktalk.ui.chat.chatRoom;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import edu.uw.tcss450.team3.tiktalk.R;
import edu.uw.tcss450.team3.tiktalk.databinding.FragmentChatBinding;
import edu.uw.tcss450.team3.tiktalk.model.UserInfoViewModel;
import edu.uw.tcss450.team3.tiktalk.ui.auth.signin.SignInFragmentArgs;
import edu.uw.tcss450.team3.tiktalk.ui.chat.chatList.ChatListFragment;
import edu.uw.tcss450.team3.tiktalk.ui.weather.WeatherSecondFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    //The chat ID for "global" chat
    //private static final int HARD_CODED_CHAT_ID = 1;

    private ChatViewModel mChatModel;
    private UserInfoViewModel mUserModel;
    private ChatSendViewModel mSendModel;
    private ImageView backIV;
    EditText editText;
    Button button;
    private int mChatID;

    public ChatFragment(int chatID) {
        // Required empty public constructor
        this.mChatID = chatID;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mUserModel = provider.get(UserInfoViewModel.class);
        mChatModel = provider.get(ChatViewModel.class);
        mChatModel.getFirstMessages(mChatID, mUserModel.getmJwt());
        mSendModel = provider.get(ChatSendViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentChatBinding binding = FragmentChatBinding.bind(getView());
        backIV = view.findViewById(R.id.idIVBackToMap);
        editText = view.findViewById(R.id.text_search_chatroom);
        button = view.findViewById(R.id.button_add_chatroom);

        //SetRefreshing shows the internal Swiper view progress bar. Show this until messages load
        binding.swipeContainer.setRefreshing(true);

        final RecyclerView rv = binding.recyclerMessages;
        //Set the Adapter to hold a reference to the list FOR THIS chat ID that the ViewModel
        //holds.
        rv.setAdapter(new ChatRecyclerViewAdapter(
                mChatModel.getMessageListByChatId(mChatID),
                mUserModel.getEmail()));


        //When the user scrolls to the top of the RV, the swiper list will "refresh"
        //The user is out of messages, go out to the service and get more
        binding.swipeContainer.setOnRefreshListener(() -> {
            mChatModel.getNextMessages(mChatID, mUserModel.getmJwt());
        });

        mChatModel.addMessageObserver(mChatID, getViewLifecycleOwner(),
                list -> {
                    /*
                     * This solution needs work on the scroll position. As a group,
                     * you will need to come up with some solution to manage the
                     * recyclerview scroll position. You also should consider a
                     * solution for when the keyboard is on the screen.
                     */
                    //inform the RV that the underlying list has (possibly) changed
                    rv.getAdapter().notifyDataSetChanged();
                    rv.scrollToPosition(rv.getAdapter().getItemCount() - 1);
                    binding.swipeContainer.setRefreshing(false);
                });

        //Send button was clicked. Send the message via the SendViewModel
        binding.buttonSend.setOnClickListener(button -> {
            mSendModel.sendMessage(mChatID,
                    mUserModel.getmJwt(),
                    binding.editMessage.getText().toString());
        });
//when we get the response back from the server, clear the edittext
        mSendModel.addResponseObserver(getViewLifecycleOwner(), response -> {
                    binding.editMessage.setText("");
                mChatModel.getFirstMessages(mChatID, mUserModel.getmJwt());
                rv.getAdapter().notifyDataSetChanged();
        });


        backIV.setOnClickListener(new View.OnClickListener() {

            ChatListFragment chatListFragment = new ChatListFragment();
            @Override
            public void onClick(View view) {
                //getActivity().getSupportFragmentManager().popBackStackImmediate();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.chat_layout_root, chatListFragment).commit();
            }
        });
    }
}