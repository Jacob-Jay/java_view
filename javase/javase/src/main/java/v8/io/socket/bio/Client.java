package v8.io.socket.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class Client {
    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.start();
    }

    private void start() throws IOException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(8899));
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();

        int index = 0;
        while (true) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            String data = "客户端数据"+index++;
            outputStream.write(data.getBytes());
            outputStream.flush();



            byte[] content = new byte[1024];

            int read = inputStream.read(content);
            if (read < 0) {
                break;
            }

            String s = new String(content, 0, read);
            System.out.println("收到服务数据："+s);


        }
    }
}
