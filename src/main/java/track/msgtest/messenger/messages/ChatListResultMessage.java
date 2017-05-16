package track.msgtest.messenger.messages;

import java.util.List;

/**
 * Created by arseniy on 16.05.17.
 */
public class ChatListResultMessage extends Message {
    private Long userId;
    private List<Long> chatsId;

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public List<Long> getChatsId() {
        return chatsId;
    }

    public void setChatsId(List<Long> chatsId) {
        this.chatsId = chatsId;
    }
}
