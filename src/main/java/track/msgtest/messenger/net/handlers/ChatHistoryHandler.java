package track.msgtest.messenger.net.handlers;

import track.msgtest.messenger.messages.*;
import track.msgtest.messenger.net.ProtocolException;
import track.msgtest.messenger.net.Session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Deramer on 16.05.17.
 */
public class ChatHistoryHandler implements Handler {


    @Override
    public void handle(Session session, Message message) throws HandlingException, IOException, ProtocolException {
        Long chatId = ((ChatHistoryMessage)message).getChatId();
        List<Long> msgsId = session.getMessageStore().getMessagesFromChat(chatId);
        List<ChatMessage> msgs = new ArrayList<>();
        for (Long id : msgsId) {
            msgs.add((ChatMessage) session.getMessageStore().getMessageById(id));
        }
        ChatHistoryResultMessage chatHistoryResultMessage = new ChatHistoryResultMessage();
        chatHistoryResultMessage.setType(Type.MSG_CHAT_HIST_RESULT);
        chatHistoryResultMessage.setChatId(chatId);
        chatHistoryResultMessage.setMessages(msgs);
        session.send(chatHistoryResultMessage);
    }
}
