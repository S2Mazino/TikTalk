package edu.uw.tcss450.team3.tiktalk.ui.chat.chatList;

import android.content.Context;
import android.graphics.drawable.Icon;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import edu.uw.tcss450.team3.tiktalk.R;
import edu.uw.tcss450.team3.tiktalk.databinding.FragmentChatListCardBinding;
import edu.uw.tcss450.team3.tiktalk.ui.connection.Contact;
import edu.uw.tcss450.team3.tiktalk.ui.connection.ContactListViewModel;
import edu.uw.tcss450.team3.tiktalk.ui.connection.ContactSearchListViewModel;

public class ChatListRecyclerViewAdapter extends RecyclerView.Adapter<ChatListRecyclerViewAdapter.ChatListViewHolder> {

    private final List<ChatRoom> mChatRoomsList;
    private final Context mContext;
    private ChatListViewModel mChatListViewModel;
    private final String mJWT;

    public ChatListRecyclerViewAdapter(Context context, List<ChatRoom> chatrooms, String jwt) {
        this.mContext = context;
        this.mChatRoomsList = chatrooms;
        mChatListViewModel = new ViewModelProvider((ViewModelStoreOwner) mContext).get(ChatListViewModel.class);
        this.mJWT = jwt;
    }

    @NonNull
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new ChatListViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_chat_list_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListViewHolder holder, int position) {
        holder.chatTitle.setText(mChatRoomsList.get(position).getTitle());

        holder.remove.setOnClickListener(button -> {
            //mChatListViewModel.removeChatRoom(mJWT, mChatRoomsList.get(position).getChatID());
            mChatRoomsList.remove(mChatRoomsList.get(position));
            notifyDataSetChanged();
        });
    }


    @Override
    public int getItemCount() {
        return mChatRoomsList.size();
    }

    /**
     * Objects from this class represent an Individual row View from the List
     * of rows in the Blog Recycler View.
     */
    public class ChatListViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        TextView chatTitle;
        ImageButton remove;

        public ChatListViewHolder(View view) {
            super(view);
            chatTitle = view.findViewById(R.id.text_chatroom_name);
            remove = view.findViewById(R.id.button_chat_remove);
            mView = view.getRootView();
        }
    }

}