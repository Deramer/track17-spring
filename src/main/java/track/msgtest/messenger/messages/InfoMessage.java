package track.msgtest.messenger.messages;

/**
 * Created by arseniy on 16.05.17.
 */
public class InfoMessage extends Message {
    Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
