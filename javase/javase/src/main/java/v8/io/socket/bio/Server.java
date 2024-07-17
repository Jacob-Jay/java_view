package v8.io.socket.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start();
    }

    private  void start() throws IOException {
        ServerSocket socket = new ServerSocket(8899);
        while (true) {
            Socket accept = socket.accept();
            new Thread(()->{
                try {
                    handle(accept);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
    }

    private  void handle(Socket accept) throws Exception{
        InputStream inputStream = accept.getInputStream();
        OutputStream outputStream = accept.getOutputStream();

        while (true) {
            byte[] content = new byte[1024];

            int read = inputStream.read(content);
            if (read < 0) {
                break;
            }

            String s = new String(content, 0, read);
            System.out.println("收到客户端数据："+s);

            outputStream.write("已收到你的数据".getBytes());
            outputStream.flush();
        }

    }
}
