package track.msgtest.messenger.messages;

import java.util.List;

/**
 * Created by arseniy on 16.05.17.
 */
public class CreateChatMessage extends Message {
    private List<Long> usersId;

    public void setUsersId(List<Long> usersId) {
        this.usersId = usersId;
    }

    public List<Long> getUsersId() {
        return usersId;
    }
}
