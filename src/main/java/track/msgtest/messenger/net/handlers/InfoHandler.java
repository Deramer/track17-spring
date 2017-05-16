package track.msgtest.messenger.net.handlers;

import track.msgtest.messenger.User;
import track.msgtest.messenger.messages.*;
import track.msgtest.messenger.net.ProtocolException;
import track.msgtest.messenger.net.Session;

import java.io.IOException;

/**
 * Created by arseniy on 16.05.17.
 */
public class InfoHandler implements Handler {
    @Override
    public void handle(Session session, Message inMsg) throws HandlingException, IOException, ProtocolException {
        InfoMessage msg = (InfoMessage)inMsg;
        Long userId = msg.getUserId();
        if (userId == null) {
            userId = session.getUser().getId();
        }
        User user = session.getUserStore().getUserById(userId);
        if (user != null) {
            InfoResultMessage infoResultMessage = new InfoResultMessage();
            infoResultMessage.setType(Type.MSG_INFO_RESULT);
            infoResultMessage.setUserId(user.getId());
            infoResultMessage.setName(user.getName());
            session.send(infoResultMessage);
        } else {
            StatusMessage statusMessage = new StatusMessage();
            statusMessage.setStatus(false);
            statusMessage.setType(Type.MSG_STATUS);
            statusMessage.setText("There's no such user.");
            session.send(statusMessage);
        }
    }
}
