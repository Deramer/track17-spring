package track.msgtest.messenger.messages;

import track.msgtest.messenger.messages.Message;

/**
 * Created by arseniy on 16.05.17.
 */
public class ChatHistoryMessage extends Message {
    private Long chatId;

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public Long getChatId() {
        return chatId;
    }
}
