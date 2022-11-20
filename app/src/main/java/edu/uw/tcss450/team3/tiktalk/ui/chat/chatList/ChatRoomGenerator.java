package edu.uw.tcss450.team3.tiktalk.ui.chat.chatList;

import java.util.Arrays;
import java.util.List;

import edu.uw.tcss450.team3.tiktalk.ui.chat.chatRoom.ChatRoom;

public class ChatRoomGenerator {

    private static final ChatRoom[] CHATROOMS;
    public static final int COUNT = 3;

    static {
        CHATROOMS = new ChatRoom[COUNT];
        for(int i = 0; i < CHATROOMS.length; i++){
            CHATROOMS[i] = new ChatRoom
                    .Builder(1, "ChatRoom #" + i)
                    .addMessage("Dummy chatroom" + i)
                    .build();
        }
    }

    public static List<ChatRoom> getChatList() {
        return Arrays.asList(CHATROOMS);
    }

    public static ChatRoom[] getChatRooms() {
        return Arrays.copyOf(CHATROOMS, CHATROOMS.length);
    }

    private ChatRoomGenerator() { }


}
