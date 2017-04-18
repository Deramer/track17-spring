package track.msgtest.messenger.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import track.msgtest.messenger.User;
import track.msgtest.messenger.messages.*;
import track.msgtest.messenger.store.MessageStore;
import track.msgtest.messenger.store.UserStore;


/**
 * Сессия связывает бизнес-логику и сетевую часть.
 * Бизнес логика представлена объектом юзера - владельца сессии.
 * Сетевая часть привязывает нас к определнному соединению по сети (от клиента)
 */
public class Session implements Runnable {
    private static Logger log = LoggerFactory.getLogger(Session.class);
    private static volatile HashMap<Long, Session> userSessionMap;
    Object lock;

    private UserStore userStore;
    private MessageStore messageStore;

    private User user;
    private Socket socket;
    private Protocol protocol;

    private InputStream in;
    private OutputStream out;

    private volatile boolean isActive;
    private byte[] buffer = new byte[1024 * 16]; // 16 kb

    public Session(Socket socket, Protocol protocol, UserStore userStore, MessageStore messageStore) throws IOException {
        this.socket = socket;
        this.protocol = protocol;
        this.userStore = userStore;
        this.messageStore = messageStore;

        in = socket.getInputStream();
        out = socket.getOutputStream();

        isActive = true;

        userSessionMap = new HashMap<>();
    }

    public synchronized void send(Message msg) throws ProtocolException, IOException {
        log.info("Send, {}", msg.toString());
        out.write(protocol.encode(msg));
        out.flush();
    }

    public void onMessage(Message msg) {
        // TODO: Пришло некое сообщение от клиента, его нужно обработать
    }

    public void close() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (isActive) {
            try {
                int readBytes = in.read(buffer);
                if (readBytes == -1) {
                    isActive = false;
                    break;
                }
                final byte[] slice = Arrays.copyOfRange(buffer, 0, readBytes);
                Message msg = protocol.decode(slice);

                if (user == null && msg.getType() != Type.MSG_LOGIN) {
                    StatusMessage newMsg = new StatusMessage();
                    newMsg.setStatus(false);
                    newMsg.setType(Type.MSG_STATUS);
                    send(newMsg);
                    continue;
                }
                if (user == null) {
                    login((LoginMessage) msg);
                    continue;
                }
                switch (msg.getType()) {
                    case MSG_TEXT:
                        log.info("/text received, {}", msg);
                        TextMessage txtMsg = (TextMessage) msg;
                        List<Long> targets =  messageStore.getUsersIdFromChat(txtMsg.getSenderId());
                        log.info(targets.toString());
                        for (Long targetId : targets) {
                            if (userSessionMap.containsKey(targetId)) {
                                userSessionMap.get(targetId).send(txtMsg);
                            }
                        }
                        break;
                    default:
                        log.error("Wrong message type, {}", msg);
                }
            } catch (Exception e) {
                isActive = false;
                log.error("Session failed: ", e);
            }
        }
        close();
    }

    private void login(LoginMessage msg) throws IOException, ProtocolException {
        User user = userStore.getUser(msg.getName(), msg.getPass());
        if (user == null) {
            StatusMessage newMsg = new StatusMessage();
            newMsg.setStatus(false);
            newMsg.setType(Type.MSG_STATUS);
            send(newMsg);
        } else {
            StatusMessage newMsg = new StatusMessage();
            newMsg.setStatus(true);
            newMsg.setType(Type.MSG_STATUS);
            send(newMsg);
            if (this.user != null) {
                userSessionMap.remove(this.user.getId(),this);
            }
            this.user = user;
            userSessionMap.put(user.getId(), this);
        }
    }
}