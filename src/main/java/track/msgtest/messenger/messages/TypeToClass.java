package track.msgtest.messenger.messages;

import java.util.HashMap;

/**
 * Created by arseniy on 16.05.17.
 */
public class TypeToClass {
    private HashMap<String, Object> typeToClass;

    public TypeToClass() {
        typeToClass = new HashMap<>();
        typeToClass.put(Type.MSG_TEXT.toString(), new TextMessage());
        typeToClass.put(Type.MSG_LOGIN.toString(), new LoginMessage());
        typeToClass.put(Type.MSG_CHAT.toString(), new ChatMessage());
        typeToClass.put(Type.MSG_STATUS.toString(), new StatusMessage());
        typeToClass.put(Type.MSG_SIGNUP.toString(), new SignUpMessage());
    }

    public Object getObj(String type) {
        return typeToClass.get(type);
    }
}
