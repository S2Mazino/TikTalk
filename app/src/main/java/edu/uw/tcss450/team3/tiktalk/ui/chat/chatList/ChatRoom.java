package edu.uw.tcss450.team3.tiktalk.ui.chat.chatList;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class ChatRoom implements Serializable {

    private final String mTitle;
    private  int mChatID;

    private ChatRoom(String title, int chatID) {
        this.mChatID = chatID;
        this.mTitle = title;
    }

    /**
     * Static factory method to turn a properly formatted JSON String into a
     * ChatMessage object.
     * @param cmAsJson the String to be parsed into a ChatMessage Object.
     * @return a ChatMessage Object with the details contained in the JSON String.
     * @throws JSONException when cmAsString cannot be parsed into a ChatMessage.
     */
    public static ChatRoom createFromJsonString(final String cmAsJson) throws JSONException {
        final JSONObject msg = new JSONObject(cmAsJson);
        return new ChatRoom(msg.getString("email"),
                            msg.getInt("messageid"));
    }

    public String getTitle() {
        return mTitle;
    }

    public int getChatID() {
        return mChatID;
    }
}

