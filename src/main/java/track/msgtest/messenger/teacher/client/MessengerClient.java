package track.msgtest.messenger.teacher.client;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import track.msgtest.messenger.messages.*;
import track.msgtest.messenger.net.*;
import track.msgtest.messenger.teacher.client.inputhandlers.InputController;
import track.msgtest.messenger.teacher.client.messagehandlers.MessagesController;


/**
 *
 */
public class MessengerClient {


    /**
     * Механизм логирования позволяет более гибко управлять записью данных в лог (консоль, файл и тд)
     * */
    private static Logger log = LoggerFactory.getLogger(MessengerClient.class);

    /**
     * Протокол, хост и порт инициализируются из конфига
     *
     * */
    private Protocol protocol;
    private int port;
    private String host;

    /**
     * С каждым сокетом связано 2 канала in/out
     */
    private InputStream in;
    private OutputStream out;

    private boolean isActive = true;

    private InputController inputController = new InputController();
    private MessagesController messagesController = new MessagesController();

    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public InputStream getIn() {
        return in;
    }

    public void initSocket() throws IOException {
        Socket socket = new Socket(host, port);
        in = socket.getInputStream();
        out = socket.getOutputStream();

        /*
         Тред "слушает" сокет на наличие входящих сообщений от сервера
        */
        Thread socketListenerThread = new Thread(() -> {
            final byte[] buf = new byte[1024 * 64];
            log.info("Starting listener thread...");
            while (!Thread.currentThread().isInterrupted() && isActive == true) {
                try {
                    // Здесь поток блокируется на ожидании данных
                    int read;
                    try {
                        read = in.read(buf);
                    } catch (SocketException e) {
                        return;
                    }
                    if (read > 0) {

                        // По сети передается поток байт, его нужно раскодировать с помощью протокола
                        Message msg = protocol.decode(Arrays.copyOf(buf, read));
                        onMessage(msg);
                    } else {
                        log.info("End of stream.");
                        isActive = false;
                        Thread.currentThread().interrupt();
                    }
                } catch (Exception e) {
                    log.error("Failed to process connection: {}", e);
                    isActive = false;
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        });

        socketListenerThread.start();
    }

    /**
     * Реагируем на входящее сообщение
     */
    public void onMessage(Message msg) {
        log.debug("Message received: {}", msg);
        messagesController.handler(msg.getType()).handle(msg);
    }

    /**
     * Обрабатывает входящую строку, полученную с консоли
     * Формат строки можно посмотреть в вики проекта
     */
    public void processInput(String line) throws IOException, ProtocolException, UserErrorException {
        String[] tokens = line.split(" ", 2);
        log.debug("Tokens: {}", Arrays.toString(tokens));
        String cmdType = tokens[0];
        inputController.handler(cmdType).handle(this, line);
    }

    /**
     * Отправка сообщения в сокет клиент -> сервер
     */
    public void send(Message msg) throws IOException, ProtocolException {
        log.info(msg.toString());
        out.write(protocol.encode(msg));
        out.flush(); // принудительно проталкиваем буфер с данными
    }

    public static void main(String[] args) throws Exception {

        MessengerClient client = new MessengerClient();
        client.setHost("localhost");
        client.setPort(19000);
        client.setProtocol(new JsonProtocol());

        try {
            client.initSocket();

            // Цикл чтения с консоли
            Scanner scanner = new Scanner(System.in);
            System.out.println("$");
            while (client.getIsActive()) {
                String input = scanner.nextLine();
                if ("q".equals(input)) {
                    client.getIn().close();
                    client.setIsActive(false);
                    return;
                }
                try {
                    client.processInput(input);
                } catch (ProtocolException | IOException e) {
                    log.error("Failed to process user input", e);
                } catch (UserErrorException e) {
                    log.info("Invalid input, {}", e.getMessage());
                }
            }
        } catch (Exception e) {
            log.error("Application failed.", e);
        } finally {
            if (client != null) {
                client.close();
            }
        }
    }

    public void close() {
        try {
            in.close();
            out.close();
        } catch (IOException e) {
            log.warn("Error during close()", e);
        }
    }
}