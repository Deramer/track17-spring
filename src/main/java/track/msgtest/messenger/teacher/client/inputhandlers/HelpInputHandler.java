package track.msgtest.messenger.teacher.client.inputhandlers;

import track.msgtest.messenger.net.ProtocolException;
import track.msgtest.messenger.net.UserErrorException;
import track.msgtest.messenger.teacher.client.MessengerClient;

import java.io.IOException;

/**
 * Created by arseniy on 16.05.17.
 */
public class HelpInputHandler implements InputHandler {
    public void handle(MessengerClient client, String input) throws IOException, ProtocolException, UserErrorException {
        String response = "/login username password\n" +
                "/text chat_id message\n" +
                "/signup username password\n" +
                "/info [user_id] // without id - about self\n" +
                "/chat_list // your chats\n" +
                "/chat_hist chat_id // chat history\n" +
                "/create_chat id1, id2, id3 // create chat with these users";
        System.out.println(response);
    }
}
