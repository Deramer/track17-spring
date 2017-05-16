package track.msgtest.messenger.teacher.client.inputhandlers;

import track.msgtest.messenger.messages.InfoMessage;
import track.msgtest.messenger.messages.Type;
import track.msgtest.messenger.net.ProtocolException;
import track.msgtest.messenger.net.UserErrorException;
import track.msgtest.messenger.teacher.client.MessengerClient;

import java.io.IOException;

/**
 * Created by arseniy on 16.05.17.
 */
public class InfoInputHandler implements InputHandler {
    public void handle(MessengerClient client, String input) throws IOException, ProtocolException, UserErrorException {
        String[] tokens = input.split(" ",2);
        InfoMessage infoMessage = new InfoMessage();
        infoMessage.setType(Type.MSG_INFO);
        if (tokens.length > 1) {
            try {
                infoMessage.setUserId(Long.valueOf(tokens[1]));
            } catch (IllegalArgumentException e) {
                throw new UserErrorException("Wrong id for info message.");
            }
        }
        client.send(infoMessage);
    }
}
