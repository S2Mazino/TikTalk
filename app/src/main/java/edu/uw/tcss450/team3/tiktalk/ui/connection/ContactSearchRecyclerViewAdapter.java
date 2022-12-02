package edu.uw.tcss450.team3.tiktalk.ui.connection;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.team3.tiktalk.R;

public class ContactSearchRecyclerViewAdapter extends RecyclerView.Adapter<ContactSearchRecyclerViewAdapter.ContactSearchViewHolder> {

    private final List<Contact> mContactSearchList;
    private final Context mContext;
    private ContactSearchListViewModel mContactSearchListViewModel;
    private final String mJWT;

    public ContactSearchRecyclerViewAdapter(Context context, List<Contact> contacts, String jwt) {
        this.mContext = context;
        this.mContactSearchList = contacts;
        mContactSearchListViewModel = new ViewModelProvider((ViewModelStoreOwner) mContext).get(ContactSearchListViewModel.class);
        this.mJWT = jwt;
    }


    @NonNull
    @Override
    public ContactSearchRecyclerViewAdapter.ContactSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.fragment_contact_card, parent, false);

        return new ContactSearchRecyclerViewAdapter.ContactSearchViewHolder(view);
//        return new ContactRecyclerViewAdapter.ContactViewHolder(LayoutInflater
//                .from(parent.getContext())
//                .inflate(R.layout.fragment_contact, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactSearchRecyclerViewAdapter.ContactSearchViewHolder holder, int position) {
        String fullName = mContactSearchList.get(position).getFName() + " " + mContactSearchList.get(position).getLName();
        holder.fullName.setText(fullName);
        holder.nickname.setText(mContactSearchList.get(position).getNickname());
        holder.email.setText(mContactSearchList.get(position).getEmail());

        holder.remove.setOnClickListener(button -> {
            //Log.d("JSON onclick",  "Your ID: " + mContactList.get(position).getMemberID());
            mContactSearchListViewModel.removeSearch(mJWT, mContactSearchList.get(position).getMemberID());
            mContactSearchList.remove(mContactSearchList.get(position));
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return mContactSearchList.size();
    }

    class ContactSearchViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        ImageView profileImage;
        TextView fullName;
        TextView nickname;
        TextView email;
        ImageButton remove;

        public ContactSearchViewHolder(@NonNull View view) {
            super(view);
            profileImage = view.findViewById(R.id.image_profile);
            fullName = view.findViewById(R.id.text_full_name);
            nickname = view.findViewById(R.id.text_nickname);
            email = view.findViewById(R.id.text_email);
            remove = view.findViewById(R.id.button_contact_remove);
            mView = view.getRootView();
        }
    }
}
