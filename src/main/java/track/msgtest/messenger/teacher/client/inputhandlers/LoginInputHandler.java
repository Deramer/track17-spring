package track.msgtest.messenger.teacher.client.inputhandlers;

import track.msgtest.messenger.messages.LoginMessage;
import track.msgtest.messenger.net.ProtocolException;
import track.msgtest.messenger.net.UserErrorException;
import track.msgtest.messenger.teacher.client.MessengerClient;

import java.io.IOException;

/**
 * Created by arseniy on 16.05.17.
 */
public class LoginInputHandler implements InputHandler {
    public void handle(MessengerClient client, String input) throws IOException, ProtocolException, UserErrorException {
        String[] tokens = input.split(" ", 3);
        if (tokens.length < 3) {
            throw new UserErrorException("/login has less than 3 tokens.");
        }
        LoginMessage logMsg = new LoginMessage(tokens[1], tokens[2]);
        client.send(logMsg);
    }
}
