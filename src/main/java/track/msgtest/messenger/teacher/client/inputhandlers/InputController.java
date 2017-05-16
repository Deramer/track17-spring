package track.msgtest.messenger.teacher.client.inputhandlers;

import java.util.HashMap;

/**
 * Created by arseniy on 16.05.17.
 */
public class InputController {
    private HashMap<String, InputHandler> map;
    private WrongInputHandler wrongInputHandler = new WrongInputHandler();

    public InputController() {
        map = new HashMap<>();
        map.put("/signup", new SignUpInputHandler());
        map.put("/login", new LoginInputHandler());
        map.put("/text", new TextInputHandler());
        map.put("/help", new HelpInputHandler());
        map.put("/info", new InfoInputHandler());
        map.put("/chat_list", new ChatListInputHandler());
        map.put("/create_chat", new CreateChatInputHandler());
        map.put("/chat_hist", new ChatHistoryInputHandler());
    }

    public InputHandler handler(String cmd) {
        if (map.containsKey(cmd)) {
            return map.get(cmd);
        } else {
            return wrongInputHandler;
        }
    }
}
