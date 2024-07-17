package normal.producer;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.List;

/**
 * 自己创建producer 同步的发送
 */
public class QueueSelect {
    public static void main(String[] args) throws MQBrokerException, RemotingException, InterruptedException, MQClientException {
        DefaultMQProducer producer = new DefaultMQProducer("sync");

        producer.setNamesrvAddr("localhost:9876");
        producer.start();
        Message message = new Message();
        message.setTopic("normal-way");
        message.setBody("hello  message".getBytes());

        SendResult send = producer.send(message, new MessageQueueSelector() {
            @Override
            public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                //arg 就是调用send方法传递的参数，根据具体的业务逻辑使用参数选择一个MessageQueue
                return null;
            }
        },"arg");
        String msgId = send.getMsgId();
        String offsetMsgId = send.getOffsetMsgId();
        SendStatus sendStatus = send.getSendStatus();
        System.out.println("msgId:   "+msgId+"     offsetMsgId    "+offsetMsgId);
    }
}
