package track.msgtest.messenger.teacher.client.inputhandlers;

import track.msgtest.messenger.net.ProtocolException;
import track.msgtest.messenger.net.UserErrorException;
import track.msgtest.messenger.teacher.client.MessengerClient;

import java.io.IOException;

/**
 * Created by arseniy on 16.05.17.
 */
public interface InputHandler {
    void handle(MessengerClient messengerClient, String input) throws IOException, ProtocolException, UserErrorException;
}
