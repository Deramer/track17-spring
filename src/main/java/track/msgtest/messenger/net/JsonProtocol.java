package track.msgtest.messenger.net;

import track.msgtest.messenger.messages.Message;

/**
 * Created by arseniy on 18.04.17.
 */
public class JsonProtocol implements Protocol {

    @Override
    public Message decode(byte[] bytes) {
        return null;
    }

    public byte[] encode(Message msg) {
        return null;
    }
}
