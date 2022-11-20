package edu.uw.tcss450.team3.tiktalk.ui.connection;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public class ContactListViewModel extends AndroidViewModel {
    private MutableLiveData<List<Contact>> mContactList;


    public ContactListViewModel(@NonNull Application application) {
        super(application);
        mContactList = new MutableLiveData<>();
        mContactList.setValue(new ArrayList<>());
    }
}
