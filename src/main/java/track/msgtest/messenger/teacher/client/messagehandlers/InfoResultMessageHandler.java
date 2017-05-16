package track.msgtest.messenger.teacher.client.messagehandlers;

import track.msgtest.messenger.messages.InfoResultMessage;
import track.msgtest.messenger.messages.Message;

/**
 * Created by arseniy on 16.05.17.
 */
public class InfoResultMessageHandler implements MessageHandler {
    public void handle(Message msg) {
        System.out.println("ID: " + ((InfoResultMessage)msg).getUserId());
        System.out.println("name: " + ((InfoResultMessage)msg).getName());
    }
}
