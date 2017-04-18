package track.msgtest.messenger.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import track.msgtest.messenger.messages.*;


/**
 * Простейший протокол передачи данных
 */
public class StringProtocol implements Protocol {

    static Logger log = LoggerFactory.getLogger(StringProtocol.class);

    public static final String DELIMITER = ";";

    @Override
    public Message decode(byte[] bytes) throws ProtocolException {
        String str = new String(bytes);
        log.info("decoded: {}", str);
        String[] tokens = str.split(DELIMITER);
        Type type = Type.valueOf(tokens[0]);
        switch (type) {
            case MSG_STATUS:
                StatusMessage statusMessage = new StatusMessage();
                statusMessage.setStatus(Boolean.valueOf(tokens[1]));
                return statusMessage;
            case MSG_TEXT:
                TextMessage textMsg = new TextMessage();
                textMsg.setSenderId(parseLong(tokens[1]));
                textMsg.setText(tokens[2]);
                textMsg.setType(type);
                return textMsg;
            case MSG_LOGIN:
                LoginMessage logMsg = new LoginMessage(tokens[1], tokens[2]);
                return logMsg;
            default:
                throw new ProtocolException("Invalid type: " + type);
        }
    }

    @Override
    public byte[] encode(Message msg) throws ProtocolException {
        StringBuilder builder = new StringBuilder();
        Type type = msg.getType();
        builder.append(type).append(DELIMITER);
        switch (type) {
            case MSG_STATUS:
                StatusMessage statusMessage = (StatusMessage) msg;
                builder.append(String.valueOf(statusMessage.getStatus())).append(DELIMITER);
                break;
            case MSG_TEXT:
                TextMessage textMessage = (TextMessage) msg;
                builder.append(String.valueOf(textMessage.getSenderId())).append(DELIMITER);
                builder.append(textMessage.getText()).append(DELIMITER);
                break;
            case MSG_LOGIN:
                LoginMessage logMsg = (LoginMessage) msg;
                builder.append(logMsg.getName()).append(DELIMITER);
                builder.append(logMsg.getPass()).append(DELIMITER);
                break;
            default:
                throw new ProtocolException("Invalid type: " + type);


        }
        log.info("encoded: {}", builder.toString());
        return builder.toString().getBytes();
    }

    private Long parseLong(String str) {
        try {
            return Long.parseLong(str);
        } catch (Exception e) {
            // who cares
        }
        return null;
    }
}