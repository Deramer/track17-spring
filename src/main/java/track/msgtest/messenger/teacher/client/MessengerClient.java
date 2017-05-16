package track.msgtest.messenger.teacher.client;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import track.msgtest.messenger.messages.*;
import track.msgtest.messenger.net.*;


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
                    int read = in.read(buf);
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
        switch (msg.getType()) {
            case MSG_STATUS:
                System.out.print("Status: ");
                System.out.println(((StatusMessage) msg).getStatus());
                System.out.println(((StatusMessage) msg).getText());
                break;
            case MSG_TEXT:
                System.out.print("Message from chat ");
                System.out.println(msg.getSenderId());
                System.out.println(((TextMessage)msg).getText());
                break;
            default:
                System.out.println("Not supported type of message");
                break;
        }
    }

    /**
     * Обрабатывает входящую строку, полученную с консоли
     * Формат строки можно посмотреть в вики проекта
     */
    public void processInput(String line) throws IOException, ProtocolException, UserErrorException {
        String[] tokens = line.split(" ", 2);
        log.info("Tokens: {}", Arrays.toString(tokens));
        String cmdType = tokens[0];
        switch (cmdType) {
            case "/login":
                tokens = line.split(" ", 3);
                if (tokens.length < 3) {
                    throw new UserErrorException("/login has less than 3 tokens.");
                }
                LoginMessage logMsg = new LoginMessage(tokens[1], tokens[2]);
                send(logMsg);
                break;
            case "/help":
                // TODO: реализация
                break;
            case "/text":
                tokens = line.split(" ", 3);
                if (tokens.length < 3) {
                    throw new UserErrorException("/text has less than 3 tokens.");
                }
                TextMessage sendMessage = new TextMessage();
                sendMessage.setType(Type.MSG_TEXT);
                try {
                    sendMessage.setSenderId(Long.parseLong(tokens[1]));
                } catch (NumberFormatException e) {
                    throw new UserErrorException("Chat id must be Long.");
                }
                sendMessage.setText(tokens[2]);
                send(sendMessage);
                break;
            case "/signup":
                tokens = line.split(" ", 3);
                if (tokens.length < 3) {
                    throw new UserErrorException("/signup has less than 3 tokens.");
                }
                SignUpMessage signUpMessage = new SignUpMessage();
                signUpMessage.setType(Type.MSG_SIGNUP);
                signUpMessage.setLogin(tokens[1]);
                signUpMessage.setPassword(tokens[2]);
                send(signUpMessage);
                break;
            default:
                log.error("Invalid input: " + line);
        }
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
                // TODO
//                client.close();
            }
        }
    }
}