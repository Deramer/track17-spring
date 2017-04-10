package track.lections;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

public class TcpEchoServer {
    private static final int BUFSIZE = 32;

    private class Handler implements Runnable {
        Socket clntSock;

        Handler(Socket clntSock) {
            this.clntSock = clntSock;
        }

        public void run() {
            int recvMsgSize;
            byte[] recieveBuf = new byte[BUFSIZE];
            try {
                InputStream in = clntSock.getInputStream();
                OutputStream out = clntSock.getOutputStream();

                while ((recvMsgSize = in.read(recieveBuf)) != -1) {
                    out.write(recieveBuf, 0, recvMsgSize);
                }
                clntSock.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            throw new IllegalArgumentException("Parameter: <Port>");
        }

        int servPort = Integer.parseInt(args[0]);

        ServerSocket serverSocket = new ServerSocket(servPort);

        int recvMsgSize;
        byte[] recieveBuf = new byte[BUFSIZE];

        while (true) {
            Socket clntSock = serverSocket.accept();

            SocketAddress clientAddress = clntSock.getRemoteSocketAddress();
            System.out.println("Handling client at " + clientAddress);
            TcpEchoServer serv = new TcpEchoServer();
            (new Thread(serv.new Handler(clntSock))).start();
            /*
            InputStream in = clntSock.getInputStream();
            OutputStream out = clntSock.getOutputStream();

            while ((recvMsgSize = in.read(recieveBuf)) != -1) {
                out.write(recieveBuf, 0, recvMsgSize);
            }

            clntSock.close();
            */
        }
    }
}
