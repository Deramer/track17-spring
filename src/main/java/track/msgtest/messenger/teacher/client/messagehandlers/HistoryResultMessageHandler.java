package track.msgtest.messenger.teacher.client.messagehandlers;

import track.msgtest.messenger.messages.ChatHistoryResultMessage;
import track.msgtest.messenger.messages.ChatMessage;
import track.msgtest.messenger.messages.Message;

/**
 * Created by arseniy on 16.05.17.
 */
public class HistoryResultMessageHandler implements MessageHandler {
    public void handle(Message msg) {
        for (ChatMessage chatMessage : ((ChatHistoryResultMessage)msg).getMessages()) {
            System.out.println(chatMessage);
        }
    }
}
