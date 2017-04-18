package track.msgtest.messenger.net.handlers;

import track.msgtest.messenger.messages.Type;

import java.util.HashMap;

/**
 * Created by arseniy on 18.04.17.
 */
public class Controller {
    private HashMap<Type, Handler> map;

    public Controller() {
        map = new HashMap<>();
        map.put(Type.MSG_LOGIN, new LoginHandler());
        map.put(Type.MSG_TEXT, new TextHandler());
    }

    public Handler handler(Type type) {
        return map.get(type);
    }
}
