package track.msgtest.messenger.teacher.client.inputhandlers;

import track.msgtest.messenger.messages.TextMessage;
import track.msgtest.messenger.messages.Type;
import track.msgtest.messenger.net.ProtocolException;
import track.msgtest.messenger.net.UserErrorException;
import track.msgtest.messenger.teacher.client.MessengerClient;

import java.io.IOException;

/**
 * Created by arseniy on 16.05.17.
 */
public class TextInputHandler implements InputHandler {
    public void handle(MessengerClient client, String input) throws IOException, ProtocolException, UserErrorException {
        String[] tokens = input.split(" ", 3);
        if (tokens.length < 3) {
            throw new UserErrorException("/text has less than 3 tokens.");
        }
        TextMessage sendMessage = new TextMessage();
        sendMessage.setType(Type.MSG_TEXT);
        try {
            sendMessage.setChatId(Long.parseLong(tokens[1]));
        } catch (NumberFormatException e) {
            throw new UserErrorException("Chat id must be Long.");
        }
        sendMessage.setText(tokens[2]);
        client.send(sendMessage);
    }
}
