package track.msgtest.messenger.teacher.client.inputhandlers;

import track.msgtest.messenger.messages.CreateChatMessage;
import track.msgtest.messenger.messages.Type;
import track.msgtest.messenger.net.ProtocolException;
import track.msgtest.messenger.net.UserErrorException;
import track.msgtest.messenger.teacher.client.MessengerClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by arseniy on 16.05.17.
 */
public class CreateChatInputHandler implements InputHandler {
    public void handle(MessengerClient client, String input) throws IOException, ProtocolException, UserErrorException {
        String[] tokens = input.split( " ", 2);
        if (tokens.length < 2) {
            throw new UserErrorException("Illegal input: list of ids is needed.");
        }
        List<Long> ids = new ArrayList<>();
        for (String id : tokens[1].split(",")) {
            try {
                ids.add(Long.valueOf(id.trim()));
            } catch (IllegalArgumentException e) {
                throw new UserErrorException("Illegal input: not all ids are numbers.");
            }
        }
        CreateChatMessage createChatMessage = new CreateChatMessage();
        createChatMessage.setType(Type.MSG_CHAT_CREATE);
        createChatMessage.setUsersId(ids);
        client.send(createChatMessage);
    }
}
