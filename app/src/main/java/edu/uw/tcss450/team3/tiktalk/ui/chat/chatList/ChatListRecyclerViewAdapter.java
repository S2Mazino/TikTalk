package edu.uw.tcss450.team3.tiktalk.ui.chat.chatList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.team3.tiktalk.R;
import edu.uw.tcss450.team3.tiktalk.ui.chat.ChatListListener;
import edu.uw.tcss450.team3.tiktalk.ui.chat.chatRoom.ChatRoom;

public class ChatListRecyclerViewAdapter extends RecyclerView.Adapter<ChatListRecyclerViewAdapter.ChatListViewHolder> {

    private final List<ChatRoom> mChatRoomsList;
    private final Context mContext;
    private ChatListViewModel mChatListViewModel;
    private final String mJWT;
    private ChatListListener chatListListener;


    public ChatListRecyclerViewAdapter(Context context, List<ChatRoom> chatrooms, String jwt, ChatListListener chatListListener) {
        this.mContext = context;
        this.mChatRoomsList = chatrooms;
        mChatListViewModel = new ViewModelProvider((ViewModelStoreOwner) mContext).get(ChatListViewModel.class);
        this.mJWT = jwt;
        this.chatListListener = chatListListener;
    }

    @NonNull
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_chat_list_card, parent, false);
        return new ChatListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListViewHolder holder, int position) {
        holder.chatTitle.setText(mChatRoomsList.get(position).getTitle());

        holder.remove.setOnClickListener(button -> {
            mChatListViewModel.removeChatroom(mJWT, mChatRoomsList.get(position).getChatID());
            mChatRoomsList.remove(mChatRoomsList.get(position));
            notifyDataSetChanged();
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chatListListener.onItemClick(mChatRoomsList.get(position));
            }
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
    public class ChatListViewHolder extends RecyclerView.ViewHolder{
        public final View mView;
        TextView chatTitle;
        ImageButton remove;
        CardView cardView;


        public ChatListViewHolder(View view) {
            super(view);
            chatTitle = view.findViewById(R.id.text_chatroom_name);
            remove = view.findViewById(R.id.button_chat_remove);
            mView = view.getRootView();
            cardView = view.findViewById(R.id.card_root);
        }
    }



}