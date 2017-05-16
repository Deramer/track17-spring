package track.msgtest.messenger.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import track.msgtest.messenger.User;

import java.sql.*;

/**
 * Postgres implementation.
 */
public class PostgresUserStore implements UserStore {

    private static Logger log = LoggerFactory.getLogger(PostgresUserStore.class);

    private Connection connection;

    private PreparedStatement selectUserStatement;
    private PreparedStatement updateUserStatement;
    private PreparedStatement insertUserStatement;
    private PreparedStatement selectUserByIdStatement;

    public PostgresUserStore(Connection connection) {
        this.connection = connection;
        String selectUserQuery = "SELECT id, name, password FROM Users WHERE name = ? AND password = ?;";
        String updateUserQuery = "UPDATE Users SET name = ? AND password = ? WHERE id = ?;";
        String insertUserQuery = "INSERT INTO Users (name, password) VALUES (?, ?) RETURNING *;";
        String selectUserByIdQuery = "SELECT id, name, password FROM Users WHERE id = ?;";
        try {
            selectUserStatement = this.connection.prepareStatement(selectUserQuery);
        } catch (SQLException e) {
            log.error("Couldn't prepare statement SelectUser", e);
        }
        try {
            updateUserStatement = this.connection.prepareStatement(updateUserQuery);
        } catch (SQLException e) {
            log.error("Couldn't prepare statement UpdateUser", e);
        }
        try {
            insertUserStatement = this.connection.prepareStatement(insertUserQuery);
        } catch (SQLException e) {
            log.error("Couldn't prepare statement InsertUser", e);
        }
        try {
            selectUserByIdStatement = this.connection.prepareStatement(selectUserByIdQuery);
        } catch (SQLException e) {
            log.error("Couldn't prepare statement SelectUserById", e);
        }
    }

    public void setDatabaseManager(Connection connection) {
        this.connection = connection;
    }

    @Override
    public synchronized User getUser(String login, String pass) {
        try {
            selectUserStatement.setString(1, login);
            selectUserStatement.setString(2, pass);
            ResultSet resultSet = selectUserStatement.executeQuery();
            selectUserStatement.clearParameters();
            if (resultSet.next()) {
                User user = new User(login, pass);
                user.setId(resultSet.getLong(1));
                return user;
            } else {
                return null;
            }
        } catch (SQLException e) {
            log.error("Db error", e);
            return null;
        }
    }

    @Override
    public synchronized User updateUser(User userWas, User userWouldBe) {
        if (userWas.getId() != userWouldBe.getId()) {
            log.info("Wrong user update: ids are different, {}, {}", userWas, userWouldBe);
            return null;
        }
        try {
            updateUserStatement.setString(1, userWouldBe.getName());
            updateUserStatement.setString(2,userWouldBe.getPass());
            updateUserStatement.setLong(3,userWas.getId());
            int res = updateUserStatement.executeUpdate();
            updateUserStatement.clearParameters();
            return userWouldBe;
        } catch (SQLException e) {
            log.error("Db error", e);
            return null;
        }
    }

    @Override
    public synchronized  User addUser(User user) {
        try {
            insertUserStatement.setString(1,user.getName());
            insertUserStatement.setString(2, user.getPass());
            ResultSet resultSet = insertUserStatement.executeQuery();
            insertUserStatement.clearParameters();
            if (resultSet.next()) {
                user.setId(resultSet.getLong(1));
                return user;
            } else {
                return null;
            }
        } catch (SQLException e) {
            log.error("Db error", e);
            return null;
        }
    }

    @Override
    public synchronized  User getUserById(Long id) {
        try {
            selectUserByIdStatement.setLong(1, id);
            ResultSet resultSet = selectUserByIdStatement.executeQuery();
            selectUserByIdStatement.clearParameters();
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

    public boolean close() {
        try {
            connection.close();
            return true;
        } catch (SQLException e) {
            log.error("Couldn't close connection", e);
            return false;
        }
    }
}
