package track.msgtest.messenger.messages;

/**
 * Created by arseniy on 18.04.17.
 */
public class StatusMessage extends Message {
    private boolean status;
    private String text;
    private Type type = Type.MSG_STATUS;

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean getStatus() {
        return status;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
