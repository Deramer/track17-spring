package track.msgtest.messenger.net.handlers;

import track.msgtest.messenger.messages.CreateChatMessage;
import track.msgtest.messenger.messages.Message;
import track.msgtest.messenger.messages.StatusMessage;
import track.msgtest.messenger.messages.Type;
import track.msgtest.messenger.net.ProtocolException;
import track.msgtest.messenger.net.Session;

import java.io.IOException;
import java.util.List;

/**
 * Go away, yellow.
 */
public class CreateChatHandler implements Handler {

    @Override
    public void handle(Session session, Message inMsg) throws HandlingException, IOException, ProtocolException {
        CreateChatMessage msg = (CreateChatMessage)inMsg;
        List<Long> usersId = msg.getUsersId();
        usersId.add(session.getUser().getId());
        Long chatId = session.getMessageStore().getOrAddChatWithUsers(usersId);
        StatusMessage statusMessage = new StatusMessage();
        statusMessage.setType(Type.MSG_STATUS);
        if (chatId != null) {
            statusMessage.setStatus(true);
            statusMessage.setText("Success! Chat id is " + chatId);
        } else {
            statusMessage.setStatus(false);
            statusMessage.setText("Failure. Try again.");
        }
        session.send(statusMessage);
    }
}
