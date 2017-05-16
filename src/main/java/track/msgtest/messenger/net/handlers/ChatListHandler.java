package track.msgtest.messenger.net.handlers;

import track.msgtest.messenger.messages.ChatListMessage;
import track.msgtest.messenger.messages.ChatListResultMessage;
import track.msgtest.messenger.messages.Message;
import track.msgtest.messenger.messages.Type;
import track.msgtest.messenger.net.ProtocolException;
import track.msgtest.messenger.net.Session;

import java.io.IOException;

/**
 * Created by arseniy on 16.05.17.
 */
public class ChatListHandler implements Handler {

    @Override
    public void handle(Session session, Message inMsg) throws HandlingException, IOException, ProtocolException {
        ChatListMessage msg = (ChatListMessage)inMsg;
        ChatListResultMessage resultMessage = new ChatListResultMessage();
        resultMessage.setType(Type.MSG_CHAT_LIST_RESULT);
        resultMessage.setUserId(session.getUser().getId());
        resultMessage.setChatsId(session.getMessageStore().getChatsByUserId(session.getUser().getId()));
        session.send(resultMessage);
    }
}
