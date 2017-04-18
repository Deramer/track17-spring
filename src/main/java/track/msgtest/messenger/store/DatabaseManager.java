package track.msgtest.messenger.store;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Settings keeper and connection creator.
 */
public class DatabaseManager {
    private String url = "jdbc:postgresql://localhost/track_messenger";
    private String user = "track_java_role";
    private String password = "passwd";
    private Connection connection = null;

    public Connection connect() throws SQLException {
        if (connection == null) {
            connection = DriverManager.getConnection(url, user, password);
        }
        return connection;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPasswd(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public Connection getConnection() {
        return connection;
    }
}
