package track.msgtest.messenger.teacher.client.messagehandlers;

import track.msgtest.messenger.messages.Type;

import java.util.HashMap;

/**
 * Created by arseniy on 16.05.17.
 */
public class MessagesController {
    private HashMap<Type, MessageHandler> map;
    private WrongTypeMessageHandler wrongTypeMessageHandler = new WrongTypeMessageHandler();

    public MessagesController() {
        map = new HashMap<>();
        map.put(Type.MSG_STATUS, new StatusMessageHandler());
        map.put(Type.MSG_TEXT, new TextMessageHandler());
        map.put(Type.MSG_CHAT_HIST_RESULT, new HistoryResultMessageHandler());
        map.put(Type.MSG_INFO_RESULT, new InfoResultMessageHandler());
        map.put(Type.MSG_CHAT_LIST_RESULT, new ChatListResultMessageHandler());
    }

    public MessageHandler handler(Type type) {
        if (map.containsKey(type)) {
            return map.get(type);
        } else {
            return wrongTypeMessageHandler;
        }
    }
}
