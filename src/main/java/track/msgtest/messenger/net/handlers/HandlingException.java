package track.msgtest.messenger.net.handlers;

/**
 * Created by arseniy on 18.04.17.
 */
public class HandlingException extends Exception {
    public HandlingException(String msg) {
        super(msg);
    }

    public HandlingException(Throwable ex) {
        super(ex);
    }
}
