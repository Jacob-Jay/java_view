package v8.io.socket.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Client {
    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }

    private void start() {


        try {
            Selector open = Selector.open();
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.register(open, SelectionKey.OP_READ|SelectionKey.OP_WRITE|SelectionKey.OP_CONNECT);
            socketChannel.connect(new InetSocketAddress(8899));
            handle(open);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private  void handle( Selector selector) {
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
//            System.out.println("readalble");
            int i = next.readyOps();

            if ((i & SelectionKey.OP_ACCEPT) != 0) {

            }else  if ((i & SelectionKey.OP_READ) != 0) {
                dealRead(next);
            }else  if ((i & SelectionKey.OP_WRITE) != 0) { //写意义不大
//                dealConnect(next);
                System.out.println("可以写了");
                next.interestOps(next.interestOps() & (~SelectionKey.OP_WRITE));
            }else  if ((i & SelectionKey.OP_CONNECT) != 0) { //写意义不大
//                dealConnect(next);

                System.out.println("可以连接了");
                next.interestOps(next.interestOps() & (~SelectionKey.OP_CONNECT));
                SocketChannel socketChannel = (SocketChannel) next.channel();

                try {
                    boolean b = socketChannel.finishConnect();

                    System.out.println(b);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
                byteBuffer.put("你好服务段".getBytes());
                byteBuffer.flip();
                try {
                    socketChannel.write(byteBuffer);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void dealRead(SelectionKey next) {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
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
            byteBuffer.clear();
            byteBuffer.put("你好客户端".getBytes());
            byteBuffer.flip();
            socketChannel.write(byteBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
