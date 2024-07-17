package v8.io.file.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.TimeUnit;

public class Hello {
    public static void main(String[] args) throws IOException, InterruptedException {
//        FileChannel open = FileChannel.open(Path.of("/Users/blackwhite/temp/niohello.txt"),StandardOpenOption.CREATE);
//        open.map(FileChannel.MapMode.PRIVATE,0,1111111111);


        RandomAccessFile file = new RandomAccessFile("/Users/blackwhite/temp/niohello.txt", "rw");
        FileChannel channel = file.getChannel(); // 获取一个可读写文件通道
        MappedByteBuffer map = channel.map(FileChannel.MapMode.PRIVATE, 0, 10);
        TimeUnit.SECONDS.sleep(1111111);
//        map.cl
    }
}
