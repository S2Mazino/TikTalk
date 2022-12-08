package edu.uw.tcss450.team3.tiktalk.ui.chat;

import edu.uw.tcss450.team3.tiktalk.ui.chat.chatRoom.ChatFragment;
import edu.uw.tcss450.team3.tiktalk.ui.chat.chatRoom.ChatRoom;

public interface ChatListListener {
    void onItemClick(ChatRoom chatRoom);
}
