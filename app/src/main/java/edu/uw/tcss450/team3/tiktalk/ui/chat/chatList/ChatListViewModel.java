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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import edu.uw.tcss450.team3.tiktalk.R;
import edu.uw.tcss450.team3.tiktalk.ui.chat.chatRoom.ChatRoom;
import edu.uw.tcss450.team3.tiktalk.ui.connection.Contact;

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

    public void connectGet(String jwt) {
        String url = getApplication().getResources().getString(R.string.base_url) + "chats";
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

    private void handleError(final VolleyError error){
        //you should add much better error handling in a production release.
        //i.e. YOUR PROJECT
        if(!Objects.isNull(error.networkResponse)){
            Log.e("CONNECTION ERROR", error.getLocalizedMessage());
        }

        //throw new IllegalStateException(error.getMessage());
    }

    private void handleResult(final JSONObject response) {
        List<ChatRoom> list;
        if (!response.has("rowCount")) {
            throw new IllegalStateException("Unexpected response in ChatroomListViewModel: " + response);
        }
        try {
            list = new ArrayList<>();
            JSONArray chatrooms = response.getJSONArray("rows");
            for(int i = 0; i < chatrooms.length(); i++) {
                JSONObject chatroom = chatrooms.getJSONObject(i);
                ChatRoom cChatroom = new ChatRoom(
                        chatroom.getString("name"),
                        chatroom.getInt("chatid")
                );
                if (!list.contains(cChatroom)) {
                    // don't add a duplicate
                    list.add(0, cChatroom);
                } else {
                    // this shouldn't happen but could with the asynchronous
                    // nature of the application
                    Log.wtf("Chatroom already received",
                            "Or duplicate id:" + cChatroom.getChatID());
                }

            }
            //inform observers of the change (setValue)
            //getOrCreateMapEntry(response.getInt("chatId")).setValue(list);
            mChatRoomList.setValue(list);
        }catch (JSONException e) {
            Log.e("JSON PARSE ERROR", "Found in handle Success ChatListViewModel");
            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
        }

    }



}