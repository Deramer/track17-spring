package track.msgtest.messenger.net;

import antlr.debug.MessageAdapter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import track.msgtest.messenger.messages.Message;
import track.msgtest.messenger.messages.Type;
import track.msgtest.messenger.messages.TypeToClass;

import java.io.IOException;

/**
 * Created by arseniy on 18.04.17.
 */
public class JsonProtocol implements Protocol {
    private static Logger log = LoggerFactory.getLogger(StringProtocol.class);

    private TypeToClass typeToClass = new TypeToClass();

    @Override
    public Message decode(byte[] bytes) {
        ObjectMapper objectMapper = new ObjectMapper();
        String type;
        Message msg;
        try {
            type = objectMapper.readTree(bytes).path("type").asText();
            msg = (Message)objectMapper.readValue(bytes, typeToClass.getObj(type).getClass());
        } catch (IOException e) {
            log.error("Couldn't decode message", e);
            return null;
        }
        log.info("decoded: {}", msg);
        return msg;
    }

    public byte[] encode(Message msg) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonStr = objectMapper.writeValueAsString(msg);
            return jsonStr.getBytes();
        } catch (JsonProcessingException e) {
            log.error("Couldn't encode message", e);
            return null;
        }
    }
}
