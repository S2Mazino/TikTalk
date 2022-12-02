package edu.uw.tcss450.team3.tiktalk.ui.connection;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.team3.tiktalk.R;
import edu.uw.tcss450.team3.tiktalk.databinding.FragmentContactBinding;
import edu.uw.tcss450.team3.tiktalk.databinding.FragmentContactRequestBinding;
import edu.uw.tcss450.team3.tiktalk.model.UserInfoViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactRequestFragment extends Fragment {

    private UserInfoViewModel mUserModel;
    private ContactRequestListViewModel mContactRequestListModel;
    private FragmentContactRequestBinding mBinding;

    public ContactRequestFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mUserModel = provider.get(UserInfoViewModel.class);
        mContactRequestListModel = provider.get(ContactRequestListViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentContactRequestBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContactRequestListModel.connectGet(mUserModel.getmJwt());

        RecyclerView rv = mBinding.listReceivedRequest;

//        mBinding.contactAdd.setOnClickListener(button ->
//                Navigation.findNavController(getView()).navigate(ContactFragmentDirections.actionContactFragmentToContactSearchFragment()));

        mContactRequestListModel.addContactListObserver(getViewLifecycleOwner(), contacts -> {
            if (!contacts.isEmpty()) {
                rv.setAdapter(new ContactRequestRecyclerViewAdapter(getActivity(), contacts, mUserModel.getmJwt()));
            }
        });
    }
}