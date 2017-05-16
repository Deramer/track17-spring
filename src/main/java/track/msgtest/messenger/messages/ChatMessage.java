package track.msgtest.messenger.messages;

/**
 * Created by arseniy on 16.05.17.
 */
public class ChatMessage extends Message {
    Type type = Type.MSG_CHAT;
    private String text;
    private String timestamp;

    public ChatMessage(Long senderId, String text, String timestamp) {
        this.senderId = senderId;
        this.text = text;
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
