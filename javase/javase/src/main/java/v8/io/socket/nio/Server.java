package v8.io.socket.nio;

import lombok.Data;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Data
public class Server {
    private int port = 8899;
    Selector selector = null;
    ServerSocketChannel serverSocketChannel = null;

    Set<SocketChannel> client = new HashSet<>();

    boolean start = false;

    public static void main(String[] args) {
        Server server = new Server();
        server.init();
        if (server.start) {
            server.handle();

            try {
                TimeUnit.SECONDS.sleep(1001100000);
            } catch (InterruptedException e) {
            }
        }

        server.stop();


    }

    private  void stop() {
        if (start) {
//            if()
        }
    }

    private  void handle() {
        while (true){
            try {
                int select = selector.select();

                if (select > 0) {
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();

                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey next = iterator.next();
                        handleEvent(next);
                        iterator.remove();
                    }

                }
            } catch (IOException e) {
              e.printStackTrace();
            }
        }
    }

    private void handleEvent(SelectionKey next) {
        if(next.isValid()){
            int i = next.readyOps();
            if ((i & SelectionKey.OP_ACCEPT) != 0) {
                dealConnect(next);
            }else  if ((i & SelectionKey.OP_READ) != 0) {
                dealRead(next);
            }else  if ((i & SelectionKey.OP_WRITE) != 0) { //写意义不大
//                dealConnect(next);
                System.out.println("可以写了");
            }else  if ((i & SelectionKey.OP_CONNECT) != 0) { //写意义不大
//                dealConnect(next);
                System.out.println("可以连接了");
            }
        }
    }

    private void dealRead(SelectionKey next) {
        SocketChannel socketChannel = (SocketChannel) next.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
        int read = 0;
        try {
            read = socketChannel.read(byteBuffer);
            if (read > 0) {
                byte[] content = new byte[read];
                byteBuffer.flip();
                byteBuffer.get(content);
                System.out.println(new String(content));
            }
             byteBuffer = ByteBuffer.allocateDirect(1024);
            byteBuffer.put("你好服客户段".getBytes());
            byteBuffer.flip();
            try {
                socketChannel.write(byteBuffer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            if (e instanceof SocketException) {
                next.cancel();

            }
            e.printStackTrace();
        }

    }

    private void dealConnect(SelectionKey next) {
        ServerSocketChannel serverSocketChannelTemp = (ServerSocketChannel) next.channel();
        try {
            SocketChannel accept = serverSocketChannelTemp.accept();
            accept.configureBlocking(false);
            accept.register(selector, SelectionKey.OP_READ);
            client.add(accept);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private  void init()  {

        try {
            selector = Selector.open();
            serverSocketChannel =  ServerSocketChannel.open();
           serverSocketChannel.bind(new InetSocketAddress(getPort()));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            start = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
