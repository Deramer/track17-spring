package track.msgtest.messenger.teacher.client.inputhandlers;

import track.msgtest.messenger.messages.ChatListMessage;
import track.msgtest.messenger.messages.Type;
import track.msgtest.messenger.net.ProtocolException;
import track.msgtest.messenger.net.UserErrorException;
import track.msgtest.messenger.teacher.client.MessengerClient;

import java.io.IOException;

/**
 * Created by arseniy on 16.05.17.
 */
public class ChatListInputHandler implements InputHandler {
    public void handle(MessengerClient client, String input) throws IOException, ProtocolException, UserErrorException {
        ChatListMessage chatListMessage = new ChatListMessage();
        chatListMessage.setType(Type.MSG_CHAT_LIST);
        client.send(chatListMessage);
    }
}
