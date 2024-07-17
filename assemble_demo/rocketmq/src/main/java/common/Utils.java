package common;

import org.apache.rocketmq.common.message.MessageDecoder;
import org.apache.rocketmq.common.message.MessageId;

import java.net.UnknownHostException;

public class Utils {
    public static void main(String[] args) {
        decodeMsgId();
    }


    /**
     * 发送消息返回的offsetMsgId可以获取broker的地址  与commitLog中的偏移量
     */
    private static void decodeMsgId() {
        try {
//            MessageId id = MessageDecoder.decodeMessageId("C0A8010800002A9F0000000000000000");
            MessageId id = MessageDecoder.decodeMessageId("FDB7FAB49E1CB5A11CE01A11F57A9ED6A1F5251A69D754C917B10000");
            System.out.println(id.getOffset());
            System.out.println(id.getAddress());
        } catch (UnknownHostException e) {

        }
    }
}
