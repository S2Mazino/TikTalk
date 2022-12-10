package edu.uw.tcss450.team3.tiktalk.ui.chat.chatRoom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.team3.tiktalk.R;
import edu.uw.tcss450.team3.tiktalk.ui.connection.Contact;

public class ChatUsersRecyclerViewAdapter extends RecyclerView.Adapter<ChatUsersRecyclerViewAdapter.ChatUsersViewHolder> {

    private final List<Contact> mContactList;
    private final Context mContext;
    private final AddContactsToChatViewModel mAddContactListViewModel;
    private final String mJWT;

    public ChatUsersRecyclerViewAdapter(Context context, List<Contact> contacts,String jwt) {
        this.mContext = context;
        this.mContactList = contacts;
        mAddContactListViewModel = new ViewModelProvider((ViewModelStoreOwner) mContext).get(AddContactsToChatViewModel.class);
        this.mJWT = jwt;
    }

    @NonNull
    @Override
    public ChatUsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.fragment_contact_request_card, parent, false);
        return new ChatUsersRecyclerViewAdapter.ChatUsersViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ChatUsersRecyclerViewAdapter.ChatUsersViewHolder holder, int position) {
        String fullName = mContactList.get(position).getFName() + " " + mContactList.get(position).getLName();
        holder.fullName.setText(fullName);
        holder.nickname.setText(mContactList.get(position).getNickname());
        holder.email.setText(mContactList.get(position).getEmail());

        holder.remove.setOnClickListener(button -> {
            //Log.d("JSON onclick",  "Your ID: " + mContactList.get(position).getMemberID());
            mAddContactListViewModel.removeUser(mJWT, mContactList.get(position).getMemberID());
            mContactList.remove(mContactList.get(position));
            notifyDataSetChanged();
        });

        holder.add.setOnClickListener(button -> {
            //Log.d("JSON onclick",  "Your ID: " + mContactList.get(position).getMemberID());
            mAddContactListViewModel.addUser(mJWT, mContactList.get(position).getMemberID());
            mContactList.remove(mContactList.get(position));
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return mContactList.size();
    }

    class ChatUsersViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        ImageView profileImage;
        TextView fullName;
        TextView nickname;
        TextView email;
        ImageButton remove, add;

        public ChatUsersViewHolder(@NonNull View view) {
            super(view);
            profileImage = view.findViewById(R.id.image_profile);
            fullName = view.findViewById(R.id.text_full_name);
            nickname = view.findViewById(R.id.text_nickname);
            email = view.findViewById(R.id.text_email);
            remove = view.findViewById(R.id.button_contact_remove);
            add = view.findViewById(R.id.button_contact_approve);
            mView = view.getRootView();
        }
    }
}
