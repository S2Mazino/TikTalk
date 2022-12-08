package edu.uw.tcss450.team3.tiktalk.ui.chat.chatRoom;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class ChatRoom implements Serializable {

    private final String mTitle;
    private  int mChatID;

    public ChatRoom(String title, int chatID) {
        this.mChatID = chatID;
        this.mTitle = title;
    }

    public String getTitle() {
        return mTitle;
    }

    public int getChatID() {
        return mChatID;
    }
}

