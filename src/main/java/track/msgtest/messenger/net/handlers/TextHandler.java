package track.msgtest.messenger.net.handlers;

import track.msgtest.messenger.messages.Message;
import track.msgtest.messenger.messages.TextMessage;
import track.msgtest.messenger.net.ProtocolException;
import track.msgtest.messenger.net.Session;

import java.io.IOException;
import java.util.List;

/**
 * Created by arseniy on 18.04.17.
 */
public class TextHandler implements Handler {

    public void handle(Session session, Message msg) throws HandlingException, IOException, ProtocolException {
        session.getLog().info("/text received, {}", msg);
        TextMessage txtMsg = (TextMessage) msg;
        List<Long> targets =  session.getMessageStore().getUsersIdFromChat(txtMsg.getSenderId());
        for (Long targetId : targets) {
            if (session.hashContainsKey(targetId)) {
                session.hashGet(targetId).send(txtMsg);
            }
        }
    }
}
