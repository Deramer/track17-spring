package track.msgtest.messenger.messages;

import java.util.List;

/**
 * Created by arseniy on 16.05.17.
 */
public class ChatHistoryResultMessage extends Message {
    private Long chatId;
    private List<ChatMessage> messages;

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
    }
}
