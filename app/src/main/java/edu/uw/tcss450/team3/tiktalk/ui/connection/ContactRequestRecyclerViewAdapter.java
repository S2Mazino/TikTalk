package edu.uw.tcss450.team3.tiktalk.ui.connection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.team3.tiktalk.R;

public class ContactRequestRecyclerViewAdapter extends RecyclerView.Adapter<ContactRequestRecyclerViewAdapter.ContactRequestViewHolder> {

    private final List<Contact> mContactRequestList;
    private final Context mContext;
    private ContactRequestListViewModel mContactRequestListViewModel;
    private final String mJWT;

    public ContactRequestRecyclerViewAdapter(Context context, List<Contact> contacts, String jwt) {
        this.mContext = context;
        this.mContactRequestList = contacts;
        mContactRequestListViewModel = new ViewModelProvider((ViewModelStoreOwner) mContext).get(ContactRequestListViewModel.class);
        this.mJWT = jwt;
    }


    @NonNull
    @Override
    public ContactRequestRecyclerViewAdapter.ContactRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.fragment_contact_request_card, parent, false);

        return new ContactRequestRecyclerViewAdapter.ContactRequestViewHolder(view);
//        return new ContactRecyclerViewAdapter.ContactViewHolder(LayoutInflater
//                .from(parent.getContext())
//                .inflate(R.layout.fragment_contact, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactRequestRecyclerViewAdapter.ContactRequestViewHolder holder, int position) {
        String fullName = mContactRequestList.get(position).getFName() + " " + mContactRequestList.get(position).getLName();
        holder.fullName.setText(fullName);
        holder.nickname.setText(mContactRequestList.get(position).getNickname());
        holder.email.setText(mContactRequestList.get(position).getEmail());

        holder.remove.setOnClickListener(button -> {
            //Log.d("JSON onclick",  "Your ID: " + mContactList.get(position).getMemberID());
            mContactRequestListViewModel.removeRequest(mJWT, mContactRequestList.get(position).getMemberID());
            mContactRequestList.remove(mContactRequestList.get(position));
            notifyDataSetChanged();
        });

        holder.add.setOnClickListener(button -> {
            //Log.d("JSON onclick",  "Your ID: " + mContactList.get(position).getMemberID());
            mContactRequestListViewModel.addRequest(mJWT, mContactRequestList.get(position).getMemberID());
            mContactRequestList.remove(mContactRequestList.get(position));
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return mContactRequestList.size();
    }

    class ContactRequestViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        ImageView profileImage;
        TextView fullName;
        TextView nickname;
        TextView email;
        ImageButton remove, add;

        public ContactRequestViewHolder(@NonNull View view) {
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
