package edu.uw.tcss450.team3.tiktalk.ui.connection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.team3.tiktalk.databinding.FragmentContactBinding;
import edu.uw.tcss450.team3.tiktalk.model.UserInfoViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends Fragment {

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContactListModel.connectGet(mUserModel.getmJwt());

        RecyclerView rv = mBinding.listRoot;

//        mBinding.contactAdd.setOnClickListener(button ->
//                Navigation.findNavController(getView()).navigate(ContactFragmentDirections.actionContactFragmentToContactSearchFragment()));

        mContactListModel.addContactListObserver(getViewLifecycleOwner(), contacts -> {
            if(!contacts.isEmpty()) {
                rv.setAdapter(new ContactRecyclerViewAdapter(getActivity(), contacts, mUserModel.getmJwt()));
            }
        });
    }


}