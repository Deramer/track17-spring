package track.msgtest.messenger.messages;

/**
 * Created by arseniy on 16.05.17.
 */
public class InfoResultMessage extends Message {
    Long userId;
    String name;

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}