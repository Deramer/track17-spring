package track.msgtest.messenger.teacher.client.messagehandlers;

import track.msgtest.messenger.messages.ChatListResultMessage;
import track.msgtest.messenger.messages.Message;

/**
 * Created by arseniy on 16.05.17.
 */
public class ChatListResultMessageHandler implements MessageHandler {
    public void handle(Message msg) {
        System.out.println("Chats:");
        for (Long chatId : ((ChatListResultMessage)msg).getChatsId()) {
            System.out.print(chatId + " ");
        }
        System.out.println("");
    }
}
