package track.msgtest.messenger.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import track.msgtest.messenger.messages.ChatMessage;
import track.msgtest.messenger.messages.Message;
import track.msgtest.messenger.messages.TextMessage;
import track.msgtest.messenger.messages.Type;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Postgres implementation of message store.
 */
public class PostgresMessageStore implements MessageStore {
    private static Logger log = LoggerFactory.getLogger(PostgresUserStore.class);

    private Connection connection;

    private PreparedStatement selectUsersIdStatement;
    private PreparedStatement selectMessagesFromChatStatement;
    private PreparedStatement selectMessageByIdStatement;
    private PreparedStatement insertMessageStatement;
    private PreparedStatement insertUserToChatStatement;
    private PreparedStatement selectChatsByUserStatement;

    public PostgresMessageStore(Connection connection) {
        this.connection = connection;
        String selectUsersIdQuery = "SELECT member_id FROM Chats WHERE id = ?;";
        String selectMessagesFromChatQuery = "SELECT id FROM Messages WHERE to_id = ?;";
        String selectMessageByIdQuery = "SELECT from_id, text, timestamp FROM Messages WHERE id = ?;";
        String insertMessageQuery = "INSERT INTO Messages (from_id, to_id, text, timestamp) VALUES (?, ?, ?, now());";
        String insertUserToChatQuery = "INSERT INTO Chats (id, member_id) VALUES (?, ?);";
        String selectChatsByUserQuery = "SELECT id FROM Chats WHERE member_id = ?;";
        try {
            selectMessageByIdStatement = this.connection.prepareStatement(selectMessageByIdQuery);
        } catch (SQLException e) {
            log.error("Couldn't prepare selectMessageById", e);
        }
        try {
            selectMessagesFromChatStatement = this.connection.prepareStatement(selectMessagesFromChatQuery);
        } catch (SQLException e) {
            log.error("Couldn't prepare selectMessageFromChat", e);
        }
        try {
            selectUsersIdStatement = this.connection.prepareStatement(selectUsersIdQuery);
        } catch (SQLException e) {
            log.error("Couldn't prepare selectUsersId", e);
        }
        try {
            insertMessageStatement = this.connection.prepareStatement(insertMessageQuery);
        } catch (SQLException e) {
            log.error("Couldn't prepare insertMessage", e);
        }
        try {
            insertUserToChatStatement = this.connection.prepareStatement(insertUserToChatQuery);
        } catch (SQLException e) {
            log.error("Couldn't prepare selectMessageById", e);
        }
        try {
            selectChatsByUserStatement = this.connection.prepareStatement(selectChatsByUserQuery);
        } catch (SQLException e) {
            log.error("Couldn't prepare selectChatsByUser", e);
        }
    }

    @Override
    public synchronized List<Long> getUsersIdFromChat(Long chatId) {
        try {
            selectUsersIdStatement.setLong(1,chatId);
            ResultSet resultSet = selectUsersIdStatement.executeQuery();
            selectUsersIdStatement.clearParameters();
            ArrayList<Long> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(resultSet.getLong(1));
            }
            return result;
        } catch (SQLException e) {
            log.error("Db error", e);
            return null;
        }
    }

    @Override
    public synchronized List<Long> getMessagesFromChat(Long chatId) {
        try {
            selectMessagesFromChatStatement.setLong(1, chatId);
            ResultSet resultSet = selectMessagesFromChatStatement.executeQuery();
            selectMessagesFromChatStatement.clearParameters();
            ArrayList<Long> messages = new ArrayList<>();
            while (resultSet.next()) {
                messages.add(resultSet.getLong(1));
            }
            return messages;
        } catch (SQLException e) {
            log.error("Db error", e);
            return null;
        }
    }

    @Override
    public synchronized Message getMessageById(Long messageId) {
        try {
            selectMessageByIdStatement.setLong(1, messageId);
            ResultSet resultSet = selectMessageByIdStatement.executeQuery();
            selectMessageByIdStatement.clearParameters();
            if (resultSet.next()) {
                return new ChatMessage(resultSet.getLong(1),
                        resultSet.getString(2), resultSet.getString(3));
            } else {
                return null;
            }
        } catch (SQLException e) {
            log.error("Db error", e);
            return null;
        }
    }

    @Override
    public synchronized void addMessage(Long chatId, Message message) {
        if (message.getType() != Type.MSG_TEXT) {
            log.info("Wrong type of message to add, only text allowed, {}", message);
            return;
        }
        try {
            insertMessageStatement.setLong(1, message.getSenderId());
            insertMessageStatement.setLong(2, chatId);
            insertMessageStatement.setString(3, ((TextMessage)message).getText());
            insertMessageStatement.execute();
            insertMessageStatement.clearParameters();
        } catch (SQLException e) {
            log.error("Couldn't add message", e);
        }
    }

    @Override
    public synchronized void addUserToChat(Long userId, Long chatId) {
        try {
            insertUserToChatStatement.setLong(1, chatId);
            insertUserToChatStatement.setLong(2, userId);
            insertUserToChatStatement.execute();
            insertUserToChatStatement.clearParameters();
        } catch (SQLException e) {
            log.error("Couldn't add user to chat", e);
        }
    }

    @Override
    public synchronized List<Long> getChatsByUserId(Long userId) {
        try {
            selectChatsByUserStatement.setLong(1, userId);
            ResultSet resultSet = selectChatsByUserStatement.executeQuery();
            selectChatsByUserStatement.clearParameters();
            List<Long> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(resultSet.getLong(1));
            }
            return result;
        } catch (SQLException e) {
            log.error("Db error", e);
            return null;
        }
    }
}
