package edu.uw.tcss450.team3.tiktalk.ui.chat.chatRoom;

import java.io.Serializable;

public class ChatRoom implements Serializable {

    private final String mTitle;
    private  int mChatID;
    private String mMessage;

    /**
     * Helper class for building Credentials.
     *
     * @author Charles Bryan
     */
    public static class Builder {
        private final String mTitle;
        private  int mChatID;
        private String mMessage = "";

        /**
         * Constructs a new Builder.
         */
        public Builder(int chatID, String title) {
            this.mChatID = chatID;
            this.mTitle = title;
        }


        /**
         * Add an optional teaser for the full blog post.
         * @param val an optional url teaser for the full blog post.
         * @return the Builder of this BlogPost
         */
        public Builder addMessage(final String val) {
            mMessage = val;
            return this;
        }

        public ChatRoom build() {
            return new ChatRoom(this);
        }

    }

    private ChatRoom(final Builder builder) {
        this.mChatID = builder.mChatID;
        this.mTitle = builder.mTitle;
        this.mMessage = builder.mMessage;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getMessage() {
        return mMessage;
    }

    public int getChatID() {
        return mChatID;
    }
}

