package track.msgtest.messenger.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Postgres implementation of message store.
 */
public class PostgresMessageStore implements MessageStore {
    private static Logger log = LoggerFactory.getLogger(PostgresUserStore.class);

    private Connection connection;

    public PostgresMessageStore(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Long> getUsersIdFromChat(Long chatId) {
        try {
            String query = "SELECT member_id FROM Chats WHERE id = '" + chatId.toString() + "';";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            ArrayList<Long> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(resultSet.getLong(1));
            }
            return result;
        } catch (SQLException e) {
            log.error("Db error, {}", e);
            return null;
        }
    }
}
