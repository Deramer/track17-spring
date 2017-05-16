package track.msgtest.messenger.messages;

/**
 * Created by arseniy on 16.05.17.
 */
public class SignUpMessage extends Message {
    private String login;
    private String password;
    private Type type = Type.MSG_SIGNUP;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
