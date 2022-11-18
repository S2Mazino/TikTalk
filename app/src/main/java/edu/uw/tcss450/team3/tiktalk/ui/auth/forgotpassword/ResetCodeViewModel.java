package edu.uw.tcss450.team3.tiktalk.ui.auth.forgotpassword;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONObject;

import edu.uw.tcss450.team3.tiktalk.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResetCodeViewModel extends AndroidViewModel {

   private MutableLiveData<JSONObject> mResponse;

    public ResetCodeViewModel(@NonNull Application application) {
        super(application);
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
    }

}