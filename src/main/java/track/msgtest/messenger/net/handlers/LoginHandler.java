package track.msgtest.messenger.net.handlers;

import track.msgtest.messenger.User;
import track.msgtest.messenger.messages.LoginMessage;
import track.msgtest.messenger.messages.Message;
import track.msgtest.messenger.messages.StatusMessage;
import track.msgtest.messenger.messages.Type;
import track.msgtest.messenger.net.ProtocolException;
import track.msgtest.messenger.net.Session;

import java.io.IOException;
import java.util.logging.*;

/**
 * Created by arseniy on 18.04.17.
 */
public class LoginHandler implements Handler {

    public void handle(Session session, Message initMsg) throws HandlingException, IOException, ProtocolException {
        if (initMsg.getType() != Type.MSG_LOGIN) {
            throw new HandlingException("Wrong type for Login handler.");
        }
        LoginMessage msg = (LoginMessage) initMsg;
        User user = session.getUserStore().getUser(msg.getName(), msg.getPass());
        if (user == null) {
            StatusMessage newMsg = new StatusMessage();
            newMsg.setStatus(false);
            newMsg.setType(Type.MSG_STATUS);
            session.send(newMsg);
        } else {
            StatusMessage newMsg = new StatusMessage();
            newMsg.setStatus(true);
            newMsg.setType(Type.MSG_STATUS);
            session.send(newMsg);
            if (session.getUser() != null) {
                session.removeFromHash(session.getUser().getId());
            }
            session.setUser(user);
            session.addToHash(user.getId(), session);
        }
    }

    public void handleLoginOnly(Session session, Message msg) throws HandlingException, IOException, ProtocolException {
        if (msg.getType() != Type.MSG_LOGIN) {
            StatusMessage newMsg = new StatusMessage();
            newMsg.setStatus(false);
            newMsg.setType(Type.MSG_STATUS);
            session.send(newMsg);
        } else {
            handle(session, msg);
        }
    }
}
