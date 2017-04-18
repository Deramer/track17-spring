package track.msgtest.messenger.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import track.msgtest.messenger.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Postgres implementation.
 */
public class PostgresUserStore implements UserStore {

    static Logger log = LoggerFactory.getLogger(PostgresUserStore.class);

    private Connection connection;

    public PostgresUserStore(Connection connection) {
        this.connection = connection;
    }

    public void setDatabaseManager(Connection connection) {
        this.connection = connection;
    }

    @Override
    public User getUser(String login, String pass) {
        try {
            String query = "SELECT id, name, password FROM Users WHERE name = '";
            query += login + "" + "' AND password = '" + pass + "';";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                User user = new User(login, pass);
                user.setId(resultSet.getLong(1));
                return user;
            } else {
                return null;
            }
        } catch (SQLException e) {
            log.error("Db error, {}", e);
            return null;
        }
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public User addUser(User user) {
        return null;
    }

    @Override
    public User getUserById(Long id) {
        try {
            String query = "SELECT id, name, password FROM Users WHERE id = '" + id.toString() + "';";
            ResultSet resultSet = connection.createStatement().executeQuery(query);
            if (resultSet.next()) {
                User user = new User(resultSet.getString(2), resultSet.getString(3));
                user.setId(resultSet.getLong(1));
                return user;
            } else {
                return null;
            }
        } catch (SQLException e) {
            log.error("Db error, {}", e);
            return null;
        }
    }
}
