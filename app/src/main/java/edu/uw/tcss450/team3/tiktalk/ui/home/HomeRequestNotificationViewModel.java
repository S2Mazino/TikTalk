package edu.uw.tcss450.team3.tiktalk.ui.home;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import edu.uw.tcss450.team3.tiktalk.R;
import edu.uw.tcss450.team3.tiktalk.ui.connection.Contact;

public class HomeRequestNotificationViewModel extends AndroidViewModel {

    private MutableLiveData<List<Contact>> mContactRequestList;

    public String requestNumber;

    public HomeRequestNotificationViewModel(@NonNull Application application) {
        super(application);
        mContactRequestList = new MutableLiveData<>();
        mContactRequestList.setValue(new ArrayList<>());
    }



    public void addContactRequestListObserver(@NonNull LifecycleOwner owner,
                                              @NonNull Observer<? super List<Contact>> observer) {
        mContactRequestList.observe(owner, observer);
    }

    public void connectGet(final String jwt) {
        String url = getApplication().getResources().getString(R.string.base_url) + "contacts/" + "request";
        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, //no body for this get request
                this::handleResult,
                this::handleError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", jwt);
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);
    }

    private void handleResult(final JSONObject response) {
        List<Contact> list;
        try {
            if (!response.has("rowCount")) {
                requestNumber = "0";
            } else {
                requestNumber = String.valueOf(response.getInt("rowCount"));
            }
        }catch (JSONException e) {
            Log.e("JSON PARSE ERROR", "Found in handle Success ChatViewModel");
            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
        }

    }

    public String getRequestNumber() {
        return requestNumber;
    }

    private void handleError(final VolleyError error) {
        //you should add much better error handling in a production release.
        //i.e. YOUR PROJECT
        if (Objects.isNull(error.networkResponse)) {
            Log.e("NETWORK ERROR", error.getMessage());
        }else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset())
                    .replace('\"', '\'');
            Log.d("CONNECTION", data);
            //throw new IllegalStateException(error.getMessage());
        }
    }
}
