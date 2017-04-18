package track.msgtest.messenger.net;

/**
 * Исключение, которое бросается, когда пользователь неправильно вводит данные.
 */
public class UserErrorException extends Exception {
    public UserErrorException(String msg) {
        super(msg);
    }

    public UserErrorException(Throwable ex) {
        super(ex);
    }
}
