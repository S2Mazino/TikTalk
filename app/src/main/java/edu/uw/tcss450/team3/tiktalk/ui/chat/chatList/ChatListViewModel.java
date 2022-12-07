package edu.uw.tcss450.team3.tiktalk.ui.chat.chatList;

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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatListViewModel extends AndroidViewModel {

    private MutableLiveData<List<ChatRoom>> mChatRoomList;
    private final MutableLiveData<JSONObject> mResponse;

    public ChatListViewModel(@NonNull Application application) {
        super(application);
        mChatRoomList = new MutableLiveData<>();
        mChatRoomList.setValue(new ArrayList<>());
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
    }

    public void addChatListObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super List<ChatRoom>> observer) {
        mChatRoomList.observe(owner, observer);
    }

    private void handleError(final VolleyError error){
        //you should add much better error handling in a production release.
        //i.e. YOUR PROJECT
        Log.e("CONNECTION ERROR", error.getLocalizedMessage());
        throw new IllegalStateException(error.getMessage());
    }

    private void handleResult(final JSONObject result) {

    }

    public void connectGet(String jwt) {
        String url =
                "https://cfb3-tcss450-labs-2021sp.herokuapp.com/phish/blog/get";
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
                headers.put("Authorization", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6ImNmYjMxQGZha2UuZW1haWwuY29tIiwibWVtYmVyaWQiOjI4OCwiaWF0IjoxNjY2NTU3ODQ2LCJleHAiOjE2NzUxOTc4NDZ9.f7IVbAzJbX72vjRUbadIksMzNm6xkPi1l_R_C4O9zb4");
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

}