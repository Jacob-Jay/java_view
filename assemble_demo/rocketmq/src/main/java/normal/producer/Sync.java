package normal.producer;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

/**
 * 自己创建producer 同步的发送
 */
public class Sync {
    public static void main(String[] args) throws MQBrokerException, RemotingException, InterruptedException, MQClientException {
        DefaultMQProducer producer = new DefaultMQProducer("sync");

        producer.setNamesrvAddr("localhost:9876");
        producer.start();
        Message message = new Message();
        message.setTopic("normal-way");
        message.setBody("hello  message".getBytes());

        SendResult send = producer.send(message);
        String msgId = send.getMsgId();
        String offsetMsgId = send.getOffsetMsgId();
        SendStatus sendStatus = send.getSendStatus();
        System.out.println(send.getMessageQueue().getBrokerName()+" "+send.getMessageQueue().getQueueId());
        System.out.println("msgId:   "+msgId+"     offsetMsgId    "+offsetMsgId);
    }
}
