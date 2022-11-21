package edu.uw.tcss450.team3.tiktalk.ui.connection;

import android.content.Context;
import android.content.res.Resources;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.shape.CornerFamily;


import java.util.List;

import edu.uw.tcss450.team3.tiktalk.R;
import edu.uw.tcss450.team3.tiktalk.databinding.FragmentContactBinding;


public class ContactRecyclerViewAdapter extends RecyclerView.Adapter<ContactRecyclerViewAdapter.ContactViewHolder> {

    private final List<Contact> mContactList;
    private final Context mContext;
    public ContactRecyclerViewAdapter(Context context, List<Contact> contacts) {
        this.mContext = context;
        this.mContactList = contacts;
    }


    @NonNull
    @Override
    public ContactRecyclerViewAdapter.ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.fragment_contact_card, parent, false);
        return new ContactRecyclerViewAdapter.ContactViewHolder(view);
//        return new ContactRecyclerViewAdapter.ContactViewHolder(LayoutInflater
//                .from(parent.getContext())
//                .inflate(R.layout.fragment_contact, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactRecyclerViewAdapter.ContactViewHolder holder, int position) {
        String fullName = mContactList.get(position).getFName() + " " + mContactList.get(position).getLName();
        holder.fullName.setText(fullName);
//        holder.nickname.setText(mContactList.get(position).getNickname());
//        holder.email.setText(mContactList.get(position).getEmail());


    }

    @Override
    public int getItemCount() {
        return mContactList.size();
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        ImageView profileImage;
        TextView fullName;
        TextView nickname;
        TextView email;
        ImageButton message, remove;

        public ContactViewHolder(@NonNull View view) {
            super(view);
            profileImage = view.findViewById(R.id.image_profile);
            fullName = view.findViewById(R.id.text_full_name);
//            nickname = view.findViewById(R.id.text_nickname);
//            email = view.findViewById(R.id.text_email);
            message = view.findViewById(R.id.button_contact_message);
            remove = view.findViewById(R.id.button_contact_remove);
            mView = view.getRootView();
        }
    }
}
