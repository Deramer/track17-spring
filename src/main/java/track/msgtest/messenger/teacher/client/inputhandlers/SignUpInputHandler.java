package track.msgtest.messenger.teacher.client.inputhandlers;

import track.msgtest.messenger.messages.SignUpMessage;
import track.msgtest.messenger.messages.Type;
import track.msgtest.messenger.net.ProtocolException;
import track.msgtest.messenger.net.UserErrorException;
import track.msgtest.messenger.teacher.client.MessengerClient;

import java.io.IOException;

/**
 * Created by arseniy on 16.05.17.
 */
public class SignUpInputHandler implements InputHandler {
    public void handle(MessengerClient client, String input) throws IOException, ProtocolException, UserErrorException {
        String[] tokens = input.split(" ", 3);
        if (tokens.length < 3) {
            throw new UserErrorException("/signup has less than 3 tokens.");
        }
        SignUpMessage signUpMessage = new SignUpMessage();
        signUpMessage.setType(Type.MSG_SIGNUP);
        signUpMessage.setLogin(tokens[1]);
        signUpMessage.setPassword(tokens[2]);
        client.send(signUpMessage);
    }
}
