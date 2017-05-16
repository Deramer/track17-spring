package track.msgtest.messenger.teacher.client.inputhandlers;

import track.msgtest.messenger.messages.ChatHistoryMessage;
import track.msgtest.messenger.messages.Type;
import track.msgtest.messenger.net.ProtocolException;
import track.msgtest.messenger.net.UserErrorException;
import track.msgtest.messenger.teacher.client.MessengerClient;

import java.io.IOException;

/**
 * Created by arseniy on 16.05.17.
 */
public class ChatHistoryInputHandler implements InputHandler {
    public void handle(MessengerClient client, String input) throws IOException, ProtocolException, UserErrorException {
        String[] tokens = input.split(" ",2);
        if (tokens.length < 2) {
            throw new UserErrorException("Illegal input: chat id is needed.");
        }
        Long chatId;
        try {
            chatId = Long.valueOf(tokens[1]);
        } catch (IllegalArgumentException e) {
            throw new UserErrorException("Chat id must be Long.");
        }
        ChatHistoryMessage chatHistoryMessage = new ChatHistoryMessage();
        chatHistoryMessage.setType(Type.MSG_CHAT_HIST);
        chatHistoryMessage.setChatId(chatId);
        client.send(chatHistoryMessage);
    }
}
