package track.msgtest.messenger.teacher.client.messagehandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import track.msgtest.messenger.messages.Message;
import track.msgtest.messenger.teacher.client.MessengerClient;

/**
 * Created by arseniy on 16.05.17.
 */
public class WrongTypeMessageHandler implements MessageHandler {
    private static Logger log = LoggerFactory.getLogger(MessengerClient.class);

    public void handle(Message msg) {
        System.out.println("Not supported type of message");
        log.warn("Not supported type of message, {}", msg.getType());
    }
}
