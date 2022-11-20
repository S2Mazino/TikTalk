package edu.uw.tcss450.team3.tiktalk.ui.auth.changepassword;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePasswordViewModel extends AndroidViewModel {

   private MutableLiveData<JSONObject> mResponse;

    public ChangePasswordViewModel(@NonNull Application application) {
        super(application);
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
    }

}