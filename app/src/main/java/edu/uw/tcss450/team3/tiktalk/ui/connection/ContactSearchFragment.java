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

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.Objects;

import edu.uw.tcss450.team3.tiktalk.R;
import edu.uw.tcss450.team3.tiktalk.databinding.FragmentContactSearchBinding;
import edu.uw.tcss450.team3.tiktalk.model.UserInfoViewModel;

/**
 * A simple {@link Fragment} subclass..
 */
public class ContactSearchFragment extends Fragment {

    private UserInfoViewModel mUserModel;
    private ContactSearchListViewModel mContactSearchListModel;
    private FragmentContactSearchBinding mBinding;

    public ContactSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mUserModel = provider.get(UserInfoViewModel.class);
        mContactSearchListModel = provider.get(ContactSearchListViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentContactSearchBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContactSearchListModel.connectGet(mUserModel.getmJwt());

        RecyclerView rv = mBinding.listSentRequest;


        mContactSearchListModel.addContactSearchListObserver(getViewLifecycleOwner(), contacts -> {
            if(!contacts.isEmpty()) {
                rv.setAdapter(new ContactSearchRecyclerViewAdapter(getActivity(), contacts, mUserModel.getmJwt()));
            }
        });

        mContactSearchListModel.addResponseObserver(getViewLifecycleOwner(), this::observeResponse);

        mBinding.buttonContactSend.setOnClickListener(button -> {
            mContactSearchListModel.addSearch(mUserModel.getmJwt(), mBinding.editSearch.getText().toString());
        });

    }

    private void observeResponse(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    if (response.getJSONObject("data").getString("message").equals("Missing required information")) {
                        mBinding.editSearch.setError("Must enter email");
                    } else if (response.getJSONObject("data").getString("message").equals("Missing email requirement")) {
                        mBinding.editSearch.setError("Must enter valid email");
                    } else if (response.getJSONObject("data").getString("message").equals("friend email not found")) {
                        mBinding.editSearch.setError("Email is not a user");
                    } else {
                        mBinding.editSearch.setError(
                                "Error Authenticating: " +
                                        response.getJSONObject("data").getString("message"));

                    }
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                //navigateToLogin();
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }

}