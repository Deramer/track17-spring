package track.msgtest.messenger.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import track.msgtest.messenger.store.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 */
public class MessengerServer {

    private static Logger log = LoggerFactory.getLogger(MessengerServer.class);

    private static int port = 19000;
    private static int threadNum = 10;

    MessengerServer() {}

    public static void setPort(int port1) {
        port = port1;
    }

    public static void setThreadNum(int threadNum1) {
        threadNum = threadNum1;
    }

    public static void main(String [] args) throws IOException, SQLException {
        ServerSocket serverSocket = new ServerSocket(port);
        ExecutorService pool = Executors.newFixedThreadPool(threadNum);
        DatabaseManager databaseManager = new DatabaseManager();
        UserStore userStore = new PostgresUserStore(databaseManager.connect());
        MessageStore messageStore = new PostgresMessageStore(databaseManager.connect());

        log.info("Server starts accepting connections.");
        while (true) {
            Socket clntSock = serverSocket.accept();

            SocketAddress clientAddress = clntSock.getRemoteSocketAddress();
            log.info("Handling client at " + clientAddress);
            pool.submit(new Session(clntSock, new JsonProtocol(), userStore, messageStore));
        }
    }

}
