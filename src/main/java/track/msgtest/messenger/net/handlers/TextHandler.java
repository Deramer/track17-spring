package track.msgtest.messenger.net.handlers;

import track.msgtest.messenger.messages.Message;
import track.msgtest.messenger.messages.TextMessage;
import track.msgtest.messenger.net.ProtocolException;
import track.msgtest.messenger.net.Session;

import java.io.IOException;
import java.util.List;

/**
 * Can I at least make it green? Or purple?
 */
public class TextHandler implements Handler {

    public void handle(Session session, Message msg) throws HandlingException, IOException, ProtocolException {
        session.getLog().info("/text received, {}", msg);
        TextMessage txtMsg = (TextMessage) msg;
        List<Long> targets =  session.getMessageStore().getUsersIdFromChat(txtMsg.getChatId());
        txtMsg.setSenderId(session.getUser().getId());
        session.getMessageStore().addMessage(txtMsg);
        for (Long targetId : targets) {
            if (session.hashContainsKey(targetId)) {
                session.hashGet(targetId).send(txtMsg);
            }
        }
    }
}
