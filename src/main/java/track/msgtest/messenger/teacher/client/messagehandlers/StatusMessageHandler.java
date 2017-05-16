package track.msgtest.messenger.teacher.client.messagehandlers;

import track.msgtest.messenger.messages.Message;
import track.msgtest.messenger.messages.StatusMessage;

/**
 * Created by arseniy on 16.05.17.
 */
public class StatusMessageHandler implements MessageHandler {
    public void handle(Message msg) {
        System.out.print("Status: ");
        System.out.println(((StatusMessage) msg).getStatus());
        System.out.println(((StatusMessage) msg).getText());
    }
}
