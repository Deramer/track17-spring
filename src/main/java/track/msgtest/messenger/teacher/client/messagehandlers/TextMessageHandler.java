package track.msgtest.messenger.teacher.client.messagehandlers;

import track.msgtest.messenger.messages.Message;
import track.msgtest.messenger.messages.TextMessage;

/**
 * Created by arseniy on 16.05.17.
 */
public class TextMessageHandler implements MessageHandler {
    public void handle(Message msg) {
        System.out.print("Message from " + msg.getSenderId() + ", from chat ");
        System.out.println(((TextMessage)msg).getChatId());
        System.out.println(((TextMessage)msg).getText());
    }
}
