package track.msgtest.messenger.net.handlers;

import track.msgtest.messenger.User;
import track.msgtest.messenger.messages.Message;
import track.msgtest.messenger.messages.SignUpMessage;
import track.msgtest.messenger.messages.StatusMessage;
import track.msgtest.messenger.messages.Type;
import track.msgtest.messenger.net.ProtocolException;
import track.msgtest.messenger.net.Session;
import track.msgtest.messenger.store.UserStore;

import java.io.IOException;

/**
 * Created by arseniy on 16.05.17.
 */
public class SignUpHandler implements Handler {
    @Override
    public void handle(Session session, Message inMsg) throws HandlingException, IOException, ProtocolException {
        SignUpMessage msg = (SignUpMessage)inMsg;
        UserStore userStore = session.getUserStore();
        User user = new User();
        user.setName(msg.getLogin());
        user.setPass(msg.getPassword());
        user = userStore.addUser(user);
        if (user != null) {
            StatusMessage statusMessage = new StatusMessage();
            statusMessage.setType(Type.MSG_STATUS);
            statusMessage.setStatus(true);
            String text = "You've successfully signed up! Log in now, please. Your id is " + user.getId() + ".";
            statusMessage.setText(text);
            session.send(statusMessage);
        } else {
            String text = "Sign up failed. Try again.";
            StatusMessage statusMessage = new StatusMessage();
            statusMessage.setType(Type.MSG_STATUS);
            statusMessage.setStatus(false);
            statusMessage.setText(text);
            session.send(statusMessage);
        }

    }
}
